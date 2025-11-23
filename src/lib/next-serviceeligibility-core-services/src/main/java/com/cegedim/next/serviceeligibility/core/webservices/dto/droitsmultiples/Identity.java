package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.List;
import lombok.Data;

@Data
public class Identity {
  private Nir nir;
  private List<AffiliationRO> affiliationsRO;
  private String birthDate;
  private String birthRank;
  private String personNumber;
  private String personExternalRef;
}
