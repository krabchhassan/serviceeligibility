package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;

@Data
public class AffiliationRO {
  private Nir nir;
  private AttachementRO attachementRO;
  private Period period;
}
