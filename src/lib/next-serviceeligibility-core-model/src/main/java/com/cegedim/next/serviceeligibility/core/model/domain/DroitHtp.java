package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.Data;

@Data
public class DroitHtp {

  private String code;
  private String codeAssureur;
  private Periode periode;
}
