package com.cegedim.next.serviceeligibility.core.services.cartedemat.carte;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.INSURER_SETTINGS_FILE_PATH;
import static java.util.stream.Collectors.groupingBy;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperCartePapier;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.CartesService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.AdresseService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.MapperCartesUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorCartesService {

  private final AdresseService adresseService;

  private final MapperCartePapier mapperCartePapier;

  private final EventService eventService;

  private final S3Service s3Service;

  private final ParametreBddService parametreBddService;

  private final CartesService cartesService;

  private final BeyondPropertiesService beyondPropertiesService;

  public void createCartesDematAndPaper(
      DataForJob620 dataForJob620,
      Declarant declarant,
      List<DeclarationConsolide> consosByContrat,
      BulkActions bulkActions,
      Periode previousSuspensionPeriod) {
    List<CarteDemat> carteDemats =
        processConsosContrat(
            consosByContrat, dataForJob620, declarant, bulkActions, previousSuspensionPeriod);
    if (CollectionUtils.isNotEmpty(carteDemats)) {
      for (CarteDemat carte : carteDemats) {
        log.debug("Creating CarteDEMAT number {}", carte.getAMC_contrat());
        bulkActions.demat(carte);
        if (carte.getCodeServices() == null
            || carte.getCodeServices().contains(Constants.CARTE_DEMATERIALISEE)) {
          bulkActions.event(eventService.prepareObservabilityEventCarteDemat(carte));
        }
      }
    }
  }

  List<CarteDemat> processConsosContrat(
      List<DeclarationConsolide> consoByContrat,
      DataForJob620 dataForJob620,
      Declarant declarant,
      BulkActions bulkActions,
      Periode previousSuspensionPeriod) {
    String minDebut =
        consoByContrat.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .min(String::compareTo)
            .orElse("");

    String dateExec = DateUtils.formatDate(dataForJob620.getToday());
    boolean isInsurer = Constants.CLIENT_TYPE_INSURER.equals(dataForJob620.getClientType());
    List<DeclarationConsolide> consosSplit =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            consoByContrat, minDebut, dateExec, isInsurer, previousSuspensionPeriod);

    if (consosSplit.isEmpty()) {
      log.debug("C18 - No declaration consolidee for carte");
      cartesService.createRejectionsFromDeclarationConsolideeList(
          consoByContrat,
          dataForJob620.getToday(),
          ConstantesRejetsConsolidations.REJET_C18,
          bulkActions);
      return Collections.emptyList();
    }
    List<DeclarationConsolide> invalidConsos =
        consosSplit.stream().filter(conso -> !conso.isDeclarationValide()).toList();

    if (!invalidConsos.isEmpty()) {
      log.debug(
          "C18 - Invalid declaration(s) consolidee(s) : {}",
          invalidConsos.stream()
              .map(DeclarationConsolide::get_id)
              .collect(Collectors.joining(", ")));
      cartesService.createRejectionsFromDeclarationConsolideeList(
          invalidConsos,
          dataForJob620.getToday(),
          ConstantesRejetsConsolidations.REJET_C18,
          bulkActions);

      setC17OnContractOtherDeclarations(
          dataForJob620.getToday(),
          consosSplit,
          invalidConsos,
          bulkActions,
          ConstantesRejetsConsolidations.REJET_C18);
    } else {
      MapperCartesUtils.sortByFinLienFamilialNaissance(consosSplit);
      return createCartes(consosSplit, minDebut, dataForJob620, declarant, bulkActions);
    }
    return Collections.emptyList();
  }

  private void setC17OnContractOtherDeclarations(
      Date today,
      List<DeclarationConsolide> allConsos,
      List<DeclarationConsolide> invalidConsos,
      BulkActions bulkActions,
      ConstantesRejetsConsolidations mainRejet) {
    // on recupere toutes les declarations consolidees du contrat qui a une
    // declaration consolidee en
    // erreur et on envoie un code C17
    List<DeclarationConsolide> declarationConsolideListForARL = new ArrayList<>(allConsos);
    declarationConsolideListForARL.removeAll(invalidConsos);

    if (CollectionUtils.isNotEmpty(declarationConsolideListForARL)) {
      cartesService.createRejectionsFromDeclarationConsolideeList(
          declarationConsolideListForARL,
          today,
          ConstantesRejetsConsolidations.REJET_C17.toString(),
          mainRejet.getService(),
          bulkActions);
    }
  }

  List<CarteDemat> createCartes(
      List<DeclarationConsolide> consosSplit,
      String dateDebut,
      DataForJob620 dataForJob620,
      Declarant declarant,
      BulkActions bulkActions) {
    String maxFin =
        consosSplit.stream()
            .map(DeclarationConsolide::getPeriodeFin)
            .max(String::compareTo)
            .orElse("");
    boolean isCarteAEditer = Strings.isNotBlank(dateDebut) && maxFin.compareTo(dateDebut) > 0;
    if (!isCarteAEditer) {
      DeclarationConsolide rdmAssure = consosSplit.get(0);
      log.info(
          "No card to edit for amc {} and contract {}",
          rdmAssure.getIdDeclarant(),
          rdmAssure.getContrat().getNumero());
    } else {
      consosSplit = MapperCartesUtils.splitAllConsosPeriodesByStartDate(consosSplit, maxFin);
      return generateCartes(consosSplit, dataForJob620, declarant, bulkActions);
    }

    return Collections.emptyList();
  }

  private List<CarteDemat> generateCartes(
      List<DeclarationConsolide> consos,
      DataForJob620 dataForJob620,
      Declarant declarant,
      BulkActions bulkActions) {
    List<CarteDemat> carteDemats = new ArrayList<>();
    JsonNode insurerSettings = getS3InsurerSettings(declarant);
    TreeMap<String, List<DeclarationConsolide>> declarationByStartDate =
        consos.stream()
            .collect(
                groupingBy(
                    DeclarationConsolide::getPeriodeDebut,
                    TreeMap::new,
                    Collectors.toCollection(ArrayList::new)));
    for (Map.Entry<String, List<DeclarationConsolide>> listEntry :
        declarationByStartDate.entrySet()) {
      List<DeclarationConsolide> group = listEntry.getValue();
      completeLibelleDomaines(group);
      CarteDemat carte =
          MapperCartesUtils.createCarteConsolidee(
              group, dataForJob620.getToday(), declarant, bulkActions);
      if (carte != null) {
        carte.setAdresse(adresseService.getAdresseForCarte(group));
        MapperCartesUtils.deleteExcessiveInformations(carte);
        carteDemats.add(carte);
        extractCartePapier(
            carte, bulkActions, insurerSettings, group, dataForJob620.getToday(), declarant);
      }
    }
    return carteDemats;
  }

  private JsonNode getS3InsurerSettings(Declarant declarant) {
    JsonNode insurerSettings = null;
    if (!CollectionUtils.isEmpty(declarant.getPilotages())
        && declarant.getPilotages().stream()
            .anyMatch(
                pilotage ->
                    Constants.CARTE_TP.equals(pilotage.getCodeService())
                        && pilotage.getServiceOuvert())) {
      insurerSettings =
          s3Service.readS3File(
              beyondPropertiesService.getPropertyOrThrowError(INSURER_SETTINGS_FILE_PATH));
    }
    return insurerSettings;
  }

  private void extractCartePapier(
      CarteDemat carteConsolidee,
      BulkActions bulkActions,
      JsonNode insurerSettings,
      List<DeclarationConsolide> consos,
      Date dateExec,
      Declarant declarant) {
    if (carteConsolidee.getCodeServices() != null
        && carteConsolidee.getCodeServices().contains(Constants.CARTE_TP)) {
      if (carteConsolidee.getAdresse() == null
          || !adresseService.checkAdresseExists(carteConsolidee.getAdresse())) {
        cartesService.createRejectionsFromDeclarationConsolideeList(
            consos, dateExec, ConstantesRejetsConsolidations.REJET_C19, bulkActions);
        List<DeclarationConsolide> invalidConsos =
            consos.stream().filter(conso -> !conso.isDeclarationValide()).toList();
        if (!invalidConsos.isEmpty()) {
          setC17OnContractOtherDeclarations(
              dateExec,
              consos,
              invalidConsos,
              bulkActions,
              ConstantesRejetsConsolidations.REJET_C19);
        }
        bulkActions.addTraceExtractionConsos(
            carteConsolidee, ConstantesRejetsConsolidations.REJET_C19.getCode());
        bulkActions.getHistoriqueExecution().incNbCartesPapierKo(1);
      } else {
        if (checkIfAtLeastOneDomainNotNC(carteConsolidee)) {
          createCartePapier(
              carteConsolidee, bulkActions, insurerSettings, consos, dateExec, declarant);
        }
      }
    }
  }

  private void createCartePapier(
      CarteDemat carteConsolidee,
      BulkActions bulkActions,
      JsonNode insurerSettings,
      List<DeclarationConsolide> consos,
      Date dateExec,
      Declarant declarant) {
    CartePapierEditique cartePapierEditique =
        mapperCartePapier.mapCartePapierEditique(
            carteConsolidee, insurerSettings, declarant, bulkActions.getContexte(), dateExec);
    if (cartePapierEditique == null) {
      cartesService.createRejectionsFromDeclarationConsolideeList(
          consos, dateExec, ConstantesRejetsConsolidations.REJET_C23, bulkActions);
      List<DeclarationConsolide> invalidConsos =
          consos.stream().filter(conso -> !conso.isDeclarationValide()).toList();
      if (!invalidConsos.isEmpty()) {
        setC17OnContractOtherDeclarations(
            dateExec, consos, invalidConsos, bulkActions, ConstantesRejetsConsolidations.REJET_C23);
      }
      bulkActions.addTraceExtractionConsos(
          carteConsolidee, ConstantesRejetsConsolidations.REJET_C23.getCode());
    } else {
      bulkActions.papier(cartePapierEditique);
    }
  }

  /**
   * Complete les libelles des domaines dans les declarations consolidees par rapport au parametrage
   * BDDS RDom
   */
  private void completeLibelleDomaines(List<DeclarationConsolide> group) {
    if (CollectionUtils.isNotEmpty(group)) {
      for (DeclarationConsolide conso : group) {
        if (CollectionUtils.isNotEmpty(conso.getDomaineDroits())) {
          for (DomaineDroit domaineDroit : conso.getDomaineDroits()) {
            ParametresDto parametreBdd =
                parametreBddService.findOneByType(Constants.DOMAINE, domaineDroit.getCode());
            if (parametreBdd != null) {
              domaineDroit.setLibelle(parametreBdd.getLibelle());
            }
          }
        }
      }
    }
  }

  /**
   * BLUE-7201 Vérifie si au moins un domaine de la carteDemat à un taux de remboursement différent
   * de NC
   *
   * @return true si au moins un domaine est couvert, faux si tous les domaines sont NC
   */
  private boolean checkIfAtLeastOneDomainNotNC(CarteDemat carte) {
    String regex = "^NC(/NC)*$";
    for (BenefCarteDemat benefCarteDemat : carte.getBeneficiaires()) {
      if (checkTauxDomaines(benefCarteDemat, regex)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Vérifie s'il existe un domaine de la carteDemat qui ne correspond pas à la regex
   *
   * @return true si un domaine ne match pas à la regex, false sinon
   */
  private boolean checkTauxDomaines(BenefCarteDemat benef, String regex) {
    if (CollectionUtils.isNotEmpty(benef.getDomainesRegroup())) {
      for (DomaineCarte domaineCarte : benef.getDomainesRegroup()) {
        if (!Pattern.matches(regex, domaineCarte.getTaux())) {
          return true;
        }
      }
    }
    if (CollectionUtils.isNotEmpty(benef.getDomainesCouverture())) {
      for (DomaineDroit domaineDroit : benef.getDomainesCouverture()) {
        if (!Pattern.matches(regex, domaineDroit.getTauxRemboursement())) {
          return true;
        }
      }
    }
    return false;
  }
}
