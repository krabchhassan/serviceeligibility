package com.cegedim.next.serviceeligibility.core.bdd.webservice.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CarteTiersPayantUtils {

  private CarteTiersPayantUtils() {}

  /**
   * Set priorites pour chaque domaine à la priorité du contrat, afin de retourner trier les
   * contrats par bases
   *
   * @param declarationDtos la liste des declarations dto
   * @param nirBeneficiaire nir beneficiaire
   */
  public static void setPrioriteCarteTiersPayant(
      final List<DeclarationDto> declarationDtos, final String nirBeneficiaire) {
    for (DeclarationDto declarationDto : declarationDtos) {
      PrioriteDroitDto priorite = declarationDto.getDomaineDroits().get(0).getPrioriteDroit();
      String prioriteDroit = priorite.getCode();

      if (nirBeneficiaire.equals(priorite.getNirPrio1())) {
        prioriteDroit = priorite.getPrioContratNir1();
      } else if (nirBeneficiaire.equals(priorite.getNirPrio2())) {
        prioriteDroit = priorite.getPrioContratNir2();
      }
      if (prioriteDroit != null) {
        priorite.setCode(prioriteDroit);
        priorite.setLibelle(prioriteDroit);
      }
    }
  }

  /**
   * Supprime les domaines de droits doublons dans chacune des declarations presentes dans la liste
   * passee en parametre. Deux domaines de droits sont identiques quand ils ont la meme valeur dans
   * la propriete code Quand deux domaines de droit sont identiques, celui ayant la propriete code
   * la plus grande doit etre supprime.
   *
   * @param declarationList La liste des declarations a filtrer.
   */
  public static void checkDomaineDroitDoublon(final List<DeclarationDto> declarationList) {

    final Iterator<DeclarationDto> declarationIterator = declarationList.iterator();
    while (declarationIterator.hasNext()) {
      DeclarationDto declaration = declarationIterator.next();
      Map<String, DomaineDroitDto> domaineDroitsEligibles = new HashMap<>();
      for (DomaineDroitDto domaineDroit : declaration.getDomaineDroits()) {
        if (domaineDroitsEligibles.containsKey(domaineDroit.getCode())) {
          if (domaineDroitsEligibles
                  .get(domaineDroit.getCode())
                  .getPrioriteDroit()
                  .getCode()
                  .compareTo(domaineDroit.getPrioriteDroit().getCode())
              > 0) {
            domaineDroitsEligibles.put(domaineDroit.getCode(), domaineDroit);
          }
        } else {
          domaineDroitsEligibles.put(domaineDroit.getCode(), domaineDroit);
        }
      }
      declaration.getDomaineDroits().clear();
      List<DomaineDroitDto> listDomainesTriees = new ArrayList<>(domaineDroitsEligibles.values());
      Collections.sort(
          listDomainesTriees,
          (o1, o2) -> {
            Integer prioDroitDomaine1 = o1.getNoOrdreDroit();
            Integer prioDroitDomaine2 = o2.getNoOrdreDroit();
            if (prioNotNull(prioDroitDomaine1, prioDroitDomaine2)) {
              return prioDroitDomaine1.compareTo(prioDroitDomaine2);
            } else {
              return o1.getCode().compareTo(o2.getCode());
            }
          });
      declaration.getDomaineDroits().addAll(listDomainesTriees);
    }
  }

  private static boolean prioNotNull(Integer prioDroitDomaine1, Integer prioDroitDomaine2) {
    return prioDroitDomaine1 != null && prioDroitDomaine2 != null;
  }

  /**
   * Retourne une map de liste de déclaration par bénéficiaire
   *
   * @param listeDeclarations la liste des déclarations a regrouper
   * @return une map contenant une liste de déclaration par benef
   */
  public static Map<String, List<DeclarationDto>> getDeclarationByBeneficiaire(
      final List<DeclarationDto> listeDeclarations) {
    Map<String, List<DeclarationDto>> mapBenef = new HashMap<>();
    Iterator<DeclarationDto> declarationIterator = listeDeclarations.iterator();
    while (declarationIterator.hasNext()) {
      DeclarationDto declaration = declarationIterator.next();
      String key = getKeyDeclaration(declaration);
      List<DeclarationDto> listeBenef = mapBenef.get(key);
      if (listeBenef == null) {
        listeBenef = new ArrayList<>();
      }
      listeBenef.add(declaration);
      mapBenef.put(key, listeBenef);
    }
    return mapBenef;
  }

  private static String getKeyDeclaration(final DeclarationDto declaration) {
    StringBuilder key = new StringBuilder(declaration.getBeneficiaire().getNirOd1());
    key.append("*");
    key.append(declaration.getBeneficiaire().getCleNirOd1());
    key.append("*");
    key.append(declaration.getBeneficiaire().getDateNaissance());
    key.append("*");
    key.append(declaration.getBeneficiaire().getRangNaissance());
    return key.toString();
  }
}
