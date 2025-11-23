package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractAdherentPeriode;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.UniqueAccessPointUtil;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalIssuingCompanyCodeException;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTpSortRights;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTpSortSubscriber;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTriTP;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public abstract class UniqueAccessPointServiceTPImpl implements UniqueAccessPointService {

  private static final String RESILIATED_CONTRACT = "Contrat résilié";
  private final UniqueAccessPointTpSortSubscriber uniqueAccessPointTpSortSubscriber;
  private final MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP;
  private final UniqueAccessPointTpSortRights uniqueAccessPointTpSortRights;
  private final UniqueAccessPointTriTP uniqueAccessPointTriTP;

  private final IssuingCompanyCodeService issuingCompanyCodeService;
  private final UAPForceService uapForceService;

  protected UniqueAccessPointServiceTPImpl(
      final UniqueAccessPointTpSortSubscriber uniqueAccessPointTpSortSubscriber,
      final MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP,
      final UniqueAccessPointTpSortRights uniqueAccessPointTpSortRights,
      final UniqueAccessPointTriTP uniqueAccessPointTriTP,
      final IssuingCompanyCodeService issuingCompanyCodeService,
      final UAPForceService uapForceService) {
    super();
    this.uniqueAccessPointTpSortSubscriber = uniqueAccessPointTpSortSubscriber;
    this.mapperUniqueAccessPointServiceTP = mapperUniqueAccessPointServiceTP;
    this.uniqueAccessPointTpSortRights = uniqueAccessPointTpSortRights;
    this.uniqueAccessPointTriTP = uniqueAccessPointTriTP;
    this.issuingCompanyCodeService = issuingCompanyCodeService;
    this.uapForceService = uapForceService;
  }

  private static final Logger LOGGER =
      LoggerFactory.getLogger(UniqueAccessPointServiceTPImpl.class);

  public UniqueAccessPointResponse execute(final UniqueAccessPointRequest requete) {
    final UniqueAccessPointResponse response = new UniqueAccessPointResponse();
    UniqueAccessPointUtil.formatDatesContractForRequestV5(requete);
    List<GenericRightDto> results = new ArrayList<>(this.executeTP(requete));
    results =
        results.stream()
            .filter(right -> !CollectionUtils.isEmpty(right.getInsured().getRights()))
            .toList();

    if (results.isEmpty()) {
      // 5911
      throw new UAPFunctionalIssuingCompanyCodeException(
          RESILIATED_CONTRACT,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_SERVICE_PRESTATION_NOT_FOUND);
    }

    response.setContracts(results);
    return response;
  }

  private List<GenericRightDto> executeTP(final UniqueAccessPointRequest requete) {
    final UniqueAccessPointTPRequest uniqueAccessPointTPRequest =
        this.getUniqueAccessPointTPRequest();
    uniqueAccessPointTPRequest.setRequest(requete);
    final boolean isForced =
        (requete instanceof UniqueAccessPointRequestV5)
            ? ((UniqueAccessPointRequestV5) requete).getIsForced()
            : false;
    boolean foundContractsWithForce = false;
    LOGGER.debug(LOG_BENEFICIAIRE_DEBUT, requete.getContext());
    this.findBenefs(uniqueAccessPointTPRequest);

    // recherche sans subscriberId et domains, ils seront traités par des filtres
    // plus tard.
    LOGGER.debug(LOG_ADHERENT_DEBUT, requete.getContext());
    List<ContractTP> contratsOnline = this.getContracts(uniqueAccessPointTPRequest);
    deleteOthersBeneficiaries(uniqueAccessPointTPRequest, contratsOnline);
    if (!CollectionUtils.isEmpty(contratsOnline) && isForced) {
      // Traitement supplémentaire suite au forçage
      final TypePeriode typePeriode =
          uniqueAccessPointTPRequest.getRequest().getContext().equals(ContextConstants.TP_ONLINE)
              ? TypePeriode.ONLINE
              : TypePeriode.OFFLINE;
      deleteUselessPeriods(typePeriode, contratsOnline);
      List<ContractTP> contractsFiltered =
          uapForceService.filterContracts(uniqueAccessPointTPRequest, contratsOnline);
      if (CollectionUtils.isEmpty(contractsFiltered)) {
        // Si pas de résultat après filtrage sur la période demandée => forçage
        contratsOnline =
            uapForceService.filterContractsForce(contratsOnline, uniqueAccessPointTPRequest);
        foundContractsWithForce = true;
      } else {
        contratsOnline = contractsFiltered;
      }
    }
    if (CollectionUtils.isEmpty(contratsOnline)) {
      // 5911
      throw new UAPFunctionalIssuingCompanyCodeException(
          RESILIATED_CONTRACT,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_RESILIATED_CONTRACT);
    }

    uniqueAccessPointTPRequest.setNumeroAdherent(
        this.getNumeroAdherent(uniqueAccessPointTPRequest, contratsOnline));
    LOGGER.debug("Numero d adherent : {}", uniqueAccessPointTPRequest.getNumeroAdherent());
    LOGGER.debug(LOG_CONTRAT_DEBUT, requete.getContext());
    List<ContractTP> contratsOnlineSelected =
        contratsOnline.stream()
            .filter(
                contract ->
                    uniqueAccessPointTPRequest
                        .getNumeroAdherent()
                        .equals(contract.getNumeroAdherent()))
            .toList();

    if (CollectionUtils.isEmpty(contratsOnlineSelected)) {
      // 5911
      throw new UAPFunctionalIssuingCompanyCodeException(
          RESILIATED_CONTRACT,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_RESILIATED_CONTRACT);
    }

    if (CollectionUtils.isNotEmpty(uniqueAccessPointTPRequest.getRequest().getDomains())) {

      throwOnNoDepenseTypeOpen(uniqueAccessPointTPRequest, contratsOnlineSelected);

      if (!foundContractsWithForce) {
        throwOnNoRightOpen(uniqueAccessPointTPRequest, contratsOnlineSelected);
      }
    }
    if (!foundContractsWithForce) {
      deletePeriodsOutOfRequest(uniqueAccessPointTPRequest, contratsOnlineSelected);
      contratsOnlineSelected = limitPeriod(uniqueAccessPointTPRequest, contratsOnlineSelected);
    }

    if (CollectionUtils.isEmpty(contratsOnlineSelected)) {
      // 5911
      throw new UAPFunctionalIssuingCompanyCodeException(
          RESILIATED_CONTRACT,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_RESILIATED_CONTRACT);
    }
    String clientType = Constants.CLIENT_TYPE_INSURER;
    if (uniqueAccessPointTPRequest
        instanceof UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5) {
      clientType = uniqueAccessPointTPRequestV5.getRequest().getClientType();
    }
    if (ContextConstants.TP_OFFLINE.equals(requete.getContext())
        || (ContextConstants.TP_ONLINE.equals(requete.getContext())
            && Constants.CLIENT_TYPE_OTP.equals(clientType))) {
      limitNaturesToDomaine(uniqueAccessPointTPRequest, contratsOnlineSelected);
    }

    List<GenericRightDto> resultsTP = new ArrayList<>();

    // map online to CC
    for (ContractTP contrat : contratsOnlineSelected) {
      try {
        mapperUniqueAccessPointServiceTP.addMappedResult(
            requete,
            resultsTP,
            contrat,
            isTpOffline(),
            contrat.getIdDeclarant(),
            uniqueAccessPointTPRequest.getNumeroPersonnes(),
            foundContractsWithForce);
      } catch (PwException e) {
        throw new UAPFunctionalException(
            e.getMessage(),
            HttpStatus.NOT_FOUND,
            RestErrorConstants.ERROR_CODE_PRODUCT_WORKSHOP_ERROR);
      }
    }
    LOGGER.debug(LOG_CONTRAT_TRI_DEBUT, requete.getContext());
    uniqueAccessPointTriTP.triTP(resultsTP, requete);
    for (GenericRightDto contract : resultsTP) {
      uniqueAccessPointTpSortRights.sort(contract.getInsured().getRights());
    }
    LOGGER.debug(LOG_CONTRAT_RESTITUTION_DEBUT, requete.getContext());
    return resultsTP;
  }

  /**
   * BLUE-5911 Méthode permettant de renvoyer une réponse même si le bénéficiaire n’est pas sur un
   * contrat ouvert à la date des soins
   *
   * @return un Triplet représentant la réponse attendue : issuingCompanyCode, insurerId,
   *     subscriberId
   */
  public Triple<String, String, String> executeBis(final UniqueAccessPointRequest requete) {
    UniqueAccessPointUtil.formatDatesForRequest(requete);
    final UniqueAccessPointTPRequest uniqueAccessPointTPRequest =
        this.getUniqueAccessPointTPRequest();
    uniqueAccessPointTPRequest.setRequest(requete);
    LOGGER.debug(LOG_BENEFICIAIRE_DEBUT, requete.getContext());
    this.findBenefs(uniqueAccessPointTPRequest);
    boolean isTpOnline = ContextConstants.TP_ONLINE.equals(requete.getContext());
    List<ContractTP> futurContractTPS =
        this.getFuturContractsForIssuingCompanyCode(uniqueAccessPointTPRequest, isTpOnline);

    if (CollectionUtils.isEmpty(futurContractTPS)) {
      List<ContractTP> pastContractTPS =
          this.getPastContractsForIssuingCompanyCode(uniqueAccessPointTPRequest, isTpOnline);
      return issuingCompanyCodeService.getPastContractInfo(
          uniqueAccessPointTPRequest.getNumeroPersonnes(), pastContractTPS, isTpOnline);
    }
    return issuingCompanyCodeService.getFuturContractInfo(
        uniqueAccessPointTPRequest.getNumeroPersonnes(), futurContractTPS, isTpOnline);
  }

  private static List<ContractTP> limitPeriod(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      List<ContractTP> contratsOnlineSelected) {
    return contratsOnlineSelected.stream()
        .filter(
            contract ->
                (contract.getBeneficiaires().stream()
                    .flatMap(
                        beneficiaireContract -> beneficiaireContract.getDomaineDroits().stream())
                    .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
                    .flatMap(garantie -> garantie.getProduits().stream())
                    .flatMap(produit -> produit.getReferencesCouverture().stream())
                    .flatMap(
                        referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
                    .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
                    .anyMatch(
                        periodeDroitContract ->
                            UniqueAccessPointUtil.isOverlappingPeriod(
                                uniqueAccessPointTPRequest.getRequest(), periodeDroitContract))))
        .toList();
  }

  private static void deletePeriodsOutOfRequest(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest, List<ContractTP> contratsOnline) {
    contratsOnline.forEach(
        contract ->
            contract.getBeneficiaires().stream()
                .flatMap(beneficiaireContract -> beneficiaireContract.getDomaineDroits().stream())
                .filter(
                    domaineDroitContract ->
                        uniqueAccessPointTPRequest
                            .getRequest()
                            .getDomains()
                            .contains(domaineDroitContract.getCode()))
                .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
                .flatMap(garantie -> garantie.getProduits().stream())
                .flatMap(produit -> produit.getReferencesCouverture().stream())
                .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
                .forEach(
                    naturePrestation ->
                        naturePrestation
                            .getPeriodesDroit()
                            .removeIf(
                                periodeDroitContract ->
                                    !UniqueAccessPointUtil.isOverlappingPeriod(
                                        uniqueAccessPointTPRequest.getRequest(),
                                        periodeDroitContract))));
  }

  private static void deleteOthersBeneficiaries(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest, List<ContractTP> contratsOnline) {
    contratsOnline.forEach(
        contract ->
            contract
                .getBeneficiaires()
                .removeIf(
                    beneficiaireContract ->
                        !uniqueAccessPointTPRequest
                            .getNumeroPersonnes()
                            .contains(beneficiaireContract.getNumeroPersonne())));
  }

  private static void deleteUselessPeriods(
      TypePeriode typePeriode, List<ContractTP> contratsOnline) {
    contratsOnline.forEach(
        contract ->
            contract
                .getBeneficiaires()
                .forEach(
                    beneficiaireContract ->
                        beneficiaireContract
                            .getDomaineDroits()
                            .forEach(
                                domaineDroitContractTP ->
                                    domaineDroitContractTP
                                        .getGaranties()
                                        .forEach(
                                            garantie ->
                                                garantie
                                                    .getProduits()
                                                    .forEach(
                                                        produit ->
                                                            produit
                                                                .getReferencesCouverture()
                                                                .forEach(
                                                                    referenceCouverture ->
                                                                        referenceCouverture
                                                                            .getNaturesPrestation()
                                                                            .forEach(
                                                                                naturePrestation ->
                                                                                    naturePrestation
                                                                                        .getPeriodesDroit()
                                                                                        .removeIf(
                                                                                            periodeDroitContractTP ->
                                                                                                periodeDroitContractTP
                                                                                                            .getTypePeriode()
                                                                                                        != null
                                                                                                    && !typePeriode
                                                                                                        .equals(
                                                                                                            periodeDroitContractTP
                                                                                                                .getTypePeriode())))))))));
  }

  private void throwOnNoRightOpen(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      List<ContractTP> contratsOnlineSelected) {
    contratsOnlineSelected =
        contratsOnlineSelected.stream()
            .filter(
                contract ->
                    (contract.getBeneficiaires().stream()
                        .flatMap(
                            beneficiaireContract ->
                                beneficiaireContract.getDomaineDroits().stream())
                        .filter(
                            domaineDroitContract ->
                                uniqueAccessPointTPRequest
                                    .getRequest()
                                    .getDomains()
                                    .contains(domaineDroitContract.getCode()))
                        .flatMap(
                            domaineDroitContract -> domaineDroitContract.getGaranties().stream())
                        .flatMap(garantie -> garantie.getProduits().stream())
                        .flatMap(produit -> produit.getReferencesCouverture().stream())
                        .flatMap(
                            referenceCouverture ->
                                referenceCouverture.getNaturesPrestation().stream())
                        .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
                        .anyMatch(
                            periodeDroitContract ->
                                UniqueAccessPointUtil.isOverlappingPeriod(
                                    uniqueAccessPointTPRequest.getRequest(),
                                    periodeDroitContract))))
            .toList();
    if (CollectionUtils.isEmpty(contratsOnlineSelected)) {
      // 5911
      throw new UAPFunctionalIssuingCompanyCodeException(
          "Droits non ouverts",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_PAU_CLOSED_RIGHTS);
    }
  }

  private void throwOnNoDepenseTypeOpen(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      List<ContractTP> contratsOnlineSelected) {
    List<PeriodeDroitContractTP> periodeDroitContractTPList =
        contratsOnlineSelected.stream()
            .flatMap(contract -> contract.getBeneficiaires().stream())
            .flatMap(beneficiaireContract -> beneficiaireContract.getDomaineDroits().stream())
            .filter(
                domaineDroitContract ->
                    uniqueAccessPointTPRequest
                        .getRequest()
                        .getDomains()
                        .contains(domaineDroitContract.getCode()))
            .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
            .flatMap(garantie -> garantie.getProduits().stream())
            .flatMap(produit -> produit.getReferencesCouverture().stream())
            .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
            .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
            .toList();
    if (CollectionUtils.isEmpty(periodeDroitContractTPList)) {
      // 5911
      throw new UAPFunctionalIssuingCompanyCodeException(
          "Type de dépense non ouvert",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_PAU_CLOSED_EXPENSE_TYPE);
    }
  }

  /**
   * BLUE-4848 : PAU TP ONLINE : Restitution des contrats - limiter les natures de prestations liées
   * aux domaines d'interrogation
   */
  private void limitNaturesToDomaine(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      List<ContractTP> contratsOnlineSelected) {
    if (CollectionUtils.isNotEmpty(uniqueAccessPointTPRequest.getRequest().getDomains())) {
      contratsOnlineSelected.stream()
          .flatMap(contract -> contract.getBeneficiaires().stream())
          .forEach(
              beneficiaireContract ->
                  beneficiaireContract.setDomaineDroits(
                      beneficiaireContract.getDomaineDroits().stream()
                          .filter(
                              domaineDroitContract ->
                                  uniqueAccessPointTPRequest
                                      .getRequest()
                                      .getDomains()
                                      .contains(domaineDroitContract.getCode()))
                          .toList()));
    }
  }

  protected abstract boolean isTpOffline();

  protected abstract List<ContractTP> getContracts(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest);

  private void findBenefs(final UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    LOGGER.debug("Recherche du beneficiaire DEBUT");
    final List<BenefAIV5> benefsOnline = this.findBenefFromRequest(uniqueAccessPointTPRequest);
    final UniqueAccessPointRequest requete = uniqueAccessPointTPRequest.getRequest();
    if (CollectionUtils.isEmpty(benefsOnline)) {
      if (StringUtils.isBlank(requete.getSubscriberId())
          && !this.hasBeneficiaryIdInRequest(uniqueAccessPointTPRequest)) {
        throw new UAPFunctionalException(
            "Veuillez faire la recherche de droits en renseignant le n° d’adhérent",
            HttpStatus.BAD_REQUEST,
            RestErrorConstants.ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND_WITHOUT_SUBSCRIBER);
      }
      throw new UAPFunctionalException(
          "Bénéficiaire non trouvé",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND);
    }

    final List<String> numerosPersonnes = new ArrayList<>();
    LOGGER.debug("Recherche des beneficiaires personnes trouvés");
    for (final BenefAIV5 benef : benefsOnline) {
      numerosPersonnes.add(benef.getIdentite().getNumeroPersonne());
      LOGGER.debug(
          LOG_BENEFICIAIRE_RESULTAT, benef.getId(), benef.getIdentite().getNumeroPersonne());
    }

    uniqueAccessPointTPRequest.setNumeroPersonnes(numerosPersonnes);
    // Le nir code n'est pas obligatoire et si l'on est là c'est que l'on est en v5
    // avec le beneficiaryId, il ne peut y avoir qu'un seul benef en sorti
    if (StringUtils.isBlank(requete.getNirCode())) {
      IdentiteContrat identiteContrat = benefsOnline.get(0).getIdentite();
      if (identiteContrat != null) {
        requete.setBirthDate(identiteContrat.getDateNaissance());
        requete.setBirthRank(identiteContrat.getRangNaissance());
        if (identiteContrat.getNir() != null) {
          requete.setNirCode(identiteContrat.getNir().getCode());
        }
      }
    }
    LOGGER.debug("Recherche du beneficiaire FIN");
  }

  protected abstract List<BenefAIV5> findBenefFromRequest(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest);

  protected abstract boolean hasBeneficiaryIdInRequest(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest);

  protected void loadResults(
      final List<BenefAIV5> benefsOffline,
      final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5,
      final String insurerId,
      final List<BenefAIV5> results) {
    final List<BenefAIV5> benefWithIdDeclarant =
        results.stream()
            .filter(
                benefAIV5 ->
                    insurerId != null && insurerId.equals(benefAIV5.getAmc().getIdDeclarant()))
            .toList();
    if (CollectionUtils.isNotEmpty(benefWithIdDeclarant)) {
      benefsOffline.addAll(benefWithIdDeclarant);
      uniqueAccessPointTPRequestV5.setFoundByNumAMCEchange(false);
    } else {
      benefsOffline.addAll(results);
      uniqueAccessPointTPRequestV5.setFoundByNumAMCEchange(true);
    }
  }

  String getNumeroAdherent(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      final List<ContractTP> contratsOnline) {
    LOGGER.debug("Recherche du numero d adherent DEBUT");
    final String subscriberId = uniqueAccessPointTPRequest.getRequest().getSubscriberId();
    final List<String> domainList = uniqueAccessPointTPRequest.getRequest().getDomains();

    final List<ContractTP> contratsOnlineForSubscriberId =
        contratsOnline.stream()
            .filter(
                contract ->
                    StringUtils.isBlank(subscriberId)
                        || contract.getNumeroAdherent().equals(subscriberId))
            .toList();

    String numeroAdherent =
        this.firstSearchOfSubscriber(
            contratsOnline, subscriberId, domainList, contratsOnlineForSubscriberId);
    if (numeroAdherent != null) return numeroAdherent;

    // Si le n° adhérent n'est pas présent dans la demande ou qu'aucun contrat n'est
    // associé au n° adhérent reçu :
    if (StringUtils.isBlank(subscriberId)
        || CollectionUtils.isEmpty(contratsOnlineForSubscriberId)) {
      numeroAdherent =
          this.getNumeroAdherentWhenFirstSearchIsEmpty(
              uniqueAccessPointTPRequest, contratsOnline, domainList);
      if (numeroAdherent != null) return numeroAdherent;
    }
    if (CollectionUtils.isNotEmpty(contratsOnline)) {
      List<ContractTP> list = new ArrayList<>(contratsOnline);
      uniqueAccessPointTpSortSubscriber.sort(list, uniqueAccessPointTPRequest.getRequest());
      return list.get(0).getNumeroAdherent();
    }

    throw new UAPFunctionalException(
        RESILIATED_CONTRACT,
        HttpStatus.NOT_FOUND,
        RestErrorConstants.ERROR_CODE_PAU_RESILIATED_CONTRACT);
  }

  /**
   * Si le n° d'adhérent est présent dans la demande Et qu’aucun domaine n’est présent dans la
   * demande et qu’un ou plusieurs contrats de la personne sont associés à ce n° adhérent avec un
   * domaine à date de début de soin, alors ce n° d'adhérent doit être retenu Et qu’au moins un
   * domaine est présent dans la demande et qu’un ou plusieurs contrats de la personne sont associés
   * à ce n° adhérent et à au moins un des domaines à date de début de soin, alors ce n° d'adhérent
   * doit être retenu.
   *
   * @param contratsOnline : liste des contrats
   * @param subscriberId : adhérent
   * @param domainList : liste de domaines
   * @param contratsOnlineForSubscriberId : contrats de l'adhérent
   * @return le numéro d'adhérent
   */
  private String firstSearchOfSubscriber(
      final List<ContractTP> contratsOnline,
      final String subscriberId,
      final List<String> domainList,
      final List<ContractTP> contratsOnlineForSubscriberId) {
    LOGGER.debug("Recherche du numero d adherent (subscriberId present)");
    if (CollectionUtils.isNotEmpty(contratsOnlineForSubscriberId)
        && StringUtils.isNotBlank(subscriberId)) {
      if (CollectionUtils.isNotEmpty(domainList)) {
        final boolean contratTrouve =
            contratsOnline.stream()
                .anyMatch(
                    contract ->
                        contract.getBeneficiaires().stream()
                            .anyMatch(
                                beneficiaireContract ->
                                    beneficiaireContract.getDomaineDroits().stream()
                                        .anyMatch(
                                            domaineDroitContract ->
                                                domainList.contains(
                                                    domaineDroitContract.getCode()))));
        if (contratTrouve) {
          return subscriberId;
        }
        return null;
      } else {
        return subscriberId;
      }
    }
    return null;
  }

  String getNumeroAdherentWhenFirstSearchIsEmpty(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest,
      final List<ContractTP> contratsOnline,
      final List<String> domainList) {
    LOGGER.debug("Recherche du numero d adherent (seconde recherche) ");
    List<ContractTP> filteredContractTP = contratsOnline;
    if (CollectionUtils.isNotEmpty(domainList)) {
      filteredContractTP =
          contratsOnline.stream()
              .filter(
                  contract ->
                      contract.getBeneficiaires().stream()
                          .anyMatch(
                              beneficiaireContract ->
                                  beneficiaireContract.getDomaineDroits().stream()
                                      .anyMatch(
                                          domaineDroitContract ->
                                              domainList.contains(domaineDroitContract.getCode()))))
              .collect(Collectors.toList());
    }
    String numeroAdherent =
        this.rechercheAdherent(uniqueAccessPointTPRequest.getRequest(), filteredContractTP);
    if (numeroAdherent == null && CollectionUtils.isNotEmpty(domainList)) {
      numeroAdherent =
          this.rechercheAdherent(uniqueAccessPointTPRequest.getRequest(), contratsOnline);
    }
    return numeroAdherent;
  }

  private String rechercheAdherent(
      final UniqueAccessPointRequest request, final List<ContractTP> filteredContractTP) {
    LOGGER.debug("Recherche du numero d adherent (la date de début la plus proche) ");
    // On sélectionne le n° adhérent lié au contrat permettant d'avoir la date de
    // début de couverture égale, ou la plus proche possible, de la date de début de
    // soins
    final List<ContractAdherentPeriode> contractAdherentPeriodeList =
        getAdherentPeriodList(filteredContractTP, request);
    final List<ContractAdherentPeriode> filteredContractAdherentPeriodeList =
        closestContractPeriod(request.getStartDate(), contractAdherentPeriodeList);
    if (CollectionUtils.isNotEmpty(filteredContractAdherentPeriodeList)) {
      if (filteredContractAdherentPeriodeList.size() == 1) {
        return filteredContractAdherentPeriodeList.getFirst().getNumeroAdherent();
      }
      // sélectionner le n° adhérent lié au contrat priorisé avec les règles suivantes
      return selectPrioritizedSubscriber(
          request, filteredContractTP, filteredContractAdherentPeriodeList);
    }
    return null;
  }

  private String selectPrioritizedSubscriber(
      final UniqueAccessPointRequest request,
      final List<ContractTP> filteredContractTP,
      final List<ContractAdherentPeriode> filteredContractAdherentPeriodeList) {
    final List<ContractTP> contractTPList =
        filteredContractTP.stream()
            .filter(
                contract ->
                    filteredContractAdherentPeriodeList.stream()
                        .anyMatch(
                            contractAdherentPeriode ->
                                contractAdherentPeriode
                                        .getNumeroAdherent()
                                        .equals(contract.getNumeroAdherent())
                                    && contractAdherentPeriode
                                        .getNumeroContrat()
                                        .equals(contract.getNumeroContrat())
                                    && contractAdherentPeriode
                                        .getIdDeclarant()
                                        .equals(contract.getIdDeclarant())))
            .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(contractTPList)) {
      this.uniqueAccessPointTpSortSubscriber.sort(contractTPList, request);
      return contractTPList.getFirst().getNumeroAdherent();
    }
    return null;
  }

  static List<ContractAdherentPeriode> getAdherentPeriodList(
      final List<ContractTP> contratsOnline, final UniqueAccessPointRequest request) {
    final List<ContractAdherentPeriode> contractAdherentPeriodeList = new ArrayList<>();

    contratsOnline.forEach(
        contract ->
            contract
                .getBeneficiaires()
                .forEach(
                    beneficiaireContract ->
                        beneficiaireContract.getDomaineDroits().stream()
                            .flatMap(
                                domaineDroitContract ->
                                    domaineDroitContract.getGaranties().stream())
                            .flatMap(garantie -> garantie.getProduits().stream())
                            .flatMap(produit -> produit.getReferencesCouverture().stream())
                            .flatMap(
                                referenceCouverture ->
                                    referenceCouverture.getNaturesPrestation().stream())
                            .flatMap(
                                naturePrestation -> naturePrestation.getPeriodesDroit().stream())
                            .filter(
                                periodeDroitContract ->
                                    DateUtils.isOverlapping(
                                        periodeDroitContract.getPeriodeDebut(),
                                        periodeDroitContract.getPeriodeFin(),
                                        request.getStartDate(),
                                        request.getEndDate()))
                            .forEach(
                                periodeDroitContract -> {
                                  final ContractAdherentPeriode contractAdherentPeriode =
                                      new ContractAdherentPeriode();
                                  contractAdherentPeriode.setNumeroAdherent(
                                      contract.getNumeroAdherent());
                                  contractAdherentPeriode.setIdDeclarant(contract.getIdDeclarant());
                                  contractAdherentPeriode.setNumeroContrat(
                                      contract.getNumeroContrat());
                                  contractAdherentPeriode.setPeriodeDroitContractTP(
                                      periodeDroitContract);
                                  contractAdherentPeriodeList.add(contractAdherentPeriode);
                                })));
    return contractAdherentPeriodeList;
  }

  static List<ContractAdherentPeriode> closestContractPeriod(
      final String startDate, final List<ContractAdherentPeriode> periodeDroitContractList) {
    List<ContractAdherentPeriode> retour = new ArrayList<>();
    final Comparator<ContractAdherentPeriode> comparator =
        Comparator.comparing(c -> c.getPeriodeDroitContractTP().getPeriodeDebut());
    final List<ContractAdherentPeriode> sortedList =
        periodeDroitContractList.stream().sorted(comparator).toList();
    LocalDate debutRequete = LocalDate.parse(startDate);
    Long ecartPrecedent = null;
    for (final ContractAdherentPeriode contractAdherentPeriode : sortedList) {
      final LocalDate debutPeriode =
          LocalDate.parse(
              contractAdherentPeriode.getPeriodeDroitContractTP().getPeriodeDebut(),
              DateUtils.SLASHED_FORMATTER);
      long ecartPeriode = ChronoUnit.DAYS.between(debutRequete, debutPeriode);
      if (ecartPrecedent == null || (ecartPrecedent > 0 && ecartPeriode < ecartPrecedent)) {
        // Une periode contrat qui couvre la date de demande primera toujours face a une
        // autre qui ne couvre pas. Sinon on prend la periode ayant une date de debut la
        // plus proche de la date demande
        retour.clear();
        retour.add(contractAdherentPeriode);
        ecartPrecedent = ecartPeriode;
      } else if (ecartPeriode == ecartPrecedent || (ecartPrecedent < 0 && ecartPeriode <= 0)) {
        // Si la periode couvre la date demande alors qu une autre la couvrait deja, on
        // l ajoute
        // quand meme a la liste
        retour.add(contractAdherentPeriode);
      }
    }
    return retour;
  }

  protected abstract UniqueAccessPointTPRequest getUniqueAccessPointTPRequest();

  protected abstract List<ContractTP> getFuturContractsForIssuingCompanyCode(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest, boolean isTpOnline);

  protected abstract List<ContractTP> getPastContractsForIssuingCompanyCode(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest, boolean isTpOnline);
}
