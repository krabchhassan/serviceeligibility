package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CardRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CardDaoImpl implements CardDao {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  @ContinueSpan(log = "findCartesDematFromRequest")
  public List<CarteDemat> findCartesDematFromRequest(CardRequest request) {
    Criteria searchCriteria = new Criteria();

    // Criteria AMC
    // If AMC is AON, search by idDeclarant/numeroAdherent
    if (Constants.NUMERO_AON.equals(request.getNumeroAmc())) {
      searchCriteria.and(Constants.ID_DECLARANT).is(request.getNumeroAmc());
      searchCriteria.and("contrat.numeroAdherent").is(request.getNumeroContrat());
    } else { // AMC is not AON, search by AMC_contrat
      String amcContrat = request.getNumeroAmc() + "-" + request.getNumeroContrat();
      searchCriteria.and(Constants.AMC_CONTRAT).is(amcContrat);
    }

    // Criteria periodeFin
    searchCriteria.and("periodeFin").gte(request.getDateReference().replace("-", "/"));

    // Criteria isLastCarteDemat
    searchCriteria.and(Constants.IS_LAST_CARTE_DEMAT).is(true);

    final Criteria carteDematCriteria =
        Criteria.where(Constants.CODE_SERVICES).in(List.of(Constants.CARTE_DEMATERIALISEE));
    final Criteria noCodeServicesCriteria = Criteria.where(Constants.CODE_SERVICES).exists(false);
    searchCriteria.orOperator(carteDematCriteria, noCodeServicesCriteria);

    // Criteria numeroAdherent
    if (StringUtils.isNotBlank(request.getNumeroAdherent())) {
      searchCriteria.and("contrat.numeroAdherent").is(request.getNumeroAdherent());
    }

    // Run the request with sorting
    Query searchQuery =
        Query.query(searchCriteria).with(Sort.by(Sort.Direction.ASC, Constants.PERIODE_DEBUT));
    return mongoTemplate.find(searchQuery, CarteDemat.class);
  }
}
