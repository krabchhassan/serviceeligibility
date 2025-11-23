package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import lombok.Data;

@Data
public class Variable {
  String variableCode;
  Integer stsVariableNumber;
  String value;

  public Variable() {
    /* empty constructor */ }

  public Variable(Variable source) {
    this.variableCode = source.getVariableCode();
    this.stsVariableNumber = source.getStsVariableNumber();
    this.value = source.getValue();
  }
}
