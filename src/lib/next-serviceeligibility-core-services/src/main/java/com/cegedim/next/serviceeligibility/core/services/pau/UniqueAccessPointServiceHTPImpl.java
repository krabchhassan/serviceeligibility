package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperPaymentRecipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractHTPForPau;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.UniqueAccessPointUtil;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Contact;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.DigitRelation;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Nir;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public abstract class UniqueAccessPointServiceHTPImpl implements UniqueAccessPointService {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(UniqueAccessPointServiceHTPImpl.class);

  @Autowired OcService ocService;

  @Autowired MapperPaymentRecipient mapperPaymentRecipient;

  protected abstract void triHTP(List<ContratAIV6> contrats, UniqueAccessPointRequest requete);

  public UniqueAccessPointResponse execute(final UniqueAccessPointRequest requete) {
    final UniqueAccessPointResponse response = new UniqueAccessPointResponse();

    UniqueAccessPointUtil.formatDatesForRequest(requete);

    LOGGER.debug(LOG_BENEFICIAIRE_DEBUT, requete.getContext());
    final List<BenefAIV5> benefs = this.findBenefFromRequest(requete);
    if (CollectionUtils.isEmpty(benefs)) {
      throw new UAPFunctionalException(
          "Bénéficiaire non trouvé",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND);
    }

    final List<String> numerosPersonnes = new ArrayList<>();
    for (final BenefAIV5 benef : benefs) {
      numerosPersonnes.add(benef.getIdentite().getNumeroPersonne());
      LOGGER.debug(
          LOG_BENEFICIAIRE_RESULTAT, benef.getId(), benef.getIdentite().getNumeroPersonne());
    }
    List<GenericRightDto> results = new ArrayList<>();

    final List<ContractHTPForPau> contrats = this.getContractsHTPForPau(requete, benefs);
    if (CollectionUtils.isNotEmpty(contrats)) {
      boolean force =
          contrats.stream().anyMatch(contractHTPForPau -> contractHTPForPau.getRetour() > 0);
      LOGGER.debug(LOG_CONTRAT_DEBUT, requete.getContext());
      List<ContratAIV6> contratAIV6s = getContratAIV6s(contrats);
      LOGGER.debug(LOG_CONTRAT_TRI_DEBUT, requete.getContext());
      this.triHTP(contratAIV6s, requete);
      for (final ContratAIV6 contrat : contratAIV6s) {
        this.addMappedResult(requete, results, contrat, numerosPersonnes, force);
      }
    }

    results =
        results.stream()
            .filter(right -> (!CollectionUtils.isEmpty(right.getInsured().getRights())))
            .toList();

    if (results.isEmpty()) {
      throw new UAPFunctionalException(
          "Contrat non trouvé",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_SERVICE_PRESTATION_NOT_FOUND);
    }
    LOGGER.debug(LOG_CONTRAT_RESTITUTION_DEBUT, requete.getContext());
    response.setContracts(results);
    return response;
  }

  protected abstract List<BenefAIV5> findBenefFromRequest(UniqueAccessPointRequest requete);

  public static List<ContratAIV6> getContratAIV6s(List<ContractHTPForPau> contrats) {
    // on prend ceux qui sont dans les clous
    List<ContractHTPForPau> contractHTPForPaus =
        contrats.stream()
            .filter(contractHTPForPau -> contractHTPForPau.getRetour() == 0)
            .collect(Collectors.toList());
    List<ContratAIV6> contratAIV6s = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(contractHTPForPaus)) {
      contratAIV6s =
          contractHTPForPaus.stream()
              .map(ContractHTPForPau::getContratAIV6)
              .collect(Collectors.toList());
    } else {
      // on regarde après ce qu'il y a après
      contractHTPForPaus =
          contrats.stream()
              .filter(contractHTPForPau -> contractHTPForPau.getRetour() == 1)
              .collect(Collectors.toList());
      // prendre la plus proche de la fin
      if (CollectionUtils.isNotEmpty(contractHTPForPaus)) {
        // on filtre tous les contrats qui ont la date début la plus petite -> la plus
        // proche de la fin de la requête
        String dateDebutMin =
            contractHTPForPaus.stream()
                .map(contractHTPForPau -> contractHTPForPau.getPeriode().getDebut())
                .min(String::compareTo)
                .orElse(null);
        contractHTPForPaus =
            contractHTPForPaus.stream()
                .filter(
                    contractHTPForPau ->
                        contractHTPForPau.getPeriode().getDebut().equals(dateDebutMin))
                .collect(Collectors.toList());
        contratAIV6s =
            contractHTPForPaus.stream()
                .map(ContractHTPForPau::getContratAIV6)
                .collect(Collectors.toList());
      } else {
        // on regarde après ce qu'il y a avant
        contractHTPForPaus =
            contrats.stream()
                .filter(contractHTPForPau -> contractHTPForPau.getRetour() == 2)
                .collect(Collectors.toList());
        // prendre la plus proche du début
        if (CollectionUtils.isNotEmpty(contractHTPForPaus)) {
          // on filtre tous les contrats qui ont la date de fin la plus grande -> la plus
          // proche de la debut de la requête
          String dateFinMax =
              contractHTPForPaus.stream()
                  .map(contractHTPForPau -> contractHTPForPau.getPeriode().getFin())
                  .max(String::compareTo)
                  .orElse(null);
          contractHTPForPaus =
              contractHTPForPaus.stream()
                  .filter(
                      contractHTPForPau ->
                          contractHTPForPau.getPeriode().getFin() == null
                              || contractHTPForPau.getPeriode().getFin().equals(dateFinMax))
                  .collect(Collectors.toList());
          contratAIV6s =
              contractHTPForPaus.stream()
                  .map(ContractHTPForPau::getContratAIV6)
                  .collect(Collectors.toList());
        }
      }
    }
    return contratAIV6s;
  }

  protected abstract List<ContractHTPForPau> getContractsHTPForPau(
      UniqueAccessPointRequest requete, List<BenefAIV5> benefs);

  // For HTP
  public void addMappedResult(
      final UniqueAccessPointRequest requete,
      final List<GenericRightDto> results,
      final ContratAIV6 contrat,
      final List<String> numerosPersonnes,
      boolean force) {
    final List<GenericRightDto> result =
        this.mapContratToGenericRights(contrat, requete, numerosPersonnes, force);
    if (CollectionUtils.isNotEmpty(result)) {
      results.addAll(result);
    }
  }

  public List<GenericRightDto> mapContratToGenericRights(
      final ContratAIV6 contract,
      final UniqueAccessPointRequest requete,
      final List<String> numerosPersonnes,
      boolean force) {
    return this.mapContratToGenericRights(contract, requete, numerosPersonnes, true, force);
  }

  public List<GenericRightDto> mapContratToGenericRights(
      final ContratAIV6 contract,
      final UniqueAccessPointRequest requete,
      final List<String> numerosPersonnes,
      boolean withRights,
      boolean force) {
    final List<GenericRightDto> genericRightDtos = new ArrayList<>();
    // il y en a forcément un
    for (final com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure benef :
        contract.getAssures()) {
      final IdentiteContrat identite = benef.getIdentite();
      if (numerosPersonnes.contains(identite.getNumeroPersonne())) {
        genericRightDtos.add(
            this.mapContratToGenericRightByBenef(contract, requete, benef, withRights, force));
      }
    }

    return genericRightDtos;
  }

  private GenericRightDto mapContratToGenericRightByBenef(
      final ContratAIV6 contract,
      final UniqueAccessPointRequest requete,
      final Assure assure,
      boolean withRights,
      boolean force) {
    final GenericRightDto genericRights = new GenericRightDto();
    genericRights.setContext(ContextConstants.HTP);

    final String idDeclarant = contract.getIdDeclarant();
    genericRights.setInsurerId(idDeclarant);

    try {
      final String codeOc = this.ocService.getOC(idDeclarant).getCode();
      genericRights.setOcCode(codeOc);
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new RequestValidationException(
          "Erreur lors de l’appel à un service externe - Impossible de déterminer l’Organisation liée à l’Organisme Complémentaire identifié par le numéro d’AMC "
              + idDeclarant,
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CALL_OC_OR_PW);
    }

    genericRights.setNumber(contract.getNumero());
    genericRights.setExternalNumber(contract.getNumeroExterne());
    genericRights.setSubscriberId(contract.getNumeroAdherent());
    genericRights.setSubscriberFullId(contract.getNumeroAdherentComplet());
    genericRights.setSubscriptionDate(DateUtils.formatDate(contract.getDateSouscription()));
    genericRights.setTerminationDate(DateUtils.formatDate(contract.getDateResiliation()));
    genericRights.setBusinessContributor(contract.getApporteurAffaire());
    genericRights.setResponsibleContractOpenPeriods(
        this.mapListPeriod(contract.getPeriodesContratResponsableOuvert()));
    genericRights.setCmuContractOpenPeriods(
        this.mapContratCMU(contract.getPeriodesContratCMUOuvert()));

    genericRights.setDetailedSecondaryCriterion(contract.getCritereSecondaireDetaille());
    genericRights.setSecondaryCriterion(contract.getCritereSecondaire());
    genericRights.setIsIndividualContract(contract.getIsContratIndividuel());
    genericRights.setOperator(contract.getGestionnaire());
    genericRights.setCollectiveContract(this.mapContratCollectif(contract.getContratCollectif()));
    genericRights.setQualification(contract.getQualification());
    genericRights.setPrioritizationOrder(contract.getOrdrePriorisation());

    genericRights.setSuspensionPeriods(
        this.mapPeriodeSuspension(
            contract.getPeriodesSuspension(), requete.getStartDate(), requete.getEndDate()));
    genericRights.setInsured(
        this.mapAssure(
            assure, idDeclarant, requete, withRights, contract.getDateResiliation(), force));
    genericRights.setIsForced(force);

    return genericRights;
  }

  private List<Period> mapListPeriod(final List<Periode> periodeList) {
    final List<Period> periodList = new ArrayList<>();
    if (periodeList != null) {
      for (final Periode periode : periodeList) {
        periodList.add(this.mapPeriod(periode));
      }
    }
    return periodList;
  }

  private List<CmuContractOpenPeriod> mapContratCMU(
      final List<PeriodeContratCMUOuvert> periodeCmuList) {
    final List<CmuContractOpenPeriod> cmuContractOpenPeriodList = new ArrayList<>();
    if (periodeCmuList != null) {
      for (final PeriodeContratCMUOuvert periodeContratCMUOuvert : periodeCmuList) {
        final CmuContractOpenPeriod cmuContractOpenPeriod = new CmuContractOpenPeriod();
        cmuContractOpenPeriod.setPeriod(this.mapPeriod(periodeContratCMUOuvert.getPeriode()));
        cmuContractOpenPeriod.setCode(periodeContratCMUOuvert.getCode());
        cmuContractOpenPeriodList.add(cmuContractOpenPeriod);
      }
    }
    return cmuContractOpenPeriodList;
  }

  protected CollectiveContractV5 mapContratCollectif(final ContratCollectif contratCollectif) {
    final CollectiveContractV5 collectiveContract = new CollectiveContractV5();
    if (contratCollectif != null) {
      collectiveContract.setNumber(contratCollectif.getNumero());
      collectiveContract.setExternalNumber(contratCollectif.getNumeroExterne());
    }
    return collectiveContract;
  }

  private List<SuspensionPeriod> mapPeriodeSuspension(
      final List<PeriodeSuspension> periodeSuspensionsList, String startDate, String endDate) {
    final List<SuspensionPeriod> suspensionPeriodList = new ArrayList<>();
    if (periodeSuspensionsList != null) {
      for (final PeriodeSuspension periodeSuspension : periodeSuspensionsList) {
        if (DateUtils.isOverlapping(
            startDate,
            endDate,
            periodeSuspension.getPeriode().getDebut(),
            periodeSuspension.getPeriode().getFin())) {
          final SuspensionPeriod suspensionPeriod = new SuspensionPeriod();
          suspensionPeriod.setPeriod(this.mapPeriod(periodeSuspension.getPeriode()));
          suspensionPeriod.setSuspensionType(periodeSuspension.getTypeSuspension());
          suspensionPeriod.setSuspensionReason(periodeSuspension.getMotifSuspension());
          suspensionPeriod.setSuspensionRemovalReason(periodeSuspension.getMotifLeveeSuspension());
          suspensionPeriodList.add(suspensionPeriod);
        }
      }
    }
    return suspensionPeriodList;
  }

  private Period mapPeriod(final Periode periode) {
    final Period period = new Period();
    if (periode != null) {
      period.setStart(periode.getDebut());
      period.setEnd(periode.getFin());
    }
    return period;
  }

  private Insured mapAssure(
      final com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure assure,
      final String idDeclarant,
      final UniqueAccessPointRequest requete,
      boolean withRights,
      String dateResiliation,
      boolean force) {
    final Insured insured = new Insured();
    insured.setIsSubscriber(assure.getIsSouscripteur());
    insured.setAdministrativeRank(assure.getRangAdministratif());
    insured.setIdentity(this.mapIdentity(assure.getIdentite(), idDeclarant));
    insured.setData(this.mapData(assure.getData()));
    insured.setHealthMutualSubscriptionDate(assure.getDateAdhesionMutuelle());
    insured.setIndividualSubscriptionStartDate(assure.getDateDebutAdhesionIndividuelle());
    insured.setIndividualSubscriptionNumber(assure.getNumeroAdhesionIndividuelle());
    insured.setCancellationDate(DateUtils.formatDate(assure.getDateRadiation()));
    insured.setDigitRelation(this.mapDigitRelation(assure.getDigitRelation()));
    insured.setAttendingPhysicianOpenedPeriods(
        this.mapListPeriod(assure.getPeriodesMedecinTraitantOuvert()));
    insured.setSpecialPlans(this.mapSpecialPlans(assure.getRegimesParticuliers()));
    insured.setSpecialStatuses(this.mapSpecialStatuses(assure.getSituationsParticulieres()));
    final Quality quality = new Quality();
    quality.setCode(assure.getQualite().getCode());
    quality.setLabel(assure.getQualite().getLibelle());
    insured.setQuality(quality);
    Periode periode = new Periode(requete.getStartDate(), requete.getEndDate());
    if (force) {
      periode = new Periode(null, null);
    }
    String dateFin = dateResiliation;
    if (dateFin != null && assure.getDateRadiation() != null) {
      dateFin = DateUtils.getMinDate(dateFin, assure.getDateRadiation(), DateUtils.FORMATTER);
    }
    if (withRights) {
      insured.setRights(this.mapRights(assure.getDroits(), requete, periode, dateFin, force));
    }

    return insured;
  }

  private List<Right> mapRights(
      final List<DroitAssure> droitAssureList,
      final UniqueAccessPointRequest requete,
      Periode periode,
      String dateFin,
      boolean force) {
    final List<Right> rightList = new ArrayList<>();
    if (droitAssureList != null) {
      for (final DroitAssure droitAssure : droitAssureList) {
        try {
          String minDate;
          String maxDate;
          if (force) {
            minDate = droitAssure.getPeriode().getDebut();
            maxDate =
                getMaxDate(
                    null, dateFin, droitAssure); // la date de fin de requete est à null parce
            // que l'on a forcé !
          } else {
            minDate =
                DateUtils.getMaxDate(
                    droitAssure.getPeriode().getDebut(),
                    requete.getStartDate(),
                    DateUtils.FORMATTER);
            maxDate = getMaxDate(requete.getEndDate(), dateFin, droitAssure);
          }
          rightList.addAll(
              this.getRightList(requete, droitAssure, periode, minDate, maxDate, force));
        } catch (final CarenceException | TriggerWarningException e) {
          LOGGER.error(e.getMessage(), e);
          throw new RequestValidationException(
              "Erreur lors de l’appel à la récupération du paramétrage des carences",
              HttpStatus.BAD_REQUEST,
              RestErrorConstants.ERROR_CALL_OC_OR_PW);
        } catch (BobbNotFoundException e) {
          LOGGER.error(e.getMessage(), e);
          throw new RequestValidationException(
              "Erreur lors de l’appel à bob",
              HttpStatus.BAD_REQUEST,
              RestErrorConstants.ERROR_CODE_BOBB_NOT_FOUND_EXCEPTION);
        } catch (PwException e) {
          LOGGER.error(e.getMessage(), e);
          throw new RequestValidationException(
              "Erreur lors de l’appel au product workshop",
              HttpStatus.BAD_REQUEST,
              RestErrorConstants.ERROR_CALL_OC_OR_PW);
        }
      }
    }

    this.sortRights(rightList);
    return rightList;
  }

  static String getMaxDate(String requestEndDate, String dateFin, DroitAssure droitAssure) {
    String maxDate = null;
    if (requestEndDate != null) {
      if (droitAssure.getPeriode().getFin() != null) {
        maxDate =
            DateUtils.getMinDate(
                droitAssure.getPeriode().getFin(), requestEndDate, DateUtils.FORMATTER);
        if (dateFin != null) {
          maxDate = DateUtils.getMinDate(maxDate, dateFin, DateUtils.FORMATTER);
        }
      } else {
        if (dateFin != null) {
          maxDate = DateUtils.getMinDate(requestEndDate, dateFin, DateUtils.FORMATTER);
        } else {
          maxDate = requestEndDate;
        }
      }
    } else {
      if (droitAssure.getPeriode().getFin() != null) {
        maxDate = droitAssure.getPeriode().getFin();
      }
      if (dateFin != null) {
        maxDate = DateUtils.getMinDate(maxDate, dateFin, DateUtils.FORMATTER);
      }
    }
    return maxDate;
  }

  protected abstract List<Right> getRightList(
      final UniqueAccessPointRequest requete,
      final DroitAssure droitAssure,
      final Periode periode,
      final String minDate,
      final String maxDate,
      final boolean force)
      throws CarenceException, BobbNotFoundException, PwException, TriggerWarningException;

  private DigitRelation mapDigitRelation(
      final com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DigitRelation
          digitRelation) {
    final DigitRelation newDigitRelation = new DigitRelation();
    final Dematerialization dematerialization = new Dematerialization();
    newDigitRelation.setDematerialization(dematerialization);
    if (digitRelation != null) {
      if (digitRelation.getDematerialisation() != null) {
        dematerialization.setIsDematerialized(
            digitRelation.getDematerialisation().getIsDematerialise());
        dematerialization.setEmail(digitRelation.getDematerialisation().getEmail());
        dematerialization.setMobile(digitRelation.getDematerialisation().getMobile());
      }
      newDigitRelation.setRemoteTransmissions(
          this.mapTransmission(digitRelation.getTeletransmissions()));
    }
    return newDigitRelation;
  }

  private List<SpecialPlan> mapSpecialPlans(final List<CodePeriode> regimesParticuliers) {
    final List<SpecialPlan> specialPlanList = new ArrayList<>();
    if (regimesParticuliers != null) {
      for (final CodePeriode codePeriode : regimesParticuliers) {
        final SpecialPlan specialPlan = new SpecialPlan();
        specialPlan.setCode(codePeriode.getCode());
        specialPlan.setPeriod(this.mapPeriod(codePeriode.getPeriode()));
        specialPlanList.add(specialPlan);
      }
    }
    return specialPlanList;
  }

  private List<SpecialStatus> mapSpecialStatuses(final List<CodePeriode> regimesParticuliers) {
    final List<SpecialStatus> specialStatusesList = new ArrayList<>();
    if (regimesParticuliers != null) {
      for (final CodePeriode codePeriode : regimesParticuliers) {
        final SpecialStatus specialStatuses = new SpecialStatus();
        specialStatuses.setCode(codePeriode.getCode());
        specialStatuses.setPeriod(this.mapPeriod(codePeriode.getPeriode()));
        specialStatusesList.add(specialStatuses);
      }
    }
    return specialStatusesList;
  }

  private List<RemoteTransmission> mapTransmission(
      final List<Teletransmission> teletransmissionList) {
    final List<RemoteTransmission> remoteTransmissionsList = new ArrayList<>();
    if (teletransmissionList != null) {
      for (final Teletransmission teletransmission : teletransmissionList) {
        final RemoteTransmission remoteTransmission = new RemoteTransmission();
        remoteTransmission.setPeriod(this.mapPeriod(teletransmission.getPeriode()));
        remoteTransmission.setIsRemotelyTransmitted(teletransmission.getIsTeletransmission());
        remoteTransmissionsList.add(remoteTransmission);
      }
    }
    return remoteTransmissionsList;
  }

  private Identity mapIdentity(final IdentiteContrat identite, final String idDeclarant) {
    final Identity identity = new Identity();
    identity.setNir(this.mapNir(identite.getNir()));
    identity.setAffiliationsRO(this.mapAffiliationRO(identite.getAffiliationsRO()));
    identity.setBirthDate(identite.getDateNaissance());
    identity.setBirthRank(identite.getRangNaissance());
    identity.setPersonNumber(idDeclarant + "-" + identite.getNumeroPersonne());
    identity.setPersonExternalRef(identite.getRefExternePersonne());

    return identity;
  }

  private Nir mapNir(final com.cegedim.next.serviceeligibility.core.model.kafka.Nir nir) {
    final Nir newNir = new Nir();
    if (nir != null) {
      newNir.setCode(nir.getCode());
      newNir.setKey(nir.getCle());
    }
    return newNir;
  }

  private List<AffiliationRO> mapAffiliationRO(final List<NirRattachementRO> nirRattachementList) {
    final List<AffiliationRO> affiliationROList = new ArrayList<>();
    if (nirRattachementList != null) {
      for (final NirRattachementRO nirRattachementRO : nirRattachementList) {
        final AffiliationRO affiliationRO = new AffiliationRO();
        affiliationRO.setNir(this.mapNir(nirRattachementRO.getNir()));
        RattachementRO rattachementRO = nirRattachementRO.getRattachementRO();
        if (rattachementRO != null) {
          final AttachementRO attachementRO = new AttachementRO();
          attachementRO.setRegimeCode(rattachementRO.getCodeRegime());
          attachementRO.setHealthInsuranceCompanyCode(rattachementRO.getCodeCaisse());
          attachementRO.setCenterCode(rattachementRO.getCodeCentre());
          affiliationRO.setAttachementRO(attachementRO);
        }
        affiliationRO.setPeriod(this.mapPeriod(nirRattachementRO.getPeriode()));
        affiliationROList.add(affiliationRO);
      }
    }
    return affiliationROList;
  }

  private InsuredData mapData(final DataAssure data) {
    final InsuredData insuredData = new InsuredData();
    insuredData.setName(this.mapName(data.getNom()));
    insuredData.setAddress(this.mapAdress(data.getAdresse()));
    insuredData.setContact(this.mapContact(data.getContact()));
    insuredData.setPaymentRecipients(this.mapPaymentRecipients(data.getDestinatairesPaiements()));
    insuredData.setBenefitStatementRecipients(
        this.mapDestRelevePrest(data.getDestinatairesRelevePrestations()));

    return insuredData;
  }

  private Name mapName(final NomAssure nom) {
    final Name name = new Name();
    if (nom != null) {
      name.setFirstName(nom.getPrenom());
      name.setLastName(nom.getNomFamille());
      name.setCommonName(nom.getNomUsage());
      name.setCivility(nom.getCivilite());
    }
    return name;
  }

  private Address mapAdress(final AdresseAssure adresse) {
    final Address address = new Address();
    if (adresse != null) {
      address.setLine1(adresse.getLigne1());
      address.setLine2(adresse.getLigne2());
      address.setLine3(adresse.getLigne3());
      address.setLine4(adresse.getLigne4());
      address.setLine5(adresse.getLigne5());
      address.setLine6(adresse.getLigne6());
      address.setLine7(adresse.getLigne7());
      address.setPostcode(adresse.getCodePostal());
    }
    return address;
  }

  private Contact mapContact(
      final com.cegedim.next.serviceeligibility.core.model.kafka.Contact contact) {
    final Contact newContact = new Contact();
    if (contact != null) {
      newContact.setLandline(contact.getFixe());
      newContact.setMobile(contact.getMobile());
      newContact.setEmail(contact.getEmail());
    }
    return newContact;
  }

  private List<BenefitStatementRecipient> mapDestRelevePrest(
      final List<DestinataireRelevePrestations> destinataireReleve) {
    final List<BenefitStatementRecipient> benefitStatementRecipientList = new ArrayList<>();
    if (destinataireReleve != null) {
      for (final DestinataireRelevePrestations destinataire : destinataireReleve) {
        final BenefitStatementRecipient benefitStatementRecipient = new BenefitStatementRecipient();
        benefitStatementRecipient.setBenefitStatementRecipientId(
            destinataire.getIdDestinataireRelevePrestations());
        benefitStatementRecipient.setBeyondBenefitStatementRecipientId(
            destinataire.getIdBeyondDestinataireRelevePrestations());
        benefitStatementRecipient.setName(
            mapperPaymentRecipient.mapNameCorporate(destinataire.getNom()));
        benefitStatementRecipient.setAddress(this.mapAdress(destinataire.getAdresse()));
        benefitStatementRecipient.setPeriod(
            mapperPaymentRecipient.mapPeriod(destinataire.getPeriode()));
        benefitStatementRecipientList.add(benefitStatementRecipient);
      }
    }
    return benefitStatementRecipientList;
  }

  private List<PaymentRecipient> mapPaymentRecipients(
      final List<DestinatairePrestations> destinatairePrestations) {
    final List<PaymentRecipient> paymentRecipientList = new ArrayList<>();
    if (destinatairePrestations != null) {
      for (final DestinatairePrestations destinatairePrestation : destinatairePrestations) {
        final PaymentRecipient paymentRecipient = new PaymentRecipient();
        paymentRecipient.setBeyondPaymentRecipientId(
            destinatairePrestation.getIdBeyondDestinatairePaiements());
        paymentRecipient.setPaymentRecipientId(destinatairePrestation.getIdDestinatairePaiements());
        paymentRecipient.setName(
            mapperPaymentRecipient.mapNameCorporate(destinatairePrestation.getNom()));
        paymentRecipient.setAddress(
            mapperPaymentRecipient.mapAdress(destinatairePrestation.getAdresse()));
        final Rib rib = new Rib();
        if (destinatairePrestation.getRib() != null) {
          rib.setBic(destinatairePrestation.getRib().getBic());
          rib.setIban(destinatairePrestation.getRib().getIban());
        }
        paymentRecipient.setRib(rib);
        paymentRecipient.setBenefitPaymentMode(
            mapperPaymentRecipient.mapPayment(destinatairePrestation.getModePaiementPrestations()));
        paymentRecipient.setPeriod(
            mapperPaymentRecipient.mapPeriod(destinatairePrestation.getPeriode()));
        paymentRecipientList.add(paymentRecipient);
      }
    }
    return paymentRecipientList;
  }

  public void sortRights(final List<Right> rights) {
    final Comparator<Right> comparator = this::orderRights;
    rights.sort(comparator);
  }

  private int orderRights(final Right r1, final Right r2) {
    final int prioritizationOrder =
        StringUtils.compare(r1.getPrioritizationOrder(), r2.getPrioritizationOrder());
    if (prioritizationOrder != 0) {
      return prioritizationOrder;
    } else {
      return r1.getCode().compareToIgnoreCase(r2.getCode());
    }
  }
}
