package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.dao.util.Criteria608Util;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationsConsolideesAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class DeclarationsConsolideesAlmerysDaoImpl implements DeclarationsConsolideesAlmerysDao {

  private final MongoTemplate mongoTemplate;

  @Override
  public void save(DeclarationsConsolideesAlmerys declarationsConsolideesAlmerys) {
    mongoTemplate.save(declarationsConsolideesAlmerys);
  }

  @Override
  public void createIndexOnTmp2(String collectionName) {
    Document document = new Document();
    document.append(Constants.CONTRAT_NUMERO, 1);
    document.append(Constants.EFFET_DEBUT, -1);
    mongoTemplate.getCollection(collectionName).createIndex(document);

    document = new Document();
    document.append(Constants.EFFET_DEBUT, -1);
    mongoTemplate.getCollection(collectionName).createIndex(document);
  }

  @Override
  public void createTmpCollection(Aggregation aggregation) {
    mongoTemplate.aggregate(
        aggregation, Constants.DECLARATIONS_CONSOLIDEES_ALMERYS, Document.class);
  }

  @Override
  public void dropTemporaryCollection(String tmpCollectionName) {
    if (mongoTemplate.collectionExists(tmpCollectionName)) {
      mongoTemplate.dropCollection(tmpCollectionName);
    }
  }

  @Override
  public Stream<TmpObject2> getAll(String collectionName) {
    Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, Constants.EFFET_DEBUT));
    return mongoTemplate.stream(new Query().with(sort), TmpObject2.class, collectionName);
  }

  @Override
  public Stream<TmpObject2> getAllForSouscripteur(String collectionName) {
    Sort sort =
        Sort.by(
            new Sort.Order(Sort.Direction.ASC, Constants.CONTRAT_NUMERO),
            new Sort.Order(Sort.Direction.DESC, Constants.EFFET_DEBUT));
    return mongoTemplate.stream(new Query().with(sort), TmpObject2.class, collectionName);
  }

  @Override
  public Integer countDeclarationConsolideesAlmerys(
      Declarant declarant,
      Pilotage pilotage,
      Date dateDerniereExecution,
      List<String> critSecondaireDetailleToExclude) {
    Criteria criteria =
        Criteria608Util.getCriteria(
            declarant, pilotage, dateDerniereExecution, critSecondaireDetailleToExclude);
    Query q = new Query(criteria);
    return Math.toIntExact(mongoTemplate.count(q, DeclarationsConsolideesAlmerys.class));
  }

  public Integer countAll() {
    return mongoTemplate
        .getDb()
        .runCommand(new Document("count", Constants.DECLARATIONS_CONSOLIDEES_ALMERYS))
        .getInteger("n");
  }

  @Override
  public void createIndexContratRefInterneOsOnTmpTable(String collectionName) {
    Document document = new Document();
    document.append(Constants.NUMERO_CONTRAT, 1);
    document.append("refInterneOS", 1);
    mongoTemplate.getCollection(collectionName).createIndex(document);
  }

  @Override
  public void createIndexContratOnTmpTable(String collectionName) {
    Document document = new Document();
    document.append(Constants.NUMERO_CONTRAT, 1);
    mongoTemplate.getCollection(collectionName).createIndex(document);
  }
}
