package com.cegedim.next.consumer.api.service;

import com.cegedim.next.consumer.api.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class RestContractServiceTpTest {

  @Autowired RestContratService restContratService;

  @Autowired MongoTemplate template;
  @MockBean TriggerCreationService triggerCreationService;
  @MockBean TraceService traceService;
  @MockBean TriggerService triggerService;
  private static final String ID_DECLARANT = "123456789";
  private static final String NUMERO_CONTRAT = "7894561";
  private static final String NUMERO_PERSONNE = "12345678";

  // TODO @Test
  void shouldDeleteContratTest() {
    ContratAIV6 contratAIV6 = getContratAIV6(DeclarantServiceTest.getIdentiteContrat());

    Mockito.when(
            template.findOne(
                Mockito.any(Query.class), Mockito.eq(ContratAIV6.class), Mockito.anyString()))
        .thenReturn(contratAIV6);

    Mockito.when(
            triggerCreationService.generateTriggerFromContracts(
                null, contratAIV6, true, null, true))
        .thenReturn("123456");

    Mockito.when(template.remove(contratAIV6)).thenReturn(null);

    BenefAIV5 benef = new BenefAIV5();
    List<String> services = new ArrayList<>();
    services.add(Constants.SERVICE_TP);
    services.add(Constants.SERVICE_PRESTATION);
    benef.setServices(services);
    benef.setIdentite(DeclarantServiceTest.getIdentiteContrat());
    ContratV5 contrat1 = new ContratV5();
    contrat1.setNumeroContrat(NUMERO_CONTRAT);
    ContratV5 contrat2 = new ContratV5();
    contrat2.setNumeroContrat("456123");
    List<ContratV5> contratV5s = new ArrayList<>();
    contratV5s.add(contrat1);
    contratV5s.add(contrat2);
    benef.setContrats(contratV5s);
    Mockito.when(
            template.findOne(
                Mockito.any(Query.class),
                Mockito.eq(BenefAIV5.class),
                Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME)))
        .thenReturn(benef);

    ContratAIV6 result =
        restContratService.deleteContrat(ID_DECLARANT, NUMERO_CONTRAT, NUMERO_PERSONNE);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("6", result.getVersion());
    Assertions.assertEquals(ID_DECLARANT, result.getIdDeclarant());
  }

  private static ContratAIV6 getContratAIV6(IdentiteContrat identiteContrat) {
    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(ID_DECLARANT);
    Assure assure = new Assure();
    assure.setIdentite(identiteContrat);
    assure.setData(new DataAssure());
    contratAIV6.setAssures(List.of(assure));
    return contratAIV6;
  }
}
