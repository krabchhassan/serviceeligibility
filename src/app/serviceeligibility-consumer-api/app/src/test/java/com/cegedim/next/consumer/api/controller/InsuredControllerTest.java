package com.cegedim.next.consumer.api.controller;

import com.cegedim.next.consumer.api.config.TestConfiguration;
import com.cegedim.next.consumer.api.exception.ContractNotFound;
import com.cegedim.next.consumer.api.repositories.ContratAIRepository;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import jakarta.validation.ValidationException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class InsuredControllerTest {
  @Autowired MongoTemplate mongoTemplate;

  @Autowired private InsuredController insuredController;

  @MockBean public TraceService traceService;

  @MockBean public ContratAIRepository contratAIRepository;

  @Autowired public AuthenticationFacade authenticationFacade;

  private String idDeclarant = "0032165199";
  private String numeroContrat = "8343484392";
  private String numeroPersonne = "288939000";

  private String traceId = "649019cd9109ce0da51793b8";

  @Test
  void manageInsuredSendingV5Test_no_idDeclarant() {
    InsuredDataV5 body = getInsuredDataV5();
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.any(List.class),
            Mockito.anyString());

    ValidationException thrown =
        Assertions.assertThrows(
            ValidationException.class,
            () ->
                insuredController.manageInsuredSendingV5(
                    "", numeroContrat, numeroPersonne, body, traceId),
            "Expected ValidationException to be thrown but it didn't");

    Assertions.assertTrue(thrown.getMessage().contains("Champ requis: idDeclarant"));
  }

  @Test
  void manageInsuredSendingV5Test_no_contract_found() {
    InsuredDataV5 body = getInsuredDataV5();
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.any(List.class),
            Mockito.anyString());

    ContractNotFound thrown =
        Assertions.assertThrows(
            ContractNotFound.class,
            () ->
                insuredController.manageInsuredSendingV5(
                    idDeclarant, numeroContrat, numeroPersonne, body, traceId),
            "Expected ContractNotFound to be thrown but it didn't");

    Assertions.assertTrue(
        thrown
            .getMessage()
            .contains(
                "Le contrat 8343484392 de l'AMC 0032165199 possédant le bénéficiaire ayant pour numéro de personne 288939000 n'a pas été trouvé"));
  }

  @Test
  void manageInsuredSendingV5Test_no_amc_in_contract() {
    InsuredDataV5 body = getInsuredDataV5();
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.anyList(),
            Mockito.anyString());
    Mockito.when(contratAIRepository.findBy(idDeclarant, numeroContrat, numeroPersonne))
        .thenReturn(new ContratAIV6());
    IdClientBoException thrown =
        Assertions.assertThrows(
            IdClientBoException.class,
            () ->
                insuredController.manageInsuredSendingV5(
                    idDeclarant, numeroContrat, numeroPersonne, body, traceId),
            "Expected IdClientBoException to be thrown but it didn't");

    Assertions.assertTrue(
        thrown.getMessage().contains("Incohérence entre les déclarants (Contrat/URL)"));
  }

  @Test
  void manageInsuredSendingV5Test_unknown_amc() {
    InsuredDataV5 body = getInsuredDataV5();
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.any(List.class),
            Mockito.anyString());
    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(idDeclarant);
    Mockito.when(contratAIRepository.findBy(idDeclarant, numeroContrat, numeroPersonne))
        .thenReturn(contratAIV6);
    IdClientBoException thrown =
        Assertions.assertThrows(
            IdClientBoException.class,
            () ->
                insuredController.manageInsuredSendingV5(
                    idDeclarant, numeroContrat, numeroPersonne, body, traceId),
            "Expected IdClientBoException to be thrown but it didn't");

    Assertions.assertTrue(thrown.getMessage().contains("Le déclarant 0032165199 n'existe pas !"));
  }

  @Test
  void manageInsuredSendingV5Test()
      throws IdClientBoException, ContractNotFound, InterruptedException {
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("Unidentified");
    Declarant declarant = new Declarant();
    declarant.setIdClientBO("Unidentified");
    Mockito.when(mongoTemplate.findById(idDeclarant, Declarant.class)).thenReturn(declarant);
    InsuredDataV5 body = getInsuredDataV5();
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.anyList(),
            Mockito.anyString());
    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(idDeclarant);
    Mockito.when(contratAIRepository.findBy(idDeclarant, numeroContrat, numeroPersonne))
        .thenReturn(contratAIV6);
    String sendResponse =
        insuredController.manageInsuredSendingV5(
            idDeclarant, numeroContrat, numeroPersonne, body, traceId);
    Assertions.assertEquals("", sendResponse);
  }

  private static InsuredDataV5 getInsuredDataV5() {
    InsuredDataV5 insuredDataV5 = new InsuredDataV5();
    insuredDataV5.setDateNaissance("19791007");
    insuredDataV5.setRangNaissance("1");
    Nir nir = new Nir("2160631412621", "41");
    insuredDataV5.setNir(nir);
    NirRattachementRO affiliationRO1 = new NirRattachementRO();
    Nir nir1 = new Nir();
    nir1.setCode("1701062498046");
    nir1.setCle("02");
    affiliationRO1.setNir(nir1);
    RattachementRO rattachementRO1 = new RattachementRO();
    rattachementRO1.setCodeCaisse("626");
    rattachementRO1.setCodeCentre("0156");
    rattachementRO1.setCodeRegime("01");
    affiliationRO1.setRattachementRO(rattachementRO1);
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    periode1.setFin("2021-12-31");
    affiliationRO1.setPeriode(periode1);
    NirRattachementRO affiliationRO2 = new NirRattachementRO();
    Nir nir2 = new Nir();
    nir2.setCode("1701062498046");
    nir2.setCle("02");
    affiliationRO2.setNir(nir2);
    RattachementRO rattachementRO2 = new RattachementRO();
    rattachementRO2.setCodeCaisse("596");
    rattachementRO2.setCodeCentre("0124");
    rattachementRO2.setCodeRegime("03");
    affiliationRO2.setRattachementRO(rattachementRO2);
    Periode periode2 = new Periode();
    periode2.setDebut("2024-01-01");
    periode2.setFin("2024-12-31");
    affiliationRO2.setPeriode(periode2);
    insuredDataV5.setAffiliationsRO(List.of(affiliationRO1, affiliationRO2));

    DestinatairePrestations destPaiement1 = new DestinatairePrestations();
    destPaiement1.setIdDestinatairePaiements("123456");
    NomDestinataire nom1 = new NomDestinataire();
    nom1.setCivilite("Mr");
    nom1.setNomFamille("DELMOTTE");
    nom1.setPrenom("JEAN PIERRE");
    destPaiement1.setNom(nom1);
    Adresse adresse1 = new Adresse();
    adresse1.setLigne1("25 RUE DES LAGRADES");
    adresse1.setLigne2("RESIDENCE DES PLATEAUX");
    adresse1.setLigne3("59250");
    adresse1.setLigne4("HALLUIN");
    adresse1.setLigne5("F");
    adresse1.setLigne6("FRANCE");
    adresse1.setLigne7("CEDEX41");
    adresse1.setCodePostal("5920");
    destPaiement1.setAdresse(adresse1);
    RibAssure ribAssure1 = new RibAssure();
    ribAssure1.setBic("MCCFFRP1");
    ribAssure1.setIban("FR3330002005500000157841Z25");
    destPaiement1.setRib(ribAssure1);
    ModePaiement modePaiement1 = new ModePaiement();
    modePaiement1.setCode("VIR");
    modePaiement1.setLibelle("Virement");
    modePaiement1.setCodeMonnaie("EUR");
    destPaiement1.setModePaiementPrestations(modePaiement1);
    PeriodeDestinataire periode3 = new PeriodeDestinataire();
    periode3.setDebut("2020-01-01");
    periode3.setFin("2020-06-30");
    destPaiement1.setPeriode(periode3);

    DestinatairePrestations destPaiement2 = new DestinatairePrestations();
    destPaiement2.setIdDestinatairePaiements("123457");
    NomDestinataire nom2 = new NomDestinataire();
    nom2.setRaisonSociale("GDP");
    destPaiement2.setNom(nom2);
    Adresse adresse2 = new Adresse();
    adresse2.setLigne1("284 RUE DU PLATEAU");
    adresse2.setLigne6("FRANCE");
    adresse2.setLigne7("62138 HAISNES");
    adresse2.setCodePostal("62138");
    destPaiement2.setAdresse(adresse2);
    destPaiement2.setRib(ribAssure1);
    destPaiement2.setModePaiementPrestations(modePaiement1);
    PeriodeDestinataire periode4 = new PeriodeDestinataire();
    periode4.setDebut("2020-07-01");
    destPaiement2.setPeriode(periode4);

    insuredDataV5.setDestinatairesPaiements(List.of(destPaiement1, destPaiement2));

    DestinataireRelevePrestations destPrest1 = new DestinataireRelevePrestations();
    destPrest1.setIdDestinataireRelevePrestations("83747438-288939000-DestRelevePrest-1");
    Dematerialisation dematerialisation = new Dematerialisation();
    dematerialisation.setIsDematerialise(false);
    destPrest1.setDematerialisation(dematerialisation);
    NomDestinataire nom3 = new NomDestinataire();
    nom3.setCivilite("MR");
    nom3.setNomFamille("DELMOTTE");
    nom3.setPrenom("JEAN PIERRE");
    destPrest1.setNom(nom3);

    AdresseAssure adresseAssure = new AdresseAssure();
    adresseAssure.setLigne1("25 RUE DES LAGRADES");
    adresseAssure.setLigne2("RESIDENCE DES PLATEAUX");
    adresseAssure.setLigne3("59250");
    adresseAssure.setLigne4("HALLUIN");
    adresseAssure.setLigne5("F");
    adresseAssure.setLigne6("FRANCE");
    adresseAssure.setLigne7("CEDEX41");
    adresseAssure.setCodePostal("5920");

    destPrest1.setAdresse(adresseAssure);
    destPrest1.setPeriode(periode3);

    DestinataireRelevePrestations destPrest2 = new DestinataireRelevePrestations();
    destPrest2.setIdDestinataireRelevePrestations("83747438-288939000-DestRelevePrest-2");
    destPrest2.setDematerialisation(dematerialisation);
    destPrest2.setNom(nom2);

    AdresseAssure adresseAssure2 = new AdresseAssure();
    adresseAssure2.setLigne1("284 RUE DU PLATEAU");
    adresseAssure2.setLigne6("FRANCE");
    adresseAssure2.setLigne7("62138 HAISNES");
    adresseAssure2.setCodePostal("62138");

    destPrest2.setAdresse(adresseAssure2);
    destPrest2.setPeriode(periode4);

    insuredDataV5.setDestinatairesRelevePrestations(List.of(destPrest1, destPrest2));
    NomAssure nomAssure = new NomAssure();
    nomAssure.setCivilite("MR");
    nomAssure.setNomFamille("DELMOTTE");
    nomAssure.setPrenom("JEAN PIERRE");
    insuredDataV5.setNom(nomAssure);
    insuredDataV5.setAdresse(adresseAssure);
    Contact contact = new Contact();
    contact.setFixe("0321992364");
    contact.setMobile("0735427677");
    contact.setEmail("jp-delmotte@gagamil.coum");
    insuredDataV5.setContact(contact);
    return insuredDataV5;
  }
}
