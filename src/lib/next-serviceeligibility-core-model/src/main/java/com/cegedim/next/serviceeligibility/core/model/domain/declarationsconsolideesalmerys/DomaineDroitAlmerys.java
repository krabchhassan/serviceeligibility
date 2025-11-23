package com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

@Data
public class DomaineDroitAlmerys implements GenericDomain<DomaineDroitAlmerys> {
  /* PROPRIETES */
  private List<String> code;
  private String codeExterneProduit;
  private String codeProduit;
  private String libelleProduit;
  private String codeGarantie;
  private String libelleGarantie;
  private String codeProfil;
  private Boolean isSuspension = false;
  private PeriodeSuspensionDeclaration periodeSuspension;
  private String dateAdhesionCouverture;
  private String origineDroits;
  private String categorie;

  private PrioriteDroit prioriteDroit;
  private PeriodeDroit periodeDroit;

  private boolean isDroitsAvecCarence;
  private List<CarenceAlmerys> carences;

  @Override
  public int compareTo(@NotNull DomaineDroitAlmerys domaine) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, domaine.code);
    compareToBuilder.append(this.codeExterneProduit, domaine.codeExterneProduit);
    compareToBuilder.append(this.codeProduit, domaine.codeProduit);
    compareToBuilder.append(this.libelleProduit, domaine.libelleProduit);
    compareToBuilder.append(this.codeGarantie, domaine.codeGarantie);
    compareToBuilder.append(this.libelleGarantie, domaine.libelleGarantie);
    compareToBuilder.append(this.codeProfil, domaine.codeProfil);
    compareToBuilder.append(this.isSuspension, domaine.isSuspension);
    compareToBuilder.append(this.dateAdhesionCouverture, domaine.dateAdhesionCouverture);
    compareToBuilder.append(this.origineDroits, domaine.origineDroits);
    compareToBuilder.append(this.categorie, domaine.categorie);
    compareToBuilder.append(this.prioriteDroit, domaine.prioriteDroit);
    compareToBuilder.append(this.periodeDroit, domaine.periodeDroit);
    compareToBuilder.append(this.isDroitsAvecCarence, domaine.isDroitsAvecCarence);
    compareToBuilder.append(this.periodeSuspension, domaine.periodeSuspension);
    return compareToBuilder.toComparison();
  }
}
