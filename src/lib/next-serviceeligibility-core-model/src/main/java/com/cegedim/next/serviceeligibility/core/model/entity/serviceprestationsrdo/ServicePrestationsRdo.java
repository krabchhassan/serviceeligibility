package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import lombok.Data;

@Data
public class ServicePrestationsRdo implements Serializable {
  private String idDeclarant;
  private String numero;
  private String numeroAdherent;
  private String dateSouscription;
  private String dateResiliation;
  private String ordrePriorisation;
  private Assure assure;
}
