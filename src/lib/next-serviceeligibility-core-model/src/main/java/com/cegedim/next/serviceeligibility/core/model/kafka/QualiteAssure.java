package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

@Data
public class QualiteAssure implements Serializable {
  @NotBlank(message = "L'information code de la qualité assuré est obligatoire")
  private String code;

  @NotBlank(message = "L'information libelle de la qualité assuré est obligatoire")
  private String libelle;

  public QualiteAssure(String code, String libelle) {
    this.code = code;
    this.libelle = libelle;
  }

  public QualiteAssure() {}
}
