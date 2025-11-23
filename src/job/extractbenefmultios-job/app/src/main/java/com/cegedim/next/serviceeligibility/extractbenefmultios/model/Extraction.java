package com.cegedim.next.serviceeligibility.extractbenefmultios.model;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import java.util.List;
import lombok.Data;

@Data
public class Extraction {
  private String key;
  private List<SocieteEmettrice> societeEmettrices;
}
