package com.cegedim.next.serviceeligibility.core.model.domain.insured.releve;

import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DestinataireRelevePrestationsAI {
  @NotNull(message = "L'information nom de destinatairesRelevePrestations est obligatoire")
  private NomAssure nom;

  private AdresseAssure adresse;

  @NotNull(message = "L'information periode de destinatairesRelevePrestations est obligatoire")
  private Periode periode;
}
