package com.cegedim.next.serviceeligibility.core.business.contrat.dao;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.Constants;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.MongoGenericWithManagementScopeDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository(Constants.CONTRAT_DAO)
public class ContratDaoImpl extends MongoGenericWithManagementScopeDao<ContractTP>
    implements ContratDao {

  private final DeclarantDao declarantDao;

  private static final String[] QUALIFICATION_CONTRAT_CARTE_FAMILLE = {"B", "C"};

  public ContratDaoImpl(
      AuthorizationScopeHandler authorizationScopeHandler, MongoTemplate mongoTemplate) {
    super(mongoTemplate, authorizationScopeHandler);
    this.declarantDao = new DeclarantDaoImpl(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findById Contract")
  public ContractTP findById(final String id) {
    return this.findById(id, ContractTP.class);
  }

  @Override
  @ContinueSpan(log = "findAll Contract")
  public List<ContractTP> findAll() {
    return this.findAll(ContractTP.class);
  }

  @Override
  @ContinueSpan(log = "findContractsTPByBeneficiary Contract")
  public List<ContractTP> findContractsTPByBeneficiary(
      final String dateNaissance,
      final String rangNaissance,
      final String nirBeneficiaire,
      final String cleNirBeneficiare,
      final String numAMC,
      final boolean isRechercheCarteFamille,
      final boolean isSearchByIdPrefectoral,
      final boolean isSearchByAdherent,
      final String numeroAdherent) {

    // CRITERIA
    final Criteria criteria =
        Criteria.where("beneficiaires.dateNaissance")
            .is(dateNaissance)
            .and("beneficiaires.rangNaissance")
            .is(rangNaissance);

    if (isSearchByAdherent) {
      criteria.and(NUMERO_ADHERENT).is(numeroAdherent);
    } else {
      criteria.orOperator(
          Criteria.where("beneficiaires.nirBeneficiaire")
              .is(nirBeneficiaire)
              .and("beneficiaires.cleNirBeneficiaire")
              .is(cleNirBeneficiare),
          Criteria.where("beneficiaires.nirOd1")
              .is(nirBeneficiaire)
              .and("beneficiaires.cleNirOd1")
              .is(cleNirBeneficiare),
          Criteria.where("beneficiaires.nirOd2")
              .is(nirBeneficiaire)
              .and("beneficiaires.cleNirOd2")
              .is(cleNirBeneficiare));
    }

    Criteria contractCriteria = new Criteria();

    applyIssuingCompanyFilter(null, criteria, GESTIONNAIRE, CLIENT_TYPE_INSURER);
    addAmcCriteria(numAMC, isSearchByIdPrefectoral, contractCriteria);

    if (isRechercheCarteFamille) {
      contractCriteria =
          contractCriteria.andOperator(
              Criteria.where("qualification")
                  .in(Arrays.asList(QUALIFICATION_CONTRAT_CARTE_FAMILLE)));
    }
    // QUERY

    contractCriteria.andOperator(criteria);

    final Aggregation agg =
        Aggregation.newAggregation(
            Aggregation.match(contractCriteria),
            Aggregation.unwind("beneficiaires"),
            Aggregation.match(criteria),
            this.getGroupContrat());

    return this.getMongoTemplate()
        .aggregate(
            agg,
            com.cegedim.next.serviceeligibility.core.utils.Constants.CONTRATS_COLLECTION_NAME,
            ContractTP.class)
        .getMappedResults();
  }

  @Override
  @ContinueSpan(log = "findContractsForCarteFamille Contract")
  public List<ContractTP> findContractsForCarteFamille(
      final String dateNaissance,
      final String rangNaissance,
      final String nirBeneficiaire,
      final String cleNirBeneficiare,
      final String numAMC,
      final boolean isRechercheCarteFamille,
      final boolean isSearchByIdPrefectoral,
      final boolean isSearchByAdherent,
      final String numeroAdherent) {

    // CRITERIA
    final Criteria criteria =
        Criteria.where("beneficiaires.dateNaissance")
            .is(dateNaissance)
            .and("beneficiaires.rangNaissance")
            .is(rangNaissance);

    if (isSearchByAdherent) {
      criteria.and(NUMERO_ADHERENT).is(numeroAdherent);

    } else {
      criteria.orOperator(
          Criteria.where("beneficiaires.nirBeneficiaire")
              .is(nirBeneficiaire)
              .and("beneficiaires.cleNirBeneficiaire")
              .is(cleNirBeneficiare),
          Criteria.where("beneficiaires.nirOd1")
              .is(nirBeneficiaire)
              .and("beneficiaires.cleNirOd1")
              .is(cleNirBeneficiare),
          Criteria.where("beneficiaires.nirOd2")
              .is(nirBeneficiaire)
              .and("beneficiaires.cleNirOd2")
              .is(cleNirBeneficiare));
    }

    Criteria contractCriteria = new Criteria();

    applyIssuingCompanyFilter(null, criteria, GESTIONNAIRE, CLIENT_TYPE_INSURER);
    addAmcCriteria(numAMC, isSearchByIdPrefectoral, contractCriteria);

    if (isRechercheCarteFamille) {
      contractCriteria.and("qualification").in(Arrays.asList(QUALIFICATION_CONTRAT_CARTE_FAMILLE));
    }

    contractCriteria.andOperator(criteria);

    return this.getMongoTemplate().find(Query.query(contractCriteria), ContractTP.class);
  }

  private void addAmcCriteria(
      String numAMC, boolean isSearchByIdPrefectoral, Criteria contractCriteria) {
    if (isSearchByIdPrefectoral) {
      applyIssuingCompanyFilter(numAMC, contractCriteria, ID_DECLARANT, CLIENT_TYPE_OTP);
      applyIssuingCompanyFilter(numAMC, contractCriteria, ID_DECLARANT, CLIENT_TYPE_INSURER, false);
    } else {
      final Declarant decl = this.declarantDao.findByAmcEchange(numAMC);
      applyIssuingCompanyFilter(decl.get_id(), contractCriteria, ID_DECLARANT, CLIENT_TYPE_OTP);
      applyIssuingCompanyFilter(
          decl.get_id(), contractCriteria, ID_DECLARANT, CLIENT_TYPE_INSURER, false);
      contractCriteria.and(NUMERO_AMC_ECHANGE).is(numAMC);
    }
  }

  private GroupOperation getGroupContrat() {
    return Aggregation.group("_id")
        .push("beneficiaires")
        .as("beneficiaires")
        .first("idDeclarant")
        .as("idDeclarant")
        .first("numeroContrat")
        .as("numeroContrat")
        .first("numeroAdherent")
        .as("numeroAdherent")
        .first("codeOc")
        .as("codeOc")
        .first("numeroAdherentComplet")
        .as("numeroAdherentComplet")
        .first("dateSouscription")
        .as("dateSouscription")
        .first("dateResiliation")
        .as("dateResiliation")
        .first("type")
        .as("type")
        .first("nomPorteur")
        .as("nomPorteur")
        .first("prenomPorteur")
        .as("prenomPorteur")
        .first("civilitePorteur")
        .as("civilitePorteur")
        .first("qualification")
        .as("qualification")
        .first("numeroContratCollectif")
        .as("numeroContratCollectif")
        .first("isContratResponsable")
        .as("isContratResponsable")
        .first("isContratCMU")
        .as("isContratCMU")
        .first("destinataire")
        .as("destinataire")
        .first("individuelOuCollectif")
        .as("individuelOuCollectif")
        .first("situationDebut")
        .as("situationDebut")
        .first("situationFin")
        .as("situationFin")
        .first("motifFinSituation")
        .as("motifFinSituation")
        .first("typeConvention")
        .as("typeConvention")
        .first("critereSecondaire")
        .as("critereSecondaire")
        .first("critereSecondaireDetaille")
        .as("critereSecondaireDetaille")
        .first("numeroExterneContratIndividuel")
        .as("numeroExterneContratIndividuel")
        .first("numeroExterneContratCollectif")
        .as("numeroExterneContratCollectif")
        .first("gestionnaire")
        .as("gestionnaire")
        .first("groupeAssures")
        .as("groupeAssures")
        .first("numeroCarte")
        .as("numeroCarte")
        .first("editeurCarte")
        .as("editeurCarte")
        .first("fondCarte")
        .as("fondCarte")
        .first("annexe1Carte")
        .as("annexe1Carte")
        .first("annexe2Carte")
        .as("annexe2Carte")
        .first("numAMCEchange")
        .as("numAMCEchange")
        .first("numOperateur")
        .as("numOperateur")
        .first("ordrePriorisation")
        .as("ordrePriorisation")
        .first("contratCMUC2S")
        .as("contratCMUC2S")
        .first("dateRestitution")
        .as("dateRestitution")
        .first("dateCreation")
        .as("dateCreation")
        .first("dateModification")
        .as("dateModification")
        .first("dateConsolidation")
        .as("dateConsolidation");
  }
}
