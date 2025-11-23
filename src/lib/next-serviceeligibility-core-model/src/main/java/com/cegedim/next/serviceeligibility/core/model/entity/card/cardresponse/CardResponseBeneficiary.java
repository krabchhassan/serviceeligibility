package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class CardResponseBeneficiary {
  private String nomBeneficiaire;
  private String nomPatronymique;
  private String nomMarital;
  private String prenom;
  private String qualite;
  private String typeAssure;
  private String lienFamilial;
  private String rangAdministratif;
  private String nirOd1;
  private String cleNirOd1;
  private String nirOd2;
  private String cleNirOd2;
  private String nirBeneficiaire;
  private String cleNirBeneficiaire;
  private String dateNaissance;
  private String rangNaissance;
  private String numeroPersonne;
  private String refExternePersonne;
  private String regimeParticulier;

  @JsonProperty("isBeneficiaireACS")
  private boolean isBeneficiaireACS;

  @JsonProperty("isTeleTransmission")
  private boolean isTeleTransmission;

  private String debutAffiliation;
  private List<CardResponseCouverture> couvertures;
}
