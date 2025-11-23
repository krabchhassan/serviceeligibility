package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "almv3_Produit")
public class Produit extends BulkObject {

  private boolean isProduitAnnule;
  private String numeroContrat;
  private String refInterneOS;
  private Integer ordre;
  private String referenceProduit;
  private String dateEntreeProduit;
  private String dateSortieProduit;
}
