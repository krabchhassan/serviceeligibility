package com.cegedim.next.triggerrenouvellement.worker.kafka;

import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.triggerrenouvellement.worker.config.TestConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.stream.Streams;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class TriggerRenouvellementConsumerTest {

  @Autowired private ConsumerDemandeRenouv consumerDemandeRenouv;

  @SpyBean private MongoTemplate mongoTemplate;

  @Autowired private TriggerDao triggerDao;

  @SpyBean private AuthenticationFacade authenticationFacade;
  private String triggerId = "triggerId";
  List<ParametrageCarteTP> databaseParametrageCarteTP = new ArrayList<>();

  @BeforeEach
  public void before() {

    Mockito.doReturn("JUNIT").when(authenticationFacade).getAuthenticationUserName();

    init();
  }

  Trigger databaseTrigger = null;

  private void init() {
    databaseTrigger = new Trigger();
    databaseTrigger.setAmc("1234567890");
    databaseTrigger.setNbBenef(1);
    databaseTrigger.setNbBenefToProcess(1);
    databaseTrigger.setNbBenefKO(0);
    databaseTrigger.setOrigine(TriggerEmitter.Renewal);
    databaseTrigger.setStatus(TriggerStatus.ToProcess);
    databaseTrigger.setId(triggerId);
    databaseTrigger.setBenefsToRecycle(Collections.emptyList());
    Mockito.when(mongoTemplate.findById(triggerId, Trigger.class)).thenReturn(databaseTrigger);

    databaseParametrageCarteTP.clear();
    databaseParametrageCarteTP.add(getParametrageCarteTP("1234567890"));

    Mockito.when(mongoTemplate.findById(Mockito.any(), Mockito.eq(ParametrageCarteTP.class)))
        .thenReturn(databaseParametrageCarteTP.getFirst());

    Mockito.when(
            mongoTemplate.find(Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.any()))
        .thenReturn(databaseParametrageCarteTP);
    Mockito.when(mongoTemplate.stream(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(Streams.of(Collections.emptyIterator()));
    Trigger tt = new Trigger();
    tt.setNbBenefToProcess(1);
    Mockito.when(mongoTemplate.findAndModify(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(tt);
  }

  private AggregationResults<TriggeredBeneficiary> getExpectedResult() {
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    List<TriggeredBeneficiary> mappedResults = List.of(triggeredBeneficiary);
    Document rawResults = new Document();
    return new AggregationResults<>(mappedResults, rawResults);
  }

  @Test
  void shouldPrepareIndex() {
    AggregationResults expectedRes = getExpectedResult();
    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.any()))
        .thenReturn(expectedRes);
    consumerDemandeRenouv.processMessageListener(
        new TriggerId(triggerId), String.valueOf(false), null);
    Mockito.when(mongoTemplate.stream(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(Streams.of(Collections.emptyIterator()));
    Trigger t = triggerDao.getTriggerById(triggerId);
    t.setStatus(TriggerStatus.ProcessedWithErrors);
    triggerDao.saveTrigger(t);

    consumerDemandeRenouv.processMessageListener(
        new TriggerId(triggerId), String.valueOf(false), null);
    t = triggerDao.getTriggerById(triggerId);
    Assertions.assertEquals(TriggerStatus.Processing, t.getStatus());
  }

  private ParametrageCarteTP getParametrageCarteTP(String amc) {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setAmc(amc);
    param.setStatut(ParametrageCarteTPStatut.Actif);
    param.setDateDebutValidite("2020-01-01");
    param.setId("paramId");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDateDeclenchementManuel("2001-01-01"); //
    // dateDeclenchementAutomatique;
    parametrageRenouvellement.setSeuilSecurite(1000);

    param.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP

    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("SP");
    parametrageDroitsCarteTP.setCodeOperateurTP("OPS");
    parametrageDroitsCarteTP.setIsCarteEditablePapier(true);
    parametrageDroitsCarteTP.setIsCarteDematerialisee(true);
    parametrageDroitsCarteTP.setRefFondCarte("fondCarte1");
    parametrageDroitsCarteTP.setCodeAnnexe1("Annexe1");
    parametrageDroitsCarteTP.setCodeAnnexe2("Annexe2");
    List<DetailDroit> detailsDroit = new ArrayList<>();
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(1);
    detailDroit.setCodeDomaineTP("PHAR");
    detailDroit.setLibelleDomaineTP("Pharmacie");
    detailDroit.setConvention("IS");

    detailsDroit.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(2);
    detailDroit.setCodeDomaineTP("DENT");
    detailDroit.setLibelleDomaineTP("Dentaire");
    detailDroit.setConvention("IS");
    detailsDroit.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(3);
    detailDroit.setCodeDomaineTP("OPTI");
    detailDroit.setLibelleDomaineTP("Optique");
    detailDroit.setConvention("SP");
    detailsDroit.add(detailDroit);
    parametrageDroitsCarteTP.setDetailsDroit(detailsDroit);

    param.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    return param;
  }

  ContratAIV6 prepareContract() {
    ContratAIV6 c = new ContratAIV6();
    Assure ass = new Assure();

    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setNomUsage("bob");
    data.setNom(nom);
    Contact contact = new Contact();
    contact.setEmail("mymail");
    data.setContact(contact);
    ass.setData(data);
    IdentiteContrat id = new IdentiteContrat();
    id.setNumeroPersonne("numero personn");
    ass.setIdentite(id);
    List<Assure> list = new ArrayList<>();
    list.add(ass);
    c.setAssures(list);

    return c;
  }
}
