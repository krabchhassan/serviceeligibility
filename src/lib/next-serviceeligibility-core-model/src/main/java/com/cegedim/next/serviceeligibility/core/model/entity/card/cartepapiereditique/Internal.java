package com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique;

import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
public class Internal implements GenericDomain<Internal> {

  ////////////////
  // Attributes //
  ////////////////

  // Properties
  String status;
  String mainOrganizationCode;
  String secondaryOrganizationCode;
  String portfolioCode;
  private String identifiant;
  private List<TraceExtractionConso> traceExtractionConso;
  private String extractionFileName;

  @Override
  public int compareTo(Internal internal) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.status, internal.status);
    compareToBuilder.append(this.mainOrganizationCode, internal.mainOrganizationCode);
    compareToBuilder.append(this.secondaryOrganizationCode, internal.secondaryOrganizationCode);
    compareToBuilder.append(this.portfolioCode, internal.portfolioCode);
    compareToBuilder.append(this.identifiant, internal.identifiant);
    compareToBuilder.append(this.traceExtractionConso, internal.traceExtractionConso);
    compareToBuilder.append(this.extractionFileName, internal.extractionFileName);
    return compareToBuilder.toComparison();
  }
}
