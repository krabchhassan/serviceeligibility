package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TracePriorisation implements GenericDomain<TracePriorisation> {

  private static final long serialVersionUID = -6583796996965697183L;

  private Date dateExecution;
  private String codeService;
  private String codeRejet;
  private String nomFichierARL;
  private Integer nbPrioOrigineDifferent;
  private String batch;

  @Override
  public int compareTo(final TracePriorisation tracePrio) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(dateExecution, tracePrio.dateExecution);
    compareToBuilder.append(codeService, tracePrio.codeService);
    return compareToBuilder.toComparison();
  }
}
