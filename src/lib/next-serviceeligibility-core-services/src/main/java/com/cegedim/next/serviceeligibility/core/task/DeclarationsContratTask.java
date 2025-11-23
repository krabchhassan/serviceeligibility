package com.cegedim.next.serviceeligibility.core.task;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * Tache qui génére les cartes demat / carte papier par bloc de contrat A ce niveau là, ce n'est que
 * de la création de sous tache par bloc de contrat + mise à jour historique execution par declarant
 */
public class DeclarationsContratTask extends RecursiveTask<BulkActions> {

  private final DataForJob620 dataForJob620;

  private final Declarant declarant;

  private final DeclarationConsolideService declarationConsolideService;

  private final InvalidationCarteService invalidationCarteService;

  private final BulkActions bulkActions;

  private final Map<String, List<Declaration>> mapDeclarations;

  public DeclarationsContratTask(
      DataForJob620 dataForJob620,
      Declarant declarant,
      DeclarationConsolideService declarationConsolideService,
      InvalidationCarteService invalidationCarteService,
      Map<String, List<Declaration>> mapDeclarations,
      BulkActions bulkActions) {
    this.dataForJob620 = dataForJob620;
    this.declarant = declarant;
    this.declarationConsolideService = declarationConsolideService;
    this.invalidationCarteService = invalidationCarteService;
    this.bulkActions = bulkActions;
    this.mapDeclarations = mapDeclarations;
  }

  @Override
  public BulkActions compute() {
    bulkActions.setCurrentThread(Thread.currentThread().getName());
    log.debug(
        "Thread courant {} from declaration {} to declaration {}, from contrat {} to contrat {}",
        bulkActions.getCurrentThread(),
        bulkActions.getLastIdDeclaration(),
        bulkActions.getFirstIdDeclaration(),
        bulkActions.getLastNumContrat(),
        bulkActions.getFirstNumContrat());
    try {
      for (Map.Entry<String, List<Declaration>> declaByContrat : mapDeclarations.entrySet()) {
        List<DeclarationConsolide> declarationConsolideList =
            declarationConsolideService.getDeclarationsConsolideesByAmcContrats(
                declaByContrat.getKey());
        List<CarteDemat> carteDematList =
            invalidationCarteService.getLastCartesByAmcContrats(declaByContrat.getKey());
        declarationConsolideService.processGroupedDeclarationsByContract(
            dataForJob620,
            bulkActions,
            declarant,
            declaByContrat.getValue(),
            declaByContrat.getKey(),
            declarationConsolideList,
            carteDematList);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      log.error(
          "Thread courant {}, reLaunch the batch  from declaration {} to declaration {}, from contrat {} to contrat {}",
          bulkActions.getCurrentThread(),
          bulkActions.getFirstIdDeclaration(),
          bulkActions.getLastIdDeclaration(),
          bulkActions.getFirstNumContrat(),
          bulkActions.getLastNumContrat());
      throw new ExceptionService(e);
    }
    return bulkActions;
  }
}
