package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TraceRetour implements GenericDomain<TraceRetour> {

  private static final long serialVersionUID = -8580560294652986496L;

  private Date dateExecution;
  private String batch;
  private String codeRejet;
  private String libelleRejet;
  private String valeurRejet;
  private String nomFichier;

  @Override
  public int compareTo(final TraceRetour traceRetour) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(dateExecution, traceRetour.dateExecution);
    compareToBuilder.append(batch, traceRetour.batch);
    return compareToBuilder.toComparison();
  }
}
