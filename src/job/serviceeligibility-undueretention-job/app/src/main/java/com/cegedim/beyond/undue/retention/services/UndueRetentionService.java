package com.cegedim.beyond.undue.retention.services;

import com.cegedim.beyond.schemas.*;
import com.cegedim.next.serviceeligibility.core.dao.RetentionDao;
import com.cegedim.next.serviceeligibility.core.model.domain.AffiliationRO;
import com.cegedim.next.serviceeligibility.core.model.domain.AttachementRO;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Nir;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Periode;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UndueRetentionService {

  private final RetentionDao retentionDao;
  private final RetentionMessageService messageService;
  private final EventService eventService;

  public int processRetention(
      String dateRetentionCalculee, Retention lockedRetention, boolean isMultiContrat) {
    String nowStr = LocalDate.now().format(DateUtils.FORMATTER);

    if (dateRetentionCalculee == null || !DateUtils.before(dateRetentionCalculee, nowStr)) {
      // retention plus valide (annulee)
      lockedRetention.setReceptionDate(LocalDateTime.now());
      lockedRetention.setStatus(RetentionStatus.CANCELLED);
      retentionDao.createRetention(lockedRetention);
      logRetention(lockedRetention);
      eventService.sendObservabilityEventRetentionFinished(
          DelaiRetentionFinishedEventDto.Action.ANNULE, lockedRetention);
      return -1;
    } else if (!lockedRetention.getCurrentEndDate().equals(dateRetentionCalculee)) {
      // changement date retention
      lockedRetention.setCurrentEndDate(dateRetentionCalculee);
      lockedRetention.setReceptionDate(LocalDateTime.now());
      lockedRetention.setStatus(RetentionStatus.TO_PROCESS);
      retentionDao.createRetention(lockedRetention);
      logRetention(lockedRetention);
      eventService.sendObservabilityEventRetentionFinished(
          DelaiRetentionFinishedEventDto.Action.MODIFIE, lockedRetention);
      return 0;
    } else {
      // retention expirée (envoi message)
      UndueRetentionMessageDto undueRetentionMessageDto =
          mapFromContract(lockedRetention, isMultiContrat);
      String key =
          lockedRetention.getInsurerId()
              + "-"
              + lockedRetention.getContractNumber()
              + "-"
              + lockedRetention.getPersonNumber();
      messageService.sendUndueRetentionMessage(key, undueRetentionMessageDto);

      lockedRetention.setReceptionDate(LocalDateTime.now());
      lockedRetention.setStatus(RetentionStatus.PROCESSED);
      retentionDao.createRetention(lockedRetention);
      logRetention(lockedRetention);
      eventService.sendObservabilityEventRetentionFinished(
          DelaiRetentionFinishedEventDto.Action.TRAITE, lockedRetention);
      return 1;
    }
  }

  private static void logRetention(Retention lockedRetention) {
    log.info(
        "Retention id {}, insuredId {}, contratNumber {}, personNumber {}, status {}",
        lockedRetention.get_id(),
        lockedRetention.getInsurerId(),
        lockedRetention.getContractNumber(),
        lockedRetention.getPersonNumber(),
        lockedRetention.getStatus());
  }

  public UndueRetentionMessageDto mapFromContract(Retention retention, boolean isMultiContrat) {
    UndueRetentionMessageDto undueRetentionMessageDto = new UndueRetentionMessageDto();
    undueRetentionMessageDto.setInsurerId(retention.getInsurerId());
    undueRetentionMessageDto.setContractNumber(retention.getContractNumber());
    undueRetentionMessageDto.setIssuingCompanyCode(retention.getIssuingCompanyCode());
    undueRetentionMessageDto.setBirthDate(retention.getBirthDate());
    undueRetentionMessageDto.setBirthRank(retention.getBirthRank());
    undueRetentionMessageDto.setNir(retention.getNir());
    undueRetentionMessageDto.setAffiliationsRO(mapFromAffiliations(retention.getAffiliationsRO()));
    undueRetentionMessageDto.setPersonNumber(retention.getPersonNumber());
    undueRetentionMessageDto.setSubscriberNumber(retention.getSubscriberNumber());
    undueRetentionMessageDto.setPreviousEndDate(retention.getOriginalEndDate());
    undueRetentionMessageDto.setNewEndDate(retention.getCurrentEndDate());
    undueRetentionMessageDto.setIsMultiContrat(isMultiContrat);

    return undueRetentionMessageDto;
  }

  public List<AffiliationsRO> mapFromAffiliations(List<AffiliationRO> affiliationsRO) {
    List<AffiliationsRO> affiliationsROList = new ArrayList<>();
    if (affiliationsRO != null) {
      for (AffiliationRO affiliationRO : affiliationsRO) {
        AffiliationsRO a = new AffiliationsRO();
        a.setPeriode(mapFromPeriode(affiliationRO.getPeriode()));
        a.setNir(mapFromNir(affiliationRO.getNir()));
        a.setAttachementRO(mapFromAttachementRO(affiliationRO.getAttachementRO()));
        affiliationsROList.add(a);
      }
    }
    return affiliationsROList;
  }

  public com.cegedim.beyond.schemas.Periode mapFromPeriode(Periode periode) {
    com.cegedim.beyond.schemas.Periode p = new com.cegedim.beyond.schemas.Periode();
    p.setStart(periode.getDebut());
    p.setEnd(periode.getFin());
    return p;
  }

  public Nir__1 mapFromNir(Nir nir) {
    Nir__1 n = new Nir__1();
    n.setCode(nir.getCode());
    n.setKey(nir.getCle());
    return n;
  }

  public AttachementRO__1 mapFromAttachementRO(AttachementRO attachementRO) {
    AttachementRO__1 a = new AttachementRO__1();
    a.setHealthInsuranceCompanyCode(attachementRO.getHealthInsuranceCompanyCode());
    a.setCenterCode(attachementRO.getCenterCode());
    a.setRegimeCode(attachementRO.getRegimeCode());
    return a;
  }
}
