package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/** Classe DTO de la convention TP. */
@JsonInclude(Include.NON_NULL)
@Data
public class RegroupementDomainesTPDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String domaineRegroupementTP;
  private List<String> codesDomainesTP;
  private boolean niveauRemboursementIdentique;
  private LocalDateTime dateDebut;
  private LocalDateTime dateFin;
}
