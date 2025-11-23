package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class PrestationContract implements GenericDto {
  private PeriodeContractDto periode;
  private List<Prestation> prestations;
}
