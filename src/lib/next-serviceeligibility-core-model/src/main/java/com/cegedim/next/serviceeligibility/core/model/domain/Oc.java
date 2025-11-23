package com.cegedim.next.serviceeligibility.core.model.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Oc implements Serializable {
  private static final long serialVersionUID = 1L;
  String code;
  String libelle;
}
