package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4;

import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseContrat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardResponseContratV4 extends CardResponseContrat {
  private String numeroOperateur;
  private String typeConvention;
  private String dateSouscription;
  private String qualification;
  private String situationParticuliere;
}
