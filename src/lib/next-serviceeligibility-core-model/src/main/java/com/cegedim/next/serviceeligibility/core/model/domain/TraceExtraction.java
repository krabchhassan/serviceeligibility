package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TraceExtraction implements GenericDomain<TraceExtraction> {

  private static final long serialVersionUID = 8474751308919498919L;

  private Date dateExecution;
  private String codeService;
  private String codeRejet;
  private String batch;
  private String nomFichier;
  private String nomFichierARL;
  private Integer numeroFichier;
  private String codeClient;
  private Integer idLigne;
  private List<TraceRetour> listeRetours;

  @Override
  public int compareTo(final TraceExtraction traceExtract) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(dateExecution, traceExtract.dateExecution);
    compareToBuilder.append(codeService, traceExtract.codeService);
    return compareToBuilder.toComparison();
  }
}
