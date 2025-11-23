package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class LotDto implements GenericDto {
  private String code;
  private String libelle;
  private List<GTDto> garantieTechniques;
}
