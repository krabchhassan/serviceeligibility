package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduRepriseBenefs implements CompteRenduGeneric {
  private long contratsHtpRepris;
  private long benefsReemisViaHtp;
  private long contratsTpRepris;
  private long contratsPrestijRepris;
  private long benefsReemisViaPrestij;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("contratsHtpRepris", ParameterValue.valueOf((int) this.contratsHtpRepris));
    map.put("benefsReemisViaHtp", ParameterValue.valueOf((int) this.benefsReemisViaHtp));
    map.put("contratsTpRepris", ParameterValue.valueOf((int) this.contratsTpRepris));
    map.put("contratsPrestijRepris", ParameterValue.valueOf((int) this.contratsPrestijRepris));
    map.put("benefsReemisViaPrestij", ParameterValue.valueOf((int) this.benefsReemisViaPrestij));

    return map;
  }

  public void addContratsHtpRepris(long contratsHtpRepris) {
    this.contratsHtpRepris += contratsHtpRepris;
  }

  public void addBenefsReemisViaHtp(long benefsReemisViaHtp) {
    this.benefsReemisViaHtp += benefsReemisViaHtp;
  }

  public void addContratsTpRepris(long contratsTpRepris) {
    this.contratsTpRepris += contratsTpRepris;
  }

  public void addContratsPrestijRepris(long contratsPrestijRepris) {
    this.contratsPrestijRepris += contratsPrestijRepris;
  }

  public void addBenefsReemisViaPrestij(long benefsReemisViaPrestij) {
    this.benefsReemisViaPrestij += benefsReemisViaPrestij;
  }
}
