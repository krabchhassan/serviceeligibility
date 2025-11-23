package com.cegedim.next.serviceeligibility.core.job.parameters;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.job.utils.ContractType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class RepriseBeneficiaireParameters {
  private int codeRetour;

  private Set<ContractType> contractTypes;
  private Date dateReprise;

  public RepriseBeneficiaireParameters() {
    this.codeRetour = CODE_RETOUR_OK;

    this.contractTypes = new HashSet<>();
    this.dateReprise = null;
  }
}
