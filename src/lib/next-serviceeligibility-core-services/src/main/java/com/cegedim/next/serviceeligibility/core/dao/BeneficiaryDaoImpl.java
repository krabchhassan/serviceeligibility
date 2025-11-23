package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.utils.BenefSearchConstants;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Iterator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("BeneficiaryDaoImpl")
@AllArgsConstructor
public class BeneficiaryDaoImpl implements BeneficiaryDao {
  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "getBeneficiaryByKey")
  public BenefAIV5 getBeneficiaryByKey(final String key) {
    Criteria criteria = Criteria.where("key").is(key);

    return template.findOne(
        new Query(criteria), BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "getBeneficiaryByKey")
  public List<BenefAIV5> getBeneficiaries(
      final String idDeclarant,
      String numeroAdherant,
      String numeroContrat,
      String numeroPersonne) {
    Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT_BENEF)
            .is(idDeclarant)
            .and(Constants.NUMERO_ADHERENT_CONTRAT_BENEF)
            .is(numeroAdherant)
            .and(Constants.NUMERO_CONTRAT_BENEF)
            .is(numeroContrat)
            .and(Constants.NUMERO_PERSONNE_BENEF)
            .ne(numeroPersonne);

    Sort sort =
        Sort.by(
            Sort.Order.asc(BenefSearchConstants.IDENTITE_DATE_NAISSANCE),
            Sort.Order.asc(BenefSearchConstants.IDENTITE_RANG_NAISSANCE),
            Sort.Order.asc(BenefSearchConstants.IDENTITE_NIR_CODE));
    return template.find(
        new Query(criteria).with(sort), BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "getBeneficiariesByDate")
  public List<BenefAIV5> getBeneficiariesByDateReference(
      final String idDeclarant, String numeroAdherent, String numeroContrat, String dateReference) {
    Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT_BENEF)
            .is(idDeclarant)
            .and(Constants.NUMERO_ADHERENT_CONTRAT_BENEF)
            .is(numeroAdherent)
            .and(Constants.NUMERO_CONTRAT_BENEF)
            .is(numeroContrat)
            .and(Constants.PERIODE_DEBUT_CONTRAT_BENEF)
            .lte(dateReference)
            .and(Constants.PERIODE_FIN_CONTRAT_BENEF)
            .gte(dateReference);

    return template.find(
        new Query(criteria), BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "deleteBeneficiaryById")
  public long deleteBeneficiaryById(final String id) {
    Criteria criteria = Criteria.where(Constants.ID).is(id);

    return template
        .remove(new Query(criteria), BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME)
        .getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteBeneficiariesByAmc")
  public long deleteBeneficiariesByAmc(final String idDeclarant) {
    Criteria criteria = Criteria.where("amc.idDeclarant").is(idDeclarant);

    return template
        .remove(new Query(criteria), BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME)
        .getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "getBeneficiaryByNirAndDateNaissanceAndRangNaissance")
  public BenefAIV5 getBeneficiaryByNirAndDateNaissanceAndRangNaissance(
      String nir, String dateNaissance, String rangNaissance) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where("")
            .orOperator(
                Criteria.where("identite.nir.code").is(nir),
                Criteria.where("identite.affiliationsRO.nir.code").is(nir)));
    query.addCriteria(Criteria.where("identite.dateNaissance").is(dateNaissance));
    query.addCriteria(Criteria.where("identite.rangNaissance").is(rangNaissance));

    return template.findOne(query, BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
  }

  @Override
  @ContinueSpan(log = "getBenefMultiOS")
  public Iterator<BenefAIV5> getBenefMultiOS() {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.SOCIETES_EMETTRICES + ".1").exists(true));
    return template.stream(query, BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME)
        .iterator();
  }

  @Override
  public BenefAIV5 save(BenefAIV5 benefToSave) {
    return template.save(benefToSave, Constants.BENEFICIAIRE_COLLECTION_NAME);
  }

  @Override
  public List<BenefAIV5> getAll() {
    return template.findAll(BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
  }

  @Override
  public void dropCollections() {
    template.remove(new Query(), Constants.BENEFICIAIRE_COLLECTION_NAME);
    template.remove(new Query(), Constants.BENEF_TRACE);
    template.remove(new Query(), Constants.DECLARATION_TRACE);
  }
}
