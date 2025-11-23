package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.domain.AdresseAvecFixe;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Data
@Document(collection = "almv3_adresseAdherent")
public class AdresseAdherent extends BulkObject {
  @Id private String refInterneOS;
  private String numeroContrat;
  private AdresseAvecFixe adresse;

  @Override
  public void bulk(BulkOperations bulkOperation) {
    Query query = new Query(Criteria.where(Constants.ID).is(refInterneOS));
    bulkOperation.replaceOne(query, this, FindAndReplaceOptions.options().upsert());
  }
}
