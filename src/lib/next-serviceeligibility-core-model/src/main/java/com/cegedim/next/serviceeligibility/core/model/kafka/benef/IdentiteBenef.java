package com.cegedim.next.serviceeligibility.core.model.kafka.benef;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import java.util.List;
import lombok.Data;

@Data
public class IdentiteBenef {
  private String numeroPersonne;
  private String dateNaissance;
  private String rangNaissance;
  private List<Nir> nirs;
}
