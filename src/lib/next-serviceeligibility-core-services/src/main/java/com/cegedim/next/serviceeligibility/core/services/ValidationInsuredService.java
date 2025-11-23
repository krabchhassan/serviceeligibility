package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ValidationException;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.stereotype.Service;

@Service
public class ValidationInsuredService extends AbstractValidationService {

  private static final String DEMATERIALISATION_EST_OBLIGATOIRE =
      ".dematerialisation est obligatoire";

  private static final String DEMATERIALISATION_EST_OBLIGATOIRE_BIS =
      ".dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire";

  private static final String DATA_DESTINATAIRES_RELEVE_PRESTATIONS =
      "data.destinatairesRelevePrestations";
  private static final String DESTINATAIRES_RELEVE_PRESTATIONS = "destinatairesRelevePrestations";

  /**
   * Validation des informations des données d'un assuré pour un contrat
   *
   * @param data données assuré
   */
  private void validateDataInfo(
      DataAssureCommun data, ContractValidationBean contractValidationBean) {
    if (data instanceof InsuredDataV5 dataV5) {
      validateAssureInfoV5(dataV5, contractValidationBean);
    } else if (data instanceof DataAssure dataV5) {
      validateAssureInfoV4(dataV5, contractValidationBean);
    }
  }

  private void validateAssureInfoV4(
      DataAssure data, ContractValidationBean contractValidationBean) {
    if (data != null && !CollectionUtils.isEmpty(data.getDestinatairesPaiements())) {
      for (DestinatairePrestations destinataire : data.getDestinatairesPaiements()) {
        validIbanAssure(destinataire.getRib(), contractValidationBean);
      }
      validDateListeDestinatairesPrestationsV4(
          data.getDestinatairesPaiements(), contractValidationBean);
    }
    if (data != null && data.getDestinatairesRelevePrestations() != null) {
      validDateListeDestinatairesRelevePrestationsV5(
          data.getDestinatairesRelevePrestations(), contractValidationBean);
    }
  }

  private void validateAssureInfoV5(
      InsuredDataV5 data, ContractValidationBean contractValidationBean) {
    validateAssureInfoV4(data, contractValidationBean);
  }

  /**
   * Validation d'un RIB
   *
   * @param rib Objet RIB
   */
  private void validIbanAssure(RibAssure rib, ContractValidationBean contractValidationBean) {
    boolean hasError = false;
    if (rib != null && StringUtils.isNotBlank(rib.getIban())) {
      String iban = rib.getIban();
      if (iban.length() < 14 || iban.length() > 34) {
        generateError(
            contractValidationBean,
            new ErrorValidationBean(
                "la longueur de l'IBAN (" + iban + ") doit être comprise entre 14 et 34",
                null,
                null));
        hasError = true;
      }
      if (!hasError && !IBANValidator.getInstance().isValid(rib.getIban())) {
        generateError(
            contractValidationBean,
            new ErrorValidationBean("l'IBAN (" + iban + ") n'est pas valide.", null, null));
      }
    }
  }

  /**
   * Validation de la liste des destinataires de prestation
   *
   * @param destinatairesPrestations Liste des destinataires de prestation
   */
  private void validDateListeDestinatairesPrestationsV4(
      List<DestinatairePrestations> destinatairesPrestations,
      ContractValidationBean contractValidationBean) {
    if (destinatairesPrestations != null) {
      for (DestinatairePrestations item : destinatairesPrestations) {
        validPeriode(item.getPeriode(), DATA_DESTINATAIRES_PRESTATIONS_V4, contractValidationBean);
      }
    }
  }

  /**
   * Valide la liste des destinataires de relevé de prestations
   *
   * @param destinatairesRelevePrestations Liste des destinataires de relevé de prestations
   */
  private void validDateListeDestinatairesRelevePrestationsV5(
      List<DestinataireRelevePrestations> destinatairesRelevePrestations,
      ContractValidationBean contractValidationBean) {
    if (destinatairesRelevePrestations != null) {
      for (DestinataireRelevePrestations item : destinatairesRelevePrestations) {
        validPeriode(
            item.getPeriode(), DATA_DESTINATAIRES_RELEVE_PRESTATIONS, contractValidationBean);
        Dematerialisation demat = item.getDematerialisation();
        if (demat == null) {
          generateError(
              contractValidationBean,
              new ErrorValidationBean(
                  L_INFORMATION2
                      + DESTINATAIRES_RELEVE_PRESTATIONS
                      + DEMATERIALISATION_EST_OBLIGATOIRE,
                  null,
                  null));
        } else if (Boolean.TRUE.equals(demat.getIsDematerialise())
            && StringUtils.isBlank(demat.getEmail())
            && StringUtils.isBlank(demat.getMobile())) {
          generateError(
              contractValidationBean,
              new ErrorValidationBean(
                  L_INFORMATION2
                      + DATA_DESTINATAIRES_RELEVE_PRESTATIONS
                      + DEMATERIALISATION_EST_OBLIGATOIRE_BIS,
                  null,
                  null));
        }
      }
    }
  }

  /**
   * Validation des dates debut et fin d'une periode
   *
   * @param periode Liste de périodes à valider
   * @param propertyName Nom du bloc d'information contenant la liste de périodes
   */
  protected void validPeriode(
      PeriodeDestinataire periode,
      String propertyName,
      ContractValidationBean contractValidationBean) {
    if (periode != null) {
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
  }

  @ContinueSpan(log = "validateInsuredData")
  public void validateInsuredData(
      InsuredDataV5 insuredDataV5, ContractValidationBean contractValidationBean) {
    validateDataAssure(insuredDataV5, contractValidationBean);

    generateErrorIfBlank(
        insuredDataV5.getDateNaissance(),
        "L'information dateNaissance est obligatoire",
        contractValidationBean);
    generateErrorIfBlank(
        insuredDataV5.getRangNaissance(),
        "L'information rangNaissance est obligatoire",
        contractValidationBean);

    isValidDateProperty(
        insuredDataV5.getDateNaissance(),
        "dateNaissance",
        Constants.BIRTH_DATE_FORMAT,
        true,
        false,
        contractValidationBean);

    if (insuredDataV5.getNir() == null
        && CollectionUtils.isEmpty(insuredDataV5.getAffiliationsRO())) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean("La requête doit posséder au moins un nir", null, null));
    }
    Nir nir = insuredDataV5.getNir();

    if (nir != null) {
      generateErrorIfBlank(
          nir.getCode(), "L'information nir.code est obligatoire", contractValidationBean);
      generateErrorIfBlank(
          nir.getCle(), "L'information nir.cle est obligatoire", contractValidationBean);
    }

    if (nir != null
        && StringUtils.isNotBlank(nir.getCode())
        && StringUtils.isNotBlank(nir.getCle())) {
      validationNir(nir.getCode(), nir.getCle(), "nir", false, contractValidationBean);
    }
    validateIdentiteAssureInfoDetails(
        insuredDataV5.getAffiliationsRO(), "affiliationsRO", false, contractValidationBean);

    List<NirRattachementRO> affiliationsRO = insuredDataV5.getAffiliationsRO();
    if (!CollectionUtils.isEmpty(affiliationsRO)) {
      for (NirRattachementRO rattRO : affiliationsRO) {
        Nir nirRatt = rattRO.getNir();
        validateModelIdentiteAssureNirRO(
            rattRO, nirRatt, "L'information affiliationsRO", contractValidationBean);
      }
    }

    if (CollectionUtils.isNotEmpty(contractValidationBean.getErrorValidationBeans())) {
      throw new ValidationContractException(
          contractValidationBean.getErrorValidationBeans(),
          getMessageFromValidationBean(contractValidationBean));
    }
  }

  /**
   * Validation des données d'un assuré
   *
   * @param dataAssure Données assuré à valider
   * @throws ValidationException Exception indiquant que les données assuré ne sont pas valide
   */
  @ContinueSpan(log = "validateDataAssure")
  public void validateDataAssure(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    validateModelAssure(dataAssure, contractValidationBean);

    validateDataInfo(dataAssure, contractValidationBean);

    if (CollectionUtils.isNotEmpty(contractValidationBean.getErrorValidationBeans())) {
      throw new ValidationContractException(
          contractValidationBean.getErrorValidationBeans(),
          getMessageFromValidationBean(contractValidationBean));
    }
  }

  /**
   * Validation du modele de Assure à partir d'un assuré
   *
   * @param dataAssure Le contrat contenant les assurés à valider
   */
  private void validateModelAssure(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    if (dataAssure != null) {
      NomAssure nom = dataAssure.getNom();
      if (nom != null) {
        generateErrorIfBlank(
            nom.getNomFamille(),
            "L'information nom.nomFamille est obligatoire",
            contractValidationBean);
        generateErrorIfBlank(
            nom.getPrenom(), "L'information nom.prenom est obligatoire", contractValidationBean);
        generateErrorIfBlank(
            nom.getCivilite(),
            "L'information nom.civilite est obligatoire",
            contractValidationBean);
      }
      if (dataAssure instanceof InsuredDataV5) {
        validateModelInsuredDataV5(dataAssure, contractValidationBean);
      } else if (dataAssure instanceof DataAssure) {
        validateModelAssureV5(dataAssure, contractValidationBean);
      }
    }
  }

  private void validateModelAssureV5(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    DataAssure dataAssureV5 = (DataAssure) dataAssure;
    checkDestinatairePrestations(dataAssureV5.getDestinatairesPaiements(), contractValidationBean);
    checkDestinataireRelevePrestations(
        dataAssureV5.getDestinatairesRelevePrestations(), contractValidationBean);
  }

  private void validateModelInsuredDataV5(
      DataAssureCommun dataAssure, ContractValidationBean contractValidationBean) {
    InsuredDataV5 dataAssureV5 = (InsuredDataV5) dataAssure;
    checkDestinatairePrestations(dataAssureV5.getDestinatairesPaiements(), contractValidationBean);
    checkDestinataireRelevePrestations(
        dataAssureV5.getDestinatairesRelevePrestations(), contractValidationBean);
  }

  private void checkDestinataireRelevePrestations(
      List<DestinataireRelevePrestations> destRelevePrests,
      ContractValidationBean contractValidationBean) {
    if (!CollectionUtils.isEmpty(destRelevePrests)) {
      for (DestinataireRelevePrestations destRelevePrest : destRelevePrests) {
        if (StringUtils.isEmpty(destRelevePrest.getIdDestinataireRelevePrestations())) {
          generateError(
              contractValidationBean,
              new ErrorValidationBean(
                  "L'information destinatairesRelevePrestations.idDestinataireRelevePrestations est obligatoire",
                  null,
                  null));
        }
        validateInfoDestRelevePrest(
            destRelevePrest.getNom(), destRelevePrest.getPeriode(), contractValidationBean);
      }
    }
  }

  private void checkDestinatairePrestations(
      List<DestinatairePrestations> destPrests, ContractValidationBean contractValidationBean) {
    if (!CollectionUtils.isEmpty(destPrests)) {
      for (DestinatairePrestations destPrest : destPrests) {
        if (StringUtils.isEmpty(destPrest.getIdDestinatairePaiements())) {
          generateError(
              contractValidationBean,
              new ErrorValidationBean(
                  "L'information destinatairesPaiements.idDestinatairePaiements est obligatoire",
                  null,
                  null));
        }
        validateInfoDestPrest(
            destPrest.getNom(),
            destPrest.getRib(),
            destPrest.getModePaiementPrestations(),
            destPrest.getPeriode(),
            contractValidationBean);
      }
    }
  }

  private void validateInfoDestRelevePrest(
      NomDestinataire nomDestPrest,
      PeriodeDestinataire periodeDestPrest,
      ContractValidationBean contractValidationBean) {
    if (nomDestPrest == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(
              "L'information destinatairesRelevePrestations.nom est obligatoire", null, null));
    } else {
      String isValidNom = nomDestPrest.validateNomDestinataire();
      if (isValidNom != null) {
        generateError(contractValidationBean, new ErrorValidationBean(isValidNom, null, null));
      }
    }

    if (periodeDestPrest == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(
              "L'information destinatairesRelevePrestations.periode est obligatoire", null, null));
    } else {
      if (StringUtils.isBlank(periodeDestPrest.getDebut())) {
        generateError(
            contractValidationBean,
            new ErrorValidationBean(
                "L'information destinatairesRelevePrestations.periode.debut est obligatoire",
                null,
                null));
      }
    }
  }

  private void validateInfoDestPrest(
      NomDestinataire nomDestPrest,
      RibAssure rib,
      ModePaiement modePaiementPrestations,
      PeriodeDestinataire periodeDestPrest,
      ContractValidationBean contractValidationBean) {
    String path = "destinatairesPaiements";
    if (nomDestPrest == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(L_INFORMATION2 + path + ".nom est obligatoire", null, null));
    } else {
      String isValidNom = nomDestPrest.validateNomDestinataire();
      if (isValidNom != null) {
        generateError(contractValidationBean, new ErrorValidationBean(isValidNom, null, null));
      }
    }

    if (rib != null) {
      validateRibDestPrest(rib, path, contractValidationBean);
    }

    if (modePaiementPrestations == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(
              L_INFORMATION2 + path + ".modePaiementPrestations est obligatoire", null, null));
    } else {
      generateErrorIfBlank(
          modePaiementPrestations.getCode(),
          L_INFORMATION2 + path + ".modePaiementPrestations.code est obligatoire",
          contractValidationBean);
      generateErrorIfBlank(
          modePaiementPrestations.getLibelle(),
          L_INFORMATION2 + path + ".modePaiementPrestations.libelle est obligatoire",
          contractValidationBean);
      generateErrorIfBlank(
          modePaiementPrestations.getCodeMonnaie(),
          L_INFORMATION2 + path + ".modePaiementPrestations.codeMonnaie est obligatoire",
          contractValidationBean);
    }

    if (periodeDestPrest == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(L_INFORMATION2 + path + PERIODE_EST_OBLIGATOIRE, null, null));
    } else {
      generateErrorIfBlank(
          periodeDestPrest.getDebut(),
          L_INFORMATION2 + path + PERIODE_DEBUT_EST_OBLIGATOIRE,
          contractValidationBean);
    }
  }

  private void validateRibDestPrest(
      RibAssure rib, String path, ContractValidationBean contractValidationBean) {
    String iban = rib.getIban();
    String bic = rib.getBic();
    if (iban != null && bic == null) {
      generateErrorIfBlank(
          bic, L_INFORMATION2 + path + ".rib.bic est obligatoire", contractValidationBean);
    } else if (iban == null && bic != null) {
      generateErrorIfBlank(
          iban, L_INFORMATION2 + path + ".rib.iban est obligatoire", contractValidationBean);
    } else if (iban != null
        && !bic.matches("^([A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{0,3})?)$")) { // NOSONAR
      // :
      // empty
      // string
      // is
      // accepted
      generateError(
          contractValidationBean,
          new ErrorValidationBean(
              L_INFORMATION2 + path + ".rib.bic n'est pas conforme", null, null));
    }
  }

  protected boolean generateErrorIfNull(
      Object s, String errorString, ContractValidationBean contractValidationBean) {
    if (s == null) {
      generateError(contractValidationBean, new ErrorValidationBean(errorString, null, null));
      return true;
    }
    return false;
  }

  @Override
  protected void generateErrorAssure(
      String errorString, ContractValidationBean contractValidationBean) {
    throw new IllegalCallerException(
        "Cette méthode ne doit pas être appelé sur l'endpoint Insured");
  }

  @Override
  protected void generateErrorContrat(
      String errorString, ContractValidationBean contractValidationBean) {
    generateError(
        contractValidationBean, new ErrorValidationBean(errorString, Constants.CONTRAT, ""));
  }

  protected void generateErrorIfBlank(
      String s, String errorString, ContractValidationBean contractValidationBean) {
    if (StringUtils.isBlank(s)) {
      generateError(contractValidationBean, new ErrorValidationBean(errorString, null, null));
    }
  }

  @Override
  protected void validateModelIdentiteAssureNirRO(
      NirRattachementRO rattRO,
      Nir nirRatt,
      String debutMessage,
      ContractValidationBean contractValidationBean) {
    if (nirRatt == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(debutMessage + ".nir est obligatoire", null, null));
    } else {
      generateErrorIfBlank(
          nirRatt.getCode(), debutMessage + ".nir.code est obligatoire", contractValidationBean);
      generateErrorIfBlank(
          nirRatt.getCle(), debutMessage + ".nir.cle est obligatoire", contractValidationBean);
    }
    Periode periode = rattRO.getPeriode();
    if (periode == null) {
      generateError(
          contractValidationBean,
          new ErrorValidationBean(debutMessage + PERIODE_EST_OBLIGATOIRE, null, null));
    } else {
      generateErrorIfBlank(
          periode.getDebut(), debutMessage + PERIODE_DEBUT_EST_OBLIGATOIRE, contractValidationBean);
    }
  }
}
