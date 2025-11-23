package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PeriodeDroitTPStep1 {

  /**
   * extrait les périodes issus des différents niveaux dans le domaine du contrat
   *
   * @param domaineDroitContract : domaine du contrat
   * @param domaineDroitForConsolidation : domaine du domaine.
   * @return la liste des périodes
   */
  public NaturePrestation extractOrCreatePeriodes(
      DomaineDroitContractTP domaineDroitContract,
      DomaineDroitForConsolidation domaineDroitForConsolidation) {
    Garantie garantie =
        getOrAdd(domaineDroitContract.getGaranties(), domaineDroitForConsolidation.getGarantie());
    Produit produit = getOrAdd(garantie.getProduits(), domaineDroitForConsolidation.getProduit());
    ReferenceCouverture referenceCouverture =
        getOrAdd(
            produit.getReferencesCouverture(),
            domaineDroitForConsolidation.getReferenceCouverture());
    return getOrAdd(
        referenceCouverture.getNaturesPrestation(),
        domaineDroitForConsolidation.getNaturePrestation());
  }

  private <T extends Mergeable> T getOrAdd(
      Collection<T> objectFromContrat, T objectFromDeclaration) {
    Optional<T> optionnal =
        objectFromContrat.stream()
            .filter(elem -> Objects.equals(elem.mergeKey(), objectFromDeclaration.mergeKey()))
            .findFirst();

    if (optionnal.isPresent()) {
      return optionnal.get();
    }
    objectFromContrat.add(objectFromDeclaration);
    return objectFromDeclaration;
  }
}
