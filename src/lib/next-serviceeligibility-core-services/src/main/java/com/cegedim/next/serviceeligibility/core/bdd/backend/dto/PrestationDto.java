package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestationDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;
  private Boolean isEditionRisqueCarte = false;
  private String codeRegroupement;
  private Date dateEffet;

  /* CLES ETRANGERES */
  private FormuleDto formule;
  private FormuleMetierDto formuleMetier;

  public PrestationDto() {
    /* empty constructor */ }

  public PrestationDto(PrestationDto source) {
    this.code = source.getCode();
    this.libelle = source.getLibelle();
    this.isEditionRisqueCarte = source.getIsEditionRisqueCarte();
    this.codeRegroupement = source.getCodeRegroupement();
    if (source.getDateEffet() != null) {
      this.dateEffet = new Date(source.getDateEffet().getTime());
    }

    if (source.getFormule() != null) {
      this.formule = new FormuleDto(source.getFormule());
    }
    if (source.getFormuleMetier() != null) {
      this.formuleMetier = new FormuleMetierDto(source.getFormuleMetier());
    }
  }
}
