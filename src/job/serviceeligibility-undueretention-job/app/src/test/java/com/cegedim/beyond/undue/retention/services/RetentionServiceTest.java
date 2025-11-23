package com.cegedim.beyond.undue.retention.services;

import static org.mockito.Mockito.when;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.undue.retention.config.TestConfiguration;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.dao.RetentionDao;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDaoImpl;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionService;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionServiceImpl;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class RetentionServiceTest {
  static MessageProducerWithApiKey messageProducerWithApiKey;
  static OrganizationService organizationService;

  @SpyBean private BeyondPropertiesService beyondPropertiesService;

  @BeforeAll
  static void setUp() {
    messageProducerWithApiKey = Mockito.mock(MessageProducerWithApiKey.class);
    organizationService = Mockito.mock(OrganizationService.class);
  }

  @Test
  void testMonoContrat() {
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);

    RetentionService retentionServiceImpl =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Declarant d = new Declarant();
    d.set_id("123");
    d.setDelaiRetention("3");
    when(declarantDao.findAll()).thenReturn(List.of(d));

    Retention r1 = new Retention();
    r1.setPersonNumber("123");
    r1.setStatus(RetentionStatus.TO_PROCESS);
    r1.setCurrentEndDate("2025-01-31");
    when(retentionDao.findAllByDelai(Mockito.any(), Mockito.anyInt())).thenReturn(List.of(r1));

    when(retentionDao.findAndLock(r1)).thenReturn(1L);

    ContratAIV6 contrat = new ContratAIV6();
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("123");
    assure1.setIdentite(identiteContrat);
    assure1.setDateRadiation("2025-01-31");
    contrat.setAssures(List.of(assure1));
    when(servicePrestationDaoImpl.findServicePrestationV6(Mockito.any(), Mockito.any()))
        .thenReturn(List.of(contrat));

    Assertions.assertFalse(retentionServiceImpl.isMultiContrat("00000001", "123"));
  }

  @Test
  void testMultiContrat() {
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);

    RetentionService retentionServiceImpl =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Declarant d = new Declarant();
    d.set_id("123");
    d.setDelaiRetention("3");
    when(declarantDao.findAll()).thenReturn(List.of(d));

    Retention r1 = new Retention();
    r1.setPersonNumber("123");
    r1.setStatus(RetentionStatus.TO_PROCESS);
    r1.setCurrentEndDate("2025-01-31");
    when(retentionDao.findAllByDelai(Mockito.any(), Mockito.anyInt())).thenReturn(List.of(r1));

    when(retentionDao.findAndLock(r1)).thenReturn(1L);

    ContratAIV6 contrat = new ContratAIV6();
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("123");
    assure1.setIdentite(identiteContrat);
    assure1.setDateRadiation("2025-01-31");
    contrat.setAssures(List.of(assure1));

    ContratAIV6 contrat2 = new ContratAIV6();
    Assure assure2 = new Assure();
    IdentiteContrat identiteContrat2 = new IdentiteContrat();
    identiteContrat2.setNumeroPersonne("123");
    assure2.setIdentite(identiteContrat2);
    assure2.setDateRadiation("2035-01-31");
    contrat2.setAssures(List.of(assure2));

    when(servicePrestationDaoImpl.findServicePrestationV6(Mockito.any(), Mockito.any()))
        .thenReturn(List.of(contrat, contrat2));

    Assertions.assertTrue(retentionServiceImpl.isMultiContrat("00000001", "123"));
  }

  @Test
  void testMultiContratRadie() {
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);

    RetentionService retentionServiceImpl =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Declarant d = new Declarant();
    d.set_id("123");
    d.setDelaiRetention("3");
    when(declarantDao.findAll()).thenReturn(List.of(d));

    Retention r1 = new Retention();
    r1.setPersonNumber("123");
    r1.setStatus(RetentionStatus.TO_PROCESS);
    r1.setCurrentEndDate("2025-01-31");
    when(retentionDao.findAllByDelai(Mockito.any(), Mockito.anyInt())).thenReturn(List.of(r1));

    when(retentionDao.findAndLock(r1)).thenReturn(1L);

    ContratAIV6 contrat = new ContratAIV6();
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("123");
    assure1.setIdentite(identiteContrat);
    assure1.setDateRadiation("2025-01-31");
    contrat.setAssures(List.of(assure1));

    ContratAIV6 contrat2 = new ContratAIV6();
    Assure assure2 = new Assure();
    IdentiteContrat identiteContrat2 = new IdentiteContrat();
    identiteContrat2.setNumeroPersonne("123");
    assure2.setIdentite(identiteContrat2);
    assure2.setDateRadiation("2025-01-31");

    Assure assure2b = new Assure();
    IdentiteContrat identiteContrat2b = new IdentiteContrat();
    identiteContrat2b.setNumeroPersonne("789");
    assure2b.setIdentite(identiteContrat2b);
    contrat2.setAssures(List.of(assure2, assure2b));

    when(servicePrestationDaoImpl.findServicePrestationV6(Mockito.any(), Mockito.any()))
        .thenReturn(List.of(contrat, contrat2));

    Assertions.assertFalse(retentionServiceImpl.isMultiContrat("00000001", "123"));
  }
}
