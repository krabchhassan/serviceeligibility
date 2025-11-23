package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "almv3_Rattachement")
public class Rattachement extends BulkObject {

  private String numeroContrat;
  private String refOsRattachant;
  private String refOsRattache;
  private String lienJuridique;
}
