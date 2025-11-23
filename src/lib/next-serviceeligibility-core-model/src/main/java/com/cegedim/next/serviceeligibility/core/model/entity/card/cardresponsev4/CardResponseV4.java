package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CardResponseCommun;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardResponseV4 extends CardResponseCommun {

  private CardResponseContratV4 contrat;
  private List<CardResponseBeneficiaryV4> beneficiaires;
}
