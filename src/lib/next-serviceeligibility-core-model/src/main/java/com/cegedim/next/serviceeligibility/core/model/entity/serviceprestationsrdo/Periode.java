package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import lombok.Data;

@Data
public class Periode implements Serializable {
  private String debut;
  private String fin;
}
