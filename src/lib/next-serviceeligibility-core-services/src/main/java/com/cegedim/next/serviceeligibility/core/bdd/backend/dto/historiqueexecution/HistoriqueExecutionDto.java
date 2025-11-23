package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.historiqueexecution;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoriqueExecutionDto implements GenericDto {
  private static final long serialVersionUID = 1L;

  private String batch;
  private String codeService;
  private String idDeclarant;
  private String typeConventionnement;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private Date dateExecution;
}
