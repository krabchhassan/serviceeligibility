package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Data;

/** Classe DTO de la fond de carte TP. */
@JsonInclude(Include.NON_NULL)
@Data
public class FondCarteTPDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String reseauSoin;
  private String fondCarte;
  private LocalDateTime dateDebut;
  private LocalDateTime dateFin;
}
