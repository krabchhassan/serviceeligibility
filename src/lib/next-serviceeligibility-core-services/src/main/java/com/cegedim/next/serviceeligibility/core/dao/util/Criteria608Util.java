package com.cegedim.next.serviceeligibility.core.dao.util;

import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.mongodb.core.query.Criteria;

@UtilityClass
public class Criteria608Util {

  public static Criteria getCriteria(
      Declarant declarant,
      Pilotage pilotage,
      Date dateConsolidation,
      List<String> critSecondaireDetailleToExclude) {
    Criteria criteria = new Criteria(Constants.ID_DECLARANT).is(declarant.getNumeroPrefectoral());
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
    return criteria;
  }
}
