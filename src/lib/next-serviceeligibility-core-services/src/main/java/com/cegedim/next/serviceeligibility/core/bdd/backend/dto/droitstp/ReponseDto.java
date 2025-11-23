package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class ReponseDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String libelle;

  private List<String> detailErreur;
}
