package com.cegedim.next.serviceeligibility.core.job.batch;

import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import java.util.Date;
import lombok.Data;

@Data
public class Rejection {

  private String codeRejet;
  private Declaration declaration;
  private DeclarationConsolide declarationConsolide;
  private Date dateExecution;
  private String codeService;

  public Rejection(
      String codeRejet, Declaration declaration, Date dateExecution, String codeService) {
    this.codeRejet = codeRejet;
    this.declaration = declaration;
    this.dateExecution = dateExecution;
    this.codeService = codeService;
  }

  public Rejection(
      String codeRejet,
      DeclarationConsolide declarationConsolide,
      Date dateExecution,
      String codeService) {
    this.codeRejet = codeRejet;
    this.declarationConsolide = declarationConsolide;
    this.dateExecution = dateExecution;
    this.codeService = codeService;
  }
}
