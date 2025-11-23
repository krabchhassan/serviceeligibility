package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class MapperAlmerysContrat {

  public Contrat mapContrat(TmpObject2 tmpObject2, Pilotage pilotage) {
    com.cegedim.next.serviceeligibility.core.model.domain.Contrat contrat = tmpObject2.getContrat();
    Contrat newContrat = new Contrat();
    boolean isHTP = UtilService.isHTP(tmpObject2);
    String num = UtilService.mapNumContratCollectif(tmpObject2, pilotage);
    newContrat.setNumeroContrat(UtilService.mapRefNumContrat(tmpObject2));
    newContrat.setNumeroContratFichier(UtilService.mapNumContratIndividuel(tmpObject2, pilotage));
    newContrat.setEtatContrat(mapEtatContrat(tmpObject2));
    newContrat.setDateImmatriculation(mapDateImmat(contrat));
    newContrat.setRefEntreprise(isHTP ? contrat.getIdentifiantCollectivite() : num);
    newContrat.setRefSite(mapRefSite(contrat, pilotage));
    newContrat.setNumeroContratCollectif(num);
    newContrat.setRefInterneCG(mapRefCG(contrat, tmpObject2.getBeneficiaire(), isHTP));
    return newContrat;
  }

  private String mapRefCG(
      com.cegedim.next.serviceeligibility.core.model.domain.Contrat contrat,
      BeneficiaireAlmerys benef,
      boolean isHTP) {
    if (!isHTP) {
      return contrat.getGestionnaire();
    }

    if (!CollectionUtils.isEmpty(benef.getAdresses())) {
      // A VOIR par contrat dans le fichier on a bien une seule ref CG donc on prend la 1er adresse
      return benef.getAdresses().stream()
          .filter(adresse -> "GE".equals(adresse.getTypeAdresse().getType()))
          .findFirst()
          .map(AdresseAlmerys::getCodeInterne)
          .orElse(null);
    }
    return null;
  }

  private String mapRefSite(
      com.cegedim.next.serviceeligibility.core.model.domain.Contrat contrat, Pilotage pilotage) {
    if (Boolean.TRUE.equals(pilotage.getCaracteristique().getNumExterneContratCollectif())
            && contrat.getNumeroContratCollectif() != null
        || contrat.getNumeroExterneContratCollectif() != null) {
      return contrat.getGroupeAssures();
    }
    return null;
  }

  private String mapDateImmat(
      com.cegedim.next.serviceeligibility.core.model.domain.Contrat contrat) {
    LocalDate parsedDate = DateUtils.stringToDate(contrat.getDateSouscription());
    return parsedDate != null ? DateUtils.SLASHED_FORMATTER.format(parsedDate) : null;
  }

  private String mapEtatContrat(TmpObject2 tmpObject2) {
    if (Constants.CODE_ETAT_INVALIDE.equals(tmpObject2.getCodeEtat())
        || (Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())
            && tmpObject2.getContrat().getDateResiliation() != null)) {
      return "FE";
    } else if (Constants.CODE_ETAT_VALIDE.equals(tmpObject2.getCodeEtat())) {
      return "OU";
    }
    return null;
  }
}
