package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineDroitBuffer;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import com.cegedim.next.serviceeligibility.core.utils.*;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

@Service
public class DomaineServiceImpl implements DomaineService {

  /**
   * Determine la validité de la liste des domaines d'un contrat. Retourne vide si pas d'erreur, le
   * message d'erreur sinon
   *
   * @param domaines la liste des domaines du contrat par code domaine
   * @return String vide si pas d'erreur, le contenu de l'erreur sinon
   */
  @Override
  @ContinueSpan(log = "validateDomainesConsolides")
  public String validateDomainesConsolides(Collection<DomaineDroitBuffer> domaines) {
    StringBuilder erreur = new StringBuilder();
    for (DomaineDroitBuffer domaine : domaines) {
      List<String> listPriorites = domaine.getPrioritesDomaine();
      String codeDomaine = domaine.getProduit().getCode();
      Set<String> listPrioritesStrings = new HashSet<>(listPriorites);
      if (listPriorites.size() != listPrioritesStrings.size()) {
        erreur =
            new StringBuilder(
                ConstantesRejetsConsolidations.REJET_C02.toString(
                    codeDomaine + " : " + listPriorites));
        break;
      }

      List<String> listUnites = domaine.getUnitesDomaine();
      Set<String> listUnitesStrings = new HashSet<>(listUnites);
      if (listUnitesStrings.size() != 1 && !listUnitesStrings.contains(TauxConstants.U_TEXT)
          || listUnitesStrings.contains(TauxConstants.EMPTY_FIELD)) {
        erreur =
            new StringBuilder(
                ConstantesRejetsConsolidations.REJET_C14.toString(
                    codeDomaine + " : " + listUnitesStrings));
        break;
      } else {
        String unite = listUnitesStrings.iterator().next();
        List<String> listTaux = domaine.getTauxDomaine();
        Set<String> listTauxStrings = new HashSet<>(listTaux);
        if (listUnitesStrings.contains(TauxConstants.U_TEXT)) {
          if (listTauxStrings.size() != 1 || listTauxStrings.contains(TauxConstants.EMPTY_FIELD)) {
            if (listTauxStrings.contains(TauxConstants.EMPTY_FIELD)) {
              erreur =
                  new StringBuilder(
                      ConstantesRejetsConsolidations.REJET_C14.toString(
                          codeDomaine + " : " + listTauxStrings));
              break;
            } else {
              Set<String> listeUniteWithoutXX =
                  listUnites.stream()
                      .filter(u -> !TauxConstants.U_TEXT.equals(u))
                      .collect(Collectors.toSet());
              Set<String> listeTauxWithoutNC =
                  listTaux.stream()
                      .filter(t -> !TauxConstants.T_NON_COUVERT.equals(t))
                      .collect(Collectors.toSet());
              if (listeUniteWithoutXX.size() != 1 && listeTauxWithoutNC.size() != 1) {
                erreur =
                    new StringBuilder(
                        ConstantesRejetsConsolidations.REJET_C14.toString(
                            codeDomaine + " : " + listeTauxWithoutNC));
                break;
              }
            }
          }
        } else {
          for (String taux : listTauxStrings) {
            if (!NumberUtils.isParsable(taux)) {
              erreur.append(
                  ConstantesRejetsConsolidations.REJET_C16.toString(
                      codeDomaine + " - " + unite + " : " + taux));
            }
          }
        }
      }
    }
    return erreur.toString();
  }

  /**
   * Met à jours la liste des domaines en ajoutant les informations nécessaire à l'édition de carte
   * TP
   *
   * @param listeDomaines la liste des domaines du contrat
   * @return liste des domaines enrichi du contrat
   */
  @ContinueSpan(log = "updateDomainesConsolides")
  public List<DomaineDroit> updateDomainesConsolides(Collection<DomaineDroitBuffer> listeDomaines) {
    List<DomaineDroit> listeDomainesEnrichis = new ArrayList<>();
    for (DomaineDroitBuffer domaine : listeDomaines) {
      listeDomainesEnrichis.add(updateDomaineCarteDemat(domaine));
    }
    return listeDomainesEnrichis.isEmpty() ? null : listeDomainesEnrichis;
  }

  @ContinueSpan(log = "updateDomaineCarteDemat")
  public DomaineDroit updateDomaineCarteDemat(DomaineDroitBuffer domaine) {
    DomaineDroit domaineEnrichi = new DomaineDroit(domaine.getProduit());
    List<String> listUnites = domaine.getUnitesDomaine();
    boolean listUnitesContainsOnlyXX = listUnites.stream().allMatch(TauxConstants.U_TEXT::equals);
    ModeAssemblage modeAssemblage = domaine.getModeAssemblagePlusPrioritaire();
    // La liste d'unite est remplie du domaine ayant le nombre le plus grand en
    // priorite au nombre le plus petit. L'unite à l'index 0 est donc l'unite du
    // domaineDroit avec la priorité la plus basse.
    int index = listUnites.size() - 1;
    String unite = listUnites.get(index);
    if (TauxConstants.U_TEXT.equals(unite) && !listUnitesContainsOnlyXX) {
      for (int i = index - 1; i >= 0; i--) {
        if (!TauxConstants.U_TEXT.equals(listUnites.get(i))) {
          unite = listUnites.get(i);
          break;
        }
      }
    }
    List<String> listTaux = domaine.getTauxDomaine();
    String taux = TauxUtils.getTauxRemboursement(listTaux, unite, modeAssemblage);
    domaineEnrichi.setUniteTauxRemboursement(unite);
    domaineEnrichi.setTauxRemboursement(taux);
    domaineEnrichi.setPrestations(
        DomaineDroitBufferConsolidationUtils.getPrestationsConso(domaine.getPrestations()));
    domaineEnrichi.setConventionnements(
        DomaineDroitBufferConsolidationUtils.getConventionnementsConso(
            domaine.getConventionnementsDomaine()));
    return domaineEnrichi;
  }

  /**
   * Decoupe filtre les {@link DomaineDroit} de la selectedDeclaration pour ne garder que ceux
   * incluant ou apres la dateExec
   */
  @ContinueSpan(log = "splitAndFilterDomaineDroits")
  public List<DomaineDroit> splitAndFilterDomaineDroits(
      Declaration selectedDeclaration, Date dateExec, boolean isInsurer) {
    String today = DateUtils.formatDate(dateExec);
    return DeclarationConsolideUtils.decoupageDomainesPeriodes(
        selectedDeclaration.getDomaineDroits(), today, isInsurer);
  }

  /**
   * Regroupe les {@link DomaineDroit} en {@link DomaineDroitBuffer} en fonction de leur DateFin et
   * code domaine
   */
  @ContinueSpan(log = "groupeByDateFinAndCode")
  public Map<String, Map<String, DomaineDroitBuffer>> groupeByDateFinAndCode(
      List<DomaineDroit> domaineDroits) {
    domaineDroits.sort(
        Comparator.comparing(DomaineDroit::getCode)
            .thenComparing(DomaineDroit::getPrioriteDroit)
            .reversed());

    Map<String, Map<String, DomaineDroitBuffer>> grouped = new HashMap<>();
    for (DomaineDroit domaineDroit : domaineDroits) {
      String periodeFin = domaineDroit.getPeriodeDroit().getPeriodeFin();
      grouped.putIfAbsent(periodeFin, new HashMap<>());
      Map<String, DomaineDroitBuffer> byCode = grouped.get(periodeFin);
      byCode.putIfAbsent(domaineDroit.getCode(), new DomaineDroitBuffer(domaineDroit));
      DomaineDroitBuffer buffer = byCode.get(domaineDroit.getCode());

      if (domaineDroit.getUniteTauxRemboursement() != null) {
        buffer.getUnitesDomaine().add(domaineDroit.getUniteTauxRemboursement());
      }
      if (domaineDroit.getTauxRemboursement() != null) {
        buffer.getTauxDomaine().add(domaineDroit.getTauxRemboursement());
      }
      if (domaineDroit.getPrioriteDroit().getCode() != null) {
        buffer.getPrioritesDomaine().add(domaineDroit.getPrioriteDroit().getCode());
      }
      if (domaineDroit.getConventionnements() != null) {
        buffer.getConventionnementsDomaine().addAll(domaineDroit.getConventionnements());
      }
      if (domaineDroit.getPrestations() != null) {
        buffer.getPrestations().addAll(domaineDroit.getPrestations());
      }
      // on prend la dernière car c'est la plus prioritaire qui nous intéresse dans le
      // calcul du taux
      buffer.setModeAssemblagePlusPrioritaire(domaineDroit.getModeAssemblage());
    }

    return grouped;
  }
}
