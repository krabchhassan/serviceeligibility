package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Conventionnement */
@Data
public class ConventionnementContrat implements GenericDomain<ConventionnementContrat>, Mergeable {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private Integer priorite;

  /* DOCUMENTS EMBEDDED */
  private TypeConventionnement typeConventionnement;

  private transient List<Periode> periodes = new ArrayList<>();

  public ConventionnementContrat() {
    /* empty constructor */ }

  public ConventionnementContrat(ConventionnementContrat source) {
    this.priorite = source.getPriorite();

    if (source.getTypeConventionnement() != null) {
      this.typeConventionnement = new TypeConventionnement(source.getTypeConventionnement());
    }
    if (!CollectionUtils.isEmpty(source.getPeriodes())) {
      this.periodes = new ArrayList<>();
      for (Periode conv : source.getPeriodes()) {
        this.periodes.add(new Periode(conv));
      }
    }
  }

  public ConventionnementContrat(Conventionnement source) {
    this.priorite = source.getPriorite();

    if (source.getTypeConventionnement() != null) {
      this.typeConventionnement = new TypeConventionnement(source.getTypeConventionnement());
    }
  }

  @Override
  public int compareTo(final ConventionnementContrat conventionnement) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.priorite, conventionnement.priorite);
    compareToBuilder.append(this.typeConventionnement, conventionnement.typeConventionnement);
    return compareToBuilder.toComparison();
  }

  @Override
  public String mergeKey() {
    return priorite + typeConventionnement.getCode();
  }

  @Override
  public String conflictKey() {
    return "" + priorite;
  }
}
