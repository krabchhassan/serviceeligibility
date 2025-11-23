package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode
public class RemboursementContrat implements GenericDomain<RemboursementContrat>, Mergeable {
  private static final long serialVersionUID = 1L;

  private String tauxRemboursement;
  private String uniteTauxRemboursement;

  private transient List<Periode> periodes = new ArrayList<>();

  public RemboursementContrat() {
    /* empty constructor */ }

  public RemboursementContrat(RemboursementContrat source) {
    this.tauxRemboursement = source.getTauxRemboursement();
    this.uniteTauxRemboursement = source.getUniteTauxRemboursement();
    this.periodes = source.getPeriodes();
  }

  public RemboursementContrat(
      String tauxRemboursement, String uniteTauxRemboursement, Periode periode) {
    this(tauxRemboursement, uniteTauxRemboursement);
    this.periodes.add(periode);
  }

  public RemboursementContrat(String tauxRemboursement, String uniteTauxRemboursement) {
    this.tauxRemboursement = tauxRemboursement;
    this.uniteTauxRemboursement = uniteTauxRemboursement;
  }

  @Override
  public int compareTo(RemboursementContrat remboursement) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.tauxRemboursement, remboursement.tauxRemboursement);
    compareToBuilder.append(this.uniteTauxRemboursement, remboursement.uniteTauxRemboursement);
    compareToBuilder.append(this.periodes, remboursement.periodes);
    return compareToBuilder.toComparison();
  }

  @Override
  public String mergeKey() {
    return tauxRemboursement + uniteTauxRemboursement;
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
