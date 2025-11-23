package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/** InfoServiceTP */
@Data
@Document(collection = "almv3_ServiceTP")
public class ServiceTP extends BulkObject {

  // unique key : {numeroContrat, refInterneOS}

  private String numeroContrat;
  private String refInterneOS;
  private String dateDebutValidite;
  private String dateFinValidite;
  private String dateDebutSuspension;
  private String dateFinSuspension;
  private String activationDesactivation;
  private String envoi;
}
