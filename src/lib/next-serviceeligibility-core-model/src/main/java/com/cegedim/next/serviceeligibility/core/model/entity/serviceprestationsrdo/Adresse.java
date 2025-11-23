package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import lombok.Data;

@Data
public class Adresse implements Serializable {
  private String ligne1;
  private String ligne2;
  private String ligne3;
  private String ligne4;
  private String ligne5;
  private String ligne6;
  private String ligne7;
}
