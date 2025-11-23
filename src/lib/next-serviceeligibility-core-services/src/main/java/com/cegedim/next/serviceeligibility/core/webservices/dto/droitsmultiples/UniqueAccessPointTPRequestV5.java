package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UniqueAccessPointTPRequestV5 extends UniqueAccessPointTPRequest {

  protected UniqueAccessPointRequestV5 requestV5;

  @Override
  public UniqueAccessPointRequestV5 getRequest() {
    return requestV5;
  }

  @Override
  public void setRequest(UniqueAccessPointRequest request) {
    this.requestV5 = (UniqueAccessPointRequestV5) request;
  }
}
