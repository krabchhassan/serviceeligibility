package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;

public interface MapperBeneficiaryPaymentRecipients
    extends GenericMapper<ServicePrestationV6, ContractWithPaymentRecipientsDto> {

  ServicePrestationV6 dtoToEntity(
      ContractWithPaymentRecipientsDto contractWithPaymentRecipientsDto);

  ContractWithPaymentRecipientsDto entityToDto(
      ServicePrestationV6 servicePrestationV6,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
