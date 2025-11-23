package com.cegedim.next.serviceeligibility.core.features.consultationdroits.interrogationdroitsbenefs;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.*;
import com.cegedim.next.serviceeligibility.core.business.consultationdroits.ConsultationDroitsService;
import com.cegedim.next.serviceeligibility.core.business.consultationdroits.idb.IDBService;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.features.utils.ConsultationDroitsUtils;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreIncorrect;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreSegmentsManquent;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperConsultationDroit;
import com.cegedim.next.serviceeligibility.core.soap.consultation.process.ConsultationBaseDeDroitProcessImpl;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.IDBCompleteResponse;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.IDBResponse;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddRequest;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.tracing.annotation.NewSpan;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IDBController {

  private final RequestValidator requestValidator;
  private final ConsultationBaseDeDroitProcessImpl consultationBaseDeDroitProcess;
  private final MapperIDB mapperIdb;
  private final IDBService idbService;
  private final ConsultationDroitsService consultationDroitsService;
  private final DeclarantService declarantService;

  private final I18NService i18NService = new I18NService();

  @Deprecated
  @NewSpan
  @GetMapping(
      value = "/v1/interrogationDroitsBenefs",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<IDBCompleteResponse> getDroits(@RequestBody final String requete) {
    log.info("Interrogation des droits des bénéficiaires");

    GetInfoBddRequestDto bddRequest;
    try {
      // Custom mapper pour récupérer les erreurs de parsing
      bddRequest = ConsultationDroitsUtils.getRequestBody(requete);
    } catch (JsonProcessingException e) {
      IDBCompleteResponse idbCompleteResponse = creerReponseVide();
      TypeCodeReponse codeReponse = idbCompleteResponse.getCodeReponse();
      codeReponse.setCode(CodeReponse.PARAM_RECHERCHE_INCORRECTS.getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode()));
      log.error("Erreur dans le parametrage de la requete", e);
      return new ResponseEntity<>(idbCompleteResponse, HttpStatus.OK);
    }

    if (bddRequest.getDates() != null) {
      bddRequest.getDates().setDateReference(bddRequest.getDates().getDateDebut());
    }
    requestValidator.validateRequestV4(bddRequest);
    return new ResponseEntity<>(
        getIDBResponse(MapperConsultationDroit.getRequest(bddRequest)), HttpStatus.OK);
  }

  @NewSpan
  @PostMapping(
      value = "/v2/interrogationDroitsBenefs",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<IDBCompleteResponse> getDroitsV2(@RequestBody final String requete) {
    log.info("Interrogation des droits des bénéficiaires");

    GetInfoBddRequestDto bddRequest;
    try {
      // Custom mapper pour récupérer les erreurs de parsing
      bddRequest = ConsultationDroitsUtils.getRequestBody(requete);
    } catch (JsonProcessingException e) {
      IDBCompleteResponse idbCompleteResponse = creerReponseVide();
      TypeCodeReponse codeReponse = idbCompleteResponse.getCodeReponse();
      codeReponse.setCode(CodeReponse.PARAM_RECHERCHE_INCORRECTS.getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode()));
      log.error("Erreur dans le parametrage de la requete", e);
      return new ResponseEntity<>(idbCompleteResponse, HttpStatus.OK);
    }

    if (bddRequest.getDates() != null) {
      bddRequest.getDates().setDateReference(bddRequest.getDates().getDateDebut());
    }
    requestValidator.validateRequestV4(bddRequest);
    return new ResponseEntity<>(
        getIDBResponse(MapperConsultationDroit.getRequest(bddRequest)), HttpStatus.OK);
  }

  private IDBCompleteResponse creerReponseVide() {
    IDBCompleteResponse idbCompleteResponse = new IDBCompleteResponse();
    idbCompleteResponse.setCodeReponse(mapperIdb.createCodeReponseOK());
    return idbCompleteResponse;
  }

  private IDBCompleteResponse getIDBResponse(final GetInfoBddRequest requete) {
    IDBCompleteResponse idbCompleteResponse = creerReponseVide();
    TypeCodeReponse codeReponse = idbCompleteResponse.getCodeReponse();
    try {
      processIDB(requete, idbCompleteResponse);
    } catch (ExceptionMetier e) {
      // Demande speciale du Test FSIQ de changer Libelle du code 6000 en
      // passant par le code 6004 - liste segments recherche vide
      log.debug("Traitement code réponse", e);
      if ("6004".equals(e.getCodeReponse().getCode())) {
        codeReponse.setCode("6000");
        codeReponse.setLibelle(i18NService.getMessage(e.getCodeReponse().getCode()));
      } else {
        codeReponse.setCode(e.getCodeReponse().getCode());
        codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode(), e.getParams()));
      }
    } catch (Exception e) {
      codeReponse.setCode(CodeReponse.KO.getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode()));
      log.error("Erreur lors de la récupération des droits", e);
    }
    return idbCompleteResponse;
  }

  public void processIDB(final GetInfoBddRequest requete, final IDBCompleteResponse response) {
    List<ContractDto> contractList;
    DemandeInfoBeneficiaire infoBenef = consultationBaseDeDroitProcess.getDemandeInfoBenef(requete);

    try {
      final TypeInfoBdd.TypeRechercheBeneficiaire typeRechercheBenef =
          requete.getInfoBdd().getTypeRechercheBenef();

      if (StringUtils.isNotBlank(requete.getInfoBdd().getTypeGaranties())
          && !"0".equals(requete.getInfoBdd().getTypeGaranties())
          && !"1".equals(requete.getInfoBdd().getTypeGaranties())) {
        throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
      }

      final boolean limitWaranties = "1".equals(requete.getInfoBdd().getTypeGaranties());
      List<IDBResponse> droits = new ArrayList<>();
      Declarant declarant;

      switch (typeRechercheBenef) {
        case BENEFICIAIRE:
          consultationBaseDeDroitProcess.isValidBeneficiaireRequest(infoBenef);
          declarant = declarantService.getDeclarantAmcRecherche(infoBenef.getNumeroPrefectoral());
          contractList =
              consultationDroitsService.getContractsBenefIDB(infoBenef, limitWaranties, declarant);
          droits.addAll(
              mapperIdb.mapIdbResponses(
                  idbService.getPeriodesAndContractNumForIDB(
                      contractList, infoBenef.getDateReference(), infoBenef.getDateFin()),
                  contractList,
                  infoBenef,
                  declarant));
          break;
        case CARTE_FAMILLE:
          consultationBaseDeDroitProcess.isValidCarteFamilleRequest(infoBenef);
          declarant = declarantService.getDeclarantAmcRecherche(infoBenef.getNumeroPrefectoral());
          contractList =
              consultationDroitsService.getContractsCarteTiersPayantIDB(infoBenef, declarant);
          Map<String, List<ContractDto>> contractsByBenef =
              idbService.getContractsByBeneficiaire(contractList);
          for (Map.Entry<String, List<ContractDto>> entry : contractsByBenef.entrySet()) {
            Pair<String, Periode> numContratWithPeriode =
                idbService.getPeriodesAndContractNumForIDB(
                    entry.getValue(), infoBenef.getDateReference(), infoBenef.getDateFin());
            droits.addAll(
                mapperIdb.mapIdbResponses(
                    numContratWithPeriode, entry.getValue(), infoBenef, declarant));
          }
          break;
        default:
          throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
      }

      response.setDroits(droits);
      response.setCodeReponse(mapperIdb.createCodeReponseOK());

    } catch (ExceptionNumeroAdherentAbsent e) {
      log.debug(
          "Le n°adhérent est absent et celui-ci est indispensable pour identifier le bénéficiaire. Veuillez émettre une nouvelle demande avec le n°adhérent",
          e);
      throw new ExceptionMetier(CodeReponse.NUM_ADHERENT_ABSENT);
    } catch (ExceptionConsultationParametreIncorrect e) {
      log.debug("Paramètre recherche incorrect", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
    } catch (ExceptionConsultationParametreSegmentsManquent e) {
      log.debug("Paramètre recherche incorrect, segments manquants", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS_SEGMENTS_MANQUENT);
    } catch (ExceptionServiceCartePapier e) {
      log.debug("Consulter carte papier", e);
      throw new ExceptionMetier(CodeReponse.CONSULT_CARTE_PAPIER);
    } catch (ExceptionServiceBeneficiaireInconnu e) {
      log.debug("Bénéficiaire inconnu", e);
      throw new ExceptionMetier(CodeReponse.BENEF_INCONNU);
    } catch (ExceptionPriorisationGaranties e) {
      log.debug("Les garanties du bénéficiaire ne sont pas correctement priorisées", e);
      throw new ExceptionMetier(CodeReponse.PRIORISATION_INCORRECTE);
    } catch (ExceptionServiceBeneficiaireNonEligible e) {
      log.debug("Bénéficiaire non éligible", e);
      throw new ExceptionMetier(CodeReponse.BENEF_NON_ELIGIBLE);
    } catch (ExceptionServiceDroitNonOuvert | ExceptionServiceDroitResilie e) {
      log.debug("Droits bénéficiaire non ouverts", e);
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date benefRefDate = infoBenef.getDateReference();
      String formattedDate = "";
      if (benefRefDate != null) {
        formattedDate = dateFormat.format(benefRefDate);
      }
      throw new ExceptionMetier(CodeReponse.DROIT_BENEF_NON_OUVERT, formattedDate);
    }
  }
}
