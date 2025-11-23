package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class CarenceDifference {

  private final List<CarenceDroit> oldCarenceDroitList;
  private final List<CarenceDroit> newCarenceDroitList;
  private final List<String> listOldCarences;
  private final List<String> listNewCarences;

  public CarenceDifference(ContratAIV5 oldContract, ContratAIV5 newContract) {
    this.oldCarenceDroitList = this.extractCarencesFromContract(oldContract);
    this.newCarenceDroitList = this.extractCarencesFromContract(newContract);

    listOldCarences = oldCarenceDroitList.stream().map(CarenceDroit::getCode).toList();
    listNewCarences = newCarenceDroitList.stream().map(CarenceDroit::getCode).toList();
  }

  public boolean checkHasDifferentCarences() {
    return !CollectionUtils.isEqualCollection(this.oldCarenceDroitList, this.newCarenceDroitList);
  }

  public boolean hasNewCarences() {
    return !listOldCarences.containsAll(listNewCarences);
  }

  public boolean hasDeletedCarences() {
    return !listNewCarences.containsAll(listOldCarences);
  }

  public boolean hasChangedCarences() {
    if (hasNewCarences() || hasDeletedCarences()) {
      return true;
    }
    for (CarenceDroit newC : newCarenceDroitList) {
      for (CarenceDroit oldC : oldCarenceDroitList) {
        if (newC.getCode().equals(oldC.getCode())
            && (!newC.getPeriode().getDebut().equals(oldC.getPeriode().getDebut())
                || !newC.getPeriode().getFin().equals(oldC.getPeriode().getFin()))) {
          return true;
        }
      }
    }

    return false;
  }

  private List<CarenceDroit> extractCarencesFromContract(ContratAIV5 contract) {
    List<Assure> newContractAssures = contract.getAssures();
    List<DroitAssure> droitAssureList =
        newContractAssures.stream()
            .filter(Objects::nonNull)
            .map(Assure::getDroits)
            .flatMap(Collection::stream)
            .toList();
    return Optional.of(
            droitAssureList.stream()
                .filter(Objects::nonNull)
                .map(DroitAssure::getCarences)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream))
        .orElse(null)
        .toList();
  }
}
