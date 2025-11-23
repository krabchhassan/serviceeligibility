package com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.mongodb.client.ClientSession;
import java.util.Collection;
import java.util.List;

public interface DeclarationConsolideService {

  Collection<DeclarationConsolide> saveDeclarationConsolides(
      List<DeclarationConsolide> declarationConsolides,
      HistoriqueExecutions620 histo,
      String codeClient);

  void deleteAll();

  void processGroupedDeclarationsByContract(
      DataForJob620 dataForJob620,
      BulkActions bulkActions,
      Declarant declarant,
      List<Declaration> declarations,
      String numContrat,
      List<DeclarationConsolide> existingDeclarationConsos,
      List<CarteDemat> existingCarteDemats);

  List<DeclarationConsolide> generateDeclarationConsolides(
      List<Declaration> declarations,
      DataForJob620 dataForJob620,
      List<String> servicesWanted,
      BulkActions bulkActions,
      boolean toBeIgnore);

  Collection<DeclarationConsolide> bulkSaveUpdateDelete(
      BulkActions bulkActions, String codeClient, ClientSession session);

  List<DeclarationConsolide> getDeclarationsConsolideesByAmcContrats(String amcContracts);
}
