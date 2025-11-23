package com.cegedim.next.serviceeligibility.batch635.job.domain.repository;

import static com.cegedim.next.serviceeligibility.batch635.job.domain.repository.CustomContratsRepositoryImpl.FieldsDefinitionConstants.*;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.extractCivilYear;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.CONTRATS_COLLECTION;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection.PeriodeDroitProjection;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class CustomContratsRepositoryImpl implements CustomContratsRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomContratsRepositoryImpl.class);
  private static final String DATE_REGEX =
      "^[\\d]{4}\\/(0[1-9]|1[012])\\/(0[1-9]|[12][\\d]|3[01])$";
  private final MongoTemplate mongoTemplate;
  private final Integer databasePaginationLimit;

  ProjectionOperation secondStep;
  UnwindOperation thirdStep;
  ProjectionOperation fourthStep;
  UnwindOperation seventhStep;
  ProjectionOperation eighthStep;
  MatchOperation ninthStep;
  ProjectionOperation tenthStep;
  GroupOperation twelfthStep;
  ProjectionOperation fourteenthStep;
  LimitOperation lastStep;

  public CustomContratsRepositoryImpl(
      @Value("${DATABASE_PAGINATION_LIMIT:20000}") Integer databasePaginationLimit,
      MongoTemplate mongoTemplate) {
    this.databasePaginationLimit = databasePaginationLimit;
    this.mongoTemplate = mongoTemplate;

    // The first, eleventh, thirteenth and fifteenth step all depend on the input
    // parameters, therefore they can't be re-used.
    this.secondStep = getSecondStep();
    this.thirdStep = getThirdStep();
    this.fourthStep = getFourthStep();
    this.seventhStep = getSeventhStep();
    this.eighthStep = getEighthStep();
    this.ninthStep = getNinthStep();
    this.tenthStep = getTenthStep();
    this.twelfthStep = getTwelfthStep();
    this.fourteenthStep = getFourteenthStep();
    this.lastStep = getLastStep();
  }

  @Override
  @ContinueSpan(log = "extractPeriodesDroit")
  public List<PeriodeDroitProjection> extractPeriodesDroit(
      String amc, String referenceDate, int pageIndex) {
    LOGGER.info(
        "Requesting page => '{}' from the database for Amc => '{}' and Reference date => '{}'!",
        pageIndex + 1,
        amc,
        referenceDate);
    MatchOperation firstStep = getFirstStep(amc);
    MatchOperation eleventhStep = getEleventhStep(referenceDate);
    ProjectionOperation thirteenthStep = getThirteenthStep(referenceDate);
    SkipOperation fifteenthStep = getFifteenthStep(pageIndex);

    List<AggregationOperation> steps = new ArrayList<>();
    steps.addAll(List.of(firstStep, secondStep, thirdStep, fourthStep));
    steps.addAll(chainUnwind());
    steps.addAll(
        List.of(
            seventhStep,
            eighthStep,
            ninthStep,
            tenthStep,
            eleventhStep,
            twelfthStep,
            thirteenthStep,
            fourteenthStep,
            fifteenthStep,
            lastStep));

    Aggregation aggregation =
        Aggregation.newAggregation(steps)
            .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());

    AggregationResults<PeriodeDroitProjection> aggregationResult =
        mongoTemplate.aggregate(aggregation, CONTRATS_COLLECTION, PeriodeDroitProjection.class);

    return aggregationResult.getMappedResults();
  }

  /**
   * Filter contracts that correspond to the amc.
   *
   * @param amc
   * @return
   */
  private MatchOperation getFirstStep(String amc) {
    Criteria criteria = Criteria.where(ID_DECLARANT_FIELD_NAME).is(amc);
    return match(criteria);
  }

  /**
   * Project the necessary fields from the global contrats document.
   *
   * @return
   */
  private ProjectionOperation getSecondStep() {
    return project(
            BENEFICIAIRES_FIELD_NAME,
            ID_DECLARANT_FIELD_NAME,
            NUMERO_CONTRAT_FIELD_NAME,
            NUMERO_ADHERENT_FIELD_NAME)
        .andExclude(ID_FIELD_NAME);
  }

  /**
   * Unwind the beneficiaries array into objects.
   *
   * @return
   */
  private UnwindOperation getThirdStep() {
    return unwind(BENEFICIAIRES_FIELD_NAME);
  }

  /**
   * Project the necessary fields from the beneficiaries object.
   *
   * @return
   */
  private ProjectionOperation getFourthStep() {
    return project(NUMERO_CONTRAT_FIELD_NAME, NUMERO_ADHERENT_FIELD_NAME, ID_DECLARANT_FIELD_NAME)
        .and(NIR_OD1_FIELD_PATH)
        .as(NIR_OD1_FIELD_NAME)
        .and(CLE_NIR_OD1_FIELD_PATH)
        .as(CLE_NIR_OD1_FIELD_NAME)
        .and(DATE_NAISSANCE_FIELD_PATH)
        .as(DATE_NAISSANCE_FIELD_NAME)
        .and(RANG_NAISSANCE_FIELD_PATH)
        .as(RANG_NAISSANCE_FIELD_NAME)
        .and(NUMERO_PERSONNE_FIELD_PATH)
        .as(NUMERO_PERSONNE_FIELD_NAME)
        .and(NOM_FIELD_PATH)
        .as(NOM_FIELD_NAME)
        .and(PRENOM_FIELD_PATH)
        .as(PRENOM_FIELD_NAME)
        .and(DOMAINE_DROITS_FIELD_PATH)
        .as(DOMAINE_DROITS_FIELD_NAME);
  }

  private UnwindOperation getSeventhStep() {
    return unwind(PERIODES_DROIT_FIELD_NAME);
  }

  /**
   * Project the necessary fields from the periodesDroit object.
   *
   * @return
   */
  private ProjectionOperation getEighthStep() {
    return project(
            NUMERO_CONTRAT_FIELD_NAME,
            ID_DECLARANT_FIELD_NAME,
            NUMERO_ADHERENT_FIELD_NAME,
            NIR_OD1_FIELD_NAME,
            CLE_NIR_OD1_FIELD_NAME,
            DATE_NAISSANCE_FIELD_NAME,
            RANG_NAISSANCE_FIELD_NAME,
            NUMERO_PERSONNE_FIELD_NAME,
            NOM_FIELD_NAME,
            PRENOM_FIELD_NAME)
        .and(PERIODE_DEBUT_FIELD_PATH)
        .as(PERIODE_DEBUT_FIELD_NAME)
        .and(PERIODE_FIN_FIELD_PATH)
        .as(PERIODE_FIN_FIELD_NAME);
  }

  /**
   * Filter to get only the documents that have an eligible date.
   *
   * @return
   */
  private MatchOperation getNinthStep() {
    Pattern periodeRegex = Pattern.compile(DATE_REGEX);

    Criteria periodeDebutCriteria = new Criteria(PERIODE_DEBUT_FIELD_NAME).regex(periodeRegex);
    Criteria periodeFinCriteria = new Criteria(PERIODE_FIN_FIELD_NAME).regex(periodeRegex);

    Criteria finalCriteria = new Criteria();
    finalCriteria.orOperator(periodeDebutCriteria, periodeFinCriteria);

    return match(finalCriteria);
  }

  /**
   * Project the civil year from periodeDebut and periodeFin
   *
   * @return
   */
  private ProjectionOperation getTenthStep() {
    ProjectionOperation projectionOperation =
        project(
            NUMERO_CONTRAT_FIELD_NAME,
            ID_DECLARANT_FIELD_NAME,
            NUMERO_ADHERENT_FIELD_NAME,
            NIR_OD1_FIELD_NAME,
            CLE_NIR_OD1_FIELD_NAME,
            DATE_NAISSANCE_FIELD_NAME,
            RANG_NAISSANCE_FIELD_NAME,
            NUMERO_PERSONNE_FIELD_NAME,
            NOM_FIELD_NAME,
            PRENOM_FIELD_NAME,
            PERIODE_DEBUT_FIELD_NAME,
            PERIODE_FIN_FIELD_NAME);
    return projectionOperation
        .and(PERIODE_DEBUT_FIELD_NAME)
        .substring(0, 4)
        .as(CIVIL_YEAR_PERIODE_DEBUT_FIELD_NAME)
        .and(PERIODE_FIN_FIELD_NAME)
        .substring(0, 4)
        .as(CIVIL_YEAR_PERIODE_FIN_FIELD_NAME);
  }

  /**
   * Filter to get only the documents for which the reference is between the periodeDebut and
   * periodeFin
   *
   * @param referenceDate
   * @return
   */
  private MatchOperation getEleventhStep(String referenceDate) {
    String civilYear = extractCivilYear(referenceDate);

    Criteria greaterCriteria =
        Criteria.where(CIVIL_YEAR_PERIODE_DEBUT_FIELD_NAME)
            .gt(civilYear)
            .and(CIVIL_YEAR_PERIODE_FIN_FIELD_NAME)
            .gt(civilYear);

    Criteria lowerCriteria =
        Criteria.where(CIVIL_YEAR_PERIODE_DEBUT_FIELD_NAME)
            .lt(civilYear)
            .and(CIVIL_YEAR_PERIODE_FIN_FIELD_NAME)
            .lt(civilYear);

    Criteria finalCriteria = new Criteria().norOperator(greaterCriteria, lowerCriteria);

    return new MatchOperation(finalCriteria);
  }

  /**
   * group each beneficiary to get min periodeDebut and max periodeFin
   *
   * @return
   */
  private GroupOperation getTwelfthStep() {
    return group(
            NUMERO_CONTRAT_FIELD_NAME,
            NUMERO_ADHERENT_FIELD_NAME,
            NUMERO_PERSONNE_FIELD_NAME,
            ID_DECLARANT_FIELD_NAME)
        .first(NUMERO_CONTRAT_FIELD_NAME)
        .as(NUMERO_CONTRAT_FIELD_NAME)
        .first(NUMERO_ADHERENT_FIELD_NAME)
        .as(NUMERO_ADHERENT_FIELD_NAME)
        .first(NUMERO_ADHERENT_FIELD_NAME)
        .as(NUMERO_ADHERENT_FIELD_NAME)
        .first(NIR_OD1_FIELD_NAME)
        .as(NIR_OD1_FIELD_NAME)
        .first(CLE_NIR_OD1_FIELD_NAME)
        .as(CLE_NIR_OD1_FIELD_NAME)
        .first(DATE_NAISSANCE_FIELD_NAME)
        .as(DATE_NAISSANCE_FIELD_NAME)
        .first(RANG_NAISSANCE_FIELD_NAME)
        .as(RANG_NAISSANCE_FIELD_NAME)
        .first(NUMERO_PERSONNE_FIELD_NAME)
        .as(NUMERO_PERSONNE_FIELD_NAME)
        .first(NOM_FIELD_NAME)
        .as(NOM_FIELD_NAME)
        .first(PRENOM_FIELD_NAME)
        .as(PRENOM_FIELD_NAME)
        .min(PERIODE_DEBUT_FIELD_NAME)
        .as(PERIODE_DEBUT_FIELD_NAME)
        .max(PERIODE_FIN_FIELD_NAME)
        .as(PERIODE_FIN_FIELD_NAME);
  }

  /**
   * Project the periodeDebut and periodeFin and add start and end of year for further limitation
   *
   * @return
   */
  private ProjectionOperation getThirteenthStep(String referenceDate) {
    String civilYear = extractCivilYear(referenceDate);
    String startOfYear = civilYear + "/01/01";
    String endOfYear = civilYear + "/12/31";
    ProjectionOperation projectionOperation =
        project(
            NUMERO_CONTRAT_FIELD_NAME,
            ID_DECLARANT_FIELD_NAME,
            NUMERO_ADHERENT_FIELD_NAME,
            NIR_OD1_FIELD_NAME,
            CLE_NIR_OD1_FIELD_NAME,
            DATE_NAISSANCE_FIELD_NAME,
            RANG_NAISSANCE_FIELD_NAME,
            NUMERO_PERSONNE_FIELD_NAME,
            NOM_FIELD_NAME,
            PRENOM_FIELD_NAME,
            PERIODE_DEBUT_FIELD_NAME,
            PERIODE_FIN_FIELD_NAME);

    return projectionOperation
        .andExpression("[0]", startOfYear)
        .as(START_OF_YEAR_FIELD_NAME)
        .andExpression("[0]", endOfYear)
        .as(END_OF_YEAR_FIELD_NAME)
        .andExclude(ID_FIELD_NAME);
  }

  /**
   * Project the periodeDebut and periodeFin limited to requested year
   *
   * @return
   */
  private ProjectionOperation getFourteenthStep() {
    ProjectionOperation projectionOperation =
        project(
            NUMERO_CONTRAT_FIELD_NAME,
            ID_DECLARANT_FIELD_NAME,
            NUMERO_ADHERENT_FIELD_NAME,
            NIR_OD1_FIELD_NAME,
            CLE_NIR_OD1_FIELD_NAME,
            DATE_NAISSANCE_FIELD_NAME,
            RANG_NAISSANCE_FIELD_NAME,
            NUMERO_PERSONNE_FIELD_NAME,
            NOM_FIELD_NAME,
            PRENOM_FIELD_NAME);

    AggregationExpression min =
        AccumulatorOperators.Min.minOf(PERIODE_FIN_FIELD_NAME).and(END_OF_YEAR_FIELD_NAME);
    AggregationExpression max =
        AccumulatorOperators.Max.maxOf(PERIODE_DEBUT_FIELD_NAME).and(START_OF_YEAR_FIELD_NAME);

    return projectionOperation
        .and(max)
        .as(PERIODE_DEBUT_FIELD_NAME)
        .and(min)
        .as(PERIODE_FIN_FIELD_NAME);
  }

  /**
   * Apply an offset to skip the documents already read
   *
   * @param pageIndex
   * @return
   */
  private SkipOperation getFifteenthStep(int pageIndex) {
    return skip((long) pageIndex * databasePaginationLimit);
  }

  /**
   * Limit the result
   *
   * @return
   */
  private LimitOperation getLastStep() {
    return limit(databasePaginationLimit);
  }

  private List<AggregationOperation> chainUnwind() {
    List<AggregationOperation> aggregationOperations = new ArrayList<>();
    aggregationOperations.addAll(baseUnwindProject("garanties", "domaineDroits"));
    aggregationOperations.addAll(baseUnwindProject("produits", "garanties"));
    aggregationOperations.addAll(baseUnwindProject("referencesCouverture", "produits"));
    aggregationOperations.addAll(baseUnwindProject("naturesPrestation", "referencesCouverture"));
    aggregationOperations.addAll(baseUnwindProject("periodesDroit", "naturesPrestation"));

    return aggregationOperations;
  }

  private List<AggregationOperation> baseUnwindProject(String fieldName, String path) {
    UnwindOperation unwind = unwind(path);
    ProjectionOperation project =
        project(
                NUMERO_CONTRAT_FIELD_NAME,
                ID_DECLARANT_FIELD_NAME,
                NUMERO_ADHERENT_FIELD_NAME,
                NIR_OD1_FIELD_NAME,
                CLE_NIR_OD1_FIELD_NAME,
                DATE_NAISSANCE_FIELD_NAME,
                RANG_NAISSANCE_FIELD_NAME,
                NUMERO_PERSONNE_FIELD_NAME,
                NOM_FIELD_NAME,
                PRENOM_FIELD_NAME)
            .and(path + "." + fieldName)
            .as(fieldName);

    return List.of(unwind, project);
  }

  public static class FieldsDefinitionConstants {
    private FieldsDefinitionConstants() {
      throw new IllegalStateException("Utility class");
    }

    // Contrats collection fields.
    public static final String ID_FIELD_NAME = "_id";
    public static final String ID_DECLARANT_FIELD_NAME = "idDeclarant";
    public static final String NUMERO_CONTRAT_FIELD_NAME = "numeroContrat";
    public static final String NUMERO_ADHERENT_FIELD_NAME = "numeroAdherent";
    public static final String BENEFICIAIRES_FIELD_NAME = "beneficiaires";

    public static final String NIR_OD1_FIELD_NAME = "nirOd1";
    public static final String NIR_OD1_FIELD_PATH = "beneficiaires.nirOd1";

    public static final String CLE_NIR_OD1_FIELD_NAME = "cleNirOd1";
    public static final String CLE_NIR_OD1_FIELD_PATH = "beneficiaires.cleNirOd1";

    public static final String DATE_NAISSANCE_FIELD_NAME = "dateNaissance";
    public static final String DATE_NAISSANCE_FIELD_PATH = "beneficiaires.dateNaissance";

    public static final String RANG_NAISSANCE_FIELD_NAME = "rangNaissance";
    public static final String RANG_NAISSANCE_FIELD_PATH = "beneficiaires.rangNaissance";

    public static final String NUMERO_PERSONNE_FIELD_NAME = "numeroPersonne";
    public static final String NUMERO_PERSONNE_FIELD_PATH = "beneficiaires.numeroPersonne";

    public static final String NOM_FIELD_NAME = "nom";
    public static final String NOM_FIELD_PATH = "beneficiaires.affiliation.nom";

    public static final String PRENOM_FIELD_NAME = "prenom";
    public static final String PRENOM_FIELD_PATH = "beneficiaires.affiliation.prenom";

    public static final String PERIODES_DROIT_FIELD_NAME = "periodesDroit";
    public static final String PERIODES_DROIT_FIELD_PATH =
        "domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit";

    public static final String PERIODE_DEBUT_FIELD_NAME = "periodeDebut";
    public static final String PERIODE_DEBUT_FIELD_PATH = "periodesDroit.periodeDebut";

    public static final String PERIODE_FIN_FIELD_NAME = "periodeFin";
    public static final String PERIODE_FIN_FIELD_PATH = "periodesDroit.periodeFin";

    public static final String DOMAINE_DROITS_FIELD_NAME = "domaineDroits";
    public static final String DOMAINE_DROITS_FIELD_PATH = "beneficiaires.domaineDroits";

    public static final String CIVIL_YEAR_PERIODE_DEBUT_FIELD_NAME = "civilYearPeriodeDebut";
    public static final String CIVIL_YEAR_PERIODE_FIN_FIELD_NAME = "civilYearPeriodeFin";

    public static final String START_OF_YEAR_FIELD_NAME = "startOfYear";
    public static final String END_OF_YEAR_FIELD_NAME = "endOfYear";
  }
}
