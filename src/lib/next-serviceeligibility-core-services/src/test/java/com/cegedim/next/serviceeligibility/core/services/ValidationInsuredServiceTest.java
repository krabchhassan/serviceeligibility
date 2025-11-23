package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Dematerialisation;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.TestingDataForValidationService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ValidationInsuredServiceTest {

  @Autowired ValidationInsuredService service;

  @Test
  void should_validate_assureV5() {
    // nom.nomFamille obligatoire
    DataAssure data = TestingDataForValidationService.getDataAssureV5();
    data.getNom().setNomFamille(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information nom.nomFamille est obligatoire", e.getLocalizedMessage());
    }

    // nom.prenom obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getNom().setPrenom(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("L'information nom.prenom est obligatoire", e.getLocalizedMessage());
    }

    // nom.civilite obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getNom().setCivilite(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information nom.civilite est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).setNom(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom.nomFamille obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getNom().setNomFamille(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom.nomFamille est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom.prenom obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getNom().setPrenom(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom.prenom est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom.civilite obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getNom().setCivilite(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom.civilite est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.iban obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getRib().setIban(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.rib.iban est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.iban n'a pas la bonne longueur
    data = TestingDataForValidationService.getDataAssureV5();
    String iban = "ABCDEFGH";
    data.getDestinatairesPaiements().get(0).getRib().setIban(iban);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "la longueur de l'IBAN (" + iban + ") doit être comprise entre 14 et 34",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.iban doit être valide
    data = TestingDataForValidationService.getDataAssureV5();
    iban = "FR761254802998987654321091";
    data.getDestinatairesPaiements().get(0).getRib().setIban(iban);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("l'IBAN (" + iban + ") n'est pas valide.", e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.bic obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getRib().setBic(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.rib.bic est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).setModePaiementPrestations(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations.code obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getModePaiementPrestations().setCode(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations.code est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations.libelle obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getModePaiementPrestations().setLibelle(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations.libelle est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations.codeMonnaie
    // obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getModePaiementPrestations().setCodeMonnaie(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations.codeMonnaie est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.periode obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).setPeriode(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.periode est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.periode.debut obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesPaiements().get(0).getPeriode().setDebut(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).setNom(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom.nomFamille obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).getNom().setNomFamille(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom.nomFamille est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom.prenom obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).getNom().setPrenom(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom.prenom est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom.civilite obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).getNom().setCivilite(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom.civilite est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.periode obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).setPeriode(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.periode.debut obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).getPeriode().setDebut(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.dematerialisation obligatoire
    data = TestingDataForValidationService.getDataAssureV5();
    Dematerialisation dematerialisation1 =
        data.getDestinatairesRelevePrestations().get(0).getDematerialisation();
    data.getDestinatairesRelevePrestations().get(0).setDematerialisation(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.dematerialisation est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.dematerialisation.email ou mobile obligatoire
    // dans le cas d'une demat
    data = TestingDataForValidationService.getDataAssureV5();
    data.getDestinatairesRelevePrestations().get(0).setDematerialisation(dematerialisation1);
    Dematerialisation dematerialisation =
        data.getDestinatairesRelevePrestations().get(1).getDematerialisation();
    dematerialisation.setIsDematerialise(Boolean.TRUE);
    dematerialisation.setEmail(null);
    try {
      service.validateDataAssure(data, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire",
          e.getLocalizedMessage());
    }
  }

  @Test
  void should_validation_assureV5WithNewData() {
    InsuredDataV5 insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getNom().setNomFamille(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information nom.nomFamille est obligatoire", e.getLocalizedMessage());
    }

    // nom.prenom obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getNom().setPrenom(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("L'information nom.prenom est obligatoire", e.getLocalizedMessage());
    }

    // nom.civilite obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getNom().setCivilite(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information nom.civilite est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).setNom(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom.nomFamille obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getNom().setNomFamille(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom.nomFamille est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom.prenom obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getNom().setPrenom(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom.prenom est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.nom.civilite obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getNom().setCivilite(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.nom.civilite est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.iban obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getRib().setIban(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.rib.iban est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.bic obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getRib().setBic(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.rib.bic est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.rib.bic format
    insuredDataV5.getDestinatairesPaiements().get(0).getRib().setBic("XXX1");
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.rib.bic n'est pas conforme",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).setModePaiementPrestations(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations.code obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getModePaiementPrestations().setCode(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations.code est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations.libelle obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getModePaiementPrestations().setLibelle(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations.libelle est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.modePaiementPrestations.codeMonnaie
    // obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5
        .getDestinatairesPaiements()
        .get(0)
        .getModePaiementPrestations()
        .setCodeMonnaie(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.modePaiementPrestations.codeMonnaie est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesPaiements.periode obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).setPeriode(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.periode est obligatoire", e.getLocalizedMessage());
    }

    // destinatairesPaiements.periode.debut obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesPaiements().get(0).getPeriode().setDebut(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesPaiements.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesRelevePrestations().get(0).setNom(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom.nomFamille obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesRelevePrestations().get(0).getNom().setNomFamille(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom.nomFamille est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom.prenom obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesRelevePrestations().get(0).getNom().setPrenom(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom.prenom est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.nom.civilite obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesRelevePrestations().get(0).getNom().setCivilite(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.nom.civilite est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.periode obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesRelevePrestations().get(0).setPeriode(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.periode est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.periode.debut obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    insuredDataV5.getDestinatairesRelevePrestations().get(0).getPeriode().setDebut(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.periode.debut est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.dematerialisation obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();

    Dematerialisation dematerialisation1 =
        insuredDataV5.getDestinatairesRelevePrestations().get(0).getDematerialisation();
    insuredDataV5.getDestinatairesRelevePrestations().get(0).setDematerialisation(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information destinatairesRelevePrestations.dematerialisation est obligatoire",
          e.getLocalizedMessage());
    }

    // destinatairesRelevePrestations.dematerialisation.email ou mobile obligatoire
    // dans le cas d'une demat
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5
        .getDestinatairesRelevePrestations()
        .get(0)
        .setDematerialisation(dematerialisation1);
    Dematerialisation dematerialisation =
        insuredDataV5.getDestinatairesRelevePrestations().get(1).getDematerialisation();
    dematerialisation.setIsDematerialise(Boolean.TRUE);
    dematerialisation.setEmail(null);
    try {
      service.validateDataAssure(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire",
          e.getLocalizedMessage());
    }

    // date de naisssance obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.setDateNaissance(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information dateNaissance est obligatoire", e.getLocalizedMessage());
    }

    // date de naisssance fausse
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.setDateNaissance("S");
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information dateNaissance(S) n'est pas une date au format yyyyMMdd",
          e.getLocalizedMessage());
    }

    // rang de naisssance obligatoire
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.setRangNaissance(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information rangNaissance est obligatoire", e.getLocalizedMessage());
    }

    // nir
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.setNir(null);
    insuredDataV5.setAffiliationsRO(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("La requête doit posséder au moins un nir", e.getLocalizedMessage());
    }

    // nir
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    Nir nir = new Nir();
    nir.setCode(null);
    nir.setCle("12");
    insuredDataV5.setNir(nir);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("L'information nir.code est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    nir = new Nir();
    nir.setCode("313513535");
    nir.setCle(null);
    insuredDataV5.setNir(nir);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals("L'information nir.cle est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    nir = new Nir();
    nir.setCode("313513535");
    nir.setCle("12");
    insuredDataV5.setNir(nir);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "le NIR (313513535 / 12) de nir n'est pas valide.", e.getLocalizedMessage());
    }

    // affiliationRO
    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).getNir().setCode("435435435");
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "le NIR (435435435 / 44) de affiliationsRO.nir n'est pas valide.",
          e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).setNir(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.nir est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).getNir().setCode(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.nir.code est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).getNir().setCle(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.nir.cle est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).setPeriode(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.periode est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).getPeriode().setDebut(null);
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.periode.debut est obligatoire", e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).getPeriode().setDebut("123");
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.periode.debut(123) n'est pas une date au format yyyy-MM-dd",
          e.getLocalizedMessage());
    }

    insuredDataV5 = TestingDataForValidationService.getInsuredDataV5();
    insuredDataV5.getAffiliationsRO().get(0).getPeriode().setFin("fin");
    try {
      service.validateInsuredData(insuredDataV5, new ContractValidationBean());
    } catch (ValidationContractException e) {
      Assertions.assertEquals(
          "L'information affiliationsRO.periode.fin(fin) n'est pas une date au format yyyy-MM-dd",
          e.getLocalizedMessage());
    }
  }
}
