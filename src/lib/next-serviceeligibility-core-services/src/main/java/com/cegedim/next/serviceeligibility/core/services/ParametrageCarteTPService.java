package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp.ParametrageCarteTPDto;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.dao.LotDao;
import com.cegedim.next.serviceeligibility.core.dao.ParametrageCarteTPDao;
import com.cegedim.next.serviceeligibility.core.dao.RequestParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTPRequest;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTPResponseDto;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametrageCarteTPResponse;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ParametrageCarteTPNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ServicePrestationNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParametrageCarteTPService {

  private final Logger logger = LoggerFactory.getLogger(ParametrageCarteTPService.class);

  private final ParametrageCarteTPDao parametrageCarteTPDao;

  private final LotDao lotDao;

  private final MapperParametrageCarteTP mapperParametrageCarteTP;

  @ContinueSpan(log = "getByParams")
  public ParametrageCarteTPResponse getByParams(
      int perPage, int page, String sortBy, String direction, ParametrageCarteTPRequest request) {
    return parametrageCarteTPDao.getByParams(perPage, page, sortBy, direction, request);
  }

  @ContinueSpan(log = "getById")
  public ParametrageCarteTPDto getById(String id) {
    return mapperParametrageCarteTP.mapParametrageCarteTPDto(parametrageCarteTPDao.getById(id));
  }

  @ContinueSpan(log = "getParametrageCarteTPForUI")
  public ParametrageCarteTP getParametrageCarteTPForUI(
      String amc, String numeroContratIndividuel, String numeroAdherent) {

    ContratAIV6 contrat =
        parametrageCarteTPDao.getServicePrestationByContratIndividuelAndNumAdherent(
            amc, numeroContratIndividuel, numeroAdherent);

    // Recherche du 1er droit du souscripteur ou du 1er assuré
    if (contrat != null) {
      return getParametrageCarteTPFromContrat(amc, contrat);
    } else {
      String message =
          String.format(
              "Aucun servicePrestation trouvé avec les informations suivantes : idDeclarant=%s - numero=%S - numeroAdherent=%S",
              amc, numeroContratIndividuel, numeroAdherent);
      logger.debug(message);
      throw new ServicePrestationNotFoundException(message);
    }
  }

  @ContinueSpan(log = "getParametrageCarteTPFromContrat")
  public ParametrageCarteTP getParametrageCarteTPFromContrat(String amc, ContratAIV6 contrat) {
    List<DroitAssure> foundDroits = getParametrageCarteTPByContratIndividuelGetDroits(contrat);

    // Appel de la recherche de paramétrage carte TP
    String identifiantCollectivite = null;
    String groupePopulation = null;
    ContratCollectifV6 contratCollectifV6 = contrat.getContratCollectif();
    if (contratCollectifV6 != null) {
      identifiantCollectivite = contratCollectifV6.getIdentifiantCollectivite();
      groupePopulation = contratCollectifV6.getGroupePopulation();
    }
    String critereSecondaireDetaille = contrat.getCritereSecondaireDetaille();

    if (CollectionUtils.isEmpty(foundDroits)) {
      logger.info(
          "Aucun droit pour le contrat identifié par : idDeclarant={} - numero={}",
          amc,
          contrat.getNumero());
      return null;
    }

    List<GarantieTechnique> gts = new ArrayList<>();
    for (DroitAssure foundDroit : foundDroits) {
      GarantieTechnique gt = new GarantieTechnique();
      gt.setCodeAssureur(foundDroit.getCodeAssureur());
      gt.setCodeGarantie(foundDroit.getCode());
      gts.add(gt);
    }

    ParametrageCarteTP bestParam =
        getBestParametrage(
            identifiantCollectivite,
            groupePopulation,
            critereSecondaireDetaille,
            gts,
            new RequestParametrageCarteTP(contrat.getIdDeclarant(), true, true, true, true));
    if (bestParam == null) {
      String message =
          String.format(
              "Aucun paramétrage TP n'est présent pour ce contrat : idDeclarant=%s - numero=%s",
              amc, contrat.getNumero());
      logger.debug(message);
      throw new ParametrageCarteTPNotFoundException(message);
    }
    return bestParam;
  }

  private List<DroitAssure> getParametrageCarteTPByContratIndividuelGetDroits(ContratAIV6 contrat) {
    List<DroitAssure> foundDroits = new ArrayList<>();
    List<Assure> assures = contrat.getAssures();
    if (!CollectionUtils.isEmpty(assures)) {
      for (Assure assure : assures) {
        List<DroitAssure> droits = assure.getDroits();
        if (!CollectionUtils.isEmpty(droits)
            && (CollectionUtils.isEmpty(foundDroits) || assure.getIsSouscripteur())) {
          foundDroits = droits;
          if (Boolean.TRUE.equals(assure.getIsSouscripteur())) {
            break;
          }
        }
      }
    }
    return foundDroits;
  }

  /** Retourne la liste des paramétrages de carte TP pour une AMC */
  @ContinueSpan(log = "getByAmc ParametrageCarteTP")
  public List<ParametrageCarteTP> getByAmc(RequestParametrageCarteTP requestParametrageCarteTP) {
    return parametrageCarteTPDao.getByAmc(requestParametrageCarteTP);
  }

  @ContinueSpan(log = "create ParametrageCarteTP")
  public void create(ParametrageCarteTP parametrageCarteTP) {
    parametrageCarteTPDao.create(parametrageCarteTP);
  }

  @ContinueSpan(log = "update ParametrageCarteTP")
  public void update(ParametrageCarteTP parametrageCarteTP) {
    parametrageCarteTPDao.update(parametrageCarteTP);
  }

  @ContinueSpan(log = "deleteAll ParametrageCarteTP")
  public void deleteAll() {
    parametrageCarteTPDao.deleteAll();
  }

  @ContinueSpan(log = "getBestParametrage")
  public ParametrageCarteTP getBestParametrageRenouvellement(
      String identifiantCollectivite,
      String groupePopulation,
      String critereSecondaireDetaille,
      List<GarantieTechnique> gts,
      List<ParametrageCarteTP> listParams) {
    if (StringUtils.isNotBlank(identifiantCollectivite)) {
      listParams =
          listParams.stream()
              .filter(
                  item -> emptyOrEqual(item.getIdentifiantCollectivite(), identifiantCollectivite))
              .toList();
    }

    if (StringUtils.isNotBlank(groupePopulation)) {
      listParams =
          listParams.stream()
              .filter(item -> emptyOrEqual(item.getGroupePopulation(), groupePopulation))
              .toList();
    }

    if (StringUtils.isNotBlank(critereSecondaireDetaille)) {
      listParams =
          listParams.stream()
              .filter(
                  item ->
                      emptyOrEqual(item.getCritereSecondaireDetaille(), critereSecondaireDetaille))
              .toList();
    }

    listParams = filterWithGTs(gts, listParams, true);

    return listParams.stream()
        .min(Comparator.comparing(ParametrageCarteTP::getPriorite))
        .orElse(null);
  }

  @ContinueSpan(log = "getBestParametrage")
  public ParametrageCarteTP getBestParametrage(
      String identifiantCollectivite,
      String groupePopulation,
      String critereSecondaireDetaille,
      List<GarantieTechnique> gts,
      RequestParametrageCarteTP requestParametrageCarteTP) {
    List<ParametrageCarteTP> listParams = getByAmc(requestParametrageCarteTP);

    if (StringUtils.isNotBlank(identifiantCollectivite)) {
      listParams =
          listParams.stream()
              .filter(
                  item -> emptyOrEqual(item.getIdentifiantCollectivite(), identifiantCollectivite))
              .toList();
    }

    if (StringUtils.isNotBlank(groupePopulation)) {
      listParams =
          listParams.stream()
              .filter(item -> emptyOrEqual(item.getGroupePopulation(), groupePopulation))
              .toList();
    }

    if (StringUtils.isNotBlank(critereSecondaireDetaille)) {
      listParams =
          listParams.stream()
              .filter(
                  item ->
                      emptyOrEqual(item.getCritereSecondaireDetaille(), critereSecondaireDetaille))
              .toList();
    }

    listParams = filterWithGTs(gts, listParams, false);

    return listParams.stream()
        .min(Comparator.comparing(ParametrageCarteTP::getPriorite))
        .orElse(null);
  }

  private List<ParametrageCarteTP> filterWithGTs(
      List<GarantieTechnique> garantieTechniqueList,
      List<ParametrageCarteTP> listParams,
      boolean renouvellement) {
    List<ParametrageCarteTP> parametrageCarteTPS = new ArrayList<>();
    for (ParametrageCarteTP parametrageCarteTP : listParams) {
      boolean inside =
          isParametrageTPCovered(garantieTechniqueList, parametrageCarteTP, renouvellement);
      if (inside) {
        parametrageCarteTPS.add(parametrageCarteTP);
      }
    }
    return parametrageCarteTPS;
  }

  /**
   * Le contrat doit avoir toutes les gt du parametrage (si parametrage gt vide pas de filtrage
   * necessaire). Le contrat doit avoir au moins 1 des GT de chaque lot du parametrage (si
   * parametrage lot vide pas de filtrage necessaire)
   */
  public boolean isParametrageTPCovered(
      List<GarantieTechnique> garantieTechniqueList,
      ParametrageCarteTP parametrageCarteTP,
      boolean renouvellement) {
    List<GarantieTechnique> gtsParam =
        Objects.requireNonNullElse(
            parametrageCarteTP.getGarantieTechniques(), Collections.emptyList());
    List<Lot> lotsParam;
    if (renouvellement) {
      lotsParam = lotDao.getListByIdsForRenewal(parametrageCarteTP.getIdLots());
    } else {
      lotsParam = lotDao.getListByIds(parametrageCarteTP.getIdLots());
    }

    for (GarantieTechnique gt : gtsParam) {
      if (!garantieTechniqueList.contains(gt)) {
        return false;
      }
    }

    for (Lot lot : lotsParam) {
      if (lot.getGarantieTechniques().stream()
          .filter(gt -> StringUtils.isBlank(gt.getDateSuppressionLogique()))
          .noneMatch(garantieTechniqueList::contains)) {
        return false;
      }
    }

    return true;
  }

  boolean emptyOrEqual(String s1, String s2) {
    return StringUtils.isBlank(s1) || s1.equals(s2);
  }

  @ContinueSpan(log = "updateStatus ParametrageCarteTP")
  public void updateStatus(String id, ParametrageCarteTPStatut statut)
      throws ParametrageCarteTPNotFoundException {
    parametrageCarteTPDao.updateStatus(id, statut);
  }

  @ContinueSpan(log = "getParametrageCarteTP")
  public ParametrageCarteTP getParametrageCarteTP(TriggeredBeneficiary benef, boolean notManual) {
    // Si le benef n'a pas de paramétrage de carte TP (ano à la création) on
    // recherche un paramétrage existant
    ParametrageCarteTP param = null;
    if (StringUtils.isBlank(benef.getParametrageCarteTPId())) {
      List<GarantieTechnique> gts = extractGTs(benef.getNewContract().getDroitsGaranties());

      ParametrageCarteTP bestParam =
          getBestParametrage(
              benef.getCollectivite(),
              benef.getCollege(),
              benef.getCritereSecondaireDetaille(),
              gts,
              new RequestParametrageCarteTP(benef.getAmc(), true, true, notManual, false));

      if (bestParam != null) {
        param = bestParam;
        benef.setParametrageCarteTPId(param.getId());
      } else {
        String message = "Impossible de trouver un paramétrage de carte TP valide";
        logger.debug(message);
      }
    } else {
      param = parametrageCarteTPDao.getById(benef.getParametrageCarteTPId());
    }
    return param;
  }

  @ContinueSpan(log = "getParametrageToExecute")
  public List<ParametrageCarteTP> getParametrageToExecute(String date, boolean isRdo) {
    return parametrageCarteTPDao.getParametrageToExecute(date, isRdo);
  }

  @ContinueSpan(log = "deleteByAmc ParametrageCarteTP")
  public long deleteByAmc(final String amc) {
    return parametrageCarteTPDao.deleteByAmc(amc);
  }

  @ContinueSpan(log = "getPriorityByAmc")
  public List<Integer> getPriorityByAmc(String amc) {
    return parametrageCarteTPDao.getPriorityByAmc(amc);
  }

  @ContinueSpan(log = "extractBestParametrage")
  public ParametrageCarteTP extractBestParametrage(
      ContratAIV6 contrat, TriggerEmitter triggerEmitter, TriggeredBeneficiary benef) {
    ContratCollectifV6 contratCollectifV6 = contrat.getContratCollectif();
    String identifiantCollectivite = null;
    String groupePopulation = null;

    // If the contract have a contexteTiersPayant...
    if (contratCollectifV6 != null) {
      identifiantCollectivite =
          contratCollectifV6.getIdentifiantCollectivite() != null
              ? contratCollectifV6.getIdentifiantCollectivite()
              : Constants.N_A;
      groupePopulation =
          contratCollectifV6.getGroupePopulation() != null
              ? contratCollectifV6.getGroupePopulation()
              : Constants.N_A;
    }

    List<GarantieTechnique> gts = extractGTs(benef.getNewContract().getDroitsGaranties());

    return getBestParametrage(
        identifiantCollectivite,
        groupePopulation,
        contrat.getCritereSecondaireDetaille(),
        gts,
        new RequestParametrageCarteTP(
            contrat.getIdDeclarant(),
            true,
            true,
            !TriggerEmitter.Request.equals(triggerEmitter),
            false));
  }

  @ContinueSpan(log = "extractBestParametrageForRenouvellement")
  public ParametrageCarteTP extractBestParametrageForRenouvellement(
      ContratAIV6 contrat,
      TriggeredBeneficiary benef,
      List<ParametrageCarteTP> parametrageCarteTPList) {
    ContratCollectifV6 contratCollectifV6 = contrat.getContratCollectif();
    String identifiantCollectivite = null;
    String groupePopulation = null;

    // If the contract have a contexteTiersPayant...
    if (contratCollectifV6 != null) {
      identifiantCollectivite =
          contratCollectifV6.getIdentifiantCollectivite() != null
              ? contratCollectifV6.getIdentifiantCollectivite()
              : Constants.N_A;
      groupePopulation =
          contratCollectifV6.getGroupePopulation() != null
              ? contratCollectifV6.getGroupePopulation()
              : Constants.N_A;
    }

    List<GarantieTechnique> gts = extractGTs(benef.getNewContract().getDroitsGaranties());

    return getBestParametrageRenouvellement(
        identifiantCollectivite,
        groupePopulation,
        contrat.getCritereSecondaireDetaille(),
        gts,
        parametrageCarteTPList);
  }

  @ContinueSpan(log = "getByGuaranteeCodeAndInsurerCode")
  public ParametrageCarteTPResponseDto getByGuaranteeCodeAndInsurerCode(
      String guaranteeCode, String insurerCode) {
    List<ParametrageCarteTP> parametrageCarteTPS =
        parametrageCarteTPDao.findByGuaranteeCodeAndInsurerCode(guaranteeCode, insurerCode);
    List<String> lotIdsAssociesAuxGT =
        lotDao.findByGT(guaranteeCode, insurerCode).stream().map(Lot::getId).toList();
    List<String> lotsAvecParametrage =
        parametrageCarteTPS.stream()
            .flatMap(p -> p.getIdLots().stream())
            .filter(lotIdsAssociesAuxGT::contains)
            .distinct()
            .toList();
    List<Lot> lots = lotDao.getListByIds(lotsAvecParametrage);

    return ParametrageCarteTPResponseDto.builder()
        .parametrageCarteTPS(parametrageCarteTPS)
        .lots(lots)
        .build();
  }

  public List<GarantieTechnique> extractGTs(List<DroitAssure> droits) {
    List<GarantieTechnique> gts = new ArrayList<>();
    for (DroitAssure foundDroit : droits) {
      GarantieTechnique gt = new GarantieTechnique();
      gt.setCodeAssureur(foundDroit.getCodeAssureur());
      gt.setCodeGarantie(foundDroit.getCode());
      gts.add(gt);
    }
    return gts;
  }

  public boolean existParametrageCarteTPActif() {
    return parametrageCarteTPDao.existParametrageCarteTPActif();
  }
}
