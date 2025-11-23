package com.cegedim.next.serviceeligibility.batch.services;

import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsRenouvellementDao;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRenouvTp;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerGenerationRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutionsRenouvellement;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import net.minidev.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Engine implements ApplicationContextAware {
  private final Logger logger = LoggerFactory.getLogger(Engine.class);

  private ApplicationContext context;

  @Autowired TriggerCreationService triggerService;

  @Autowired HistoriqueExecutionsRenouvellementDao historiqueExecutionsRenouvellementDao;

  @Autowired private CrexProducer crexProducer;

  @Override
  @ContinueSpan(log = "setApplicationContext")
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  public int processStart(int result, boolean isRdo) throws InterruptedException {
    CompteRenduRenouvTp compteRendu = new CompteRenduRenouvTp();
    try {
      if (result == 0) {
        generateTriggers(isRdo, compteRendu);
      }
    } catch (Exception e) {
      logger.error(
          String.format("Exception rencontrée lors du traitement : %s", e.getMessage()), e);
      result = -1;
    }
    // Generate CREX
    crexProducer.generateCrex(compteRendu);

    Thread.sleep(1000L);
    if (context != null) {
      ((ConfigurableApplicationContext) context).close();
      return result;
    }
    return -1;
  }

  private void generateTriggers(boolean isRdo, CompteRenduRenouvTp compteRendu) {
    logger.info("Début du batch de génération des déclencheurs de renouvellement");
    LocalDate currentDate = LocalDate.now(ZoneOffset.UTC);

    HistoriqueExecutionsRenouvellement historique = new HistoriqueExecutionsRenouvellement();
    historique.setDateCreation(new Date());

    // Extraction de la dernière exécution du batch
    HistoriqueExecutionsRenouvellement lastExecution;

    if (isRdo) {
      lastExecution = historiqueExecutionsRenouvellementDao.getLastExecution();
    } else { // !isRdo
      lastExecution = historiqueExecutionsRenouvellementDao.getLastExecutionIgnoringRdo();
    }

    LocalDate lastExecutionDate;
    if (lastExecution != null) {
      lastExecutionDate = LocalDate.parse(lastExecution.getDateTraitement());
    } else {
      lastExecutionDate = currentDate.minusDays(1);
    }

    // On vérifie si on doit lancer le traitement en fonction de isRdo et de
    // lastExecutionDate
    boolean run;

    if (!isRdo && lastExecutionDate.isEqual(currentDate)) {
      logger.info("Le batch a déjà tourné aujourd'hui.");
      run = false;
    } else if (isRdo && lastExecutionDate.isEqual(currentDate)) {
      // On enlève un jour pour quand même traiter la date du jour
      lastExecutionDate = lastExecutionDate.minusDays(1);
      run = true;
    } else { // !lastExecutionDate.isEqual(currentDate)
      run = true;
    }

    if (run) {
      while (lastExecutionDate.isBefore(currentDate)) {
        lastExecutionDate = lastExecutionDate.plusDays(1);
        traiterDate(lastExecutionDate, isRdo, compteRendu);
      }

      historique.setDateExecution(new Date());
      historique.setDateTraitement(currentDate.toString());
      historique.setNombreTriggersCreesRenouvellement(compteRendu.getTriggerCount());
      historique.setNombreTriggeredBeneficiariesCreesRenouvellement(
          compteRendu.getTriggeredBenefCount());
      historique.setRdo(isRdo);
      historiqueExecutionsRenouvellementDao.create(historique);

      logger.info("Fin du batch de génération des déclencheurs de renouvellement");
      logger.info(
          "{} trigger(s) générés pour {} benefs",
          compteRendu.getTriggerCount(),
          compteRendu.getTriggeredBenefCount());
    }
  }

  @NewSpan
  public void traiterDate(LocalDate currentDate, boolean isRdo, CompteRenduRenouvTp compteRendu) {
    TriggerGenerationRequest request = new TriggerGenerationRequest();
    request.setEmitter(TriggerEmitter.Renewal);
    request.setDate(currentDate.toString());
    request.setRdo(isRdo);
    try {
      // Sub-item that will be part of the CREX list
      JSONObject dayCount = new JSONObject();
      dayCount.put("dateRenouvellement", currentDate.toString());
      int subTriggerCount = 0;
      int subBenefCount = 0;

      List<Trigger> triggersGenerated = triggerService.generateTriggers(request);
      if (CollectionUtils.isNotEmpty(triggersGenerated)) {
        compteRendu.addTriggerCount(triggersGenerated.size());
        subTriggerCount += triggersGenerated.size();
        for (Trigger trigger : triggersGenerated) {
          compteRendu.addTriggeredBenefCount(trigger.getNbBenef());
          subBenefCount += trigger.getNbBenef();
        }
      }

      dayCount.put("triggerCount", subTriggerCount);
      dayCount.put("triggeredBenefCount", subBenefCount);

      compteRendu.addCompteRenduValue(dayCount.toString());
    } catch (TriggerException e) {
      throw new TriggerException(String.format("TriggerException %s :", e.getMessage()), e);
    } catch (Exception e) {
      throw new TriggerException(String.format("Plantage improbable ! %s", e.getMessage()), e);
    }
  }
}
