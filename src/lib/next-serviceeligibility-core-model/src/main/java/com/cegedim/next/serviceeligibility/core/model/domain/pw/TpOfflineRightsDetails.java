package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.util.CollectionUtils;

@Data
public class TpOfflineRightsDetails {
  String formulaCode;
  String formulaLabel;
  String formulaMask;
  String stsFormulaCode;
  String nature;
  List<Variable> variables;

  public TpOfflineRightsDetails() {
    /* empty constructor */ }

  public TpOfflineRightsDetails(TpOfflineRightsDetails source) {
    this.formulaCode = source.getFormulaCode();
    this.formulaLabel = source.getFormulaLabel();
    this.formulaMask = source.getFormulaMask();
    this.stsFormulaCode = source.getStsFormulaCode();
    this.nature = source.getNature();
    if (!CollectionUtils.isEmpty(source.getVariables())) {
      this.variables = new ArrayList<>();
      for (Variable variable : source.getVariables()) {
        this.variables.add(new Variable(variable));
      }
    }
  }
}
