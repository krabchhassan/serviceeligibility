package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExtractContractsService {

  private final ExtractContractProducer extractContractProducer;

  public void sendExtractContractByBeneficiaryIdMessage(final ContratAIV6 contrat) {
    contrat.getAssures().stream()
        .flatMap(assure -> this.getBeneficiaires(assure).stream())
        .forEach(c -> this.extractContractProducer.send(c, c.getKey()));
  }

  public Set<BeneficiaireId> getBeneficiaires(final Assure assure) {
    final Set<BeneficiaireId> beneficiaires = new HashSet<>();
    if (assure.getIdentite().getNir() != null) {
      beneficiaires.add(this.extractId(assure));
    }
    if (assure.getIdentite().getAffiliationsRO() != null) {
      beneficiaires.addAll(
          assure.getIdentite().getAffiliationsRO().stream()
              .map(affiliation -> this.extractId(assure, affiliation))
              .toList());
    }
    return beneficiaires;
  }

  private BeneficiaireId extractId(final Assure assure) {
    return BeneficiaireId.builder()
        .nir(assure.getIdentite().getNir().getCode())
        .dateNaissance(assure.getIdentite().getDateNaissance())
        .rangNaissance(assure.getIdentite().getRangNaissance())
        .build();
  }

  private BeneficiaireId extractId(final Assure assure, final NirRattachementRO affiliation) {
    return BeneficiaireId.builder()
        .nir(affiliation.getNir().getCode())
        .dateNaissance(assure.getIdentite().getDateNaissance())
        .rangNaissance(assure.getIdentite().getRangNaissance())
        .build();
  }
}
