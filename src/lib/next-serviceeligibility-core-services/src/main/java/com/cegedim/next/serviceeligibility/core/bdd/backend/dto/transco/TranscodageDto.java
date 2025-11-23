package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

/** Classe DTO de l'entite {@code Transcodage}. */
@JsonInclude(Include.NON_EMPTY)
@Data
public class TranscodageDto implements GenericDto, Comparable<TranscodageDto> {

  private static final long serialVersionUID = 1L;

  private String codeService;
  private String codeObjetTransco;
  private List<String> cle;
  private String codeTransco;

  @Override
  public int compareTo(TranscodageDto o) {
    // Premier element de cle
    String premierCleO1 = this.getCle().get(0);
    String premierCleO2 = o.getCle().get(0);
    return premierCleO1.compareTo(premierCleO2);
  }
}
