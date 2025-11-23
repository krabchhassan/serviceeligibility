package com.cegedim.next.serviceeligibility.core.dao;

import lombok.Data;

@Data
public class RequestParametrageCarteTP {

  private String amc;
  private boolean onlyActif;
  private boolean onlyValid;
  private boolean notManual;
  private boolean notPilotageBO;

  public RequestParametrageCarteTP(String amc, boolean onlyActif) {
    this.amc = amc;
    this.onlyActif = onlyActif;
    this.onlyValid = false;
    this.notManual = false;
    this.notPilotageBO = false;
  }

  public RequestParametrageCarteTP(
      String amc, boolean onlyActif, boolean onlyValid, boolean notManual, boolean notPilotageBO) {
    this.amc = amc;
    this.onlyActif = onlyActif;
    this.onlyValid = onlyValid;
    this.notManual = notManual;
    this.notPilotageBO = notPilotageBO;
  }
}
