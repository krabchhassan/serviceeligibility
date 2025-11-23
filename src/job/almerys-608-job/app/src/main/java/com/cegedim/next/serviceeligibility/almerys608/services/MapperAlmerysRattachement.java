package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsExtractions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapperAlmerysRattachement {

  public List<BulkObject> mapRattachement(
      Souscripteur souscripteur,
      List<InfosSouscripteur> infosSouscripteurList,
      List<Transcodage> lienJuridiqueTranscoList,
      List<Rejet> rejetsContratList,
      boolean isHtp) {
    List<BulkObject> rattachementList = new ArrayList<>();
    if (souscripteur != null) {
      infosSouscripteurList.forEach(
          infosSouscripteur -> {
            if (!souscripteur.getRefInterneOs().equals(infosSouscripteur.numeroPersonne())) {
              String type = isHtp ? infosSouscripteur.qualite() : infosSouscripteur.typeAssure();
              Optional<String> code =
                  lienJuridiqueTranscoList.stream()
                      .filter(t -> t.getCle().contains(type))
                      .map(Transcodage::getCodeTransco)
                      .findFirst();
              if (code.isPresent()) {
                Rattachement rattachement = new Rattachement();
                rattachement.setRefOsRattachant(
                    UtilService.resizeField(souscripteur.getRefInterneOs(), 30));
                rattachement.setRefOsRattache(
                    UtilService.resizeField(infosSouscripteur.numeroPersonne(), 30));
                rattachement.setNumeroContrat(infosSouscripteur.numeroContrat());
                rattachement.setLienJuridique(code.get());
                rattachementList.add(rattachement);
              } else {
                // rejet A06
                Rejet r = infosSouscripteur.rejet();
                r.setError(ConstantesRejetsExtractions.REJET_A06.getMessage());
                r.setCodeRejetTraces(ConstantesRejetsExtractions.REJET_A06.getCode());
                rejetsContratList.add(r);
              }
            }
          });
    }
    return rattachementList;
  }
}
