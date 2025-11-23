package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.SerializationUtils.serializeToJsonSafely;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.UniqueAccessPointUtil;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequestV5;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/** Classe d'accès aux {@code Declaration} de la base de donnees. */
@Repository("contractBackendDao")
public class ContractBackendDaoImpl extends MongoGenericWithManagementScopeDao<ContractTP>
    implements ContractBackendDao {

  public static final String ID_DECLARANT = "idDeclarant";

  public static final String NUM_AMC_ECHANGE = "numAMCEchange";

  public static final String NUMERO_PERSONNE = "numeroPersonne";

  public static final String NUMERO_ADHERANT = "numeroAdherent";
  public static final String NUMERO_CONTRAT = "numero";
  public static final String SERVICE_PRESTATION = "servicePrestation";

  public static final String ASSURES = "assures";
  public static final String DATE_MODIFICATION = "dateModification";
  public static final String SERVICE_PRESTATION_ASSURE_NUMERO_PERSONNE = "identite.numeroPersonne";

  public static final String DATE_RESTITUTION = "dateRestitution";

  public static final String PERIODES = "periodes";
  public static final String PERIODE_DEBUT = "periodeDebut";
  public static final String TYPE_PERIODE = "typePeriode";
  public static final String PERIODE_DEBUT_HTP = "periode.debut";
  public static final String PERIODE_FIN = "periodeFin";
  public static final String PERIODE_FIN_HTP = "periode.fin";
  public static final String PERIODE_FIN_FERMETURE = "periodeFinFermeture";
  public static final String DOMAINE_DROITS_PERIODES_DROIT =
      "domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit";
  public static final String DATE_RADIATION = "dateRadiation";
  public static final String DATE_RESILIATION = "dateResiliation";
  public static final String DEBUT = "debut";
  public static final String FIN = "fin";
  public static final String EMPTY = "";
  public static final String GESTIONNAIRE = "gestionnaire";
  public static final String SOCIETE_EMETTRICE = "societeEmettrice";

  private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, DATE_MODIFICATION);
  private final Logger logger = LoggerFactory.getLogger(ContractBackendDaoImpl.class);

  public ContractBackendDaoImpl(
      AuthorizationScopeHandler authorizationScopeHandler, MongoTemplate mongoTemplate) {
    super(mongoTemplate, authorizationScopeHandler);
  }

  private static Criteria getCriteriaDateExistsOrHigher(final String field, final String date) {
    return new Criteria()
        .orOperator(
            Criteria.where(field).is(EMPTY),
            Criteria.where(field).exists(false),
            Criteria.where(field).is(null),
            Criteria.where(field).gte(date));
  }

  private static Criteria getCriteriaHasGoodPeriode(final String startDate, final String endDate) {
    if (endDate == null) {
      return Criteria.where(PERIODES)
          .elemMatch(
              new Criteria()
                  .orOperator(
                      Criteria.where(FIN).is(EMPTY),
                      Criteria.where(FIN).exists(false),
                      Criteria.where(FIN).is(null),
                      Criteria.where(FIN).gte(startDate),
                      Criteria.where(DEBUT).gte(startDate)));
    }
    return Criteria.where(PERIODES)
        .elemMatch(
            new Criteria(DEBUT)
                .lte(endDate)
                .orOperator(
                    Criteria.where(FIN).is(EMPTY),
                    Criteria.where(FIN).exists(false),
                    Criteria.where(FIN).is(null),
                    Criteria.where(FIN).gte(startDate)));
  }

  private Criteria getInitialCriteria(boolean amcEchange, String insurerId) {
    final Criteria criteria = new Criteria();
    var insurer = insurerId != null ? insurerId.trim() : null;
    if (insurer != null) {
      if (amcEchange) {
        criteria.and(NUM_AMC_ECHANGE).in(insurer);
      } else {
        criteria.and(ID_DECLARANT).in(insurer);
      }
    }
    return criteria;
  }

  private Criteria getInitialCriteriaAuthorized(boolean amcEchange, String insurerId) {
    final Criteria criteria = new Criteria();
    var insurer = insurerId != null ? insurerId.trim() : null;
    if (insurer != null) {
      if (amcEchange) {
        criteria.and(NUM_AMC_ECHANGE).is(insurer);
      } else {
        applyIssuingCompanyFilter(insurer, criteria, ID_DECLARANT, CLIENT_TYPE_OTP);
        applyIssuingCompanyFilter(insurer, criteria, ID_DECLARANT, CLIENT_TYPE_INSURER, false);
      }
    }
    return criteria;
  }

  private Criteria createCriteriaTPOffline(
      final boolean amcEchange,
      final String startDate,
      final String endDate,
      final String insurerId,
      final Collection<String> numeroPersonnes,
      final String issuingCompanyCode,
      final String clientType,
      final Boolean isForced) {
    final Criteria criteria = getInitialCriteria(amcEchange, insurerId);
    applyIssuingCompanyFilter(issuingCompanyCode, criteria, GESTIONNAIRE, CLIENT_TYPE_INSURER);

    final Criteria dateRestitDoesntExists = Criteria.where(DATE_RESTITUTION).exists(false);
    final Criteria nullDate = Criteria.where(DATE_RESTITUTION).is(null);
    final Criteria criteriaBenef = new Criteria();
    if (isForced) {
      criteria.orOperator(dateRestitDoesntExists, nullDate);
    } else {
      criteriaBenef
          .and(DOMAINE_DROITS_PERIODES_DROIT)
          .elemMatch(this.getDatesCheck(startDate, endDate, false, clientType));
      criteria.orOperator(
          dateRestitDoesntExists, Criteria.where(DATE_RESTITUTION).gte(startDate), nullDate);
    }

    if (!CollectionUtils.isEmpty(numeroPersonnes)) {
      final Criteria numeroPersonneCriteria = getCriteriaPersonne(numeroPersonnes, false);
      criteriaBenef.andOperator(numeroPersonneCriteria);
    }

    criteria.and(Constants.BENEFICIAIRE_COLLECTION_NAME).elemMatch(criteriaBenef);

    return criteria;
  }

  private Criteria createCriteriaTPOnline(
      final boolean amcEchange,
      final String startDate,
      final String endDate,
      final String insurerId,
      final Collection<String> numeroPersonnes,
      final String issuingCompanyCode,
      String clientType) {
    final Criteria criteria = getInitialCriteriaAuthorized(amcEchange, insurerId);
    applyIssuingCompanyFilter(issuingCompanyCode, criteria, GESTIONNAIRE, CLIENT_TYPE_INSURER);

    final Criteria criteriaBenef = new Criteria();

    final Criteria datesCheck = this.getDatesCheck(startDate, endDate, true, clientType);

    final Criteria numeroPersonneCriteria = Criteria.where(NUMERO_PERSONNE).in(numeroPersonnes);
    final Criteria periodeOnlineCriteria =
        Criteria.where(DOMAINE_DROITS_PERIODES_DROIT).elemMatch(datesCheck);
    criteriaBenef.andOperator(numeroPersonneCriteria, periodeOnlineCriteria);

    criteria.and(Constants.BENEFICIAIRE_COLLECTION_NAME).elemMatch(criteriaBenef);

    return criteria;
  }

  private Criteria createCriteriaTPOnlineForced(
      final boolean amcEchange,
      final String insurerId,
      final Collection<String> numeroPersonnes,
      final String issuingCompanyCode) {
    final Criteria criteria = getInitialCriteria(amcEchange, insurerId);
    if (StringUtils.isNotBlank(issuingCompanyCode)) {
      criteria.and(GESTIONNAIRE).is(issuingCompanyCode);
    }
    criteria
        .and(Constants.BENEFICIAIRE_COLLECTION_NAME)
        .elemMatch(Criteria.where(NUMERO_PERSONNE).in(numeroPersonnes));
    return criteria;
  }

  private Criteria createCriteriaHTP(
      final String startDate,
      final String endDate,
      final String insurerId,
      final String subscriberId,
      final String contractNumber,
      final Collection<String> numerosPersonnes,
      final String issuingCompanyCode) {
    final Criteria criteria = new Criteria();

    applyIssuingCompanyFilter(issuingCompanyCode, criteria, SOCIETE_EMETTRICE, CLIENT_TYPE_INSURER);
    applyIssuingCompanyFilter(insurerId.trim(), criteria, ID_DECLARANT, CLIENT_TYPE_OTP);
    applyIssuingCompanyFilter(insurerId.trim(), criteria, ID_DECLARANT, CLIENT_TYPE_INSURER, false);

    if (StringUtils.isNotBlank(subscriberId)) {
      criteria.and(NUMERO_ADHERANT).is(subscriberId.trim());
    }

    if (StringUtils.isNotEmpty(contractNumber)) {
      criteria.and(NUMERO_CONTRAT).is(contractNumber);
    }

    final Criteria criteriaBenef = new Criteria();
    final Criteria datesCheck = this.getDatesCheckHTP(startDate, endDate);
    criteriaBenef.and("droits").elemMatch(datesCheck);

    final Criteria numeroPersonneCriteria = getCriteriaPersonne(numerosPersonnes, true);

    // BLUE-5002
    final Criteria radiation = getCriteriaDateExistsOrHigher(DATE_RADIATION, startDate);
    final Criteria resiliation = getCriteriaDateExistsOrHigher(DATE_RESILIATION, startDate);
    criteria.andOperator(resiliation);
    // fin BLUE-5002

    // BLUE-5383
    final Criteria periodesAssure = getCriteriaHasGoodPeriode(startDate, endDate);
    // fin BLUE-5383

    criteriaBenef.andOperator(numeroPersonneCriteria, radiation, periodesAssure);

    criteria.and(ASSURES).elemMatch(criteriaBenef);

    return criteria;
  }

  private static @NotNull Criteria getCriteriaPersonne(
      Collection<String> numerosPersonnes, boolean htp) {
    if (htp) {
      return Criteria.where(SERVICE_PRESTATION_ASSURE_NUMERO_PERSONNE).in(numerosPersonnes);
    } else {
      return Criteria.where(NUMERO_PERSONNE).in(numerosPersonnes);
    }
  }

  private Criteria createCriteriaHTPForced(
      final String insurerId,
      final String subscriberId,
      final String contractNumber,
      final Collection<String> numerosPersonnes,
      final String issuingCompanyCode) {
    final Criteria criteria = new Criteria();
    criteria.and(ID_DECLARANT).in(insurerId.trim());

    if (StringUtils.isNotBlank(subscriberId)) {
      criteria.and(NUMERO_ADHERANT).is(subscriberId.trim());
    }

    if (StringUtils.isNotEmpty(contractNumber)) {
      criteria.and(NUMERO_CONTRAT).is(contractNumber);
    }

    final Criteria numeroPersonneCriteria = getCriteriaPersonne(numerosPersonnes, true);

    if (StringUtils.isNotBlank(issuingCompanyCode)) {
      criteria.and("societeEmettrice").is(issuingCompanyCode);
    }

    criteria.and(ASSURES).elemMatch(numeroPersonneCriteria);
    return criteria;
  }

  private Criteria getDatesCheck(
      final String startDate, final String endDate, final boolean isTpOnline, String clientType) {
    Criteria datesCheck = new Criteria();
    if (CLIENT_TYPE_INSURER.equals(clientType)) {
      if (isTpOnline) {
        if (endDate == null) {
          datesCheck.orOperator(
              Criteria.where(PERIODE_FIN).exists(false),
              Criteria.where(PERIODE_FIN).gte(startDate));
        } else {
          datesCheck.orOperator(
              Criteria.where(PERIODE_DEBUT).lte(endDate),
              Criteria.where(PERIODE_FIN).gte(startDate));
        }
        datesCheck.andOperator(Criteria.where(TYPE_PERIODE).is("ONLINE"));
      } else {
        if (endDate == null) {
          datesCheck.orOperator(
              Criteria.where(PERIODE_FIN_FERMETURE).gte(startDate),
              Criteria.where(PERIODE_FIN).gte(startDate));
        } else {
          datesCheck.orOperator(
              Criteria.where(PERIODE_DEBUT).lte(endDate),
              Criteria.where(PERIODE_FIN_FERMETURE).gte(startDate),
              Criteria.where(PERIODE_FIN).gte(startDate));
        }
        datesCheck.andOperator(Criteria.where(TYPE_PERIODE).is("OFFLINE"));
      }
    } else {
      if (isTpOnline) {
        if (endDate == null) {
          datesCheck.orOperator(
              Criteria.where(PERIODE_FIN).exists(false),
              Criteria.where(PERIODE_FIN).gte(startDate));
        } else {
          datesCheck.orOperator(
              Criteria.where(PERIODE_DEBUT).lte(endDate),
              Criteria.where(PERIODE_FIN).gte(startDate));
        }
      } else {
        if (endDate == null) {
          datesCheck.orOperator(
              Criteria.where(PERIODE_FIN_FERMETURE).gte(startDate),
              Criteria.where(PERIODE_FIN).gte(startDate));
        } else {
          datesCheck.orOperator(
              Criteria.where(PERIODE_DEBUT).lte(endDate),
              Criteria.where(PERIODE_FIN_FERMETURE).gte(startDate),
              Criteria.where(PERIODE_FIN).gte(startDate));
        }
      }
    }
    return datesCheck;
  }

  private Criteria getDatesCheckHTP(final String startDate, final String endDate) {
    if (endDate == null) {
      return new Criteria()
          .orOperator(
              Criteria.where(PERIODE_FIN_HTP).is(EMPTY),
              Criteria.where(PERIODE_FIN_HTP).exists(false),
              Criteria.where(PERIODE_FIN_HTP).is(null),
              Criteria.where(PERIODE_FIN_HTP).gte(startDate),
              Criteria.where(PERIODE_DEBUT_HTP).gte(startDate));
    }
    return Criteria.where(PERIODE_DEBUT_HTP)
        .lte(endDate)
        .orOperator(
            Criteria.where(PERIODE_FIN_HTP).is(EMPTY),
            Criteria.where(PERIODE_FIN_HTP).exists(false),
            Criteria.where(PERIODE_FIN_HTP).is(null),
            Criteria.where(PERIODE_FIN_HTP).gte(startDate));
  }

  @Override
  @ContinueSpan(log = "findContractsForTPOnline")
  public List<ContractTP> findContractsForTPOnline(
      final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5) {
    final UniqueAccessPointRequestV5 requete = uniqueAccessPointTPRequestV5.getRequest();
    final Criteria criteria;
    if (requete.getIsForced()) {
      criteria =
          this.createCriteriaTPOnlineForced(
              uniqueAccessPointTPRequestV5.isFoundByNumAMCEchange(),
              requete.getInsurerId(),
              uniqueAccessPointTPRequestV5.getNumeroPersonnes(),
              requete.getIssuingCompanyCode());
    } else {
      criteria =
          this.createCriteriaTPOnline(
              uniqueAccessPointTPRequestV5.isFoundByNumAMCEchange(),
              requete.getStartDateForMongoContract(),
              requete.getEndDateForMongoContract(),
              requete.getInsurerId(),
              uniqueAccessPointTPRequestV5.getNumeroPersonnes(),
              requete.getIssuingCompanyCode(),
              requete.getClientType());
    }

    return this.getMongoTemplate()
        .find(
            query(criteria).with(DEFAULT_SORT),
            ContractTP.class,
            Constants.CONTRATS_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "findContractsForTPOffline")
  public List<ContractTP> findContractsForTPOffline(
      final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5) {
    final UniqueAccessPointRequestV5 requete = uniqueAccessPointTPRequestV5.getRequest();
    final var criteria =
        this.createCriteriaTPOffline(
            uniqueAccessPointTPRequestV5.isFoundByNumAMCEchange(),
            requete.getStartDateForMongoContract(),
            requete.getEndDateForMongoContract(),
            requete.getInsurerId(),
            // en V5, on cherche le numéro d'adhérent différement
            uniqueAccessPointTPRequestV5.getNumeroPersonnes(),
            requete.getIssuingCompanyCode(),
            requete.getClientType(),
            requete.getIsForced());

    return this.getMongoTemplate()
        .find(
            query(criteria).with(DEFAULT_SORT),
            ContractTP.class,
            Constants.CONTRATS_COLLECTION_NAME);
  }

  private List<ContratAIV6> getContratAIV5s(
      final UniqueAccessPointRequest request, final List<BenefAIV5> benefs, final String version) {
    final var numerosPersonnes = UniqueAccessPointUtil.extractNumPersonnes(benefs);

    final String issuingCompanyCode =
        (request instanceof UniqueAccessPointRequestV5)
            ? ((UniqueAccessPointRequestV5) request).getIssuingCompanyCode()
            : null;
    final boolean isForced =
        (request instanceof UniqueAccessPointRequestV5)
            ? ((UniqueAccessPointRequestV5) request).getIsForced()
            : false;
    final Criteria criteria;
    if (isForced) {
      criteria =
          this.createCriteriaHTPForced(
              request.getInsurerId(),
              request.getSubscriberId(),
              request.getContractNumber(),
              numerosPersonnes,
              issuingCompanyCode);
    } else {
      criteria =
          this.createCriteriaHTP(
              request.getStartDate(),
              request.getEndDate(),
              request.getInsurerId(),
              request.getSubscriberId(),
              request.getContractNumber(),
              numerosPersonnes,
              issuingCompanyCode);
    }
    logger.debug(
        "PAU {}, récupération des serviceprestation pour restitution des droits HTP. Execution de la query => {}",
        version,
        serializeToJsonSafely(criteria.getCriteriaObject()));

    return this.getMongoTemplate()
        .find(query(criteria).with(DEFAULT_SORT), ContratAIV6.class, SERVICE_PRESTATION);
  }

  @Override
  @ContinueSpan(log = "findContractsForHTP")
  public List<ContratAIV6> findContractsForHTP(
      final UniqueAccessPointRequestV5 requestV5, final List<BenefAIV5> benefs) {
    return this.getContratAIV5s(requestV5, benefs, "V5");
  }

  @Override
  @ContinueSpan(log = "findPastContractsForOS")
  public List<ContractTP> findPastContractsForOS(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest, boolean isTpOnline) {
    Criteria criteria = getCriteria(uniqueAccessPointTPRequest, isTpOnline, false);
    return this.getMongoTemplate()
        .find(query(criteria), ContractTP.class, Constants.CONTRATS_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "findFuturContractsForOS")
  public List<ContractTP> findFuturContractsForOS(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest, boolean isTpOnline) {
    Criteria criteriaPasse = getCriteria(uniqueAccessPointTPRequest, isTpOnline, true);
    return this.getMongoTemplate()
        .find(query(criteriaPasse), ContractTP.class, Constants.CONTRATS_COLLECTION_NAME);
  }

  private Criteria getCriteria(
      UniqueAccessPointTPRequest uniqueAccessPointTPRequest, boolean isTpOnline, boolean isFutur) {
    Criteria criteria;
    if (isTpOnline) {
      criteria =
          this.createCriteriaTPOnlineToFindOS(
              uniqueAccessPointTPRequest.isFoundByNumAMCEchange(),
              uniqueAccessPointTPRequest.getRequest().getStartDateForMongoContract(),
              uniqueAccessPointTPRequest.getRequest().getInsurerId(),
              uniqueAccessPointTPRequest.getNumeroPersonnes(),
              isFutur);
    } else {
      criteria =
          this.createCriteriaOfflineToFindOS(
              uniqueAccessPointTPRequest.isFoundByNumAMCEchange(),
              uniqueAccessPointTPRequest.getRequest().getStartDateForMongoContract(),
              uniqueAccessPointTPRequest.getRequest().getInsurerId(),
              // en V5, on cherche le numéro d'adhérent différement
              uniqueAccessPointTPRequest.getNumeroPersonnes(),
              isFutur);
    }
    return criteria;
  }

  private Criteria createCriteriaTPOnlineToFindOS(
      final boolean amcEchange,
      final String startDate,
      final String insurerId,
      final List<String> numeroPersonnes,
      boolean isFutur) {
    final Criteria criteria = getInitialCriteria(amcEchange, insurerId);

    final Criteria criteriaBenef = new Criteria();
    Criteria datesCheck;

    if (Boolean.TRUE.equals(isFutur)) {
      datesCheck = this.getDatesFutur(startDate);
    } else {
      datesCheck = this.getDatesPasseOnline(startDate);
    }

    final Criteria numeroPersonneCriteria = getCriteriaPersonne(numeroPersonnes, false);
    final Criteria periodeOnlineCriteria =
        Criteria.where(DOMAINE_DROITS_PERIODES_DROIT).elemMatch(datesCheck);
    criteriaBenef.andOperator(numeroPersonneCriteria, periodeOnlineCriteria);

    criteria.and(Constants.BENEFICIAIRE_COLLECTION_NAME).elemMatch(criteriaBenef);
    return criteria;
  }

  private Criteria createCriteriaOfflineToFindOS(
      final boolean amcEchange,
      final String startDate,
      final String insurerId,
      final Collection<String> numeroPersonnes,
      boolean isFutur) {
    final Criteria criteria = getInitialCriteria(amcEchange, insurerId);

    final Criteria criteriaBenef = new Criteria();

    Criteria datesCheck;

    if (Boolean.TRUE.equals(isFutur)) {
      datesCheck = this.getDatesFutur(startDate);
    } else {
      datesCheck = this.getDatesPasseOffline(startDate);
    }

    criteriaBenef.and(DOMAINE_DROITS_PERIODES_DROIT).elemMatch(datesCheck);

    final Criteria dateRestitDoesntExists = Criteria.where(DATE_RESTITUTION).exists(false);
    final Criteria nullDate = Criteria.where(DATE_RESTITUTION).is(null);
    final Criteria dateRestitGteStart = Criteria.where(DATE_RESTITUTION).gte(startDate);
    criteria.orOperator(dateRestitDoesntExists, dateRestitGteStart, nullDate);

    if (!CollectionUtils.isEmpty(numeroPersonnes)) {
      final Criteria numeroPersonneCriteria = getCriteriaPersonne(numeroPersonnes, false);
      criteriaBenef.andOperator(numeroPersonneCriteria);
    }

    criteria.and(Constants.BENEFICIAIRE_COLLECTION_NAME).elemMatch(criteriaBenef);
    return criteria;
  }

  private Criteria getDatesFutur(final String startDate) {
    return Criteria.where(PERIODE_DEBUT).gte(startDate.replace("-", "/"));
  }

  private Criteria getDatesPasseOnline(final String startDate) {
    return Criteria.where(PERIODE_FIN).lte(startDate.replace("-", "/"));
  }

  private Criteria getDatesPasseOffline(final String startDate) {
    return new Criteria()
        .orOperator(
            Criteria.where(PERIODE_FIN).lte(startDate.replace("-", "/")),
            Criteria.where(PERIODE_FIN_FERMETURE).lte(startDate.replace("-", "/")));
  }
}
