package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclarantDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String nom;
  private String libelle;
  private String siret;
  private String numeroPrefectoral;
  private String codePartenaire;
  private String numeroRNM;

  /* TRACE */
  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;
}
