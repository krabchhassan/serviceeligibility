package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractTPPeriode;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public final class MapperPeriodeDroitContrat {

  private MapperPeriodeDroitContrat() {
    // mapper util
  }

  public static List<PeriodeDroitContractTP> createNewPeriode(
      DomaineDroitForConsolidation domConso) {
    List<PeriodeDroitContractTP> periodes = new ArrayList<>();
    if (!DateUtils.isReverseDate(domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())) {
      PeriodeDroitContractTP newPeriode =
          MapperPeriodeDroitContrat.createNewPeriodeDroit(
              domConso.getDomaineDroit(),
              domConso.getDeclPeriodeDebut(),
              domConso.getDeclPeriodeFin(),
              domConso.getPeriodeDroitContractTP().getTypePeriode());

      domConso.setPeriodeDroitContractTP(newPeriode);
      periodes.add(newPeriode);
    }
    return periodes;
  }

  public static PeriodeDroitContractTP createNewPeriodeDroit(
      DomaineDroit newDom,
      String declPeriodeDebut,
      String declPeriodeFin,
      TypePeriode typePeriode) {

    PeriodeDroitContractTP newPer = new PeriodeDroitContractTP();
    PeriodeDroit periodeDeclaration;

    if (TypePeriode.OFFLINE.equals(typePeriode)) {
      periodeDeclaration = newDom.getPeriodeDroit();
    } else { // if typePeriode == ONLINE
      periodeDeclaration = newDom.getPeriodeOnline();
    }

    newPer.setTypePeriode(typePeriode);
    newPer.setPeriodeDebut(declPeriodeDebut);
    newPer.setPeriodeFin(declPeriodeFin);

    newPer.setMotifEvenement(periodeDeclaration.getMotifEvenement());
    newPer.setLibelleEvenement(periodeDeclaration.getLibelleEvenement());
    newPer.setModeObtention(periodeDeclaration.getModeObtention());

    return newPer;
  }

  public static void deleteEmptyPeriodes(DomaineDroitContractTP contratDomToEdit) {
    Iterator<Garantie> itGarantie = contratDomToEdit.getGaranties().iterator();
    while (itGarantie.hasNext()) {
      Garantie garantie = itGarantie.next();
      Iterator<Produit> itProd = garantie.getProduits().iterator();
      while (itProd.hasNext()) {
        Produit produit = itProd.next();
        Iterator<ReferenceCouverture> itRef = produit.getReferencesCouverture().iterator();
        while (itRef.hasNext()) {
          ReferenceCouverture refCouverture = itRef.next();
          refCouverture
              .getNaturesPrestation()
              .removeIf(
                  naturePrestation -> CollectionUtils.isEmpty(naturePrestation.getPeriodesDroit()));
          if (CollectionUtils.isEmpty(refCouverture.getNaturesPrestation())) {
            itRef.remove();
          }
        }
        if (CollectionUtils.isEmpty(produit.getReferencesCouverture())) {
          itProd.remove();
        }
      }
      if (CollectionUtils.isEmpty(garantie.getProduits())) {
        itGarantie.remove();
      }
    }
  }

  public static boolean chevauchementPeriode(
      ContractTPPeriode contractTPPeriode,
      DomaineDroitForConsolidation domConso,
      boolean tpOnline) {
    return (domConso.getDeclPeriodeFinFormatDatePlus1() == null
            || !contractTPPeriode
                .getContratPeriodeDebutFormatDate()
                .isAfter(domConso.getDeclPeriodeFinFormatDatePlus1()))
        && ((contractTPPeriode.getContratPeriodeFinFormatDate() == null
                || !contractTPPeriode
                    .getContratPeriodeFinFormatDate()
                    .isBefore(domConso.getDeclPeriodeDebutFormatDateMinus1()))
            || (!tpOnline
                && DateUtils.isReverseDate(
                    contractTPPeriode.getContratPeriodeDebut(),
                    contractTPPeriode.getContratPeriodeFin())
                && contractTPPeriode.getContratPeriodeFermetureFin() != null
                && !contractTPPeriode
                    .getContratPeriodeFermetureFinFormatDate()
                    .isBefore(domConso.getDeclPeriodeDebutFormatDate())));
  }
}
