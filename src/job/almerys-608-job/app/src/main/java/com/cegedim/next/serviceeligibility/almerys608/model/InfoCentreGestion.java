package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Data
@Document(collection = "almv3_InfoCentreGestion")
public class InfoCentreGestion extends BulkObject {
  @Id private String refInterneCG;
  private List<String> infoCartes;
  private String typeGestionnaire;
  private String gestionnaireContrat;
  private Adresse adresseCG;

  @Override
  public void bulk(BulkOperations bulkOperation) {
    Query query = new Query(Criteria.where(Constants.ID).is(refInterneCG));
    bulkOperation.replaceOne(query, this, FindAndReplaceOptions.options().upsert());
  }
}
