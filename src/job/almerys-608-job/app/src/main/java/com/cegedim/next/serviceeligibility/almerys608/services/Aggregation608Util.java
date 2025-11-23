package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.utils.Constants608;
import com.cegedim.next.serviceeligibility.core.dao.util.Criteria608Util;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

@UtilityClass
public class Aggregation608Util {
  public static Aggregation aggregationCollectionTmp2(
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
            out)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  private static ProjectionOperation getLastProjection() {
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
        .and(Constants608.DECL_INFOS_CARTE_TP)
        .as(Constants.INFOS_CARTE_TP)
        .and(Constants608.DECL_DOMAINE_DROIT)
        .as(Constants.DOMAINE_DROIT)
        .and(Constants608.DECL_ID_DECLARATIONS)
        .as(Constants.ID_DECLARATION)
        .and(Constants608.DECL2_DOMAINE_DROIT_IS_SUSPENSION)
        .as(Constants608.FLAG)
        .and(Constants608.DECL_ORIGINE_DECLARATION)
        .as(Constants608.ORIGINE_DECLARATION)
        .and(Constants608.DECL_DATE_RESILIATION)
        .as(Constants608.DATE_RESILIATION);
  }

  public static Aggregation aggregationCollectionTmp1(
      Declarant declarant,
      Pilotage pilotage,
      List<String> critSecondaireDetailleToExclude,
      Date dateDerniereExecution,
      String nameTmpCollection1) {
    MatchOperation matchDeclarationsConsoAlmerys =
        matchDeclarationsConsolideesAlmerys(
            declarant, pilotage, dateDerniereExecution, critSecondaireDetailleToExclude);
    ProjectionOperation project = Aggregation.project(Constants.AMC_CONTRAT_CSD);
    GroupOperation group =
        Aggregation.group(Constants.AMC_CONTRAT_CSD).count().as(Constants608.COUNT_DECLARATIONS);
    SortOperation sort = Aggregation.sort(Sort.by(Constants.AMC_CONTRAT_CSD));
    OutOperation out = Aggregation.out(nameTmpCollection1);
    return Aggregation.newAggregation(matchDeclarationsConsoAlmerys, project, group, sort, out)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  public static MatchOperation matchDeclarationsConsolideesAlmerys(
      Declarant declarant,
      Pilotage pilotage,
      Date dateDerniereExecution,
      List<String> critSecondaireDetailleToExclude) {
    return Aggregation.match(
        Criteria608Util.getCriteria(
            declarant, pilotage, dateDerniereExecution, critSecondaireDetailleToExclude));
  }
}
