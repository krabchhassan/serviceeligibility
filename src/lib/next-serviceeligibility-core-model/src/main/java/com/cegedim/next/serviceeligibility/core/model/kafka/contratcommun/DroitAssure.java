package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor
public class DroitAssure extends DroitAssureCommun {

  @Getter private List<CarenceDroit> carences;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    DroitAssureCommun other = (DroitAssureCommun) obj;
    if (code == null) {
      return other.getCode() == null;
    } else return code.equals(other.getCode());
  }

  public boolean equalsForDeclaration(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DroitAssure that = (DroitAssure) o;
    return code.equals(that.code)
        && codeAssureur.equals(that.codeAssureur)
        && Objects.equals(libelle, that.libelle)
        && Objects.equals(ordrePriorisation, that.ordrePriorisation)
        && Objects.equals(type, that.type)
        && Objects.equals(dateAncienneteGarantie, that.dateAncienneteGarantie);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    return result;
  }

  public void setCarences(List<CarenceDroit> carences) {
    if (carences != null) {
      this.carences = carences;
    }
  }

  public void setCarencesToNull() {
    this.carences = null;
  }

  public DroitAssure(DroitAssure source) {
    if (!CollectionUtils.isEmpty(source.getCarences())) {
      this.carences = new ArrayList<>();
      for (CarenceDroit carenceDroitV3 : source.getCarences()) {
        this.carences.add(new CarenceDroit(carenceDroitV3));
      }
    }

    this.setCode(source.getCode());
    this.setCodeAssureur(source.getCodeAssureur());
    this.setLibelle(source.getLibelle());
    this.setOrdrePriorisation(source.getOrdrePriorisation());
    this.setType(source.getType());
    if (source.getPeriode() != null) {
      this.setPeriode(new Periode(source.getPeriode()));
    }
    this.setDateAncienneteGarantie(source.getDateAncienneteGarantie());
  }
}
