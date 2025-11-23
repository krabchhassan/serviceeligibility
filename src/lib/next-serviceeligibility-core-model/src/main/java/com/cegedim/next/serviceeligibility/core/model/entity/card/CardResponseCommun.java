package com.cegedim.next.serviceeligibility.core.model.entity.card;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseDomain;
import java.util.List;
import lombok.Data;

@Data
public class CardResponseCommun {
  private String numeroAMC;
  private String nomAMC;
  private String libelleAMC;
  private String periodeDebut;
  private String periodeFin;
  private String societeEmettrice;
  private String codeRenvoi;
  private String libelleRenvoi;
  private String codeConvention;
  private String libelleConvention;
  private String fondCarte;
  private String annexe1Carte;
  private String annexe2Carte;
  private String codeItelis;
  private String numeroAMCEchange;

  private Adresse adresseContrat;
  private List<CardResponseDomain> domaines;
}
