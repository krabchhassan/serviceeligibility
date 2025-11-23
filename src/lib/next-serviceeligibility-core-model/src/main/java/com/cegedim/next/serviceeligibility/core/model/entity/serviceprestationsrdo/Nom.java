package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import lombok.Data;

@Data
public class Nom implements Serializable {
  private String nomFamille;
  private String nomUsage;
  private String prenom;
  private String civilite;
}
