package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public class BusinessSortUtility {

  private BusinessSortUtility() {}

  private static final String PERIODE_DEBUT_DEFAUT = "0000-00-00";
  private static final String PERIODE_FIN_DEFAUT = "9999-99-99";

  private static int dateDebutCompare(String d1, String d2, String f1, String f2) {
    String debut1 = PERIODE_DEBUT_DEFAUT;
    String debut2 = PERIODE_DEBUT_DEFAUT;
    if (d1 != null) {
      debut1 = d1;
    }
    if (d2 != null) {
      debut2 = d2;
    }
    if (!debut2.equals(debut1)) {
      return debut2.compareTo(debut1);
    }
    String fin1 = PERIODE_FIN_DEFAUT;
    String fin2 = PERIODE_FIN_DEFAUT;
    if (f1 != null) {
      fin1 = f1;
    }
    if (f2 != null) {
      fin2 = f2;
    }
    return fin2.compareTo(fin1);
  }

  private static int dateDebutCompareWithCancelledPeriod(String d1, String d2) {
    return d2.compareTo(d1);
  }

  public static int periodeCompare(PeriodeCarence p1, PeriodeCarence p2) {
    return dateDebutCompare(p1.getDebut(), p2.getDebut(), p1.getFin(), p2.getFin());
  }

  public static int periodeCompareWithCancelledPeriod(Periode p1, Periode p2) {
    return dateDebutCompareWithCancelledPeriod(p1.getDebut(), p2.getDebut());
  }

  public static int periodeCompare(Periode p1, Periode p2) {
    return dateDebutCompare(p1.getDebut(), p2.getDebut(), p1.getFin(), p2.getFin());
  }

  public static int periodeCompare(PeriodeDestinataire p1, PeriodeDestinataire p2) {
    return dateDebutCompare(p1.getDebut(), p2.getDebut(), p1.getFin(), p2.getFin());
  }

  public static boolean periodeEquals(Periode periode1, Periode periode2) {
    return periodeEquals(
        periode1.getDebut(), periode2.getDebut(), periode1.getFin(), periode2.getFin());
  }

  private static boolean periodeEquals(String d1, String d2, String fin1, String fin2) {
    return d1.equals(d2) && Objects.equals(fin1, fin2);
  }

  /**
   * Tri un objet Liste de periode par la date de début si elle est renseignée
   *
   * @param periodes Liste de périodes à trier
   */
  public static void triListePeriode(List<Periode> periodes) {
    if (periodes != null) {
      periodes.sort(BusinessSortUtility::periodeCompare);
    }
  }

  public static void triListePeriodeContratCMUOuvert(List<PeriodeContratCMUOuvert> periodes) {
    if (periodes != null) {
      periodes.sort(
          (p1, p2) -> BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode()));
    }
  }

  /**
   * Tri un objet Liste de code/periode par la date de début si elle est renseignée
   *
   * @param codePeriodes Liste de code périodes à trier
   */
  public static void triListeCodePeriode(List<CodePeriode> codePeriodes) {
    if (codePeriodes != null) {
      codePeriodes.sort((p1, p2) -> periodeCompare(p1.getPeriode(), p2.getPeriode()));
    }
  }

  /**
   * Tri décroissant des periode d'affiliation RO
   *
   * @param affiliationsRO
   */
  public static void triListeAffiliationRO(List<NirRattachementRO> affiliationsRO) {
    if (!CollectionUtils.isEmpty(affiliationsRO)) {
      affiliationsRO.sort(
          (p1, p2) -> BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode()));
    }
  }

  public static void triListePeriodesSupension(List<PeriodeSuspension> list) {
    if (!CollectionUtils.isEmpty(list)) {
      list.sort(
          (p1, p2) ->
              BusinessSortUtility.periodeCompareWithCancelledPeriod(
                  p1.getPeriode(), p2.getPeriode()));
    }
  }

  public static void updateNirs(
      IdentiteContrat newIdentity, IdentiteContrat oldIdentity, Source source) {
    newIdentity = Util.assign(newIdentity, new IdentiteContrat());
    oldIdentity = Util.assign(oldIdentity, new IdentiteContrat());

    // Changement de NIR Principal
    Nir newNir = newIdentity.getNir();
    Nir oldNir = oldIdentity.getNir();
    if (newNir != null) {
      newIdentity.setNir(newNir);
    } else if (oldNir != null) {
      newIdentity.setNir(oldNir);
    }

    setAffiliationsRO(newIdentity, oldIdentity, source);
    triListeAffiliationRO(newIdentity.getAffiliationsRO());
  }

  public static void setAffiliationsRO(
      IdentiteContrat newIdentity, IdentiteContrat oldIdentity, Source source) {
    switch (source) {
      case SERVICE_PRESTATION, TDB_DECLARATION:
        // cf 7233 : Il faut bien garder les affiliation RO n'étant pas présente dans le contrat
        // réceptionné. Pourquoi ?
        //
        // Car un enfant peut être sur le contrat du père et de la mère avec des affiliation RO
        // différentes sur les 2 contrat, on ne peut donc pas supprimer les affiliation RO à chaque
        // réception de contrat.
        //
        // Il faut donc uniquement créer ou modifier les affiliation RO, et charge au BO contrat de
        // positionner une date de fin antérieure à la date de début si une affiliation TO ne doit
        // plus être utilisée.
        affiliationROAggregate(newIdentity, oldIdentity);
        break;
      case PREST_IJ, AUTRE:
      default:
        affiliationROGetOld(newIdentity, oldIdentity);
    }
  }

  private static void affiliationROGetOld(
      IdentiteContrat newIdentity, IdentiteContrat oldIdentity) {
    newIdentity.setAffiliationsRO(oldIdentity.getAffiliationsRO());
  }

  /** Modifie les anciennes periodes des nirs deja presents et ajoute les nouveaux nirs */
  private static void affiliationROAggregate(
      IdentiteContrat newIdentity, IdentiteContrat oldIdentity) {
    Map<Integer, List<NirRattachementRO>> oldGroupByEquals = new HashMap<>();
    Map<Integer, List<NirRattachementRO>> newGroupByEquals = new HashMap<>();
    groupByEquals(newGroupByEquals, newIdentity.getAffiliationsRO());
    groupByEquals(oldGroupByEquals, oldIdentity.getAffiliationsRO());

    List<NirRattachementRO> aggregated = new ArrayList<>();
    oldGroupByEquals.forEach(
        (key, nirs) -> {
          modifyAndAddNewNir(key, nirs, newGroupByEquals);
          aggregated.addAll(
              DateUtils.getObjPeriodsMerged(
                  nirs, NirRattachementRO::getPeriode, DateUtils.FORMATTER));
        });
    newGroupByEquals.forEach(
        (key, nirs) ->
            aggregated.addAll(
                DateUtils.getObjPeriodsMerged(
                    nirs, NirRattachementRO::getPeriode, DateUtils.FORMATTER)));

    newIdentity.setAffiliationsRO(aggregated);
  }

  /** */
  private static void modifyAndAddNewNir(
      Integer key,
      List<NirRattachementRO> nirs,
      Map<Integer, List<NirRattachementRO>> newGroupByEquals) {
    if (!newGroupByEquals.containsKey(key)) {
      return;
    }
    for (NirRattachementRO newNir : newGroupByEquals.get(key)) {
      boolean used = false;
      for (NirRattachementRO oldNir : nirs) {
        String oldMinusOne = DateUtils.dateMinusOneDay(oldNir.getPeriode().getDebut());
        if (DateUtils.isOverlapping(
            newNir.getPeriode().getDebut(),
            newNir.getPeriode().getFin(),
            oldMinusOne,
            oldNir.getPeriode().getFin())) {
          if (newNir.getPeriode().getFin() != null) {
            oldNir.getPeriode().setFin(newNir.getPeriode().getFin());
          } else {
            oldNir.getPeriode().setFinToNull();
          }
          used = true;
        }
      }

      if (!used) {
        nirs.add(newNir);
      }
    }

    newGroupByEquals.remove(key);
  }

  /**
   * Regroupe les NirRattachementRO via un hash partiel correspondant au nir et au rattachementRO
   * (on ne prend pas en compte la periode dans ce hash partiel)
   */
  private static void groupByEquals(
      Map<Integer, List<NirRattachementRO>> groupByEquals, List<NirRattachementRO> toGroup) {
    if (toGroup != null) {
      for (NirRattachementRO nirRattachementRO : toGroup) {
        if (nirRattachementRO.getPeriode().getFin() == null
            || nirRattachementRO
                    .getPeriode()
                    .getFin()
                    .compareTo(nirRattachementRO.getPeriode().getDebut())
                > 0) {
          int partialHash =
              Objects.hash(nirRattachementRO.getNir(), nirRattachementRO.getRattachementRO());
          groupByEquals.putIfAbsent(partialHash, new ArrayList<>());
          groupByEquals.get(partialHash).add(nirRattachementRO);
        }
      }
    }
  }

  /**
   * Tri décroissant des périodes de carences
   *
   * @param carences
   */
  public static void triListeCarences(List<CarenceDroit> carences) {
    if (!CollectionUtils.isEmpty(carences)) {
      carences.sort(
          (p1, p2) -> {
            int res = BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode());
            if (res != 0) {
              return res;
            } else {
              return p2.getCode().compareTo(p1.getCode());
            }
          });
    }
  }

  /**
   * Tri décroissant des période de teletransmission
   *
   * @param teletransmissions
   */
  public static void triListeTeletransmissions(List<Teletransmission> teletransmissions) {
    if (!CollectionUtils.isEmpty(teletransmissions)) {
      teletransmissions.sort(
          (p1, p2) -> BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode()));
    }
  }

  /**
   * Tri un objet Liste de destinataire prestation
   *
   * @param destinatairesPrestations Liste des destinataires de prestation
   */
  public static void triListeDestinatairesPrestationsV4(
      List<DestinatairePrestations> destinatairesPrestations) {
    if (destinatairesPrestations != null) {
      destinatairesPrestations.sort(
          (p1, p2) -> BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode()));
    }
  }

  /**
   * Tri un objet Liste de destinataire prestation
   *
   * @param destinatairesRelevePrestations Liste des destinataires de prestation
   */
  public static void triListeDestinatairesRelevePrestationsV5(
      List<DestinataireRelevePrestations> destinatairesRelevePrestations) {
    if (destinatairesRelevePrestations != null) {
      destinatairesRelevePrestations.sort(
          (p1, p2) -> BusinessSortUtility.periodeCompare(p1.getPeriode(), p2.getPeriode()));
    }
  }
}
