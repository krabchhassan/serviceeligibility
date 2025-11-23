package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import lombok.Data;

@Data
public class PrioriteDroit implements GenericDto {
  private String code;
  private String libelle;
  private String typeDroit;
  private String prioriteBO;
  private String nirPrio1;
  private String nirPrio2;
  private String prioDroitNir1;
  private String prioDroitNir2;
  private String prioContratNir1;
  private String prioContratNir2;
}
