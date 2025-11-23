package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import lombok.Data;

@Data
public class NirRattachementRODto {
  private NirDto nir;
  private RattachementRODto rattachementRO;
  private PeriodeContractDto periode;
}
