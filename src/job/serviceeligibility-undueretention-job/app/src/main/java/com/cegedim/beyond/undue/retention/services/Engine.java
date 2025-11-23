package com.cegedim.beyond.undue.retention.services;

import com.cegedim.beyond.schemas.DelaiRetentionFinishedEventDto;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.dao.RetentionDao;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduUndueRetention;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class Engine {

  private final CrexProducer crexProducer;
  private final DeclarantDao declarantDao;
  private final RetentionDao retentionDao;
  private final ServicePrestationDao servicePrestationDao;
  private final UndueRetentionService undueRetentionService;
  private final RetentionService retentionService;
  private final EventService eventService;

  public int process() {
    int status = 0;
    CompteRenduUndueRetention compteRendu = new CompteRenduUndueRetention();

    try {
      // pour chaque déclarant
      // on récupére les retentions candidates (TO_PROCESS & age > délai)
      // pour chaque retention
      // on essaye de locker (si pas possible, on ignore la retention)
      // on recalcule la date par rapport au dernier contrat stocké
      // si nouvelle date null ou >= date jour : annulation retention
      // sinon, si nouvelle date différente de l'ancienne, on met à jour la retention
      // sinon, retention à envoyer

      List<Declarant> declarants = declarantDao.findAll();
      declarants.forEach(
          declarant -> {
            int delai = 3;
            if (StringUtils.isNotBlank(declarant.getDelaiRetention())) {
              delai = Integer.parseInt(declarant.getDelaiRetention());
              log.info(
                  "Declarant : {} delai : {}", declarant.get_id(), declarant.getDelaiRetention());
            }
            List<Retention> retentions = retentionDao.findAllByDelai(declarant.get_id(), delai);
            retentions.forEach(
                retention -> {
                  long retentionExists = retentionDao.findAndLock(retention);
                  if (retentionExists > 0) {
                    ContratAIV6 contrat =
                        servicePrestationDao.findServicePrestationByContractNumber(
                            retention.getContractNumber(),
                            retention.getInsurerId(),
                            retention.getSubscriberNumber());

                    if (contrat == null) {
                      // contrat introuvable, on annule la retention
                      retention.setReceptionDate(LocalDateTime.now());
                      retention.setStatus(RetentionStatus.CANCELLED);
                      retentionDao.createRetention(retention);
                      eventService.sendObservabilityEventRetentionFinished(
                          DelaiRetentionFinishedEventDto.Action.ANNULE, retention);
                      compteRendu.incNbOccurrenceAnnulee();
                    } else {
                      contratFound(retention, contrat, compteRendu);
                    }
                  }
                  // else = retention lockée par un autre service
                });
          });

    } catch (Exception e) {
      log.error("Ca a planté ...");
      throw new ExceptionTechnique(e.getMessage(), e);
    } finally {
      crexProducer.generateCrex(compteRendu);
    }

    return status;
  }

  private void contratFound(
      Retention retention, ContratAIV6 contrat, CompteRenduUndueRetention compteRendu) {
    String dateRetentionCalculee =
        retentionService.calculDateRetention(contrat, retention.getPersonNumber());

    boolean isMultiContrat =
        retentionService.isMultiContrat(contrat.getIdDeclarant(), retention.getPersonNumber());

    switch (undueRetentionService.processRetention(
        dateRetentionCalculee, retention, isMultiContrat)) {
      case -1:
        compteRendu.incNbOccurrenceAnnulee();
        break;
      case 0:
        compteRendu.incNbOccurrenceModifiee();
        break;
      case 1:
        compteRendu.incNbOccurrenceEnvoyee();
        break;
      default:
        throw new IllegalStateException("Unexpected value: -1, 0 ou 1");
    }
  }
}
