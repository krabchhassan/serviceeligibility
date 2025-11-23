package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.AdresseAdherent;
import com.cegedim.next.serviceeligibility.almerys608.model.InfoCentreGestion;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.AdresseAvecFixe;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class MapperAlmerysAdresse {

  public List<InfoCentreGestion> mapInfoCentreGestions(TmpObject2 tmpObject2, Pilotage pilotage) {
    BeneficiaireAlmerys beneficiaire = tmpObject2.getBeneficiaire();
    boolean isHTP = UtilService.isHTP(tmpObject2);
    return beneficiaire.getAdresses().stream()
        .filter(
            adresse ->
                Constants.ADRESSE_TYPE_GESTIONNAIRE.equals(adresse.getTypeAdresse().getType()))
        .map(
            adresseBenef -> {
              InfoCentreGestion infoCentreGestion = new InfoCentreGestion();
              infoCentreGestion.setRefInterneCG(
                  UtilService.resizeField(
                      isHTP
                          ? adresseBenef.getCodeInterne()
                          : tmpObject2.getContrat().getGestionnaire(),
                      15));
              List<String> infosCarte = new ArrayList<>();
              tmpObject2
                  .getInfosCarteTP()
                  .forEach(
                      infoCarte -> {
                        if (StringUtils.isNotBlank(infoCarte)) {
                          infosCarte.add(StringUtils.substring(infoCarte, 0, 49));
                        }
                      });
              infoCentreGestion.setInfoCartes(infosCarte);
              infoCentreGestion.setTypeGestionnaire(
                  UtilService.resizeField(
                      pilotage.getCaracteristique().getTypeGestionnaireBO(), 50));
              infoCentreGestion.setGestionnaireContrat(
                  UtilService.resizeField(
                      isHTP
                          ? adresseBenef.getNomCommercial()
                          : pilotage.getCaracteristique().getLibelleGestionnaireBO(),
                      30));
              infoCentreGestion.setAdresseCG(mapAdresseCG(adresseBenef));
              return infoCentreGestion;
            })
        .toList();
  }

  public List<AdresseAdherent> mapAdressesAdherent(TmpObject2 tmpObject2) {
    BeneficiaireAlmerys beneficiaire = tmpObject2.getBeneficiaire();
    return beneficiaire.getAdresses().stream()
        .filter(
            adresse -> Constants.ADRESSE_TYPE_ADHERENT.equals(adresse.getTypeAdresse().getType()))
        .map(
            adresseBenef -> {
              AdresseAdherent adresseAdherent = new AdresseAdherent();
              adresseAdherent.setRefInterneOS(beneficiaire.getNumeroPersonne());
              adresseAdherent.setNumeroContrat(UtilService.mapRefNumContrat(tmpObject2));
              adresseAdherent.setAdresse(mapAdresseAvecFixe(adresseBenef));
              return adresseAdherent;
            })
        .toList();
  }

  private Adresse mapAdresseCG(AdresseAlmerys adresseBenefAlmerys) {
    Adresse adresse = new Adresse();
    UtilService.fillAdressLine(adresse, adresseBenefAlmerys, 100);
    return adresse;
  }

  private AdresseAvecFixe mapAdresseAvecFixe(AdresseAlmerys adresseBenefAlmerys) {
    AdresseAvecFixe adresseAvecFixe = new AdresseAvecFixe();
    UtilService.fillAdressLine(adresseAvecFixe, adresseBenefAlmerys);
    adresseAvecFixe.setFixe(adresseBenefAlmerys.getFixe());
    return adresseAvecFixe;
  }
}
