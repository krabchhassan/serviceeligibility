package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TranscoDomainesTP implements GenericDomain<TranscoDomainesTP> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String domaineSource;
  private List<String> domainesCible;

  @Override
  public int compareTo(TranscoDomainesTP o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.domainesCible, o.domainesCible);
    compareToBuilder.append(this.domaineSource, o.domaineSource);
    return compareToBuilder.toComparison();
  }

  @Override
  public TranscoDomainesTP clone() throws CloneNotSupportedException {
    return (TranscoDomainesTP) super.clone();
  }
}
