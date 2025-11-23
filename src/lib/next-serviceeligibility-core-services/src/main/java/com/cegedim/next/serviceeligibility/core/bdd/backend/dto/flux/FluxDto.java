package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.ArrayList;
import java.util.List;

public class FluxDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private long totalFlux;

  private List<FluxInfoDto> fluxInfo = new ArrayList<>();

  public long getTotalFlux() {
    return totalFlux;
  }

  public void setTotalFlux(long totalFlux) {
    this.totalFlux = totalFlux;
  }

  public List<FluxInfoDto> getFluxInfo() {
    return fluxInfo;
  }

  public void setFluxInfo(List<FluxInfoDto> fluxInfo) {
    this.fluxInfo = fluxInfo;
  }
}
