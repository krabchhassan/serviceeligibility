package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.Data;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Data
@Document(collection = "almv3_Contrat")
public class Contrat extends BulkObject {
  private String numeroContrat;
  private String numeroContratFichier;
  private String refAdhContrat;
  private String etatContrat;
  private String dateImmatriculation;
  private String refEntreprise;
  private String refSite;
  private String numeroContratCollectif;
  private String refInterneCG;

  // TODO n est jamais rempli dans talend ??
  @Deprecated private String dateRadiation;

  @Override
  public void bulk(BulkOperations bulkOperation) {
    Query query = new Query(Criteria.where(Constants.NUMERO_CONTRAT).is(numeroContrat));
    bulkOperation.replaceOne(query, this, FindAndReplaceOptions.options().upsert());
  }
}
