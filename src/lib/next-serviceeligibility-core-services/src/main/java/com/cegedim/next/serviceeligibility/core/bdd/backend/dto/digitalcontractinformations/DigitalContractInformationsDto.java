package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Identity;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Name;
import java.util.List;
import lombok.Data;

@Data
public class DigitalContractInformationsDto implements GenericDto {
  private Identity identity;
  private String administrativeRank;
  private Boolean isSubscriber;
  private String quality;
  private Name name;
  private List<PaymentRecipientDto> paymentRecipients;
  private List<DomainDto> domains;
}
