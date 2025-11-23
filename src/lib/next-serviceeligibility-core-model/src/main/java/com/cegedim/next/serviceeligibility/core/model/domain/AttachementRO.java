package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttachementRO extends DocumentEntity implements GenericDomain<AttachementRO> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String centerCode;
  private String healthInsuranceCompanyCode;
  private String regimeCode;

  @Override
  public int compareTo(AttachementRO attachementRO) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.centerCode, attachementRO.centerCode);
    compareToBuilder.append(
        this.healthInsuranceCompanyCode, attachementRO.healthInsuranceCompanyCode);
    compareToBuilder.append(this.regimeCode, attachementRO.regimeCode);
    return compareToBuilder.toComparison();
  }
}
