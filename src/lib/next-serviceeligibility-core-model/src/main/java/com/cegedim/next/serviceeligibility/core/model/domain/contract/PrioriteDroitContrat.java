package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode
public class PrioriteDroitContrat implements GenericDomain<PrioriteDroitContrat>, Mergeable {

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

  private transient List<Periode> periodes = new ArrayList<>();

  public PrioriteDroitContrat() {
    /* empty constructor : use for spring */ }

  public PrioriteDroitContrat(PrioriteDroitContrat source) {
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
    this.periodes = source.getPeriodes();
  }

  public PrioriteDroitContrat(PrioriteDroit source) {
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
  public int compareTo(final PrioriteDroitContrat prioriteDroit) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, prioriteDroit.code);
    compareToBuilder.append(this.libelle, prioriteDroit.libelle);
    return compareToBuilder.toComparison();
  }

  @Override
  public String mergeKey() {
    return code + libelle;
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
