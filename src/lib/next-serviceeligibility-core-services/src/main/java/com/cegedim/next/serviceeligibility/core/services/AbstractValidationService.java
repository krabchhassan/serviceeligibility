package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.common.base.core.model.entity.NirValidation;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public abstract class AbstractValidationService {

  protected static final String PERIODE_FIN = ".periode.fin";
  protected static final String PERIODE_DEBUT = ".periode.debut";

  protected static final String PERIODE_EST_OBLIGATOIRE = ".periode est obligatoire";

  protected static final String PERIODE_DEBUT_EST_OBLIGATOIRE = ".periode.debut est obligatoire";

  protected static final String L_INFORMATION2 = "L'information ";

  protected static final String L_INFORMATION = "l'information ";
  protected static final String DATA_DESTINATAIRES_PAIEMENTS = "data.destinatairesPaiements";
  protected static final String DATA_DESTINATAIRES_PRESTATIONS_V4 = DATA_DESTINATAIRES_PAIEMENTS;

  protected static final String MESSAGE_NIR_INVALID = "le NIR (%s / %s) de %s n'est pas valide.";

  /** Générer une erreur au format attendu */
  protected void generateError(
      ContractValidationBean contractValidationBean, ErrorValidationBean errorValidationBean) {
    contractValidationBean.getErrorValidationBeans().add(errorValidationBean);
  }

  /**
   * Validation d'une date d'une propriété donnée via une String selon un format donné
   *
   * @param date Date sous forme de String à valider
   * @param propertyName Nom de la propriété à valider afin de pouvoir soulever une exception
   *     indiquant cette propriété
   * @param format Le format de date à respecter
   * @param isLenient Permet la gestion de date lunaire (true pour uniquement des dates grégorienne)
   */
  protected void isValidDateProperty(
      String date,
      String propertyName,
      String format,
      boolean isLenient,
      boolean isAssure,
      ContractValidationBean contractValidationBean) {
    if (StringUtils.isNotEmpty(date) && !DateUtils.isValidDate(date, format, isLenient)) {
      if (isAssure) {
        generateErrorAssure(
            L_INFORMATION + propertyName + "(" + date + ") n'est pas une date au format " + format,
            contractValidationBean);
      } else {
        generateErrorContrat(
            L_INFORMATION2 + propertyName + "(" + date + ") n'est pas une date au format " + format,
            contractValidationBean);
      }
    }
  }

  protected abstract void generateErrorIfBlank(
      String s, String errorString, ContractValidationBean contractValidationBean);

  protected abstract boolean generateErrorIfNull(
      Object s, String errorString, ContractValidationBean contractValidationBean);

  protected abstract void generateErrorAssure(
      String errorString, ContractValidationBean contractValidationBean);

  protected abstract void generateErrorContrat(
      String errorString, ContractValidationBean contractValidationBean);

  protected void validationNir(
      String nir,
      String cle,
      String champ,
      boolean isAssure,
      ContractValidationBean contractValidationBean) {
    NirValidation nirValidation =
        com.cegedim.common.base.core.services.ValidationService.isValidNir(nir, cle);
    if (!nirValidation.isValid()) {
      if (isAssure) {
        generateErrorAssure(
            String.format(MESSAGE_NIR_INVALID, nir, cle, champ), contractValidationBean);
      } else {
        generateErrorContrat(
            String.format(MESSAGE_NIR_INVALID, nir, cle, champ), contractValidationBean);
      }
    }
  }

  protected void validateIdentiteAssureInfoDetails(
      List<NirRattachementRO> lstAffiliationsRO,
      String propertyName,
      boolean isAssure,
      ContractValidationBean contractValidationBean) {
    Nir nir;
    if (!CollectionUtils.isEmpty(lstAffiliationsRO)) {
      for (NirRattachementRO affiliationRo : lstAffiliationsRO) {
        nir = affiliationRo.getNir();
        if (nir != null
            && StringUtils.isNotBlank(nir.getCode())
            && StringUtils.isNotBlank(nir.getCle())) {
          validationNir(
              nir.getCode(), nir.getCle(), propertyName + ".nir", isAssure, contractValidationBean);
        }
        Periode periode = affiliationRo.getPeriode();
        if (periode != null) {
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
  }

  protected abstract void validateModelIdentiteAssureNirRO(
      NirRattachementRO rattRO,
      Nir nirRatt,
      String debutMessage,
      ContractValidationBean contractValidationBean);

  public static String getMessageFromValidationBean(ContractValidationBean contractValidationBean) {
    StringBuilder message = new StringBuilder();
    int compteur = 0;
    for (ErrorValidationBean errorValidationBean :
        contractValidationBean.getErrorValidationBeans()) {
      if (compteur > 0) {
        message.append("\n");
      }
      message.append(errorValidationBean.getError());
      compteur++;
    }
    return message.toString();
  }
}
