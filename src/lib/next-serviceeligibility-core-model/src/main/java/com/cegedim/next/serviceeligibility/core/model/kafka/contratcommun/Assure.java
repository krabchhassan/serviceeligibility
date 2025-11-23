package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Assure extends AssureCommun {
  private DataAssure data;
  private DigitRelation digitRelation;
  private List<DroitAssure> droits;
  private String parametrageCarteId;
}
