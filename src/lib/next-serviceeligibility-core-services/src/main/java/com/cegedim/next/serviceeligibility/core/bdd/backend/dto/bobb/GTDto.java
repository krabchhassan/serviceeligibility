package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class GTDto implements GenericDto {
  private String codeAssureur;
  private String codeGarantie;
  private String dateAjout;
  private String dateSuppressionLogique;
}
