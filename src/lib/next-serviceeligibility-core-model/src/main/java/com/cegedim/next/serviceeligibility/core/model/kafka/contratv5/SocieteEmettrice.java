package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.List;
import lombok.Data;

@Data
public class SocieteEmettrice {
  private String code;
  private List<Periode> periodes;
}
