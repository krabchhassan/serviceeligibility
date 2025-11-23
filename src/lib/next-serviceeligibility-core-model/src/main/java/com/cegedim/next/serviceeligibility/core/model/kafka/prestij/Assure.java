package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class Assure {
  @Valid
  @NotNull(message = "Le NIR de l'assuré est obligatoire")
  private Nir nir;

  @NotEmpty(message = "Le numéro de personne de l'assuré est obligatoire")
  private String numeroPersonne;

  private String referenceExternePersonne;

  @NotEmpty(message = "La date de naissance de l'assuré est obligatoire")
  private String dateNaissance;

  @NotEmpty(message = "Le rang de naissance est obligatoire")
  @Min(value = 1, message = "Le rang de naissance doit être comprise entre 1 et 99")
  @Max(value = 99, message = "Le rang de naissance doit être comprise entre 1 et 99")
  private String rangNaissance;

  @Valid
  @NotNull(message = "L'information data de l'assuré est obligatoire")
  private DataAssure data;

  @Valid
  @NotEmpty(message = "L'information droit de l'assuré est obligatoire")
  private List<DroitAssure> droits;
}
