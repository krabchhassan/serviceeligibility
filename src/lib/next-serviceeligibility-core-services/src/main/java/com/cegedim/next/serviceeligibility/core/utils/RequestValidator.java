package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.services.GlobalValidationService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationListException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedimassurances.norme.amc.TypeAmc;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.ListeSegmentRecherche;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.TypeRechercheSegment;
import com.cegedimassurances.norme.beneficiaire.TypeBeneficiaireDemandeur;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RequestValidator {

  public static final String TIRET = "-";
  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";
  public static final String INVALID_REQUEST = "Invalid request";
  public static final String BENEFICIAIRE_ID_INVALIDE =
      "L'identifiant du bénéficiaire '%s' est invalide.";
  @Autowired public GlobalValidationService globalValidationService;

  private static RequestValidationException obligatoireException(String name) {
    return new RequestValidationException(String.format("%s est obligatoire", name));
  }

  private static RequestValidationException pauObligatoireException(String name) {
    return new RequestValidationException(
        String.format("%s est obligatoire", name),
        HttpStatus.BAD_REQUEST,
        RestErrorConstants.ERROR_CODE_MISSING_VARIABLE);
  }

  private static List<RequestValidationException> validateBenef(TypeBeneficiaireDemandeur benef) {
    List<RequestValidationException> errors = new ArrayList<>();
    // nir is validated with annotations
    String nirCle = benef.getCleNIR();
    String dateNaissance = benef.getDateNaissance();
    int rang = benef.getRangGemellaire();
    if (nirCle == null) {
      errors.add(obligatoireException("beneficiaire.cleNIR"));
    }
    if (dateNaissance == null) {
      errors.add(obligatoireException("beneficiaire.dateNaissance"));
    }
    if (rang > 9 || rang < 1) {
      errors.add(
          new RequestValidationException("rangGemellaire doit être une valeur entre 1 et 9"));
    }
    return errors;
  }

  private static List<RequestValidationException> validateAmc(TypeAmc amc) {
    List<RequestValidationException> errors = new ArrayList<>();
    // Nom is validated with annotations
    // adherent is validated with annotations
    String numPref = amc.getNumeroAMCPrefectoral();
    if (numPref == null) {
      errors.add(obligatoireException("amc.numAmcPrefectoral"));
    }
    return errors;
  }

  private static List<RequestValidationException> validateInfoBdd(TypeInfoBdd info) {
    List<RequestValidationException> errors = new ArrayList<>();
    TypeRechercheSegment typeRechercheSegment = info.getTypeRechercheSegment();
    if (typeRechercheSegment != null
        && typeRechercheSegment == TypeRechercheSegment.LISTE_SEGMENT) {
      ListeSegmentRecherche listeSegmentRecherche = info.getListeSegmentRecherche();
      if (listeSegmentRecherche == null) {
        errors.add(obligatoireException("infoBdd.listeSegmentRecherche"));
      } else {
        List<String> list = listeSegmentRecherche.getSegmentRecherche();
        if (list == null || list.isEmpty()) {
          errors.add(obligatoireException("segmentRecherche"));
        } else {
          addError(errors, list);
        }
      }
    }

    return errors;
  }

  private static void addError(List<RequestValidationException> errors, List<String> list) {
    for (String segment : list) {
      if (segment != null && segment.length() > 8) {
        errors.add(
            new RequestValidationException(
                "ingoBdd.listeSegmentRecherche.segmentRecherche "
                    + segment
                    + " a plus que 8 caractères"));
      }
    }
  }

  @ContinueSpan(log = "validateRequestV4")
  public void validateRequestV4(GetInfoBddRequestDto request) {
    List<RequestValidationException> errors = new ArrayList<>();
    Locale.setDefault(Locale.FRENCH);

    Set<ConstraintViolation<GetInfoBddRequestDto>> requestViolations =
        globalValidationService.validate(request);
    for (ConstraintViolation<GetInfoBddRequestDto> item : requestViolations) {
      errors.add(new RequestValidationException(item.getPropertyPath() + ":" + item.getMessage()));
    }

    TypeBeneficiaireDemandeur benef = request.getBeneficiaire();
    TypeAmc amc = request.getAmc();
    TypeInfoBdd infoBdd = request.getInfoBdd();

    if (benef != null) {
      errors.addAll(validateBenef(benef));
    }
    if (amc != null) {
      errors.addAll(validateAmc(amc));
    }
    if (infoBdd != null) {
      errors.addAll(validateInfoBdd(infoBdd));
    }
    if (!errors.isEmpty()) {
      throw new RequestValidationListException(INVALID_REQUEST, errors);
    }
  }

  private static boolean validateDate(
      String date, String fieldName, List<RequestValidationException> errors) {
    if (!DateUtils.isValidDate(date, Constants.YYYY_MM_DD, false)) {
      errors.add(
          new RequestValidationException(
              String.format(
                  "'%s' avec la valeur '%s' ne respecte pas le format '%s' .",
                  fieldName, date, Constants.YYYY_MM_DD),
              HttpStatus.BAD_REQUEST,
              RestErrorConstants.ERROR_CODE_PAU_INVALID_DATE_FORMAT));
      return false;
    }
    return true;
  }

  private static void validatePeriod(
      String startDate, String endDate, List<RequestValidationException> errors) {
    boolean ok = true;
    if (startDate != null) {
      ok = validateDate(startDate, START_DATE, errors);
    }
    if (endDate != null) {
      ok = validateDate(endDate, END_DATE, errors);
    }
    if (ok && startDate != null && endDate != null) {
      if (!startDate.equals(endDate)) {
        ok = validateDate(endDate, END_DATE, errors);
      }
      if (ok && LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
        errors.add(
            new RequestValidationException(
                "La date de début d'interrogation est supérieure à la date de fin.",
                HttpStatus.BAD_REQUEST,
                RestErrorConstants.ERROR_CODE_PAU_DATE_START_GTE_END));
      }
    }
  }

  @ContinueSpan(log = "validateRequestUAP (1 param)")
  public static void validateRequestUAP(UniqueAccessPointRequest request) {
    List<RequestValidationException> errors = new ArrayList<>();

    if (StringUtils.isBlank(request.getStartDate())) {
      errors.add(pauObligatoireException(START_DATE));
    }

    if (StringUtils.isBlank(request.getInsurerId())) {
      errors.add(pauObligatoireException("insurerId"));
    }

    if (StringUtils.isNotEmpty(request.getBirthDate())
        && (!DateUtils.isValidDate(request.getBirthDate(), "yyyyMMdd", true)
            || !request.getBirthDate().matches("[\\d]{8}"))) {
      errors.add(
          new RequestValidationException(
              String.format("La date de naissance %s n'est pas valide", request.getBirthDate()),
              HttpStatus.BAD_REQUEST,
              RestErrorConstants.ERROR_CODE_PAU_INVALID_DATE_FORMAT));
    }

    validateExistingUAPV5((UniqueAccessPointRequestV5) request, errors);

    validatePeriod(request.getStartDate(), request.getEndDate(), errors);

    if (StringUtils.isBlank(request.getContext())) {
      errors.add(pauObligatoireException("context"));
    } else {
      if (request.getContext().contains(",")) {
        errors.add(
            new RequestValidationException(
                "Le paramètre context doit ne contenir qu'un seul contexte",
                HttpStatus.BAD_REQUEST,
                RestErrorConstants.ERROR_CODE_PAU_UNKNOWN_CONTEXT));
      } else {
        List<String> allowedContexts =
            List.of(ContextConstants.HTP, ContextConstants.TP_ONLINE, ContextConstants.TP_OFFLINE);
        if (!allowedContexts.contains(request.getContext())) {
          errors.add(
              new RequestValidationException(
                  String.format(
                      "Le contexte '%s' est inconnu. Les contextes permis sont 'HTP', 'TP_ONLINE' et 'TP_OFFLINE'.",
                      request.getContext()),
                  HttpStatus.BAD_REQUEST,
                  RestErrorConstants.ERROR_CODE_PAU_UNKNOWN_CONTEXT));
        }
      }
    }

    if (!errors.isEmpty()) {
      throw new RequestValidationListException(INVALID_REQUEST, errors);
    }
  }

  private static void validateExistingUAPV5(
      UniqueAccessPointRequestV5 request, List<RequestValidationException> errors) {
    boolean isNirBlank = StringUtils.isBlank(request.getNirCode());
    boolean isSubscriberIdBlank = StringUtils.isBlank(request.getSubscriberId());
    boolean isBirthDateOrRankBlank =
        StringUtils.isBlank(request.getBirthDate()) || StringUtils.isBlank(request.getBirthRank());

    boolean error =
        StringUtils.isBlank(request.getBeneficiaryId())
            && ((isSubscriberIdBlank && isNirBlank)
                || (!ContextConstants.HTP.equals(request.getContext())
                    && !isNirBlank
                    && isBirthDateOrRankBlank));

    if (error) {
      errors.add(
          new RequestValidationException(
              "Veuillez renseigner les critères de recherche du bénéficiaire",
              HttpStatus.BAD_REQUEST,
              RestErrorConstants.ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND_WITH_BENEFICIARY_ID));
    }
  }

  @ContinueSpan(log = "validateRequestBddstoBlb")
  public static void validateRequestBddstoBlb(final UniqueAccessPointRequestV5 request) {
    if (StringUtils.isBlank(request.getNirCode())) {
      throw new RequestValidationException(
          "Le code nir doit être renseigné",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION);
    }
    if (StringUtils.isBlank(request.getBirthDate())) {
      throw new RequestValidationException(
          "La date de naissance doit être renseignée",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION);
    }
    if (StringUtils.isBlank(request.getBirthRank())) {
      throw new RequestValidationException(
          "Le rang de naissance doit être renseigné",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION);
    }
    if (!List.of("TP_ONLINE", "TP_OFFLINE").contains(request.getContext())) {
      throw new RequestValidationException(
          String.format(
              "Le contexte '%s' est inconnu. Les contextes permis sont 'TP_ONLINE' et 'TP_OFFLINE'.",
              request.getContext()),
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_PAU_UNKNOWN_CONTEXT);
    }
    if (StringUtils.isNotEmpty(request.getBirthDate())
        && (!DateUtils.isValidDate(request.getBirthDate(), "yyyyMMdd", true)
            || !request.getBirthDate().matches("\\d{8}"))) {
      throw new RequestValidationException(
          String.format("La date de naissance %s n'est pas valide", request.getBirthDate()),
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_PAU_INVALID_DATE_FORMAT);
    }
    if (!DateUtils.isValidDate(request.getStartDate(), Constants.YYYY_MM_DD, false)) {
      throw new RequestValidationException(
          String.format(
              "'%s' avec la valeur '%s' ne respecte pas le format '%s' .",
              START_DATE, request.getStartDate(), Constants.YYYY_MM_DD),
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_PAU_INVALID_DATE_FORMAT);
    }
  }

  @ContinueSpan(log = "validateRequestAndExtractBenefId for ContractsByBenef (2 params)")
  public static String[] validateRequestAndExtractBenefId(String beneficiaryId, String context) {
    List<RequestValidationException> errors = new ArrayList<>();
    String[] benefIdSplitted = validateAndExtractBenefId(beneficiaryId, errors);

    if (StringUtils.isBlank(context)) {
      errors.add(pauObligatoireException("context"));
    } else {
      if (context.contains(",")) {
        errors.add(
            new RequestValidationException(
                "Le paramètre context ne doit contenir qu'un seul contexte",
                HttpStatus.BAD_REQUEST,
                RestErrorConstants.ERROR_CODE_PAU_UNKNOWN_CONTEXT));
      } else {
        List<String> allowedContexts =
            List.of(ContextConstants.HTP, ContextConstants.TP_ONLINE, ContextConstants.TP_OFFLINE);
        if (!allowedContexts.contains(context)) {
          errors.add(
              new RequestValidationException(
                  String.format(
                      "Le contexte '%s' est inconnu. Les contextes permis sont 'HTP', 'TP_ONLINE' et 'TP_OFFLINE'.",
                      context),
                  HttpStatus.BAD_REQUEST,
                  RestErrorConstants.ERROR_CODE_PAU_UNKNOWN_CONTEXT));
        }
      }
    }

    if (!errors.isEmpty()) {
      throw new RequestValidationListException(INVALID_REQUEST, errors);
    }
    return benefIdSplitted;
  }

  private static String[] validateAndExtractBenefId(
      String beneficiaryId, List<RequestValidationException> errors) {
    String[] benefIdSplitted = null;
    if (StringUtils.isBlank(beneficiaryId)) {
      errors.add(pauObligatoireException("beneficiaryId"));
    } else {
      if (!beneficiaryId.contains(TIRET)) {
        errors.add(
            new RequestValidationException(
                String.format(BENEFICIAIRE_ID_INVALIDE, beneficiaryId),
                HttpStatus.BAD_REQUEST,
                RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION));
      } else {
        benefIdSplitted = beneficiaryId.split(TIRET, 2);
        if (benefIdSplitted.length != 2
            || StringUtils.isBlank(benefIdSplitted[0])
            || StringUtils.isBlank(benefIdSplitted[1])) {
          errors.add(
              new RequestValidationException(
                  String.format(BENEFICIAIRE_ID_INVALIDE, beneficiaryId),
                  HttpStatus.BAD_REQUEST,
                  RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION));
        }
      }
    }
    return benefIdSplitted;
  }
}
