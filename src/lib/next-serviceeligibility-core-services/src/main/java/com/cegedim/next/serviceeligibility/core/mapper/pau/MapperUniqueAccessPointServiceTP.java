package com.cegedim.next.serviceeligibility.core.mapper.pau;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;
import static java.util.stream.Collectors.groupingBy;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirRattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.RattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeSuspensionContract;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapperUniqueAccessPointServiceTP {

  private final OcService ocService;

  private final MapperUAPRightTDB mapperUAPRightTDB;

  private final MapperUAPRightEvent mapperUAPRightEvent;

  private final BeyondPropertiesService beyondPropertiesService;

  public void addMappedResult(
      final UniqueAccessPointRequest requete,
      final List<GenericRightDto> results,
      final ContractTP contrat,
      final boolean isTpOffline,
      final String idDeclarant,
      final List<String> numeroPersonnes,
      final boolean foundContractsWithForce)
      throws PwException {
    final List<GenericRightDto> result =
        this.mapContratToGenericRightV4(
            contrat,
            isTpOffline,
            requete,
            this.mapperUAPRightTDB.getOc(idDeclarant),
            numeroPersonnes,
            foundContractsWithForce);
    if (CollectionUtils.isNotEmpty(result)) {
      results.addAll(result);
      manageDomains(result, isTpOffline);
    }
  }

  List<GenericRightDto> mapContratToGenericRightV4(
      final ContractTP contractTP,
      final boolean isTpOffline,
      final UniqueAccessPointRequest requete,
      final Oc oc,
      final List<String> numeroPersonnes,
      final boolean foundContractsWithForce) {
    final List<GenericRightDto> genericRightDtos = new ArrayList<>();
    // si les benefs du contrat ont été retourné de la recherche des benefs, alors
    // on crée un contrat par benef
    for (final BeneficiaireContractTP ben : contractTP.getBeneficiaires()) {
      if (numeroPersonnes.contains(ben.getNumeroPersonne())) {
        genericRightDtos.add(
            this.mapContratToGenericRightV4ByBenef(
                contractTP, isTpOffline, requete, oc, ben, foundContractsWithForce));
      }
    }
    return genericRightDtos;
  }

  private GenericRightDto mapContratToGenericRightV4ByBenef(
      final ContractTP contractTP,
      final boolean isTpOffline,
      final UniqueAccessPointRequest requete,
      final Oc oc,
      final BeneficiaireContractTP benef,
      final boolean foundContractsWithForce) {
    final GenericRightDto genericRights = new GenericRightDto();
    genericRights.setContext(
        isTpOffline ? ContextConstants.TP_OFFLINE : ContextConstants.TP_ONLINE);

    genericRights.setInsurerId(contractTP.getIdDeclarant());
    genericRights.setIssuingCompanyCode(contractTP.getGestionnaire());
    genericRights.setOcCode(oc.getCode());

    genericRights.setNumber(contractTP.getNumeroContrat());
    genericRights.setExternalNumber(contractTP.getNumeroExterneContratIndividuel());
    genericRights.setSubscriberId(contractTP.getNumeroAdherent());
    genericRights.setSubscriberFullId(contractTP.getNumeroAdherentComplet());
    genericRights.setSubscriptionDate(DateUtils.formatDate(contractTP.getDateSouscription()));
    genericRights.setTerminationDate(DateUtils.formatDate(contractTP.getDateResiliation()));

    genericRights.setDetailedSecondaryCriterion(contractTP.getCritereSecondaireDetaille());
    genericRights.setSecondaryCriterion(contractTP.getCritereSecondaire());
    genericRights.setIsIndividualContract("1".equals(contractTP.getIndividuelOuCollectif()));
    genericRights.setOperator(contractTP.getGestionnaire());
    this.manageCollectiveContract(contractTP, genericRights);
    genericRights.setQualification(contractTP.getQualification());
    genericRights.setPrioritizationOrder(contractTP.getOrdrePriorisation());
    genericRights.setItelisCode(contractTP.getCodeItelis());
    genericRights.setIsForced(foundContractsWithForce);

    final List<Right> rights;
    final Period minMaxPeriod = new Period();
    boolean isExclusiviteCarteDematerialise =
        Util.isExcluDemat(contractTP.getCarteTPaEditerOuDigitale());
    if (Constants.ORIGINE_DECLARATIONEVT.equals(contractTP.getOrigineDeclaration())) {
      rights =
          this.mapperUAPRightEvent.manageRightDomains(
              benef,
              requete,
              isTpOffline,
              oc,
              contractTP.getDateRestitution(),
              isExclusiviteCarteDematerialise,
              foundContractsWithForce);
    } else {
      rights =
          this.mapperUAPRightTDB.manageRightDomains(
              benef,
              requete,
              isTpOffline,
              oc,
              contractTP.getDateRestitution(),
              isExclusiviteCarteDematerialise,
              foundContractsWithForce);
    }

    final Insured insured = this.getInsuredV4(contractTP, benef, rights, isTpOffline);

    this.manageInsuredData(benef, insured);

    final List<Period> periods = new ArrayList<>();
    String startDate = DateUtils.formatDate(requete.getStartDate());
    String endDate = DateUtils.formatDate(requete.getEndDate());
    minMaxPeriod.setStart(startDate);
    minMaxPeriod.setEnd(endDate);
    periods.add(minMaxPeriod);

    this.manageDigitRelation(benef, insured, periods);
    this.manageDigitRelationInsurer(benef, insured);

    this.manageSpecialPlan(benef, minMaxPeriod, insured);
    this.manageSpecialPlanInsurer(benef, insured);
    this.manageSpecialStatusesInsurer(benef, insured);

    this.manageQuality(benef, insured);
    genericRights.setInsured(insured);
    if (Constants.ORIGINE_DECLARATIONEVT.equals(contractTP.getOrigineDeclaration())) {
      this.manageSuspensionPeriod(contractTP, genericRights, requete);
    }
    if (Boolean.TRUE.equals(benef.getAffiliation().getHasMedecinTraitant())) {
      insured.setAttendingPhysicianOpenedPeriods(periods);
    }
    mapAttendingPhysicianOpenedPeriodsInsurer(insured, benef);

    if (Boolean.TRUE.equals(contractTP.getIsContratResponsable())) {
      genericRights.setResponsibleContractOpenPeriods(periods);
    }
    mapResponsibleContractOpenPeriodsInsurer(contractTP, genericRights);

    manageCMUPeriods(contractTP, genericRights, periods);
    manageCMUPeriodsInsurer(contractTP, genericRights);

    return genericRights;
  }

  private void manageCMUPeriods(
      final ContractTP contractTP,
      final GenericRightDto genericRights,
      final List<Period> periods) {
    if ("1".equals(contractTP.getContratCMUC2S()) || "2".equals(contractTP.getContratCMUC2S())) {
      final List<CmuContractOpenPeriod> listePeriodeCMU = new ArrayList<>();
      for (final Period period : periods) {
        final CmuContractOpenPeriod periodCMU = new CmuContractOpenPeriod();
        periodCMU.setCode(("1".equals(contractTP.getContratCMUC2S()) ? "CMU" : "CMUP"));
        periodCMU.setPeriod(new Period(period.getStart(), period.getEnd()));
        listePeriodeCMU.add(periodCMU);
      }
      genericRights.setCmuContractOpenPeriods(listePeriodeCMU);
    }
  }

  void manageCollectiveContract(final ContractTP contractTP, final GenericRightDto genericRights) {
    final CollectiveContractV5 collContract = new CollectiveContractV5();
    collContract.setCompanyName(contractTP.getRaisonSociale());
    collContract.setNumber(contractTP.getNumeroContratCollectif());
    collContract.setExternalNumber(contractTP.getNumeroExterneContratCollectif());
    genericRights.setCollectiveContract(collContract);
  }

  void manageDigitRelation(
      final BeneficiaireContractTP benef, final Insured insured, final List<Period> periods) {
    final DigitRelation digitRelation = new DigitRelation();
    final List<RemoteTransmission> listRemoteTransmission = new ArrayList<>();
    for (final Period period : periods) {
      final RemoteTransmission remoteTransmission = new RemoteTransmission();
      remoteTransmission.setPeriod(new Period(period.getStart(), period.getEnd()));
      remoteTransmission.setIsRemotelyTransmitted(benef.getAffiliation().getIsTeleTransmission());
      listRemoteTransmission.add(remoteTransmission);
    }
    digitRelation.setRemoteTransmissions(listRemoteTransmission);
    insured.setDigitRelation(digitRelation);
  }

  void manageQuality(final BeneficiaireContractTP benef, final Insured insured) {
    final Quality quality = new Quality();
    final String code = benef.getAffiliation().getQualite();
    quality.setCode(code);
    quality.setLabel(this.getLabelFromCode(code));
    insured.setQuality(quality);
  }

  void manageSpecialPlan(
      final BeneficiaireContractTP benef, final Period minMaxPeriod, final Insured insured) {
    if (StringUtils.isNotBlank(benef.getAffiliation().getRegimeParticulier())) {
      final SpecialPlan specialPlan = new SpecialPlan();
      specialPlan.setCode(benef.getAffiliation().getRegimeParticulier());
      specialPlan.setPeriod(new Period(minMaxPeriod.getStart(), minMaxPeriod.getEnd()));
      insured.setSpecialPlans(List.of(specialPlan));
    }
  }

  public void manageInsuredData(final BeneficiaireContractTP benef, final Insured insured) {
    final InsuredData data = new InsuredData();
    final Name name = new Name();
    name.setLastName(benef.getAffiliation().getNom());
    name.setCommonName(
        StringUtils.isNotBlank(benef.getAffiliation().getNomPatronymique())
            ? benef.getAffiliation().getNomPatronymique()
            : benef.getAffiliation().getNomMarital());
    name.setFirstName(benef.getAffiliation().getPrenom());
    name.setCivility(benef.getAffiliation().getCivilite());
    data.setName(name);

    this.manageAdressAndContact(benef, data, insured);
  }

  Insured getInsuredV4(
      final ContractTP contractTP,
      final BeneficiaireContractTP benef,
      final List<Right> rights,
      boolean isTpOffline) {
    final Insured insured = new Insured();
    insured.setIsSubscriber(Boolean.FALSE);
    insured.setRights(rights);
    insured.setAdministrativeRank(benef.getRangAdministratif());
    final Identity id = new Identity();
    final Nir nir = new Nir();
    nir.setCode(benef.getNirBeneficiaire());
    nir.setKey(benef.getCleNirBeneficiaire());
    id.setNir(nir);

    final List<AffiliationRO> affs = new ArrayList<>();
    final Period affiliationPeriod = new Period();
    affiliationPeriod.setStart(DateUtils.formatDate(benef.getAffiliation().getPeriodeDebut()));
    if (isTpOffline) {
      String maxEndDate =
          rights.stream()
              .map(Right::getProducts)
              .flatMap(Collection::stream)
              .map(Product::getBenefitsType)
              .flatMap(Collection::stream)
              .map(BenefitType::getPeriod)
              .map(Period::getEnd)
              .filter(StringUtils::isNotBlank)
              .max(String::compareTo)
              .orElse(null);
      affiliationPeriod.setEnd(maxEndDate);
    } else {
      affiliationPeriod.setEnd(DateUtils.formatDate(benef.getAffiliation().getPeriodeFin()));
    }

    if (StringUtils.isNotEmpty(benef.getNirOd1())) {
      final AffiliationRO aff1 = new AffiliationRO();
      final Nir nirOd1 = new Nir();
      nirOd1.setCode(benef.getNirOd1());
      nirOd1.setKey(benef.getCleNirOd1());
      aff1.setNir(nirOd1);
      final AttachementRO att1 = new AttachementRO();
      att1.setRegimeCode(benef.getAffiliation().getRegimeOD1());
      att1.setHealthInsuranceCompanyCode(benef.getAffiliation().getCaisseOD1());
      att1.setCenterCode(benef.getAffiliation().getCentreOD1());
      aff1.setAttachementRO(att1);
      aff1.setPeriod(affiliationPeriod);
      affs.add(aff1);
    }

    if (StringUtils.isNotEmpty(benef.getNirOd2())) {
      final AffiliationRO aff2 = new AffiliationRO();
      final Nir nirOd2 = new Nir();
      nirOd2.setCode(benef.getNirOd2());
      nirOd2.setKey(benef.getCleNirOd2());
      aff2.setNir(nirOd2);
      final AttachementRO att2 = new AttachementRO();
      att2.setRegimeCode(benef.getAffiliation().getRegimeOD2());
      att2.setHealthInsuranceCompanyCode(benef.getAffiliation().getCaisseOD2());
      att2.setCenterCode(benef.getAffiliation().getCentreOD2());
      aff2.setAttachementRO(att2);
      aff2.setPeriod(affiliationPeriod);
      affs.add(aff2);
    }

    id.setAffiliationsRO(affs);

    mapAffiliationROInsurer(id, benef);

    id.setBirthDate(benef.getDateNaissance());
    id.setBirthRank(benef.getRangNaissance());
    id.setPersonNumber(contractTP.getIdDeclarant() + "-" + benef.getNumeroPersonne());
    id.setPersonExternalRef(benef.getRefExternePersonne());

    insured.setIdentity(id);

    insured.setHealthMutualSubscriptionDate(DateUtils.formatDate(benef.getDateAdhesionMutuelle()));
    insured.setIndividualSubscriptionStartDate(
        DateUtils.formatDate(benef.getDateDebutAdhesionIndividuelle()));
    insured.setIndividualSubscriptionNumber(benef.getNumeroAdhesionIndividuelle());
    insured.setCancellationDate(DateUtils.formatDate(benef.getDateRadiation()));

    return insured;
  }

  void manageSuspensionPeriod(
      final ContractTP contractTP,
      final GenericRightDto genericRights,
      final UniqueAccessPointRequest request) {
    final List<SuspensionPeriod> suspensionPeriods = new ArrayList<>();
    final LocalDate startDate = DateUtils.stringToDate(request.getStartDate());
    final LocalDate endDate = DateUtils.stringToDate(request.getEndDate());
    if (contractTP.getSuspension() != null
        && contractTP.getSuspension().getPeriodesSuspension() != null) {
      for (final PeriodeSuspensionContract periodeSuspensionContract :
          contractTP.getSuspension().getPeriodesSuspension()) {
        final LocalDate dateDebutSuspension =
            DateUtils.stringToDate(periodeSuspensionContract.getDateDebutSuspension());
        LocalDate dateFinSuspension = null;
        if (periodeSuspensionContract.getDateFinSuspension() != null) {
          dateFinSuspension =
              DateUtils.stringToDate(periodeSuspensionContract.getDateFinSuspension());
        }
        if (DateUtils.isOverlapping(startDate, endDate, dateDebutSuspension, dateFinSuspension)) {

          final SuspensionPeriod suspensionPeriod = new SuspensionPeriod();
          final Period period =
              new Period(
                  periodeSuspensionContract.getDateDebutSuspension(),
                  periodeSuspensionContract.getDateFinSuspension());
          suspensionPeriod.setPeriod(period);
          suspensionPeriod.setSuspensionType("Provisoire");
          suspensionPeriods.add(suspensionPeriod);
        }
      }
    }
    if (CollectionUtils.isNotEmpty(suspensionPeriods)) {
      genericRights.setSuspensionPeriods(suspensionPeriods);
    }
  }

  void manageAdressAndContact(
      final BeneficiaireContractTP benef, final InsuredData data, final Insured insured) {
    final List<Adresse> adresses = benef.getAdresses();
    final Address address = new Address();
    final Contact contact = new Contact();
    if (CollectionUtils.isNotEmpty(adresses) && adresses.get(0) != null) {
      final Adresse adresse = adresses.get(0);

      address.setLine1(adresse.getLigne1());
      address.setLine2(adresse.getLigne2());
      address.setLine3(adresse.getLigne3());
      address.setLine4(adresse.getLigne4());
      address.setLine5(adresse.getLigne5());
      address.setLine6(adresse.getLigne6());
      address.setLine7(adresse.getLigne7());
      address.setPostcode(adresse.getCodePostal());

      contact.setLandline(adresse.getTelephone());
      contact.setMobile(adresse.getTelephone());
      contact.setEmail(adresse.getEmail());
    }

    data.setAddress(address);
    data.setContact(contact);
    insured.setData(data);
  }

  private String getLabelFromCode(final String code) {
    return switch (code) {
      case "A" -> "Adhérent";
      case "C" -> "Conjoint";
      case "E" -> "Enfant";
      default -> "Autres";
    };
  }

  public List<Right> mapFromExtendedRightsToRight(
      final List<DroitsTPExtended> extendedList,
      final UniqueAccessPointRequest requete,
      final boolean force) {
    final List<Right> rightV4List = new ArrayList<>();
    // regroupement par code garantie / code oc
    final Map<ImmutablePair<String, String>, List<DroitsTPExtended>> mappedExtendedRights =
        extendedList.stream()
            .collect(
                groupingBy(
                    droitsTPExtended ->
                        new ImmutablePair<>(
                            droitsTPExtended.getCodeGarantie(),
                            droitsTPExtended.getInsurerCode())));

    for (final List<DroitsTPExtended> stringMapEntry : mappedExtendedRights.values()) {
      final Right rightV4 = new Right();
      final Map<ImmutablePair<String, String>, List<DroitsTPExtended>> mapProducts =
          stringMapEntry.stream()
              .collect(
                  groupingBy(
                      droitsTPExtended ->
                          new ImmutablePair<>(
                              droitsTPExtended.getCodeProduit(), droitsTPExtended.getCodeOffre())));
      rightV4.setProducts(new ArrayList<>());
      for (Map.Entry<ImmutablePair<String, String>, List<DroitsTPExtended>> products :
          mapProducts.entrySet()) {
        String dateDebutRight =
            products.getValue().stream()
                .map(DroitsTPExtended::getDateDebut)
                .filter(Objects::nonNull)
                .min(String::compareTo)
                .orElse(requete.getStartDate());
        String dateFinRight = getDateFinRight(requete, force, products.getValue());

        for (final DroitsTPExtended extendedRight : products.getValue()) {
          initializeRightHTP(extendedRight, rightV4);
          if (extendedRight.getCodeProduit() != null) {
            Product productV4 =
                rightV4.getProducts().stream()
                    .filter(
                        product ->
                            product.getProductCode().equals(extendedRight.getCodeProduit())
                                && product.getOfferCode().equals(extendedRight.getCodeOffre()))
                    .findFirst()
                    .orElse(null);
            if (productV4 == null) {
              productV4 = new Product();
              this.initializeProductV4HTP(dateDebutRight, dateFinRight, productV4, extendedRight);
              rightV4.getProducts().add(productV4);
            }
            if (extendedRight.getPapNatureTags() != null) {
              final BenefitType benefitType = getBenefitType(extendedRight);
              productV4.getBenefitsType().add(benefitType);
              if (extendedRight.getDateFin() == null && dateFinRight != null) {
                productV4.getPeriod().setEnd(null);
              } else if (dateFinRight != null
                  && DateUtils.before(dateFinRight, extendedRight.getDateFin())) {
                productV4.getPeriod().setEnd(extendedRight.getDateFin());
              }
            }
          }
        }
      }
      rightV4List.add(rightV4);
    }
    log.debug("after mapping, number of rights returned : {}", rightV4List.size());
    return rightV4List;
  }

  private static BenefitType getBenefitType(DroitsTPExtended extendedRight) {
    final BenefitType benefitType = new BenefitType();
    benefitType.setBenefitType(extendedRight.getPapNatureTags().getNature());
    benefitType.setTags(extendedRight.getPapNatureTags().getTags());
    final Period periodBenefit = new Period();
    periodBenefit.setStart(extendedRight.getDateDebut());
    periodBenefit.setEnd(extendedRight.getDateFin());
    benefitType.setPeriod(periodBenefit);
    return benefitType;
  }

  private static String getDateFinRight(
      UniqueAccessPointRequest requete, boolean force, List<DroitsTPExtended> stringMapEntry) {
    String dateFinRight;
    if (force) {
      if (stringMapEntry.stream()
          .anyMatch(droitsTPExtended -> droitsTPExtended.getDateFin() == null)) {
        dateFinRight = null;
      } else {
        dateFinRight =
            stringMapEntry.stream()
                .map(DroitsTPExtended::getDateFin)
                .max(String::compareTo)
                .orElse(null);
      }
    } else {
      dateFinRight =
          stringMapEntry.stream()
              .map(DroitsTPExtended::getDateFin)
              .filter(Objects::nonNull)
              .max(String::compareTo)
              .orElse(requete.getEndDate());
    }
    return dateFinRight;
  }

  private static void initializeRightHTP(
      final DroitsTPExtended extendedRight, final Right rightV4) {
    rightV4.setOriginCode(extendedRight.getOriginCode());
    rightV4.setWaitingCode(extendedRight.getCarenceCode());
    rightV4.setOriginInsurerCode(extendedRight.getOriginInsurerCode());
    rightV4.setCode(extendedRight.getCodeGarantie());
    rightV4.setType(extendedRight.getType());
    rightV4.setGuaranteeAgeDate(extendedRight.getDateAncienneteGarantie());
    rightV4.setPrioritizationOrder(extendedRight.getOrdrePriorisation());
    rightV4.setInsurerCode(extendedRight.getInsurerCode());
  }

  private void initializeProductV4HTP(
      String dateDebut, String dateFin, Product productV4, final DroitsTPExtended extendedRight) {
    productV4.setProductCode(extendedRight.getCodeProduit());
    productV4.setOfferCode(extendedRight.getCodeOffre());
    productV4.setIssuingCompanyCode(extendedRight.getCodeOc());
    try {
      final Oc oc = this.ocService.getOCByCode(extendedRight.getCodeOc());
      productV4.setIssuingCompanyName(oc != null ? oc.getLibelle() : "INCONNUE");
    } catch (final Exception e) {
      productV4.setIssuingCompanyName("INCONNUE");
    }
    final Period period = new Period();
    period.setStart(dateDebut);
    period.setEnd(dateFin);
    productV4.setPeriod(period);
  }

  private void mapResponsibleContractOpenPeriodsInsurer(
      ContractTP contractTP, GenericRightDto genericRights) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(contractTP.getPeriodeResponsableOuverts())) {
      List<Period> periods = new ArrayList<>();
      genericRights.setResponsibleContractOpenPeriods(periods);

      for (PeriodeComparable periodeResponsableOuvert : contractTP.getPeriodeResponsableOuverts()) {
        periods.add(
            new Period(periodeResponsableOuvert.getDebut(), periodeResponsableOuvert.getFin()));
      }
    }
  }

  private void mapAttendingPhysicianOpenedPeriodsInsurer(
      Insured insured, BeneficiaireContractTP benef) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(benef.getPeriodesMedecinTraitant())) {
      List<Period> attendingPhysicianOpenedPeriods = new ArrayList<>();
      insured.setAttendingPhysicianOpenedPeriods(attendingPhysicianOpenedPeriods);

      for (PeriodeComparable periode : benef.getPeriodesMedecinTraitant()) {
        attendingPhysicianOpenedPeriods.add(new Period(periode.getDebut(), periode.getFin()));
      }
    }
  }

  private void manageSpecialStatusesInsurer(BeneficiaireContractTP benef, Insured insured) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(benef.getSituationsParticulieres())) {
      List<SpecialStatus> specialStatuses = new ArrayList<>();
      insured.setSpecialStatuses(specialStatuses);

      for (CodePeriodeDeclaration situation : benef.getSituationsParticulieres()) {
        SpecialStatus specialStatus = new SpecialStatus();
        specialStatus.setCode(situation.getCode());

        PeriodeComparable periode = situation.getPeriode();
        if (periode != null) {
          specialStatus.setPeriod(new Period(periode.getDebut(), periode.getFin()));
        }

        specialStatuses.add(specialStatus);
      }
    }
  }

  private void manageCMUPeriodsInsurer(
      final ContractTP contractTP, final GenericRightDto genericRights) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(contractTP.getPeriodeCMUOuverts())) {
      final List<CmuContractOpenPeriod> listePeriodeCMU = new ArrayList<>();
      genericRights.setCmuContractOpenPeriods(listePeriodeCMU);

      for (PeriodeCMUOuvert periodeCMUOuvert : contractTP.getPeriodeCMUOuverts()) {
        CmuContractOpenPeriod cmuContractOpenPeriod = new CmuContractOpenPeriod();
        listePeriodeCMU.add(cmuContractOpenPeriod);
        cmuContractOpenPeriod.setCode(periodeCMUOuvert.getCode());
        PeriodeComparable periode = periodeCMUOuvert.getPeriode();
        if (periode != null) {
          cmuContractOpenPeriod.setPeriod(new Period(periode.getDebut(), periode.getFin()));
        }
      }
    }
  }

  private void manageDigitRelationInsurer(
      final BeneficiaireContractTP benef, final Insured insured) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(benef.getTeletransmissions())) {
      final DigitRelation digitRelation = new DigitRelation();
      final List<RemoteTransmission> listRemoteTransmission = new ArrayList<>();

      for (TeletransmissionDeclaration teletransmission : benef.getTeletransmissions()) {
        RemoteTransmission remoteTransmission = new RemoteTransmission();
        PeriodeComparable periodeComparable = teletransmission.getPeriode();
        if (periodeComparable != null) {
          remoteTransmission.setPeriod(
              new Period(periodeComparable.getDebut(), periodeComparable.getFin()));
        }
        remoteTransmission.setIsRemotelyTransmitted(teletransmission.getIsTeletransmission());

        listRemoteTransmission.add(remoteTransmission);
      }

      digitRelation.setRemoteTransmissions(listRemoteTransmission);
      insured.setDigitRelation(digitRelation);
    }
  }

  private void manageSpecialPlanInsurer(final BeneficiaireContractTP benef, final Insured insured) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(benef.getRegimesParticuliers())) {
      List<SpecialPlan> specialPlanList = new ArrayList<>();
      insured.setSpecialPlans(specialPlanList);
      for (CodePeriodeDeclaration regime : benef.getRegimesParticuliers()) {
        SpecialPlan specialPlan = new SpecialPlan();
        specialPlan.setCode(regime.getCode());

        PeriodeComparable periodeComparable = regime.getPeriode();
        if (periodeComparable != null) {
          specialPlan.setPeriod(
              new Period(periodeComparable.getDebut(), periodeComparable.getFin()));
        }

        specialPlanList.add(specialPlan);
      }
    }
  }

  private void mapAffiliationROInsurer(Identity id, BeneficiaireContractTP benef) {
    if (Constants.CLIENT_TYPE_INSURER.equals(
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && CollectionUtils.isNotEmpty(benef.getAffiliationsRO())) {
      List<AffiliationRO> affiliationROS = new ArrayList<>();
      for (NirRattachementRODeclaration nirRoDecl : benef.getAffiliationsRO()) {
        AffiliationRO affiliationRO = new AffiliationRO();
        affiliationROS.add(affiliationRO);

        if (nirRoDecl.getNir() != null) {
          NirDeclaration nirDeclaration = nirRoDecl.getNir();
          Nir nir = new Nir();
          nir.setCode(nirDeclaration.getCode());
          nir.setKey(nirDeclaration.getCle());
          affiliationRO.setNir(nir);
        }

        if (nirRoDecl.getRattachementRO() != null) {
          RattachementRODeclaration rattachementRODeclaration = nirRoDecl.getRattachementRO();
          AttachementRO attachementRO = new AttachementRO();
          attachementRO.setCenterCode(rattachementRODeclaration.getCodeCentre());
          attachementRO.setRegimeCode(rattachementRODeclaration.getCodeRegime());
          attachementRO.setHealthInsuranceCompanyCode(rattachementRODeclaration.getCodeCaisse());
          affiliationRO.setAttachementRO(attachementRO);
        }

        if (nirRoDecl.getPeriode() != null) {
          PeriodeComparable periodeDecl = nirRoDecl.getPeriode();
          Period period = new Period(periodeDecl.getDebut(), periodeDecl.getFin());
          affiliationRO.setPeriod(period);
        }
      }
      id.setAffiliationsRO(affiliationROS);
    }
  }

  /** Vide les domaines si requete TP_OFFLINE */
  public void manageDomains(List<GenericRightDto> contrats, Boolean isTPOffline) {
    if (CollectionUtils.isNotEmpty(contrats) && Boolean.TRUE.equals(isTPOffline)) {
      contrats.forEach(
          genericRightV4Dto ->
              genericRightV4Dto
                  .getInsured()
                  .getRights()
                  .forEach(
                      rightV4 ->
                          rightV4
                              .getProducts()
                              .forEach(
                                  productV4 ->
                                      productV4
                                          .getBenefitsType()
                                          .forEach(benefitType -> benefitType.setDomains(null)))));
    }
  }
}
