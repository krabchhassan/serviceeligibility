package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import lombok.Data;

@Data
public class RattachementRO implements Serializable {
  private String codeCaisse;
  private String codeCentre;
  private String codeRegime;
}
