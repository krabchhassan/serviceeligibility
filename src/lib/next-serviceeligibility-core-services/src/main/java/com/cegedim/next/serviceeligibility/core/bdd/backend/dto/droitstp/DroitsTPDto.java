package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DroitsTPDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private ReponseDto reponse;

  private List<DeclarationDto> declarations = new ArrayList<>();
}
