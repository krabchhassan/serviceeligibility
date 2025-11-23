package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class CodeRenvoiTP implements GenericDomain<CodeRenvoiTP> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String domaineTP;
  private String reseauSoin;
  private String codeRenvoi;
  private LocalDateTime dateDebut;
  private LocalDateTime dateFin;

  @Override
  public int compareTo(CodeRenvoiTP o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.domaineTP, o.domaineTP);
    compareToBuilder.append(this.reseauSoin, o.reseauSoin);
    compareToBuilder.append(this.codeRenvoi, o.codeRenvoi);
    compareToBuilder.append(this.dateDebut, o.dateDebut);
    compareToBuilder.append(this.dateFin, o.dateFin);
    return compareToBuilder.toComparison();
  }

  @Override
  public CodeRenvoiTP clone() throws CloneNotSupportedException {
    return (CodeRenvoiTP) super.clone();
  }
}
