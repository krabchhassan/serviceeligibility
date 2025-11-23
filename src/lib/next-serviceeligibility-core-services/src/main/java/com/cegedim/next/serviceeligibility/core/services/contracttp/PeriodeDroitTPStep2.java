package com.cegedim.next.serviceeligibility.core.services.contracttp;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.FORMATTERSLASHED;

import com.cegedim.next.serviceeligibility.core.mapper.MapperPeriodeDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.services.pojo.CasDeclaration;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractTPPeriode;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PeriodeDroitTPStep2 {

  public boolean extractAndUpdatePeriodesDroitContrat(
      List<PeriodeDroitContractTP> periodesDroitContrat, DomaineDroitForConsolidation domConso) {
    boolean tpOnline = domConso.isOnline();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTERSLASHED);
    DomaineDroit domaineDroitDeclaration = domConso.getDomaineDroit();

    PeriodeDroitContractTP periodeToUpdate = null;
    List<PeriodeDroitContractTP> removeList = new ArrayList<>();
    for (PeriodeDroitContractTP periodeContrat : periodesDroitContrat) {
      if (domConso
          .getPeriodeDroitContractTP()
          .getTypePeriode()
          .equals(periodeContrat.getTypePeriode())) {
        ContractTPPeriode contractTPPeriode = new ContractTPPeriode(periodeContrat, formatter);
        // Si la période du contrat se chevauche avec celle de la déclaration (ou
        // contigüe),
        // alors il faut mettre à jour cette période
        // mais si on en trouve plusieurs, il faut mettre à jour uniquement la première
        // (en aggrégant les dates) et
        // supprimer les autres
        periodeToUpdate =
            chevauchementPeriode(
                domConso,
                periodeContrat,
                contractTPPeriode,
                periodeToUpdate,
                domaineDroitDeclaration,
                tpOnline,
                removeList);
      }
    }

    periodesDroitContrat.removeAll(removeList);
    return periodeToUpdate != null;
  }

  private PeriodeDroitContractTP chevauchementPeriode(
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      ContractTPPeriode contractTPPeriode,
      PeriodeDroitContractTP perToUpdate,
      DomaineDroit domaineDroitDeclaration,
      boolean tpOnline,
      List<PeriodeDroitContractTP> removeList) {
    if (MapperPeriodeDroitContrat.chevauchementPeriode(contractTPPeriode, domConso, tpOnline)) {
      updateOnOldDecl(domConso, periodeContrat, tpOnline);

      if (perToUpdate == null) {
        perToUpdate =
            getPeriodeDroitToUpdate(
                domConso, periodeContrat, contractTPPeriode, domaineDroitDeclaration, tpOnline);
      } else {
        updatePeriodeDroitAndRemoveIt(
            domConso,
            periodeContrat,
            contractTPPeriode,
            perToUpdate,
            domaineDroitDeclaration,
            tpOnline,
            removeList);
      }
    }
    return perToUpdate;
  }

  /**
   * Cree les periodes fermeture quand elles n existent pas lors de la reception d une declaration
   * de fermeture
   */
  private void updateOnOldDecl(
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      boolean isOnline) {
    boolean isFermeture =
        CasDeclaration.FERMETURE.equals(domConso.getCasDeclaration())
            || CasDeclaration.TFD.equals(domConso.getCasDeclaration());
    if (isFermeture
        && !isOnline
        && domConso.getDeclPeriodeDebutFermeture() == null
        && domConso.getDeclPeriodeFinFermeture() == null) {
      DomaineDroit domaineDroit = domConso.getDomaineDroit();
      PeriodeDroit periodeDroitOff = domaineDroit.getPeriodeDroit();
      String finPlus1 =
          DateUtils.datePlusOneDay(periodeDroitOff.getPeriodeFin(), DateUtils.SLASHED_FORMATTER);
      String oldFin = periodeContrat.getPeriodeFin();
      periodeDroitOff.setPeriodeFermetureDebut(finPlus1);
      periodeDroitOff.setPeriodeFermetureFin(oldFin);
      domConso.setDomaineDroit(domaineDroit);
    }
  }

  private PeriodeDroitContractTP getPeriodeDroitToUpdate(
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      ContractTPPeriode contractTPPeriode,
      DomaineDroit domaineDroitDeclaration,
      boolean tpOnline) {
    if (domConso
        .getDeclPeriodeDebutFormatDate()
        .isBefore(contractTPPeriode.getContratPeriodeDebutFormatDate())) {
      periodeContrat.setPeriodeDebut(domConso.getDeclPeriodeDebut());
    }

    if (!DateUtils.isReverseDate(domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())
        && shouldOverwriteFin(
            domConso.getDeclPeriodeFinFormatDate(),
            contractTPPeriode.getContratPeriodeFinFormatDate(),
            domConso.getDeclPeriodeDebutFormatDate(),
            contractTPPeriode.getContratPeriodeDebutFormatDate())) {

      periodeContrat.setPeriodeFin(domConso.getDeclPeriodeFin());
      periodeContrat.setPeriodeFinFermeture(null);
    }
    setPeriodeDroit(domaineDroitDeclaration, periodeContrat, tpOnline);
    return periodeContrat;
  }

  private boolean shouldOverwriteFin(
      LocalDate declPeriodeFinFormatDate,
      LocalDate contratPeriodeFinFormatDate,
      LocalDate declPeriodeDebutFormatDate,
      LocalDate contratPeriodeDebutFormatDate) {
    if (declPeriodeFinFormatDate == null
        || contratPeriodeFinFormatDate != null
            && declPeriodeFinFormatDate.isAfter(contratPeriodeFinFormatDate)) {
      return true;
    }

    return declPeriodeDebutFormatDate.equals(contratPeriodeDebutFormatDate)
        && !declPeriodeFinFormatDate.isBefore(contratPeriodeDebutFormatDate)
        && (contratPeriodeFinFormatDate == null
            || declPeriodeFinFormatDate.isAfter(contratPeriodeFinFormatDate));
  }

  private void updatePeriodeDroitAndRemoveIt(
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      ContractTPPeriode contractTPPeriode,
      PeriodeDroitContractTP periodeToUpdate,
      DomaineDroit domaineDroitDeclaration,
      boolean tpOnline,
      List<PeriodeDroitContractTP> removeList) {
    if (contractTPPeriode
        .getContratPeriodeDebutFormatDate()
        .isBefore(domConso.getDeclPeriodeDebutFormatDate())) {
      periodeToUpdate.setPeriodeDebut(contractTPPeriode.getContratPeriodeDebut());
    }
    if (contractTPPeriode.getContratPeriodeFinFormatDate() == null
        || (domConso.getDeclPeriodeFinFormatDate() != null
            && contractTPPeriode
                .getContratPeriodeFinFormatDate()
                .isAfter(domConso.getDeclPeriodeFinFormatDate()))) {
      periodeToUpdate.setPeriodeFin(contractTPPeriode.getContratPeriodeFin());
    }
    setPeriodeDroit(domaineDroitDeclaration, periodeToUpdate, tpOnline);
    removeList.add(periodeContrat);
  }

  private void setPeriodeDroit(
      DomaineDroit domaineDroit, PeriodeDroitContractTP periodeDroit, boolean isOnline) {
    PeriodeDroit periodeDeclaration =
        isOnline ? domaineDroit.getPeriodeOnline() : domaineDroit.getPeriodeDroit();
    periodeDroit.setMotifEvenement(periodeDeclaration.getMotifEvenement());
    periodeDroit.setLibelleEvenement(periodeDeclaration.getLibelleEvenement());
    periodeDroit.setModeObtention(periodeDeclaration.getModeObtention());
  }
}
