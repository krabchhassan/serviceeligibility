package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class GestionDroits {
  List<DroitsTPExtended> listeDroits;
  LocalDate dateDebutDroit;
  LocalDate dateFinDroit;
  Periode periodeDroitCalcule;

  public GestionDroits(
      List<DroitsTPExtended> listeDroits,
      LocalDate dateDebutDroit,
      LocalDate dateFinDroit,
      Periode periodeDroitCalcule) {
    this.listeDroits = listeDroits;
    this.dateDebutDroit = dateDebutDroit;
    this.dateFinDroit = dateFinDroit;
    this.periodeDroitCalcule = periodeDroitCalcule;
  }
}
