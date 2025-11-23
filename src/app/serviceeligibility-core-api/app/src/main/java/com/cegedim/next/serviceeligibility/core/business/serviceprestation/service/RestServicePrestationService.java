package com.cegedim.next.serviceeligibility.core.business.serviceprestation.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.RequestBeneficiaryPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.PaymentRecipientsByContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.RequestPaymentRecipientsByContract;
import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.List;

/** Interface du serviceRest d'accès aux {@code ServicePrestation} de la base de donnees. */
public interface RestServicePrestationService {

  /**
   * Recherche dans la base de donnees un service Prestation en fonction de l'AMC, l'Adhérent sa
   * date naissance, son rang de naissance et une période de soins
   *
   * @param idDeclarant l'AMC
   * @param numeroAdherent l'adhérent
   * @param dateNaissance la date de naissance de l'adhérent
   * @param rangNaissance le rang de naissance de l'adhérent
   * @param debutPeriodeSoin la date de début de période de soin
   * @param finPeriodeSoin la date de fin de période de soin
   * @param nir le NIR du bénéficiaire
   * @return le Service Prestation
   */
  ContratV6 findServicePrestationV6(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String debutPeriodeSoin,
      String finPeriodeSoin,
      String nir);

  ContratV5 findServicePrestationV5(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String debutPeriodeSoin,
      String finPeriodeSoin,
      String nir);

  List<ServicePrestationV6> findServicePrestationV6(String idDeclarant, String numeroPersonne);

  List<ServicePrestationV5> findServicePrestationV5(String idDeclarant, String numeroPersonne);

  /**
   * Creation dans la base de donnees d'un service Prestation en fonction de l'objet brut passé
   *
   * @param servicePrestation l'objet à insérer dans la collection
   */
  void create(Object servicePrestation);

  /** Suppression de la collection dans la base de données */
  void dropCollection();

  ContratAIV6 findServicePrestationV6(String id);

  ContratAIV5 findServicePrestationV5(String id);

  List<ContractWithPaymentRecipientsDto> getBeneficiaryPaymentRecipients(
      RequestBeneficiaryPaymentRecipientsDto request);

  List<PaymentRecipientsByContractDto> getPaymentRecipientsByContract(
      RequestPaymentRecipientsByContract request);
}
