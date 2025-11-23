package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.dao.CartePapierDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.CartesService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.ProcessorConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.DeclarationConsolideUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestBatch620Service {

  private final ARLService arlService;
  private final HistoriqueExecutionsDao historiqueExecutionsDao;
  private final ProcessorConsolidationService consolidationService;
  private final CartesService cartesService;
  private final DeclarationDao declarationDao;
  private final InvalidationCarteService invalidationCarteService;
  private final CartePapierDao cartePapierDao;

  public void processDeclarations620(String date, String couloir) throws Exception {
    arlService.deleteDirectoryARL();
    DataForJob620 dataForJob620 = new DataForJob620();
    dataForJob620.setToday(DateUtils.parseDate(date, DateUtils.YYYY_MM_DD));
    dataForJob620.setIdentifiant(Util.getRandomString());
    dataForJob620.setLastExecution(
        historiqueExecutionsDao.getLastExecution(
            Constants.NUMERO_BATCH_620, HistoriqueExecutions620.class));
    dataForJob620.setClientType(Constants.CLIENT_TYPE_INSURER);
    if (couloir != null) {
      dataForJob620.setCouloirClient(couloir);
    }

    HistoriqueExecutions620 newHistoriqueExecutions620 = new HistoriqueExecutions620();
    LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);
    newHistoriqueExecutions620.setBatch(Constants.NUMERO_BATCH_620);
    newHistoriqueExecutions620.setDateExecution(Date.from(currentDate.toInstant(ZoneOffset.UTC)));
    consolidationService.processConsolidation(dataForJob620, newHistoriqueExecutions620);

    historiqueExecutionsDao.save(newHistoriqueExecutions620);
  }

  public void invalidationCarte620(String idDeclarant, String numeroContrat) {
    arlService.deleteDirectoryARL();
    List<Declaration> declarations =
        declarationDao.findDeclarationsOfBenef(idDeclarant, numeroContrat, "", "", "", "", null);
    List<CarteDemat> cartes =
        cartesService.findCartesDematByDeclarantAndContrat(idDeclarant, numeroContrat);
    List<CarteDemat> invalidCartes =
        invalidationCarteService.invalidationCartes(
            cartes,
            declarations,
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_INSURER,
            DeclarationConsolideUtils.getPeriodeSuspensionList(declarations));
    invalidationCarteService.saveInvalid(invalidCartes, null);
    invalidationCarteService.sendEventInvalidCards(invalidCartes);
  }

  public List<CartePapierEditique> findAll() {
    return cartePapierDao.findAll(CartePapierEditique.class);
  }

  public void dropCollection() {
    cartePapierDao.dropCollection(CartePapierEditique.class);
  }
}
