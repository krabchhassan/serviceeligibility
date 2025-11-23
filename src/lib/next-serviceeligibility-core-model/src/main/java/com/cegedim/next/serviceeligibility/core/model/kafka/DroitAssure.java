package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DroitAssure {
  @NotBlank(message = "L'information code du droit de l'assuré est obligatoire")
  private String code;

  @NotBlank(message = "L'information codeAssureur est obligatoire")
  private String codeAssureur;

  @NotBlank(message = "L'information libelle des droits de l'assuré est obligatoire")
  private String libelle;

  @NotBlank(message = "L'information ordrePriorisation des droits de l'assuré est obligatoire")
  private String ordrePriorisation;

  @NotBlank(message = "L'information type des droits de l'assuré est obligatoire")
  private String type;

  @NotNull(message = "L'information periode des droits de l'assuré est obligatoire")
  @Valid
  private Periode periode;

  @NotBlank(message = "L'information dateAncienneteGarantie des droits de l'assuré est obligatoire")
  private String dateAncienneteGarantie;

  @Valid private CarenceDroit carence;
}
