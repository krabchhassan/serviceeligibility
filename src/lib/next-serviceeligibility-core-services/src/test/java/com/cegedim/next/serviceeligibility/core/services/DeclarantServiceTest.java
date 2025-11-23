package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
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
class DeclarantServiceTest {
  private final Logger logger = LoggerFactory.getLogger(DeclarantServiceTest.class);

  @Autowired MongoTemplate mongoTemplate;

  @Autowired private DeclarantService service;

  @Test
  void shouldCountOneAmc() {
    Mockito.when(mongoTemplate.count(Mockito.any(Query.class), Mockito.eq(Declarant.class)))
        .thenReturn(Long.valueOf(1));

    Assertions.assertTrue(service.isCompatible("Unidentified", "0000401026"));
  }

  @Test
  void stage1_should_not_be_compatible() {
    logger.info("Test de non compatibilité");
    Assertions.assertFalse(service.isCompatible("toto", "123456789"));
  }

  @Test
  void stage2_should_be_compatible() {
    logger.info("Test de compatibilité");
    Mockito.when(mongoTemplate.count(Mockito.any(Query.class), Mockito.eq(Declarant.class)))
        .thenReturn(Long.valueOf(1));
    Assertions.assertTrue(service.isCompatible("Unidentified", "0000401026"));
  }

  @Test
  void stage3_should_not_exists_declarant() {
    logger.info("Test de non existence du declarant");
    try {
      service.getIdClientBo("9999999999");
    } catch (ValidationException e) {
      Assertions.assertEquals("Le déclarant 9999999999 n'existe pas !", e.getLocalizedMessage());
    }
  }

  @Test
  void stage4_should_not_have_idClientBO() {
    logger.info("Test d idClientBo non renseigné sur le declarant");
    try {
      Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Declarant.class)))
          .thenReturn(getDeclarant1());
      service.getIdClientBo("0000401027");
    } catch (ValidationException e) {
      Assertions.assertEquals(
          "Le déclarant 0000401027 ne référence aucun idClientBO !", e.getLocalizedMessage());
    }
  }

  @Test
  void stage5_should_have_idClientBO() {
    logger.info("Test idClientBo sur le declarant");
    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Declarant.class)))
        .thenReturn(getDeclarant1());
    service.getIdClientBo("0000401026");
    Assertions.assertEquals("Unidentified", service.getIdClientBo("0000401026"));
  }

  private Declarant getDeclarant1() {
    Declarant decl1 = new Declarant();
    decl1.set_id("0000401026");
    decl1.setNom("AMC Test JUnit");
    decl1.setIdClientBO("Unidentified");

    return decl1;
  }
}
