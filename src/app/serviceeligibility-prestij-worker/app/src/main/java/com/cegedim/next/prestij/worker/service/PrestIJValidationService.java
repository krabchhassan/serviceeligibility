package com.cegedim.next.prestij.worker.service;

import com.cegedim.common.base.core.model.entity.NirValidation;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.*;
import com.cegedim.next.serviceeligibility.core.services.GlobalValidationService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PrestIJValidationService {

  @Autowired GlobalValidationService globalValidationService;

  private static final Logger LOGGER = LoggerFactory.getLogger(PrestIJValidationService.class);
  private String errors = "";

  private void isValidDateProperty(
      String date, String propertyName, String format, boolean isLenient) {
    if (StringUtils.isNotBlank(date) && !isValidDate(date, format, isLenient)) {
      errors += generateError(propertyName + " n'est pas une date au format " + format);
    }
  }

  private void areDatesOrdered(
      String lowerDate,
      String propertyNameL,
      String higherDate,
      String propertyNameH,
      String format,
      boolean isLenient) {
    DateFormat sdf = new SimpleDateFormat(format);
    sdf.setLenient(isLenient);
    if (StringUtils.isNotBlank(lowerDate) && StringUtils.isNotBlank(higherDate)) {
      try {
        Date lowerD = sdf.parse(lowerDate);
        Date higherD = sdf.parse(higherDate);
        if (lowerD.after(higherD)) {
          errors +=
              generateError(propertyNameL + " n'est pas une date inférieure à " + propertyNameH);
        }
      } catch (ParseException e) {
        // N/A (une des dates est invalide)
        LOGGER.info(
            "Au moins une des dates ({} ou {}) est invalide. Contrôle déjà effectué par isValidDateProperty",
            propertyNameL,
            propertyNameH);
      }
    }
  }

  public void validate(PrestIJ prestIJ) throws ValidationException {
    // Réinitialisation de la variable, car en cas de multiple appel, la
    // liste des erreurs du précédent appels est encore dans la variable
    errors = "";

    ContratIJ contratIJ = prestIJ.getContrat();
    // All annotations will be validated with the below validator
    Set<ConstraintViolation<PrestIJ>> violations = globalValidationService.validate(prestIJ);

    if (!violations.isEmpty()) {
      for (ConstraintViolation<PrestIJ> error : violations) {
        errors += generateError(error.getMessage());
      }
    }
    Entreprise entreprise = prestIJ.getEntreprise();
    if (entreprise != null
        && StringUtils.isBlank(entreprise.getIdentifiantSiren())
        && StringUtils.isBlank(entreprise.getIdentifiantSiret())) {
      errors += generateError("Le SIREN ou SIRET de l'entreprise est obligatoire");
    }

    // Allbusiness logic (date formats are validated below)
    if (contratIJ != null) {
      isValidDateProperty(
          contratIJ.getDateSouscription(), "dateSouscription", DateUtils.YYYY_MM_DD, false);
      isValidDateProperty(
          contratIJ.getDateResiliation(), "dateResiliation", DateUtils.YYYY_MM_DD, false);
      isValidDateProperty(contratIJ.getDateEffet(), "dateEffet", DateUtils.YYYY_MM_DD, false);
    }

    validateAssurePrestIJ(prestIJ);

    if (StringUtils.isNotEmpty(errors)) {
      throw new ValidationException(errors);
    }
  }

  private void validateAssurePrestIJ(PrestIJ prestIJ) {
    List<Assure> assures = prestIJ.getAssures();
    if (!CollectionUtils.isEmpty(assures)) {
      // Contrôle des assurés
      for (Assure assure : assures) {
        isValidDateProperty(assure.getDateNaissance(), "dateNaissance", "yyyyMMdd", true);

        // Contrôle des droits de l'assuré
        validateDroitsAssurePrestIJ(assure);

        // Contrôle de la validité du NIR
        validateNirAssurePrestIJ(assure);
      }
    }
  }

  private void validateNirAssurePrestIJ(Assure assure) {
    if (assure.getNir() != null) {
      NirValidation nirValidation =
          com.cegedim.common.base.core.services.ValidationService.isValidNir(
              assure.getNir().getCode(), assure.getNir().getCle());
      if (!nirValidation.isValid()) {
        errors += generateError("Le NIR n'est pas valide");
      }
    }
  }

  private void validateDroitsAssurePrestIJ(Assure assure) {
    if (!CollectionUtils.isEmpty(assure.getDroits())) {
      for (DroitAssure droit : assure.getDroits()) {
        isValidDateProperty(droit.getDateDebut(), "dateDebut", DateUtils.YYYY_MM_DD, false);
        isValidDateProperty(droit.getDateFin(), "dateFin", DateUtils.YYYY_MM_DD, false);
        areDatesOrdered(
            droit.getDateDebut(),
            "dateDebut",
            droit.getDateFin(),
            "dateFin",
            DateUtils.YYYY_MM_DD,
            false);
      }
    }
  }

  private boolean isValidDate(String date, String format, boolean isLenient) {
    DateFormat sdf = new SimpleDateFormat(format);
    if (date.length() != format.length()) {
      return false;
    }
    sdf.setLenient(isLenient);
    try {
      sdf.parse(date);
    } catch (ParseException e) {
      return false;
    }

    return true;
  }

  /**
   * Générer une erreur au format attendu
   *
   * @param message Le message a mettre au bon format
   * @return L'erreur avec l'erreur au bon format
   */
  private String generateError(String message) {
    if (StringUtils.isEmpty(errors)) {
      return message;
    }
    return "\n" + message;
  }
}
