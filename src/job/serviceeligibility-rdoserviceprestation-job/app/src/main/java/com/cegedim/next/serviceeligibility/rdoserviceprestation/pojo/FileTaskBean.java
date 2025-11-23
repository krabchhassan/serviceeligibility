package com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo;

import lombok.Data;

@Data
public class FileTaskBean {

  private String clientBo;

  private String firstTriggerId;

  private String firstTraceId;

  private Long nbContratOk = 0L;

  private Long nbContratKo = 0L;

  private Long numeroContrat = 0L;

  public void incNbContratOk() {
    nbContratOk++;
  }

  public void incNbContratKo() {
    nbContratKo++;
  }

  public void incNumeroContrat() {
    numeroContrat++;
  }
}
