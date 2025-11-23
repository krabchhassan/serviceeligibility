package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode
public class PrioriteDroit implements GenericDomain<PrioriteDroit> {

  private static final long serialVersionUID = 1L;

  private String code;
  private String libelle;
  private String typeDroit;
  private String prioriteBO;
  private String nirPrio1;
  private String nirPrio2;
  private String prioDroitNir1;
  private String prioDroitNir2;
  private String prioContratNir1;
  private String prioContratNir2;

  public PrioriteDroit() {
    /* empty constructor */ }

  public PrioriteDroit(PrioriteDroit source) {
    this.code = source.getCode();
    this.libelle = source.getLibelle();
    this.typeDroit = source.getTypeDroit();
    this.prioriteBO = source.getPrioriteBO();
    this.nirPrio1 = source.getNirPrio1();
    this.nirPrio2 = source.getNirPrio2();
    this.prioDroitNir1 = source.getPrioDroitNir1();
    this.prioDroitNir2 = source.getPrioDroitNir2();
    this.prioContratNir1 = source.getPrioContratNir1();
    this.prioContratNir2 = source.getPrioContratNir2();
  }

  @Override
  public int compareTo(final PrioriteDroit prioriteDroit) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, prioriteDroit.code);
    compareToBuilder.append(this.libelle, prioriteDroit.libelle);
    return compareToBuilder.toComparison();
  }
}
