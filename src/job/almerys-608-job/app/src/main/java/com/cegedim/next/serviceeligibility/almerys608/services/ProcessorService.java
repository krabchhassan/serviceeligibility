package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.almerys608.utils.EnumTempTable;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class ProcessorService {
  private final MappersAlmerys mappersAlmerys;
  private final DomainService domainService;
  private final SouscripteurService souscripteurService;

  public void actionFinBoucle1(
      BulkAndList bulkAndList,
      List<Transcodage> lienJuridiqueTranscoList,
      String currentNumeroContrat,
      boolean forceSaveBulk,
      HistoriqueExecution608 histoPilotage,
      boolean isHtp) {
    bulkAndList
        .bulkList()
        .addMulti(EnumTempTable.SERVICE_TP.name(), bulkAndList.serviceTPsContrat().values());
    bulkAndList.serviceTPsContrat().clear();
    Souscripteur souscripteur =
        souscripteurService.findSouscripteur(
            bulkAndList.infosSouscripteurList(), currentNumeroContrat);
    bulkAndList.bulkList().addOne(EnumTempTable.SOUSCRIPTEUR.name(), souscripteur);
    List<BulkObject> rattachementList =
        mappersAlmerys
            .mapperAlmerysRattachement()
            .mapRattachement(
                souscripteur,
                bulkAndList.infosSouscripteurList(),
                lienJuridiqueTranscoList,
                bulkAndList.rejetBloquantsList(),
                isHtp);
    if (!CollectionUtils.isEmpty(rattachementList)) {
      bulkAndList.bulkList().addMulti(EnumTempTable.RATTACHEMENT.name(), rattachementList);
    }
    bulkAndList
        .bulkList()
        .addMulti(EnumTempTable.CARENCE.name(), bulkAndList.carencesList().values());
    bulkAndList.carencesList().clear();

    if (!bulkAndList.produitsToCheckList().isEmpty()) {
      mappersAlmerys
          .mapperAlmerysProduit()
          .mapProduitStep2(
              bulkAndList.produitsToCheckList(),
              bulkAndList.produitsCheckedList(),
              bulkAndList.rejetNonBloquantsList());
      bulkAndList.produitsToCheckList().clear();
      bulkAndList
          .bulkList()
          .addMulti(
              EnumTempTable.PRODUIT.name(), new ArrayList<>(bulkAndList.produitsCheckedList()));
    }

    controlProduitsExclus(
        bulkAndList.rejetsProduitsExclusList(),
        bulkAndList.produitsCheckedList(),
        bulkAndList.beneficiariesList(),
        bulkAndList.rejetBloquantsList(),
        bulkAndList.rejetNonBloquantsList());
    bulkAndList
        .bulkList()
        .addMulti(
            EnumTempTable.REJET_NON_BLOQUANT.name(),
            new ArrayList<>(bulkAndList.rejetNonBloquantsList()));
    bulkAndList.rejetNonBloquantsList().clear();
    controlC17(bulkAndList.infosSouscripteurList(), bulkAndList.rejetBloquantsList());

    bulkAndList.infosSouscripteurList().clear();
    bulkAndList
        .bulkList()
        .addMulti(
            EnumTempTable.BENEFICIAIRE.name(), new ArrayList<>(bulkAndList.beneficiariesList()));
    bulkAndList.beneficiariesList().clear();
    bulkAndList
        .bulkList()
        .addMulti(EnumTempTable.REJET.name(), new ArrayList<>(bulkAndList.rejetBloquantsList()));
    bulkAndList.rejetBloquantsList().clear();
    bulkAndList
        .bulkList()
        .addMulti(
            EnumTempTable.REJET_PRODUIT_EXCLU.name(),
            new ArrayList<>(bulkAndList.rejetsProduitsExclusList()));
    bulkAndList.rejetsProduitsExclusList().clear();
    bulkAndList.produitsCheckedList().clear();

    domainService.saveBulks(bulkAndList.bulkList(), forceSaveBulk, histoPilotage);

    bulkAndList.numeroPersonneList().clear();
  }

  private void controlC17(
      List<InfosSouscripteur> infosSouscripteurList, List<Rejet> rejetsContratList) {
    infosSouscripteurList.forEach(
        infosSouscripteur -> {
          if (rejetsContratList.stream()
                  .map(Rejet::getNumContrat)
                  .toList()
                  .contains(infosSouscripteur.numeroContrat())
              && !rejetsContratList.stream()
                  .map(Rejet::getRefInterneOS)
                  .toList()
                  .contains(infosSouscripteur.numeroPersonne())) {
            Rejet r = infosSouscripteur.rejet();
            r.setCodeRejetTraces(ConstantesRejetsConsolidations.REJET_C17.getCode());
            r.setError(ConstantesRejetsConsolidations.REJET_C17.getMessage());
            rejetsContratList.add(r);
          }
        });
  }

  public void controlProduitsExclus(
      List<Rejet> rejetsProduitsExclusList,
      List<Produit> produitsCheckedList,
      List<Beneficiaire> beneficiariesList,
      List<Rejet> rejetsContratList,
      List<Rejet> rejetNonBloquantBulk) {
    rejetsProduitsExclusList.forEach(
        rejetProduitExclu ->
            beneficiariesList.stream()
                .filter(
                    benef -> benef.getRefInterneOS().equals(rejetProduitExclu.getRefInterneOS()))
                .findFirst()
                .ifPresent(
                    benef ->
                        produitsCheckedList.stream()
                            .filter(
                                produit ->
                                    produit.getRefInterneOS().equals(benef.getRefInterneOS()))
                            .findAny()
                            .ifPresentOrElse(
                                produit -> rejetNonBloquantBulk.add(rejetProduitExclu),
                                () -> rejetsContratList.add(rejetProduitExclu))));
  }

  public <T> List<T> getAll(String collectionName, Class<T> objectClass) {
    return domainService.getAll(collectionName, objectClass);
  }
}
