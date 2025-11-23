package com.cegedim.next.serviceeligibility.batch635.job.domain.repository;

import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection.PeriodeDroitProjection;
import java.util.List;

public interface CustomContratsRepository {
  List<PeriodeDroitProjection> extractPeriodesDroit(
      String amc, String referenceDate, int pageIndex);
}
