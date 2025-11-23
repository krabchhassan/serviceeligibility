package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TraceConsolidation implements GenericDomain<TraceConsolidation> {

  private static final long serialVersionUID = -4286848017244769995L;

  private String idDeclarationConsolidee;
  private Date dateExecution;
  private String codeService;
  private String codeRejet;
  private String batch;
  private String nomFichierARL;
  private String codeClient;
  private String collectionConsolidee;
  private List<TraceExtraction> listeExtractions;

  @Override
  public int compareTo(final TraceConsolidation traceConso) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(idDeclarationConsolidee, traceConso.idDeclarationConsolidee);
    compareToBuilder.append(dateExecution, traceConso.dateExecution);
    return compareToBuilder.toComparison();
  }
}
