package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class DeclarantRequestDto implements GenericDto {
  private static final long serialVersionUID = 1L;
  private String idClientBO;
  private String numero;
  private String nom;
  private String libelle;
  private String siret;
  private String codePartenaire;
  private String codeCircuit;
  private String emetteurDroits;
  private String operateurPrincipal;
  private List<String> numerosAMCEchanges;
  private String user;
  private List<PilotageDto> pilotages;
  private List<TranscoDomainesTPDto> transcodageDomainesTP;
  private List<ConventionTPDto> conventionTP;
  private List<CodeRenvoiTPDto> codeRenvoiTP;
  private List<RegroupementDomainesTPDto> regroupementDomainesTP;
  private List<FondCarteTPDto> fondCarteTP;
  private String delaiRetention;
}
