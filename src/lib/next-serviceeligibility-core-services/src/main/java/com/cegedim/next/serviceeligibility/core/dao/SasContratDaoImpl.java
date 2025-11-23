package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository("sasContratDaoImpl")
@RequiredArgsConstructor
public class SasContratDaoImpl implements SasContratDao {

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "save SasContrat")
  public SasContrat save(SasContrat sas) {
    return template.save(sas);
  }

  @Override
  @ContinueSpan(log = "getByFunctionalKey SasContrat")
  public SasContrat getByFunctionalKey(
      String idDeclarant, String numeroContrat, String numeroAdherent) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.NUMERO_CONTRAT)
            .is(numeroContrat)
            .and(Constants.NUMERO_ADHERENT)
            .is(numeroAdherent));
    return template.findOne(query, SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getByIdTrigger SasContrat")
  public List<SasContrat> getByIdTrigger(String idTrigger) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(String.format("%s.%s", Constants.TRIGGERS_BENEFS, Constants.TRIGGER_ID))
            .is(idTrigger));
    return template.find(query, SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "delete SasContrat")
  public void delete(String id) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.ID).is(id));
    template.findAndRemove(query, SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "deleteByAmc SasContrat")
  public long deleteByAmc(String amc) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.ID_DECLARANT).is(amc));
    return template.remove(query, SasContrat.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "getByServicePrestationId SasContrat")
  public SasContrat getByServicePrestationId(String idServicePrestation) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.SERVICE_PRESTATION_ID).is(idServicePrestation));
    return template.findOne(query, SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "deleteAll SasContrat")
  public void deleteAll() {
    template.findAllAndRemove(new Query(), Constants.SAS_CONTRAT_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "updateRecycling SasContrat")
  public void updateRecycling(String sasId, boolean recycling) {
    log.debug("Updating sas {} to recycling {}", sasId, recycling);
    Criteria query = Criteria.where(Constants.ID).is(sasId);
    Update update = new Update().set("recycling", recycling);
    template.updateFirst(new Query(query), update, SasContrat.class);
  }

  @Override
  public void abandonTrigger(String idTrigger) {
    String path = String.format("%s.%s", Constants.TRIGGERS_BENEFS, Constants.TRIGGER_ID);
    Query query = new Query(Criteria.where(path).is(idTrigger));
    Update update =
        new Update()
            .pull(
                Constants.TRIGGERS_BENEFS,
                new Query(Criteria.where(Constants.TRIGGER_ID).is(idTrigger)));
    template.updateMulti(query, update, SasContrat.class);
  }

  @Override
  public void removeEmptySas() {
    Query query = new Query(Criteria.where(Constants.TRIGGERS_BENEFS + ".0").exists(false));
    template.remove(query, SasContrat.class);
  }

  @Override
  @ContinueSpan(log = "getByPersonNumber SasContrat")
  public List<String> getByPersonNumber(String personNumber) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.TRIGGERS_BENEFS_NUMERO_PERSONNE).is(personNumber));
    List<SasContrat> sasContratList =
        template.find(query, SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);

    return sasContratList.stream().map(SasContrat::getNumeroContrat).toList();
  }

  @Override
  @ContinueSpan(log = "getByContractNumber SasContrat")
  public List<SasContrat> getByContractNumber(String contractNumber) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.NUMERO_CONTRAT).is(contractNumber));
    return template.find(query, SasContrat.class, Constants.SAS_CONTRAT_COLLECTION);
  }
}
