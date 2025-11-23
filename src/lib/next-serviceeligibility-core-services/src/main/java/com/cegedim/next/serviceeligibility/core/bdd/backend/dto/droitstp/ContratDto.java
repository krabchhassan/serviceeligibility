package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class ContratDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String numero;

  private String dateSouscription;

  private String dateResiliation;

  private String type;

  private String nomPorteur;

  private String prenomPorteur;

  private String civilitePorteur;

  private String numeroAdherent;

  private String qualification;

  private String numeroContratCollectif;

  private String rangAdministratif;

  private Boolean isContratResponsable;

  private Boolean isContratCMU;

  private String individuelOuCollectif;

  private String situationDebut;

  private String situationFin;

  private String motifFinSituation;

  private String lienFamilial;

  private String situationParticuliere;

  private String codeItelis;

  private String libelleContratCollectif;
}
