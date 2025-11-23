package com.cegedim.next.serviceeligibility.core.business.digitalcontractinfos.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.business.digitalcontractinfos.MapperDigitalContractInfos;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.ContractTPMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.MailleDomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPAgregationService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

@Service
public class DigitalContractInfosService {
  private final BeneficiaryService beneficiaryService;
  private final ContractDao contratDao;
  private final ServicePrestationService servicePrestationService;
  private final DeclarantBackendService declarantService;
  private final ParametreBddDaoImpl parametreBddDaoImpl;
  private final ContractTPAgregationService agregationService;
  private static final String ASSURE = "ASSURE";

  public DigitalContractInfosService(
      BeneficiaryService beneficiaryService,
      ContractDao contratDao,
      ServicePrestationService servicePrestationService,
      DeclarantBackendService declarantService,
      ParametreBddDaoImpl parametreBddDaoImpl,
      ContractTPAgregationService agregationService) {
    this.beneficiaryService = beneficiaryService;
    this.contratDao = contratDao;
    this.servicePrestationService = servicePrestationService;
    this.declarantService = declarantService;
    this.parametreBddDaoImpl = parametreBddDaoImpl;
    this.agregationService = agregationService;
  }

  public List<DigitalContractInformationsDto> getDigitalContractInformations(
      RequestDigitalContractInformationsDto request) {
    List<DigitalContractInformationsDto> digitalContractInformationsDtoList = new ArrayList<>();
    List<BenefAIV5> benefAIV5List =
        beneficiaryService.getBeneficiariesByDateReference(
            request.getInsurerId(),
            request.getSubscriberId(),
            request.getContractNumber(),
            request.getDate());

    ContratAIV6 contratHTP =
        servicePrestationService.getContratByUK(
            request.getInsurerId(), request.getContractNumber(), request.getSubscriberId());
    ContractTP contratTP =
        contratDao.getContract(
            request.getInsurerId(), request.getContractNumber(), request.getSubscriberId());
    boolean subscriberFound = false;

    for (BenefAIV5 beneficiaire : benefAIV5List) {
      subscriberFound =
          process(
              request,
              digitalContractInformationsDtoList,
              contratHTP,
              contratTP,
              subscriberFound,
              beneficiaire);
    }

    return digitalContractInformationsDtoList;
  }

  private boolean process(
      RequestDigitalContractInformationsDto request,
      List<DigitalContractInformationsDto> digitalContractInformationsDtoList,
      ContratAIV6 contratHTP,
      ContractTP contratTP,
      boolean subscriberFound,
      BenefAIV5 beneficiaire) {
    DigitalContractInformationsDto digitalContractInformationsDto =
        new DigitalContractInformationsDto();
    String numeroPersonne = beneficiaire.getIdentite().getNumeroPersonne();
    boolean isBeneficiaireHandled = false;

    if (contratHTP != null) {
      Optional<Assure> assureOptional =
          contratHTP.getAssures().stream()
              .filter(assure -> numeroPersonne.equals(assure.getIdentite().getNumeroPersonne()))
              .findFirst();
      if (assureOptional.isPresent()) {
        Assure assure = assureOptional.get();
        digitalContractInformationsDto.setAdministrativeRank(assure.getRangAdministratif());
        if (subscriberFound) {
          digitalContractInformationsDto.setIsSubscriber(false);
        } else {
          digitalContractInformationsDto.setIsSubscriber(assure.getIsSouscripteur());
          subscriberFound = assure.getIsSouscripteur();
        }
        digitalContractInformationsDto.setQuality(assure.getQualite().getCode());
        digitalContractInformationsDto.setName(
            MapperDigitalContractInfos.mapName(assure.getData().getNom()));
        isBeneficiaireHandled = true;

        // Payement recipient
        handlePaymentRecipients(assure, request.getDate(), digitalContractInformationsDto);
      }
    }
    // Si benef non trouvé dans le contrat HTP, vérifier le contrat TP
    if (!isBeneficiaireHandled && contratTP != null) {
      isBeneficiaireHandled = true;
      subscriberFound =
          processContractTP(
              contratTP, subscriberFound, digitalContractInformationsDto, numeroPersonne);
    }

    if (isBeneficiaireHandled) {
      digitalContractInformationsDto.setIdentity(
          MapperDigitalContractInfos.mapBeneficiareIdentite(
              beneficiaire.getIdentite(), request.getDate()));

      if (contratTP != null) {
        // BLUE-6171
        handleDomains(request, contratTP, digitalContractInformationsDto, numeroPersonne);
      }

      digitalContractInformationsDtoList.add(digitalContractInformationsDto);
    }
    return subscriberFound;
  }

  private void handlePaymentRecipients(
      Assure assure, String date, DigitalContractInformationsDto digitalContractInformationsDto) {
    if (CollectionUtils.isNotEmpty(assure.getData().getDestinatairesPaiements())) {
      final LocalDate dateReference = DateUtils.parse(date, DateUtils.FORMATTER);
      List<DestinatairePrestations> updatedDestinatairesPaiements =
          this.updatePaymentsBeneficiary(
              dateReference, assure.getData().getDestinatairesPaiements());
      digitalContractInformationsDto.setPaymentRecipients(
          MapperDigitalContractInfos.mapPaymentRecipients(updatedDestinatairesPaiements));
    }
  }

  public List<DestinatairePrestations> updatePaymentsBeneficiary(
      final LocalDate dateToTest, final List<DestinatairePrestations> destinatairesPaiements) {
    final Comparator<DestinatairePrestations> comparator =
        Comparator.comparing(p -> p.getPeriode().getDebut());
    destinatairesPaiements.sort(comparator.reversed());
    final List<DestinatairePrestations> newDestinatairesPaiementsList = new ArrayList<>();

    for (final DestinatairePrestations destinatairePrestationsV4 : destinatairesPaiements) {
      final boolean alreadyExist =
          newDestinatairesPaiementsList.stream()
              .anyMatch(
                  destinataire ->
                      destinatairePrestationsV4.getIdDestinatairePaiements() != null
                          && destinatairePrestationsV4
                              .getIdDestinatairePaiements()
                              .equals(destinataire.getIdDestinatairePaiements()));

      if (!alreadyExist) {
        LocalDate dateFin = null;
        if (destinatairePrestationsV4.getPeriode().getFin() != null) {
          dateFin =
              LocalDate.parse(destinatairePrestationsV4.getPeriode().getFin(), DateUtils.FORMATTER);
        }
        if (DateUtils.fromDate(dateToTest, dateFin)) {
          newDestinatairesPaiementsList.add(destinatairePrestationsV4);
        }
      }
    }

    return newDestinatairesPaiementsList;
  }

  private boolean processContractTP(
      ContractTP contratTP,
      boolean subscriberFound,
      DigitalContractInformationsDto digitalContractInformationsDto,
      String numeroPersonne) {
    Optional<BeneficiaireContractTP> benefOptional =
        contratTP.getBeneficiaires().stream()
            .filter(benef -> numeroPersonne.equals(benef.getNumeroPersonne()))
            .findFirst();
    if (benefOptional.isPresent()) {
      BeneficiaireContractTP benef = benefOptional.get();
      digitalContractInformationsDto.setAdministrativeRank(benef.getRangAdministratif());
      if (subscriberFound) {
        digitalContractInformationsDto.setIsSubscriber(false);
      } else {
        digitalContractInformationsDto.setIsSubscriber(
            ASSURE.equals(benef.getAffiliation().getTypeAssure()));
        subscriberFound = ASSURE.equals(benef.getAffiliation().getTypeAssure());
      }
      digitalContractInformationsDto.setQuality(benef.getAffiliation().getQualite());
      digitalContractInformationsDto.setName(
          MapperDigitalContractInfos.mapName(benef.getAffiliation()));
    }
    return subscriberFound;
  }

  private void handleDomains(
      RequestDigitalContractInformationsDto request,
      ContractTP contratTP,
      DigitalContractInformationsDto digitalContractInformationsDto,
      String numeroPersonne) {
    List<DomainDto> domainGroups = new ArrayList<>();
    ContractTPMailleDomaine contractTPMailleDomaine =
        agregationService.agregationMailleDomaine(contratTP);

    contractTPMailleDomaine.getBeneficiaires().stream()
        .filter(benef -> numeroPersonne.equals(benef.getNumeroPersonne()))
        .findFirst()
        .ifPresent(
            beneficiaire -> {
              List<MailleDomaineDroit> domaineDroitsBenef = beneficiaire.getDomaineDroits();
              filterDomainesFrom(domaineDroitsBenef, request.getDate());

              if (CollectionUtils.isNotEmpty(domaineDroitsBenef)) {
                for (String domain : request.getDomains()) {
                  String declarantDomain =
                      this.declarantService.transcodeDomain(request.getInsurerId(), domain);
                  DomainDto domainGroup = new DomainDto();
                  MapperDigitalContractInfos.mapDomainDto(
                      domainGroup, domain, getLibelleDomain(domain));

                  if (declarantDomain.contains(",")) {
                    handleTranscodedDomains(
                        domainGroups, domaineDroitsBenef, declarantDomain, domainGroup);
                  } else {
                    handleOtherDomains(
                        domainGroups, domain, domaineDroitsBenef, declarantDomain, domainGroup);
                  }
                }
              }
            });
    digitalContractInformationsDto.setDomains(domainGroups);
  }

  private void handleTranscodedDomains(
      List<DomainDto> domainGroups,
      List<MailleDomaineDroit> domaineDroitsBenef,
      String declarantDomain,
      DomainDto domainGroup) {
    List<String> domaineCibleList = Util.stringToList(declarantDomain);
    List<MailleDomaineDroit> filteredDomaineDroitsBenef =
        domaineDroitsBenef.stream()
            .filter(domaine -> domaineCibleList.contains(domaine.getCode()))
            .toList();

    if (CollectionUtils.isNotEmpty(filteredDomaineDroitsBenef)) {
      List<ConventionDto> conventionsCommunes = handleConventions(filteredDomaineDroitsBenef);

      if (CollectionUtils.isNotEmpty(conventionsCommunes)) {
        domainGroup.setConventions(conventionsCommunes);
        List<RegroupedDomainDto> regroupedDomains =
            filteredDomaineDroitsBenef.stream()
                .map(
                    domaineCible ->
                        MapperDigitalContractInfos.mapRegroupedDomainDto(
                            domaineCible.getCode(),
                            conventionsCommunes,
                            getLibelleDomain(domaineCible.getCode())))
                .toList();
        domainGroup.setRegroupedDomains(regroupedDomains);
        domainGroups.add(domainGroup);
      } else {
        filteredDomaineDroitsBenef.forEach(
            domaineCible -> {
              DomainDto individualDomainGroup =
                  MapperDigitalContractInfos.mapDomain(
                      domaineCible.getCode(), getLibelleDomain(domaineCible.getCode()));
              handleOtherDomains(
                  domainGroups,
                  domaineCible.getCode(),
                  filteredDomaineDroitsBenef,
                  declarantDomain,
                  individualDomainGroup);
            });
      }
    } else {
      Optional<MailleDomaineDroit> domainSourceBenef =
          domaineDroitsBenef.stream()
              .filter(domaine -> domainGroup.getDomainCode().equals(domaine.getCode()))
              .findFirst();
      domainSourceBenef.ifPresent(
          domaineDroitContract ->
              handleTranscodedDomainSource(domainGroups, domainGroup, domaineDroitContract));
    }
  }

  private void handleTranscodedDomainSource(
      List<DomainDto> domainGroups, DomainDto domainGroup, MailleDomaineDroit domainSourceBenef) {
    MapperDigitalContractInfos.mapDomainDto(
        domainGroup, domainSourceBenef.getCode(), getLibelleDomain(domainSourceBenef.getCode()));
    List<ConventionnementContrat> conventions = domainSourceBenef.getConventionnements();

    List<ConventionDto> conventionsDto =
        conventions.stream().map(MapperDigitalContractInfos::mapConventionDto).toList();
    domainGroup.setConventions(conventionsDto);
    domainGroup.setRegroupedDomains(
        List.of(
            MapperDigitalContractInfos.mapRegroupedDomainDto(
                domainSourceBenef.getCode(),
                conventionsDto,
                getLibelleDomain(domainSourceBenef.getCode()))));

    domainGroups.add(domainGroup);
  }

  private void handleOtherDomains(
      List<DomainDto> domainGroups,
      String domain,
      List<MailleDomaineDroit> domaineDroitsBenef,
      String declarantDomain,
      DomainDto domainGroup) {
    MailleDomaineDroit domainBenef =
        domaineDroitsBenef.stream()
            .filter(domaine -> declarantDomain.contains(domaine.getCode()))
            .findFirst()
            .orElse(null);

    if (domainBenef != null) {
      List<ConventionnementContrat> conventions =
          domaineDroitsBenef.stream()
              .filter(domaineDroit -> domain.equals(domaineDroit.getCode()))
              .flatMap(domaineDroit -> domaineDroit.getConventionnements().stream())
              .toList();
      List<ConventionDto> conventionsDto =
          conventions.stream().map(MapperDigitalContractInfos::mapConventionDto).toList();

      domainGroup.setConventions(conventionsDto);
      RegroupedDomainDto regroupedDomainDto = new RegroupedDomainDto();
      regroupedDomainDto.setDomainCode(domainGroup.getDomainCode());
      regroupedDomainDto.setDomainLabel(domainGroup.getDomainLabel());
      regroupedDomainDto.setConventions(conventionsDto);
      domainGroup.setRegroupedDomains(List.of(regroupedDomainDto));
      domainGroups.add(domainGroup);
    }
  }

  private List<ConventionDto> handleConventions(
      List<MailleDomaineDroit> filteredDomaineDroitsBenef) {
    List<ConventionDto> resultConventionnements = new ArrayList<>();

    Optional<List<ConventionnementContrat>> firstConventionnementsOptional =
        filteredDomaineDroitsBenef.stream()
            .map(MailleDomaineDroit::getConventionnements)
            .findFirst();

    if (firstConventionnementsOptional.isEmpty()) {
      return resultConventionnements;
    }

    List<ConventionnementContrat> referenceConventionnements = firstConventionnementsOptional.get();
    if (hasSameConventions(filteredDomaineDroitsBenef, referenceConventionnements)) {
      referenceConventionnements.forEach(
          conventionnement -> {
            ConventionDto conventionDto =
                MapperDigitalContractInfos.mapConventionDto(conventionnement);
            resultConventionnements.add(conventionDto);
          });
    }
    return resultConventionnements;
  }

  private boolean hasSameConventions(
      List<MailleDomaineDroit> filteredDomaineDroitsBenef,
      List<ConventionnementContrat> referenceConventionnements) {
    return filteredDomaineDroitsBenef.stream()
        .allMatch(
            domaine ->
                areConventionnementsEqual(
                    referenceConventionnements, domaine.getConventionnements()));
  }

  private boolean areConventionnementsEqual(
      List<ConventionnementContrat> referenceConventionnements,
      List<ConventionnementContrat> otherConventionnements) {
    // Comparaison des ensembles pour vérifier l'égalité des conventionnements
    Set<ConventionnementContrat> referenceSet = new HashSet<>(referenceConventionnements);
    Set<ConventionnementContrat> otherSet = new HashSet<>(otherConventionnements);
    return referenceSet.equals(otherSet);
  }

  public String getLibelleDomain(String codeDomain) {
    ParametreBdd param = parametreBddDaoImpl.findParametres("domaine");
    if (param != null && param.getListeValeurs() != null) {
      return param.getListeValeurs().stream()
          .filter(Map.class::isInstance)
          .map(obj -> (Map<?, ?>) obj)
          .filter(valeur -> codeDomain.equals(valeur.get("code")))
          .map(valeur -> (String) valeur.get("libelle"))
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  private void filterDomainesFrom(List<MailleDomaineDroit> domaineDroitsBenef, String from) {
    Iterator<MailleDomaineDroit> iterator = ListUtils.emptyIfNull(domaineDroitsBenef).iterator();
    while (iterator.hasNext()) {
      MailleDomaineDroit mailleDomaineDroit = iterator.next();
      filterPeriodesDroitsFrom(mailleDomaineDroit.getPeriodesDroit(), from);
      if (CollectionUtils.isNotEmpty(mailleDomaineDroit.getPeriodesDroit())) {
        filterSubPeriodesFrom(
            mailleDomaineDroit.getPrioritesDroit(), PrioriteDroitContrat::getPeriodes, from);
        filterSubPeriodesFrom(
            mailleDomaineDroit.getRemboursements(), RemboursementContrat::getPeriodes, from);
        filterSubPeriodesFrom(
            mailleDomaineDroit.getPrestations(), PrestationContrat::getPeriodes, from);
        filterSubPeriodesFrom(
            mailleDomaineDroit.getConventionnements(), ConventionnementContrat::getPeriodes, from);
      } else {
        iterator.remove();
      }
    }
  }

  private void filterPeriodesDroitsFrom(List<PeriodeDroitContractTP> periodesDroits, String from) {
    ListUtils.emptyIfNull(periodesDroits)
        .removeIf(periode -> DateUtils.beforeAnyFormat(periode.getPeriodeFin(), from));
  }

  private <T> void filterSubPeriodesFrom(
      List<T> subObj, Function<T, List<Periode>> getPeriodes, String from) {
    Iterator<T> iterator = ListUtils.emptyIfNull(subObj).iterator();
    while (iterator.hasNext()) {
      T obj = iterator.next();
      List<Periode> periodes = ListUtils.emptyIfNull(getPeriodes.apply(obj));
      periodes.removeIf(periode -> DateUtils.beforeAnyFormat(periode.getFin(), from));
      if (CollectionUtils.isEmpty(periodes)) {
        iterator.remove();
      }
    }
  }
}
