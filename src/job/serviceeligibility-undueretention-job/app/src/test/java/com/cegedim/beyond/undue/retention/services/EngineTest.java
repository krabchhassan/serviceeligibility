package com.cegedim.beyond.undue.retention.services;

import static org.mockito.Mockito.*;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.undue.retention.config.TestConfiguration;
import com.cegedim.common.organisation.dto.OrganizationDto;
import com.cegedim.common.organisation.exception.OrganizationIndexInitException;
import com.cegedim.common.organisation.exception.OrganizationNotFoundException;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.dao.RetentionDao;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDaoImpl;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduUndueRetention;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionService;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionServiceImpl;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class EngineTest {
  static CrexProducer crexProducer;
  static MessageProducerWithApiKey messageProducerWithApiKey;
  static OrganizationService organizationService;

  @BeforeAll
  static void setUp() {
    crexProducer = Mockito.mock(CrexProducer.class);
    messageProducerWithApiKey = Mockito.mock(MessageProducerWithApiKey.class);
    organizationService = Mockito.spy(OrganizationService.class);
  }

  private void mockOrga() throws OrganizationIndexInitException, OrganizationNotFoundException {
    OrganizationDto organizationDto = new OrganizationDto();
    organizationDto.setIsMainType(Boolean.TRUE);
    organizationDto.setCode("");
    organizationDto.setFullName("");
    organizationDto.setCommercialName("");
    Mockito.doReturn(organizationDto)
        .when(organizationService)
        .getOrganizationByAmcNumber(Mockito.anyString());
  }

  @Test
  void testRetentionOk() throws OrganizationIndexInitException, OrganizationNotFoundException {
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);
    BeyondPropertiesService beyondPropertiesService = Mockito.mock(BeyondPropertiesService.class);

    UndueRetentionService undueRetentionService =
        new UndueRetentionService(
            retentionDao,
            new RetentionMessageService(
                messageProducerWithApiKey, beyondPropertiesService, organizationService),
            eventService);

    RetentionService retentionService =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Engine engine =
        new Engine(
            crexProducer,
            declarantDao,
            retentionDao,
            servicePrestationDaoImpl,
            undueRetentionService,
            retentionService,
            eventService);

    mockOrga();
    Declarant d = new Declarant();
    d.set_id("123");
    d.setDelaiRetention("3");
    when(declarantDao.findAll()).thenReturn(List.of(d));

    Retention r1 = new Retention();
    r1.setPersonNumber("123");
    r1.setStatus(RetentionStatus.TO_PROCESS);
    r1.setCurrentEndDate("2025-01-31");
    r1.setIssuingCompanyCode("tata");
    r1.setInsurerId("toto");
    when(retentionDao.findAllByDelai(Mockito.any(), Mockito.anyInt())).thenReturn(List.of(r1));

    when(retentionDao.findAndLock(r1)).thenReturn(1L);

    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("toto");
    contrat.setSocieteEmettrice("tata");
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("123");
    assure1.setIdentite(identiteContrat);
    assure1.setDateRadiation("2025-01-31");
    contrat.setAssures(List.of(assure1));
    when(servicePrestationDaoImpl.findServicePrestationByContractNumber(
            Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(contrat);

    ArgumentCaptor<CompteRenduUndueRetention> valueCapture =
        ArgumentCaptor.forClass(CompteRenduUndueRetention.class);
    doNothing().when(crexProducer).generateCrex(valueCapture.capture());

    Assertions.assertEquals(0, engine.process());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceAnnulee());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceModifiee());
    Assertions.assertEquals(1, valueCapture.getValue().getNbOccurrenceEnvoyee());
  }

  @Test
  void testRetentionAnnulee() {
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);
    BeyondPropertiesService beyondPropertiesService = Mockito.mock(BeyondPropertiesService.class);

    UndueRetentionService undueRetentionService =
        new UndueRetentionService(
            retentionDao,
            new RetentionMessageService(
                messageProducerWithApiKey, beyondPropertiesService, organizationService),
            eventService);

    RetentionService retentionService =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Engine engine =
        new Engine(
            crexProducer,
            declarantDao,
            retentionDao,
            servicePrestationDaoImpl,
            undueRetentionService,
            retentionService,
            eventService);
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
    contrat.setAssures(List.of(assure1));
    when(servicePrestationDaoImpl.findServicePrestationByContractNumber(
            Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(contrat);

    ArgumentCaptor<CompteRenduUndueRetention> valueCapture =
        ArgumentCaptor.forClass(CompteRenduUndueRetention.class);
    doNothing().when(crexProducer).generateCrex(valueCapture.capture());

    Assertions.assertEquals(0, engine.process());
    Assertions.assertEquals(1, valueCapture.getValue().getNbOccurrenceAnnulee());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceModifiee());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceEnvoyee());
  }

  @Test
  void testRetentionMaj() {
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);
    BeyondPropertiesService beyondPropertiesService = Mockito.mock(BeyondPropertiesService.class);

    UndueRetentionService undueRetentionService =
        new UndueRetentionService(
            retentionDao,
            new RetentionMessageService(
                messageProducerWithApiKey, beyondPropertiesService, organizationService),
            eventService);

    RetentionService retentionService =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Engine engine =
        new Engine(
            crexProducer,
            declarantDao,
            retentionDao,
            servicePrestationDaoImpl,
            undueRetentionService,
            retentionService,
            eventService);
    Declarant d = new Declarant();
    d.set_id("123");
    d.setDelaiRetention("3");
    when(declarantDao.findAll()).thenReturn(List.of(d));

    Retention r1 = new Retention();
    r1.setStatus(RetentionStatus.TO_PROCESS);
    r1.setCurrentEndDate("2025-01-31");
    r1.setPersonNumber("123");
    r1.setOriginalEndDate(null);
    when(retentionDao.findAllByDelai(Mockito.any(), Mockito.anyInt())).thenReturn(List.of(r1));

    when(retentionDao.findAndLock(r1)).thenReturn(1L);

    ContratAIV6 contrat = new ContratAIV6();
    Assure assure1 = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("123");
    assure1.setIdentite(identiteContrat);
    assure1.setDateRadiation("2025-02-01");
    contrat.setAssures(List.of(assure1));
    when(servicePrestationDaoImpl.findServicePrestationByContractNumber(
            Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(contrat);

    ArgumentCaptor<CompteRenduUndueRetention> valueCapture =
        ArgumentCaptor.forClass(CompteRenduUndueRetention.class);
    doNothing().when(crexProducer).generateCrex(valueCapture.capture());

    Assertions.assertEquals(0, engine.process());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceAnnulee());
    Assertions.assertEquals(1, valueCapture.getValue().getNbOccurrenceModifiee());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceEnvoyee());
  }

  @Test
  void testRetentionRetroactive() {
    // dans ce cas, à la date du jour, les droits sont ouvert donc on ignore la
    // retention précédente
    DeclarantDao declarantDao = Mockito.mock(DeclarantDao.class);
    RetentionDao retentionDao = Mockito.mock(RetentionDao.class);
    ServicePrestationDaoImpl servicePrestationDaoImpl =
        Mockito.mock(ServicePrestationDaoImpl.class);
    EventService eventService = Mockito.mock(EventService.class);
    BeyondPropertiesService beyondPropertiesService = Mockito.mock(BeyondPropertiesService.class);

    UndueRetentionService undueRetentionService =
        new UndueRetentionService(
            retentionDao,
            new RetentionMessageService(
                messageProducerWithApiKey, beyondPropertiesService, organizationService),
            eventService);

    RetentionService retentionService =
        new RetentionServiceImpl(retentionDao, servicePrestationDaoImpl, eventService);

    Engine engine =
        new Engine(
            crexProducer,
            declarantDao,
            retentionDao,
            servicePrestationDaoImpl,
            undueRetentionService,
            retentionService,
            eventService);
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
    Periode periode1 = new Periode();
    periode1.setDebut("2025-01-01");
    periode1.setFin("2025-02-01");
    Periode periode2 = new Periode();
    periode2.setDebut("2025-02-10");
    assure1.setPeriodes(List.of(periode1, periode2));
    contrat.setAssures(List.of(assure1));
    when(servicePrestationDaoImpl.findServicePrestationByContractNumber(
            Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(contrat);

    ArgumentCaptor<CompteRenduUndueRetention> valueCapture =
        ArgumentCaptor.forClass(CompteRenduUndueRetention.class);
    doNothing().when(crexProducer).generateCrex(valueCapture.capture());

    Assertions.assertEquals(0, engine.process());
    Assertions.assertEquals(1, valueCapture.getValue().getNbOccurrenceAnnulee());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceModifiee());
    Assertions.assertEquals(0, valueCapture.getValue().getNbOccurrenceEnvoyee());
  }
}
