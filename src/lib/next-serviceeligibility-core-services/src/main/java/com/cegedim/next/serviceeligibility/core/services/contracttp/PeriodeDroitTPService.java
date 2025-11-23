package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.mapper.MapperPeriodeDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.DomaineDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.NaturePrestation;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeriodeDroitTPService {

  private final PeriodeDroitTPStep1 periodeDroitTPStep1;

  private final PeriodeDroitTPStep2 periodeDroitTPStep2;

  private final PeriodeDroitTPStep3 periodeDroitTPStep3;

  private final PeriodeDroitTPStep4 periodeDroitTPStep4;

  public void consolidatePeriods(
      DomaineDroitContractTP domaineDroitContract,
      Set<DomaineDroitForConsolidation> newDomaineDroitForConsolidations) {
    for (DomaineDroitForConsolidation domaineDroitForConsolidation :
        newDomaineDroitForConsolidations) {
      NaturePrestation naturePrestation =
          periodeDroitTPStep1.extractOrCreatePeriodes(
              domaineDroitContract, domaineDroitForConsolidation);
      List<PeriodeDroitContractTP> periodeDroitContractTPS = naturePrestation.getPeriodesDroit();
      boolean foundAndUpdated =
          periodeDroitTPStep2.extractAndUpdatePeriodesDroitContrat(
              periodeDroitContractTPS, domaineDroitForConsolidation);

      if (!foundAndUpdated) {
        periodeDroitContractTPS.addAll(
            MapperPeriodeDroitContrat.createNewPeriode(domaineDroitForConsolidation));
      }

      periodeDroitTPStep3.extractAndUpdateFinPeriodesDroitContrat(
          periodeDroitContractTPS, domaineDroitForConsolidation);

      periodeDroitTPStep4.handleSubPeriods(naturePrestation, domaineDroitForConsolidation);
    }
    MapperPeriodeDroitContrat.deleteEmptyPeriodes(domaineDroitContract);
  }
}
