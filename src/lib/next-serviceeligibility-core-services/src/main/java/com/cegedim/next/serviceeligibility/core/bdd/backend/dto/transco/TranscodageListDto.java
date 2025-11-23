package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

/** ServicesTranscoDto contient la liste des transcodages. */
@Data
public class TranscodageListDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String codeService;
  private String codeObjetTransco;

  /* DOCUMENTS EMBEDDED */
  private List<TranscodageDto> transcoList;
}
