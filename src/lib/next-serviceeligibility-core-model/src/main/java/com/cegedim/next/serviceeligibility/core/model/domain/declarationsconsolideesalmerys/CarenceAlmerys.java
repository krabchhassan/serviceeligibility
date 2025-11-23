package com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

@Data
public class CarenceAlmerys implements GenericDomain<CarenceAlmerys> {
  private String domaineDroitCarence;
  private String periodeDebutCarence; // yyyy/MM/dd
  private String periodeFinCarence; // yyyy/MM/dd

  @Override
  public int compareTo(@NotNull CarenceAlmerys carence) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.domaineDroitCarence, carence.domaineDroitCarence);
    compareToBuilder.append(this.periodeDebutCarence, carence.periodeDebutCarence);
    compareToBuilder.append(this.periodeFinCarence, carence.periodeFinCarence);
    return compareToBuilder.toComparison();
  }
}
