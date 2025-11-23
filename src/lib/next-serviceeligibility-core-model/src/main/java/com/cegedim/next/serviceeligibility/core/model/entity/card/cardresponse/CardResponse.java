package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CardResponseCommun;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardResponse extends CardResponseCommun {

  private CardResponseContrat contrat;
  private List<CardResponseBeneficiary> beneficiaires;
}
