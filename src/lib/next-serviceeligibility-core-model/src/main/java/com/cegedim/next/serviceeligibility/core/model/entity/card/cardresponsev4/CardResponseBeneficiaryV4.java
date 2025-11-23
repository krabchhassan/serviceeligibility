package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4;

import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseBeneficiary;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardResponseBeneficiaryV4 extends CardResponseBeneficiary {
  private String regimeOD1;
  private String caisseOD1;
  private String centreOD1;
  private String regimeOD2;
  private String caisseOD2;
  private String centreOD2;
  private Boolean hasMedecinTraitant;
}
