package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectiveContractV5 extends CollectiveContract {
  String companyName;
}
