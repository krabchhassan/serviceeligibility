package com.cegedim.next.serviceeligibility.core.model.kafka;

import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DigitRelation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Assure {
  @NotBlank(message = "L'information rangAdministratif est obligatoire")
  private String rangAdministratif;

  @NotNull(message = "L'information identite est obligatoire")
  @Valid
  private IdentiteContrat identite;

  @NotNull(message = "L'information data est obligatoire")
  @Valid
  private DataAssure data;

  @NotBlank(message = "L'information dateAdhesionMutuelle est obligatoire")
  private String dateAdhesionMutuelle;

  private String dateDebutAdhesionIndividuelle;
  private String numeroAdhesionIndividuelle;
  private String dateRadiation;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;

  @NotNull(message = "L'information digitRelation est obligatoire")
  @Valid
  private DigitRelation digitRelation;

  @NotNull(message = "L'information modePaiementPrestations est obligatoire")
  @Valid
  private ModePaiement modePaiementPrestations;

  @NotNull(message = "L'information periodes est obligatoire")
  @Valid
  private List<Periode> periodes;

  private List<Periode> periodesMedecinTraitantOuvert;
  private List<Periode> periodesRegimeParticulierOuvert;
  private List<Periode> periodesBeneficiaireCSSOuvert;

  @NotNull(message = "L'information qualite est obligatoire")
  @Valid
  private QualiteAssure qualite;

  @NotNull(message = "L'information droits est obligatoire")
  @Valid
  private List<DroitAssure> droits;
}
