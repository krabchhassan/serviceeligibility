package com.cegedim.next.serviceeligibility.core.mapper;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeAdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.AttestationDetailDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ConventionDto;
import com.cegedim.next.serviceeligibility.core.elast.contract.ContratElastic;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.BeneficiaireMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.ContractTPMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.BeneficiaireMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.ContractTPMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.BeneficiaireMailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.ContractTPMailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.BeneficiaireMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.ContractTPMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.services.CartesService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPAgregationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperBenefDetails {
  public static final int MAX_CERTIFICATIONS = 15;

  private final CartesService cartesService;
  private final ContractTPAgregationService agregationService;
  private final BeyondPropertiesService beyondPropertiesService;

  @ContinueSpan(log = "mapAttestations AttestationsContractDto")
  public AttestationsContractDto mapAttestations(
      int startIndex, String idDeclarant, String numeroPersonne, String numeroContrat) {
    AttestationsContractDto attestationsContract = new AttestationsContractDto();
    List<AttestationContractDto> attestationDtoList =
        getAttestationContractList(idDeclarant, numeroPersonne, numeroContrat);

    // Calcul de la fin de la sous-liste (finIndex)
    int finIndex = Math.min(startIndex + MAX_CERTIFICATIONS, attestationDtoList.size());

    // Vérification s'il reste encore des declarations à récupérer après ce lot
    boolean searchNextCertifications = (finIndex < attestationDtoList.size());

    attestationsContract.setCertifications(
        startIndex > finIndex
            ? Collections.emptyList()
            : attestationDtoList.subList(startIndex, finIndex));
    attestationsContract.setSearchNextCertifications(searchNextCertifications);

    return attestationsContract;
  }

  private List<AttestationContractDto> getAttestationContractList(
      String idDeclarant, String numeroPersonne, String numeroContrat) {
    List<CarteDemat> carteDematList =
        cartesService.findAllCartesDematByDeclarantAndAMCContrat(
            idDeclarant, numeroContrat, numeroPersonne);

    List<AttestationContractDto> attestationDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(carteDematList)) {
      for (CarteDemat carte : carteDematList) {
        attestationDtoList.add(mapAttestationContractDto(carte, numeroPersonne));
      }
    }
    return attestationDtoList;
  }

  private AttestationContractDto mapAttestationContractDto(CarteDemat carte, String personNumber) {
    AttestationContractDto attestationContractDto = new AttestationContractDto();

    List<String> codeServices = carte.getCodeServices();
    attestationContractDto.setIsCarteDemat(codeServices.contains(Constants.CARTE_DEMATERIALISEE));
    attestationContractDto.setIsCartePapier(codeServices.contains(Constants.CARTE_TP));
    attestationContractDto.setIsLastCarteDemat(carte.getIsLastCarteDemat());
    attestationContractDto.setDateCreation(carte.getDateCreation());
    attestationContractDto.setPeriodeDebut(carte.getPeriodeDebut());
    attestationContractDto.setPeriodeFin(carte.getPeriodeFin());
    Contrat contrat = carte.getContrat();
    attestationContractDto.setCodeRenvoi(contrat.getCodeRenvoi());
    attestationContractDto.setLibelleRenvoi(contrat.getLibelleCodeRenvoi());
    attestationContractDto.setModeleCarte(contrat.getFondCarte());
    attestationContractDto.setAnnexe1Carte(contrat.getAnnexe1Carte());
    attestationContractDto.setAnnexe2Carte(contrat.getAnnexe2Carte());
    attestationContractDto.setCodeItelis(contrat.getCodeItelis());

    List<BenefCarteDemat> beneficiaires = carte.getBeneficiaires();
    if (CollectionUtils.isNotEmpty(beneficiaires)) {
      Optional<BenefCarteDemat> benefCarteDematOptional =
          beneficiaires.stream()
              .filter(
                  benefCarteDemat ->
                      personNumber.equals(benefCarteDemat.getBeneficiaire().getNumeroPersonne()))
              .findFirst();
      if (benefCarteDematOptional.isPresent()) {
        List<DomaineCarte> domaineCartes = benefCarteDematOptional.get().getDomainesRegroup();
        if (CollectionUtils.isNotEmpty(domaineCartes)) {
          mapAttestationDetailsfromDomaineCarte(attestationContractDto, domaineCartes);
        } else {
          // old 620
          List<DomaineDroit> domaineCouv = benefCarteDematOptional.get().getDomainesCouverture();
          if (CollectionUtils.isNotEmpty(domaineCouv)) {
            mapAttestationDetailsFromDomaineCouverture(attestationContractDto, domaineCouv);
          }
        }
      }
    }

    return attestationContractDto;
  }

  private static void mapAttestationDetailsfromDomaineCarte(
      AttestationContractDto attestationContractDto, List<DomaineCarte> domaineCartes) {
    List<AttestationDetailDto> attestationDetailDtoList = new ArrayList<>();

    for (DomaineCarte domaineCarte : domaineCartes) {
      AttestationDetailDto attestationDetailDto = new AttestationDetailDto();
      attestationDetailDto.setNumOrdreEdition(domaineCarte.getNoOrdreDroit());
      attestationDetailDto.setCodeDomaine(domaineCarte.getCode());
      attestationDetailDto.setTaux(domaineCarte.getTaux());
      attestationDetailDto.setUniteTaux(domaineCarte.getUnite());
      attestationDetailDto.setConventions(mapConventions(domaineCarte.getConventionnements()));
      attestationDetailDto.setCodeRenvois(domaineCarte.getCodeRenvoiAdditionnel());
      attestationDetailDto.setLibelleRenvois(domaineCarte.getLibelleCodeRenvoi());
      attestationDetailDto.setCodeRenvoisAdditionnel(domaineCarte.getCodeRenvoiAdditionnel());
      attestationDetailDto.setLibelleRenvoisAdditionnel(
          domaineCarte.getLibelleCodeRenvoiAdditionnel());

      attestationDetailDtoList.add(attestationDetailDto);
    }
    attestationDetailDtoList.sort(comparing(AttestationDetailDto::getNumOrdreEdition));
    attestationContractDto.setDetails(attestationDetailDtoList);
  }

  private static void mapAttestationDetailsFromDomaineCouverture(
      AttestationContractDto attestationContractDto, List<DomaineDroit> domaineDroits) {
    List<AttestationDetailDto> attestationDetailDtoList = new ArrayList<>();

    for (DomaineDroit domaineDroit : domaineDroits) {
      AttestationDetailDto attestationDetailDto = new AttestationDetailDto();
      attestationDetailDto.setNumOrdreEdition(domaineDroit.getNoOrdreDroit());
      attestationDetailDto.setCodeDomaine(domaineDroit.getCode());
      attestationDetailDto.setTaux(domaineDroit.getTauxRemboursement());
      attestationDetailDto.setUniteTaux(domaineDroit.getUniteTauxRemboursement());
      attestationDetailDto.setConventions(mapConventions(domaineDroit.getConventionnements()));
      attestationDetailDto.setCodeRenvois(domaineDroit.getCodeRenvoiAdditionnel());
      attestationDetailDto.setLibelleRenvois(domaineDroit.getLibelleCodeRenvoi());
      attestationDetailDto.setCodeRenvoisAdditionnel(domaineDroit.getCodeRenvoiAdditionnel());
      attestationDetailDto.setLibelleRenvoisAdditionnel(
          domaineDroit.getLibelleCodeRenvoiAdditionnel());

      attestationDetailDtoList.add(attestationDetailDto);
    }
    attestationDetailDtoList.sort(comparing(AttestationDetailDto::getNumOrdreEdition));
    attestationContractDto.setDetails(attestationDetailDtoList);
  }

  private static List<ConventionDto> mapConventions(List<Conventionnement> conventionnementList) {
    List<ConventionDto> conventionDtoList = new ArrayList<>();
    for (Conventionnement conventionnement : conventionnementList) {
      ConventionDto conventionDto = new ConventionDto();
      conventionDto.setCode(conventionnement.getTypeConventionnement().getCode());
      conventionDto.setPriorite(String.valueOf(conventionnement.getPriorite()));
      conventionDtoList.add(conventionDto);
    }
    return conventionDtoList;
  }

  @ContinueSpan(log = "mapConsolidatedContractDto ConsolidatedContractDto")
  public ConsolidatedContractDto mapConsolidatedContractDto(ContractTP contractTP) {
    ConsolidatedContractDto consolidatedContractDto = new ConsolidatedContractDto();
    consolidatedContractDto.setIdDeclarant(contractTP.getIdDeclarant());
    consolidatedContractDto.setNumeroContrat(contractTP.getNumeroContrat());
    consolidatedContractDto.setNumeroAdherent(contractTP.getNumeroAdherent());
    consolidatedContractDto.setNumeroAdherentComplet(contractTP.getNumeroAdherentComplet());
    consolidatedContractDto.setDateSouscription(contractTP.getDateSouscription());
    consolidatedContractDto.setDateResiliation(contractTP.getDateResiliation());
    consolidatedContractDto.setType(contractTP.getType());
    consolidatedContractDto.setNomPorteur(contractTP.getNomPorteur());
    consolidatedContractDto.setPrenomPorteur(contractTP.getPrenomPorteur());
    consolidatedContractDto.setCivilitePorteur(contractTP.getCivilitePorteur());
    consolidatedContractDto.setQualification(contractTP.getQualification());
    consolidatedContractDto.setNumeroContratCollectif(contractTP.getNumeroContratCollectif());
    consolidatedContractDto.setIsContratResponsable(contractTP.getIsContratResponsable());
    consolidatedContractDto.setIsContratCMU(contractTP.getIsContratCMU());
    consolidatedContractDto.setDestinataire(contractTP.getDestinataire());
    consolidatedContractDto.setIndividuelOuCollectif(contractTP.getIndividuelOuCollectif());
    consolidatedContractDto.setTypeConvention(contractTP.getTypeConvention());
    consolidatedContractDto.setCritereSecondaire(contractTP.getCritereSecondaire());
    consolidatedContractDto.setCritereSecondaireDetaille(contractTP.getCritereSecondaireDetaille());
    consolidatedContractDto.setNumeroExterneContratIndividuel(
        contractTP.getNumeroExterneContratIndividuel());
    consolidatedContractDto.setNumeroExterneContratCollectif(
        contractTP.getNumeroExterneContratCollectif());
    consolidatedContractDto.setGestionnaire(contractTP.getGestionnaire());
    consolidatedContractDto.setGroupeAssures(contractTP.getGroupeAssures());
    consolidatedContractDto.setNumAMCEchange(contractTP.getNumAMCEchange());
    consolidatedContractDto.setNumOperateur(contractTP.getNumOperateur());
    consolidatedContractDto.setOrdrePriorisation(contractTP.getOrdrePriorisation());
    consolidatedContractDto.setContratCMUC2S(contractTP.getContratCMUC2S());
    consolidatedContractDto.setDateRestitution(contractTP.getDateRestitution());
    consolidatedContractDto.setDateConsolidation(
        DateUtils.formatDateTime(contractTP.getDateConsolidation()));
    consolidatedContractDto.setIdentifiantCollectivite(contractTP.getIdentifiantCollectivite());
    consolidatedContractDto.setRaisonSociale(contractTP.getRaisonSociale());
    consolidatedContractDto.setSiret(contractTP.getSiret());
    consolidatedContractDto.setGroupePopulation(contractTP.getGroupePopulation());

    mapSuspension(contractTP, consolidatedContractDto);
    mapContratCMUOuvert(contractTP, consolidatedContractDto);
    mapContratResponsableOuvert(contractTP, consolidatedContractDto);

    mapBeneficiaires(contractTP, consolidatedContractDto);

    return consolidatedContractDto;
  }

  public ConsolidatedContractDto mapConsolidatedContractForHistoConso(
      ContratElastic consolidatedContract) {
    ConsolidatedContractDto consolidatedContractDto = new ConsolidatedContractDto();
    ContractTP contractTP = consolidatedContract.getContrat();
    consolidatedContractDto.setNumeroContrat(contractTP.getNumeroContrat());
    consolidatedContractDto.setNumeroAdherent(contractTP.getNumeroAdherent());
    consolidatedContractDto.setIdDeclarant(contractTP.getIdDeclarant());

    consolidatedContractDto.setDateConsolidation(
        DateUtils.formatDateTime(contractTP.getDateConsolidation(), DateUtils.FORMATTER_DATETIME));

    mapSuspension(contractTP, consolidatedContractDto);
    mapContratCMUOuvert(contractTP, consolidatedContractDto);
    mapContratResponsableOuvert(contractTP, consolidatedContractDto);

    mapBeneficiaires(contractTP, consolidatedContractDto);

    return consolidatedContractDto;
  }

  private void mapBeneficiaires(
      ContractTP contractTP, ConsolidatedContractDto consolidatedContractDto) {

    List<BeneficiaireContractTP> beneficiaires = contractTP.getBeneficiaires();
    if (CollectionUtils.isNotEmpty(beneficiaires)) {
      Comparator<BeneficiaireContractTP> comparator =
          comparing(BeneficiaireContractTP::getDateNaissance, reverseOrder())
              .thenComparing(BeneficiaireContractTP::getRangNaissance);
      beneficiaires.sort(comparator);

      preparePeriodsForOtp(contractTP);

      ContractTPMailleRefCouv contractTPMailleRefCouv =
          agregationService.agregationMailleReferenceCouverture(contractTP);
      ContractTPMailleProduit contractTPMailleProduit =
          agregationService.agregationMailleProduit(contractTP);
      ContractTPMailleGarantie contractTPMailleGarantie =
          agregationService.agregationMailleGarantie(contractTP);
      ContractTPMailleDomaine contractTPMailleDomaine =
          agregationService.agregationMailleDomaine(contractTP);

      List<BeneficiaireContractDto> beneficiaireContractDtoList = new ArrayList<>();
      for (int i = 0; i < beneficiaires.size(); i++) {
        BeneficiaireContractTP beneficiaire = beneficiaires.get(i);
        BeneficiaireContractDto beneficiaireContractDto = new BeneficiaireContractDto();
        beneficiaireContractDto.setDateRadiation(beneficiaire.getDateRadiation());
        beneficiaireContractDto.setDateNaissance(beneficiaire.getDateNaissance());
        beneficiaireContractDto.setRangNaissance(beneficiaire.getRangNaissance());
        beneficiaireContractDto.setNirBeneficiaire(beneficiaire.getNirBeneficiaire());
        beneficiaireContractDto.setCleNirBeneficiaire(beneficiaire.getCleNirBeneficiaire());
        beneficiaireContractDto.setNirOd1(beneficiaire.getNirOd1());
        beneficiaireContractDto.setCleNirOd1(beneficiaire.getCleNirOd1());
        beneficiaireContractDto.setNirOd2(beneficiaire.getNirOd2());
        beneficiaireContractDto.setCleNirOd2(beneficiaire.getCleNirOd2());
        beneficiaireContractDto.setNumeroPersonne(beneficiaire.getNumeroPersonne());
        beneficiaireContractDto.setRefExternePersonne(beneficiaire.getRefExternePersonne());
        beneficiaireContractDto.setRangAdministratif(beneficiaire.getRangAdministratif());
        beneficiaireContractDto.setCategorieSociale(beneficiaire.getCategorieSociale());
        beneficiaireContractDto.setSituationParticuliere(beneficiaire.getSituationParticuliere());
        beneficiaireContractDto.setModePaiementPrestations(
            beneficiaire.getModePaiementPrestations());
        beneficiaireContractDto.setDateAdhesionMutuelle(beneficiaire.getDateAdhesionMutuelle());
        beneficiaireContractDto.setDateDebutAdhesionIndividuelle(
            beneficiaire.getDateDebutAdhesionIndividuelle());
        beneficiaireContractDto.setNumeroAdhesionIndividuelle(
            beneficiaire.getNumeroAdhesionIndividuelle());
        mapAffiliation(beneficiaire, beneficiaireContractDto);

        List<AdresseDto> adresseDtoList = mapAdresses(beneficiaire);
        beneficiaireContractDto.setAdresses(adresseDtoList);

        List<DomaineDroitContractDto> domaineDroitDtoList =
            MapperDomaineDroitContractDto.mapDomaineDroits(
                beneficiaire,
                contractTP.getDateRestitution(),
                Util.isExcluDemat(contractTP.getCarteTPaEditerOuDigitale()),
                Constants.CLIENT_TYPE_INSURER.equals(
                    beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE)));

        BeneficiaireMailleDomaine beneficiaireMailleDomaine =
            contractTPMailleDomaine.getBeneficiaires().get(i);
        List<DomaineDroitContractDto> domaineDroitContractDtosMailleDomaine =
            MapperDomaineDroitContractDto.mapDomaineDroits(
                beneficiaireMailleDomaine,
                contractTP.getDateRestitution(),
                Util.isExcluDemat(contractTP.getCarteTPaEditerOuDigitale()),
                Constants.CLIENT_TYPE_INSURER.equals(
                    beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE)));

        BeneficiaireMailleGarantie beneficiaireMailleGarantie =
            contractTPMailleGarantie.getBeneficiaires().get(i);
        List<DomaineDroitContractDto> domaineDroitContractDtosMailleGarantie =
            MapperDomaineDroitContractDto.mapDomaineDroits(
                beneficiaireMailleGarantie,
                contractTP.getDateRestitution(),
                Util.isExcluDemat(contractTP.getCarteTPaEditerOuDigitale()),
                Constants.CLIENT_TYPE_INSURER.equals(
                    beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE)));

        BeneficiaireMailleProduit beneficiaireMailleProduit =
            contractTPMailleProduit.getBeneficiaires().get(i);
        List<DomaineDroitContractDto> domaineDroitContractDtosMailleProduit =
            MapperDomaineDroitContractDto.mapDomaineDroits(
                beneficiaireMailleProduit,
                contractTP.getDateRestitution(),
                Util.isExcluDemat(contractTP.getCarteTPaEditerOuDigitale()),
                Constants.CLIENT_TYPE_INSURER.equals(
                    beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE)));

        BeneficiaireMailleRefCouv beneficiaireMailleRefCouv =
            contractTPMailleRefCouv.getBeneficiaires().get(i);
        List<DomaineDroitContractDto> domaineDroitContractDtosMailleRefCouv =
            MapperDomaineDroitContractDto.mapDomaineDroits(
                beneficiaireMailleRefCouv,
                contractTP.getDateRestitution(),
                Util.isExcluDemat(contractTP.getCarteTPaEditerOuDigitale()),
                Constants.CLIENT_TYPE_INSURER.equals(
                    beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE)));

        triDomaines(domaineDroitDtoList);
        triDomaines(domaineDroitContractDtosMailleDomaine);
        triDomaines(domaineDroitContractDtosMailleGarantie);
        triDomaines(domaineDroitContractDtosMailleProduit);
        triDomaines(domaineDroitContractDtosMailleRefCouv);

        Map<String, List<DomaineDroitContractDto>> mapMailleDomaineDroits = new HashMap<>();
        mapMailleDomaineDroits.put(Constants.MAILLE_NATURE_PRESTATIONS_FOR_UI, domaineDroitDtoList);
        mapMailleDomaineDroits.put(
            Constants.MAILLE_REF_COUVERTURES_FOR_UI, domaineDroitContractDtosMailleRefCouv);
        mapMailleDomaineDroits.put(
            Constants.MAILLE_PRODUIT_FOR_UI, domaineDroitContractDtosMailleProduit);
        mapMailleDomaineDroits.put(
            Constants.MAILLE_GARANTIE_FOR_UI, domaineDroitContractDtosMailleGarantie);
        mapMailleDomaineDroits.put(
            Constants.MAILLE_DOMAINE_TP_FOR_UI, domaineDroitContractDtosMailleDomaine);
        beneficiaireContractDto.setMaillesDomaineDroits(mapMailleDomaineDroits);
        mapRegimesParticuliers(beneficiaire, beneficiaireContractDto);
        mapPeriodesMedecinTraitant(beneficiaire, beneficiaireContractDto);
        mapSituationsParticulieres(beneficiaire, beneficiaireContractDto);
        mapAffiliationsRO(beneficiaire, beneficiaireContractDto);
        mapTeletransmissions(beneficiaire, beneficiaireContractDto);
        beneficiaireContractDtoList.add(beneficiaireContractDto);
      }

      consolidatedContractDto.setBeneficiaires(beneficiaireContractDtoList);
    }
  }

  /** Pour un client OTP vient generer une periode online pour chaque periode offline presente */
  private void preparePeriodsForOtp(ContractTP contractTP) {
    if (Constants.CLIENT_TYPE_OTP.equals(
        beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
      for (BeneficiaireContractTP benef : ListUtils.emptyIfNull(contractTP.getBeneficiaires())) {
        for (DomaineDroitContractTP domain : ListUtils.emptyIfNull(benef.getDomaineDroits())) {
          for (Garantie garanty : ListUtils.emptyIfNull(domain.getGaranties())) {
            for (Produit product : ListUtils.emptyIfNull(garanty.getProduits())) {
              for (ReferenceCouverture ref :
                  ListUtils.emptyIfNull(product.getReferencesCouverture())) {
                for (NaturePrestation nat : ListUtils.emptyIfNull(ref.getNaturesPrestation())) {
                  List<PeriodeDroitContractTP> periods =
                      Objects.requireNonNullElse(nat.getPeriodesDroit(), new ArrayList<>());
                  List<PeriodeDroitContractTP> onlines = new ArrayList<>();

                  for (PeriodeDroitContractTP period : periods) {
                    // Creation periode online
                    PeriodeDroitContractTP online = new PeriodeDroitContractTP(period);
                    online.setTypePeriode(TypePeriode.ONLINE);
                    onlines.add(online);
                  }

                  periods.addAll(onlines);
                }
              }
            }
          }
        }
      }
    }
  }

  private static void triDomaines(List<DomaineDroitContractDto> domaineDroitDtoList) {
    // Tri par periodeDebut décroissante
    Comparator<DomaineDroitContractDto> comparingPeriodeDebut =
        comparing(MapperBenefDetails::getLatestPeriodeDebut).reversed();

    // Tri par code domaine croissant
    Comparator<DomaineDroitContractDto> comparingCode = comparing(DomaineDroitContractDto::getCode);

    domaineDroitDtoList.sort(comparingCode.thenComparing(comparingPeriodeDebut));
  }

  private static LocalDate getLatestPeriodeDebut(DomaineDroitContractDto domaine) {
    LocalDate latestDate = LocalDate.MIN;
    for (PeriodeDroitContractDto periode : domaine.getPeriodesDroit()) {
      LocalDate periodeDebut =
          DateUtils.parseLocalDate(periode.getPeriodeDebut(), DateUtils.FORMATTERSLASHED);
      if (periodeDebut != null && periodeDebut.isAfter(latestDate)) {
        latestDate = periodeDebut;
      }
    }
    return latestDate;
  }

  @ContinueSpan(log = "mapOtherBeneficiaires List<BeneficiaireContractDto>")
  public List<BeneficiaireContractDto> mapOtherBeneficiaires(
      List<BenefAIV5> benefAIV5List, String numeroContrat) {
    List<BeneficiaireContractDto> beneficiaireContractDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(benefAIV5List)) {
      for (BenefAIV5 benefAIV5 : benefAIV5List) {
        BeneficiaireContractDto beneficiaireContractDto = new BeneficiaireContractDto();

        Optional<ContratV5> benefContrat =
            benefAIV5.getContrats().stream()
                .filter(contratV5 -> numeroContrat.equals(contratV5.getNumeroContrat()))
                .findFirst();
        if (benefContrat.isPresent()) {
          ContratV5 contrat = benefContrat.get();
          AffiliationDto affiliation = new AffiliationDto();
          affiliation.setNom(contrat.getData().getNom().getNomFamille());
          affiliation.setNomMarital(contrat.getData().getNom().getNomUsage());
          affiliation.setPrenom(contrat.getData().getNom().getPrenom());
          beneficiaireContractDto.setAffiliation(affiliation);
          beneficiaireContractDto.setDateNaissance(benefAIV5.getIdentite().getDateNaissance());
          beneficiaireContractDto.setRangNaissance(benefAIV5.getIdentite().getRangNaissance());
          beneficiaireContractDto.setRangNaissance(benefAIV5.getIdentite().getRangNaissance());
          if (benefAIV5.getIdentite().getNir() != null) {
            beneficiaireContractDto.setNirBeneficiaire(benefAIV5.getIdentite().getNir().getCode());
            beneficiaireContractDto.setCleNirBeneficiaire(
                benefAIV5.getIdentite().getNir().getCle());
          }
          // Si pas de nir => on prend le 1er nir de son affiliationRO
          else if (CollectionUtils.isNotEmpty(benefAIV5.getIdentite().getAffiliationsRO())
              && benefAIV5.getIdentite().getAffiliationsRO().get(0).getNir() != null) {
            beneficiaireContractDto.setNirBeneficiaire(
                benefAIV5.getIdentite().getAffiliationsRO().get(0).getNir().getCode());
            beneficiaireContractDto.setCleNirBeneficiaire(
                benefAIV5.getIdentite().getAffiliationsRO().get(0).getNir().getCle());
          }
          beneficiaireContractDto.setNumeroPersonne(benefAIV5.getIdentite().getNumeroPersonne());

          beneficiaireContractDtoList.add(beneficiaireContractDto);
        }
      }
    }
    return beneficiaireContractDtoList;
  }

  private static List<AdresseDto> mapAdresses(BeneficiaireContractTP beneficiaire) {
    List<Adresse> adresses = beneficiaire.getAdresses();
    List<AdresseDto> adresseDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(adresses)) {
      for (Adresse adresse : adresses) {
        AdresseDto adresseDto = null;
        if (adresse != null) {
          adresseDto = new AdresseDto();
          adresseDto.setCodePostal(adresse.getCodePostal());
          adresseDto.setEmail(adresse.getEmail());
          adresseDto.setLigne1(adresse.getLigne1());
          adresseDto.setLigne2(adresse.getLigne2());
          adresseDto.setLigne3(adresse.getLigne3());
          adresseDto.setLigne4(adresse.getLigne4());
          adresseDto.setLigne5(adresse.getLigne5());
          adresseDto.setLigne6(adresse.getLigne6());
          adresseDto.setLigne7(adresse.getLigne7());
          adresseDto.setPays(adresse.getPays());
          adresseDto.setTelephone(adresse.getTelephone());
          mapTypeAdresse(adresse, adresseDto);
        }
        adresseDtoList.add(adresseDto);
      }
    }
    return adresseDtoList;
  }

  private static void mapTypeAdresse(Adresse adresse, AdresseDto adresseDto) {
    TypeAdresse typeAdresse = adresse.getTypeAdresse();
    TypeAdresseDto typeAdresseDto = null;
    if (typeAdresse != null) {
      typeAdresseDto = new TypeAdresseDto();
      typeAdresseDto.setLibelle(typeAdresse.getLibelle());
      typeAdresseDto.setType(typeAdresse.getType());
    }
    adresseDto.setTypeAdresseDto(typeAdresseDto);
  }

  private static void mapAffiliation(
      BeneficiaireContractTP beneficiaire, BeneficiaireContractDto beneficiaireContractDto) {
    Affiliation affiliation = beneficiaire.getAffiliation();
    AffiliationDto affiliationDto = null;
    if (affiliation != null) {
      affiliationDto = new AffiliationDto();
      affiliationDto.setCaisseOD1(affiliation.getCaisseOD1());
      affiliationDto.setCaisseOD2(affiliation.getCaisseOD2());
      affiliationDto.setCentreOD1(affiliation.getCentreOD1());
      affiliationDto.setCentreOD2(affiliation.getCentreOD2());
      affiliationDto.setCivilite(affiliation.getCivilite());
      affiliationDto.setIsBeneficiaireACS(affiliation.getIsBeneficiaireACS());
      affiliationDto.setMedecinTraitant(affiliation.getHasMedecinTraitant());
      affiliationDto.setNom(affiliation.getNom());
      affiliationDto.setNomMarital(affiliation.getNomMarital());
      affiliationDto.setNomPatronymique(affiliation.getNomPatronymique());
      if (StringUtils.isNotEmpty(affiliation.getPeriodeDebut())) {
        affiliationDto.setPeriodeDebut(
            DateUtils.parseDate(affiliation.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotEmpty(affiliation.getPeriodeFin())) {
        affiliationDto.setPeriodeFin(
            DateUtils.parseDate(affiliation.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
      }
      affiliationDto.setPrenom(affiliation.getPrenom());
      affiliationDto.setQualite(affiliation.getQualite());
      affiliationDto.setRegimeOD1(affiliation.getRegimeOD1());
      affiliationDto.setRegimeOD2(affiliation.getRegimeOD2());
      affiliationDto.setRegimeParticulier(affiliation.getRegimeParticulier());
      affiliationDto.setHasTeleTransmission(affiliation.getIsTeleTransmission());
      affiliationDto.setTypeAssure(affiliation.getTypeAssure());
    }
    beneficiaireContractDto.setAffiliation(affiliationDto);
  }

  private static void mapSuspension(
      ContractTP contractTP, ConsolidatedContractDto consolidatedContractDto) {
    SuspensionContract suspension = contractTP.getSuspension();
    if (suspension != null) {
      SuspensionContractDto suspensionContractDto = new SuspensionContractDto();
      suspensionContractDto.setEtatSuspension(suspension.getEtatSuspension());
      List<PeriodeSuspensionContract> periodesSuspension = suspension.getPeriodesSuspension();
      List<PeriodeSuspensionContractDto> periodesSuspensionContractDtoList = new ArrayList<>();
      for (PeriodeSuspensionContract periodeSuspensionContract : periodesSuspension) {
        PeriodeSuspensionContractDto periodeSuspensionContractDto =
            new PeriodeSuspensionContractDto();
        periodeSuspensionContractDto.setDateDebutSuspension(
            periodeSuspensionContract.getDateDebutSuspension());
        periodeSuspensionContractDto.setDateFinSuspension(
            periodeSuspensionContract.getDateFinSuspension());
        periodesSuspensionContractDtoList.add(periodeSuspensionContractDto);
      }
      suspensionContractDto.setPeriodesSuspension(periodesSuspensionContractDtoList);
      consolidatedContractDto.setSuspension(suspensionContractDto);
    }
  }

  public static void mapContratCMUOuvert(
      ContractTP contratTP, ConsolidatedContractDto consolidatedContractDto) {
    List<PeriodeCMUOuvert> periodesContratCMUOuvert = contratTP.getPeriodeCMUOuverts();
    List<CodePeriodeContractDto> periodeContratCMUOuvertDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodesContratCMUOuvert)) {
      for (PeriodeCMUOuvert periodeContratCMUOuvert : periodesContratCMUOuvert) {
        CodePeriodeContractDto periodeContratCMUOuvertDto = new CodePeriodeContractDto();
        periodeContratCMUOuvertDto.setCode(periodeContratCMUOuvert.getCode());
        periodeContratCMUOuvertDto.setPeriode(
            mapPeriodeContract(periodeContratCMUOuvert.getPeriode()));
        periodeContratCMUOuvertDtos.add(periodeContratCMUOuvertDto);
      }
      consolidatedContractDto.setPeriodesContratCMU(periodeContratCMUOuvertDtos);
    }
  }

  public static void mapContratResponsableOuvert(
      ContractTP contratTP, ConsolidatedContractDto consolidatedContractDto) {
    List<PeriodeComparable> periodesContratResponsableOuvert =
        contratTP.getPeriodeResponsableOuverts();
    List<PeriodeContractDto> periodeContractDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodesContratResponsableOuvert)) {
      for (PeriodeComparable periodeContratResponsableOuvert : periodesContratResponsableOuvert) {
        PeriodeContractDto periodeContractDto = mapPeriodeContract(periodeContratResponsableOuvert);
        periodeContractDtos.add(periodeContractDto);
      }
      consolidatedContractDto.setPeriodesContratResponsable(periodeContractDtos);
    }
  }

  public static void mapRegimesParticuliers(
      BeneficiaireContractTP beneficiaireContractTP,
      BeneficiaireContractDto beneficiaireContractDto) {
    List<CodePeriodeDeclaration> regimesParticuliers =
        beneficiaireContractTP.getRegimesParticuliers();
    List<CodePeriodeContractDto> periodeContractDtos = getCodePeriodeList(regimesParticuliers);
    beneficiaireContractDto.setRegimesParticuliers(periodeContractDtos);
  }

  public static void mapPeriodesMedecinTraitant(
      BeneficiaireContractTP beneficiaireContractTP,
      BeneficiaireContractDto beneficiaireContractDto) {
    List<PeriodeComparable> periodesMedecinTraitant =
        beneficiaireContractTP.getPeriodesMedecinTraitant();
    List<PeriodeContractDto> periodeContractDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(periodesMedecinTraitant)) {
      for (PeriodeComparable periodeMedecinTraitant : periodesMedecinTraitant) {
        PeriodeContractDto periodeContractDto = mapPeriodeContract(periodeMedecinTraitant);
        periodeContractDtos.add(periodeContractDto);
      }
      beneficiaireContractDto.setPeriodesMedecinTraitant(periodeContractDtos);
    }
  }

  public static void mapSituationsParticulieres(
      BeneficiaireContractTP beneficiaireContractTP,
      BeneficiaireContractDto beneficiaireContractDto) {
    List<CodePeriodeDeclaration> situationsParticulieres =
        beneficiaireContractTP.getSituationsParticulieres();
    List<CodePeriodeContractDto> periodeContractDtos = getCodePeriodeList(situationsParticulieres);
    beneficiaireContractDto.setSituationsParticulieres(periodeContractDtos);
  }

  public static void mapAffiliationsRO(
      BeneficiaireContractTP beneficiaireContractTP,
      BeneficiaireContractDto beneficiaireContractDto) {
    List<NirRattachementRODeclaration> affiliationsRO = beneficiaireContractTP.getAffiliationsRO();
    List<NirRattachementRODto> nirRattachementRODtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(affiliationsRO)) {
      mapNirRattachementRO(affiliationsRO, nirRattachementRODtos);
      beneficiaireContractDto.setAffiliationsRO(nirRattachementRODtos);
    }
  }

  public static void mapNirRattachementRO(
      List<NirRattachementRODeclaration> affiliationsRO,
      List<NirRattachementRODto> nirRattachementRODtos) {
    for (NirRattachementRODeclaration nirRattachementRO : affiliationsRO) {
      NirRattachementRODto nirRattachementRODto = new NirRattachementRODto();
      nirRattachementRODto.setNir(mapNir(nirRattachementRO.getNir()));
      if (nirRattachementRO.getRattachementRO() != null) {
        nirRattachementRODto.setRattachementRO(
            mapRattachementRo(nirRattachementRO.getRattachementRO()));
      }
      if (nirRattachementRO.getPeriode() != null) {
        nirRattachementRODto.setPeriode(mapPeriodeContract(nirRattachementRO.getPeriode()));
      }
      nirRattachementRODtos.add(nirRattachementRODto);
    }
  }

  public static void mapTeletransmissions(
      BeneficiaireContractTP beneficiaireContractTP,
      BeneficiaireContractDto beneficiaireContractDto) {
    List<TeletransmissionDeclaration> teletransmissions =
        beneficiaireContractTP.getTeletransmissions();
    List<TeletransmissionDto> teletransmissionDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(teletransmissions)) {
      for (TeletransmissionDeclaration teletransmission : teletransmissions) {
        TeletransmissionDto teletransmissionDto = new TeletransmissionDto();
        teletransmissionDto.setIsTeletransmission(teletransmission.getIsTeletransmission());
        teletransmissionDto.setPeriode(mapPeriodeContract(teletransmission.getPeriode()));
        teletransmissionDtos.add(teletransmissionDto);
      }
      beneficiaireContractDto.setTeletransmissions(teletransmissionDtos);
    }
  }

  private static List<CodePeriodeContractDto> getCodePeriodeList(
      List<CodePeriodeDeclaration> regimesParticuliers) {
    List<CodePeriodeContractDto> periodeContractDtos = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(regimesParticuliers)) {
      for (CodePeriodeDeclaration codePeriode : regimesParticuliers) {
        CodePeriodeContractDto codePeriodeContractDto = new CodePeriodeContractDto();
        codePeriodeContractDto.setCode(codePeriode.getCode());
        PeriodeContractDto periodeContractDto = new PeriodeContractDto();
        periodeContractDto.setDebut(codePeriode.getPeriode().getDebut());
        periodeContractDto.setFin(codePeriode.getPeriode().getFin());
        codePeriodeContractDto.setPeriode(periodeContractDto);
        periodeContractDtos.add(codePeriodeContractDto);
      }
    }
    return periodeContractDtos;
  }

  @NotNull
  private static PeriodeContractDto mapPeriodeContract(PeriodeComparable periode) {
    PeriodeContractDto periodeContractDto = new PeriodeContractDto();
    if (periode != null) {
      periodeContractDto.setDebut(periode.getDebut());
      periodeContractDto.setFin(periode.getFin());
    }
    return periodeContractDto;
  }

  @NotNull
  private static NirDto mapNir(NirDeclaration nir) {
    NirDto nirDto = new NirDto();
    if (nir != null) {
      nirDto.setCode(nir.getCode());
      nirDto.setCle(nir.getCle());
    }
    return nirDto;
  }

  @NotNull
  private static RattachementRODto mapRattachementRo(RattachementRODeclaration rattachementRO) {
    RattachementRODto rattachementRODto = new RattachementRODto();
    if (rattachementRO != null) {
      rattachementRODto.setCodeCaisse(rattachementRO.getCodeCaisse());
      rattachementRODto.setCodeCentre(rattachementRO.getCodeCentre());
      rattachementRODto.setCodeRegime(rattachementRO.getCodeRegime());
    }
    return rattachementRODto;
  }

  public static BeneficiaryDto mapBeneficiaryDto(BenefAIV5 benefAIV5) {
    BeneficiaryDto beneficiaryDto = new BeneficiaryDto();
    beneficiaryDto.setKey(benefAIV5.getKey());
    beneficiaryDto.setEnvironnement(benefAIV5.getEnvironnement());
    beneficiaryDto.setAmc(benefAIV5.getAmc());
    beneficiaryDto.setServices(benefAIV5.getServices());
    beneficiaryDto.setIdentite(benefAIV5.getIdentite());
    beneficiaryDto.setSocietesEmettrices(benefAIV5.getSocietesEmettrices());
    beneficiaryDto.setContrats(benefAIV5.getContrats());
    return beneficiaryDto;
  }
}
