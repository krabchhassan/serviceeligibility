package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.PaymentRecipientDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperBeneficiaryPaymentRecipientsImpl;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.utils.TestUtils;
import java.util.List;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MapperBeneficiaryPaymentRecipientsImplTest {
  @Autowired private MapperBeneficiaryPaymentRecipientsImpl mapperBeneficiaryPaymentRecipients;

  private ServicePrestationV6 servicePrestationV6;

  private ContractWithPaymentRecipientsDto contractWithPaymentRecipientsDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    servicePrestationV6 = TestUtils.getServicePrestationV6();

    contractWithPaymentRecipientsDto = new ContractWithPaymentRecipientsDto();

    PaymentRecipientDto paymentRecipientDto = new PaymentRecipientDto();
    contractWithPaymentRecipientsDto.setPaymentRecipients(List.of(paymentRecipientDto));
    contractWithPaymentRecipientsDto.setSubscriberId("");
    contractWithPaymentRecipientsDto.setNumber("");
    contractWithPaymentRecipientsDto.setTerminationDate("");
    contractWithPaymentRecipientsDto.setSubscriptionDate("");
  }

  @Test
  void should_create_dto_from_entity() {
    final ContractWithPaymentRecipientsDto contractWithPaymentRecipientsDto =
        mapperBeneficiaryPaymentRecipients.entityToDto(
            servicePrestationV6, null, false, false, null);

    Assertions.assertNotNull(contractWithPaymentRecipientsDto);
    PaymentRecipientDto paymentRecipientDto =
        contractWithPaymentRecipientsDto.getPaymentRecipients().get(0);
    DestinatairePrestations destinatairePrestationsV4 =
        servicePrestationV6.getAssure().getData().getDestinatairesPaiements().get(0);
    Assertions.assertEquals(
        paymentRecipientDto.getPaymentRecipientId(),
        destinatairePrestationsV4.getIdDestinatairePaiements());
    Assertions.assertEquals(
        paymentRecipientDto.getBeyondPaymentRecipientId(),
        destinatairePrestationsV4.getIdBeyondDestinatairePaiements());
    Assertions.assertEquals(
        paymentRecipientDto.getName().getCorporateName(),
        destinatairePrestationsV4.getNom().getRaisonSociale());
    Assertions.assertEquals(
        paymentRecipientDto.getName().getCommonName(),
        destinatairePrestationsV4.getNom().getNomUsage());
    Assertions.assertEquals(
        paymentRecipientDto.getName().getLastName(),
        destinatairePrestationsV4.getNom().getNomFamille());
    Assertions.assertEquals(
        paymentRecipientDto.getName().getFirstName(),
        destinatairePrestationsV4.getNom().getPrenom());
    Assertions.assertEquals(
        paymentRecipientDto.getName().getCivility(),
        destinatairePrestationsV4.getNom().getCivilite());

    Assertions.assertEquals(
        contractWithPaymentRecipientsDto.getSubscriberId(),
        servicePrestationV6.getNumeroAdherent());
    Assertions.assertEquals(
        contractWithPaymentRecipientsDto.getNumber(), servicePrestationV6.getNumero());
    Assertions.assertEquals(
        contractWithPaymentRecipientsDto.getTerminationDate(),
        servicePrestationV6.getDateResiliation());
    Assertions.assertEquals(
        contractWithPaymentRecipientsDto.getSubscriptionDate(),
        servicePrestationV6.getDateSouscription());
  }

  @Test
  void should_create_entity_from_dto() {
    final ServicePrestationV6 servicePrestationV6 =
        mapperBeneficiaryPaymentRecipients.dtoToEntity(contractWithPaymentRecipientsDto);

    Assertions.assertNull(servicePrestationV6);
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
