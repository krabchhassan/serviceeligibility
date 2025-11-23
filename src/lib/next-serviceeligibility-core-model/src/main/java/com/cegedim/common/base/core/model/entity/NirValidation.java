package com.cegedim.common.base.core.model.entity;

import lombok.Data;

@Data
public class NirValidation {
  private boolean isValid;
  private int errorCode;
  private String errorMessage;
}
