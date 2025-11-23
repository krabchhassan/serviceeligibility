package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ContratV6 {
  private List<ServicePrestationV6> contrats; // NOSONAR
}
