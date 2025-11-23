package com.cegedim.next.serviceeligibility.core.dao;

import static org.springframework.data.mongodb.core.query.Query.query;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.query.ContractRequest;
import com.cegedim.next.serviceeligibility.core.services.contracttp.BulkContratTP;
import com.cegedim.next.serviceeligibility.core.services.pojo.BillingResult;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteResult;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository("contractDaoImpl")
@RequiredArgsConstructor
@Slf4j
public class ContractDaoImpl implements ContractDao {

  private static final String ID_DECLARANT = "idDeclarant";
  private static final String NIR = "beneficiaires.nirBeneficiaire";
  private static final String NIR_OD1 = "beneficiaires.nirOd1";
  private static final String NIR_OD2 = "beneficiaires.nirOd2";
  private static final String DATE_NAISSANCE = "beneficiaires.dateNaissance";
  private static final String PERIODES_DROIT =
      "beneficiaires.domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit";
  private static final String PERIODE_DEBUT = "periodeDebut";
  private static final String PERIODE_FIN = "periodeFin";
  private static final String PERIODE_TYPE = "typePeriode";
  private static final String PERSON_NUMBER = "beneficiaires.numeroPersonne";

  private final MongoTemplate mongoTemplate;

  @Override
  @ContinueSpan(log = "getContract (3 params)")
  public ContractTP getContract(String idDeclarant, String numeroContrat, String numeroAdherent) {
    // Critères de recherche d'un contrat (unique)
    Criteria contratCriteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.NUMERO_CONTRAT)
            .is(numeroContrat)
            .and(Constants.NUMERO_ADHERENT)
            .is(numeroAdherent);
    return mongoTemplate.findOne(
        Query.query(contratCriteria), ContractTP.class, Constants.CONTRATS_COLLECTION_NAME);
  }

  @ContinueSpan(log = "getContract (4 params)")
  @Cacheable(
      value = "contrat634",
      key = "{#idDeclarant,#numeroContrat,#numeroAdherent,#collection}",
      unless = "#result == null")
  public ContractTP getContract(
      String idDeclarant, String numeroContrat, String numeroAdherent, String collection) {
    // Critères de recherche d'un contrat (unique)
    Criteria contratCriteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.NUMERO_CONTRAT)
            .is(numeroContrat)
            .and(Constants.NUMERO_ADHERENT)
            .is(numeroAdherent);
    return mongoTemplate.findOne(Query.query(contratCriteria), ContractTP.class, collection);
  }

  @Override
  @ContinueSpan(log = "saveContract (1 param)")
  public void saveContract(ContractTP contractTP) {
    saveContract(contractTP, Constants.CONTRATS_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "saveContract (2 params)")
  public void saveContract(ContractTP contractTP, String collection) {
    mongoTemplate.save(contractTP, collection);
  }

  @Override
  @ContinueSpan(log = "deleteContract (1 param)")
  public void deleteContract(String id) {
    deleteContract(id, Constants.CONTRATS_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "deleteContract (2 params)")
  public void deleteContract(String id, String collection) {
    Query q = new Query();
    q.addCriteria(Criteria.where(Constants.ID).is(id));
    mongoTemplate.findAndRemove(q, ContractTP.class, collection);
  }

  @Override
  @ContinueSpan(log = "deleteContractByAmc")
  public long deleteContractByAmc(String idDeclarant) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);

    return mongoTemplate.remove(new Query(criteria), ContractTP.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "getContractsForBillingJob")
  public List<BillingResult> getContractsForBillingJob(LocalDate date) {
    String formatedDate = DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED).format(date);

    UnwindOperation unwindBenefs = Aggregation.unwind(Constants.BENEFICIAIRE_COLLECTION_NAME);
    ProjectionOperation projectFoDistinct =
        getProjectForDistinct(
            Aggregation.project(
                Constants.ID_DECLARANT, Constants.GESTIONNAIRE, Constants.DATE_RESILIATION));

    Criteria criteriaNotResiliated = getCriteriaNotResiliated(formatedDate);
    Criteria criteriaNotFinished = getCriteriaNotFinished(formatedDate);
    Criteria criteriaStarted = getCriteriaStarted(formatedDate);

    GroupOperation distinct =
        Aggregation.group(
            Constants.ID_DECLARANT,
            Constants.GESTIONNAIRE,
            Constants.NIR_BENEF,
            Constants.DATE_NAISSANCE,
            Constants.RANG_NAISSANCE);

    GroupOperation byDeclarantGestionnaire =
        Aggregation.group(Constants.ID_DECLARANT, Constants.GESTIONNAIRE)
            .count()
            .as(Constants.COUNT);

    ProjectionOperation finalResult =
        Aggregation.project(Constants.COUNT, "_id.idDeclarant", "_id.gestionnaire")
            .andExclude("_id");

    Aggregation aggregation =
        Aggregation.newAggregation(
            unwindBenefs,
            projectFoDistinct,
            Aggregation.match(
                new Criteria()
                    .andOperator(criteriaNotFinished, criteriaStarted, criteriaNotResiliated)),
            distinct,
            byDeclarantGestionnaire,
            finalResult);

    return mongoTemplate
        .aggregate(aggregation, ContractTP.class, BillingResult.class)
        .getMappedResults();
  }

  @NotNull
  private static ProjectionOperation getProjectForDistinct(
      ProjectionOperation projectionOperation) {
    return projectionOperation
        .and(DATE_NAISSANCE)
        .as(Constants.DATE_NAISSANCE)
        .and("beneficiaires.rangNaissance")
        .as(Constants.RANG_NAISSANCE)
        .and("beneficiaires.domaineDroits")
        .as("domaineDroits")
        .and(ConditionalOperators.ifNull(NIR).then(NIR_OD1))
        .as(Constants.NIR_BENEF)
        .andExclude("_id");
  }

  @NotNull
  private static Criteria getCriteriaStarted(String formatedDate) {
    return Criteria.where(
            "domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit.periodeDebut")
        .lte(formatedDate);
  }

  @NotNull
  private static Criteria getCriteriaNotFinished(String formatedDate) {
    return Criteria.where(
            "domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit.periodeFin")
        .gte(formatedDate);
  }

  @NotNull
  private static Criteria getCriteriaNotResiliated(String formatedDate) {
    return new Criteria()
        .orOperator(
            Criteria.where(Constants.DATE_RESILIATION).isNull(),
            Criteria.where(Constants.DATE_RESILIATION).gt(formatedDate));
  }

  @Override
  @ContinueSpan(log = "getContractsForBillingOTPJob")
  public List<BillingResult> getContractsForBillingOTPJob(LocalDate date, String amc) {
    String formatedDate = DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED).format(date);

    UnwindOperation unwindBenefs = Aggregation.unwind(Constants.BENEFICIAIRE_COLLECTION_NAME);
    ProjectionOperation projectFoDistinct =
        getProjectForDistinct(Aggregation.project(Constants.ID_DECLARANT));

    Criteria criteriaNotResiliated = getCriteriaNotResiliated(formatedDate);
    Criteria criteriaNotFinished =
        Criteria.where(
                "beneficiaires.domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit.periodeFin")
            .gte(formatedDate);
    Criteria criteriaStarted =
        Criteria.where(
                "beneficiaires.domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit.periodeDebut")
            .lte(formatedDate);

    Criteria criteriaAmc = Criteria.where(Constants.ID_DECLARANT).is(amc);

    GroupOperation distinct =
        Aggregation.group(
            Constants.ID_DECLARANT,
            Constants.NIR_BENEF,
            Constants.DATE_NAISSANCE,
            Constants.RANG_NAISSANCE);

    GroupOperation byDeclarant =
        Aggregation.group(Constants.ID_DECLARANT).count().as(Constants.COUNT);

    ProjectionOperation finalResult =
        Aggregation.project(Constants.COUNT)
            .and("_id")
            .as(Constants.ID_DECLARANT)
            .andExclude("_id");

    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.match(
                new Criteria()
                    .andOperator(
                        criteriaAmc, criteriaNotFinished, criteriaStarted, criteriaNotResiliated)),
            unwindBenefs,
            projectFoDistinct,
            Aggregation.match(getCriteriaNotFinished(formatedDate)),
            distinct,
            byDeclarant,
            finalResult);

    return mongoTemplate
        .aggregate(aggregation, ContractTP.class, BillingResult.class)
        .getMappedResults();
  }

  @Override
  public int bulkInsert(List<ContractTP> contrats, String collection) {
    if (contrats.isEmpty()) {
      return 0;
    } else {
      Instant start = Instant.now();
      mongoTemplate.setWriteConcern(WriteConcern.W1.withJournal(false));
      // mongoTemplate.setWriteConcern(WriteConcern.W1.withJournal(true));
      // mongoTemplate.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
      BulkOperations bulkInsertion =
          mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ContractTP.class, collection);
      bulkInsertion.insert(contrats);
      BulkWriteResult bulkWriteResult = bulkInsertion.execute();
      log.info(
          "Insertion de {} documents en {} milliseconds",
          bulkWriteResult.getInsertedCount(),
          Duration.between(start, Instant.now()).toMillis());
      return bulkWriteResult.getInsertedCount();
    }
  }

  @Override
  public void bulkOp(BulkContratTP bulkContratTP, String collection) {
    boolean toExecute = false;
    mongoTemplate.setWriteConcern(WriteConcern.W1.withJournal(false));
    BulkOperations bulkOperations =
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ContractTP.class, collection);
    if (!bulkContratTP.toInsert.isEmpty()) {
      toExecute = true;
      bulkOperations.insert(new ArrayList<>(bulkContratTP.toInsert));
    }

    List<Query> removes = new ArrayList<>();
    for (ContractTP toDelete : bulkContratTP.toDelete) {
      Query query = Query.query(Criteria.where(Constants.ID).is(toDelete.get_id()));
      removes.add(query);
    }
    if (!removes.isEmpty()) {
      toExecute = true;
      bulkOperations.remove(removes);
    }

    if (!bulkContratTP.toUpdate.isEmpty() && !toExecute) {
      toExecute = true;
    }

    for (ContractTP toUpdate : bulkContratTP.toUpdate) {
      Query query = Query.query(Criteria.where(Constants.ID).is(toUpdate.get_id()));
      bulkOperations.replaceOne(query, toUpdate);
    }
    if (toExecute) {
      bulkOperations.execute();
    }
  }

  @Override
  public List<ContractTP> findBy(ContractRequest request) {
    final var find = this.toQuery(request);
    this.mongoTemplate.setReadPreference(ReadPreference.secondary());
    return this.mongoTemplate.find(query(find), ContractTP.class);
  }

  @Override
  public long countBy(ContractRequest request) {
    final var find = this.toQuery(request);
    return this.mongoTemplate.count(query(find), ContractTP.class);
  }

  // --------------------
  // UTILS
  // --------------------
  private Criteria toQuery(ContractRequest request) {
    final var criteria = new Criteria();

    // AMC
    if (StringUtils.hasLength(request.getAmc())) criteria.and(ID_DECLARANT).is(request.getAmc());

    // NIR (nirBeneficiaire | nirOd1 | nirOd2)
    if (StringUtils.hasLength(request.getNir())) {
      final var nirMatch = Criteria.where(NIR).is(request.getNir());
      final var nirOd1Match = Criteria.where(NIR_OD1).is(request.getNir());
      final var nirOd2Match = Criteria.where(NIR_OD2).is(request.getNir());
      criteria.orOperator(nirMatch, nirOd1Match, nirOd2Match);
    }

    // BIRTHDATE
    String birthDate = request.getBirthDate();
    if (StringUtils.hasLength(birthDate)) {
      final var birthdayMatch = new Criteria();

      if (birthDate.length() == 6) {
        // Handle century match
        String[] centuryOptions = getDateWithCentury(birthDate).split(",");
        birthdayMatch.orOperator(
            Criteria.where(DATE_NAISSANCE).is(centuryOptions[0]),
            Criteria.where(DATE_NAISSANCE).is(centuryOptions[1]));
      } else if (birthDate.length() == 8) {
        // Exact match
        final var exactMatch = Criteria.where(DATE_NAISSANCE).is(birthDate);

        // Tolerance match using regex
        String year = birthDate.substring(0, 4);
        String month = birthDate.substring(4, 6);
        String day = birthDate.substring(6, 8);

        birthdayMatch.orOperator(
            exactMatch,
            Criteria.where(DATE_NAISSANCE).regex("^" + year + ".*"),
            Criteria.where(DATE_NAISSANCE).regex("^\\d{4}" + month + ".*"),
            Criteria.where(DATE_NAISSANCE).regex("^\\d{6}" + day + ".*"));
      }

      criteria.andOperator(birthdayMatch);
    }

    // PERIODES DROIT (type, debut <= date <= fin)
    if (StringUtils.hasLength(request.getSearchDate())) {
      // debut <= X
      final var match = Criteria.where(PERIODE_DEBUT).lte(request.getSearchDate());
      // fin >= X (null safe)
      match.and(PERIODE_FIN).not().lt(request.getSearchDate());
      // type
      if (request.getType() != null) match.and(PERIODE_TYPE).is(request.getType());

      criteria.and(PERIODES_DROIT).elemMatch(match);
    }

    return criteria;
  }

  private String getDateWithCentury(String birthDate) {
    String year = birthDate.substring(0, 2);
    String rest = birthDate.substring(2);

    return String.format("19%s%s,20%s%s", year, rest, year, rest);
  }

  @Override
  public List<ContractTP> findBy(String amc, String personNumber) {
    final Criteria criteria = new Criteria();
    criteria.and(ID_DECLARANT).is(amc);
    criteria.and(PERSON_NUMBER).is(personNumber);
    return this.mongoTemplate.find(query(criteria), ContractTP.class);
  }
}
