package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import com.cegedim.next.serviceeligibility.core.model.kafka.QualiteAssure;
import java.io.Serializable;
import java.util.List;

@lombok.Data
public class Assure implements Serializable {
  private String rangAdministratif;
  private Identite identite;
  private Data data;
  private QualiteAssure qualite;
  private List<Droit> droits;
}
