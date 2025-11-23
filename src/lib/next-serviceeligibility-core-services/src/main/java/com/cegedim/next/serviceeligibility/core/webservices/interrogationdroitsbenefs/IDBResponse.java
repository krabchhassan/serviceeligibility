package com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeBeneficiaire;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeContrat;
import java.util.List;
import lombok.Data;

@Data
public class IDBResponse {
  private DeclarantAmc declarantAmc;

  private TypeBeneficiaire beneficiaire;

  private List<Periode> periodesDroits;

  private TypeContrat contrat;
}
