package com.cegedim.beyond.blb.recalcul.kafka.model;

import lombok.Data;

@Data
public class BlbLogicalDeletionResponseDto {
  private int total;
  private String errorCode;
  private String errorLabel;

  // --------------------
  // UTILS
  // --------------------
  public String errorMsg(String defaultIfEmpty) {
    if (errorCode == null && errorLabel == null) return defaultIfEmpty;
    return String.format("%s : %s", errorCode, errorLabel);
  }
}
