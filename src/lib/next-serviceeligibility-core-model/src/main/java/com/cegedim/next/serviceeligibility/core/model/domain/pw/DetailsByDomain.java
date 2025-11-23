package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.util.CollectionUtils;

@Data
public class DetailsByDomain {
  String formulaCode;
  String formulaLabel;
  String formulaMask;
  String stsFormulaCode;
  String natureCode;
  List<Variable> variables;

  public DetailsByDomain() {
    /* empty constructor */ }

  public DetailsByDomain(DetailsByDomain source) {
    this.formulaCode = source.getFormulaCode();
    this.formulaLabel = source.getFormulaLabel();
    this.formulaMask = source.getFormulaMask();
    this.stsFormulaCode = source.getStsFormulaCode();
    this.natureCode = source.getNatureCode();
    if (!CollectionUtils.isEmpty(source.getVariables())) {
      this.variables = new ArrayList<>();
      for (Variable variable : source.getVariables()) {
        this.variables.add(new Variable(variable));
      }
    }
  }
}
