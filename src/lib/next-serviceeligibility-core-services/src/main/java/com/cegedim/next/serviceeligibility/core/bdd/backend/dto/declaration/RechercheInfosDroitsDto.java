package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import java.util.List;
import lombok.Data;

@Data
public class RechercheInfosDroitsDto {

  int totalDroits = 0;

  private List<RechercheDroitDto> droits;
}
