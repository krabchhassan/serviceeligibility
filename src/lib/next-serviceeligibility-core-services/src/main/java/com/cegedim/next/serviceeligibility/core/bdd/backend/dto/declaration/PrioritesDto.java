package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class PrioritesDto implements GenericDto {
  /** */
  private static final long serialVersionUID = 5895360336198889540L;

  private String prioriteBackOffice;
  private String numeroRO1;
  private String prioContratRO1;
  private String prioDroitRO1;
  private String numeroRO2;
  private String prioContratRO2;
  private String prioDroitRO2;

  public String getPrioriteBackOffice() {
    return prioriteBackOffice;
  }

  public void setPrioriteBackOffice(String prioriteBackOffice) {
    this.prioriteBackOffice = prioriteBackOffice;
  }

  public String getNumeroRO1() {
    return numeroRO1;
  }

  public void setNumeroRO1(String numeroRO1) {
    this.numeroRO1 = numeroRO1;
  }

  public String getPrioContratRO1() {
    return prioContratRO1;
  }

  public void setPrioContratRO1(String prioContratRO1) {
    this.prioContratRO1 = prioContratRO1;
  }

  public String getPrioDroitRO1() {
    return prioDroitRO1;
  }

  public void setPrioDroitRO1(String prioDroitRO1) {
    this.prioDroitRO1 = prioDroitRO1;
  }

  public String getNumeroRO2() {
    return numeroRO2;
  }

  public void setNumeroRO2(String numeroRO2) {
    this.numeroRO2 = numeroRO2;
  }

  public String getPrioContratRO2() {
    return prioContratRO2;
  }

  public void setPrioContratRO2(String prioContratRO2) {
    this.prioContratRO2 = prioContratRO2;
  }

  public String getPrioDroitRO2() {
    return prioDroitRO2;
  }

  public void setPrioDroitRO2(String prioDroitRO2) {
    this.prioDroitRO2 = prioDroitRO2;
  }
}
