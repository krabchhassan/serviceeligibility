package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Oc {

  @NotEmpty(message = "L'utilisateur Keycloak -> idClientBO de l'OC est obligatoire")
  private String idClientBO;

  @NotEmpty(message = "L'identifiant PrestIJ de l'OC est obligatoire")
  private String identifiantPrestIj;

  @NotEmpty(message = "L'identifiant de l'OC est obligatoire")
  private String identifiant;

  @NotEmpty(message = "La dénomination de l'OC est obligatoire")
  private String denomination;
}
