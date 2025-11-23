package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "almv3_Souscripteur")
@Data
public class Souscripteur extends BulkObject {

  private String numeroContrat;
  private String refInterneOs;
  private boolean souscripteur; // NOSONAR
  private String position;
}
