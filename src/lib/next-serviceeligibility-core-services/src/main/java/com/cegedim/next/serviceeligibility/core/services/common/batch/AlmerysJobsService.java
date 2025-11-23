package com.cegedim.next.serviceeligibility.core.services.common.batch;

import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;

@Slf4j
@RequiredArgsConstructor
public class AlmerysJobsService {

  public Criteria getLastHistoriquePilotageCriteria(
      Declarant declarant, Pilotage pilotage, String numeroBatch) {
    return Criteria.where(Constants.BATCH)
        .is(numeroBatch)
        .and(Constants.CODE_SERVICE)
        .is(pilotage.getCodeService())
        .and(Constants.ID_DECLARANT)
        .is(declarant.getNumeroPrefectoral())
        .and(Constants.CRITERE_SECONDAIRE)
        .is(pilotage.getCritereRegroupement())
        .and(Constants.CRITERE_SECONDAIRE_DETAILLE)
        .is(pilotage.getCritereRegroupementDetaille())
        .and(Constants.TYPE_CONVENTIONNEMENT)
        .is(pilotage.getTypeConventionnement());
  }
}
