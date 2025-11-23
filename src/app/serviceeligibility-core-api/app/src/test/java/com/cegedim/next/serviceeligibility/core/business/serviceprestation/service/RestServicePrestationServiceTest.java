package com.cegedim.next.serviceeligibility.core.business.serviceprestation.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.RequestBeneficiaryPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperBeneficiaryPaymentRecipientsImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperBeneficiaryPaymentRecipients;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestServicePrestationServiceTest {

  private MapperBeneficiaryPaymentRecipients mapperBeneficiaryPaymentRecipients;

  @BeforeAll
  public void initTests() {
    mapperBeneficiaryPaymentRecipients = new MapperBeneficiaryPaymentRecipientsImpl();
  }

  @NotNull
  private RestServicePrestationService getRestServicePrestationService() {
    ServicePrestationDao servicePrestationDao = Mockito.mock(ServicePrestationDao.class);
    List<ServicePrestationV6> prestationV5List = new ArrayList<>();
    Mockito.when(
            servicePrestationDao.findServicePrestationWithPaymentRecipients(
                Mockito.any(RequestBeneficiaryPaymentRecipientsDto.class)))
        .thenReturn(prestationV5List);
    RestServicePrestationService restServicePrestationServiceImpl =
        new RestServicePrestationServiceImpl(
            servicePrestationDao, mapperBeneficiaryPaymentRecipients, null, null, null);
    return restServicePrestationServiceImpl;
  }

  @Test
  void shouldReturnPayments() {
    ServicePrestationDao servicePrestationDao = Mockito.mock(ServicePrestationDao.class);
    List<ServicePrestationV6> prestationV5List = getServicePrestationV6s();
    Mockito.when(
            servicePrestationDao.findServicePrestationWithPaymentRecipients(
                Mockito.any(RequestBeneficiaryPaymentRecipientsDto.class)))
        .thenReturn(prestationV5List);
    RestServicePrestationService restServicePrestationServiceImpl =
        new RestServicePrestationServiceImpl(
            servicePrestationDao, mapperBeneficiaryPaymentRecipients, null, null, null);
    RequestBeneficiaryPaymentRecipientsDto request =
        new RequestBeneficiaryPaymentRecipientsDto("AAA", "12345", null, "2020-01-01");
    List<ContractWithPaymentRecipientsDto> contractWithPaymentRecipientsDtoList =
        restServicePrestationServiceImpl.getBeneficiaryPaymentRecipients(request);
    Assertions.assertEquals(1, contractWithPaymentRecipientsDtoList.size());
    ContractWithPaymentRecipientsDto contractWithPaymentRecipientsDto =
        contractWithPaymentRecipientsDtoList.get(0);
    Assertions.assertEquals(
        "123456",
        contractWithPaymentRecipientsDto.getPaymentRecipients().get(0).getPaymentRecipientId());
    Assertions.assertEquals(
        "RAISON",
        contractWithPaymentRecipientsDto
            .getPaymentRecipients()
            .get(0)
            .getName()
            .getCorporateName());
    Assertions.assertEquals(
        "NOMU",
        contractWithPaymentRecipientsDto.getPaymentRecipients().get(0).getName().getCommonName());
    Assertions.assertEquals(
        "Mr",
        contractWithPaymentRecipientsDto.getPaymentRecipients().get(0).getName().getCivility());
    Assertions.assertEquals(
        "PRE",
        contractWithPaymentRecipientsDto.getPaymentRecipients().get(0).getName().getFirstName());
    Assertions.assertEquals(
        "NOM",
        contractWithPaymentRecipientsDto.getPaymentRecipients().get(0).getName().getLastName());

    Assertions.assertEquals("12345", contractWithPaymentRecipientsDto.getNumber());
    Assertions.assertEquals("ADH", contractWithPaymentRecipientsDto.getSubscriberId());
    Assertions.assertEquals("2020-01-01", contractWithPaymentRecipientsDto.getSubscriptionDate());
    Assertions.assertEquals("2023-01-01", contractWithPaymentRecipientsDto.getTerminationDate());
  }

  @Test
  void shouldReturnEmptyPayments() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();
    RequestBeneficiaryPaymentRecipientsDto request =
        new RequestBeneficiaryPaymentRecipientsDto("AAA", "12345", null, "2020-01-01");
    List<ContractWithPaymentRecipientsDto> contractWithPaymentRecipientsDtoList =
        restServicePrestationServiceImpl.getBeneficiaryPaymentRecipients(request);
    Assertions.assertEquals(0, contractWithPaymentRecipientsDtoList.size());
  }

  private static List<ServicePrestationV6> getServicePrestationV6s() {
    List<ServicePrestationV6> prestationV6List = new ArrayList<>();
    ServicePrestationV6 servicePrestationV6 = new ServicePrestationV6();
    servicePrestationV6.setNumero("12345");
    servicePrestationV6.setIdDeclarant("AAA");
    servicePrestationV6.setDateSouscription("2020-01-01");
    servicePrestationV6.setDateResiliation("2023-01-01");
    servicePrestationV6.setNumeroAdherent("ADH");
    Assure assure = new Assure();
    DataAssure dataAssureV4 = new DataAssure();
    List<DestinatairePrestations> destinatairePrestationsList = new ArrayList<>();
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements("123456");
    destinatairePrestations.setIdBeyondDestinatairePaiements("123456-AAA");
    NomDestinataire nomDestinataire = new NomDestinataire();
    nomDestinataire.setCivilite("Mr");
    nomDestinataire.setNomFamille("NOM");
    nomDestinataire.setNomUsage("NOMU");
    nomDestinataire.setPrenom("PRE");
    nomDestinataire.setRaisonSociale("RAISON");
    destinatairePrestations.setNom(nomDestinataire);
    destinatairePrestationsList.add(destinatairePrestations);
    dataAssureV4.setDestinatairesPaiements(destinatairePrestationsList);
    assure.setData(dataAssureV4);
    servicePrestationV6.setAssure(assure);
    prestationV6List.add(servicePrestationV6);
    return prestationV6List;
  }

  @Test
  void filterPaymentRecipientsByReferenceDate_ValidAtReferenceDate() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();

    String referenceDate = "2025-01-15";
    List<DestinatairePrestations> paymentRecipientsList = new ArrayList<>();
    DestinatairePrestations paymentRecipients1 = new DestinatairePrestations();
    paymentRecipients1.setPeriode(new PeriodeDestinataire("2025-01-01", "2025-01-31"));
    DestinatairePrestations paymentRecipients2 = new DestinatairePrestations();
    paymentRecipients2.setPeriode(new PeriodeDestinataire("2025-02-01", "2025-02-28"));
    paymentRecipientsList.add(paymentRecipients1);
    paymentRecipientsList.add(paymentRecipients2);

    List<DestinatairePrestations> result =
        ((RestServicePrestationServiceImpl) restServicePrestationServiceImpl)
            .filterPaymentRecipientsByReferenceDate(paymentRecipientsList, referenceDate);

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2025-01-01", result.get(0).getPeriode().getDebut());
    Assertions.assertEquals("2025-01-31", result.get(0).getPeriode().getFin());
  }

  @Test
  void filterPaymentRecipientsByReferenceDate_ValidAfterReferenceDate() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();

    String referenceDate = "2025-01-01";
    List<DestinatairePrestations> paymentRecipientsList = new ArrayList<>();
    DestinatairePrestations paymentRecipients1 = new DestinatairePrestations();
    paymentRecipients1.setPeriode(new PeriodeDestinataire("2025-03-01", "2025-05-31"));
    DestinatairePrestations paymentRecipients2 = new DestinatairePrestations();
    paymentRecipients2.setPeriode(new PeriodeDestinataire("2025-06-01", null));
    DestinatairePrestations paymentRecipients3 = new DestinatairePrestations();
    paymentRecipients3.setPeriode(new PeriodeDestinataire("2025-03-01", null));
    paymentRecipientsList.add(paymentRecipients1);
    paymentRecipientsList.add(paymentRecipients2);
    paymentRecipientsList.add(paymentRecipients3);

    List<DestinatairePrestations> result =
        ((RestServicePrestationServiceImpl) restServicePrestationServiceImpl)
            .filterPaymentRecipientsByReferenceDate(paymentRecipientsList, referenceDate);

    Assertions.assertEquals(2, result.size());
    Assertions.assertTrue(
        result.stream().allMatch(d -> d.getPeriode().getDebut().equals("2025-03-01")));
  }

  @Test
  void filterPaymentRecipientsByReferenceDate_ValidBeforeReferenceDate() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();

    String referenceDate = "2026-01-01";
    List<DestinatairePrestations> paymentRecipientsList = new ArrayList<>();
    DestinatairePrestations paymentRecipients1 = new DestinatairePrestations();
    paymentRecipients1.setPeriode(new PeriodeDestinataire("2024-03-01", "2025-05-31"));
    DestinatairePrestations paymentRecipients2 = new DestinatairePrestations();
    paymentRecipients2.setPeriode(new PeriodeDestinataire("2025-06-01", "2025-05-31"));
    DestinatairePrestations paymentRecipients3 = new DestinatairePrestations();
    paymentRecipients3.setPeriode(new PeriodeDestinataire("2025-03-01", "2025-03-31"));
    paymentRecipientsList.add(paymentRecipients1);
    paymentRecipientsList.add(paymentRecipients2);
    paymentRecipientsList.add(paymentRecipients3);

    List<DestinatairePrestations> result =
        ((RestServicePrestationServiceImpl) restServicePrestationServiceImpl)
            .filterPaymentRecipientsByReferenceDate(paymentRecipientsList, referenceDate);

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024-03-01", result.get(0).getPeriode().getDebut());
    Assertions.assertEquals("2025-05-31", result.get(0).getPeriode().getFin());
  }

  @Test
  void filterPaymentRecipientsByReferenceDate_NoValidRecipientsDate() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();

    String referenceDate = "2024-12-31";
    List<DestinatairePrestations> paymentRecipientsList = new ArrayList<>();
    DestinatairePrestations paymentRecipients1 = new DestinatairePrestations();
    paymentRecipients1.setPeriode(new PeriodeDestinataire("2025-03-01", "2025-02-24"));
    DestinatairePrestations paymentRecipients2 = new DestinatairePrestations();
    paymentRecipients2.setPeriode(new PeriodeDestinataire("2025-06-01", "2025-05-31"));
    paymentRecipientsList.add(paymentRecipients1);
    paymentRecipientsList.add(paymentRecipients2);

    List<DestinatairePrestations> result =
        ((RestServicePrestationServiceImpl) restServicePrestationServiceImpl)
            .filterPaymentRecipientsByReferenceDate(paymentRecipientsList, referenceDate);

    Assertions.assertTrue(CollectionUtils.isEmpty(result));
    ;
  }

  @Test
  void filterPaymentRecipientsByReferenceDate_OnlyOneValidRecipientsDate() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();

    String referenceDate = "2025-01-01";
    List<DestinatairePrestations> paymentRecipientsList = new ArrayList<>();
    DestinatairePrestations paymentRecipients1 = new DestinatairePrestations();
    paymentRecipients1.setPeriode(new PeriodeDestinataire("2020-01-01", "2023-12-31"));
    DestinatairePrestations paymentRecipients2 = new DestinatairePrestations();
    paymentRecipients2.setPeriode(new PeriodeDestinataire("2024-01-01", "2023-12-31"));
    DestinatairePrestations paymentRecipients3 = new DestinatairePrestations();
    paymentRecipients3.setPeriode(new PeriodeDestinataire("2025-01-01", "2024-12-31"));
    paymentRecipientsList.add(paymentRecipients1);
    paymentRecipientsList.add(paymentRecipients2);
    paymentRecipientsList.add(paymentRecipients3);

    List<DestinatairePrestations> result2 =
        ((RestServicePrestationServiceImpl) restServicePrestationServiceImpl)
            .filterPaymentRecipientsByReferenceDate(paymentRecipientsList, referenceDate);

    Assertions.assertEquals(1, result2.size());
    Assertions.assertEquals("2020-01-01", result2.get(0).getPeriode().getDebut());
    Assertions.assertEquals("2023-12-31", result2.get(0).getPeriode().getFin());
  }

  @Test
  void filterPaymentRecipientsByReferenceDate_EmptyList() {
    RestServicePrestationService restServicePrestationServiceImpl =
        getRestServicePrestationService();

    String referenceDate = "2025-01-01";
    List<DestinatairePrestations> paymentRecipientsList = Collections.emptyList();

    List<DestinatairePrestations> result =
        ((RestServicePrestationServiceImpl) restServicePrestationServiceImpl)
            .filterPaymentRecipientsByReferenceDate(paymentRecipientsList, referenceDate);

    Assertions.assertTrue(result.isEmpty());
  }
}
