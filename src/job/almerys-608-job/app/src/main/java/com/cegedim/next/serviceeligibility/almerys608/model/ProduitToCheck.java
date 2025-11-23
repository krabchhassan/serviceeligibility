package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import lombok.Data;

@Data
public class ProduitToCheck {

  String numContrat;
  String refInterneOS;
  String ordre;
  String dateEntreeProduit;
  String dateSortieProduit;
  String referenceProduit;
  Rejet rejet;
}
