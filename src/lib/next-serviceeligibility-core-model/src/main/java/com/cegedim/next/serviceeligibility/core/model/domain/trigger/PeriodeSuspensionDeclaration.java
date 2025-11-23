package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
public class PeriodeSuspensionDeclaration implements GenericDomain<PeriodeSuspensionDeclaration> {

  private String debut;
  private String fin;
  private String typeSuspension;
  private String motifSuspension;
  private String motifLeveeSuspension;

  @Override
  public int compareTo(PeriodeSuspensionDeclaration source) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.debut, source.getDebut());
    compareToBuilder.append(this.fin, source.getFin());
    compareToBuilder.append(this.typeSuspension, source.getTypeSuspension());
    compareToBuilder.append(this.motifSuspension, source.getMotifSuspension());
    compareToBuilder.append(this.motifLeveeSuspension, source.getMotifLeveeSuspension());
    return 0;
  }
}
