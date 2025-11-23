package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoDeclarationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  private String idDeclaration;
  private String debEffet;
  private String codeEtat;
  private String nirOd1;
  private String cleNirOd1;
  private String nomBeneficiaire;
  private String prenomBeneficiaire;
  private String dateNaissanceBeneficiaire;
  private String rangNaissanceBeneficiaire;
  private String qualite;
  private String numeroPersonne;
  private String numeroAdherent;
  private String numeroContrat;
  private String qualifContrat;
  private String nirOd2;
  private String cleNirOd2;
  private String userCreation;
  private String dateCreation;
  private String userModification;
  private String dateModification;

  @Override
  public String toString() {
    return "ResultatRechercheDeclarationsDto [idDeclaration="
        + idDeclaration
        + ", debEffet="
        + debEffet
        + ", codeEtat="
        + codeEtat
        + ", nirOd1="
        + nirOd1
        + ", cleNirOd1="
        + cleNirOd1
        + ", nomBeneficiaire="
        + nomBeneficiaire
        + ", prenomBeneficiaire="
        + prenomBeneficiaire
        + ", dateNaissanceBeneficiaire="
        + dateNaissanceBeneficiaire
        + ", rangNaissanceBeneficiaire="
        + rangNaissanceBeneficiaire
        + ", qualite="
        + qualite
        + ", numeroPersonne="
        + numeroPersonne
        + ", numeroAdherent="
        + numeroAdherent
        + ", numeroContrat="
        + numeroContrat
        + ", qualifContrat="
        + qualifContrat
        + ", nirOd2="
        + nirOd2
        + ", cleNirOd2="
        + cleNirOd2
        + "]";
  }
}
