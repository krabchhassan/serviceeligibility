package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("LotDaoImpl")
@RequiredArgsConstructor
public class LotDaoImpl implements LotDao {

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "getById lot")
  public Lot getById(String id) {
    return template.findById(id, Lot.class);
  }

  @Override
  @ContinueSpan(log = "getListByIdsForRenewal lot")
  @Cacheable(value = "lotCacheByIdForRenewal", key = "{#ids}")
  public List<Lot> getListByIdsForRenewal(List<String> ids) {
    if (CollectionUtils.isEmpty(ids)) {
      return new ArrayList<>();
    }

    List<Criteria> criteriaList = new ArrayList<>();

    for (String id : ids) {
      criteriaList.add(Criteria.where("_id").is(id));
    }

    Criteria orCriteria = new Criteria().orOperator(criteriaList);

    return template.find(new Query(orCriteria), Lot.class);
  }

  @Override
  @ContinueSpan(log = "getListById lot")
  // BLUE-6117 : désactivation du cache (voir l'historique git pour plus d'infos)
  public List<Lot> getListByIds(List<String> ids) {
    if (CollectionUtils.isEmpty(ids)) {
      return new ArrayList<>();
    }

    List<Criteria> criteriaList = new ArrayList<>();

    for (String id : ids) {
      criteriaList.add(Criteria.where("_id").is(id));
    }

    Criteria orCriteria = new Criteria().orOperator(criteriaList);

    return template.find(new Query(orCriteria), Lot.class);
  }

  @Override
  @ContinueSpan(log = "getById lot")
  public Lot getByCode(String codeLot) {
    Criteria criteria = Criteria.where("code").is(codeLot);
    return template.findOne(new Query(criteria), Lot.class);
  }

  @Override
  @ContinueSpan(log = "deleteAll lots")
  public void deleteAllLots() {
    template.findAllAndRemove(new Query(), "lot");
  }

  @Override
  @ContinueSpan(log = "findByGT lot")
  public List<Lot> findByGT(String guaranteeCode, String insurerCode) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(Constants.GARANTIE_TECHNIQUES)
            .elemMatch(
                Criteria.where(Constants.CODE_GARANTIE)
                    .is(guaranteeCode)
                    .and(Constants.CODE_ASSUREUR)
                    .is(insurerCode)
                    .orOperator(
                        Criteria.where(Constants.DATE_SUPPRESSION_LOGIQUE).is(null),
                        Criteria.where(Constants.DATE_SUPPRESSION_LOGIQUE).is(""))));
    return template.find(query, Lot.class);
  }
}
