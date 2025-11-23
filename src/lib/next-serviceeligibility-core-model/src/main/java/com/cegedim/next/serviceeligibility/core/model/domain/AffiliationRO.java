package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Nir;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Periode;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
public class AffiliationRO extends DocumentEntity implements GenericDomain<AffiliationRO> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private Nir nir;
  private Periode periode;
  private AttachementRO attachementRO;

  @Override
  public int compareTo(AffiliationRO affiliationRO) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.nir, affiliationRO.nir);
    compareToBuilder.append(this.periode, affiliationRO.periode);
    compareToBuilder.append(this.attachementRO, affiliationRO.attachementRO);
    return compareToBuilder.toComparison();
  }
}
