package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Data;

@Data
public class Domain {
  String domainCode;
  String convention;

  public Domain(String domainCode, String convention) {
    this.domainCode = domainCode;
    this.convention = convention;
  }
}
