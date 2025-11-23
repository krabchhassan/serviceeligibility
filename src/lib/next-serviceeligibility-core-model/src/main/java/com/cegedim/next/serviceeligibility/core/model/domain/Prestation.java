package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode
public class Prestation implements GenericDomain<Prestation> {

  private static final long serialVersionUID = 1L;

  private String code;
  private String codeRegroupement;
  private String libelle;
  private Boolean isEditionRisqueCarte = false;
  private String dateEffet;

  /* DOCUMENTS EMBEDDED */
  private Formule formule;
  private FormuleMetier formuleMetier;

  public Prestation() {
    /* empty constructor */ }

  public Prestation(Prestation source) {
    this.code = source.getCode();
    this.codeRegroupement = source.getCodeRegroupement();
    this.libelle = source.getLibelle();
    this.isEditionRisqueCarte = source.getIsEditionRisqueCarte();
    this.dateEffet = source.getDateEffet();

    if (source.getFormule() != null) {
      this.formule = new Formule(source.getFormule());
    }
    if (source.getFormuleMetier() != null) {
      this.formuleMetier = new FormuleMetier(source.getFormuleMetier());
    }
  }

  @Override
  public int compareTo(final Prestation prestation) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, prestation.code);
    compareToBuilder.append(this.codeRegroupement, prestation.codeRegroupement);
    compareToBuilder.append(this.dateEffet, prestation.dateEffet);
    compareToBuilder.append(this.formule, prestation.formule);
    compareToBuilder.append(this.formuleMetier, prestation.formuleMetier);
    compareToBuilder.append(this.isEditionRisqueCarte, prestation.isEditionRisqueCarte);
    compareToBuilder.append(this.libelle, prestation.libelle);
    return compareToBuilder.toComparison();
  }
}
