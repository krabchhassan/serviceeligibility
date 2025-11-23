package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ReferentielParametrageCarteTP;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ReferentielParametrageCarteTPServiceTest {
  private final Logger logger =
      LoggerFactory.getLogger(ReferentielParametrageCarteTPServiceTest.class);

  @Autowired ReferentielParametrageCarteTPService service;

  @Autowired MongoTemplate mongoTemplate;

  @BeforeEach
  public void loadReferentielParametrageCarteTPForAmc() {
    logger.info("Suppression des referentiel cartes TP");
    mongoTemplate.findAllAndRemove(new Query(), "referentielParametragesCarteTP");
    logger.info("Chargement de referentiel paramétrage de Carte TP");

    ReferentielParametrageCarteTP ref1 = getReferentielParametrageCarteTP("0123456789");
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class),
                Mockito.eq(ReferentielParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(ref1);
    service.update(ref1);

    ReferentielParametrageCarteTP ref2 = getReferentielParametrageCarteTPSimple("0123456788");
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class),
                Mockito.eq(ReferentielParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(ref2);
    service.update(ref2);

    ReferentielParametrageCarteTP ref3 = getReferentielParametrageCarteTP("0123456788");
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class),
                Mockito.eq(ReferentielParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(ref3);
    service.update(ref3);
  }

  @Test
  void ShouldHaveOneGroupePopulationResult() {
    logger.info("Retourne un résultat de recherche par amc");
    List<String> groupesPopulation = service.getGroupesPopulationForAmc("0123456789");
    Assertions.assertEquals(1, groupesPopulation.size());
  }

  @Test
  void ShouldHaveTwoCollectivitesResults() {
    logger.info("Retourne 2 résultats de recherche par amc");
    List<String> collectivites = service.getIdentifiantsCollectiviteForAmc("0123456789");
    Assertions.assertEquals(2, collectivites.size());
  }

  @Test
  void ShouldHaveThreePortefeuillesResults() {
    logger.info("Retourne 3 résultats de recherche par amc");
    List<String> portefeuille = service.getPortefeuilleForAmc("0123456789");
    Assertions.assertEquals(3, portefeuille.size());
  }

  @Test
  void ShouldHaveNoResult() {
    logger.info("Retourne aucun résultat de recherche par amc");
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class),
                Mockito.eq(ReferentielParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(null);
    ReferentielParametrageCarteTP ref = service.getByAmc("9999999999");
    Assertions.assertNull(ref);
  }

  @Test
  void ShouldHaveTwoResults() {
    logger.info("Retourne 2 résultats de recherche par amc");
    List<String> amcs = new ArrayList<>();
    amcs.add("0123456789");
    amcs.add("0123456788");
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ReferentielParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(
            List.of(
                getReferentielParametrageCarteTP("0123456789"),
                getReferentielParametrageCarteTP("0123456788")));
    List<ReferentielParametrageCarteTP> refs = service.getByAmcs(amcs);
    Assertions.assertEquals(2, refs.size());
  }

  private ReferentielParametrageCarteTP getReferentielParametrageCarteTP(String amc) {
    ReferentielParametrageCarteTP ref = new ReferentielParametrageCarteTP();
    ref.setAmc(amc);
    List<String> collectivites = new ArrayList<>();
    collectivites.add("coll1");
    collectivites.add("coll2");
    ref.setIdentifiantsCollectivite(collectivites);

    List<String> groupesPopulation = new ArrayList<>();
    groupesPopulation.add("college1");
    ref.setGroupesPopulation(groupesPopulation);

    List<String> portefeuille = new ArrayList<>();
    portefeuille.add("critere1");
    portefeuille.add("critere2");
    portefeuille.add("critere3");
    ref.setPortefeuille(portefeuille);

    return ref;
  }

  private ReferentielParametrageCarteTP getReferentielParametrageCarteTPSimple(String amc) {
    ReferentielParametrageCarteTP ref = new ReferentielParametrageCarteTP();
    ref.setAmc(amc);

    List<String> collectivites = new ArrayList<>();
    ref.setIdentifiantsCollectivite(collectivites);

    List<String> groupesPopulation = new ArrayList<>();
    groupesPopulation.add("college1");
    ref.setGroupesPopulation(groupesPopulation);

    List<String> portefeuille = new ArrayList<>();
    ref.setPortefeuille(portefeuille);

    return ref;
  }
}
