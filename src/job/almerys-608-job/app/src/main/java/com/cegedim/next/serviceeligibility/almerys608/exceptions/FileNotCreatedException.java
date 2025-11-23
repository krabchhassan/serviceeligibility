package com.cegedim.next.serviceeligibility.almerys608.exceptions;

import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;

public class FileNotCreatedException extends RuntimeException {
  public FileNotCreatedException(Pilotage pilotage) {
    super(
        String.format(
            "fichier non valide pour %s-%s-%s",
            pilotage.getCaracteristique().getNumEmetteur(),
            pilotage.getCritereRegroupement(),
            pilotage.getCritereRegroupementDetaille()));
  }
}
