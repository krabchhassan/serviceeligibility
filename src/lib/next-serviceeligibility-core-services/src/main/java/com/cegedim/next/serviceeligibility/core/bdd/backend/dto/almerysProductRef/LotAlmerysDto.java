package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class LotAlmerysDto implements GenericDto {
  private String id;
  private String code;
  private String dateAjout;
  private String dateSuppressionLogique;
}
