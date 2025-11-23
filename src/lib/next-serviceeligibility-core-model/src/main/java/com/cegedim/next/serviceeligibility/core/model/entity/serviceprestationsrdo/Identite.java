package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class Identite implements Serializable {
  private Nir nir;
  private String dateNaissance;
  private String rangNaissance;
  private String numeroPersonne;
  private List<NirRattachementRO> affiliationsRO;
}
