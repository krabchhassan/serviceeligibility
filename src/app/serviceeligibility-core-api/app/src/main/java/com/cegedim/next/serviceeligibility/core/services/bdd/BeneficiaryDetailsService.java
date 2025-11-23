package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarationBackendService;
import com.cegedim.next.serviceeligibility.core.elast.contract.ContratElastic;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.mapper.MapperBenefDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.RestitutionCarte;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeneficiaryDetailsService {

  private final BeneficiaryService beneficiaryService;

  private final DeclarationBackendService declarationBackendService;

  private final ContractService contractService;

  private final MapperBenefDetails mapperBenefDetails;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  private final RestitutionCarteDao restitutionCarteDao;

  private final SasContratService sasContratService;

  private static final int CONSOLIDATED_CONTRATS_TP_END_LIMIT = 10;
  private static final int MAX_CONSOLIDATED_CONTRATS_TP = 5;
  private static final DateTimeFormatter FORMATTER_YYYY_MM_DD =
      DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED);
  private static final DateTimeFormatter FORMATTER_DD_MM_YYYY =
      DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY);

  public void validateRequestTP(
      String personNumber, String insurerId, List<String> declarationsIds, String contractNumber)
      throws RequestValidationException {
    // personNumber et insurerId sont obligatoires
    if (StringUtils.isBlank(personNumber) || StringUtils.isBlank(insurerId)) {
      throw new RequestValidationException(
          "Les champs personNumber et insurerId sont obligatoires", HttpStatus.BAD_REQUEST);
    }

    // Si declarationId n'est pas vide, alors contractNumber ne doit pas l'être, et
    // inversement
    if (CollectionUtils.isEmpty(declarationsIds) ^ StringUtils.isBlank(contractNumber)) {
      throw new RequestValidationException(
          "Les champs declarationId et contractNumber sont facultatifs, mais indissociables.",
          HttpStatus.BAD_REQUEST);
    }
  }

  @ContinueSpan(log = "getResponse BeneficiaryDetailsDto")
  public BeneficiaryDetailsDto getResponse(
      String personNumber,
      String insurerId,
      List<String> declarationsIds,
      String contractNumber,
      String subscriberNumber) {

    boolean clickOnContract = CollectionUtils.isNotEmpty(declarationsIds);

    if (clickOnContract) {
      return getPartialResponse(
          declarationsIds, contractNumber, subscriberNumber, personNumber, insurerId);
    }
    return getFullResponse(personNumber, insurerId);
  }

  BeneficiaryDetailsDto getFullResponse(String personNumber, String insurerId) {
    BeneficiaryDetailsDto beneficiaryDetails = new BeneficiaryDetailsDto();

    // Get beneficiary details from ElasticSearch
    Thread benefDetailsThread =
        new Thread(
            () -> {
              BenefAIV5 benefAIV5 =
                  beneficiaryService.getBeneficiaryByKey(
                      beneficiaryService.calculateKey(insurerId, personNumber));

              BeneficiaryDto beneficiaryDto = MapperBenefDetails.mapBeneficiaryDto(benefAIV5);
              List<ContratV5> filteredContracts = new ArrayList<>();
              benefAIV5
                  .getContrats()
                  .forEach(
                      contratV5 -> {
                        if (CollectionUtils.isNotEmpty(
                            declarationBackendService
                                .getDeclarationDao()
                                .findDeclarationsByBenefContrat(
                                    benefAIV5.getAmc().getIdDeclarant(),
                                    benefAIV5.getIdentite().getNumeroPersonne(),
                                    contratV5.getNumeroContrat(),
                                    1))) {
                          filteredContracts.add(contratV5);
                        }
                      });
              // Tri par numeroAdherent décroissant, puis par numeroContrat décroissant
              filteredContracts.sort(
                  Comparator.comparing(ContratV5::getNumeroAdherent, Comparator.reverseOrder())
                      .thenComparing(ContratV5::getNumeroContrat, Comparator.reverseOrder()));

              // Extraction des numéros après tri
              List<String> numeroContratTP =
                  filteredContracts.stream().map(ContratV5::getNumeroContrat).toList();

              beneficiaryDto.setNumerosContratTP(numeroContratTP);
              beneficiaryDetails.setBenefDetails(beneficiaryDto);
            });

    // Get declarationToOpen
    AtomicReference<List<RechercheDroitDto>> droitDtos = new AtomicReference<>();

    Thread droitDtosThread =
        new Thread(
            () -> {
              droitDtos.set(
                  declarationBackendService.getInfoDroitsAssures(insurerId, personNumber));
              RechercheInfosDroitsDto declarationToOpen = new RechercheInfosDroitsDto();
              declarationToOpen.setDroits(droitDtos.get());
              declarationToOpen.setTotalDroits(droitDtos.get().size());

              beneficiaryDetails.setDeclarationToOpen(declarationToOpen);
            });

    // Start all threads
    benefDetailsThread.start();
    droitDtosThread.start();

    try {
      // Wait for benefDetailsThread
      benefDetailsThread.join();
      if (beneficiaryDetails.getBenefDetails() == null) {
        throw new RequestValidationException(
            "Beneficiary not found in ElasticSearch", HttpStatus.NOT_FOUND);
      }

      // Get consolidatedContractList
      // Get contractToOpen
      // Get attestations
      // Get historiqueConsolidations
      // Get otherBenefs
      // Get sasContractList
      fillContractData(beneficiaryDetails.getBenefDetails().getKey(), beneficiaryDetails);

      // Wait for droitDtosThread
      droitDtosThread.join();

      // Throw an error if no declaration were found for this person
      if (CollectionUtils.isEmpty(droitDtos.get())) {
        throw new RequestValidationException(
            "No declarations found for person " + personNumber + " and amc " + insurerId,
            HttpStatus.NOT_FOUND);
      }

      // Determine list of declarations to get
      Pair<Boolean, List<String>> declarationsIds =
          getDeclarationList(null, beneficiaryDetails, droitDtos, 0);
      // Get declarationDetails
      // Get restitutionsCarte
      DeclarationDetailsDto declarationDetails;
      List<InfosAssureDto> infosAssureRestitCarteList;
      if (beneficiaryDetails.getContractToOpen() != null) {
        infosAssureRestitCarteList =
            getRestitutionsCarte(
                personNumber,
                insurerId,
                beneficiaryDetails.getContractToOpen().getNumeroContrat(),
                beneficiaryDetails.getContractToOpen().getNumeroAdherent(),
                null);
        declarationDetails =
            getDeclarationDetails(
                declarationsIds,
                beneficiaryDetails.getContractToOpen().getNumeroContrat(),
                infosAssureRestitCarteList);
      } else {
        infosAssureRestitCarteList =
            getRestitutionsCarte(personNumber, insurerId, null, null, null);
        declarationDetails =
            getDeclarationDetails(declarationsIds, null, infosAssureRestitCarteList);
      }

      beneficiaryDetails.setDeclarationDetails(declarationDetails);

    } catch (InterruptedException e) {
      // Interrupt remaining threads
      benefDetailsThread.interrupt();
      droitDtosThread.interrupt();

      // Rethrow error
      throw new RequestValidationException(
          "Error while waiting for threads", e, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (RequestValidationException e) {
      // Interrupt remaining threads
      benefDetailsThread.interrupt();
      droitDtosThread.interrupt();

      // Rethrow error
      throw e;
    }

    return beneficiaryDetails;
  }

  @NotNull
  private List<InfosAssureDto> getRestitutionsCarte(
      String personNumber,
      String insurerId,
      String contractNumber,
      String subscriberNumber,
      Date dateEffetDebut) {
    List<RestitutionCarte> restitutionsCarte =
        restitutionCarteDao.findRestitutionByIdDeclarantBenefContrat(
            insurerId,
            personNumber,
            contractNumber,
            subscriberNumber,
            dateEffetDebut,
            Constants.MAX_ITEMS_PER_LOAD_UI);

    return restitutionsCarte.stream().map(this::mapRestitution).toList();
  }

  private List<InfosAssureDto> getNextRestitutionsCartes(
      String personNumber,
      String insurerId,
      String contractNumber,
      String subscriberNumber,
      int startIndex) {
    List<InfosAssureDto> infosAssureRestitCarteList = new ArrayList<>();
    List<String> restitutionsCartesIds =
        restitutionCarteDao.getRestitutionsIdsByIdDeclarantBenefContrat(
            insurerId,
            personNumber,
            contractNumber,
            subscriberNumber,
            Constants.MAX_DECLARATIONS_FOR_UI);
    if (CollectionUtils.isNotEmpty(restitutionsCartesIds)) {
      // On limite à MAX_ITEMS_PER_LOAD_UI éléments pour ne pas surcharger l'UI avec l'affichage des
      // déclarations
      // Calcul de la fin de la sous-liste (finIndex)
      int finIndex =
          Math.min(startIndex + Constants.MAX_ITEMS_PER_LOAD_UI, restitutionsCartesIds.size());
      List<String> restitutionsIds = restitutionsCartesIds.subList(startIndex, finIndex);
      restitutionsIds.forEach(
          restitutionId ->
              infosAssureRestitCarteList.add(
                  mapRestitution(restitutionCarteDao.findById(restitutionId))));
      return infosAssureRestitCarteList;
    }
    return infosAssureRestitCarteList;
  }

  private InfosAssureDto mapRestitution(RestitutionCarte restitutionCarte) {
    InfosAssureDto infosAssureRestitCarte = new InfosAssureDto();

    String formattedRestitutionCarte =
        Optional.ofNullable(restitutionCarte.getDateRestitutionCarte())
            .map(
                dateStr ->
                    LocalDate.parse(dateStr, FORMATTER_YYYY_MM_DD).format(FORMATTER_DD_MM_YYYY))
            .orElse(null);

    infosAssureRestitCarte.setIsRestitutionCarte(true);
    infosAssureRestitCarte.setDateRestitution(formattedRestitutionCarte);
    infosAssureRestitCarte.setEffetDebut(restitutionCarte.getEffetDebut());
    infosAssureRestitCarte.setId(restitutionCarte.get_id());
    ContratDto contratDto = new ContratDto();
    contratDto.setNumeroAdherent(restitutionCarte.getNumeroAdherent());
    contratDto.setNumero(restitutionCarte.getNumeroContrat());
    infosAssureRestitCarte.setContrat(contratDto);
    return infosAssureRestitCarte;
  }

  @NotNull
  private Pair<Boolean, List<String>> getDeclarationList(
      String numContrat,
      BeneficiaryDetailsDto beneficiaryDetails,
      AtomicReference<List<RechercheDroitDto>> droitDtos,
      int startIndex) {
    for (RechercheDroitDto droit : droitDtos.get()) {
      for (RechercheContratDroitDto contrat : droit.getContrats()) {
        if (numContrat == null
            ? beneficiaryDetails.getContractToOpen() == null
                || beneficiaryDetails
                    .getContractToOpen()
                    .getNumeroContrat()
                    .equals(contrat.getNumero())
            : numContrat.equals(contrat.getNumero())) {
          List<HistoriqueInfoDeclarationDto> historiquesInfos =
              getHistoriqueInfoDeclarationDtos(contrat);

          return extractDeclarationIds(historiquesInfos, startIndex);
        }
      }
    }
    return Pair.of(false, Collections.emptyList());
  }

  @NotNull
  private static List<HistoriqueInfoDeclarationDto> getHistoriqueInfoDeclarationDtos(
      RechercheContratDroitDto contrat) {
    List<HistoriqueInfoDeclarationDto> historiquesInfos =
        contrat.getHistorique().getInfosHistorique();

    if (CollectionUtils.isEmpty(historiquesInfos)) {
      throw new RequestValidationException(
          "No history found for contract " + contrat.getNumero(), HttpStatus.NOT_FOUND);
    }
    return historiquesInfos.stream()
        .filter(histoInfoDecl -> histoInfoDecl.getIdHistorique() != null)
        .toList();
  }

  private Pair<Boolean, List<String>> extractDeclarationIds(
      List<HistoriqueInfoDeclarationDto> historiquesInfos, int startIndex) {
    // On limite à MAX_ITEMS_PER_LOAD_UI éléments pour ne pas surcharger l'UI avec l'affichage des
    // déclarations
    // Calcul de la fin de la sous-liste (finIndex)
    int finIndex = Math.min(startIndex + Constants.MAX_ITEMS_PER_LOAD_UI, historiquesInfos.size());

    // Vérification s'il reste encore des declarations à récupérer après ce lot
    boolean searchNextDeclaration = (finIndex < historiquesInfos.size());

    // The first history in the list is the most recent
    return Pair.of(
        searchNextDeclaration,
        getAllIdHistorique(
            startIndex > finIndex
                ? Collections.emptyList()
                : historiquesInfos.subList(startIndex, finIndex)));
  }

  private void fillContractData(String benefKey, BeneficiaryDetailsDto beneficiaryDetails) {
    BenefAIV5 benef = beneficiaryService.getBeneficiaryByKey(benefKey);
    String numeroPersonneFromBenefKey = benefKey.substring(benefKey.indexOf('-') + 1);
    fillContractDataCommon(benef, beneficiaryDetails, null, numeroPersonneFromBenefKey, false);
  }

  private void fillContractDataLight(
      String numeroPersonne,
      String idDeclarant,
      BeneficiaryDetailsDto beneficiaryDetails,
      String numeroContrat) {
    BenefAIV5 benef = beneficiaryService.getBeneficiaryByKey(idDeclarant + "-" + numeroPersonne);
    fillContractDataCommon(benef, beneficiaryDetails, numeroContrat, numeroPersonne, true);
  }

  private void fillContractDataCommon(
      BenefAIV5 benef,
      BeneficiaryDetailsDto beneficiaryDetails,
      String numeroContrat,
      String numeroPersonne,
      boolean lightCall) {
    List<ConsolidatedContractDto> consolidatedContractDtoList = new ArrayList<>();
    Map<String, AttestationsContractDto> attestations = new HashMap<>();
    Map<String, ConsolidatedContractHistory> historiqueConsolidations = new HashMap<>();
    Map<String, List<BeneficiaireContractDto>> otherBenefs = new HashMap<>();
    for (ContratV5 contrat : benef.getContrats()) {
      ContractTP contractTP =
          contractService.getContract(
              benef.getAmc().getIdDeclarant(),
              contrat.getNumeroContrat(),
              contrat.getNumeroAdherent());
      if (contractTP != null) {
        ConsolidatedContractDto consolidatedContractDto =
            mapperBenefDetails.mapConsolidatedContractDto(contractTP);
        consolidatedContractDtoList.add(consolidatedContractDto);

        if (!lightCall) {
          historiqueConsolidations.put(
              contrat.getNumeroContrat(),
              getHistoriqueConso(
                  benef.getAmc().getIdDeclarant(),
                  contrat.getNumeroContrat(),
                  contrat.getNumeroAdherent(),
                  numeroPersonne,
                  0));

          attestations.put(
              contrat.getNumeroContrat(),
              mapperBenefDetails.mapAttestations(
                  0,
                  benef.getAmc().getIdDeclarant(),
                  benef.getIdentite().getNumeroPersonne(),
                  contrat.getNumeroContrat()));
          otherBenefs.put(contrat.getNumeroContrat(), getBeneficiaries(benef, contrat));
        }
      }
    }

    beneficiaryDetails.setAttestations(attestations);
    beneficiaryDetails.setConsolidatedContractList(consolidatedContractDtoList);
    beneficiaryDetails.setHistoriqueConsolidations(historiqueConsolidations);
    beneficiaryDetails.setOtherBenefs(otherBenefs);

    if (CollectionUtils.isNotEmpty(consolidatedContractDtoList)) {
      LocalDate lastConsolidationDate = getLastConsolidationDate(consolidatedContractDtoList);

      setContractToOpen(
          beneficiaryDetails,
          consolidatedContractDtoList,
          lastConsolidationDate,
          numeroContrat,
          numeroPersonne);

      beneficiaryDetails.setSasContractList(sasContratService.getByPersonNumber(numeroPersonne));
    }
  }

  private List<BeneficiaireContractDto> getBeneficiaries(BenefAIV5 benef, ContratV5 contrat) {
    List<BenefAIV5> benefAIV5List =
        beneficiaryService.getBeneficiaries(
            benef.getAmc().getIdDeclarant(),
            contrat.getNumeroAdherent(),
            contrat.getNumeroContrat(),
            benef.getIdentite().getNumeroPersonne());

    return mapperBenefDetails.mapOtherBeneficiaires(benefAIV5List, contrat.getNumeroContrat());
  }

  private static void setContractToOpen(
      BeneficiaryDetailsDto beneficiaryDetails,
      List<ConsolidatedContractDto> consolidatedContractDtoList,
      LocalDate lastConsolidationDate,
      String numeroContrat,
      String numeroPersonne) {

    String formattedLastConsolidationDate = DateUtils.formatDate(lastConsolidationDate);

    List<ConsolidatedContractDto> filteredContracts =
        consolidatedContractDtoList.stream()
            .filter(
                consolidatedContract -> {
                  boolean matchesContractNumber =
                      numeroContrat != null
                          && !numeroContrat.isEmpty()
                          && numeroContrat.equals(consolidatedContract.getNumeroContrat());
                  boolean matchesConsolidationDate =
                      (numeroContrat == null || numeroContrat.isEmpty())
                          && formattedLastConsolidationDate.equals(
                              consolidatedContract.getDateConsolidation());
                  return matchesContractNumber || matchesConsolidationDate;
                })
            .toList();

    String targetNumeroPersonne =
        (numeroPersonne != null)
            ? numeroPersonne
            : beneficiaryDetails.getBenefDetails().getIdentite().getNumeroPersonne();

    Optional<ConsolidatedContractDto> contractToOpen =
        filteredContracts.stream()
            .filter(
                consolidatedContract ->
                    consolidatedContract.getBeneficiaires().stream()
                        .anyMatch(
                            beneficiaireContractDto ->
                                targetNumeroPersonne.equals(
                                    beneficiaireContractDto.getNumeroPersonne())))
            .findFirst();

    contractToOpen.ifPresent(beneficiaryDetails::setContractToOpen);
  }

  private static LocalDate getLastConsolidationDate(
      List<ConsolidatedContractDto> consolidatedContractDtoList) {
    Optional<LocalDate> lastConsolidationDateOptional =
        consolidatedContractDtoList.stream()
            .map(
                consoDto ->
                    DateUtils.parseLocalDate(consoDto.getDateConsolidation(), DateUtils.YYYY_MM_DD))
            .filter(Objects::nonNull)
            .max(LocalDate::compareTo);
    return lastConsolidationDateOptional.orElse(null);
  }

  ConsolidatedContractHistory getHistoriqueConso(
      String idDeclarant,
      String numeroContrat,
      String numeroAdherent,
      String numeroPersonne,
      int startIndex) {
    log.debug("getHistoriqueConso from index  {}", startIndex);
    Pageable pageable;
    // je rajoute 10 pour que le bouton "affiche plus" soit présent
    if (startIndex > 0) {
      pageable = PageRequest.of(0, startIndex + CONSOLIDATED_CONTRATS_TP_END_LIMIT);
    } else {
      pageable = PageRequest.of(startIndex, CONSOLIDATED_CONTRATS_TP_END_LIMIT);
    }

    List<ContratElastic> consolidationList =
        getContratsElastic(idDeclarant, numeroContrat, numeroAdherent, numeroPersonne, pageable);

    // Calcul de la fin de la sous-liste (finIndex)
    int finIndex = Math.min(startIndex + MAX_CONSOLIDATED_CONTRATS_TP, consolidationList.size());

    // Vérification s'il reste encore des consolidations à récupérer après ce lot
    boolean searchNextConsoContractTp = (finIndex < consolidationList.size());

    return getHistoriqueConsoContratsTP(
        startIndex > finIndex
            ? Collections.emptyList()
            : consolidationList.subList(startIndex, finIndex),
        searchNextConsoContractTp);
  }

  private ConsolidatedContractHistory getHistoriqueConsoContratsTP(
      List<ContratElastic> consolidationList, boolean searchNextConsoContractTp) {
    ConsolidatedContractHistory consolidatedContractHistory = new ConsolidatedContractHistory();
    List<ConsolidatedContractDto> contractListDto = new ArrayList<>();

    for (ContratElastic consolidatedContract : consolidationList) {
      ConsolidatedContractDto consolidatedContractDto =
          mapperBenefDetails.mapConsolidatedContractForHistoConso(consolidatedContract);
      contractListDto.add(consolidatedContractDto);
    }
    consolidatedContractHistory.setConsolidatedContracts(contractListDto);
    consolidatedContractHistory.setSearchNextConsoContractTp(searchNextConsoContractTp);

    return consolidatedContractHistory;
  }

  private List<ContratElastic> getContratsElastic(
      String idDeclarant,
      String numeroContrat,
      String numeroAdherent,
      String numeroPersonne,
      Pageable pageable) {
    List<ContratElastic> consolidationList =
        elasticHistorisationContractService.findByIdDeclarantContratAdherentAndNumeroPersonne(
            idDeclarant, numeroContrat, numeroAdherent, numeroPersonne, pageable);

    // Tri par ordre décroissant de la date d’historisation
    Comparator<ContratElastic> consolidationListComparator =
        Comparator.comparing(conso -> this.getConsoDateTime(conso.getDateSauvegarde()));
    consolidationList.sort(consolidationListComparator.reversed());

    return consolidationList;
  }

  private LocalDateTime getConsoDateTime(LocalDateTime dateTime) {
    String date = DateUtils.formatDateTime(dateTime);
    String time = DateUtils.formatTime(dateTime);
    LocalDate parsedDate = DateUtils.parse(date, DateUtils.FORMATTER);
    LocalTime parsedTime = DateUtils.parseTime(time, DateUtils.HH_MM_SS_FORMATTER);
    return LocalDateTime.of(parsedDate, parsedTime);
  }

  List<String> getAllIdHistorique(List<HistoriqueInfoDeclarationDto> infos) {
    List<String> allIdHistorique = new ArrayList<>();
    // Should never happen
    if (CollectionUtils.isEmpty(infos)) {
      throw new RequestValidationException("La liste des historiques de la déclaration est vide.");
    }

    for (HistoriqueInfoDeclarationDto info : infos) {
      if (StringUtils.isNotBlank(info.getIdHistorique())) {
        allIdHistorique.add(info.getIdHistorique());
      }
    }

    // Should never happen
    if (allIdHistorique.isEmpty()) {
      throw new RequestValidationException(
          "Aucun historique n'a pu être lu dans la liste de la déclaration.");
    }

    return allIdHistorique;
  }

  BeneficiaryDetailsDto getPartialResponse(
      List<String> declarationsIds,
      String contractNumber,
      String subscriberNumber,
      String personNumber,
      String insurerId) {
    BeneficiaryDetailsDto beneficiaryDetails = new BeneficiaryDetailsDto();

    // On limite à MAX_ITEMS_PER_LOAD_UI éléments pour ne pas surcharger l'UI avec l'affichage des
    // déclarations
    // Calcul de la fin de la sous-liste (finIndex)
    int finIndex = Math.min(Constants.MAX_ITEMS_PER_LOAD_UI, declarationsIds.size());

    // Vérification s'il reste encore des declarations à récupérer après ce lot
    boolean searchNextDeclaration = (finIndex < declarationsIds.size());

    // Get restitutionsCarte
    List<InfosAssureDto> infosAssureRestitCarteList =
        getRestitutionsCarte(personNumber, insurerId, contractNumber, subscriberNumber, null);

    // Get declarationDetails
    DeclarationDetailsDto declarationDetails =
        getDeclarationDetails(
            Pair.of(searchNextDeclaration, declarationsIds.subList(0, finIndex)),
            contractNumber,
            infosAssureRestitCarteList);

    beneficiaryDetails.setDeclarationDetails(declarationDetails);

    // Get consolidatedContractList
    // Get contractToOpen
    // Get attestations => []
    // Get historiqueConsolidations => []
    // Get otherBenefs => []
    // Get sasContractList
    fillContractDataLight(personNumber, insurerId, beneficiaryDetails, contractNumber);

    return beneficiaryDetails;
  }

  DeclarationDetailsDto getDeclarationDetails(
      Pair<Boolean, List<String>> declarationsIds,
      String contractNumber,
      List<InfosAssureDto> infosAssureRestitCarteList)
      throws RequestValidationException {
    List<InfosAssureDto> infosAssure = new ArrayList<>();
    boolean searchNextDeclarations = declarationsIds.getLeft();

    for (String declarationId : declarationsIds.getRight()) {
      // Get declaration details
      InfosAssureDto detail = declarationBackendService.findById(declarationId, true);

      // We add the declaration only if it matches the asked contract
      if (detail != null
          && (contractNumber == null || contractNumber.equals(detail.getContrat().getNumero()))) {
        // detail is always non-null here
        infosAssure.add(detail);
      }
    }

    // If no declaration was found, throw an error
    if (CollectionUtils.isEmpty(infosAssure)) {
      throw new RequestValidationException(
          "Impossible de trouver une déclaration avec les ids fournis en entrée : "
              + declarationsIds,
          HttpStatus.NOT_FOUND);
    }
    return fillDeclarationDetails(infosAssure, infosAssureRestitCarteList, searchNextDeclarations);
  }

  private DeclarationDetailsDto fillDeclarationDetails(
      List<InfosAssureDto> infosAssure,
      List<InfosAssureDto> infosAssureRestitCarteList,
      boolean searchNextDeclarations) {
    DeclarationDetailsDto declarationDetailsDto = new DeclarationDetailsDto();
    List<InfosAssureDto> infosAssureDtos = new ArrayList<>();
    infosAssure.sort(
        Comparator.comparing(InfosAssureDto::getEffetDebut, Comparator.reverseOrder())
            .thenComparing(InfosAssureDto::getId, Comparator.reverseOrder()));
    if (CollectionUtils.isNotEmpty(infosAssureRestitCarteList)) {
      List<InfosAssureDto> restitutionsCartes = new ArrayList<>(infosAssureRestitCarteList);
      restitutionsCartes.sort(
          Comparator.comparing(InfosAssureDto::getEffetDebut, Comparator.reverseOrder())
              .thenComparing(InfosAssureDto::getId, Comparator.reverseOrder()));

      infosAssure.forEach(
          declaration -> {
            if (StringUtils.isNotBlank(declaration.getDroits().getDateRestitutionCarte())) {
              Optional<InfosAssureDto> matchingRestitution =
                  restitutionsCartes.stream()
                      .filter(
                          restitutionCarte ->
                              declaration
                                      .getDroits()
                                      .getDateRestitutionCarte()
                                      .equals(restitutionCarte.getDateRestitution())
                                  && (restitutionCarte
                                          .getEffetDebut()
                                          .equals(declaration.getEffetDebut())
                                      || org.apache.commons.lang3.time.DateUtils.isSameDay(
                                          restitutionCarte.getEffetDebut(),
                                          declaration.getEffetDebut())))
                      .findFirst();
              if (matchingRestitution.isPresent()) {
                infosAssureDtos.add(matchingRestitution.get());
                infosAssureDtos.add(declaration);
                restitutionsCartes.remove(matchingRestitution.get());
              } else {
                infosAssureDtos.add(declaration);
              }
            } else {
              infosAssureDtos.add(declaration);
            }
          });

      if (infosAssureDtos.size() > Constants.MAX_ITEMS_PER_LOAD_UI) {
        // Supprime les éléments en trop pour ne garder que les MAX_ITEMS_PER_LOAD_UI
        // premiers éléments.
        infosAssureDtos.subList(Constants.MAX_ITEMS_PER_LOAD_UI, infosAssureDtos.size()).clear();
        searchNextDeclarations = true;
      }
      declarationDetailsDto.setInfosAssureDtos(infosAssureDtos);
    } else {
      declarationDetailsDto.setInfosAssureDtos(infosAssure);
    }
    declarationDetailsDto.setSearchNextDeclarations(searchNextDeclarations);
    return declarationDetailsDto;
  }

  public AttestationsContractDto getNextCertifications(
      int startIndex, String idDeclarant, String contractNumber, String personNumber) {
    return mapperBenefDetails.mapAttestations(
        startIndex, idDeclarant, personNumber, contractNumber);
  }

  public ConsolidatedContractHistory getNextConsolidatedContratsTP(
      int startIndex,
      String amcId,
      String contractNumber,
      String subscriberId,
      String personNumber) {
    return getHistoriqueConso(amcId, contractNumber, subscriberId, personNumber, startIndex);
  }

  public DeclarationDetailsDto getNextDeclarations(
      int startIndex,
      int startIndexRestit,
      String idDeclarant,
      String contractNumber,
      String subscriberNumber,
      String personNumber) {

    AtomicReference<List<RechercheDroitDto>> droitDtos = new AtomicReference<>();
    droitDtos.set(declarationBackendService.getInfoDroitsAssures(idDeclarant, personNumber));

    // Get restitutionsCarte
    List<InfosAssureDto> infosAssureRestitCarteList =
        getNextRestitutionsCartes(
            personNumber, idDeclarant, contractNumber, subscriberNumber, startIndexRestit);

    Pair<Boolean, List<String>> declarationsIds =
        getDeclarationList(contractNumber, null, droitDtos, startIndex);
    return getDeclarationDetails(declarationsIds, contractNumber, infosAssureRestitCarteList);
  }
}
