package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class ParametresDroitsTPDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String numeroAMC;
  private String numeroAdherent;
  private String typeConventionnement;
  private List<String> domainesTiersPayant;
  private String dateDebut;
  private String dateFin;
  private String nir;
  private int cle;
  private int rangNaissance;
  private String dateNaissance;
  private int typeRecherche;
  private int profondeurRecherche;

  @Override
  public String toString() {
    return "Criteres Recherche Droits TP [numeroAMC="
        + numeroAMC
        + ", numeroAdherent="
        + numeroAdherent
        + ", typeConventionnement="
        + typeConventionnement
        + ", domainesTiersPayant="
        + domainesTiersPayant
        + ", dateDebut="
        + dateDebut
        + ", dateFin="
        + dateFin
        + ", nir="
        + nir
        + ", cle="
        + cle
        + ", dateNaissance="
        + dateNaissance
        + ", rangNaissance="
        + rangNaissance
        + ", typeRecherche="
        + typeRecherche
        + ", profondeurRecherche="
        + profondeurRecherche
        + "]";
  }
}
