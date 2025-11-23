package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Teletransmission;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public abstract class UniqueAccessPointTriHtpImpl implements UniqueAccessPointTriHTP {

  public void triHTP(List<ContratAIV6> contrats, UniqueAccessPointRequest requete) {
    Comparator<ContratAIV6> comparator =
        (ContratAIV6 c1, ContratAIV6 c2) -> triNir(c1, c2, requete);
    contrats.sort(comparator);
  }

  private int triNir(ContratAIV6 c1, ContratAIV6 c2, UniqueAccessPointRequest requete) {
    String nirCode = requete.getNirCode();
    if (nirCode == null) {
      return triC2S(c1, c2, requete);
    }
    LocalDate startDate = DateUtils.stringToDate(requete.getStartDate());
    LocalDate endDate = DateUtils.stringToDate(requete.getEndDate());

    boolean isC1Affiliated = isAffiliated(c1.getAssures(), nirCode, startDate, endDate);
    boolean isC2Affiliated = isAffiliated(c2.getAssures(), nirCode, startDate, endDate);
    if (isC1Affiliated && !isC2Affiliated) {
      return -1;
    } else {
      if (isC2Affiliated && !isC1Affiliated) {
        return 1;
      } else {
        return triC2S(c1, c2, requete);
      }
    }
  }

  private boolean isAffiliated(
      List<Assure> assures, String nir, LocalDate startDate, LocalDate endDate) {
    boolean isNirOfEnfant =
        assures.stream()
            .filter(assure -> Constants.QUALITE_E.equals(assure.getQualite().getCode()))
            .anyMatch(
                assure -> {
                  IdentiteContrat identite = assure.getIdentite();
                  String nirPrincipal =
                      identite.getNir() != null ? identite.getNir().getCode() : null;
                  return identite.getAffiliationsRO().stream()
                      .anyMatch(aff -> isAttachedToNir(nir, nirPrincipal, aff, startDate, endDate));
                });

    if (isNirOfEnfant) {
      // On vérifie maintenant s'il y a un autre assuré (non enfant) qui est aussi
      // affilié à ce nir
      return assures.stream()
          .filter(
              assure ->
                  Constants.QUALITE_A.equals(assure.getQualite().getCode())
                      || Constants.QUALITE_C.equals(assure.getQualite().getCode()))
          .anyMatch(
              assure -> {
                IdentiteContrat identite = assure.getIdentite();
                String nirPrincipal =
                    identite.getNir() != null ? identite.getNir().getCode() : null;
                return identite.getAffiliationsRO().stream()
                    .anyMatch(aff -> isAttachedToNir(nir, nirPrincipal, aff, startDate, endDate));
              });
    }

    // Sinon, comportement standard
    return assures.stream()
        .anyMatch(
            assure -> {
              String nirPrincipal =
                  assure.getIdentite().getNir() != null
                      ? assure.getIdentite().getNir().getCode()
                      : null;
              if (!CollectionUtils.isEmpty(assure.getIdentite().getAffiliationsRO())) {
                return assure.getIdentite().getAffiliationsRO().stream()
                    .anyMatch(aff -> isAttachedToNir(nir, nirPrincipal, aff, startDate, endDate));
              }
              return nir.equals(nirPrincipal);
            });
  }

  private boolean isAttachedToNir(
      String nir,
      String nirPrincipal,
      NirRattachementRO aff,
      LocalDate startDate,
      LocalDate endDate) {
    return (nir.equals(aff.getNir().getCode()) || nir.equals(nirPrincipal))
        && DateUtils.isOverlapping(
            DateUtils.stringToDate(aff.getPeriode().getDebut()),
            DateUtils.stringToDate(aff.getPeriode().getFin()),
            startDate,
            endDate);
  }

  private int triC2S(ContratAIV6 c1, ContratAIV6 c2, UniqueAccessPointRequest requete) {
    LocalDate startDate = DateUtils.stringToDate(requete.getStartDate());
    LocalDate endDate = DateUtils.stringToDate(requete.getEndDate());
    boolean isC1CMU = isCmu(c1, startDate, endDate);
    boolean isC2CMU = isCmu(c2, startDate, endDate);
    if (isC1CMU && !isC2CMU) {
      return -1;
    } else {
      if (isC2CMU && !isC1CMU) {
        return 1;
      } else {
        return triPrio(c1, c2, requete);
      }
    }
  }

  private boolean isCmu(ContratAIV6 c, LocalDate startDate, LocalDate endDate) {
    List<PeriodeContratCMUOuvert> cListCMU = c.getPeriodesContratCMUOuvert();
    if (!CollectionUtils.isEmpty(cListCMU)) {
      for (PeriodeContratCMUOuvert cmuPeriod : cListCMU) {
        Periode p = cmuPeriod.getPeriode();
        LocalDate start = DateUtils.stringToDate(p.getDebut());
        LocalDate end = DateUtils.stringToDate(p.getFin());
        if (DateUtils.isOverlapping(start, end, startDate, endDate)) {
          return true;
        }
      }
    }
    return false;
  }

  private int triPrio(ContratAIV6 c1, ContratAIV6 c2, UniqueAccessPointRequest requete) {
    int res = StringUtils.compare(c1.getOrdrePriorisation(), c2.getOrdrePriorisation());
    if (res != 0) {
      return res;
    } else {
      return triAssurPrinc(c1, c2, requete);
    }
  }

  private int triAssurPrinc(ContratAIV6 c1, ContratAIV6 c2, UniqueAccessPointRequest requete) {
    boolean isc1AssurPrinc = isSouscripteur(c1.getAssures(), requete.getNirCode());
    boolean isc2AssurPrinc = isSouscripteur(c2.getAssures(), requete.getNirCode());
    if (isc1AssurPrinc && !isc2AssurPrinc) {
      return -1;
    } else {
      if (isc2AssurPrinc && !isc1AssurPrinc) {
        return 1;
      } else {
        return triContratColl(c1, c2, requete);
      }
    }
  }

  private boolean isSouscripteur(List<Assure> assures, String nirCode) {
    return assures.stream()
        .filter(assure -> Boolean.TRUE.equals(assure.getIsSouscripteur()))
        .anyMatch(
            assure ->
                assure.getIdentite().getNir() != null
                    && assure.getIdentite().getNir().getCode().equals(nirCode));
  }

  private int triContratColl(ContratAIV6 c1, ContratAIV6 c2, UniqueAccessPointRequest requete) {
    boolean isc1ContratColl = !c1.getIsContratIndividuel();
    boolean isc2ContratColl = !c2.getIsContratIndividuel();
    if (isc1ContratColl && !isc2ContratColl) {
      return -1;
    } else {
      if (isc2ContratColl && !isc1ContratColl) return 1;
      else return triNoemie(c1, c2, requete);
    }
  }

  private int triNoemie(ContratAIV6 c1, ContratAIV6 c2, UniqueAccessPointRequest requete) {
    LocalDate startDate = DateUtils.stringToDate(requete.getStartDate());
    LocalDate endDate = DateUtils.stringToDate(requete.getEndDate());

    boolean isC1Noemise = isNoemise(c1, startDate, endDate);
    boolean isC2Noemise = isNoemise(c2, startDate, endDate);
    if (isC1Noemise && !isC2Noemise) {
      return -1;
    } else {
      if (isC2Noemise && !isC1Noemise) return 1;
      else return triNumContrat(c1, c2);
    }
  }

  private boolean isNoemise(ContratAIV6 c, LocalDate startDate, LocalDate endDate) {
    for (Assure assure : c.getAssures()) {
      if (assure != null && assure.getDigitRelation() != null) {
        List<Teletransmission> transmissions = assure.getDigitRelation().getTeletransmissions();
        if (!CollectionUtils.isEmpty(transmissions)
            && checkTeletransmission(startDate, endDate, transmissions)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean checkTeletransmission(
      LocalDate startDate, LocalDate endDate, List<Teletransmission> transmissions) {
    for (Teletransmission r : transmissions) {
      Periode p = r.getPeriode();
      LocalDate start = DateUtils.stringToDate(p.getDebut());
      LocalDate end = DateUtils.stringToDate(p.getFin());
      if (Boolean.TRUE.equals(r.getIsTeletransmission())
          && DateUtils.isOverlapping(start, end, startDate, endDate)) {
        return true;
      }
    }
    return false;
  }

  private int triNumContrat(ContratAIV6 c1, ContratAIV6 c2) {
    return c1.getNumero().compareTo(c2.getNumero());
  }
}
