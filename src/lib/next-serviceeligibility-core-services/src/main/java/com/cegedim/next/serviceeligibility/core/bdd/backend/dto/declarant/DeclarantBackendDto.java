package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** Classe DTO de l'entite {@code Declarant}. */
@Data
@JsonInclude(Include.NON_NULL)
public class DeclarantBackendDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String idClientBO;
  private String numero;
  private String nom;
  private String libelle;
  private String siret;
  private String codePartenaire;
  private String codeCircuit;
  private String emetteurDroits;
  private List<String> numerosAMCEchanges;
  private String operateurPrincipal;

  /* DOCUMENTS EMBEDDED */
  private List<PilotageDto> pilotages = new ArrayList<>();
  private List<TranscoDomainesTPDto> transcodageDomainesTP;
  private List<ConventionTPDto> conventionTP;
  private List<CodeRenvoiTPDto> codeRenvoiTP;
  private List<RegroupementDomainesTPDto> regroupementDomainesTP;
  private List<FondCarteTPDto> fondCarteTP;
  private String delaiRetention;
}
