package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NameCorporate extends Name {
  private String corporateName;
}
