package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.PaymentRecipientDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperBeneficiaryPaymentRecipients;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.NameCorporate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MapperBeneficiaryPaymentRecipientsImpl
    extends GenericMapperImpl<ServicePrestationV6, ContractWithPaymentRecipientsDto>
    implements MapperBeneficiaryPaymentRecipients {

  @Override
  public ServicePrestationV6 dtoToEntity(ContractWithPaymentRecipientsDto dto) {
    return null;
  }

  @Override
  public ContractWithPaymentRecipientsDto entityToDto(
      ServicePrestationV6 servicePrestationV6,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    ContractWithPaymentRecipientsDto contractWithPaymentRecipientsDto =
        new ContractWithPaymentRecipientsDto();
    contractWithPaymentRecipientsDto.setNumber(servicePrestationV6.getNumero());
    contractWithPaymentRecipientsDto.setSubscriptionDate(servicePrestationV6.getDateSouscription());
    contractWithPaymentRecipientsDto.setTerminationDate(servicePrestationV6.getDateResiliation());
    contractWithPaymentRecipientsDto.setSubscriberId(servicePrestationV6.getNumeroAdherent());
    Assure assure = servicePrestationV6.getAssure();
    List<PaymentRecipientDto> listPaymentRecipientDto = new ArrayList<>();
    for (DestinatairePrestations destinatairePrestations :
        assure.getData().getDestinatairesPaiements()) {
      PaymentRecipientDto paymentRecipientDto = new PaymentRecipientDto();
      paymentRecipientDto.setBeyondPaymentRecipientId(
          destinatairePrestations.getIdBeyondDestinatairePaiements());
      paymentRecipientDto.setPaymentRecipientId(
          destinatairePrestations.getIdDestinatairePaiements());
      NameCorporate nameCorporate = new NameCorporate();
      nameCorporate.setCorporateName(destinatairePrestations.getNom().getRaisonSociale());
      nameCorporate.setCommonName(destinatairePrestations.getNom().getNomUsage());
      nameCorporate.setLastName(destinatairePrestations.getNom().getNomFamille());
      nameCorporate.setFirstName(destinatairePrestations.getNom().getPrenom());
      nameCorporate.setCivility(destinatairePrestations.getNom().getCivilite());
      paymentRecipientDto.setName(nameCorporate);
      listPaymentRecipientDto.add(paymentRecipientDto);
    }
    contractWithPaymentRecipientsDto.setPaymentRecipients(listPaymentRecipientDto);
    return contractWithPaymentRecipientsDto;
  }
}
