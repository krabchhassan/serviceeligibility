package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode
public class PrestationContrat implements GenericDomain<PrestationContrat>, Mergeable {

  private static final long serialVersionUID = 1L;

  private String code;
  private String codeRegroupement;
  private String libelle;
  private Boolean isEditionRisqueCarte = false;
  private String dateEffet;

  /* DOCUMENTS EMBEDDED */
  private Formule formule;
  private FormuleMetier formuleMetier;

  private transient List<Periode> periodes = new ArrayList<>();

  public PrestationContrat() {
    /* empty constructor */ }

  public PrestationContrat(PrestationContrat source) {
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
    if (!CollectionUtils.isEmpty(source.getPeriodes())) {
      this.periodes = new ArrayList<>();
      for (Periode conv : source.getPeriodes()) {
        this.periodes.add(new Periode(conv));
      }
    }
  }

  public PrestationContrat(Prestation source) {
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
  public int compareTo(final PrestationContrat prestation) {
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

  @Override
  public String mergeKey() {
    return code + isEditionRisqueCarte + formule.getNumero();
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
