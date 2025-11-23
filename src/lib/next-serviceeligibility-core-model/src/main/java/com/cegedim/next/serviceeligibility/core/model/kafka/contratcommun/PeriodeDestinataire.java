package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class PeriodeDestinataire implements GenericDomain<PeriodeDestinataire> {
  @NotBlank(message = "L'information début de période est obligatoire")
  private String debut;

  private String fin;

  public PeriodeDestinataire() {
    /* empty constructor */ }

  public PeriodeDestinataire(String debut, String fin) {
    this.debut = debut;
    this.fin = fin;
  }

  @Override
  public int compareTo(PeriodeDestinataire periodeDestinataire) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.debut, periodeDestinataire.debut);
    compareToBuilder.append(this.fin, periodeDestinataire.fin);
    return compareToBuilder.toComparison();
  }
}
