package com.cegedim.next.consumer.api.service;

import com.cegedim.next.consumer.api.config.TestConfiguration;
import com.cegedim.next.consumer.api.exception.ContractNotFound;
import com.cegedim.next.consumer.api.repositories.ContratAIRepository;
import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.IdClientBOService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
public class InsuredServiceTest {
  public static final String DATE_NAISSANCE = "20161101";
  public static final String RANG_NAISSANCE = "1";
  public static final String NIR_CODE = "2160631412621";
  public static final String NIR_CLE = "41";
  @Autowired MongoTemplate template;

  @Autowired InsuredService insuredService;
  @MockBean EventService eventService;

  @MockBean ContratAIRepository contratAIRepository;
  @MockBean IdClientBOService idClientBOService;

  private static final String ID_DECLARANT = "123456789";
  private static final String NUMERO_CONTRAT = "7894561";
  private static final String NUMERO_PERSONNE = "12345678";
  private static final String TRACE_ID = "852741";

  @Test
  void shouldUpdateInsuredTest()
      throws IdClientBoException, ContractNotFound, InterruptedException {
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");

    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(ID_DECLARANT);
    contratAIV6.setDateSouscription("2023-01-01");
    contratAIV6.setSocieteEmettrice("OS1");
    Assure assureV5 = new Assure();
    assureV5.setIdentite(DeclarantServiceTest.getIdentiteContrat());
    DroitAssure droitAssure = new DroitAssure();
    droitAssure.setPeriode(new Periode("2023-01-01", null));
    assureV5.setDroits(List.of(droitAssure));
    assureV5.setPeriodes(List.of(new Periode("2023-01-01", null)));
    contratAIV6.setAssures(List.of(assureV5));
    Mockito.when(contratAIRepository.findBy(ID_DECLARANT, NUMERO_CONTRAT, NUMERO_PERSONNE))
        .thenReturn(contratAIV6);
    Mockito.doNothing()
        .when(idClientBOService)
        .controlIdClientBO(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    Mockito.when(
            template.updateFirst(
                Mockito.any(Query.class), Mockito.any(Update.class), Mockito.eq(ContratAIV6.class)))
        .thenReturn(null);

    InsuredDataV5 insuredDataV5 = getInsuredDataV5();
    String result =
        insuredService.updateInsured(
            insuredDataV5, ID_DECLARANT, NUMERO_CONTRAT, NUMERO_PERSONNE, TRACE_ID);
    Assertions.assertNotNull(result);
  }

  @Test
  void shouldUpdateInsuredContractNotFoundTest() {
    ContractNotFound thrown =
        Assertions.assertThrows(
            ContractNotFound.class,
            () ->
                insuredService.updateInsured(
                    new InsuredDataV5(), ID_DECLARANT, NUMERO_CONTRAT, NUMERO_PERSONNE, TRACE_ID),
            "Expected ContractNotFound to be thrown but it didn't");

    Assertions.assertTrue(
        thrown
            .getMessage()
            .contains(
                "Le contrat "
                    + NUMERO_CONTRAT
                    + " de l'AMC "
                    + ID_DECLARANT
                    + " possédant le bénéficiaire ayant pour numéro de personne "
                    + NUMERO_PERSONNE
                    + " n'a pas été trouvé"));
  }

  @Test
  void shouldUpdateInsuredNotValidTest() throws IdClientBoException {
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");

    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(ID_DECLARANT);
    Mockito.when(contratAIRepository.findBy(ID_DECLARANT, NUMERO_CONTRAT, NUMERO_PERSONNE))
        .thenReturn(contratAIV6);
    Mockito.doNothing()
        .when(idClientBOService)
        .controlIdClientBO(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    InsuredDataV5 data = new InsuredDataV5();
    ValidationException thrown =
        Assertions.assertThrows(
            ValidationException.class,
            () ->
                insuredService.updateInsured(
                    data, ID_DECLARANT, NUMERO_CONTRAT, NUMERO_PERSONNE, TRACE_ID),
            "Expected IdClientBoException to be thrown but it didn't");

    Assertions.assertTrue(
        thrown.getMessage().contains("L'information dateNaissance est obligatoire"));
  }

  private static InsuredDataV5 getInsuredDataV5() {
    InsuredDataV5 insuredDataV5 = new InsuredDataV5();
    insuredDataV5.setDateNaissance(DATE_NAISSANCE);
    insuredDataV5.setRangNaissance(RANG_NAISSANCE);
    Nir nir = new Nir();
    nir.setCode(NIR_CODE);
    nir.setCle(NIR_CLE);
    insuredDataV5.setNir(nir);
    AdresseAssure adresse = new AdresseAssure();
    adresse.setLigne1("reu Berlioz");
    adresse.setCodePostal("31000");
    insuredDataV5.setAdresse(adresse);
    NomAssure nom = new NomAssure();
    nom.setNomFamille("Toulouse");
    nom.setPrenom("Marie");
    nom.setCivilite("Mle");
    insuredDataV5.setNom(nom);
    insuredDataV5.setContact(new Contact("0566666666", "0566666666", "test@gmail.com"));
    Periode periode = new Periode("2023-01-01", "2023-12-31");
    PeriodeDestinataire periodeDestinataire = new PeriodeDestinataire("2023-01-01", "2023-12-31");
    NirRattachementRO nirRattachementRO = new NirRattachementRO();
    nirRattachementRO.setNir(nir);
    nirRattachementRO.setPeriode(periode);
    List<NirRattachementRO> nirRattachementROList = new ArrayList<>();
    nirRattachementROList.add(nirRattachementRO);
    insuredDataV5.setAffiliationsRO(nirRattachementROList);
    DestinataireRelevePrestations destinataireRelevePrestationsV5 =
        new DestinataireRelevePrestations();
    destinataireRelevePrestationsV5.setIdDestinataireRelevePrestations("987654");
    List<DestinataireRelevePrestations> destinataireRelevePrestationsV5s = new ArrayList<>();
    destinataireRelevePrestationsV5s.add(destinataireRelevePrestationsV5);
    insuredDataV5.setDestinatairesRelevePrestations(destinataireRelevePrestationsV5s);
    DestinatairePrestations destinatairePrestationsV4 = new DestinatairePrestations();
    destinatairePrestationsV4.setIdDestinatairePaiements("654321");
    NomDestinataire nomDestinataireV3 = new NomDestinataire();
    nomDestinataireV3.setNomFamille("Toulouse");
    nomDestinataireV3.setPrenom("Marie");
    nomDestinataireV3.setCivilite("Mle");
    destinataireRelevePrestationsV5.setNom(nomDestinataireV3);
    Dematerialisation dematerialisation = new Dematerialisation();
    destinataireRelevePrestationsV5.setDematerialisation(dematerialisation);
    destinataireRelevePrestationsV5.setPeriode(periodeDestinataire);
    destinatairePrestationsV4.setNom(nomDestinataireV3);
    ModePaiement modePaiementPrestations = new ModePaiement();
    modePaiementPrestations.setCodeMonnaie("EUR");
    modePaiementPrestations.setLibelle("euro");
    modePaiementPrestations.setCode("EUR");
    destinatairePrestationsV4.setModePaiementPrestations(modePaiementPrestations);
    destinatairePrestationsV4.setPeriode(periodeDestinataire);
    List<DestinatairePrestations> destinatairePrestationsV4s = new ArrayList<>();
    destinatairePrestationsV4s.add(destinatairePrestationsV4);
    insuredDataV5.setDestinatairesPaiements(destinatairePrestationsV4s);
    return insuredDataV5;
  }
}
