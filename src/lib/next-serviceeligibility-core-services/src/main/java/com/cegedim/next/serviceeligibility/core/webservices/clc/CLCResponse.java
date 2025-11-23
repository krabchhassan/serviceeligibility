package com.cegedim.next.serviceeligibility.core.webservices.clc;

import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeBeneficiaire;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeContrat;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.DeclarantAmc;
import java.util.List;
import lombok.Data;

@Data
public class CLCResponse {
  private DeclarantAmc declarantAmc;

  private TypeBeneficiaire beneficiaire;

  private List<DomaineDroitContratCLC> domainesDroits;

  private TypeContrat contrat;
}
