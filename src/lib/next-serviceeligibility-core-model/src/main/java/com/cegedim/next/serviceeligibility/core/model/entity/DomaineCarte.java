package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import java.util.List;
import lombok.Data;

@Data
public class DomaineCarte {
  private String code;
  private String libelle;
  private String taux;
  private String unite;
  private int rang;
  private List<DomaineCarte> regroupement;
  private List<Conventionnement> conventionnements;
  private String codeExterne;
  private String libelleExterne;
  private String codeExterneProduit;
  private String codeOptionMutualiste;
  private String libelleOptionMutualiste;
  private String codeProduit;
  private String libelleProduit;
  private String codeGarantie;
  private String libelleGarantie;
  private PrioriteDroit prioriteDroits;
  private String codeRenvoi;
  private String libelleCodeRenvoi;
  private String codeRenvoiAdditionnel;
  private String libelleCodeRenvoiAdditionnel;
  private String categorieDomaine;
  private String periodeDebut;
  private String periodeFin;
  private String referenceCouverture;
  private Integer noOrdreDroit;
  private String formulaMask;
  private Boolean isEditable;
}
