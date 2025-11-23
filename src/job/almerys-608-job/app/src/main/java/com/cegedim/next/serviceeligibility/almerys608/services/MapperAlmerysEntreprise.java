package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.InfoEntreprise;
import com.cegedim.next.serviceeligibility.almerys608.model.InfoSite;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class MapperAlmerysEntreprise {

  public InfoEntreprise mapEntreprise(TmpObject2 tmpObject2, Pilotage pilotage) {
    Contrat contrat = tmpObject2.getContrat();
    if (StringUtils.isAllBlank(
        contrat.getNumeroExterneContratCollectif(), contrat.getNumeroContratCollectif())) {
      return null;
    }
    InfoEntreprise infoEntreprise = new InfoEntreprise();
    String num = UtilService.mapNumContratCollectif(tmpObject2, pilotage);
    infoEntreprise.setRefEntreprise(
        UtilService.resizeField(
            UtilService.isHTP(tmpObject2) ? contrat.getIdentifiantCollectivite() : num, 15));
    infoEntreprise.setNumContratCollectif(List.of(UtilService.resizeField(num, 30)));
    infoEntreprise.setInfoSites(mapInfoSites(tmpObject2, pilotage));
    return infoEntreprise;
  }

  private List<InfoSite> mapInfoSites(TmpObject2 tmpObject2, Pilotage pilotage) {
    String refSite = mapRefSite(tmpObject2.getContrat(), pilotage);
    String numEntreprise = UtilService.mapNumContratCollectif(tmpObject2, pilotage);

    return tmpObject2.getBeneficiaire().getAdresses().stream()
        .filter(adresse -> "EN".equals(adresse.getTypeAdresse().getType()))
        .map(
            adresse -> {
              InfoSite infoSite = new InfoSite();
              infoSite.setRefSite(UtilService.resizeField(refSite, 15));
              infoSite.setRefEntreprise(numEntreprise);
              infoSite.setAdresse(mapAdresse(adresse));
              return infoSite;
            })
        .toList();
  }

  private Adresse mapAdresse(AdresseAlmerys adresse) {
    Adresse newAdresse = new Adresse();
    UtilService.fillAdressLine(newAdresse, adresse);
    return newAdresse;
  }

  private String mapRefSite(Contrat contrat, Pilotage pilotage) {
    if (Boolean.TRUE.equals(pilotage.getCaracteristique().getNumExterneContratCollectif())
            && contrat.getNumeroContratCollectif() != null
        || contrat.getNumeroExterneContratCollectif() != null) {
      return contrat.getGroupeAssures();
    }
    return null;
  }
}
