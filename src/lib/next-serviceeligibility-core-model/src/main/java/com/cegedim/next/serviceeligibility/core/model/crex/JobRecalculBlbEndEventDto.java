package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobRecalculBlbEndEventDto {

  private String deletionBeforeReinit;
  private int nbBeneficiaries;
  private int nbTrackedBeneficiariesOK;
  private int nbTrackedBeneficiariesKO;
  private int nbTrackedBeneficiariesNoResponse;
  private int nbTrackedBeneficiariesNoSend;

  // UTILS
  public Map<String, ParameterValue> toCrexMap() {
    return Map.of(
        "deletionBeforeReinit",
        ParameterValue.valueOf(deletionBeforeReinit), //
        "nbBeneficiaries",
        ParameterValue.valueOf(nbBeneficiaries), //
        "nbTrackedBeneficiariesOK",
        ParameterValue.valueOf(nbTrackedBeneficiariesOK), //
        "nbTrackedBeneficiariesKO",
        ParameterValue.valueOf(nbTrackedBeneficiariesKO), //
        "nbTrackedBeneficiariesNoResponse",
        ParameterValue.valueOf(nbTrackedBeneficiariesNoResponse), //
        "nbTrackedBeneficiariesNoSend",
        ParameterValue.valueOf(nbTrackedBeneficiariesNoSend) //
        );
  }

  public Map<String, Object> toMap() {
    return Map.of(
        "deletionBeforeReinit",
        deletionBeforeReinit, //
        "nbBeneficiaries",
        nbBeneficiaries, //
        "nbTrackedBeneficiariesOK",
        nbTrackedBeneficiariesOK, //
        "nbTrackedBeneficiariesKO",
        nbTrackedBeneficiariesKO, //
        "nbTrackedBeneficiariesNoResponse",
        nbTrackedBeneficiariesNoResponse, //
        "nbTrackedBeneficiariesNoSend",
        nbTrackedBeneficiariesNoSend //
        );
  }
}
