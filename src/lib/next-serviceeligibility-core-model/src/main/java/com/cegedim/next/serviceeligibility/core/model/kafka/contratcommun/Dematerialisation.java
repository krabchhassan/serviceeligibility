package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class Dematerialisation implements GenericDomain<Dematerialisation> {
  private Boolean isDematerialise;
  private String email;
  private String mobile;

  public void setIsDematerialise(Boolean newDematerialise) {
    if (newDematerialise != null) {
      isDematerialise = newDematerialise;
    }
  }

  public void setMobile(String mobile) {
    if (mobile != null) {
      this.mobile = mobile;
    }
  }

  public void setEmail(String email) {
    if (email != null) {
      this.email = email;
    }
  }

  @Override
  public int compareTo(Dematerialisation dematerialisation) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.isDematerialise, dematerialisation.isDematerialise);
    compareToBuilder.append(this.email, dematerialisation.email);
    compareToBuilder.append(this.mobile, dematerialisation.mobile);
    return compareToBuilder.toComparison();
  }
}
