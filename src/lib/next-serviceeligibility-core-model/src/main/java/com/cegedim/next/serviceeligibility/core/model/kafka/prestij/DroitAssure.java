package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DroitAssure {
  @NotEmpty(message = "Le type de droit est obligatoire")
  private String type;

  @NotEmpty(message = "La date de début de droit est obligatoire")
  private String dateDebut;

  private String dateFin;
}
