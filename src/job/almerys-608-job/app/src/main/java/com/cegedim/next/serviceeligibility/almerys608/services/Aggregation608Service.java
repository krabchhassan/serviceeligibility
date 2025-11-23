package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.utils.Constants608;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

public class Aggregation608Service {
  protected Aggregation aggregationCollectionTmp2(
      Declarant declarant, String nameTmpCollection1, String nameNewTmpCollection) {
    MatchOperation matchDeclarationsConsolidees =
        Aggregation.match(
            Criteria.where(Constants.ID_DECLARANT).is(declarant.getNumeroPrefectoral()));
    LookupOperation lookupOperation =
        Aggregation.lookup(
            nameTmpCollection1,
            Constants.AMC_CONTRAT_CSD,
            Constants.ID,
            Constants608.CONTRAT_MODIFIED);
    MatchOperation matchContratModified =
        Aggregation.match(Criteria.where(Constants608.CONTRAT_MODIFIED_ID).exists(true));
    UnwindOperation unwindOperation = Aggregation.unwind(Constants608.CONTRAT_MODIFIED);
    SortOperation sortOperation =
        Aggregation.sort(
                Sort.by(
                    Constants.AMC_CONTRAT_CSD,
                    Constants.BENEFICIAIRE_NUMERO_PERSONNE,
                    Constants.DOMAINE_DROIT_CODE_EXTERNE_PRODUIT))
            .and(Sort.by(Sort.Direction.DESC, Constants.DATE_CONSOLIDATION));
    GroupOperation group =
        Aggregation.group(
                Constants.AMC_CONTRAT_CSD,
                Constants.BENEFICIAIRE_NUMERO_PERSONNE,
                Constants.DATE_CONSOLIDATION)
            .push(Constants608.ROOT)
            .as(Constants608.DECLS);
    SortOperation sortOperation2 =
        Aggregation.sort(Sort.by(Constants608.ID_AMC_CONTRAT_CSD, Constants608.ID_NUMERO_PERSONNE))
            .and(Sort.by(Sort.Direction.DESC, Constants608.ID_DATE_CONSOLIDATION));
    GroupOperation group2 =
        Aggregation.group(Constants608.ID_AMC_CONTRAT_CSD, Constants608.ID_NUMERO_PERSONNE)
            .push(Constants608.DECLS)
            .as(Constants608.DECLS);
    ProjectionOperation project =
        Aggregation.project(Constants.ID)
            .and(Constants608.DECLS)
            .arrayElementAt(0)
            .as(Constants608.DECL)
            .and(Constants608.DECLS)
            .arrayElementAt(1)
            .as(Constants608.DECLS2);
    ProjectionOperation project2 =
        Aggregation.project(Constants.ID)
            .and(Constants608.DECL)
            .as(Constants608.DECL)
            .and(Constants608.DECLS2)
            .arrayElementAt(0)
            .as(Constants608.DECL2);
    UnwindOperation unwindOperation2 = Aggregation.unwind(Constants608.DECL);
    ProjectionOperation project3 = getLastProjection();
    OutOperation out = Aggregation.out(nameNewTmpCollection);
    return Aggregation.newAggregation(
        matchDeclarationsConsolidees,
        lookupOperation,
        matchContratModified,
        unwindOperation,
        sortOperation,
        group,
        sortOperation2,
        group2,
        project,
        project2,
        unwindOperation2,
        project3,
        out);
  }

  private ProjectionOperation getLastProjection() {
    return Aggregation.project()
        .and(Constants608.DECL_ID)
        .as(Constants.ID)
        .and(Constants608.DECL_ID_INTERNE)
        .as(Constants608.ID_INTERNE)
        .and(Constants608.DECL_REF_EXTERNE)
        .as(Constants608.REF_EXTERNE)
        .and(Constants608.DECL_CODE_ETAT)
        .as(Constants.CODE_ETAT)
        .and(Constants608.DECL_EFFET_DEBUT)
        .as(Constants.EFFET_DEBUT)
        .and(Constants608.DECL_ID_DECLARANT)
        .as(Constants.ID_DECLARANT)
        .and(Constants608.DECL_IS_CARTE_TP_A_EDITER)
        .as(Constants608.IS_CARTE_TP_A_EDITER)
        .and(Constants608.DECL_DATE_CREATION)
        .as(Constants.DATE_CREATION)
        .and(Constants608.DECL_USER_CREATION)
        .as(Constants.USER_CREATION)
        .and(Constants608.DECL_DATE_MODIFICATION)
        .as(Constants.DATE_MODIFICATION)
        .and(Constants608.DECL_USER_MODIFICATION)
        .as(Constants.USER_MODIFICATION)
        .and(Constants608.DECL_BENEFICIAIRE)
        .as(Constants.BENEF_IN_DECLARATION)
        .and(Constants608.DECL_CONTRAT)
        .as(Constants.CONTRAT)
        .and(Constants608.DECL_DOMAINE_DROIT)
        .as(Constants.DOMAINE_DROIT)
        .and(Constants608.DECL_ID_DECLARATIONS)
        .as(Constants.ID_DECLARATION)
        .and(Constants608.DECL2_DOMAINE_DROIT_IS_SUSPENSION)
        .as(Constants608.FLAG);
  }

  protected Aggregation aggregationCollectionTmp1(
      Declarant declarant,
      Pilotage pilotage,
      List<String> critSecondaireDetailleToExclude,
      HistoriqueExecution608 lastExecution,
      String nameTmpCollection1) {
    MatchOperation matchDeclarationsConsoAlmerys =
        matchDeclarationsConsolideesAlmerys(
            declarant, pilotage, lastExecution, critSecondaireDetailleToExclude);
    ProjectionOperation project = Aggregation.project(Constants.AMC_CONTRAT_CSD);
    GroupOperation group =
        Aggregation.group(Constants.AMC_CONTRAT_CSD).count().as(Constants608.COUNT_DECLARATIONS);
    SortOperation sort = Aggregation.sort(Sort.by(Constants.AMC_CONTRAT_CSD));
    OutOperation out = Aggregation.out(nameTmpCollection1);
    return Aggregation.newAggregation(matchDeclarationsConsoAlmerys, project, group, sort, out);
  }

  private MatchOperation matchDeclarationsConsolideesAlmerys(
      Declarant declarant,
      Pilotage pilotage,
      HistoriqueExecution608 lastExecution,
      List<String> critSecondaireDetailleToExclude) {
    Criteria criteria = new Criteria(Constants.ID_DECLARANT).is(declarant.getNumeroPrefectoral());
    Date lastExecutionDate = lastExecution != null ? lastExecution.getDateExecution() : null;
    Date dateConsolidation =
        pilotage.getDateSynchronisation() != null
            ? pilotage.getDateSynchronisation()
            : lastExecutionDate;
    if (dateConsolidation != null) {
      criteria.and(Constants.DATE_CONSOLIDATION).gte(dateConsolidation);
    }
    if (pilotage.getCritereRegroupementDetaille() != null) {
      criteria
          .and(Constants.CONTRAT_CRITERE_SECONDAIRE_DETAILLE)
          .is(pilotage.getCritereRegroupementDetaille());
    } else if (CollectionUtils.isNotEmpty(critSecondaireDetailleToExclude)) {
      criteria
          .and(Constants.CONTRAT_CRITERE_SECONDAIRE_DETAILLE)
          .not()
          .in(critSecondaireDetailleToExclude);
    }
    return Aggregation.match(criteria);
  }
}
