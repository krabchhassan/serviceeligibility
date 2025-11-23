package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDao;
import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.almerys608.utils.EnumTempTable;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Slf4j
public class DomainService {
  private final CommonDao commonDao;
  private final UtilService utilService;

  public <T extends BulkObject> void insertAll(List<T> toInsert, String collectionName) {
    commonDao.insertAll(toInsert, toInsert.get(0).getClass(), collectionName);
  }

  public void clearTempTables(Map<String, String> tempTables) {
    commonDao.clearTempTable(tempTables);
  }

  public <T> T get(Class<T> tempClass, String collectionName, String numeroContrat) {
    return commonDao.get(tempClass, collectionName, numeroContrat);
  }

  public <T> List<T> getAll(String collectionName, Class<T> objectClass) {
    return commonDao.getAll(collectionName, objectClass);
  }

  public <T extends BulkObject> void saveBulks(
      BulkList<T> bulkList, HistoriqueExecution608 historiqueExecution608) {
    saveBulks(bulkList, false, historiqueExecution608);
  }

  public <T extends BulkObject> void saveBulks(
      BulkList<T> bulkList, boolean forced, HistoriqueExecution608 historiqueExecution608) {
    String numAmc = historiqueExecution608.getIdDeclarant();
    String csd = historiqueExecution608.getCritereSecondaireDetaille();
    for (Map.Entry<String, List<T>> entry : bulkList.getBulks().entrySet()) {
      if (!CollectionUtils.isEmpty(entry.getValue())
          && (entry.getValue().size() >= 1000 || forced)) {
        String tableName = bulkList.getTempTables().get(entry.getKey());
        insertAll(entry.getValue(), tableName);
        if (tableName.equals(
            utilService.getTempCollectionName(
                EnumTempTable.MEMBRE_CONTRAT.getTableName(), numAmc, csd))) {
          historiqueExecution608.incMembresContrats(entry.getValue().size());
        } else if (tableName.equals(
            utilService.getTempCollectionName(EnumTempTable.CONTRAT.getTableName(), numAmc, csd))) {
          historiqueExecution608.incContrats(entry.getValue().size());
          historiqueExecution608.incNombreContrat(entry.getValue().size());
        } else if (tableName.equals(
            utilService.getTempCollectionName(
                EnumTempTable.BENEFICIAIRE.getTableName(), numAmc, csd))) {
          historiqueExecution608.incNombreBeneficiaire(entry.getValue().size());
        }
        entry.getValue().clear();
      }
    }
  }
}
