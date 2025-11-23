package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.almerys608.utils.Constants608;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Data
@Document(collection = "almv3_InfoEntreprise")
public class InfoEntreprise extends BulkObject {
  @Id private String refEntreprise;
  private String nomEntreprise;
  private List<String> numContratCollectif;
  private List<InfoSite> infoSites;

  @Override
  public void bulk(BulkOperations bulkOperation) {
    Query query = new Query(Criteria.where(Constants.ID).is(refEntreprise));
    Update update =
        new Update()
            .setOnInsert("nomEntreprise", nomEntreprise)
            .setOnInsert("numContratCollectif", numContratCollectif)
            .addToSet(Constants608.INFO_SITES)
            .each(infoSites);
    bulkOperation.upsert(query, update);
  }
}
