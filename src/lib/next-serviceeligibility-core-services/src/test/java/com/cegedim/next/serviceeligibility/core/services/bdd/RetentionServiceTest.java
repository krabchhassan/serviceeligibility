package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class RetentionServiceTest {

  private static final String RETENTION = "retention";
  @MockBean private DeclarantService declarantService;
  @Autowired private MongoTemplate template;
  @Autowired private RetentionServiceImpl retentionService;

  @BeforeEach
  public void before() {
    template.dropCollection(RETENTION);
    Mockito.when(
            template.updateMulti(
                Mockito.any(Query.class),
                Mockito.any(UpdateDefinition.class),
                Mockito.eq(Retention.class)))
        .thenReturn(UpdateResult.acknowledged(0, null, null));
  }

  // date radiations différentes sur des assurés séparés
  @Test
  void date_radiations_differentes_sur_assures_separes_test() {
    // pour les periodes de droits, on les consolident ensemble (comme avant) et on
    // cherche si la date du jour est hors periode, dans ce cas, on prend la plus
    // grand date de fin inférieure à la date du jour
    // -> pour les dates de radiation, on prend la plus grande date parmi celles des
    // assurés (même numéro personne)
    // -> on prend la plus petite date entre les deux précédentes et la date de
    // résiliation => si inférieur à date du jour : rétention
    // -> la détection de changement se fait que si la date finale calculée change
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2025-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    oldContract.setAssures(List.of(assure1));

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setDateSouscription("2025-01-01");
    Assure assure2 = new Assure();
    IdentiteContrat identiteContrat1 = new IdentiteContrat();
    identiteContrat1.setNumeroPersonne("1");
    assure2.setIdentite(identiteContrat1);
    assure2.setIdentite(identiteContrat);
    assure2.setDateRadiation("2025-01-31");
    Periode periode2 = new Periode("2025-01-01", "2025-01-31");
    assure2.setPeriodes(List.of(periode2));

    Assure assure3 = new Assure();
    assure3.setIdentite(identiteContrat1);
    assure3.setDateRadiation("2025-02-11");
    Periode periode3 = new Periode("2025-02-01", "2025-07-31");
    assure3.setPeriodes(List.of(periode3));
    newContract.setAssures(List.of(assure2, assure3));
    Declarant declarant = new Declarant();
    declarant.setDelaiRetention("3");
    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(declarant);
    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(1, retentionList.size());
    Assertions.assertEquals("1", retentionList.get(0).getPersonNumber());
    Assertions.assertEquals("2025-02-11", retentionList.get(0).getCurrentEndDate());
    Assertions.assertNotNull(retentionList.get(0).getReceptionDate());
  }

  // date resiliation dans le futur
  @Test
  void date_resiliation_futur_test() {
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2025-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    oldContract.setAssures(List.of(assure1));

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setDateSouscription("2025-01-01");
    newContract.setDateResiliation("2035-06-01");
    Assure assure2 = new Assure();
    IdentiteContrat identiteContrat1 = new IdentiteContrat();
    identiteContrat1.setNumeroPersonne("1");
    assure2.setIdentite(identiteContrat1);
    assure2.setIdentite(identiteContrat);
    Periode periode2 = new Periode("2025-01-01", "2025-01-31");
    assure2.setPeriodes(List.of(periode2));

    Assure assure3 = new Assure();
    assure3.setIdentite(identiteContrat1);
    Periode periode3 = new Periode("2025-02-01", "2035-07-31");
    assure3.setPeriodes(List.of(periode3));
    newContract.setAssures(List.of(assure2, assure3));
    Declarant declarant = new Declarant();
    declarant.setDelaiRetention("3");
    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(declarant);
    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(0, retentionList.size());
  }

  // date resiliation dans le passe
  @Test
  void date_resiliation_passe_test() {
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2025-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    oldContract.setAssures(List.of(assure1));

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setDateSouscription("2025-01-01");
    newContract.setDateResiliation("2025-02-01");
    Assure assure2 = new Assure();
    IdentiteContrat identiteContrat1 = new IdentiteContrat();
    identiteContrat1.setNumeroPersonne("1");
    assure2.setIdentite(identiteContrat1);
    assure2.setIdentite(identiteContrat);
    Periode periode2 = new Periode("2025-01-01", "2025-01-31");
    assure2.setPeriodes(List.of(periode2));

    Assure assure3 = new Assure();
    assure3.setIdentite(identiteContrat1);
    Periode periode3 = new Periode("2025-02-01", "2025-07-31");
    assure3.setPeriodes(List.of(periode3));
    newContract.setAssures(List.of(assure2, assure3));
    Declarant declarant = new Declarant();
    declarant.setDelaiRetention("3");
    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(declarant);
    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(1, retentionList.size());
    Assertions.assertEquals("1", retentionList.get(0).getPersonNumber());
    Assertions.assertEquals("2025-02-01", retentionList.get(0).getCurrentEndDate());
    Assertions.assertNotNull(retentionList.get(0).getReceptionDate());
  }

  // date resiliation avec changement de date
  @Test
  void date_resiliation_changement_test() {
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    oldContract.setDateResiliation("2035-02-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2025-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    oldContract.setAssures(List.of(assure1));

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setDateSouscription("2025-01-01");
    newContract.setDateResiliation("2025-02-11");
    Assure assure2 = new Assure();
    IdentiteContrat identiteContrat1 = new IdentiteContrat();
    identiteContrat1.setNumeroPersonne("1");
    assure2.setIdentite(identiteContrat1);
    assure2.setIdentite(identiteContrat);
    Periode periode2 = new Periode("2025-01-01", "2025-01-31");
    assure2.setPeriodes(List.of(periode2));

    Assure assure3 = new Assure();
    assure3.setIdentite(identiteContrat1);
    Periode periode3 = new Periode("2025-02-01", "2025-07-31");
    assure3.setPeriodes(List.of(periode3));
    newContract.setAssures(List.of(assure2, assure3));
    Declarant declarant = new Declarant();
    declarant.setDelaiRetention("3");
    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(declarant);
    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(1, retentionList.size());
    Assertions.assertEquals("1", retentionList.get(0).getPersonNumber());
    Assertions.assertEquals("2025-02-11", retentionList.get(0).getCurrentEndDate());
    Assertions.assertNotNull(retentionList.get(0).getReceptionDate());
  }

  @Test
  void cas_resiliation_egale_a_celle_deja_en_base() {
    // En base : contrat résilié avant la date du jour
    // On reçoit, le même contrat avec la même résiliation => on ne met pas à jour
    // la rétention
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2024-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    oldContract.setAssures(List.of(assure1));
    oldContract.setDateResiliation("2025-03-09");

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setDateSouscription("2025-01-01");
    newContract.setAssures(List.of(assure1));
    newContract.setDateResiliation("2025-03-09");

    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(0, retentionList.size());
  }

  @Test
  void cas_radiation_egale_a_celle_deja_en_base() {
    // En base : contrat avec assuré radié avant la date du jour
    // On reçoit, le même contrat avec la même radiation => on ne met pas à jour la
    // rétention
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2024-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    assure1.setDateRadiation("2025-03-09");
    oldContract.setAssures(List.of(assure1));

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setAssures(List.of(assure1));

    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(0, retentionList.size());
  }

  @Test
  void cas_radiation_et_resiliation_egale_a_celle_deja_en_base() {
    // En base : contrat avec assuré radié + résiliation avant la date du jour
    // On reçoit, le même contrat avec les mêmes radiation/résil => on ne met pas à
    // jour la rétention
    ContratAIV6 oldContract = new ContratAIV6();
    oldContract.setDateSouscription("2025-01-01");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("1");
    assure1.setIdentite(identiteContrat);
    Periode periode1 = new Periode("2024-01-01", null);
    assure1.setPeriodes(List.of(periode1));
    assure1.setDateRadiation("2025-03-09");
    oldContract.setAssures(List.of(assure1));
    oldContract.setDateResiliation("2025-03-09");

    ContratAIV6 newContract = new ContratAIV6();
    newContract.setDateSouscription("2025-01-01");
    newContract.setAssures(List.of(assure1));
    newContract.setDateResiliation("2025-03-09");

    List<Retention> retentionList = retentionService.manageRetention(newContract, oldContract);
    Assertions.assertEquals(0, retentionList.size());
  }

  @Test
  void testNullAndDefaultDate() {
    Assertions.assertNull(retentionService.calculDateRetention(null, "1"));

    ContratAIV6 defaultContract = new ContratAIV6();
    defaultContract.setAssures(new ArrayList<>());
    Assertions.assertNull(retentionService.calculDateRetention(defaultContract, "1"));
  }

  @Test
  void sansEffetContrat() {
    ContratAIV6 oldContrat = new ContratAIV6();
    oldContrat.setDateSouscription("2025-01-01");

    Assure assure = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("123");
    assure.setIdentite(identiteContrat);
    oldContrat.setAssures(List.of(assure));

    List<Retention> retentionList = new ArrayList<>();

    retentionService.manageRetentionAssure(null, oldContrat, assure, retentionList);

    Assertions.assertEquals(1, retentionList.size());
  }
}
