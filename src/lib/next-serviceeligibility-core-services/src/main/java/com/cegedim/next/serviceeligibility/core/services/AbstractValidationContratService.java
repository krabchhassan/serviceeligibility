package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.compareDate;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.referential.Referential;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.BusinessSortUtility;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ReferentialGenericException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ValidationException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.IBANValidator;

@Slf4j
public abstract class AbstractValidationContratService extends AbstractValidationService {

  private static final String
      L_INFORMATION_DATA_DESTINATAIRES_RELEVE_PRESTATIONS_NOM_EST_OBLIGATOIRE =
          "l'information data.destinatairesRelevePrestations.nom est obligatoire";

  private static final String IDENTITE_DATE_NAISSANCE = "identite.dateNaissance";
  private static final String DROITS_DATE_ANCIENNETE_GARANTIE = "droits.dateAncienneteGarantie";
  private static final String DROIT_PERIODE_FIN = "droit.periode.fin";
  private static final String DROIT_PERIODE_DEBUT = "droit.periode.debut";
  private static final String DROITS_CARENCES_PERIODE_FIN = "droits.carences.periode.fin";
  private static final String DROITS_CARENCES_PERIODE_DEBUT = "droits.carences.periode.debut";

  // utilisé seulement pour les tests
  @Setter protected boolean controleCorrespondanceBobb;

  @Setter private boolean controleMetierHtp;

  // utilisé pour les TU
  @Setter protected boolean useReferentialValidation;

  private final ContractElementService contractElementService;

  private final ReferentialService referentialService;

  private final EventService eventService;

  public AbstractValidationContratService(
      ContractElementService contractElementService,
      ReferentialService referentialService,
      EventService eventService,
      BeyondPropertiesService beyondPropertiesService) {
    this.contractElementService = contractElementService;
    this.referentialService = referentialService;
    this.eventService = eventService;
    this.controleCorrespondanceBobb =
        beyondPropertiesService
            .getBooleanProperty(CONTROLE_CORRESPONDANCE_BOBB)
            .orElse(Boolean.TRUE);
    controleMetierHtp =
        beyondPropertiesService.getBooleanProperty(CONTROLE_METIER_HTP).orElse(Boolean.TRUE);
    useReferentialValidation =
        beyondPropertiesService.getBooleanProperty(USE_REFERENTIAL_VALIDATION).orElse(Boolean.TRUE);
  }

  @Override
  protected void generateErrorIfBlank(
      String s, String errorString, ContractValidationBean contractValidationBean) {
    if (StringUtils.isBlank(s)) {
      generateErrorAssure(errorString, contractValidationBean);
    }
  }

  @Override
  protected boolean generateErrorIfNull(
      Object s, String errorString, ContractValidationBean contractValidationBean) {
    if (s == null) {
      generateErrorAssure(errorString, contractValidationBean);
      return true;
    }
    return false;
  }

  protected void generateErrorAssure(
      String message, ContractValidationBean contractValidationBean) {
    generateError(
        contractValidationBean,
        new ErrorValidationBean(
            "Pour le contrat n°"
                + contractValidationBean.getNumeroContrat()
                + " de l'adhérent n°"
                + contractValidationBean.getNumeroAdherent()
                + " lié à l'AMC "
                + contractValidationBean.getIdDeclarant()
                + " et l'assuré n°"
                + contractValidationBean.getNumeroPersonne()
                + " : "
                + message,
            Constants.ASSURE,
            contractValidationBean.getNumeroPersonne()));
  }

  protected void generateErrorContrat(
      String message, ContractValidationBean contractValidationBean) {
    generateError(
        contractValidationBean,
        new ErrorValidationBean(
            "Pour le contrat n°"
                + contractValidationBean.getNumeroContrat()
                + " de l'adhérent n°"
                + contractValidationBean.getNumeroAdherent()
                + " lié à l'AMC "
                + contractValidationBean.getIdDeclarant()
                + " : "
                + message,
            Constants.CONTRAT,
            null));
  }

  /**
   * Validation des dates debut et fin d'une liste de Periode
   *
   * @param periodes Liste de périodes à valider
   * @param propertyName Nom du bloc d'information contenant la liste de périodes
   */
  private void validPeriodes(
      List<Periode> periodes,
      String propertyName,
      boolean isAssure,
      ContractValidationBean contractValidationBean) {
    if (periodes != null) {
      for (Periode periode : periodes) {
        isValidDateProperty(
            periode.getDebut(),
            propertyName + PERIODE_DEBUT,
            DateUtils.YYYY_MM_DD,
            false,
            isAssure,
            contractValidationBean);
        isValidDateProperty(
            periode.getFin(),
            propertyName + PERIODE_FIN,
            DateUtils.YYYY_MM_DD,
            false,
            isAssure,
            contractValidationBean);
      }
    }
  }

  private void validPeriodesContratCMUOuvert(
      List<PeriodeContratCMUOuvert> periodes, ContractValidationBean contractValidationBean) {
    if (periodes != null) {
      for (PeriodeContratCMUOuvert periode : periodes) {
        if (periode.getPeriode() != null) {
          validPeriodes(
              List.of(periode.getPeriode()),
              "periodesContratCMUOuvert.periode",
              false,
              contractValidationBean);
        }
      }
    }
  }

  private void validPeriodesSuspension(
      List<PeriodeSuspension> periodes, ContractValidationBean contractValidationBean) {
    String propertyName = "periodesSuspension";
    if (periodes != null) {
      for (PeriodeSuspension item : periodes) {
        Periode periode = item.getPeriode();
        if (periode == null) {
          generateErrorContrat(
              L_INFORMATION + propertyName + PERIODE_EST_OBLIGATOIRE, contractValidationBean);
        } else {
          if (StringUtils.isBlank(item.getPeriode().getDebut())) {
            generateErrorContrat(
                L_INFORMATION + propertyName + PERIODE_DEBUT_EST_OBLIGATOIRE,
                contractValidationBean);
          }
          isValidDateProperty(
              periode.getDebut(),
              propertyName + PERIODE_DEBUT,
              DateUtils.YYYY_MM_DD,
              false,
              false,
              contractValidationBean);
          isValidDateProperty(
              periode.getFin(),
              propertyName + PERIODE_FIN,
              DateUtils.YYYY_MM_DD,
              false,
              false,
              contractValidationBean);
        }
        String typeSuspension = item.getTypeSuspension();
        if (StringUtils.isBlank(typeSuspension)) {
          generateErrorContrat(
              L_INFORMATION2 + propertyName + ".typeSuspension est obligatoire",
              contractValidationBean);
        } else if (!EnumUtils.isValidEnum(TypeSuspension.class, typeSuspension)) {
          generateErrorContrat(
              L_INFORMATION2
                  + propertyName
                  + ".typeSuspension ne contient pas une valeur autorisée",
              contractValidationBean);
        }
      }
    }
  }

  /**
   * Validation du code des périodes début et fin
   *
   * @param periodes Périodes
   * @param propertyName nom de la propriété
   */
  private void validCodePeriodes(
      List<CodePeriode> periodes,
      String propertyName,
      ContractValidationBean contractValidationBean) {
    if (periodes != null) {
      for (CodePeriode item : periodes) {
        if (StringUtils.isBlank(item.getCode())) {
          generateErrorAssure(
              L_INFORMATION + propertyName + ".code est obligatoire", contractValidationBean);
        } else {
          validCodePeriodesValue(propertyName, item, contractValidationBean);
        }
      }
    }
  }

  private void validCodePeriodesValue(
      String propertyName, CodePeriode item, ContractValidationBean contractValidationBean) {
    if (item.getPeriode() != null) {
      if (StringUtils.isBlank(item.getPeriode().getDebut())) {
        generateErrorAssure(
            L_INFORMATION + propertyName + PERIODE_DEBUT_EST_OBLIGATOIRE, contractValidationBean);
      }
      isValidDateProperty(
          item.getPeriode().getDebut(),
          propertyName + PERIODE_DEBUT,
          DateUtils.YYYY_MM_DD,
          false,
          true,
          contractValidationBean);
      isValidDateProperty(
          item.getPeriode().getFin(),
          propertyName + PERIODE_FIN,
          DateUtils.YYYY_MM_DD,
          false,
          true,
          contractValidationBean);
    } else {
      generateErrorAssure(
          L_INFORMATION + propertyName + PERIODE_EST_OBLIGATOIRE, contractValidationBean);
    }
  }

  /**
   * Validation des informations directement liées au contrat
   *
   * @param contratAI Contrat à valider
   */
  private void validateContractInfo(
      ContratAICommun contratAI, ContractValidationBean contractValidationBean) {
    if (contratAI != null) {
      // Contrôle des dates du contrat
      isValidDateProperty(
          contratAI.getDateSouscription(),
          "dateSouscription",
          DateUtils.YYYY_MM_DD,
          false,
          false,
          contractValidationBean);
      isValidDateProperty(
          contratAI.getDateResiliation(),
          "dateResiliation",
          DateUtils.YYYY_MM_DD,
          false,
          false,
          contractValidationBean);
      validPeriodes(
          contratAI.getPeriodesContratResponsableOuvert(),
          "periodesContratResponsableOuvert",
          false,
          contractValidationBean);
      if (contratAI instanceof ContratAIV5 contratAIv5) {
        validPeriodesSuspension(contratAIv5.getPeriodesSuspension(), contractValidationBean);
        validPeriodesContratCMUOuvert(
            contratAIv5.getPeriodesContratCMUOuvert(), contractValidationBean);
      }
      if (contratAI instanceof ContratAIV6 contratAIv6) {
        validPeriodesSuspension(contratAIv6.getPeriodesSuspension(), contractValidationBean);
        validPeriodesContratCMUOuvert(
            contratAIv6.getPeriodesContratCMUOuvert(), contractValidationBean);
      }
    }
  }

  /**
   * Validation des informations concernant l'identité de l'assuré
   *
   * @param assure Assuré à valider
   */
  private void validateIdentiteAssureInfo(
      AssureCommun assure, ContractValidationBean contractValidationBean) {
    IdentiteContrat identite = assure.getIdentite();
    if (identite != null) {
      Nir nir = identite.getNir();
      List<NirRattachementRO> lstAffiliationsRO = identite.getAffiliationsRO();

      if (nir != null
          && StringUtils.isNotBlank(nir.getCode())
          && StringUtils.isNotBlank(nir.getCle())) {
        validationNir(nir.getCode(), nir.getCle(), "identite.nir", true, contractValidationBean);
      }
      validateIdentiteAssureInfoDetails(
          lstAffiliationsRO, "identite.affiliationsRO", true, contractValidationBean);
      isValidDateProperty(
          identite.getDateNaissance(),
          IDENTITE_DATE_NAISSANCE,
          Constants.BIRTH_DATE_FORMAT,
          true,
          true,
          contractValidationBean);
    }
  }

  /**
   * Validation des droits de l'assuré
   *
   * @param assure Assure ayant les droits à valider
   */
  private void validateDroitAssureInfo(
      AssureCommun assure, ContractValidationBean contractValidationBean) {
    if (assure instanceof Assure assureV5) {
      log.debug("Validation des droits assure V5");
      if (!CollectionUtils.isEmpty(assureV5.getDroits())) {
        validateDroitsAssureV3(assureV5.getDroits(), contractValidationBean);
      }
    }
  }

  private void validateDroitsAssureV3(
      List<DroitAssure> droitsAassureV3, ContractValidationBean contractValidationBean) {
    for (DroitAssure droit : droitsAassureV3) {
      if (!CollectionUtils.isEmpty(droit.getCarences())) {
        log.debug("Validation des carences");
        for (CarenceDroit carence : droit.getCarences()) {
          PeriodeCarence periodeCarence = carence.getPeriode();
          if (periodeCarence != null) {
            isValidDateProperty(
                periodeCarence.getDebut(),
                DROITS_CARENCES_PERIODE_DEBUT,
                DateUtils.YYYY_MM_DD,
                false,
                true,
                contractValidationBean);
            isValidDateProperty(
                periodeCarence.getFin(),
                DROITS_CARENCES_PERIODE_FIN,
                DateUtils.YYYY_MM_DD,
                false,
                true,
                contractValidationBean);
            if (carence.getDroitRemplacement() != null
                && controleCorrespondanceBobb
                && isPeriodeDroitValide(
                    new Periode(carence.getPeriode().getDebut(), carence.getPeriode().getFin()))) {
              log.debug("Validation des correspondances Bobb sur les droits de remplacement");
              existBobb(
                  carence.getDroitRemplacement().getCode(),
                  carence.getDroitRemplacement().getCodeAssureur(),
                  contractValidationBean);
            }
          }
        }
      }
      if (controleCorrespondanceBobb && isPeriodeDroitValide(droit.getPeriode())) {
        log.debug("Validation des correspondances Bobb");
        existBobb(droit.getCode(), droit.getCodeAssureur(), contractValidationBean);
      }
    }
  }

  private void validatePeriodeDroit(
      DroitAssureCommun droit, ContractValidationBean contractValidationBean) {
    Periode periode = droit.getPeriode();
    if (periode != null) {
      isValidDateProperty(
          periode.getDebut(),
          DROIT_PERIODE_DEBUT,
          DateUtils.YYYY_MM_DD,
          false,
          true,
          contractValidationBean);
      isValidDateProperty(
          periode.getFin(),
          DROIT_PERIODE_FIN,
          DateUtils.YYYY_MM_DD,
          false,
          true,
          contractValidationBean);
    }
    isValidDateProperty(
        droit.getDateAncienneteGarantie(),
        DROITS_DATE_ANCIENNETE_GARANTIE,
        DateUtils.YYYY_MM_DD,
        false,
        true,
        contractValidationBean);
  }

  /**
   * Validation des informations des assurés d'un contrat
   *
   * @param contratAI Contrat contenant les assurés à valider
   */
  private void validateContractAssuresInfo(
      ContratAICommun contratAI, ContractValidationBean contractValidationBean) {
    if (contratAI != null) {

      List<AssureCommun> assures = getAssuresFromContrat(contratAI);

      if (!CollectionUtils.isEmpty(assures)) {
        for (AssureCommun assure : assures) {
          extractIdentite(assure.getIdentite(), contractValidationBean);

          // Test des NIRS de l'assuré
          validateIdentiteAssureInfo(assure, contractValidationBean);

          // Contrôle des dates de l'assure
          isValidDateProperty(
              assure.getDateAdhesionMutuelle(),
              "dateAdhesionMutuelle",
              DateUtils.YYYY_MM_DD,
              false,
              true,
              contractValidationBean);
          isValidDateProperty(
              assure.getDateDebutAdhesionIndividuelle(),
              "dateDebutAdhesionIndividuelle",
              DateUtils.YYYY_MM_DD,
              false,
              true,
              contractValidationBean);
          isValidDateProperty(
              assure.getDateRadiation(),
              "dateRadiation",
              DateUtils.YYYY_MM_DD,
              false,
              true,
              contractValidationBean);
          validPeriodes(assure.getPeriodes(), "assures.periodes", true, contractValidationBean);
          validPeriodes(
              assure.getPeriodesMedecinTraitantOuvert(),
              "periodesMedecinTraitantOuvert",
              true,
              contractValidationBean);
          validCodePeriodes(
              assure.getRegimesParticuliers(), "regimesParticuliers", contractValidationBean);
          validCodePeriodes(
              assure.getSituationsParticulieres(),
              "situationsParticulieres",
              contractValidationBean);
          validateDroitAssureInfo(assure, contractValidationBean);
        }
      }
    }
  }

  /**
   * Réalisation de contrôles métier et validation d'un contrat
   *
   * @param contratAI Le contrat HTP
   * @throws ValidationContractException * Exception indiquant que le contrat n'est pas valide
   */
  @ContinueSpan(log = "validateMetierHtp")
  public void validateMetierHtp(
      ContratAICommun contratAI, ContractValidationBean contractValidationBean) {
    List<AssureCommun> assureList = getAssuresFromContrat(contratAI);
    if (CollectionUtils.isEmpty(assureList)) {
      generateErrorContrat("L'information assures est obligatoire", contractValidationBean);
    } else {
      onlyOnePrincipalInsured(assureList, contractValidationBean);
      onlyOneSubscriber(assureList, contractValidationBean);
      administrativeRankUnique(assureList, contractValidationBean);
      noOverlappingGuaranteePeriod(assureList, contractValidationBean);
      noOverlappingGuaranteePeriodForSamePersonNumber(assureList, contractValidationBean);
    }
  }

  protected void noOverlappingGuaranteePeriod(
      List<AssureCommun> assureList, ContractValidationBean contractValidationBean) {
    for (AssureCommun assure : assureList) {
      List<DroitAssure> droitAssureList = null;
      if (assure instanceof Assure v5) {
        droitAssureList = v5.getDroits();
      }

      if (droitAssureList != null) {
        extractIdentite(assure.getIdentite(), contractValidationBean);
        checkOverlappingGuaranteePeriods(droitAssureList, contractValidationBean);
      }
    }
  }

  protected void noOverlappingGuaranteePeriodForSamePersonNumber(
      List<AssureCommun> assureList, ContractValidationBean contractValidationBean) {
    Map<String, List<AssureCommun>> assureGrouped =
        assureList.stream()
            .collect(
                Collectors.groupingBy(
                    assureCommun -> {
                      if (assureCommun.getIdentite() != null
                          && StringUtils.isNotBlank(
                              assureCommun.getIdentite().getNumeroPersonne())) {
                        return assureCommun.getIdentite().getNumeroPersonne();
                      }
                      return ContractValidationBean.NON_RENSEIGNE;
                    }));
    assureGrouped.forEach(
        (numeroPersonne, sameAssures) -> {
          if (sameAssures.size() > 1) {
            List<DroitAssure> droitAssureList = new ArrayList<>();
            for (AssureCommun assure : sameAssures) {
              if (assure instanceof Assure v5) {
                droitAssureList.addAll(v5.getDroits());
              }
            }
            if (CollectionUtils.isNotEmpty(droitAssureList)) {
              contractValidationBean.setNumeroPersonne(numeroPersonne);
              checkOverlappingGuaranteePeriods(droitAssureList, contractValidationBean);
            }
          }
        });
  }

  private void checkOverlappingGuaranteePeriods(
      List<DroitAssure> droitAssureList, ContractValidationBean contractValidationBean) {
    for (int droit_i = 0; droit_i < droitAssureList.size() - 1; droit_i++) {
      if (!isPeriodeDroitValide(droitAssureList.get(droit_i).getPeriode())) {
        break;
      }
      for (int droit_j = droit_i + 1; droit_j < droitAssureList.size(); droit_j++) {
        DroitAssure droitAssureI = droitAssureList.get(droit_i);
        DroitAssure droitAssureJ = droitAssureList.get(droit_j);
        if (droitAssureI != null
            && droitAssureJ != null
            && droitAssureI.getCode().equals(droitAssureJ.getCode())
            && droitAssureI.getCodeAssureur().equals(droitAssureJ.getCodeAssureur())
            && isPeriodeDroitValide(droitAssureI.getPeriode())
            && isPeriodeDroitValide(droitAssureJ.getPeriode())) {
          compareAndGenerateError(droitAssureI, droitAssureJ, contractValidationBean);
        }
      }
    }
  }

  private void compareAndGenerateError(
      DroitAssure droitAssureI,
      DroitAssure droitAssureJ,
      ContractValidationBean contractValidationBean) {
    String debut1 = droitAssureI.getPeriode().getDebut();
    String fin1 = droitAssureI.getPeriode().getFin();
    String debut2 = droitAssureJ.getPeriode().getDebut();
    String fin2 = droitAssureJ.getPeriode().getFin();
    if (debut1 != null
        && debut2 != null
        && ((debut2.compareTo(debut1) >= 0
                && (StringUtils.isBlank(fin1) || debut2.compareTo(fin1) <= 0))
            || (debut1.compareTo(debut2) >= 0
                && (StringUtils.isBlank(fin2) || debut1.compareTo(fin2) <= 0)))) {
      generateErrorAssure(
          "Les périodes de garantie pour une même garantie et un même assuré ne doivent pas se chevaucher.",
          contractValidationBean);
    }
  }

  protected void administrativeRankUnique(
      List<AssureCommun> assureList, ContractValidationBean contractValidationBean) {
    if (assureList.stream().map(AssureCommun::getRangAdministratif).distinct().count()
        != assureList.size())
      generateErrorContrat(
          "Le rang administratif doit être unique pour chaque assuré.", contractValidationBean);
  }

  protected void onlyOneSubscriber(
      List<AssureCommun> assureList, ContractValidationBean contractValidationBean) {
    if (assureList.stream().filter(AssureCommun::getIsSouscripteur).count() > 1)
      generateErrorContrat(
          "Il ne peut y avoir qu'un seul souscripteur sur le contrat.", contractValidationBean);
  }

  protected void onlyOnePrincipalInsured(
      List<AssureCommun> assureList, ContractValidationBean contractValidationBean) {
    if (assureList.stream()
            .filter(
                assureCommun ->
                    assureCommun.getQualite() != null
                        && "A".equals(assureCommun.getQualite().getCode()))
            .count()
        > 1)
      generateErrorContrat(
          "Il ne peut y avoir qu'un seul assuré principal sur le contrat.", contractValidationBean);
  }

  @ContinueSpan(log = "validateContrat (1 param)")
  public void validateContrat(
      ContratAICommun contratAI, ContractValidationBean contractValidationBean) {
    validateContrat(contratAI, false, contractValidationBean);
  }

  /**
   * Validation d'un contrat
   *
   * @param contratAI Contrat à valider
   * @throws ValidationException Exception indiquant que le contrat n'est pas valide
   */
  @ContinueSpan(log = "validateContrat (2 params)")
  public void validateContrat(
      ContratAICommun contratAI, boolean isRDO, ContractValidationBean contractValidationBean) {
    fillContractInfoInValidationBean(contratAI, contractValidationBean);

    if (controleMetierHtp) {
      validateMetierHtp(contratAI, contractValidationBean);
    }
    log.debug("Validation du modele du contrat");
    validateModelContrat(contratAI, contractValidationBean);
    log.debug("Validation des informations du contrat");
    validateContractInfo(contratAI, contractValidationBean);
    log.debug("Validation des informations des assures du contrat");
    validateContractAssuresInfo(contratAI, contractValidationBean);

    if (contratAI instanceof ContratAIV5 contratAIV5) {
      controlAndChangeMotifSuspensionV5(contratAIV5);
      ContratAIV6 contratAIV6 = MapperContrat.mapV5toV6(contratAIV5);
      if (useReferentialValidation) {
        log.debug("Validation des informations liees a referential");
        validateContratReferentiel(contratAIV6, isRDO, contractValidationBean);
      }
      log.debug("Appel à Orga");
      callOrga(contratAIV6, contractValidationBean);
    } else if (contratAI instanceof ContratAIV6 contratAIV6) {
      controlAndChangeMotifSuspensionV6(contratAIV6, contractValidationBean);
      if (useReferentialValidation) {
        log.debug("Validation des informations liees a referential");
        validateContratReferentiel(contratAIV6, isRDO, contractValidationBean);
      }
      log.debug("Appel à Orga");
      callOrga(contratAIV6, contractValidationBean);
    }

    if (CollectionUtils.isNotEmpty(contractValidationBean.getErrorValidationBeans())) {
      log.debug("Contrat invalide !!!");

      eventService.sendObservabilityEventContractReceptionInvalid(
          contratAI, contractValidationBean.getErrorValidationBeans().getFirst().getError());

      throw new ValidationContractException(
          contractValidationBean.getErrorValidationBeans(),
          getMessageFromValidationBean(contractValidationBean));
    }
  }

  private void validateContratReferentiel(
      ContratAIV6 contratAI, boolean isRDO, ContractValidationBean contractValidationBean) {
    log.debug("Récupération du référentiel");
    List<Referential> rs;
    try {
      if (isRDO) {
        rs = referentialService.getReferentialRDO(Constants.CONTROLES_SERVICE_PRESTATION);
      } else {
        rs = referentialService.getReferential(Constants.CONTROLES_SERVICE_PRESTATION);
      }
    } catch (ReferentialGenericException e) {
      generateErrorContrat(e.getMessage(), contractValidationBean);
      return;
    }

    log.debug("Debut du contrôle de référentiel");
    validateContratReferentiel(contratAI, rs, contractValidationBean);
  }

  @ContinueSpan(log = "validateContratReferentiel")
  public void validateContratReferentiel(
      ContratAIV6 contratAI, List<Referential> rs, ContractValidationBean contractValidationBean) {
    for (Referential r : rs) {
      try {
        List<String> gets = List.of(r.getCode().split("\\."));
        recursiveDynamicCheck(gets, contratAI, r, contractValidationBean);
      } catch (Exception e) {
        log.debug(e.getMessage());
        log.error("Validation du contrôle {} impossible", r.getCode());

        if (r.getCode().split("\\.")[0].equalsIgnoreCase("assures")) {
          generateErrorAssure(
              L_INFORMATION2 + r.getCode() + " n'a pas pu être validée", contractValidationBean);
        } else {
          generateErrorContrat(
              L_INFORMATION2 + r.getCode() + " n'a pas pu être validée", contractValidationBean);
        }
      }
    }
  }

  private void recursiveDynamicCheck(
      List<String> s, Object o, Referential r, ContractValidationBean contractValidationBean)
      throws Exception {
    if (o == null) {
      return;
    }

    if (o instanceof AssureCommun assureCommun) {
      IdentiteContrat identite = assureCommun.getIdentite();
      extractIdentite(identite, contractValidationBean);
    }

    Method get = o.getClass().getMethod("get" + s.getFirst());
    if (s.size() == 1) {
      Object getValueObject = get.invoke(o);
      boolean isAssure = r.getCode().split("\\.")[0].equalsIgnoreCase("assures");
      if (getValueObject == null || !r.getAuthorizedValues().contains(getValueObject.toString())) {
        if (isAssure) {
          generateErrorAssure(
              L_INFORMATION2
                  + r.getCode()
                  + " n'a pas une valeur comprise dans la liste : "
                  + r.getAuthorizedValues(),
              contractValidationBean);
        } else {
          generateErrorContrat(
              L_INFORMATION2
                  + r.getCode()
                  + " n'a pas une valeur comprise dans la liste : "
                  + r.getAuthorizedValues(),
              contractValidationBean);
        }
      }
    } else {
      Object subObject = get.invoke(o);
      if (subObject instanceof List<?> subObjectList) {
        for (Object sub : subObjectList) {
          recursiveDynamicCheck(s.subList(1, s.size()), sub, r, contractValidationBean);
        }
      } else {
        recursiveDynamicCheck(s.subList(1, s.size()), subObject, r, contractValidationBean);
      }
    }
  }

  private void extractIdentite(
      IdentiteContrat identite, ContractValidationBean contractValidationBean) {
    if (identite != null && StringUtils.isNotBlank(identite.getNumeroPersonne())) {
      contractValidationBean.setNumeroPersonne(identite.getNumeroPersonne());
    } else {
      contractValidationBean.setNumeroPersonne(ContractValidationBean.NON_RENSEIGNE);
    }
  }

  /**
   * Transformation du contrat reçu : - Valorisation des informations manquantes - Tri décroissant
   * des listes contenant un objet periode - Affectation du code Postal
   *
   * @param contratAI Le contrat a transformer
   */
  @ContinueSpan(log = "transformeContrat")
  public void transformeContrat(ContratAICommun contratAI) {
    BusinessSortUtility.triListePeriode(contratAI.getPeriodesContratResponsableOuvert());

    if (contratAI instanceof ContratAIV5 contratAIv5) {
      transformeContratAssures(contratAIv5.getAssures());
      BusinessSortUtility.triListePeriodesSupension(contratAIv5.getPeriodesSuspension());
      BusinessSortUtility.triListePeriodeContratCMUOuvert(
          contratAIv5.getPeriodesContratCMUOuvert());
    } else if (contratAI instanceof ContratAIV6 contratAIv6) {
      transformeContratAssures(contratAIv6.getAssures());
      BusinessSortUtility.triListePeriodesSupension(contratAIv6.getPeriodesSuspension());
      BusinessSortUtility.triListePeriodeContratCMUOuvert(
          contratAIv6.getPeriodesContratCMUOuvert());
    }
  }

  @ContinueSpan(log = "transformeContratAssures")
  public void transformeContratAssures(List<Assure> listeAssures) {
    if (!CollectionUtils.isEmpty(listeAssures)) {
      for (Assure assure : listeAssures) {
        sortCommun(assure);
        if (assure.getData() != null) {
          BusinessSortUtility.triListeDestinatairesPrestationsV4(
              assure.getData().getDestinatairesPaiements());
          BusinessSortUtility.triListeDestinatairesRelevePrestationsV5(
              assure.getData().getDestinatairesRelevePrestations());
        }
        transformeContratDigitRelation(assure);

        transformeContratAdresse(assure);

        if (!CollectionUtils.isEmpty(assure.getDroits())) {
          assure
              .getDroits()
              .sort(
                  (p1, p2) -> BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode()));
        }
      }
    }
  }

  private void transformeContratDigitRelation(Assure assure) {
    transformeContratDigitRelationCommun(assure.getDroits(), assure.getDigitRelation());
  }

  private void extractCodePostal(AdresseAssure adr) {
    if (adr != null
        && StringUtils.isNotBlank(adr.getLigne6())
        && StringUtils.isBlank(adr.getCodePostal())) {
      String codePostal = StringUtils.substring(adr.getLigne6(), 0, 6).strip();
      if (StringUtils.isNumeric(codePostal)) {
        adr.setCodePostal(codePostal);
      }
    }
  }

  private void transformeContratAdresse(Assure assure) {
    if (!CollectionUtils.isEmpty(assure.getData().getDestinatairesRelevePrestations())) {
      for (DestinataireRelevePrestations dest :
          assure.getData().getDestinatairesRelevePrestations()) {
        extractCodePostal(dest.getAdresse());
      }
    }
  }

  private void sortCommun(AssureCommun assure) {
    BusinessSortUtility.triListePeriode(assure.getPeriodes());
    BusinessSortUtility.triListePeriode(assure.getPeriodesMedecinTraitantOuvert());
    BusinessSortUtility.triListeCodePeriode(assure.getRegimesParticuliers());
    BusinessSortUtility.triListeCodePeriode(assure.getSituationsParticulieres());
    if (assure.getIdentite() != null) {
      BusinessSortUtility.triListeAffiliationRO(assure.getIdentite().getAffiliationsRO());
    }
  }

  private void transformeContratDigitRelationCommun(List<DroitAssure> droits, DigitRelation digit) {
    if (digit != null) {
      BusinessSortUtility.triListeTeletransmissions(digit.getTeletransmissions());
    }
    if (!CollectionUtils.isEmpty(droits)) {
      for (DroitAssure droit : droits) {
        List<CarenceDroit> carences = droit.getCarences();
        // La date de début de carence est obligatoire en
        // V3, on ne l'affecte plus avec la date de début de
        // droit
        BusinessSortUtility.triListeCarences(carences);
      }
    }
  }

  /**
   * Validation du modele de contrat V1
   *
   * @param contrat Le contrat à valider
   */
  private void validateModelContrat(
      ContratAICommun contrat, ContractValidationBean contractValidationBean) {
    if (contrat == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean("Le contrat est obligatoire", Constants.CONTRAT, ""));
    } else {
      validateModelContratPart1(contrat, contractValidationBean);
      validateModelContratPart2(contrat, contractValidationBean);
      validateModelContratCollectif(contrat, contractValidationBean);

      PeriodesDroitsCarte periode = null;
      String dateRestitution = null;
      if (contrat instanceof ContratAIV5 contratV5) {
        ContexteTP contexteTP = contratV5.getContexteTiersPayant();
        if (contexteTP != null) {
          periode = contexteTP.getPeriodesDroitsCarte();
          dateRestitution = contexteTP.getDateRestitutionCarte();
        }
      } else {
        ContratAIV6 contratV6 = (ContratAIV6) contrat;
        ContexteTPV6 contexteTP = contratV6.getContexteTiersPayant();
        if (contexteTP != null) {
          periode = contexteTP.getPeriodesDroitsCarte();
          dateRestitution = contexteTP.getDateRestitutionCarte();
        }
      }
      validatePeriodesDroitsCarte(periode, contractValidationBean);
      if (StringUtils.isNotBlank(dateRestitution)) {
        isValidDateProperty(
            dateRestitution,
            "contexteTiersPayant.dateRestitutionCarte",
            DateUtils.YYYY_MM_DD,
            false,
            false,
            contractValidationBean);
      }
      validateModelAssure(contrat, contractValidationBean);
    }
  }

  private void validatePeriodesDroitsCarte(
      PeriodesDroitsCarte periode, ContractValidationBean contractValidationBean) {
    if (periode != null) {
      if (StringUtils.isBlank(periode.getDebut())) {
        generateErrorContrat(
            "L'information contexteTiersPayant.periodesDroitsCarte.debut est obligatoire",
            contractValidationBean);
      }
      if (StringUtils.isBlank(periode.getFin())) {
        generateErrorContrat(
            "L'information contexteTiersPayant.periodesDroitsCarte.fin est obligatoire",
            contractValidationBean);
      }
      isValidDateProperty(
          periode.getDebut(),
          "contexteTiersPayant.periodesDroitsCarte.debut",
          DateUtils.YYYY_MM_DD,
          false,
          false,
          contractValidationBean);
      isValidDateProperty(
          periode.getFin(),
          "contexteTiersPayant.periodesDroitsCarte.fin",
          DateUtils.YYYY_MM_DD,
          false,
          false,
          contractValidationBean);
    }
  }

  private void validateModelContratCollectif(
      ContratAICommun contrat, ContractValidationBean contractValidationBean) {

    if (contrat instanceof ContratAIV5 contratAIv5) {
      ContratCollectif contratCollectif = contratAIv5.getContratCollectif();
      if (contratCollectif != null && StringUtils.isBlank(contratCollectif.getNumero())) {
        generateErrorContrat(
            "L'information contratCollectif.numero est obligatoire", contractValidationBean);
      }
    } else if (contrat instanceof ContratAIV6 contratAIv6) {
      ContratCollectifV6 contratCollectif = contratAIv6.getContratCollectif();
      if (contratCollectif != null) {
        validateContratCollectifV6(contratCollectif, contractValidationBean);
      }
    }
  }

  private void validateContratCollectifV6(
      ContratCollectifV6 contratCollectif, ContractValidationBean contractValidationBean) {
    if (StringUtils.isBlank(contratCollectif.getNumero())) {
      generateErrorContrat(
          "L'information contratCollectif.numero est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contratCollectif.getIdentifiantCollectivite())) {
      generateErrorContrat(
          "L'information contratCollectif.identifiantCollectivite est obligatoire",
          contractValidationBean);
    }
    if (StringUtils.isBlank(contratCollectif.getRaisonSociale())) {
      generateErrorContrat(
          "L'information contratCollectif.raisonSociale est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contratCollectif.getGroupePopulation())) {
      generateErrorContrat(
          "L'information contratCollectif.groupePopulation est obligatoire",
          contractValidationBean);
    }
  }

  private void validateModelContratPart2(
      ContratAICommun contrat, ContractValidationBean contractValidationBean) {
    if (StringUtils.isBlank(contrat.getQualification())) {
      generateErrorContrat("L'information qualification est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contrat.getOrdrePriorisation())) {
      generateErrorContrat(
          "L'information ordrePriorisation est obligatoire", contractValidationBean);
    }
    validatePeriodesContratResponsableOuvert(contrat, contractValidationBean);
    validatePeriodesContratCMUOuvert(contrat, contractValidationBean);
  }

  private void validatePeriodesContratCMUOuvert(
      ContratAICommun contrat, ContractValidationBean contractValidationBean) {
    List<PeriodeContratCMUOuvert> periodes = new ArrayList<>();
    if (contrat instanceof ContratAIV5 contratAIv5) {
      periodes = contratAIv5.getPeriodesContratCMUOuvert();
    } else if (contrat instanceof ContratAIV6 contratAIv6) {
      periodes = contratAIv6.getPeriodesContratCMUOuvert();
    }

    if (!CollectionUtils.isEmpty(periodes)) {
      for (PeriodeContratCMUOuvert periode : periodes) {
        if (StringUtils.isBlank(periode.getCode())) {
          generateErrorContrat(
              "L'information periodesContratCMUOuvert.code est obligatoire",
              contractValidationBean);
        }
        if (periode.getPeriode() == null || StringUtils.isBlank(periode.getPeriode().getDebut())) {
          generateErrorContrat(
              "L'information periodesContratCMUOuvert.periode.debut est obligatoire",
              contractValidationBean);
        }
      }
    }
  }

  private void validatePeriodesContratResponsableOuvert(
      ContratAICommun contrat, ContractValidationBean contractValidationBean) {
    if (!CollectionUtils.isEmpty(contrat.getPeriodesContratResponsableOuvert())) {
      List<Periode> periodes = contrat.getPeriodesContratResponsableOuvert();
      for (Periode periode : periodes) {
        if (StringUtils.isBlank(periode.getDebut())) {
          generateErrorContrat(
              "L'information periodesContratResponsableOuvert.debut est obligatoire",
              contractValidationBean);
        }
      }
    }
  }

  private void validateModelContratPart1(
      ContratAICommun contrat, ContractValidationBean contractValidationBean) {
    if (StringUtils.isBlank(contrat.getIdDeclarant())) {
      generateErrorContrat("L'information idDeclarant est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contrat.getSocieteEmettrice())) {
      generateErrorContrat(
          "L'information societeEmettrice est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contrat.getNumero())) {
      generateErrorContrat("L'information numero est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contrat.getNumeroAdherent())) {
      generateErrorContrat("L'information numeroAdherent est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contrat.getDateSouscription())) {
      generateErrorContrat(
          "L'information dateSouscription est obligatoire", contractValidationBean);
    }
    if (contrat.getIsContratIndividuel() == null) {
      generateErrorContrat(
          "L'information isContratIndividuel est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(contrat.getGestionnaire())) {
      generateErrorContrat("L'information gestionnaire est obligatoire", contractValidationBean);
    }
  }

  /**
   * Validation du model assure quelque soit la version du contrat
   *
   * @param contratAI contract
   */
  private void validateModelAssure(
      ContratAICommun contratAI, ContractValidationBean contractValidationBean) {
    List<AssureCommun> assures = getAssuresFromContrat(contratAI);
    if (CollectionUtils.isEmpty(assures)) {
      generateErrorContrat("L'information assures est obligatoire", contractValidationBean);
    } else {
      for (AssureCommun assure : assures) {

        IdentiteContrat identite = assure.getIdentite();
        extractIdentite(identite, contractValidationBean);

        validateModelAssurePart1(assure, identite, contractValidationBean);

        validateModelAssurePart3(assure, contractValidationBean);

        QualiteAssure qualite = assure.getQualite();
        validateModelAssurePart4(qualite, contractValidationBean);

        List<DroitAssureCommun> droits = new ArrayList<>();
        validateModelAssurePart5(assure, droits);
        validateModelDroitsAssure(droits, contractValidationBean);
        validateModelAssureDigitRelation(assure, contractValidationBean);
      }
    }
  }

  private void validateModelAssurePart5(AssureCommun assure, List<DroitAssureCommun> droits) {
    if (assure instanceof Assure assureV5) {
      List<DroitAssure> droitsV3 = assureV5.getDroits();
      if (!CollectionUtils.isEmpty(droitsV3)) {
        droits.addAll(droitsV3);
      }
    }
  }

  private void validateModelAssurePart4(
      QualiteAssure qualite, ContractValidationBean contractValidationBean) {
    if (qualite == null) {
      generateErrorAssure("l'information qualite est obligatoire", contractValidationBean);
    } else {
      generateErrorIfBlank(
          qualite.getCode(), "l'information qualite.code est obligatoire", contractValidationBean);
      generateErrorIfBlank(
          qualite.getLibelle(),
          "l'information qualite.libelle est obligatoire",
          contractValidationBean);
    }
  }

  private void validateModelAssurePart3(
      AssureCommun assure, ContractValidationBean contractValidationBean) {
    List<Periode> periodes = assure.getPeriodes();
    if (CollectionUtils.isEmpty(periodes)) {
      generateErrorAssure("l'information periodes est obligatoire", contractValidationBean);
    } else {
      for (Periode periode : periodes) {
        generateErrorIfBlank(
            periode.getDebut(),
            "l'information periodes.debut est obligatoire",
            contractValidationBean);
      }
    }

    List<Periode> periodesMedecin = assure.getPeriodesMedecinTraitantOuvert();
    if (!CollectionUtils.isEmpty(periodesMedecin)) {
      for (Periode periode : periodesMedecin) {
        generateErrorIfBlank(
            periode.getDebut(),
            "l'information periodesMedecinTraitantOuvert.debut est obligatoire",
            contractValidationBean);
      }
    }
  }

  private void validateModelAssurePart1(
      AssureCommun assure,
      IdentiteContrat identite,
      ContractValidationBean contractValidationBean) {
    if (assure.getIsSouscripteur() == null) {
      generateErrorAssure("l'information isSouscripteur est obligatoire", contractValidationBean);
    }
    generateErrorIfBlank(
        assure.getRangAdministratif(),
        "l'information rangAdministratif est obligatoire",
        contractValidationBean);
    generateErrorIfBlank(
        assure.getDateAdhesionMutuelle(),
        "l'information dateAdhesionMutuelle est obligatoire",
        contractValidationBean);

    validateModelIdentiteAssure(identite, contractValidationBean);
    if (assure instanceof Assure assureV5) {
      validateModelDataAssure(assureV5.getData(), contractValidationBean);
    }
  }

  private void validateModelAssureDigitRelation(
      AssureCommun assure, ContractValidationBean contractValidationBean) {
    if (assure instanceof Assure assureV5) {
      DigitRelation digitRelation = assureV5.getDigitRelation();
      if (digitRelation == null) {
        generateErrorAssure("l'information digitRelation est obligatoire", contractValidationBean);
      } else {
        validateTeletransmissions(contractValidationBean, digitRelation);
      }
    }
  }

  private void validateTeletransmissions(
      ContractValidationBean contractValidationBean, DigitRelation digitRelation) {
    String debutMessage = "l'information digitRelation.teletransmissions";
    if (digitRelation.getTeletransmissions() == null) {
      generateErrorAssure(debutMessage + " est obligatoire", contractValidationBean);
    } else if (digitRelation.getTeletransmissions().isEmpty()) {
      generateErrorAssure(debutMessage + PERIODE_EST_OBLIGATOIRE, contractValidationBean);
      generateErrorAssure(
          debutMessage + ".isTeletransmission est obligatoire", contractValidationBean);
    } else {
      for (Teletransmission teletransmission : digitRelation.getTeletransmissions()) {
        if (teletransmission.getPeriode() == null) {
          generateErrorAssure(debutMessage + PERIODE_EST_OBLIGATOIRE, contractValidationBean);
        } else {
          generateErrorIfBlank(
              teletransmission.getPeriode().getDebut(),
              debutMessage + PERIODE_DEBUT_EST_OBLIGATOIRE,
              contractValidationBean);
        }
        if (teletransmission.getIsTeletransmission() == null) {
          generateErrorAssure(
              debutMessage + ".isTeletransmission est obligatoire", contractValidationBean);
        }
      }
    }
  }

  /**
   * Methode retournant une liste d'assurés
   *
   * @param contratAI le contrat
   * @return la liste d'assurés
   */
  private List<AssureCommun> getAssuresFromContrat(ContratAICommun contratAI) {
    List<AssureCommun> assures = new ArrayList<>();
    if (contratAI != null) {
      if (contratAI instanceof ContratAIV5) {
        getAssuresFromContratV5(contratAI, assures);
      } else if (contratAI instanceof ContratAIV6) {
        getAssuresFromContratV6(contratAI, assures);
      }
    }
    return assures;
  }

  private void getAssuresFromContratV6(ContratAICommun contratAI, List<AssureCommun> assures) {
    ContratAIV6 contrat = (ContratAIV6) contratAI;
    if (!CollectionUtils.isEmpty(contrat.getAssures())) {
      assures.addAll(contrat.getAssures());
    }
  }

  private void getAssuresFromContratV5(ContratAICommun contratAI, List<AssureCommun> assures) {
    ContratAIV5 contrat = (ContratAIV5) contratAI;
    if (!CollectionUtils.isEmpty(contrat.getAssures())) {
      assures.addAll(contrat.getAssures());
    }
  }

  /**
   * Validation de l'identite de l'assuré
   *
   * @param identite Objet à valider
   */
  private void validateModelIdentiteAssure(
      IdentiteContrat identite, ContractValidationBean contractValidationBean) {
    if (identite == null) {
      generateErrorAssure("l'information identite est obligatoire", contractValidationBean);
    } else {
      validateModelIdentiteAssurePart1(identite, contractValidationBean);

      validateModelIdentiteAssureNir(identite, contractValidationBean);
      List<NirRattachementRO> affiliationsRO = identite.getAffiliationsRO();
      if (!CollectionUtils.isEmpty(affiliationsRO)) {
        for (NirRattachementRO rattRO : affiliationsRO) {
          Nir nirRatt = rattRO.getNir();
          validateModelIdentiteAssureNirRO(
              rattRO, nirRatt, "l'information identite.affiliationsRO", contractValidationBean);
        }
      }
    }
  }

  private void validateModelIdentiteAssureNir(
      IdentiteContrat identite, ContractValidationBean contractValidationBean) {
    Nir nir = identite.getNir();
    if (nir != null) {
      generateErrorIfBlank(
          nir.getCode(), "l'information identite.nir.code est obligatoire", contractValidationBean);
      generateErrorIfBlank(
          nir.getCle(), "l'information identite.nir.cle est obligatoire", contractValidationBean);
    }
  }

  private void validateModelIdentiteAssurePart1(
      IdentiteContrat identite, ContractValidationBean contractValidationBean) {
    generateErrorIfBlank(
        identite.getDateNaissance(),
        "l'information identite.dateNaissance est obligatoire",
        contractValidationBean);
    generateErrorIfBlank(
        identite.getRangNaissance(),
        "l'information identite.rangNaissance est obligatoire",
        contractValidationBean);
    generateErrorIfBlank(
        identite.getNumeroPersonne(),
        "l'information identite.numeroPersonne est obligatoire",
        contractValidationBean);
    if (identite.getNir() == null && CollectionUtils.isEmpty(identite.getAffiliationsRO())) {
      generateErrorAssure("doit posséder au moins un nir", contractValidationBean);
    }
  }

  /**
   * Validation des données DataAssureV1 de l'assuré
   *
   * @param dataAssure Objet à valider
   */
  private void validateModelDataAssure(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    if (dataAssure == null) {
      generateErrorAssure("l'information data est obligatoire", contractValidationBean);
    } else {
      validateModelDataAssureValidateNom(dataAssure, contractValidationBean);

      AdresseAssure adresse = dataAssure.getAdresse();
      if (adresse == null) {
        generateErrorAssure("l'information data.adresse est obligatoire", contractValidationBean);
      }

      if (dataAssure instanceof DataAssure) {
        validateModelDataAssureV5(dataAssure, contractValidationBean);
      }
    }
  }

  private void validateDestinatairePrestation(
      NomDestinataire nomDestPrest,
      PeriodeDestinataire periode,
      ContractValidationBean contractValidationBean) {
    if (nomDestPrest == null) {
      generateErrorAssure(
          L_INFORMATION_DATA_DESTINATAIRES_RELEVE_PRESTATIONS_NOM_EST_OBLIGATOIRE,
          contractValidationBean);
    } else {
      String isValidNom = nomDestPrest.validateNomDestinataire();
      if (isValidNom != null) {
        generateErrorAssure(isValidNom, contractValidationBean);
      }
    }

    validatePeriodeDestinataireRelevePrestation(periode, contractValidationBean);
  }

  private void validateModelDataAssureV5(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    DataAssure dataAssureV5 = (DataAssure) dataAssure;
    List<DestinatairePrestations> destPrests = dataAssureV5.getDestinatairesPaiements();
    validateModelDataAssureV4Part1(destPrests, contractValidationBean);

    List<DestinataireRelevePrestations> destRelevePrests =
        dataAssureV5.getDestinatairesRelevePrestations();
    if (!CollectionUtils.isEmpty(destRelevePrests)) {
      for (DestinataireRelevePrestations destRelevePrest : destRelevePrests) {
        if (StringUtils.isEmpty(destRelevePrest.getIdDestinataireRelevePrestations())) {
          generateErrorAssure(
              "l'information data.destinatairesRelevePrestations.idDestinataireRelevePrestations est obligatoire",
              contractValidationBean);
        }

        NomDestinataire nomDestPrest = destRelevePrest.getNom();
        validateDestinatairePrestation(
            nomDestPrest, destRelevePrest.getPeriode(), contractValidationBean);

        Dematerialisation demat = destRelevePrest.getDematerialisation();
        if (demat == null) {
          generateErrorAssure(
              "l'information data.destinatairesRelevePrestations.dematerialisation est obligatoire",
              contractValidationBean);
        } else if (Boolean.TRUE.equals(demat.getIsDematerialise())
            && StringUtils.isBlank(demat.getEmail())
            && StringUtils.isBlank(demat.getMobile())) {
          generateErrorAssure(
              "l'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire",
              contractValidationBean);
        }
      }
    }
  }

  private void validateModelDataAssureV4Part1(
      List<DestinatairePrestations> destPrests, ContractValidationBean contractValidationBean) {
    if (!CollectionUtils.isEmpty(destPrests)) {
      for (DestinatairePrestations destPrest : destPrests) {

        if (StringUtils.isEmpty(destPrest.getIdDestinatairePaiements())) {
          generateErrorAssure(
              "l'information data.destinatairesPaiements.idDestinatairePaiements est obligatoire",
              contractValidationBean);
        }
        NomDestinataire nomDestPrest = destPrest.getNom();
        if (nomDestPrest == null) {
          generateErrorAssure(
              "l'information data.destinatairesPaiements.nom est obligatoire",
              contractValidationBean);
        } else {
          String isValidNom = nomDestPrest.validateNomDestinataire();
          if (isValidNom != null) {
            generateErrorAssure(isValidNom, contractValidationBean);
          }
        }

        RibAssure rib = destPrest.getRib();
        validateRibDestinatairePrestation(rib, contractValidationBean);

        ModePaiement modePaiementPrestations = destPrest.getModePaiementPrestations();
        validateModePaiementDestinatairePrestation(modePaiementPrestations, contractValidationBean);

        PeriodeDestinataire periodeDestPrest = destPrest.getPeriode();
        validatePeriodeDestinatairePrestation(periodeDestPrest, contractValidationBean);
      }
    }
  }

  private void validateModelDataAssureValidateNom(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    NomAssure nom = dataAssure.getNom();
    if (nom == null) {
      generateErrorAssure("l'information data.nom est obligatoire", contractValidationBean);
    } else {
      if (StringUtils.isBlank(nom.getNomFamille())) {
        generateErrorAssure(
            "l'information data.nom.nomFamille est obligatoire", contractValidationBean);
      }
      if (StringUtils.isBlank(nom.getPrenom())) {
        generateErrorAssure(
            "l'information data.nom.prenom est obligatoire", contractValidationBean);
      }
      if (StringUtils.isBlank(nom.getCivilite())) {
        generateErrorAssure(
            "l'information data.nom.civilite est obligatoire", contractValidationBean);
      }
    }
  }

  private void validatePeriodeDestinataireRelevePrestation(
      PeriodeDestinataire periodeDestPrest, ContractValidationBean contractValidationBean) {
    if (periodeDestPrest == null) {
      generateErrorAssure(
          "l'information data.destinatairesRelevePrestations.periode est obligatoire",
          contractValidationBean);
    } else {
      if (StringUtils.isBlank(periodeDestPrest.getDebut())) {
        generateErrorAssure(
            "l'information data.destinatairesRelevePrestations.periode.debut est obligatoire",
            contractValidationBean);
      }
    }
  }

  private void validatePeriodeDestinatairePrestation(
      PeriodeDestinataire periodeDestPrest, ContractValidationBean contractValidationBean) {
    String path = DATA_DESTINATAIRES_PAIEMENTS;
    if (periodeDestPrest == null) {
      generateErrorAssure(L_INFORMATION + path + PERIODE_EST_OBLIGATOIRE, contractValidationBean);
    } else {
      if (StringUtils.isBlank(periodeDestPrest.getDebut())) {
        generateErrorAssure(
            L_INFORMATION + path + PERIODE_DEBUT_EST_OBLIGATOIRE, contractValidationBean);
      }
    }
  }

  private void validateModePaiementDestinatairePrestation(
      ModePaiement modePaiementPrestations, ContractValidationBean contractValidationBean) {
    String path = DATA_DESTINATAIRES_PRESTATIONS_V4;
    if (modePaiementPrestations == null) {
      generateErrorAssure(
          L_INFORMATION + path + ".modePaiementPrestations est obligatoire",
          contractValidationBean);
    } else {
      if (StringUtils.isBlank(modePaiementPrestations.getCode())) {
        generateErrorAssure(
            L_INFORMATION + path + ".modePaiementPrestations.code est obligatoire",
            contractValidationBean);
      }
      if (StringUtils.isBlank(modePaiementPrestations.getLibelle())) {
        generateErrorAssure(
            L_INFORMATION + path + ".modePaiementPrestations.libelle est obligatoire",
            contractValidationBean);
      }
      if (StringUtils.isBlank(modePaiementPrestations.getCodeMonnaie())) {
        generateErrorAssure(
            L_INFORMATION + path + ".modePaiementPrestations.codeMonnaie est obligatoire",
            contractValidationBean);
      }
    }
  }

  private void validateRibDestinatairePrestation(
      RibAssure rib, ContractValidationBean contractValidationBean) {
    String path = DATA_DESTINATAIRES_PRESTATIONS_V4;
    if (rib != null) {
      String iban = rib.getIban();
      String bic = rib.getBic();

      if (StringUtils.isBlank(iban) && StringUtils.isNotBlank(bic)) {
        generateErrorAssure(
            L_INFORMATION + path + ".rib.iban est obligatoire", contractValidationBean);
      } else if (StringUtils.isNotBlank(iban) && StringUtils.isBlank(bic)) {
        generateErrorAssure(L_INFORMATION + path + ".rib.bic obligatoire.", contractValidationBean);
      } else if (StringUtils.isNotBlank(iban) && StringUtils.isNotBlank(bic)) {
        if (iban.length() < 14 || iban.length() > 34) {
          generateErrorAssure(
              "la longueur de l'IBAN (" + iban + ") doit être comprise entre 14 et 34",
              contractValidationBean);
        } else if (!IBANValidator.getInstance().isValid(iban)) {
          generateErrorAssure("l'IBAN (" + iban + ") n'est pas valide.", contractValidationBean);
        } else if (!bic.matches(
            "^([A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{0,3})?)$")) { // NOSONAR : empty string is
          // accepted
          generateErrorAssure("le BIC (" + bic + ") n'est pas conforme.", contractValidationBean);
        }
      }
    }
  }

  /**
   * Validation des données DroitAssureV1 de l'assuré
   *
   * @param droits Liste des droits à valider
   */
  private void validateModelDroitsAssure(
      List<DroitAssureCommun> droits, ContractValidationBean contractValidationBean) {
    if (CollectionUtils.isEmpty(droits)) {
      generateErrorAssure("l'information droits est obligatoire", contractValidationBean);
    } else {
      for (DroitAssureCommun droit : droits) {
        validateModelDroitsAssurePart1(droit, contractValidationBean);
        Periode periode = droit.getPeriode();
        if (!generateErrorIfNull(
            periode, "l'information droits.periode est obligatoire", contractValidationBean)) {
          generateErrorIfNull(
              periode.getDebut(),
              "l'information droits.periode.debut est obligatoire",
              contractValidationBean);
        }
        validatePeriodeDroit(droit, contractValidationBean);
        generateErrorIfBlank(
            droit.getDateAncienneteGarantie(),
            "l'information droits.dateAncienneteGarantie est obligatoire",
            contractValidationBean);

        if (droit instanceof DroitAssure) {
          validateModelDroitsAssureV3(droit, contractValidationBean);
        }
      }
    }
  }

  private boolean isPeriodeDroitValide(Periode periode) {
    if (periode == null) {
      return false;
    }

    return periode.getDebut() != null
        && compareDate(periode.getDebut(), periode.getFin(), DateUtils.FORMATTER) <= 0;
  }

  private void validateModelDroitsAssureV3(
      DroitAssureCommun droit, ContractValidationBean contractValidationBean) {
    DroitAssure droitV3 = (DroitAssure) droit;
    List<CarenceDroit> carences = droitV3.getCarences();
    if (!CollectionUtils.isEmpty(carences)) {
      for (CarenceDroit carence : carences) {
        generateErrorIfBlank(
            carence.getCode(),
            "l'information droits.carences.code est obligatoire",
            contractValidationBean);

        if (carence.getPeriode() == null) {
          generateErrorAssure(
              "l'information droits.carences.periode est obligatoire", contractValidationBean);
        } else if (carence.getPeriode().getDebut() == null) {
          generateErrorAssure(
              "l'information droits.carences.periode.debut est obligatoire",
              contractValidationBean);
        } else if (carence.getPeriode().getFin() == null) {
          generateErrorAssure(
              "l'information droits.carences.periode.fin est obligatoire", contractValidationBean);
        }
        DroitRemplacement droitRemplacement = carence.getDroitRemplacement();
        if (droitRemplacement != null) {
          generateErrorIfBlank(
              droitRemplacement.getCode(),
              "l'information droits.carences.droitRemplacement.code est obligatoire",
              contractValidationBean);
          generateErrorIfBlank(
              droitRemplacement.getCodeAssureur(),
              "l'information droits.carences.droitRemplacement.codeAssureur est obligatoire",
              contractValidationBean);
          generateErrorIfBlank(
              droitRemplacement.getLibelle(),
              "l'information droits.carences.droitRemplacement.libelle est obligatoire",
              contractValidationBean);
        }
      }
    }
  }

  private void validateModelDroitsAssurePart1(
      DroitAssureCommun droit, ContractValidationBean contractValidationBean) {
    if (StringUtils.isBlank(droit.getCode())) {
      generateErrorAssure("l'information droits.code est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(droit.getCodeAssureur())) {
      generateErrorAssure(
          "l'information droits.codeAssureur est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(droit.getLibelle())) {
      generateErrorAssure("l'information droits.libelle est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(droit.getOrdrePriorisation())) {
      generateErrorAssure(
          "l'information droits.ordrePriorisation est obligatoire", contractValidationBean);
    }
    if (StringUtils.isBlank(droit.getType())) {
      generateErrorAssure("l'information droits.type est obligatoire", contractValidationBean);
    }
  }

  public abstract void callOrga(
      ContratAIV6 contract, ContractValidationBean contractValidationBean);

  protected static void fillContractInfoInValidationBean(
      ContratAICommun contract, ContractValidationBean contractValidationBean) {
    if (contract != null) {
      if (StringUtils.isNotBlank(contract.getNumero())) {
        contractValidationBean.setNumeroContrat(contract.getNumero());
      }
      if (StringUtils.isNotBlank(contract.getNumeroAdherent())) {
        contractValidationBean.setNumeroAdherent(contract.getNumeroAdherent());
      }
      if (StringUtils.isNotBlank(contract.getIdDeclarant())) {
        contractValidationBean.setIdDeclarant(contract.getIdDeclarant());
      }
    }
  }

  @ContinueSpan(log = "existBobb")
  public void existBobb(
      String code, String codeAssureur, ContractValidationBean contractValidationBean) {
    // include ignored
    ContractElement contractElement = contractElementService.get(code, codeAssureur, true);
    if (contractElement == null) {
      generateErrorAssure(
          "Le produit BO code "
              + code
              + " et assureur "
              + codeAssureur
              + " n'a pas de correspondance dans Beyond",
          contractValidationBean);
    }
  }

  @Override
  protected void validateModelIdentiteAssureNirRO(
      NirRattachementRO rattRO,
      Nir nirRatt,
      String debutMessage,
      ContractValidationBean contractValidationBean) {
    if (nirRatt == null) {
      generateErrorAssure(debutMessage + ".nir est obligatoire", contractValidationBean);
    } else {
      generateErrorIfBlank(
          nirRatt.getCode(), debutMessage + ".nir.code est obligatoire", contractValidationBean);
      generateErrorIfBlank(
          nirRatt.getCle(), debutMessage + ".nir.cle est obligatoire", contractValidationBean);
    }
    Periode periode = rattRO.getPeriode();
    if (periode == null) {
      generateErrorAssure(debutMessage + PERIODE_EST_OBLIGATOIRE, contractValidationBean);
    } else {
      generateErrorIfBlank(
          periode.getDebut(), debutMessage + PERIODE_DEBUT_EST_OBLIGATOIRE, contractValidationBean);
    }
  }

  /**
   * Si le contrôle du référentiel n’est pas activé, et que le motif ne correspond pas aux valeurs
   * possible on force la valeur à NON_PAIEMENT_COTISATIONS. On rejet les contrats dont le motif de
   * suspension est valorisé à PORTABILITE_NON_JUSTIFIEE et dont le type de suspension est
   * Definitif. Le motif de suspension devra désormais être une donnée obligatoire en V6 (reste
   * facultative en V5)
   */
  private void controlAndChangeMotifSuspensionV6(
      ContratAIV6 contratAIV6, ContractValidationBean contractValidationBean) {
    List<PeriodeSuspension> periodeSuspensions =
        Objects.requireNonNullElse(contratAIV6.getPeriodesSuspension(), Collections.emptyList());

    for (PeriodeSuspension periodeSuspension : periodeSuspensions) {
      if (StringUtils.isBlank(periodeSuspension.getMotifSuspension())) {
        generateErrorContrat(
            "L'information periodesSuspension.motifSuspension est obligatoire",
            contractValidationBean);
      }

      if (!Constants.NON_PAIEMENT_COTISATIONS.equals(periodeSuspension.getMotifSuspension())) {
        if (!Constants.PORTABILITE_NON_JUSTIFIEE.equals(periodeSuspension.getMotifSuspension())) {
          if (!useReferentialValidation) {
            periodeSuspension.setMotifSuspension(Constants.NON_PAIEMENT_COTISATIONS);
          }
        } else if (TypeSuspension.Definitif.name().equals(periodeSuspension.getTypeSuspension())) {
          generateErrorContrat(
              "Le motif de suspension "
                  + Constants.PORTABILITE_NON_JUSTIFIEE
                  + " n'est pas un motif valide pour le type de suspension "
                  + TypeSuspension.Definitif.name(),
              contractValidationBean);
        }
      }
    }
  }

  /**
   * Lors de la réception des contrats HTP dans le format V5 (en push-event et en RDO) si le motif
   * de suspension n’est pas dans une des valeurs autorisée par le référentiel
   * NON_PAIEMENT_COTISATIONS, PORTABILITE_NON_JUSTIFIEE. Ou que le motif est valorisé à
   * PORTABILITE_NON_JUSTIFIEE et que le type de suspension est Definitif il faut alors positionner
   * le motif à NON_PAIEMENT_COTISATIONS
   */
  private void controlAndChangeMotifSuspensionV5(ContratAIV5 contratAIV5) {
    List<PeriodeSuspension> periodeSuspensions =
        Objects.requireNonNullElse(contratAIV5.getPeriodesSuspension(), Collections.emptyList());

    for (PeriodeSuspension periodeSuspension : periodeSuspensions) {

      boolean isNomPaiement =
          Constants.NON_PAIEMENT_COTISATIONS.equals(periodeSuspension.getMotifSuspension());
      boolean isPortabiliteNonJustif =
          Constants.PORTABILITE_NON_JUSTIFIEE.equals(periodeSuspension.getMotifSuspension());
      boolean isDefinitif =
          TypeSuspension.Definitif.name().equals(periodeSuspension.getTypeSuspension());

      if (!isNomPaiement && !isPortabiliteNonJustif || isPortabiliteNonJustif && isDefinitif) {
        periodeSuspension.setMotifSuspension(Constants.NON_PAIEMENT_COTISATIONS);
      }
    }
  }
}
