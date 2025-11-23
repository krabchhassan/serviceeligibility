package com.cegedim.next.serviceeligibility.core.mapper.carte;

import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4.CardResponseBeneficiaryV4;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4.CardResponseContratV4;
import org.mapstruct.Mapper;

@Mapper
public interface MapperWebServiceCardV4 {
  CardResponseContratV4 cardResponseContratToCardResponseContratV4(
      CardResponseContrat cardResponseContrat);

  CardResponseBeneficiaryV4 cardResponseBenefToCardResponseBenefV4(
      CardResponseBeneficiary cardResponseBeneficiary);
}
