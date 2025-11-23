package com.cegedim.next.serviceeligibility.consolidationcontract.util;

import com.cegedim.next.serviceeligibility.consolidationcontract.bean.ContractKey;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@UtilityClass
public class ConsolidationJobUtil {

  public static final int OFFSET_LIMIT = 10000;

  public static Criteria getGreatherCriteria(ContractKey contractKey, boolean declaration) {
    if (declaration) {
      return new Criteria()
          .orOperator(
              Criteria.where(Constants.ID_DECLARANT).gt(contractKey.getIdDeclarant()),
              new Criteria()
                  .andOperator(
                      Criteria.where(Constants.ID_DECLARANT).is(contractKey.getIdDeclarant()),
                      Criteria.where(Constants.DECLARATION_NUMERO_CONTRAT)
                          .gt(contractKey.getNumeroContrat())),
              new Criteria()
                  .andOperator(
                      Criteria.where(Constants.ID_DECLARANT).is(contractKey.getIdDeclarant()),
                      Criteria.where(Constants.DECLARATION_NUMERO_CONTRAT)
                          .is(contractKey.getNumeroContrat()),
                      Criteria.where(Constants.CONTRAT_NUMERO_ADHERENT)
                          .gte(contractKey.getNumeroAdherent())));
    } else {
      return new Criteria()
          .orOperator(
              Criteria.where(Constants.ID_DECLARANT).gt(contractKey.getIdDeclarant()),
              new Criteria()
                  .andOperator(
                      Criteria.where(Constants.ID_DECLARANT).is(contractKey.getIdDeclarant()),
                      Criteria.where(Constants.DECLARATION_NUMERO_CONTRAT)
                          .gt(contractKey.getNumeroContrat())),
              new Criteria()
                  .andOperator(
                      Criteria.where(Constants.ID_DECLARANT).is(contractKey.getIdDeclarant()),
                      Criteria.where(Constants.DECLARATION_NUMERO_CONTRAT)
                          .is(contractKey.getNumeroContrat()),
                      Criteria.where(Constants.NUMERO_ADHERENT)
                          .gt(contractKey.getNumeroAdherent())));
    }
  }

  public static Query includeDeclarationIndexFields(Query query, List<String> declarantList) {
    if (CollectionUtils.isNotEmpty(declarantList)) {
      Criteria criteria = Criteria.where(Constants.ID_DECLARANT).in(declarantList);
      query.addCriteria(criteria);
    }
    query
        .fields()
        .include(
            Constants.ID_DECLARANT,
            Constants.DECLARATION_NUMERO_CONTRAT,
            Constants.CONTRAT_NUMERO_ADHERENT,
            Constants.EFFET_DEBUT,
            Constants.ID);
    return query;
  }
}
