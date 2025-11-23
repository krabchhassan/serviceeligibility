package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineDroitBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomaineDroitBufferConsolidationUtils {

  /**
   * Retourne une liste de conventionnements unique a partir d'un tableau de liste de
   * conventionnement pour un domaine donnée
   *
   * @param tabConventionnement les {@link Conventionnement} a traiter
   * @return une liste de conventionnements consolidé
   */
  public static List<Conventionnement> getConventionnementsConso(
      List<Conventionnement> tabConventionnement) {
    List<Conventionnement> conventionnementConso = new ArrayList<>();
    Set<String> codesConventionnement = new HashSet<>();
    for (Conventionnement conventionnement : tabConventionnement) {
      String conventionnementCode = conventionnement.getTypeConventionnement().getCode();
      if (codesConventionnement.add(conventionnementCode)) {
        conventionnementConso.add(conventionnement);
      }
    }

    conventionnementConso.sort(Comparator.comparingInt(Conventionnement::getPriorite));
    return conventionnementConso;
  }

  /**
   * Retourne une liste de prestations unique a partir d'un tableau de liste de prestations pour un
   * domaine donnée
   *
   * @param domaine le domaine pour lequel on veut consolider les prestations
   * @return une liste de prestations consolidé
   */
  public static List<Prestation> getPrestationsConso(List<Prestation> domaine) {
    LinkedHashSet<Prestation> uniquePresta = new LinkedHashSet<>(domaine);
    return new ArrayList<>(uniquePresta);
  }

  /**
   * Retourne la liste des domaines avec pour chacun la liste des conventions associé, trié dans
   * l'ordre d'affichage
   *
   * @param listeDomaines la liste des domaines du contrat
   * @return la liste des domaines avec leur liste de convention
   */
  public static List<DomaineConvention> getListDomaineConvention(
      Collection<DomaineDroitBuffer> listeDomaines) {
    List<DomaineConvention> listDomainesConvention = new ArrayList<>();
    for (DomaineDroitBuffer domaine : listeDomaines) {
      listDomainesConvention.add(createDomaineConventions(domaine));
    }
    listDomainesConvention.sort(Comparator.comparingInt(DomaineConvention::getRang));
    return listDomainesConvention;
  }

  /**
   * Créé la liste des conventions pour un domaines donnée au format domaineConvention
   *
   * @param domaine {@link DomaineDroitBuffer}
   * @return
   */
  private static DomaineConvention createDomaineConventions(DomaineDroitBuffer domaine) {
    DomaineConvention domaineConvention = new DomaineConvention();
    String codeDomaine = domaine.getProduit().getCode();

    Integer rangDomaineJson = domaine.getProduit().getNoOrdreDroit();
    int rangDomaine = rangDomaineJson != null ? rangDomaineJson : 0;

    List<Conventionnement> listeConventionnements =
        getConventionnementsConso(domaine.getConventionnementsDomaine());
    List<Convention> listConventions = new ArrayList<>();

    for (Conventionnement conventionnement : listeConventionnements) {
      Convention convention = new Convention();
      convention.setCode(conventionnement.getTypeConventionnement().getCode());
      convention.setPriorite(conventionnement.getPriorite());
      listConventions.add(convention);
    }

    domaineConvention.setCode(codeDomaine);
    domaineConvention.setRang(rangDomaine);

    listConventions.sort(Comparator.comparingInt(Convention::getPriorite));
    domaineConvention.setConventions(listConventions);
    return domaineConvention;
  }

  /**
   * Création d'un objet produit pour le beneficiaire
   *
   * @param produit
   * @return
   */
  public static DomaineDroit getProduitBenef(DomaineDroit produit) {
    DomaineDroit result = new DomaineDroit();
    result.setCodeExterneProduit(produit.getCodeExterneProduit());
    result.setLibelleExterne(produit.getLibelleExterne());
    result.setCodeOptionMutualiste(produit.getCodeOptionMutualiste());
    result.setLibelleOptionMutualiste(produit.getLibelleOptionMutualiste());
    result.setCodeProduit(produit.getCodeProduit());
    result.setLibelleProduit(produit.getLibelleProduit());
    return result;
  }

  public static DomaineDroit getProduitBenef(Collection<DomaineDroitBuffer> domaines) {
    return getProduitBenef(domaines.iterator().next().getProduit());
  }
}
