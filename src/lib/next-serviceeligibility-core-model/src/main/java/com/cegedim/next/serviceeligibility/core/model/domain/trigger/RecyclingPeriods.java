package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.Data;

@Data
public class RecyclingPeriods {

  Periode periode = new Periode();

  private Long id;
}
