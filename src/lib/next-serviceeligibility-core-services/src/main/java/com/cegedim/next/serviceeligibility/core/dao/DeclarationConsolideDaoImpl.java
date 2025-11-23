package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeclarationConsolideDaoImpl implements DeclarationConsolideDao {

  private final MongoTemplate mongoTemplate;

  @Override
  public Collection<DeclarationConsolide> insertAll(
      List<DeclarationConsolide> declarationConsolides) {
    mongoTemplate.setWriteConcern(WriteConcern.W1.withJournal(false));
    return mongoTemplate.insertAll(declarationConsolides);
  }

  @ContinueSpan(log = "getDeclarationsConsolideesByAmcContrats")
  @Override
  public List<DeclarationConsolide> getDeclarationsConsolideesByAmcContrats(String amcContracts) {
    Criteria criteria = Criteria.where(Constants.AMC_CONTRAT).is(amcContracts);
    return mongoTemplate.find(new Query(criteria), DeclarationConsolide.class);
  }

  @Override
  public void deleteAll() {
    mongoTemplate.remove(new Query(), DeclarationConsolide.class);
  }

  @Override
  public void bulkDeleteUpdate(
      List<DeclarationConsolide> toDeletes,
      List<DeclarationConsolide> toUpdates,
      ClientSession session) {
    mongoTemplate.withSession(session).setWriteConcern(WriteConcern.W1.withJournal(false));
    BulkOperations bulkOp =
        mongoTemplate
            .withSession(session)
            .bulkOps(BulkOperations.BulkMode.UNORDERED, DeclarationConsolide.class);

    List<Query> removes = new ArrayList<>();
    for (DeclarationConsolide toDelete : toDeletes) {
      Query query = Query.query(Criteria.where(Constants.ID).is(toDelete.get_id()));
      removes.add(query);
    }
    if (!removes.isEmpty()) {
      bulkOp.remove(removes);
    }

    List<Pair<Query, UpdateDefinition>> updates = new ArrayList<>();
    for (DeclarationConsolide toUpdate : toUpdates) {
      Query query = Query.query(Criteria.where(Constants.ID).is(toUpdate.get_id()));
      Update update = new Update();
      update.set(Constants.IDENTIFIANT, toUpdate.getIdentifiant());
      updates.add(Pair.of(query, update));
    }
    if (!updates.isEmpty()) {
      bulkOp.updateMulti(updates);
    }

    if (!updates.isEmpty() || !removes.isEmpty()) {
      bulkOp.execute();
    }
  }

  @Override
  @ContinueSpan(log = "deleteByAMC")
  public long deleteByAMC(String amc) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(amc);
    return mongoTemplate.remove(new Query(criteria), DeclarationConsolide.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteByDeclaration")
  public long deleteByDeclaration(String declaration) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARATION).is(declaration);
    return mongoTemplate.remove(new Query(criteria), DeclarationConsolide.class).getDeletedCount();
  }
}
