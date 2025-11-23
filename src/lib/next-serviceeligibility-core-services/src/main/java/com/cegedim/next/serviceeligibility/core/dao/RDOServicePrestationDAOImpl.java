package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.SERVICE_PRESTATIONS_RDO;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.RDOGroup;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ServicePrestationsRdo;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("RDOServicePrestationDAOImpl")
@RequiredArgsConstructor
public class RDOServicePrestationDAOImpl implements RDOServicePrestationDAO {

  private final MongoTemplate template;

  /**
   * Exemple de requete mongo : <a
   * href="https://cegedim-insurance.atlassian.net/browse/BLUE-6228">cf commentaire jira</a>
   */
  @Override
  public void upsert(RDOGroup rdoGroup) {
    Query query = Query.query(Criteria.where(Constants.ID).is(rdoGroup.get_id()));

    List<ServicePrestationsRdo> servicePrestationsRdos =
        ListUtils.emptyIfNull(rdoGroup.getServicePrestationsRdo());
    List<String> ids =
        servicePrestationsRdos.stream().map(ServicePrestationsRdo::getNumero).toList();

    final String itemCursor = "rdo";
    BooleanOperators.Not keepNotSameNum =
        BooleanOperators.not(
            ArrayOperators.In.arrayOf(ids).containsValue("$$" + itemCursor + ".numero"));

    ArrayOperators.Filter filterByNum =
        ArrayOperators.Filter.filter(SERVICE_PRESTATIONS_RDO).as(itemCursor).by(keepNotSameNum);

    ConditionalOperators.IfNull ifNewList =
        ConditionalOperators.ifNull(filterByNum).then(Collections.emptyList());

    AggregationUpdate update =
        Aggregation.newUpdate()
            .set(SERVICE_PRESTATIONS_RDO)
            .toValue(ArrayOperators.ConcatArrays.arrayOf(servicePrestationsRdos).concat(ifNewList));

    template.upsert(query, update, RDOGroup.class);
  }

  @Override
  public void upsertMulti(List<RDOGroup> rdoGroups) {
    // Impossible de passer par un bulk pour traiter la liste d'un coup en mode
    // upsert -> bulk upsert n accepte pas les AggregationUpdate dans la version
    // mongo actuelle. La version qui fonctionne est la 4.1 cf
    // https://github.com/spring-projects/spring-data-mongodb/issues/3872
    for (RDOGroup rdoGroup : ListUtils.emptyIfNull(rdoGroups)) {
      upsert(rdoGroup);
    }
  }

  @Override
  public RDOGroup getRDOGroupById(String id) {
    return template.findById(id, RDOGroup.class);
  }

  @Override
  public void createRDOGroup(RDOGroup rdoGroup) {
    template.save(rdoGroup, Constants.RDO_SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  public void deleteAll() {
    template.remove(new Query(), RDOGroup.class);
  }

  @Override
  public long deleteByAMC(String idAMC) {
    Criteria criteria = Criteria.where(Constants.ID).regex("^" + idAMC, "i");
    return template.remove(Query.query(criteria), RDOGroup.class).getDeletedCount();
  }
}
