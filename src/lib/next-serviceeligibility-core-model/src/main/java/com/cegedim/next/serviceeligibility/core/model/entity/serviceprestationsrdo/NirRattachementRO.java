package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import lombok.Data;

@Data
public class NirRattachementRO implements Serializable {
  private Nir nir;
  private Periode periode;
  private RattachementRO rattachementRO;
}
