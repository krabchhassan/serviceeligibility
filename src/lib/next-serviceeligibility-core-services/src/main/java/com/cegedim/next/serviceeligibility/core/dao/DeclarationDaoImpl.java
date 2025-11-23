package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.BATCH_SIZE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsultationHistory;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationLight;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository("declarationDaoImpl")
@RequiredArgsConstructor
public class DeclarationDaoImpl implements DeclarationDao {
  private final MongoTemplate template;

  private final BeyondPropertiesService beyondPropertiesService;

  @Override
  @ContinueSpan(log = "createDeclaration")
  public Declaration createDeclaration(Declaration declaration, ClientSession session) {
    if (declaration != null) {
      if (session != null) { // TU
        return template.withSession(session).save(declaration, Constants.DECLARATION_COLLECTION);
      } else {
        return template.save(declaration, Constants.DECLARATION_COLLECTION);
      }
    }

    return null;
  }

  @Override
  @ContinueSpan(log = "findDeclarationsByNomFichierOrigine")
  public List<Declaration> findDeclarationsByNomFichierOrigine(String nomFichierOrigine) {
    Criteria criteria = Criteria.where("nomFichierOrigine").is(nomFichierOrigine);

    return template.find(
        new Query(criteria)
            .limit((int) beyondPropertiesService.getIntegerPropertyOrThrowError(BATCH_SIZE)),
        Declaration.class);
  }

  @Override
  @ContinueSpan(log = "findDeclarationsOfBenef")
  public List<Declaration> findDeclarationsOfBenef(
      String amc,
      String numeroContrat,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String rangAdministratif,
      ClientSession session) {
    Query queryDecl =
        getQueryForDeclarationsBenef(
            amc, numeroPersonne, dateNaissance, rangNaissance, rangAdministratif);
    queryDecl.addCriteria(Criteria.where(Constants.DECLARATION_NUMERO_CONTRAT).is(numeroContrat));
    if (session != null) {
      return template.withSession(session).find(queryDecl, Declaration.class);
    }
    return template.find(queryDecl, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "findDeclarationsOfBenef")
  public List<Declaration> findDeclarationsOfBenef(
      String amc, String numeroPersonne, String dateNaissance, String rangNaissance) {
    Query queryDecl =
        getQueryForDeclarationsBenef(amc, numeroPersonne, dateNaissance, rangNaissance, "");

    return template.find(queryDecl, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "findDeclarationsLightOfBenef")
  public List<DeclarationLight> findDeclarationsLightOfBenef(
      String amc, String numeroPersonne, String dateNaissance, String rangNaissance) {
    Query queryDecl =
        getQueryForDeclarationsBenef(amc, numeroPersonne, dateNaissance, rangNaissance, "");

    List<Declaration> declarations = template.find(queryDecl, Declaration.class);

    List<DeclarationLight> declarationsLight = new ArrayList<>();

    if (!CollectionUtils.isEmpty(declarations)) {
      for (Declaration declaration : declarations) {
        declarationsLight.add(new DeclarationLight(declaration));
      }
    }

    return declarationsLight;
  }

  private Query getQueryForDeclarationsBenef(
      String amc,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String rangAdministratif) {
    Query queryDecl = new Query();
    queryDecl.addCriteria(Criteria.where(Constants.ID_DECLARANT).is(amc));
    if (StringUtils.isNotBlank(numeroPersonne)) {
      queryDecl.addCriteria(
          Criteria.where(Constants.BENEF_IN_DECLARATION + "." + Constants.NUMERO_PERSONNE)
              .is(numeroPersonne));
    }
    if (StringUtils.isNotBlank(dateNaissance)) {
      queryDecl.addCriteria(
          Criteria.where(Constants.BENEF_IN_DECLARATION + "." + Constants.DATE_NAISSANCE)
              .is(dateNaissance));
    }
    if (StringUtils.isNotBlank(rangNaissance)) {
      queryDecl.addCriteria(
          Criteria.where(Constants.BENEF_IN_DECLARATION + "." + Constants.RANG_NAISSANCE)
              .is(rangNaissance));
    }
    if (StringUtils.isNotBlank(rangAdministratif)) {
      queryDecl.addCriteria(
          Criteria.where(Constants.CONTRAT + "." + Constants.RANG_ADMINISTRATIF)
              .is(rangAdministratif));
    }
    queryDecl.with(Sort.by("dateCreation").ascending().and(Sort.by("_id").ascending()));

    return queryDecl;
  }

  private Query getQueryForDeclarationsSuspendus(
      String amc,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String dateDebutSuspension) {
    Query queryDecl =
        getQueryForDeclarationsBenef(amc, numeroPersonne, dateNaissance, rangNaissance, "");
    queryDecl.addCriteria(Criteria.where("dateDebutSuspension").is(dateDebutSuspension));
    return queryDecl;
  }

  @Override
  @ContinueSpan(log = "deleteDeclarationByAmc")
  public long deleteDeclarationByAmc(String idDeclarant) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);

    return template.remove(new Query(criteria), Declaration.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteDeclarationById")
  public long deleteDeclarationById(String id) {
    Criteria criteria = Criteria.where(Constants.ID).is(id);

    return template.remove(new Query(criteria), Declaration.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "replayDeclaration")
  public long replayDeclaration(String idDeclaration) {
    Update update = new Update();
    update.inc("updateElastic", 1);

    return template
        .updateFirst(
            new Query(Criteria.where(Constants.ID).is(new ObjectId(idDeclaration))),
            update,
            Declaration.class)
        .getModifiedCount();
  }

  @Override
  @ContinueSpan(log = "existDeclarationsSuspendues")
  public boolean existDeclarationsSuspendues(
      String amc,
      String numeroContrat,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String dateDebutSuspension) {
    Query queryDecl =
        getQueryForDeclarationsSuspendus(
            amc, numeroPersonne, dateNaissance, rangNaissance, dateDebutSuspension);
    queryDecl.addCriteria(Criteria.where(Constants.DECLARATION_NUMERO_CONTRAT).is(numeroContrat));
    List<Declaration> declarations = template.find(queryDecl, Declaration.class);

    return CollectionUtils.isNotEmpty(declarations);
  }

  @Override
  @ContinueSpan(log = "getDeclarationById")
  public Declaration getDeclarationById(String idDeclaration) {
    return template.findOne(
        new Query(Criteria.where(Constants.ID).is(new ObjectId(idDeclaration))), Declaration.class);
  }

  @Override
  @ContinueSpan(log = "getNextDeclarationById")
  public Declaration getNextDeclarationById(String idDeclaration) {
    return getNextDeclarationByIdAndAmc(idDeclaration, null);
  }

  @Override
  @ContinueSpan(log = "getNextDeclarationByIdAndAmc")
  public Declaration getNextDeclarationByIdAndAmc(String idDeclaration, String amc) {
    Query queryDecl = new Query();
    queryDecl.addCriteria(Criteria.where(Constants.ID).gt(new ObjectId(idDeclaration)));
    if (StringUtils.isNotBlank(amc)) {
      queryDecl.addCriteria(Criteria.where(Constants.ID_DECLARANT).is(amc));
    }
    queryDecl.with(Sort.by(Constants.ID).ascending()).limit(1);
    return template.findOne(queryDecl, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "getNextDeclarationByDateEffet")
  public Declaration getNextDeclarationByDateEffet(LocalDate dateEffet) {
    Query queryDecl = new Query();
    queryDecl.addCriteria(Criteria.where(Constants.EFFET_DEBUT).gt(dateEffet));
    queryDecl.with(Sort.by(Constants.ID).ascending()).limit(1);
    return template.findOne(queryDecl, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "getLastDeclarationId")
  public String getLastDeclarationId() {
    Query queryDecl = new Query();
    queryDecl.with(Sort.by(Constants.ID).descending()).limit(1);
    Declaration declaration = template.findOne(queryDecl, Declaration.class);
    if (declaration != null) {
      return declaration.get_id();
    }
    return "0";
  }

  @Override
  @ContinueSpan(log = "deleteAllDeclarationConsultationHistories")
  public long deleteAllDeclarationConsultationHistories() {
    return template.findAllAndRemove(new Query(), DeclarationConsultationHistory.class).size();
  }

  @Override
  @ContinueSpan(log = "findDeclarationsByAMCandCarteEditable")
  public Stream<Declaration> findDeclarationsByAMCandCarteEditable(
      String idDeclarant, Date dateSynchro) {
    Criteria criteria = getCriteria(idDeclarant, dateSynchro);
    AggregationOperation match = Aggregation.match(criteria);

    Sort sort = getOrders();
    AggregationOperation aggregationSort = Aggregation.sort(sort);
    final Aggregation aggregation =
        Aggregation.newAggregation(match, aggregationSort)
            .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
    return this.template.aggregateStream(
        aggregation, Constants.DECLARATION_COLLECTION, Declaration.class);
  }

  @Override
  @ContinueSpan(log = "findDeclarationsByAMCandCarteEditableAndContracts")
  public Stream<Declaration> findDeclarationsByAMCandCarteEditableAndContracts(
      String idDeclarant, String fromContrat, String toContrat, String fromAdherent) {
    Criteria criteria = getCriteria(idDeclarant, fromContrat, toContrat, fromAdherent);
    AggregationOperation match = Aggregation.match(criteria);

    Sort sort = getOrders();
    AggregationOperation aggregationSort = Aggregation.sort(sort);
    final Aggregation aggregation =
        Aggregation.newAggregation(match, aggregationSort)
            .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
    return this.template.aggregateStream(
        aggregation, Constants.DECLARATION_COLLECTION, Declaration.class);
  }

  @Override
  public List<Declaration> findDeclarationsOfContratAndTrigger(
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger) {
    Query queryDecl = new Query();
    queryDecl.addCriteria(
        Criteria.where(Constants.CONTRAT_NUMERO)
            .is(consolidationDeclarationsContratTrigger.getNumeroContrat()));
    queryDecl.addCriteria(
        Criteria.where(Constants.CONTRAT_NUMERO_ADHERENT)
            .is(consolidationDeclarationsContratTrigger.getNumeroAdherent()));
    queryDecl.addCriteria(
        Criteria.where(Constants.ID_DECLARANT)
            .is(consolidationDeclarationsContratTrigger.getIdDeclarant()));
    queryDecl.addCriteria(
        Criteria.where(Constants.ID_TRIGGER)
            .is(consolidationDeclarationsContratTrigger.getIdTrigger()));
    queryDecl.with(getOrdersForContratTP());
    return template.find(queryDecl, Declaration.class);
  }

  private static Sort getOrders() {
    return Sort.by(
        new Sort.Order(Sort.Direction.DESC, Constants.CONTRAT_NUMERO),
        new Sort.Order(Sort.Direction.DESC, Constants.BENEFICIAIRE_NUMERO_PERSONNE),
        new Sort.Order(Sort.Direction.DESC, Constants.EFFET_DEBUT),
        new Sort.Order(Sort.Direction.DESC, Constants.ID));
  }

  private static Sort getOrdersForContratTP() {
    return Sort.by(new Sort.Order(Sort.Direction.ASC, Constants.ID));
  }

  private static Criteria getCriteria(String idDeclarant, Date dateSynchro) {
    Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.CONTRAT_QUALIFICATION)
            .in(List.of("B", "C"))
            .and("versionDeclaration")
            .ne("V01");
    if (dateSynchro != null) {
      criteria = criteria.and(Constants.EFFET_DEBUT).gte(dateSynchro);
    }
    return criteria;
  }

  private static Criteria getCriteria(
      String idDeclarant, String fromContrat, String toContrat, String fromAdherent) {
    Criteria criteria = getCriteria(idDeclarant, null);
    addContractToCriteria(criteria, fromContrat, toContrat, fromAdherent);
    return criteria;
  }

  private static void addContractToCriteria(
      Criteria criteria, String fromContrat, String toContrat, String fromAdherent) {
    if (toContrat != null) {
      criteria.andOperator(
          Criteria.where(Constants.CONTRAT_NUMERO).gte(fromContrat),
          Criteria.where(Constants.CONTRAT_NUMERO).lte(toContrat));
    } else {
      criteria.and(Constants.CONTRAT_NUMERO).lt(fromContrat);
      criteria.and(Constants.CONTRAT_NUMERO_ADHERENT).lt(fromAdherent);
    }
  }

  @Override
  @ContinueSpan(log = "getNextSortedDeclarationsById")
  public Stream<Declaration> getNextSortedDeclarationsById(String idDeclaration) {
    // L ordre naturel n est pas forcement par id croissant donc le sort est obligatoire
    Sort sort = Sort.by(Constants.ID).ascending();
    Criteria criteria = Criteria.where(Constants.ID).gte(new ObjectId(idDeclaration));
    Query query = Query.query(criteria).with(sort);

    return template.stream(query, Declaration.class);
  }

  @Override
  public List<Declaration> findDeclarationsOfContratBenefAMC(
      String idDeclarant, String numAdherent, String numContrat) {
    Query queryDecl = new Query();
    queryDecl.addCriteria(Criteria.where(Constants.ID_DECLARANT).is(idDeclarant));
    queryDecl.addCriteria(Criteria.where(Constants.CONTRAT_NUMERO_ADHERENT).is(numAdherent));
    queryDecl.addCriteria(Criteria.where(Constants.CONTRAT_NUMERO).gte(numContrat));
    Sort sort =
        Sort.by(
            new Sort.Order(Sort.Direction.ASC, Constants.EFFET_DEBUT),
            new Sort.Order(Sort.Direction.ASC, Constants.ID));
    queryDecl.with(sort);
    return template.find(queryDecl, Declaration.class);
  }

  @Override
  public Integer countDeclaration(String idDeclaration) {
    if (idDeclaration == null) {
      return template
          .getDb()
          .runCommand(new Document("count", Constants.DECLARATION_COLLECTION))
          .getInteger("n");
    }
    Criteria criteria = Criteria.where(Constants.ID).gte(new ObjectId(idDeclaration));

    Query q = new Query(criteria);
    return Math.toIntExact(template.count(q, Declaration.class));
  }

  public Stream<Declaration> getNextSortedDeclarations(Aggregation aggregation) {
    return template.aggregateStream(aggregation, Declaration.class, Declaration.class);
  }

  public String getMinDateFromDeclarations(List<String> idDeclarations) {
    record MinDate(String minDate) {}

    MatchOperation matchDeclarations = Aggregation.match(Criteria.where("_id").in(idDeclarations));
    GroupOperation groupDeclarations =
        Aggregation.group().min("domaineDroit.periodeDroit.periodeDebut").as("minDate");

    Iterator<MinDate> x =
        template
            .aggregate(
                Aggregation.newAggregation(matchDeclarations, groupDeclarations),
                Declaration.class,
                MinDate.class)
            .iterator();
    if (x.hasNext()) {
      return x.next().minDate();
    }
    return null;
  }

  /**
   * Vérifie si des droits TP existent pour un contrat donné
   *
   * @param idDeclarant
   * @param numeroAdherent
   * @param numeroPersonne
   * @param numeroContrat
   * @return vrai si une déclaration existe pour le contrat donné
   */
  public boolean hasTPrightsForContract(
      String idDeclarant, String numeroAdherent, String numeroPersonne, String numeroContrat) {
    Query qryDeclaration = new Query();
    qryDeclaration.addCriteria(
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.CONTRAT_NUMERO_ADHERENT)
            .is(numeroAdherent)
            .and(Constants.CONTRAT_NUMERO)
            .is(numeroContrat)
            .and(Constants.BENEFICIAIRE_NUMERO_PERSONNE)
            .is(numeroPersonne));
    return template.findOne(qryDeclaration, Declaration.class) != null;
  }

  @Override
  public void removeAll() {
    template.findAllAndRemove(new Query(), Constants.DECLARATION_COLLECTION);
  }

  @Override
  public List<Declaration> findAll() {
    return template.findAll(Declaration.class);
  }
}
