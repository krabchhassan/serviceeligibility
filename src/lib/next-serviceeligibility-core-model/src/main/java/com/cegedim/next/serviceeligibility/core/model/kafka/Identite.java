package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class Identite {
  private String numeroPersonne;
  private String dateNaissance;
  private String rangNaissance;
  private Nir nir;
  private String refExternePersonne;
}
