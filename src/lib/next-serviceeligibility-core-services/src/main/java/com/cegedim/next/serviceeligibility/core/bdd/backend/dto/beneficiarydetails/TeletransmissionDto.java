package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import lombok.Data;

@Data
public class TeletransmissionDto {
  private Boolean isTeletransmission;
  private PeriodeContractDto periode;
}
