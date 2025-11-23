package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class BeneficiaryDataNameDto implements GenericDto, Comparable<BeneficiaryDataNameDto> {

  private String lastName;
  private String commonName;
  private String firstName;
  private String civility;

  @Override
  public int compareTo(BeneficiaryDataNameDto beneficiaryDataNameDto) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.lastName, beneficiaryDataNameDto.lastName);
    compareToBuilder.append(this.commonName, beneficiaryDataNameDto.commonName);
    compareToBuilder.append(this.firstName, beneficiaryDataNameDto.firstName);
    compareToBuilder.append(this.civility, beneficiaryDataNameDto.civility);
    return compareToBuilder.toComparison();
  }
}
