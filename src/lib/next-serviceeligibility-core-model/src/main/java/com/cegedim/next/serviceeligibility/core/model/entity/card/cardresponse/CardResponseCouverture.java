package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse;

import lombok.Data;

@Data
public class CardResponseCouverture {
  private String codeDomaine;
  private String libelleDomaine;
  private String tauxRemboursement;
  private String uniteTauxRemboursement;
  private String periodeDebut;
  private String periodeFin;
  private String codeExterneProduit;
  private String codeOptionMutualiste;
  private String libelleOptionMutualiste;
  private String codeProduit;
  private String libelleProduit;
  private String codeGarantie;
  private String libelleGarantie;
  private CardResponsePrioriteDroit prioriteDroits;
  private String codeRenvoi;
  private String libelleCodeRenvoi;
  private String codeRenvoiAdditionnel;
  private String libelleCodeRenvoiAdditionnel;
  private String categorieDomaine;
}
