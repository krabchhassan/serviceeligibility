package com.cegedim.next.serviceeligibility.core.model.domain.cartedemat;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document DomaineConvention */
@Data
public class DomaineConvention implements GenericDomain<DomaineConvention> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;

  private Integer rang;

  /* DOCUMENTS EMBEDDED */
  private List<Convention> conventions;

  /**
   * @return La liste des conventions.
   */
  public List<Convention> getConventions() {
    if (this.conventions == null) {
      this.conventions = new ArrayList<>();
    }
    return this.conventions;
  }

  @Override
  public int compareTo(final DomaineConvention domaineConvention) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, domaineConvention.code);
    compareToBuilder.append(this.rang, domaineConvention.rang);
    List<Convention> thisConv =
        Objects.requireNonNullElse(this.conventions, Collections.emptyList());
    List<Convention> otherConv =
        Objects.requireNonNullElse(domaineConvention.conventions, Collections.emptyList());
    compareToBuilder.append(thisConv.toArray(), otherConv.toArray());
    return compareToBuilder.toComparison();
  }
}
