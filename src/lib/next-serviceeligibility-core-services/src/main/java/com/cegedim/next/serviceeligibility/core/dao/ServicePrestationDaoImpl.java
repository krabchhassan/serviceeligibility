package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.BATCH_SIZE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationCommun;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ServicePrestationsRdo;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.AssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CodePeriode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5Recipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6Light;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.CustomAggregationOperation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository("servicePrestationDaoImpl")
public class ServicePrestationDaoImpl implements ServicePrestationDao {
  public static final String BATCH_EXECUTION_ID = "batchExecutionId";

  private final Logger logger = LoggerFactory.getLogger(ServicePrestationDaoImpl.class);

  private final MongoTemplate template;

  private final LotDao lotDao;

  private final BeyondPropertiesService beyondPropertiesService;

  public ServicePrestationDaoImpl(
      MongoTemplate template, LotDao lotDao, BeyondPropertiesService beyondPropertiesService) {
    this.template = template;
    this.lotDao = lotDao;
    this.beyondPropertiesService = beyondPropertiesService;
  }

  private static final String SERVICE_PRESTATION = "servicePrestation";

  private static final String ASSURES = "assures";
  private static final String ASSURES_DROITS = "assures.droits";
  private static final String PERIODE_FIN = "periode.fin";
  private static final String PERIODE_DEBUT = "periode.debut";
  private static final String NIR_CODE = "nir.code";
  private static final String ASSURES_IDENTITE_NIR_AFFILIATION_RO =
      "assures.identite.affiliationsRO";
  private static final String ASSURES_IDENTITE_NIR_CODE = "assures.identite.nir.code";
  private static final String ASSURES_IDENTITE_RANG_NAISSANCE = "assures.identite.rangNaissance";
  private static final String ASSURES_IDENTITE_DATE_NAISSANCE = "assures.identite.dateNaissance";
  private static final String NUMERO_ADHERENT = "numeroAdherent";
  private static final String ASSURES_IDENTITE_NUMERO_PERSONNE = "assures.identite.numeroPersonne";
  private static final String ASSURES_IDENTITE_AFFILIATIONS_RO = "assures.identite.affiliationsRO";
  private static final String ID_DECLARANT = "idDeclarant";
  private static final String DATE_RESILIATION = "dateResiliation";
  private static final String DATE_SOUSCRIPTION = "dateSouscription";
  private static final String NUMERO = "numero";

  @Override
  @ContinueSpan(log = "getContratForParametrageStream")
  public Stream<ContratAIV6> getContratForParametrageStream(
      final ParametrageCarteTP param,
      final Date dateTraitement,
      final String dateValiditeDroit,
      final String batchExecutionId) {
    final Aggregation agg =
        this.getAggregationContratForParametrageStream(
            param, dateTraitement, dateValiditeDroit, batchExecutionId);
    if (agg != null) {
      return this.template.aggregateStream(
          agg, Constants.SERVICE_PRESTATION_COLLECTION, ContratAIV6.class);
    }
    return null;
  }

  @Override
  @ContinueSpan(log = "getAllAggregationContratForParametrageStream")
  public Stream<ContratAIV6> getAllContratForParametrageStream(String idDeclarant) {
    return this.template.aggregateStream(
        this.getAllAggregationContratForParametrageStream(idDeclarant),
        Constants.SERVICE_PRESTATION_COLLECTION,
        ContratAIV6.class);
  }

  private Aggregation getAllAggregationContratForParametrageStream(String idDeclarant) {
    // Il reste au moins un assure
    Criteria criteria = new Criteria();
    criteria.and(String.format("%s.0", Constants.ASSURES)).exists(true);
    if (idDeclarant != null) {
      criteria.and(Constants.ID_DECLARANT).is(idDeclarant);
    }
    AggregationOperation matchOperation = Aggregation.match(criteria);

    return Aggregation.newAggregation(matchOperation)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  private Aggregation getAggregationContratForParametrageStream(
      final ParametrageCarteTP param,
      final Date dateTraitement,
      final String dateValiditeDroit,
      final String batchExecutionId) {
    Criteria criteria = new Criteria().and(Constants.ID_DECLARANT).is(param.getAmc());

    // Dans le cas d'un paramétrage Automatique, on ne veut pas prendre les
    // contrats
    // ayant déjà été traités par le batch
    ParametrageRenouvellement parametrageRenouvellement = param.getParametrageRenouvellement();
    if (ModeDeclenchementCarteTP.Automatique.equals(
        parametrageRenouvellement.getModeDeclenchement())) {
      criteria.and(BATCH_EXECUTION_ID).ne(batchExecutionId);
    }

    if (StringUtils.isNotBlank(param.getIdentifiantCollectivite())) {
      criteria
          .and(
              String.format(
                  Constants.S_S, Constants.CONTRAT_COLLECTIF, Constants.IDENTIFIANT_COLLECTIVITE))
          .is(param.getIdentifiantCollectivite());
    }
    if (StringUtils.isNotBlank(param.getGroupePopulation())) {
      criteria
          .and(
              String.format(
                  Constants.S_S, Constants.CONTRAT_COLLECTIF, Constants.GROUPE_POPULATION))
          .is(param.getGroupePopulation());
    }
    if (StringUtils.isNotBlank(param.getCritereSecondaireDetaille())) {
      criteria.and(Constants.CRITERE_SECONDAIRE_DETAILLE).is(param.getCritereSecondaireDetaille());
    }
    // Gestion de la date anniversaire contrat
    if (parametrageRenouvellement
        .getDateRenouvellementCarteTP()
        .equals(DateRenouvellementCarteTP.AnniversaireContrat)) {
      if (ModeDeclenchementCarteTP.Automatique.equals(
          parametrageRenouvellement.getModeDeclenchement())) {
        aggregationForAutomaticAnniversary(param, dateTraitement, criteria);
      } else {
        aggregationForManualAnniversary(dateTraitement, criteria);
      }
    }
    criteria =
        criteria.orOperator(
            Criteria.where(DATE_RESILIATION).isNull(),
            Criteria.where(DATE_RESILIATION).gte(dateValiditeDroit));
    final AggregationOperation match = Aggregation.match(criteria);

    // Requete de filtre
    String queryFilter;
    try (InputStream inputStream =
        getClass()
            .getResourceAsStream("/FiltreServicePrestationAssureAvecDroitsOuverts.requeteMongo")) {
      if (inputStream == null) {
        return null;
      }
      queryFilter = new String(inputStream.readAllBytes());
      queryFilter = queryFilter.replace("##DATE_RECHERCHE_DROIT##", dateValiditeDroit);

      final String dateFinDroitAnnee = dateValiditeDroit.substring(0, 4) + "-12-31";
      queryFilter = queryFilter.replace("##DATE_FIN_DROIT##", dateFinDroitAnnee);
    } catch (final IOException e1) {
      this.logger.error(
          "Unable to open file FiltreServicePrestationAssureAvecDroitsOuverts.requeteMongo", e1);
      return null;
    }

    // Il reste au moins un assure
    final AggregationOperation matchWithInsured =
        Aggregation.match(
            new Criteria().and(String.format("%s.0", Constants.ASSURES)).exists(true));

    // Filtre les assures restants en fonction des garanties du paramétrage de carte
    // TP
    final AggregationOperation matchGaranties = getMatchGaranties(param);
    if (matchGaranties != null) {
      return Aggregation.newAggregation(
              match, new CustomAggregationOperation(queryFilter), matchWithInsured, matchGaranties)
          .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
    }
    return Aggregation.newAggregation(
            match, new CustomAggregationOperation(queryFilter), matchWithInsured)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  private static void aggregationForAutomaticAnniversary(
      ParametrageCarteTP param, Date dateTraitement, Criteria criteria) {
    Date dateSouscription = Objects.requireNonNullElseGet(dateTraitement, Date::new);

    final Integer delai = param.getParametrageRenouvellement().getDelaiDeclenchementCarteTP();
    if (delai != null) {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateSouscription);
      calendar.add(Calendar.DATE, delai);
      dateSouscription = calendar.getTime();
    }
    final String dateContrat = DateUtils.formatDate(dateSouscription, DateUtils.YYYY_MM_DD);
    criteria
        .and(Constants.DATE_SOUSCRIPTION)
        .regex(String.format("%s$", StringUtils.right(dateContrat, 5)));
  }

  private static void aggregationForManualAnniversary(Date dateTraitement, Criteria criteria) {
    final LocalDate lastDay =
        dateTraitement
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .with(TemporalAdjusters.lastDayOfYear());
    criteria.and(Constants.DATE_SOUSCRIPTION).lte(lastDay.toString());
  }

  private AggregationOperation getMatchGaranties(ParametrageCarteTP param) {
    List<Criteria> criteriaLots = new ArrayList<>();

    List<Lot> lotsParam = lotDao.getListByIdsForRenewal(param.getIdLots());

    if (CollectionUtils.isNotEmpty(lotsParam)) {
      for (Lot lot : lotsParam) {
        List<Criteria> embeddedOrCriterias = new ArrayList<>();

        for (GarantieTechnique garantie : lot.getGarantieTechniques()) {
          if (garantie.getDateSuppressionLogique() == null) {
            Criteria criteria =
                Criteria.where("assures.droits.code").is(garantie.getCodeGarantie());
            criteria.and("assures.droits.codeAssureur").is(garantie.getCodeAssureur());
            embeddedOrCriterias.add(criteria);
          }
        }

        criteriaLots.add(new Criteria().orOperator(embeddedOrCriterias));
      }
    }

    if (CollectionUtils.isNotEmpty(param.getGarantieTechniques())) {
      for (GarantieTechnique garantie : param.getGarantieTechniques()) {
        Criteria criteria = Criteria.where("assures.droits.code").is(garantie.getCodeGarantie());
        criteria.and("assures.droits.codeAssureur").is(garantie.getCodeAssureur());
        criteriaLots.add(criteria);
      }
    }
    if (CollectionUtils.isNotEmpty(criteriaLots)) {
      return Aggregation.match(new Criteria().andOperator(criteriaLots.toArray(Criteria[]::new)));
    }
    return null;
  }

  @Override
  @ContinueSpan(log = "getContratByUK")
  public ContratAIV6 getContratByUK(
      final String idDeclarant, final String numero, final String numeroAdherent) {
    final Query qryContrat = new Query();
    qryContrat.addCriteria(
        Criteria.where(Constants.ID_DECLARANT).is(idDeclarant).and(Constants.NUMERO).is(numero));
    if (StringUtils.isNotBlank(numeroAdherent)) {
      qryContrat.addCriteria(Criteria.where(Constants.NUMERO_ADHERENT).is(numeroAdherent));
    }
    return template.findOne(qryContrat, ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getContratById")
  public ContratAIV6 getContratById(final String id) {
    final Criteria criteria = Criteria.where(Constants.ID).is(id);
    return this.template.findOne(
        new Query(criteria), ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getContratByIdDeclarant")
  public ContratAIV6 getContratByIdDeclarant(final String idDeclarant) {
    final Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);
    return this.template.findOne(
        new Query(criteria), ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "deleteContratById")
  public long deleteContratById(final String id) {
    final Criteria criteria = Criteria.where(Constants.ID).is(id);

    return this.template.remove(new Query(criteria), ContratAIV6.class).getDeletedCount();
  }

  @Override
  public void remove(ContratAIV6 contratAIV6) {
    template.remove(contratAIV6);
  }

  @Override
  @ContinueSpan(log = "getContratsByFileName")
  public List<ContratAIV6> getContratsByFileName(final String fileName) {
    final Criteria criteria = Criteria.where(Constants.NOM_FICHIER_ORIGINE).is(fileName);

    return this.template.find(new Query(criteria), ContratAIV6.class);
  }

  @Override
  @ContinueSpan(log = "getContratsByAmc")
  public List<ContratAIV6> getContratsByAmc(final String idDeclarant) {
    final Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);

    return this.template.find(new Query(criteria), ContratAIV6.class);
  }

  @Override
  @ContinueSpan(log = "getContratsLightByFileName")
  public List<ContratAIV6Light> getContratsLightByFileName(final String fileName) {
    final Criteria criteria = Criteria.where(Constants.NOM_FICHIER_ORIGINE).is(fileName);
    final Aggregation aggregation = this.getServicePrestationAggregation(criteria);

    return this.template
        .aggregate(aggregation, Constants.SERVICE_PRESTATION_COLLECTION, ContratAIV6Light.class)
        .getMappedResults();
  }

  @Override
  @ContinueSpan(log = "getContratsLightByAmc")
  public List<ContratAIV6Light> getContratsLightByAmc(final String idDeclarant) {
    final Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);
    final Aggregation aggregation = this.getServicePrestationAggregation(criteria);

    return this.template
        .aggregate(aggregation, Constants.SERVICE_PRESTATION_COLLECTION, ContratAIV6Light.class)
        .getMappedResults();
  }

  private Aggregation getServicePrestationAggregation(final Criteria criteria) {
    final AggregationOperation projectOperation =
        Aggregation.project()
            .andExpression(Constants.ID)
            .as("id")
            .andExpression(Constants.SERVICE_PRESTATION_ASSURES_IDENTITE_NUMERO)
            .as(Constants.SERVICE_PRESTATION_LIST_NUMERO_PERSONNE)
            .andInclude(Constants.SERVICE_PRESTATION_TRACE_ID)
            .andInclude(Constants.ID_DECLARANT)
            .andInclude(Constants.SERVICE_PRESTATION_NUMERO);
    final MatchOperation match = Aggregation.match(criteria);

    final List<AggregationOperation> aggregationOperations = new ArrayList<>();

    aggregationOperations.add(match);
    aggregationOperations.add(projectOperation);
    aggregationOperations.add(
        Aggregation.limit(beyondPropertiesService.getLongPropertyOrThrowError(BATCH_SIZE)));

    return newAggregation(aggregationOperations)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  @Override
  @ContinueSpan(log = "deleteContratsByFileName")
  public long deleteContratsByFileName(final String fileName) {
    final Criteria criteria = Criteria.where(Constants.NOM_FICHIER_ORIGINE).is(fileName);

    return this.template.remove(new Query(criteria), ContratAIV6.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteContratsByAmc")
  public long deleteContratsByAmc(final String idDeclarant) {
    final Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);

    return this.template.remove(new Query(criteria), ContratAIV6.class).getDeletedCount();
  }

  /**
   * Contruction des critères de recherche de l'adhérent
   *
   * @param idDeclarant Le déclarant (id de l'AMC)
   * @param numeroAdherent le N° de l'adhérent
   * @return le critère de recherche
   */
  @Override
  @ContinueSpan(log = "getCriteriaAdherent")
  public Criteria getCriteriaAdherent(final String idDeclarant, final String numeroAdherent) {
    final Criteria adherentCriteria = new Criteria();
    if (StringUtils.isNotBlank(idDeclarant)) {
      adherentCriteria.and(ID_DECLARANT).is(idDeclarant);
    }
    if (StringUtils.isNotBlank(numeroAdherent)) {
      adherentCriteria.and(NUMERO_ADHERENT).is(numeroAdherent);
    }
    return adherentCriteria;
  }

  /**
   * Contruction des critères de recherche de l'adhérent
   *
   * @param idDeclarant Le déclarant (id de l'AMC)
   * @param numeroAdherent le N° de l'adhérent
   * @param contractNumberList Liste des numeros contrat a retourner
   * @return le critère de recherche
   */
  @ContinueSpan(log = "getCriteriaAdherentContrat")
  public Criteria getCriteriaAdherentContrat(
      final String idDeclarant, final String numeroAdherent, List<String> contractNumberList) {
    final Criteria adherentCriteria = getCriteriaAdherent(idDeclarant, numeroAdherent);
    if (CollectionUtils.isNotEmpty(contractNumberList)) {
      adherentCriteria.and(NUMERO).in(contractNumberList);
    }
    return adherentCriteria;
  }

  /**
   * Contruction des critères de recherche de l'assuré
   *
   * @param dateNaissance Date de naissance de l'assuré
   * @param rangNaissance Rang de naissance de l'assuré
   * @return le critère de recherche
   */
  private Criteria getCriteriaAssure(final String dateNaissance, final String rangNaissance) {
    final Criteria assureCriteria =
        Criteria.where(ASSURES_IDENTITE_DATE_NAISSANCE).is(dateNaissance);
    if (rangNaissance != null) {
      assureCriteria.and(ASSURES_IDENTITE_RANG_NAISSANCE).is(rangNaissance);
    }
    return assureCriteria;
  }

  /**
   * Contruction des critères de recherche de l'assuré
   *
   * @param numeroPersonne N° de personne
   * @return le critère de recherche
   */
  @Override
  @ContinueSpan(log = "getCriteriaAssure")
  public Criteria getCriteriaAssure(final String numeroPersonne) {
    final Criteria assureCriteria = new Criteria();
    if (numeroPersonne != null) {
      assureCriteria.and(ASSURES_IDENTITE_NUMERO_PERSONNE).is(numeroPersonne);
    }
    return assureCriteria;
  }

  /**
   * Contruction des critères de recherche du NIR
   *
   * @param nir le Nir de l'assuré
   * @param debutPeriodeSoin Début de la période de soin
   * @param finPeriodeSoin Fin de la période de soin
   * @return le critère de recherche
   */
  private Criteria getCriteriaNir(
      final String nir, final String debutPeriodeSoin, final String finPeriodeSoin) {
    final Criteria nirCriteria = new Criteria();
    final String debutPeriode =
        (StringUtils.isNotBlank(debutPeriodeSoin)) ? debutPeriodeSoin : "0001-01-01";
    final String finPeriode =
        (StringUtils.isNotBlank(finPeriodeSoin)) ? finPeriodeSoin : "9999-12-31";

    if (StringUtils.isNotBlank(nir)) {
      nirCriteria.orOperator(
          Criteria.where(ASSURES_IDENTITE_NIR_CODE).is(nir),
          Criteria.where(ASSURES_IDENTITE_NIR_AFFILIATION_RO)
              .elemMatch(
                  Criteria.where(NIR_CODE)
                      .is(nir)
                      .andOperator(
                          Criteria.where(PERIODE_FIN)
                              .gte(debutPeriode)
                              .andOperator(Criteria.where(PERIODE_DEBUT).lte(finPeriode)))),
          Criteria.where(ASSURES_IDENTITE_NIR_AFFILIATION_RO)
              .elemMatch(
                  Criteria.where(NIR_CODE)
                      .is(nir)
                      .andOperator(
                          Criteria.where(PERIODE_DEBUT)
                              .lte(finPeriode)
                              .orOperator(
                                  Criteria.where(PERIODE_FIN).is(""),
                                  Criteria.where(PERIODE_FIN).exists(false),
                                  Criteria.where(PERIODE_FIN).is(null)))));
    }
    return nirCriteria;
  }

  /**
   * Contruction des critères de recherche des périodes de droits
   *
   * @param debutPeriodeSoin Début de la période de soin
   * @param finPeriodeSoin Fin de la période de soin
   * @return le critère de recherche
   */
  private Criteria getCriteriaPeriodeDroit(
      final String debutPeriodeSoin, final String finPeriodeSoin) {
    final Criteria periodeDroitCriteria = new Criteria();
    final String debutPeriode =
        (StringUtils.isNotBlank(debutPeriodeSoin)) ? debutPeriodeSoin : "0001-01-01";
    final String finPeriode =
        (StringUtils.isNotBlank(finPeriodeSoin)) ? finPeriodeSoin : "9999-12-31";
    if (StringUtils.isNotBlank(debutPeriodeSoin) || StringUtils.isNotBlank(finPeriodeSoin)) {
      periodeDroitCriteria.orOperator(
          Criteria.where(ASSURES_DROITS)
              .elemMatch(
                  Criteria.where(PERIODE_FIN)
                      .gte(debutPeriode)
                      .andOperator(Criteria.where(PERIODE_DEBUT).lte(finPeriode))),
          Criteria.where(ASSURES_DROITS)
              .elemMatch(
                  Criteria.where(PERIODE_DEBUT)
                      .lte(finPeriode)
                      .orOperator(
                          Criteria.where(PERIODE_FIN).is(""),
                          Criteria.where(PERIODE_FIN).exists(false),
                          Criteria.where(PERIODE_FIN).is(null))));
    }
    return periodeDroitCriteria;
  }

  /**
   * Contruction des critères de recherche de l'assuré
   *
   * @param nir * nir de l'assuré
   * @param dateNaissance Date de naissance de l'assuré
   * @param rangNaissance Rang de naissance de l'assuré
   * @return le critère de recherche
   */
  private Criteria getCriteriaAssure(
      final String nir, final String dateNaissance, final String rangNaissance) {
    Criteria assureCriteria =
        rangNaissance != null
            ? new Criteria()
                .andOperator(
                    Criteria.where("identite.dateNaissance").is(dateNaissance),
                    Criteria.where("identite.rangNaissance").is(rangNaissance),
                    new Criteria()
                        .orOperator(
                            Criteria.where("identite.nir.code").is(nir),
                            Criteria.where("identite.affiliationsRO.nir.code").is(nir)))
            : new Criteria()
                .andOperator(
                    Criteria.where("identite.dateNaissance").is(dateNaissance),
                    new Criteria()
                        .orOperator(
                            Criteria.where("identite.nir.code").is(nir),
                            Criteria.where("identite.affiliationsRO.nir.code").is(nir)));
    return Criteria.where(ASSURES).elemMatch(assureCriteria);
  }

  /**
   * Construction de l'aggregation permettant la recherche en fonction des différents critère
   * posssibles
   *
   * @param adherentCriteria Critères de recherche de l'adhérent
   * @param assureCriteria Critères de recherche de l'assuré
   * @param nirCriteria Critères de recherche du Nir
   * @param periodeDroitCriteria Critères de recherche des droits
   * @return l'aggregation pour la recherche des droits de l'assuré
   */
  private Aggregation getServicePrestationAgg(
      final Criteria adherentCriteria,
      final Criteria assureCriteria,
      final Criteria nirCriteria,
      final Criteria periodeDroitCriteria) {
    return Aggregation.newAggregation(
        Aggregation.match(adherentCriteria),
        Aggregation.match(assureCriteria),
        Aggregation.match(nirCriteria),
        Aggregation.unwind(ASSURES),
        Aggregation.match(assureCriteria),
        Aggregation.match(nirCriteria),
        Aggregation.match(periodeDroitCriteria));
  }

  /**
   * Construction de l'aggregation permettant la recherche en fonction des différents critère
   * posssibles
   *
   * @param adherentCriteria Critères de recherche de l'adhérent
   * @param assureCriteria Critères de recherche de l'assuré
   * @param nirCriteria Critères de recherche du Nir
   * @return l'aggregation pour la recherche des droits de l'assuré
   */
  private Aggregation getServicePrestationAgg(
      final Criteria adherentCriteria, final Criteria assureCriteria, final Criteria nirCriteria) {
    return Aggregation.newAggregation(
        Aggregation.match(
            new Criteria().andOperator(adherentCriteria, assureCriteria, nirCriteria)),
        Aggregation.unwind(ASSURES),
        Aggregation.match(new Criteria().andOperator(assureCriteria, nirCriteria)),
        Aggregation.project()
            .and(ID_DECLARANT)
            .as(ID_DECLARANT)
            .and(NUMERO)
            .as(NUMERO)
            .and(NUMERO_ADHERENT)
            .as(NUMERO_ADHERENT)
            .and(DATE_SOUSCRIPTION)
            .as(DATE_SOUSCRIPTION)
            .and(DATE_RESILIATION)
            .as(DATE_RESILIATION)
            .and("ordrePriorisation")
            .as("ordrePriorisation")
            .and("assures.rangAdministratif")
            .as("assure.rangAdministratif")
            .and(ASSURES_IDENTITE_NIR_CODE)
            .as("assure.identite.nir.code")
            .and(ASSURES_IDENTITE_DATE_NAISSANCE)
            .as("assure.identite.dateNaissance")
            .and(ASSURES_IDENTITE_RANG_NAISSANCE)
            .as("assure.identite.rangNaissance")
            .and(ASSURES_IDENTITE_NUMERO_PERSONNE)
            .as("assure.identite.numeroPersonne")
            .and(ASSURES_IDENTITE_AFFILIATIONS_RO)
            .as("assure.identite.affiliationsRO")
            .and("assures.data.destinatairesRelevePrestations")
            .as("assure.data.destinatairesRelevePrestations")
            .and("assures.data.adresse")
            .as("assure.data.adresse")
            .and("assures.data.nom")
            .as("assure.data.nom")
            .and("assures.qualite")
            .as("assure.qualite")
            .and(ASSURES_DROITS)
            .as("assure.droits")
            .andExclude("_id"));
  }

  /**
   * Recupère les droits d'un assuré
   *
   * @param agg L'aggregation permettant la recherche
   * @return les droits de l'assuré
   */
  @Override
  @ContinueSpan(log = "getServicePrestation")
  public List<ServicePrestationV6> getServicePrestation(final Aggregation agg) {
    return this.template
        .aggregate(agg, SERVICE_PRESTATION, ServicePrestationV6.class)
        .getMappedResults();
  }

  @ContinueSpan(log = "getServicePrestationRdo")
  public List<ServicePrestationsRdo> getServicePrestationRdo(final Aggregation agg) {
    return this.template
        .aggregate(agg, SERVICE_PRESTATION, ServicePrestationsRdo.class)
        .getMappedResults();
  }

  /**
   * Recupère les droits d'un assuré
   *
   * @param query La requete permettant la recherche
   * @return les droits de l'assuré
   */
  @Override
  @ContinueSpan(log = "getContratAIV6")
  public List<ContratAIV6> getContratAIV6(final Query query) {
    return this.template.find(query, ContratAIV6.class, SERVICE_PRESTATION);
  }

  @Override
  @ContinueSpan(log = "getContractsRecipientsPaginated")
  public List<ContratAIV5Recipient> getContractsRecipientsPaginated(long batchSize, int page) {
    Pageable pageable = PageRequest.of(page, (int) batchSize);
    Query query = new Query().with(pageable);

    return template.find(query, ContratAIV5Recipient.class);
  }

  /**
   * Construction de l'aggregation permettant la recherche des droits d'un assuré en V1 et V2
   *
   * @param idDeclarant N° de déclarant (AMC)
   * @param numeroAdherent N° de l'adhérent
   * @param dateNaissance Date de naissance de l'assuré
   * @param rangNaissance Rang de naissance de l'assuré
   * @param debutPeriodeSoin Début de période de soin
   * @param finPeriodeSoin Fin de période de soin
   * @param nir Nir de l'assuré (recherche sur le NIR principal et de l'affiliation RO)
   * @return aggregation mongo
   */
  private Aggregation getAggregationServicePrestation(
      final String idDeclarant,
      final String numeroAdherent,
      final String dateNaissance,
      final String rangNaissance,
      final String debutPeriodeSoin,
      final String finPeriodeSoin,
      final String nir) {
    // Contruction des critères de recherches
    final Criteria adherentCriteria = this.getCriteriaAdherent(idDeclarant, numeroAdherent);
    final Criteria assureCriteria = this.getCriteriaAssure(dateNaissance, rangNaissance);
    final Criteria nirCriteria = this.getCriteriaNir(nir, debutPeriodeSoin, finPeriodeSoin);
    final Criteria periodeDroitCriteria =
        this.getCriteriaPeriodeDroit(debutPeriodeSoin, finPeriodeSoin);

    // Construction de l'aggrégation de recherche
    return this.getServicePrestationAgg(
        adherentCriteria, assureCriteria, nirCriteria, periodeDroitCriteria);
  }

  /**
   * Construction de l'aggregation permettant la recherche des droits d'un assuré en V1 et V2
   *
   * @param dateNaissance Date de naissance de l'assuré
   * @param rangNaissance Rang de naissance de l'assuré
   * @param nir Nir de l'assuré (recherche sur le NIR principal et de l'affiliation RO)
   * @return aggregation mongo
   */
  private Query getQueryContratAIV6ForBlb(
      final String dateNaissance, final String rangNaissance, final String nir) {
    // Contruction des critères de recherches
    final Criteria assureCriteria = this.getCriteriaAssure(nir, dateNaissance, rangNaissance);
    final Criteria nirCriteria = this.getCriteriaNir(nir, null, null);

    // Construction de la requete de recherche
    return new Query().addCriteria(assureCriteria).addCriteria(nirCriteria);
  }

  @Override
  @ContinueSpan(log = "findContratAIV6")
  public List<ContratAIV6> findContratAIV6ForBlb(
      final String dateNaissance, final String rangNaissance, final String nir) {
    final Query qry = this.getQueryContratAIV6ForBlb(dateNaissance, rangNaissance, nir);

    return this.getContratAIV6(qry);
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV6")
  public List<ServicePrestationV6> findServicePrestationV6(
      final String idDeclarant,
      final String numeroAdherent,
      final String dateNaissance,
      final String rangNaissance,
      final String debutPeriodeSoin,
      final String finPeriodeSoin,
      final String nir) {

    final Aggregation agg =
        this.getAggregationServicePrestation(
            idDeclarant,
            numeroAdherent,
            dateNaissance,
            rangNaissance,
            debutPeriodeSoin,
            finPeriodeSoin,
            nir);

    final List<ServicePrestationV6> servicePrestations = this.getServicePrestation(agg);

    // Affectation dans une liste de prestationV5
    final List<ServicePrestationV6> servicePrestationsV6 = new ArrayList<>();
    if (!CollectionUtils.isEmpty(servicePrestations)) {
      servicePrestationsV6.addAll(servicePrestations);
    }

    // Suppresion des periodes sans effet
    if (!CollectionUtils.isEmpty(servicePrestationsV6)) {
      final Iterator<ServicePrestationV6> itSp = servicePrestationsV6.iterator();
      while (itSp.hasNext()) {
        final ServicePrestationV6 sp = itSp.next();
        this.supprimePeriodeSansEffet(sp);
        if (sp.getAssure().getDroits().isEmpty()) {
          itSp.remove();
        }
      }
    }

    return servicePrestationsV6;
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV6")
  public List<ContratAIV6> findServicePrestationV6(
      final String idDeclarant, final String numeroPersonne) {
    Query qry = new Query();
    qry.addCriteria(
        Criteria.where(ID_DECLARANT)
            .is(idDeclarant)
            .and(ASSURES_IDENTITE_NUMERO_PERSONNE)
            .is(numeroPersonne));
    return template.find(qry, ContratAIV6.class);
  }

  @Cacheable(
      value = "servicePrestationsRdoCache",
      key =
          "{#idDeclarant, #numeroAdherent, #dateNaissance, #rangNaissance, #nir, #contractNumberList}")
  @ContinueSpan(log = "findServicePrestationsRdo")
  public List<ServicePrestationsRdo> findServicePrestationsRdo(
      final String idDeclarant,
      final String numeroAdherent,
      final String dateNaissance,
      final String rangNaissance,
      final String nir,
      List<String> contractNumberList) {

    final Criteria adherentCriteria =
        this.getCriteriaAdherentContrat(idDeclarant, numeroAdherent, contractNumberList);
    final Criteria assureCriteria = this.getCriteriaAssure(dateNaissance, rangNaissance);
    final Criteria nirCriteria = this.getCriteriaNir(nir, null, null);

    Aggregation aggregation =
        getServicePrestationAgg(adherentCriteria, assureCriteria, nirCriteria);

    final List<ServicePrestationsRdo> servicePrestationsRdo =
        this.getServicePrestationRdo(aggregation);

    final List<ServicePrestationsRdo> servicePrestationsRdoList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(servicePrestationsRdo)) {
      servicePrestationsRdoList.addAll(servicePrestationsRdo);
    }

    return servicePrestationsRdoList;
  }

  @Override
  @ContinueSpan(log = "findServicePrestationByContractNumber")
  public ContratAIV6 findServicePrestationByContractNumber(
      final String numeroContrat, final String idDeclarant, final String subscriberId) {
    final Criteria criteria =
        Criteria.where(ID_DECLARANT)
            .is(idDeclarant)
            .and(NUMERO)
            .is(numeroContrat)
            .and(NUMERO_ADHERENT)
            .is(subscriberId);
    final Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria));

    final List<ContratAIV6> servicePrestationList =
        this.template.aggregate(agg, SERVICE_PRESTATION, ContratAIV6.class).getMappedResults();

    if (!CollectionUtils.isEmpty(servicePrestationList)) {
      return servicePrestationList.get(0);
    } else {
      return null;
    }
  }

  /**
   * Methode permettant de supprimer les périodes sans effet (date de début supérieur à la date de
   * fin) d'un ServicePrestation
   *
   * @param servicePrestationCommun contrat
   */
  private void supprimePeriodeSansEffet(final ServicePrestationCommun servicePrestationCommun) {
    this.supprimePeriodeSansEffetListePeriode(
        servicePrestationCommun.getPeriodesContratResponsableOuvert());

    if (servicePrestationCommun instanceof ServicePrestationV6 spV5) {
      this.supprimePeriodeSansEffetListePeriodeSuspension(spV5.getPeriodesSuspension());
      this.supprimePeriodeSansEffetListePeriodeContratCMUOuvert(spV5.getPeriodesContratCMUOuvert());

      final AssureCommun assure = spV5.getAssure();
      if (assure != null) {
        this.supprimePeriodeSansEffetAssure(assure);
      }
    }
  }

  /**
   * Suppression des periodes sans effet d'un assuré
   *
   * @param assure assure
   */
  private void supprimePeriodeSansEffetAssure(final AssureCommun assure) {
    if (assure.getIdentite() != null
        && !CollectionUtils.isEmpty(assure.getIdentite().getAffiliationsRO())) {
      final Iterator<NirRattachementRO> it = assure.getIdentite().getAffiliationsRO().iterator();
      while (it.hasNext()) {
        final NirRattachementRO nirRattRO = it.next();
        final Periode periode = nirRattRO.getPeriode();
        if (this.isPeriodeSansEffet(periode)) {
          it.remove();
        }
      }
    }

    this.supprimePeriodeSansEffetListePeriode(assure.getPeriodes());
    this.supprimePeriodeSansEffetListePeriode(assure.getPeriodesMedecinTraitantOuvert());
    this.supprimePeriodeSansEffetListeCodePeriode(assure.getRegimesParticuliers());
    this.supprimePeriodeSansEffetListeCodePeriode(assure.getSituationsParticulieres());
  }

  /**
   * Methode suprrimant les periodes sans effet d'une Liste de periodes
   *
   * @param periodes periodes à supprimer
   */
  private void supprimePeriodeSansEffetListePeriode(final List<Periode> periodes) {
    if (!CollectionUtils.isEmpty(periodes)) {
      periodes.removeIf(this::isPeriodeSansEffet);
    }
  }

  private void supprimePeriodeSansEffetListePeriodeSuspension(
      final List<PeriodeSuspension> periodes) {
    if (!CollectionUtils.isEmpty(periodes)) {
      final Iterator<PeriodeSuspension> it = periodes.iterator();
      while (it.hasNext()) {
        final PeriodeSuspension periodeSuspension = it.next();
        final Periode periode = periodeSuspension.getPeriode();
        if (this.isPeriodeSansEffet(periode)) {
          it.remove();
        }
      }
    }
  }

  private void supprimePeriodeSansEffetListePeriodeContratCMUOuvert(
      final List<PeriodeContratCMUOuvert> periodes) {
    if (!CollectionUtils.isEmpty(periodes)) {
      final Iterator<PeriodeContratCMUOuvert> it = periodes.iterator();
      while (it.hasNext()) {
        final PeriodeContratCMUOuvert periodeCMU = it.next();
        final Periode periode = periodeCMU.getPeriode();
        if (this.isPeriodeSansEffet(periode)) {
          it.remove();
        }
      }
    }
  }

  /**
   * Methode suprrimant les periodes sans effet d'une Liste de periodes
   *
   * @param codePeriodes periodes à supprimer
   */
  private void supprimePeriodeSansEffetListeCodePeriode(final List<CodePeriode> codePeriodes) {
    if (!CollectionUtils.isEmpty(codePeriodes)) {
      codePeriodes.removeIf(
          codePeriode -> codePeriode != null && this.isPeriodeSansEffet(codePeriode.getPeriode()));
    }
  }

  /**
   * Méthode permettant de déterminer si une période est sans effet
   *
   * @param periode periode à tester
   * @return true / false
   */
  private boolean isPeriodeSansEffet(final Periode periode) {
    return (periode != null
        && StringUtils.isNotBlank(periode.getDebut())
        && StringUtils.isNotBlank(periode.getFin())
        && periode.getDebut().compareTo(periode.getFin()) > 0);
  }

  @Override
  @ContinueSpan(log = "getContratByNumeroContractAndAmc")
  public ContratAIV6 getContratByNumeroContractAndAmc(
      final String numeroContrat, final String idDeclarant) {
    final Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(idDeclarant);
    criteria.and(Constants.NUMERO).is(numeroContrat);
    return this.template.findOne(
        new Query(criteria), ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "updateContractsExecutionId")
  public long updateContractsExecutionId(List<ObjectId> contractIdList, String batchExecutionId) {
    Criteria criteria = Criteria.where("_id").in(contractIdList);
    Update updateDefinition = new Update().set(BATCH_EXECUTION_ID, batchExecutionId);
    return this.template
        .updateMulti(new Query(criteria), updateDefinition, ContratAIV6.class)
        .getMatchedCount();
  }

  @Override
  @ContinueSpan(log = "updateContrat")
  public void updateContrat(ContratAIV6 contrat) {
    template.save(contrat, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "existContrat")
  public boolean existContrat(Query query) {
    return template.exists(query, ContratAIV6.class);
  }

  @Override
  public Stream<ContratAIV6> getAllContrats() {
    return template.stream(Query.query(new Criteria()), ContratAIV6.class);
  }

  @Override
  @ContinueSpan(log = "create servicePrestation")
  public void create(Object servicePrestation) {
    template.insert(servicePrestation, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "dropCollection servicePrestation")
  public void dropCollection() {
    final Criteria criteria = Criteria.where(Constants.ID_DECLARANT).gte("0");
    template.remove(new Query(criteria), Constants.SERVICE_PRESTATION_COLLECTION);
  }
}
