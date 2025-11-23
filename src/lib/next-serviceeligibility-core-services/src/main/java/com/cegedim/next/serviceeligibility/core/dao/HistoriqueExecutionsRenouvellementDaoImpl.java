package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.HISTORIQUE_RENOUV_IS_RDO_FIELD;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutionsRenouvellement;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("HistoriqueExecutionsRenouvellement")
@RequiredArgsConstructor
public class HistoriqueExecutionsRenouvellementDaoImpl
    implements HistoriqueExecutionsRenouvellementDao {

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "getLastExecution")
  public HistoriqueExecutionsRenouvellement getLastExecution() {
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.DESC, Constants.ID));
    List<HistoriqueExecutionsRenouvellement> res =
        template.find(query, HistoriqueExecutionsRenouvellement.class);
    if (CollectionUtils.isNotEmpty(res)) {
      return res.get(0);
    }
    return null;
  }

  @Override
  @ContinueSpan(log = "getLastExecutionIgnoringRdo")
  public HistoriqueExecutionsRenouvellement getLastExecutionIgnoringRdo() {
    Query query = new Query();
    query.addCriteria(Criteria.where(HISTORIQUE_RENOUV_IS_RDO_FIELD).ne(true));
    query.with(Sort.by(Sort.Direction.DESC, Constants.ID));
    List<HistoriqueExecutionsRenouvellement> res =
        template.find(query, HistoriqueExecutionsRenouvellement.class);
    if (CollectionUtils.isNotEmpty(res)) {
      return res.get(0);
    }
    return null;
  }

  @Override
  public void create(HistoriqueExecutionsRenouvellement h) {
    template.save(h);
  }

  @Override
  @ContinueSpan(log = "deleteAll historique execution renouvellement")
  public long deleteAll() {
    return template.remove(HistoriqueExecutionsRenouvellement.class).all().getDeletedCount();
  }
}
