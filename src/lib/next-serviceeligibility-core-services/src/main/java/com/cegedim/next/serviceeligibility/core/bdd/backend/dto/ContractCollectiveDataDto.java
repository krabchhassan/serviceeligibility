package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class ContractCollectiveDataDto
    implements GenericDto, Comparable<ContractCollectiveDataDto> {
  private static final long serialVersionUID = 1L;

  private Boolean isIndividualContract;
  private String collectiveContractNumber;
  private String companyId;
  private String companyName;
  private String siret;
  private String populationGroup;

  @Override
  public int compareTo(ContractCollectiveDataDto o) {
    return this.getCollectiveContractNumber().compareTo(o.getCollectiveContractNumber());
  }
}
