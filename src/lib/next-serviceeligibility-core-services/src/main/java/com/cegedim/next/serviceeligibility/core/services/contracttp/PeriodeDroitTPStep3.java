package com.cegedim.next.serviceeligibility.core.services.contracttp;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.FORMATTERSLASHED;
import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.SLASHED_FORMATTER;

import com.cegedim.next.serviceeligibility.core.mapper.MapperPeriodeDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractTPPeriode;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PeriodeDroitTPStep3 {

  private static final String PERIODE_SUPPRIME = "periode supprimé";

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTERSLASHED);

  public void extractAndUpdateFinPeriodesDroitContrat(
      List<PeriodeDroitContractTP> periodesDroitContrat, DomaineDroitForConsolidation domConso) {
    boolean tpOnline = domConso.isOnline();
    DomaineDroit domaineDroitDeclaration = domConso.getDomaineDroit();
    List<PeriodeDroitContractTP> removeList = new ArrayList<>();
    boolean tobreak = false;
    List<PeriodeDroitContractTP> periodToAdd = new ArrayList<>();
    // Traitement des périodes de fermeture (declPeriodeDebutFermeture ->
    // declPeriodeFinFermeture)
    for (PeriodeDroitContractTP periodeContrat : periodesDroitContrat) {
      if (domConso
          .getPeriodeDroitContractTP()
          .getTypePeriode()
          .equals(periodeContrat.getTypePeriode())) {
        ContractTPPeriode contractTPPeriode = new ContractTPPeriode(periodeContrat, formatter);
        log.debug(
            "tpOnline {} contratPeriodeDebutFormatDate {} contratPeriodeFinFormatDate {} declPeriodeDebutFormatDate {} declPeriodeFinFormatDate {} declPeriodeDebutFermeture {} declPeriodeFinFermeture {}",
            tpOnline,
            contractTPPeriode.getContratPeriodeDebutFormatDate(),
            contractTPPeriode.getContratPeriodeFinFormatDate(),
            domConso.getDeclPeriodeDebutFormatDate(),
            domConso.getDeclPeriodeFinFormatDate(),
            domConso.getDeclPeriodeDebutFermeture(),
            domConso.getDeclPeriodeFinFermeture());
        // si chevauchement entre la période du contrat et de la déclaration
        if ((contractTPPeriode.getContratPeriodeFinFormatDate() == null
                || !contractTPPeriode
                    .getContratPeriodeFinFormatDate()
                    .isBefore(domConso.getDeclPeriodeDebutFormatDate()))
            && !contractTPPeriode
                .getContratPeriodeDebutFormatDate()
                .isAfter(domConso.getDeclPeriodeDebutFormatDate())) {
          tobreak =
              chevauchementFinPeriode(
                  periodToAdd,
                  domConso,
                  periodeContrat,
                  contractTPPeriode.getContratPeriodeDebut(),
                  removeList,
                  domaineDroitDeclaration,
                  tpOnline);
        } else if (DateUtils.isReverseDate(
                domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())
            && domConso.getDeclPeriodeDebutFermetureFormatDate() != null
            && domConso.getDeclPeriodeFinFermetureFormatDate() != null
            && (contractTPPeriode.getContratPeriodeFinFormatDate() == null
                || !contractTPPeriode
                    .getContratPeriodeFinFormatDate()
                    .isBefore(domConso.getDeclPeriodeDebutFermetureFormatDate()))
            && !contractTPPeriode
                .getContratPeriodeDebutFormatDate()
                .isAfter(domConso.getDeclPeriodeFinFermetureFormatDate())) {
          isReverseDateDeclaration(domConso, periodeContrat, contractTPPeriode, removeList);
        } else if (tpOnline
            && DateUtils.isReverseDate(
                domConso.getDeclPeriodeDebutFermeture(), domConso.getDeclPeriodeFinFermeture())
            && DateUtils.betweenString(
                contractTPPeriode.getContratPeriodeDebut(),
                domConso.getDeclPeriodeDebut(),
                domConso.getDeclPeriodeFinFermeture())) {
          log.debug("mise à jour date fin online");
          periodeContrat.setPeriodeFin(domConso.getDeclPeriodeFin());
        } else if (!tpOnline
            && DateUtils.isReverseDate(domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())
            && DateUtils.isReverseDate(
                domConso.getDeclPeriodeDebutFermeture(), domConso.getDeclPeriodeFinFermeture())) {
          String contratPeriodeFermetureFin = periodeContrat.getPeriodeFinFermeture();
          if (DateUtils.betweenString(
                  domConso.getDeclPeriodeDebut(),
                  periodeContrat.getPeriodeDebut(),
                  periodeContrat.getPeriodeFin())
              || contratPeriodeFermetureFin != null
                  && DateUtils.betweenString(
                      domConso.getDeclPeriodeDebut(),
                      periodeContrat.getPeriodeDebut(),
                      contratPeriodeFermetureFin)) {
            // Cas d un forçage de fermeture de droit
            periodeContrat.setPeriodeFin(domConso.getDeclPeriodeFin());
            periodeContrat.setPeriodeFinFermeture(domConso.getDeclPeriodeFin());
            if (DateUtils.isReverseDate(
                periodeContrat.getPeriodeDebut(), periodeContrat.getPeriodeFin())) {
              log.debug(PERIODE_SUPPRIME);
              removeList.add(periodeContrat);
            }
          }
        }
      }
      if (tobreak) {
        break;
      }
    }
    periodesDroitContrat.addAll(periodToAdd);
    periodesDroitContrat.removeAll(removeList);
  }

  private static void isReverseDateDeclaration(
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      ContractTPPeriode contractTPPeriode,
      List<PeriodeDroitContractTP> removeList) {
    log.debug("periode de déclaration à l'envers");
    if (contractTPPeriode.getContratPeriodeFinFormatDate() == null
        || !contractTPPeriode
                .getContratPeriodeFinFormatDate()
                .isAfter(domConso.getDeclPeriodeFinFermetureFormatDate())
            && !contractTPPeriode
                .getContratPeriodeDebutFormatDate()
                .isBefore(domConso.getDeclPeriodeDebutFermetureFormatDate())) {
      log.debug(PERIODE_SUPPRIME);
      removeList.add(periodeContrat);
    } else {
      if (!contractTPPeriode
          .getContratPeriodeDebutFormatDate()
          .isBefore(domConso.getDeclPeriodeDebutFermetureFormatDate())) {
        periodeContrat.setPeriodeDebut(
            DateUtils.getStringDatePlusDays(
                domConso.getDeclPeriodeFinFermeture(), 1, DateUtils.SLASHED_FORMATTER));
        periodeContrat.setPeriodeFinFermeture(null);
      } else {
        // non couvert par TU
        periodeContrat.setPeriodeFin(
            DateUtils.getStringDatePlusDays(
                domConso.getDeclPeriodeDebutFermeture(), -1, DateUtils.SLASHED_FORMATTER));
        periodeContrat.setPeriodeFinFermeture(
            DateUtils.getMinDate(
                contractTPPeriode.getContratPeriodeFin(), domConso.getDeclPeriodeFinFermeture()));
      }
    }
  }

  private boolean chevauchementFinPeriode(
      List<PeriodeDroitContractTP> periodToAdd,
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      String contratPeriodeDebut,
      List<PeriodeDroitContractTP> removeList,
      DomaineDroit domaineDroitDeclaration,
      boolean tpOnline) {
    log.debug("chevauchement des périodes");

    // si pas de période de fermeture
    if (StringUtils.isBlank(domConso.getDeclPeriodeDebutFermeture())
        && StringUtils.isBlank(domConso.getDeclPeriodeFinFermeture())) {
      // si date début et fin de la déclaration inversées
      return isReverseDateWithoutFermeture(
          periodToAdd,
          domConso,
          periodeContrat,
          contratPeriodeDebut,
          removeList,
          domaineDroitDeclaration,
          tpOnline);
    }
    // BLUE-6183 - Cas des sans-effet pour cloturer un contrat
    else if (DateUtils.isReverseDate(domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())
        && DateUtils.isReverseDate(
            domConso.getDeclPeriodeDebutFermeture(), domConso.getDeclPeriodeFinFermeture())) {

      periodeContrat.setPeriodeFin(domConso.getDeclPeriodeFin());
      periodeContrat.setPeriodeFinFermeture(domConso.getDeclPeriodeFin());

      // Si la nouvelle periode est inverse alors on la cloture
      if (DateUtils.isReverseDate(
          periodeContrat.getPeriodeDebut(), periodeContrat.getPeriodeFin())) {
        removeList.add(periodeContrat);
        log.debug(PERIODE_SUPPRIME);
      }
    }
    // si les dates de fermeture ne sont pas inversées
    else if (!DateUtils.isReverseDate(
        domConso.getDeclPeriodeDebutFermeture(), domConso.getDeclPeriodeFinFermeture())) {
      return dateFermetureNonInverse(
          periodToAdd,
          domConso,
          periodeContrat,
          contratPeriodeDebut,
          removeList,
          domaineDroitDeclaration,
          tpOnline);
    }
    // cas où la date de fin online pose vraiment pb !
    // Attention au periodeContrat.getPeriodeFin à NULL -> ne sert peut être à rien
    // dernières jira en cours sur le sujet bouillant : [BLUE-5502, BLUE-5516]
    // Dans les cas tordus de la 5516 (resiliation avant context tier payant), la
    // finFermetureOnline n'est pas renseigner. Si jamais celle-ci doit etre
    // renseignee, le cas passe dans ce if et la finfermeture peut etre valorisee
    // ici (a valider).
    else if (tpOnline
        && !domConso.isFermetureOnOnline()
        && (periodeContrat.getPeriodeFin() == null
            || !DateUtils.betweenString(
                domConso.getDeclPeriodeFin(),
                periodeContrat.getPeriodeDebut(),
                periodeContrat.getPeriodeFin()))) {
      log.debug("mise à jour date fin online");
      periodeContrat.setPeriodeFin(domConso.getDeclPeriodeFin());
    } else if (tpOnline
        && domConso.isFermetureOnOnline()
        && (periodeContrat.getPeriodeFin() == null
            || DateUtils.before(
                domConso.getDeclPeriodeFin(), periodeContrat.getPeriodeFin(), SLASHED_FORMATTER))) {
      // cas de la 5894
      periodeContrat.setPeriodeFin(domConso.getDeclPeriodeFin());
    }
    return false;
  }

  private boolean dateFermetureNonInverse(
      List<PeriodeDroitContractTP> periodToAdd,
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      String contratPeriodeDebut,
      List<PeriodeDroitContractTP> removeList,
      DomaineDroit domaineDroitDeclaration,
      boolean tpOnline) {
    // si la periode est déjà comprise dedans, on ne la traite pas.
    if (tpOnline
        && DateUtils.isReverseDate(domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())
        && contratPeriodeDebut.equals(domConso.getDeclPeriodeDebutFermeture())) {
      removeList.add(periodeContrat);
      log.debug(PERIODE_SUPPRIME);
    } else {
      log.debug("updateClosedPeriod");
      updateClosedPeriod(
          domaineDroitDeclaration,
          periodToAdd,
          periodeContrat,
          domConso.getDeclPeriodeDebutFermeture(),
          domConso.getDeclPeriodeFinFermeture(),
          tpOnline ? TypePeriode.ONLINE : TypePeriode.OFFLINE);
      return tpOnline;
    }
    return false;
  }

  private boolean isReverseDateWithoutFermeture(
      List<PeriodeDroitContractTP> periodToAdd,
      DomaineDroitForConsolidation domConso,
      PeriodeDroitContractTP periodeContrat,
      String contratPeriodeDebut,
      List<PeriodeDroitContractTP> removeList,
      DomaineDroit domaineDroitDeclaration,
      boolean tpOnline) {
    if (DateUtils.isReverseDate(domConso.getDeclPeriodeDebut(), domConso.getDeclPeriodeFin())) {
      if (contratPeriodeDebut.equals(domConso.getDeclPeriodeDebut())) {
        log.debug(PERIODE_SUPPRIME);
        removeList.add(periodeContrat);
      } else {
        // pas de période de fermeture mais inversion date droit donc calcul de periode
        // fermeture
        log.debug("updateClosedPeriod");
        updateClosedPeriod(
            domaineDroitDeclaration,
            periodToAdd,
            periodeContrat,
            domConso.getDeclPeriodeDebutFermeturePlus1(),
            domConso.getDeclPeriodeFinFermetureMinus1(),
            tpOnline ? TypePeriode.ONLINE : TypePeriode.OFFLINE);
        return true;
      }
    }
    return false;
  }

  /**
   * met à jour la date de fin de la période et si l'on ferme en plein milieu, on en crée une
   * nouvelle période.
   *
   * @param domaineDroit domaine de droit utilisé pour la création de la période
   * @param periodToAdd liste des periodes a ajouter
   * @param periodInProgress periode en cours à modifier
   * @param typePeriode online ou offline
   */
  private void updateClosedPeriod(
      DomaineDroit domaineDroit,
      List<PeriodeDroitContractTP> periodToAdd,
      PeriodeDroitContractTP periodInProgress,
      String declarationPeriodeDateDebutFermeture,
      String declarationPeriodeDateFinFermeture,
      TypePeriode typePeriode) {
    String perDateDebut = periodInProgress.getPeriodeDebut();
    String perDateFin = periodInProgress.getPeriodeFin();

    String periodeFin = getPeriodeFin(perDateFin, declarationPeriodeDateDebutFermeture);
    periodInProgress.setPeriodeFin(periodeFin);
    if (StringUtils.isNotBlank(declarationPeriodeDateFinFermeture)) {
      periodInProgress.setPeriodeFinFermeture(declarationPeriodeDateFinFermeture);
    }

    String periodeDebut = getPeriodeDebut(perDateDebut, declarationPeriodeDateFinFermeture);
    if (!StringUtils.isEmpty(declarationPeriodeDateFinFermeture)
        && (StringUtils.isEmpty(perDateFin)
            || DateUtils.before(
                declarationPeriodeDateFinFermeture, perDateFin, DateUtils.SLASHED_FORMATTER))) {
      PeriodeDroitContractTP newPer =
          MapperPeriodeDroitContrat.createNewPeriodeDroit(
              domaineDroit, periodeDebut, perDateFin, typePeriode);
      periodToAdd.add(newPer);
    }
  }

  private static String getPeriodeDebut(String debut, String finFermeture) {
    if (finFermeture != null) {
      return DateUtils.getStringDatePlusDays(finFermeture, 1, DateUtils.SLASHED_FORMATTER);
    }
    return debut;
  }

  private static String getPeriodeFin(String fin, String debutFermeture) {
    if (debutFermeture != null) {
      return DateUtils.getStringDatePlusDays(debutFermeture, -1, DateUtils.SLASHED_FORMATTER);
    }
    return fin;
  }
}
