package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

public class MapperAlmerysCarence {
  public void mapCarences(
      TmpObject2 tmpObject2,
      Map<Pair<String, String>, BulkObject> carencesList,
      List<Transcodage> domaineDroitsTranscoList) {
    String refInterneOs = tmpObject2.getBeneficiaire().getNumeroPersonne();
    Pair<String, String> carenceKey =
        Pair.of(refInterneOs, tmpObject2.getDomaineDroit().getCodeExterneProduit());

    // todo a améliorer plus tard, car en multiproduit, risque de carences en double ...
    if (!CollectionUtils.isEmpty(tmpObject2.getDomaineDroit().getCarences())) {
      BulkObject carence =
          carencesList.computeIfAbsent(
              carenceKey,
              k -> {
                Carence c = new Carence();
                c.setNumeroContrat(UtilService.mapRefNumContrat(tmpObject2));
                c.setRefInterneOS(refInterneOs);
                c.setRefProduit(
                    UtilService.resizeField(
                        tmpObject2.getDomaineDroit().getCodeExterneProduit(), 80));
                List<CarenceInfo> carenceInfos = new ArrayList<>();
                c.setCarenceInfos(carenceInfos);
                return c;
              });

      List<CarenceInfo> carenceInfos = ((Carence) carence).getCarenceInfos();

      tmpObject2
          .getDomaineDroit()
          .getCarences()
          .forEach(
              c -> {
                CarenceInfo carenceInfo = new CarenceInfo();
                String code =
                    domaineDroitsTranscoList.stream()
                        .filter(t -> t.getCle().contains(c.getDomaineDroitCarence()))
                        .map(Transcodage::getCodeTransco)
                        .findFirst()
                        .orElse(tmpObject2.getContrat().getModePaiementPrestations());
                carenceInfo.setIdentColonne(code);
                carenceInfo.setDateDebutEffet(c.getPeriodeDebutCarence());
                carenceInfo.setDateFinEffet(c.getPeriodeFinCarence());
                carenceInfos.add(carenceInfo);
              });
    }
  }
}
