package com.cegedim.next.serviceeligibility.core.model.domain.cartepapier;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document DomaineConventionCartePapier */
@Data
public class DomaineConventionCartePapier implements GenericDomain<DomaineConventionCartePapier> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String code;

  private String libelle;

  private Integer rang;

  /* DOCUMENTS EMBEDDED */
  private List<ConventionCartePapier> conventions;

  /**
   * @return La liste des conventions.
   */
  public List<ConventionCartePapier> getConventions() {
    if (this.conventions == null) {
      this.conventions = new ArrayList<>();
    }
    return this.conventions;
  }

  @Override
  public int compareTo(final DomaineConventionCartePapier domaineConvention) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, domaineConvention.code);
    compareToBuilder.append(this.libelle, domaineConvention.libelle);
    compareToBuilder.append(this.rang, domaineConvention.code);
    compareToBuilder.append(this.conventions, domaineConvention.conventions);
    return compareToBuilder.toComparison();
  }
}
