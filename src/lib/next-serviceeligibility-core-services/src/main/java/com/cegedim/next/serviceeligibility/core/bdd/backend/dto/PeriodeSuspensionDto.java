package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import lombok.Data;

@Data
public class PeriodeSuspensionDto implements GenericDto {
  private String debut;
  private String fin;
  private String typeSuspension;
  private String motifSuspension;
  private String motifLeveeSuspension;

  public PeriodeSuspensionDto(PeriodeSuspensionDeclaration periodeSuspensionDeclaration) {
    this.debut = periodeSuspensionDeclaration.getDebut();
    this.fin = periodeSuspensionDeclaration.getFin();
    this.typeSuspension = periodeSuspensionDeclaration.getTypeSuspension();
    this.motifSuspension = periodeSuspensionDeclaration.getMotifSuspension();
    this.motifLeveeSuspension = periodeSuspensionDeclaration.getMotifLeveeSuspension();
  }
}
