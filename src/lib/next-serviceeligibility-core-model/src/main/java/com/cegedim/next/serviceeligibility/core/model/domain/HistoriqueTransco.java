package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class HistoriqueTransco implements GenericDomain<HistoriqueTransco> {

  private static final long serialVersionUID = -6343092083890791303L;

  private List<String> cle;
  private String codeTransco;

  @Override
  public int compareTo(HistoriqueTransco historiqueTransco) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.cle, historiqueTransco.cle);
    compareToBuilder.append(this.codeTransco, historiqueTransco.codeTransco);
    return compareToBuilder.toComparison();
  }
}
