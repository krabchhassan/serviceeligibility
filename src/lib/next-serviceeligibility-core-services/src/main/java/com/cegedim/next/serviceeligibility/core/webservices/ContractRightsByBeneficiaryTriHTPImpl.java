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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ContractRightsByBeneficiaryTriHTPImpl implements UniqueAccessPointTriHTP {

  public void triHTP(List<ContratAIV6> contrats, UniqueAccessPointRequest request) {
    Comparator<ContratAIV6> comparator =
        (ContratAIV6 c1, ContratAIV6 c2) -> triNir(c1, c2, request.getNirCode());
    contrats.sort(comparator);
  }

  private int triNir(ContratAIV6 c1, ContratAIV6 c2, String nirCode) {
    if (nirCode == null) {
      return triC2S(c1, c2);
    }
    boolean isC1Affiliated = isAffiliated(c1.getAssures(), nirCode);
    boolean isC2Affiliated = isAffiliated(c2.getAssures(), nirCode);

    if (isC1Affiliated && !isC2Affiliated) {
      return -1;
    }
    if (isC2Affiliated && !isC1Affiliated) {
      return 1;
    }
    return triC2S(c1, c2);
  }

  private boolean isAffiliated(List<Assure> assures, String nir) {
    boolean isNirOfEnfant =
        assures.stream()
            .filter(assure -> Constants.QUALITE_E.equals(assure.getQualite().getCode()))
            .anyMatch(
                assure -> {
                  IdentiteContrat identite = assure.getIdentite();
                  String nirPrincipal =
                      identite.getNir() != null ? identite.getNir().getCode() : null;
                  return identite.getAffiliationsRO().stream()
                      .anyMatch(aff -> isAttachedToNir(nir, nirPrincipal, aff));
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
                    .anyMatch(aff -> isAttachedToNir(nir, nirPrincipal, aff));
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
                    .anyMatch(aff -> isAttachedToNir(nir, nirPrincipal, aff));
              }
              return nir.equals(nirPrincipal);
            });
  }

  private boolean isAttachedToNir(String nir, String nirPrincipal, NirRattachementRO aff) {
    return (nir.equals(aff.getNir().getCode()) || nir.equals(nirPrincipal))
        && DateUtils.isPeriodeValide(
            DateUtils.stringToDate(aff.getPeriode().getDebut()),
            DateUtils.stringToDate(aff.getPeriode().getFin()));
  }

  private int triC2S(ContratAIV6 c1, ContratAIV6 c2) {
    boolean isC1CMU = isCmu(c1);
    boolean isC2CMU = isCmu(c2);
    if (isC1CMU && !isC2CMU) {
      return -1;
    }
    if (isC2CMU && !isC1CMU) {
      return 1;
    }
    return triPrio(c1, c2);
  }

  private boolean isCmu(ContratAIV6 c) {
    List<PeriodeContratCMUOuvert> cListCMU = c.getPeriodesContratCMUOuvert();
    if (!CollectionUtils.isEmpty(cListCMU)) {
      for (PeriodeContratCMUOuvert cmuPeriod : cListCMU) {
        Periode p = cmuPeriod.getPeriode();
        LocalDate start = DateUtils.stringToDate(p.getDebut());
        LocalDate end = DateUtils.stringToDate(p.getFin());
        if (DateUtils.isPeriodeValide(start, end)) {
          return true;
        }
      }
    }
    return false;
  }

  private int triPrio(ContratAIV6 c1, ContratAIV6 c2) {
    int res = StringUtils.compare(c1.getOrdrePriorisation(), c2.getOrdrePriorisation());
    if (res != 0) {
      return res;
    } else {
      return triAssurPrinc(c1, c2);
    }
  }

  private int triAssurPrinc(ContratAIV6 c1, ContratAIV6 c2) {
    boolean isc1AssurPrinc = hasSouscripteur(c1.getAssures());
    boolean isc2AssurPrinc = hasSouscripteur(c2.getAssures());
    if (isc1AssurPrinc && !isc2AssurPrinc) {
      return -1;
    } else {
      if (isc2AssurPrinc && !isc1AssurPrinc) return 1;
      return triContratColl(c1, c2);
    }
  }

  private boolean hasSouscripteur(List<Assure> assures) {
    return assures.stream().anyMatch(assure -> Boolean.TRUE.equals(assure.getIsSouscripteur()));
  }

  private int triContratColl(ContratAIV6 c1, ContratAIV6 c2) {
    boolean isc1ContratColl =
        c1.getContratCollectif() != null
            && StringUtils.isNotBlank(c1.getContratCollectif().getNumero());
    boolean isc2ContratColl =
        c2.getContratCollectif() != null
            && StringUtils.isNotBlank(c2.getContratCollectif().getNumero());
    if (isc1ContratColl && !isc2ContratColl) {
      return -1;
    }
    if (isc2ContratColl && !isc1ContratColl) {
      return 1;
    }
    return triNoemie(c1, c2);
  }

  private int triNoemie(ContratAIV6 c1, ContratAIV6 c2) {
    boolean isC1Noemise = isNoemise(c1);
    boolean isC2Noemise = isNoemise(c2);
    if (isC1Noemise && !isC2Noemise) {
      return -1;
    } else {
      if (isC2Noemise && !isC1Noemise) return 1;
      return triNumContrat(c1, c2);
    }
  }

  private boolean isNoemise(ContratAIV6 c) {
    for (Assure assure : c.getAssures()) {
      if (assure != null && assure.getDigitRelation() != null) {
        List<Teletransmission> transmissions = assure.getDigitRelation().getTeletransmissions();
        if (!CollectionUtils.isEmpty(transmissions)
            && periodeValideOnTransmissions(transmissions)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean periodeValideOnTransmissions(List<Teletransmission> transmissions) {
    for (Teletransmission r : transmissions) {
      Periode p = r.getPeriode();
      LocalDate start = DateUtils.stringToDate(p.getDebut());
      LocalDate end = DateUtils.stringToDate(p.getFin());
      if (Boolean.TRUE.equals(r.getIsTeletransmission()) && DateUtils.isPeriodeValide(start, end)) {
        return true;
      }
    }
    return false;
  }

  private int triNumContrat(ContratAIV6 c1, ContratAIV6 c2) {
    return c1.getNumero().compareTo(c2.getNumero());
  }
}
