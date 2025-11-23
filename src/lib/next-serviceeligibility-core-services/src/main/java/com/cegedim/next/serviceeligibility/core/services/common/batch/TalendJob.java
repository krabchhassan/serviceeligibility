package com.cegedim.next.serviceeligibility.core.services.common.batch;

import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class TalendJob<T extends HistoriqueExecutions<T>, Y extends DataForJob<T>> {

  @Autowired protected HistoriqueExecutionsDao historiqueExecutionsDao;

  @Autowired protected DeclarationDao declarationDao;

  protected static final int HEXADECIMAL = 16;

  protected final StopWatch mainWatch = new StopWatch();
  protected final StopWatch stopWatch = new StopWatch();
  protected final Map<String, Long> stepsDurations = new ConcurrentHashMap<>();

  protected T newHistoriqueExecutions;

  protected abstract String getBatchNumber();

  protected abstract String getCollection();

  protected abstract boolean isAmcReprise();

  protected void log() {
    log.info("Temps total d'exécution : {}", mainWatch.getTime());
    stepsDurations.forEach((key, value) -> log.info("Temps d'exécution : {} -> {}", key, value));
    log.info(
        "Somme des temps d'exécution : {}",
        stepsDurations.values().stream().mapToLong(i -> i).sum());
  }

  protected void registerTime(String action) {
    stopWatch.stop();
    stepsDurations.put(action, stepsDurations.getOrDefault(action, 0L) + stopWatch.getTime());
    stopWatch.reset();
  }

  /**
   * @param dataForJob : donnée de requête et d'informations pour le job
   * @return errorCode
   */
  public int process(Y dataForJob) {
    newHistoriqueExecutions.clear();
    if (dataForJob.getJddSize() > 0) {
      newHistoriqueExecutions.setNbDeclarationATraiter(dataForJob.getJddSize());
    }

    stepsDurations.clear();

    initCollection(dataForJob);
    log.info("Début du batch consolidation {}", dataForJob.getCollection());
    mainWatch.reset();
    mainWatch.start();
    stopWatch.start();

    dataForJob.setLastExecution(
        historiqueExecutionsDao.getLastExecution(
            getBatchNumber() + dataForJob.getCollectionName(), dataForJob.getHistoClass()));

    LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);
    newHistoriqueExecutions.setBatch(getBatchNumber() + dataForJob.getCollectionName());
    newHistoriqueExecutions.setDateExecution(Date.from(currentDate.toInstant(ZoneOffset.UTC)));

    fillLastIdProcessedAndDeclaration(dataForJob);

    registerTime("Upsert historiqueExecutionsDao");

    // si il y a des amcs à reprendre
    if (isAmcReprise()) {
      manageAMCReprise(dataForJob);
    }

    // on traite les amc une par une jusqu'à ne plus en avoir
    // les histo sont sauvegardé au fur et à mesure pour permettre la reprise
    // depuis le même endroit ou ça aurait été arrêté
    processRecords(dataForJob);
    mainWatch.stop();
    newHistoriqueExecutions.log();
    this.log();
    if (newHistoriqueExecutions.getNbDeclarationATraiter() > 0) {
      log.info(
          "Nombre de déclarations lues  {} / {}",
          newHistoriqueExecutions.getNbDeclarationLue(),
          newHistoriqueExecutions.getNbDeclarationATraiter());
    } else {
      log.info("Nombre de déclarations lues  {}", newHistoriqueExecutions.getNbDeclarationLue());
    }
    return 0;
  }

  private void initCollection(Y dataForJob) {
    if (StringUtils.isNotBlank(dataForJob.getCollectionName())) {
      dataForJob.setCollection(dataForJob.getCollectionName());
      dataForJob.setCollectionName(dataForJob.getCollectionName());
    } else {
      dataForJob.setCollection(getCollection());
      dataForJob.setCollectionName("");
    }
  }

  protected abstract void processRecords(Y dataForJob);

  protected abstract void manageAMCReprise(Y dataForJob);

  protected abstract void fillLastIdProcessedAndDeclaration(Y dataForJob);
}
