package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract;

import java.util.List;
import lombok.Data;

@Data
public class ReferenceCouvertureDto {
  private String referenceCouverture;
  private List<NaturePrestationDto> naturesPrestation;
}
