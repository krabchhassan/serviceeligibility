package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContratIJ {
  @NotEmpty(message = "Le numéro de contrat est obligatoire")
  private String numero;

  private String numeroExterne;

  @NotEmpty(message = "Le numéro d'adhérent est obligatoire")
  private String numeroAdherent;

  private String numeroAdherentComplet;

  @NotEmpty(message = "La date de souscription est obligatoire")
  private String dateSouscription;

  private String dateResiliation;

  @NotEmpty(message = "La date d'effet est obligatoire")
  private String dateEffet;

  @NotNull(message = "L'information contrat individuel est obligatoire")
  private Boolean isContratIndividuel;

  private ContratCollectif contratCollectif;
}
