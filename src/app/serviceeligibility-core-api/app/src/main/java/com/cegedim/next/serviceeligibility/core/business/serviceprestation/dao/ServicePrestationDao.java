package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.RequestBeneficiaryPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.InsuredPeriodDto;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ContratV6;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationCommun;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.time.LocalDate;
import java.util.List;

/** Interface de la classe d'accès aux {@code ServicePrestation} de la base de donnees. */
public interface ServicePrestationDao extends IMongoGenericDao<ServicePrestationCommun> {
  ContratV6 findServicePrestationV6(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String debutPeriodeSoin,
      String finPeriodeSoin,
      String nir);

  List<ServicePrestationV6> findServicePrestationWithPaymentRecipients(
      RequestBeneficiaryPaymentRecipientsDto requestBeneficiaryPaymentRecipientsDto);

  List<DestinatairePrestations> getPaymentRecipients(
      List<InsuredPeriodDto> insuredPeriods,
      String insurerId,
      String personNumber,
      String subscriberId,
      String contractNumber);

  List<ServicePrestationV6> findServicePrestation(String idDeclarant, String numeroPersonne);

  ContratAIV6 getServicePrestationV6ById(String id);

  void updatePaymentsBeneficiary(
      final LocalDate dateToTest, final List<ServicePrestationV6> benefitServiceList);

  void create(Object servicePrestation);

  void dropCollection();
}
