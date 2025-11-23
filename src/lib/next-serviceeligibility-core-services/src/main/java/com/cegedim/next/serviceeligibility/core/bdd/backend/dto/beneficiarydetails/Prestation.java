package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;
import lombok.Data;

@Data
public class Prestation implements GenericDto {
  private String code;
  private String codeRegroupement;
  private String libelle;
  private Boolean isEditionRisqueCarte = false;
  private String dateEffet;
  private Formule formule;
  private FormuleMetier formuleMetier;
}
