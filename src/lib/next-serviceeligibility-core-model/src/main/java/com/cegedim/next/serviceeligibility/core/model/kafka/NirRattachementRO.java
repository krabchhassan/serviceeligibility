package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class NirRattachementRO {

  @NotNull(message = "L'information nir du rattachement RO est obligatoire")
  @Valid
  private Nir nir;

  @Valid private RattachementRO rattachementRO;

  @NotNull(message = "L'information periode du rattachement RO est obligatoire")
  @Valid
  private Periode periode;

  public NirRattachementRO() {}

  public NirRattachementRO(NirRattachementRO source) {
    if (source.getNir() != null) {
      Nir oldNir = source.getNir();
      this.nir = new Nir(oldNir.getCode(), oldNir.getCle());
    }

    if (source.getRattachementRO() != null) {
      RattachementRO rattachementRO = source.getRattachementRO();
      this.rattachementRO =
          new RattachementRO(
              rattachementRO.getCodeRegime(),
              rattachementRO.getCodeCaisse(),
              rattachementRO.getCodeCentre());
    }

    if (source.getPeriode() != null) {
      this.periode = new Periode(source.getPeriode());
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nir == null) ? 0 : nir.hashCode());
    result = prime * result + ((periode == null) ? 0 : periode.hashCode());
    result = prime * result + ((rattachementRO == null) ? 0 : rattachementRO.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null) {
      NirRattachementRO other = (NirRattachementRO) obj;

      String oldCode = other.getNir().getCode();
      String newCode = this.getNir().getCode();
      return StringUtils.isNotEmpty(oldCode)
          && StringUtils.isNotEmpty(newCode)
          && oldCode.equals(newCode)
          && StringUtils.isNotEmpty(other.getPeriode().getDebut())
          && StringUtils.isNotEmpty(this.getPeriode().getDebut())
          && Objects.equals(this.rattachementRO, other.getRattachementRO());
    }
    return false;
  }

  public void setNir(Nir newNir) {
    if (newNir != null) {
      this.nir = newNir;
    }
  }

  public void setRattachementRO(RattachementRO newRattachementRO) {
    if (newRattachementRO != null) {
      this.rattachementRO = newRattachementRO;
    }
  }

  public void setPeriode(Periode newPeriode) {
    if (newPeriode != null) {
      this.periode = newPeriode;
    }
  }

  public String nirCode() {
    return (nir != null) ? nir.getCode() : null;
  }
}
