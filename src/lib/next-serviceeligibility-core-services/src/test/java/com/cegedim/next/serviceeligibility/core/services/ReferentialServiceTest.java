package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.referential.Referential;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import jakarta.validation.ValidationException;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ReferentialServiceTest {

  @Autowired private ValidationContratService validationContratService;

  @Autowired private RestConnector restConnector;

  @Test
  void test_validateContratReferentiel() {
    ContratAIV6 contratAI = new ContratAIV6();
    ContratCollectifV6 contratCollectifV6 = new ContratCollectifV6();
    contratCollectifV6.setGroupePopulation("ABC");
    contratAI.setContratCollectif(contratCollectifV6);
    Assure a = new Assure();
    IdentiteContrat i = new IdentiteContrat();
    i.setNumeroPersonne("123");
    i.setDateNaissance("20000101");
    i.setRangNaissance("1");
    a.setIdentite(i);
    contratAI.setAssures(List.of(a));
    Referential r = new Referential();
    r.setCode("ContratCollectif.GroupePopulation");
    r.setAuthorizedValues(List.of("ABC"));

    validationContratService.validateContratReferentiel(
        contratAI, List.of(r), new ContractValidationBean());
    try {
      validationContratService.validateContratReferentiel(
          contratAI, List.of(r), new ContractValidationBean());
    } catch (ValidationException e) {
      Assertions.fail();
    }

    r.setCode("ContratCollectif.GroupePopulation2");

    validationContratService.validateContratReferentiel(
        contratAI, List.of(r), new ContractValidationBean());
    try {
      validationContratService.validateContratReferentiel(
          contratAI, List.of(r), new ContractValidationBean());
    } catch (ValidationException e) {
      Assertions.assertEquals(
          "Validation du contrôle ContratCollectif.GroupePopulation2 impossible",
          e.getLocalizedMessage());
    }

    r.setCode("ContratCollectif.GroupePopulation");
    r.setAuthorizedValues(List.of("CBA"));

    validationContratService.validateContratReferentiel(
        contratAI, List.of(r), new ContractValidationBean());
    try {
      validationContratService.validateContratReferentiel(
          contratAI, List.of(r), new ContractValidationBean());
    } catch (ValidationException e) {
      Assertions.assertEquals(
          "L'information ContratCollectif.GroupePopulation n'a pas une valeur comprise dans la liste : CBA",
          e.getLocalizedMessage());
    }

    r.setCode("Assures.RangAdministratif");
    r.setAuthorizedValues(List.of("A"));

    validationContratService.validateContratReferentiel(
        contratAI, List.of(r), new ContractValidationBean());
    try {
      validationContratService.validateContratReferentiel(
          contratAI, List.of(r), new ContractValidationBean());
    } catch (ValidationException e) {
      Assertions.assertEquals(
          "Pour l'assuré numéro personne 123, date de naissance 20000101, rang de naissance 1 : l'information Assures.RangAdministratif n'a pas une valeur comprise dans la liste : A",
          e.getLocalizedMessage());
    }

    PeriodeSuspension p = new PeriodeSuspension();
    p.setTypeSuspension("Provisoire");
    contratAI.setPeriodesSuspension(List.of(p));
    r.setCode("PeriodesSuspension.TypeSuspension");
    r.setAuthorizedValues(List.of("Provisoire"));

    validationContratService.validateContratReferentiel(
        contratAI, List.of(r), new ContractValidationBean());
    try {
      validationContratService.validateContratReferentiel(
          contratAI, List.of(r), new ContractValidationBean());
    } catch (ValidationException e) {
      Assertions.fail();
    }

    contratAI.setQualification("B");
    r.setCode("Qualification");
    r.setAuthorizedValues(List.of("B"));

    validationContratService.validateContratReferentiel(
        contratAI, List.of(r), new ContractValidationBean());
    try {
      validationContratService.validateContratReferentiel(
          contratAI, List.of(r), new ContractValidationBean());
    } catch (ValidationException e) {
      Assertions.fail();
    }
  }

  @Test
  void test_getReferential() {
    BeyondPropertiesService beyondPropertiesService = Mockito.mock(BeyondPropertiesService.class);
    Mockito.when(beyondPropertiesService.getPropertyOrThrowError(CLIENT_ID))
        .thenReturn("testClient");
    Mockito.when(beyondPropertiesService.getPropertyOrThrowError(CLIENT_SECRET))
        .thenReturn("testSecret");

    ReferentialService refService = new ReferentialService(restConnector, beyondPropertiesService);

    JSONObject j =
        new JSONObject(
            "{\"data\": [{\"value\": {\"code\": \"Qualification\",\"label\": \"Qualification\",\"authorizedValues\": \"B\"}}]}");
    Mockito.when(restConnector.fetchObject(Mockito.anyString(), Mockito.any())).thenReturn(j);
    List<Referential> rs = refService.getReferential("");
    Assertions.assertEquals(1, rs.size());
    Assertions.assertEquals("Qualification", rs.getFirst().getCode());
    Assertions.assertEquals(1, rs.getFirst().getAuthorizedValues().size());
    Assertions.assertEquals("B", rs.getFirst().getAuthorizedValues().getFirst());
  }
}
