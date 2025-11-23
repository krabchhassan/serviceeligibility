package com.cegedim.next.serviceeligibility.core.features.consultationdroits.calcul;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionNumeroAdherentAbsent;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionPriorisationGaranties;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireInconnu;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireNonEligible;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceCartePapier;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitResilie;
import com.cegedim.next.serviceeligibility.core.business.consultationdroits.ConsultationDroitsService;
import com.cegedim.next.serviceeligibility.core.features.utils.ConsultationDroitsUtils;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreIncorrect;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreSegmentsManquent;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperConsultationDroit;
import com.cegedim.next.serviceeligibility.core.soap.consultation.process.ConsultationBaseDeDroitProcessImpl;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import com.cegedim.next.serviceeligibility.core.webservices.clc.CLCCompleteResponse;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddRequest;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.tracing.annotation.NewSpan;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CLCController {

  private final RequestValidator requestValidator;
  private final ConsultationBaseDeDroitProcessImpl consultationBaseDeDroitProcess;
  private final CLCMapper mapperClc;
  private final ConsultationDroitsService consultationDroitsService;

  private final I18NService i18NService = new I18NService();

  @NewSpan
  @GetMapping(
      value = "/v1/calcul",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<CLCCompleteResponse> getDroits(@RequestBody final String requete) {
    log.info("Interrogation des droits des bénéficiaires");

    GetInfoBddRequestDto bddRequest;
    try {
      // Custom mapper pour récupérer les erreurs de parsing
      bddRequest = ConsultationDroitsUtils.getRequestBody(requete);
    } catch (JsonProcessingException e) {
      CLCCompleteResponse completeResponse = mapperClc.creerReponseVide();
      TypeCodeReponse codeReponse = completeResponse.getCodeReponse();
      codeReponse.setCode(CodeReponse.PARAM_RECHERCHE_INCORRECTS.getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode()));
      log.error("Erreur dans le parametrage de la requete", e);
      return new ResponseEntity<>(completeResponse, HttpStatus.OK);
    }

    if (bddRequest.getDates() != null) {
      bddRequest.getDates().setDateReference(bddRequest.getDates().getDateDebut());
    }
    requestValidator.validateRequestV4(bddRequest);
    return new ResponseEntity<>(
        getCLCResponse(MapperConsultationDroit.getRequest(bddRequest)), HttpStatus.OK);
  }

  private CLCCompleteResponse getCLCResponse(GetInfoBddRequest request) {
    CLCCompleteResponse completeResponse = mapperClc.creerReponseVide();
    TypeCodeReponse codeReponse = completeResponse.getCodeReponse();
    try {
      process(request, completeResponse);
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
    return completeResponse;
  }

  public void process(final GetInfoBddRequest requete, final CLCCompleteResponse response) {
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
      List<ContractDto> contractDtoList;

      switch (typeRechercheBenef) {
        case BENEFICIAIRE:
          consultationBaseDeDroitProcess.isValidBeneficiaireRequest(infoBenef);
          contractDtoList =
              consultationDroitsService.getContractsBenefCLC(infoBenef, limitWaranties);
          ConsultationDroitsUtils.filterContractsPeriodsNotOverlappingRequest(
              contractDtoList,
              DateUtils.formatDate(infoBenef.getDateReference()),
              DateUtils.formatDate(infoBenef.getDateFin()));
          mapperClc.completeResponseCLC(contractDtoList, response);
          break;
        case CARTE_FAMILLE:
          consultationBaseDeDroitProcess.isValidCarteFamilleRequest(infoBenef);
          contractDtoList = consultationDroitsService.getContractsCarteTiersPayantCLC(infoBenef);
          ConsultationDroitsUtils.filterContractsPeriodsNotOverlappingRequest(
              contractDtoList,
              DateUtils.formatDate(infoBenef.getDateReference()),
              DateUtils.formatDate(infoBenef.getDateFin()));
          mapperClc.completeResponseCLC(contractDtoList, response);
          break;
        default:
          throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
      }

      response.setCodeReponse(mapperClc.createCodeReponseOK());

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
