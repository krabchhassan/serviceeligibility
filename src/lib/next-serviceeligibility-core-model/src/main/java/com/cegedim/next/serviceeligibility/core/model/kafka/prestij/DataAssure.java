package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DataAssure {
  @Valid
  @NotNull(message = "Le nom est obligatoire")
  private NomAssure nom;
}
