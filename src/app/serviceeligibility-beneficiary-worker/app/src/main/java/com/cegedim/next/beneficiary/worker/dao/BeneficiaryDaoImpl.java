package com.cegedim.next.beneficiary.worker.dao;

import com.cegedim.next.serviceeligibility.core.model.kafka.BenefAI;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository("beneficiaryDaoImpl")
@AllArgsConstructor
public class BeneficiaryDaoImpl implements BeneficiaryDao {

  private static final String ID_DECLARANT = "amc.idDeclarant";
  private static final String NUMERO_ADHERENT = "numeroAdherent";
  private static final String NUMERO_PERSONNE = "identite.numeroPersonne";
  private static final String DATE_NAISSANCE = "identite.dateNaissance";
  private static final String RANG_NAISSANCE = "identite.rangNaissance";

  private final MongoTemplate mongoTemplate;

  @Override
  public BenefAI getBeneficiary(
      String idDeclarant,
      String numeroAdherent,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance) {
    // Crtères de recherche d'un bénéficiaire
    Criteria benefCriteria = Criteria.where(ID_DECLARANT).is(idDeclarant);
    benefCriteria.and(NUMERO_ADHERENT).is(numeroAdherent);
    benefCriteria.and(NUMERO_PERSONNE).is(numeroPersonne);
    benefCriteria.and(DATE_NAISSANCE).is(dateNaissance);
    benefCriteria.and(RANG_NAISSANCE).is(rangNaissance);

    Aggregation agg = Aggregation.newAggregation(Aggregation.match(benefCriteria));

    List<BenefAI> benefs =
        mongoTemplate
            .aggregate(agg, Constants.BENEFICIAIRE_COLLECTION_NAME, BenefAI.class)
            .getMappedResults();
    if (CollectionUtils.isEmpty(benefs)) {
      return null;
    }
    // Retourne le 1er bénef trouvé
    return benefs.get(0);
  }
}
