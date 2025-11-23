package com.cegedim.next.serviceeligibility.core.services;

import static org.mockito.Mockito.doAnswer;

import com.cegedim.beyond.schemas.*;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CartePapier;
import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.ContratIJ;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Oc;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trigger.EventServiceUtilsTest;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class EventServiceTest {

  @SpyBean private EventService eventService;

  private BenefAIV5 getBenef() {
    BenefAIV5 benef = new BenefAIV5();
    ContratV5 contract = new ContratV5();
    DataAssure data = new DataAssure();
    NomAssure nomAssure = new NomAssure();
    IdentiteContrat id = new IdentiteContrat();
    Nir nir = new Nir();
    Amc amc = new Amc();

    benef.setServices(List.of("servicePrestation"));
    benef.setKey("5487953265-test-19791006-1");
    nomAssure.setNomFamille("DELMOTTE");
    nomAssure.setPrenom("JEAN PIERRE");
    id.setDateNaissance("19791006");
    id.setRangNaissance("1");
    nir.setCode("1701062498046");
    amc.setIdDeclarant("5487953265");
    benef.setNumeroAdherent("test");

    data.setNom(nomAssure);
    contract.setData(data);
    benef.setContrats(List.of(contract));
    id.setNir(nir);
    benef.setIdentite(id);
    benef.setAmc(amc);

    return benef;
  }

  private BenefAIV5 getBenefNull() {
    BenefAIV5 benef = new BenefAIV5();
    ContratV5 contract = new ContratV5();
    DataAssure data = new DataAssure();
    NomAssure nomAssure = new NomAssure();
    IdentiteContrat id = new IdentiteContrat();
    Nir nir = new Nir();
    Amc amc = new Amc();

    benef.setServices(List.of(""));
    benef.setKey("");
    nomAssure.setNomFamille("");
    nomAssure.setPrenom("");
    id.setDateNaissance("");
    id.setRangNaissance("");
    nir.setCode("");
    amc.setIdDeclarant("");
    benef.setNumeroAdherent("");

    data.setNom(nomAssure);
    contract.setData(data);
    benef.setContrats(List.of(contract));
    id.setNir(nir);
    benef.setIdentite(id);
    benef.setAmc(amc);

    return benef;
  }

  private ContratAIV5 getContrat() {
    ContratAIV5 contrat = new ContratAIV5();
    contrat.setId("32103203013");
    contrat.setIdDeclarant("5487953265");
    contrat.setTraceId("65299555");
    contrat.setNumero("555484848");
    return contrat;
  }

  private PrestIJ getPrestij() {
    PrestIJ prestij = new PrestIJ();
    prestij.setTraceId("310321032");
    prestij.set_id("3013366454987989");
    ContratIJ contratij = new ContratIJ();
    contratij.setNumero("CIJ3210321032");
    contratij.setNumeroAdherent("ADH65046540987");
    prestij.setContrat(contratij);
    Oc oc = new Oc();
    oc.setIdClientBO("IDCBO203210321");
    prestij.setOc(oc);
    return prestij;
  }

  private Declaration getDeclaration() {
    Declaration declaration = new Declaration();
    declaration.setIdDeclarant("5487953266");
    Contrat contrat = new Contrat();
    contrat.setNumero("3210321032");
    contrat.setNumeroAdherent("65046540987");
    declaration.setContrat(contrat);
    BeneficiaireV2 beneficiaire = new BeneficiaireV2();
    beneficiaire.setNumeroPersonne("123456789");
    beneficiaire.setNirBeneficiaire("1701062498046");
    beneficiaire.setDateNaissance("19791006");
    beneficiaire.setRangNaissance("1");
    declaration.setBeneficiaire(beneficiaire);
    declaration.setCodeEtat("V");
    declaration.setNomFichierOrigine("NomFichierTest");
    return declaration;
  }

  private <T extends Event> void assertEvent(Class<T> clazz, Consumer<T> assertions) {
    doAnswer(
            answer -> {
              T event = answer.getArgument(0);
              assertions.accept(event);
              return null;
            })
        .when(eventService)
        .sendObservabilityEvent(Mockito.any(clazz));
  }

  @Test
  void createObservabilityEventBeneficiaryCreationTest() {
    assertEvent(
        BeneficiaryCreationEventDto.class,
        benefEvent -> {
          Assertions.assertNotNull(benefEvent);
          Assertions.assertEquals("servicePrestation", benefEvent.getService());
          Assertions.assertEquals("5487953265-test-19791006-1", benefEvent.getKey());
          Assertions.assertEquals("DELMOTTE", benefEvent.getLastName());
          Assertions.assertEquals("JEAN PIERRE", benefEvent.getFirstName());
          Assertions.assertEquals("19791006", benefEvent.getBirthDate());
          Assertions.assertEquals("1", benefEvent.getBirthRank());
          Assertions.assertEquals("1701062498046", benefEvent.getNir());
          Assertions.assertEquals("5487953265", benefEvent.getDeclarantId());
          Assertions.assertEquals("test", benefEvent.getSubscriberNumber());
        });
    eventService.sendObservabilityEventBeneficiaryCreation(getBenef());
  }

  @Test
  void createObservabilityEventBeneficiaryCreationNullTest() {
    BenefAIV5 benef = getBenefNull();
    benef.setServices(null);
    benef.getContrats().get(0).setData(null);
    benef.setIdentite(null);
    benef.setAmc(null);
    assertEvent(
        BeneficiaryCreationEventDto.class,
        benefEvent -> {
          Assertions.assertNotNull(benefEvent);
          Assertions.assertEquals("", benefEvent.getService());
          Assertions.assertEquals("", benefEvent.getLastName());
          Assertions.assertEquals("", benefEvent.getFirstName());
          Assertions.assertEquals("", benefEvent.getBirthDate());
          Assertions.assertEquals("", benefEvent.getBirthRank());
          Assertions.assertEquals("", benefEvent.getNir());
          Assertions.assertEquals("", benefEvent.getDeclarantId());
        });
    eventService.sendObservabilityEventBeneficiaryCreation(benef);
  }

  @Test
  void sendObservabilityEventContractDeleteTest() {
    assertEvent(
        ContractSuppressionEventDto.class,
        contractEvent -> {
          Assertions.assertNotNull(contractEvent);
          Assertions.assertEquals("555484848", contractEvent.getNumber());
          Assertions.assertEquals("5487953265", contractEvent.getDeclarantId());
          Assertions.assertEquals("65299555", contractEvent.getTraceId());
        });
    eventService.sendObservabilityEventContractDelete(getContrat());
  }

  @Test
  void sendObservabilityEventInsuredTest() {
    assertEvent(
        InsuredReceptionEventDto.class,
        insuredEvent -> {
          Assertions.assertNotNull(insuredEvent);
          Assertions.assertEquals("99663322", insuredEvent.getContractId());
        });
    eventService.sendObservabilityEventInsured("99663322");
  }

  @Test
  void sendObservabilityEventInsuredInvalidTest() {
    assertEvent(
        InsuredInvalidReceptionEventDto.class,
        insuredEvent -> {
          Assertions.assertNotNull(insuredEvent);
          Assertions.assertEquals("6633225511", insuredEvent.getDeclarantId());
          Assertions.assertEquals("66336622", insuredEvent.getContractNumber());
          Assertions.assertEquals("200112011", insuredEvent.getBirthDateAndRank());
        });
    eventService.sendObservabilityEventInsuredInvalid(
        "6633225511", "66336622", "20011201", "1", "rien");
  }

  @Test
  void sendObservabilityEventPrestijReceptionTest() {
    assertEvent(
        PrestijReceptionEventDto.class,
        prestijEvent -> {
          Assertions.assertNotNull(prestijEvent);
          Assertions.assertEquals("310321032", prestijEvent.getTraceId());
        });
    eventService.sendObservabilityEventPrestijReception(getPrestij());
  }

  @Test
  void sendObservabilityEventPrestijCreationTest() {
    assertEvent(
        PrestijCreationEventDto.class,
        prestijEvent -> {
          Assertions.assertNotNull(prestijEvent);
          Assertions.assertEquals("3013366454987989", prestijEvent.getId());
          Assertions.assertEquals("CIJ3210321032", prestijEvent.getNumber());
          Assertions.assertEquals("ADH65046540987", prestijEvent.getSubscriberNumber());
          Assertions.assertEquals("IDCBO203210321", prestijEvent.getDeclarantId());
          Assertions.assertEquals("310321032", prestijEvent.getTraceId());
        });
    eventService.sendObservabilityEventPrestijCreation(getPrestij());
  }

  @Test
  void createObservabilityEventDeclarationTest() {
    assertEvent(
        DeclarationCreationEventDto.class,
        declarationEvent -> {
          Assertions.assertNotNull(declarationEvent);
          Assertions.assertEquals("5487953266", declarationEvent.getDeclarantId());
          Assertions.assertEquals("3210321032", declarationEvent.getContractNumber());
          Assertions.assertEquals("65046540987", declarationEvent.getSubscriberNumber());
          Assertions.assertEquals("123456789", declarationEvent.getPersonNumber());
          Assertions.assertEquals("1701062498046", declarationEvent.getNir());
          Assertions.assertEquals("19791006", declarationEvent.getBirthDate());
          Assertions.assertEquals("1", declarationEvent.getBirthRank());
          Assertions.assertEquals("V", declarationEvent.getCodeEtat());
          Assertions.assertEquals("NomFichierTest", declarationEvent.getFileName());
        });

    eventService.sendObservabilityEventDeclarationCreation(getDeclaration());
  }

  private static DeclarationConsolide getDeclarationConsolide() {
    DeclarationConsolide declarationConsolide = new DeclarationConsolide();
    declarationConsolide.setIdDeclarant("000000001");
    declarationConsolide.setIdDeclarations("000000011");
    declarationConsolide.setPeriodeDebut("2024/01/01");
    declarationConsolide.setPeriodeFin("2024/12/31");
    declarationConsolide.setCodeServices(List.of("CARTE_TP"));
    Contrat contrat = new Contrat();
    contrat.setNumero("1111111");
    contrat.setNumeroAdherent("222222222");
    contrat.setGestionnaire("333333333");
    declarationConsolide.setContrat(contrat);
    Beneficiaire beneficiaire = new Beneficiaire();
    beneficiaire.setNumeroPersonne("000000111");
    declarationConsolide.setBeneficiaire(beneficiaire);
    return declarationConsolide;
  }

  private static CarteDemat getCarteDemat() {
    CarteDemat carteDemat = new CarteDemat();
    carteDemat.setIdDeclarant("000000001");
    carteDemat.setPeriodeDebut("2024/01/01");
    carteDemat.setPeriodeFin("2024/12/31");
    Contrat contrat = new Contrat();
    contrat.setNumero("1111111");
    contrat.setNumeroAdherent("222222222");
    contrat.setGestionnaire("333333333");
    carteDemat.setContrat(contrat);
    BenefCarteDemat beneficiaire = new BenefCarteDemat();
    Beneficiaire benef = new Beneficiaire();
    benef.setNumeroPersonne("000000111");
    beneficiaire.setBeneficiaire(benef);
    carteDemat.setBeneficiaires(List.of(beneficiaire));
    return carteDemat;
  }

  private static CartePapier getCartePapier() {
    CartePapier cartePapier = new CartePapier();
    cartePapier.setNumeroAMC("000000001");
    cartePapier.setPeriodeDebut("2024/01/01");
    cartePapier.setPeriodeFin("2024/12/31");
    Contrat contrat = new Contrat();
    contrat.setNumero("1111111");
    contrat.setNumeroAdherent("222222222");
    contrat.setGestionnaire("333333333");
    cartePapier.setContrat(contrat);
    BenefCarteDemat beneficiaire = new BenefCarteDemat();
    Beneficiaire benef = new Beneficiaire();
    benef.setNumeroPersonne("000000111");
    beneficiaire.setBeneficiaire(benef);
    cartePapier.setBeneficiaires(List.of(beneficiaire));
    return cartePapier;
  }

  @Test
  void sendObservabilityEventConsolidationDeclarationTest() {
    assertEvent(
        DeclarationConsoEventDto.class,
        declaration -> {
          Assertions.assertNotNull(declaration);
          Assertions.assertEquals("000000001", declaration.getDeclarantId());
          Assertions.assertEquals("000000011", declaration.getDeclarationsId());
          Assertions.assertEquals(List.of("CARTE_TP"), declaration.getServiceCodes());
          Assertions.assertEquals("1111111", declaration.getContractNumber());
          Assertions.assertEquals("333333333", declaration.getIssuingCompanyCode());
          Assertions.assertEquals("222222222", declaration.getSubscriberNumber());
          Assertions.assertEquals("000000111", declaration.getPersonNumber());
          Assertions.assertEquals("2024-01-01", declaration.getStartDate());
          Assertions.assertEquals("2024-12-31", declaration.getEndDate());
        });
    eventService.sendObservabilityEventConsolidationDeclaration(getDeclarationConsolide());
  }

  @Test
  void sendObservabilityEventCarteDematTest() {
    assertEvent(
        CartetpDematCreationEventDto.class,
        carteDematEvent -> {
          Assertions.assertNotNull(carteDematEvent);
          Assertions.assertEquals("000000001", carteDematEvent.getDeclarantId());
          Assertions.assertEquals("1111111", carteDematEvent.getContractNumber());
          Assertions.assertEquals("333333333", carteDematEvent.getIssuingCompanyCode());
          Assertions.assertEquals("222222222", carteDematEvent.getSubscriberNumber());
          Assertions.assertEquals("2024-01-01", carteDematEvent.getStartDate());
          Assertions.assertEquals("2024-12-31", carteDematEvent.getEndDate());
          Assertions.assertEquals(1, carteDematEvent.getNbBeneficiaries());
        });
    eventService.sendObservabilityEvent(
        eventService.prepareObservabilityEventCarteDemat(getCarteDemat()));
  }

  @Test
  void sendObservabilityEventCartePapierTest() {
    assertEvent(
        CartetpPapierGenerateEventDto.class,
        cartePapierEvent -> {
          Assertions.assertNotNull(cartePapierEvent);
          Assertions.assertEquals("000000001", cartePapierEvent.getDeclarantId());
          Assertions.assertEquals("1111111", cartePapierEvent.getContractNumber());
          Assertions.assertEquals("333333333", cartePapierEvent.getIssuingCompanyCode());
          Assertions.assertEquals("222222222", cartePapierEvent.getSubscriberNumber());
          Assertions.assertEquals("2024-01-01", cartePapierEvent.getStartDate());
          Assertions.assertEquals("2024-12-31", cartePapierEvent.getEndDate());
          Assertions.assertEquals(1, cartePapierEvent.getNbBeneficiaries());
        });
    eventService.sendObservabilityEventCartePapier(getCartePapier());
  }

  @Test
  void createObservabilityEventCartesDematActiveTest() {
    assertEvent(
        CartetpDematActiveEventDto.class,
        cartesDematActiveEvent -> {
          Assertions.assertNotNull(cartesDematActiveEvent);
          Assertions.assertEquals("0000401166", cartesDematActiveEvent.getDeclarantId());
          Assertions.assertEquals(20, cartesDematActiveEvent.getNbActiveDematerializedCards());
          Assertions.assertEquals("2024-04-01", cartesDematActiveEvent.getProcessingDate());
        });
    eventService.sendObservabilityEventCartesDematActivesCount("0000401166", "2024/04/01", 20);
  }

  @Test
  void createObservabilityEventAlmerysProductCreationTest() {
    assertEvent(
        AlmerysProductCreationEventDto.class,
        almerysProductCreationEventDto -> {
          Assertions.assertNotNull(almerysProductCreationEventDto);
          Assertions.assertEquals("PDT_ALM1", almerysProductCreationEventDto.getCode());
          Assertions.assertEquals("test", almerysProductCreationEventDto.getDescription());
          Assertions.assertEquals("Période ", almerysProductCreationEventDto.getCombinationList());
        });
    AlmerysProduct almerysProduct = EventServiceUtilsTest.getAlmerysProduct(false);
    eventService.sendObservabilityEventAlmerysProductCreation(almerysProduct, "test");
  }

  @Test
  void createObservabilityEventAlmerysProductModificationTest() {
    assertEvent(
        AlmerysProductModificationEventDto.class,
        almerysProductModificationEventDto -> {
          Assertions.assertNotNull(almerysProductModificationEventDto);
          Assertions.assertEquals("PDT_ALM1", almerysProductModificationEventDto.getCode());
          Assertions.assertEquals("test", almerysProductModificationEventDto.getDescription());
          Assertions.assertEquals(
              "Période ", almerysProductModificationEventDto.getCombinationList());
        });
    AlmerysProduct almerysProduct = EventServiceUtilsTest.getAlmerysProduct(false);
    AlmerysProduct almerysProduct2 = EventServiceUtilsTest.getAlmerysProduct(true);
    eventService.sendObservabilityEventAlmerysProductModification(
        almerysProduct, almerysProduct2, "test");
  }
}
