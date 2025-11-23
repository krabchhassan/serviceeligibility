package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import lombok.Data;

@Data
public class TriggerGenerationRequest {
  private TriggerEmitter emitter;
  private String idDeclarant;
  private String individualContractNumber;
  private String numeroAdherent;
  private String idParametrageCarteTP;
  private String date;
  private boolean isRdo;
  private Boolean forcePapier;
  private Boolean forceDemat;

  private boolean generationNextYear;
}
