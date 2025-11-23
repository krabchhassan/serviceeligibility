package com.cegedim.beyond.blb.recalcul.service;

import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbTrackingRepo;
import com.cegedim.next.serviceeligibility.core.model.crex.JobRecalculBlbEndEventDto;
import com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BlbEndEventFactory {
  private final BddsToBlbTrackingRepo blbTrackingDao;

  private final AtomicInteger nbBeneficiaries = new AtomicInteger(0);
  private String deletionBeforeReinit;

  // FACTORY METHODS
  public static BlbEndEventFactory init(final BddsToBlbTrackingRepo blbTrackingDao) {
    return new BlbEndEventFactory(blbTrackingDao);
  }

  public synchronized JobRecalculBlbEndEventDto build() {
    int success = blbTrackingDao.countByStatus(BddsToBlbStatus.SUCCESS).intValue();
    int failed = blbTrackingDao.countByStatus(BddsToBlbStatus.FAILED).intValue();
    int noResponse = blbTrackingDao.countByStatus(BddsToBlbStatus.NO_RESPONSE).intValue();
    int noSend = blbTrackingDao.countByStatus(BddsToBlbStatus.NO_SEND).intValue();
    return JobRecalculBlbEndEventDto.builder()
        .nbTrackedBeneficiariesOK(success)
        .nbTrackedBeneficiariesKO(failed)
        .nbTrackedBeneficiariesNoResponse(noResponse)
        .nbTrackedBeneficiariesNoSend(noSend)
        .nbBeneficiaries(this.nbBeneficiaries.get())
        .deletionBeforeReinit(this.deletionBeforeReinit)
        .build();
  }

  // FLUENT METHODS
  public synchronized BlbEndEventFactory addBeneficiary() {
    this.nbBeneficiaries.incrementAndGet();
    return this;
  }

  public synchronized BlbEndEventFactory setDeletionBeforeReinit(
      final String deletionBeforeReinit) {
    this.deletionBeforeReinit = deletionBeforeReinit;
    return this;
  }
}
