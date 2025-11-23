package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class BenefitType {
  String benefitType;
  Period period;

  // Pour le PAU v5
  // TODO : A supprimer ?
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  List<String> tags;

  // Pour le PAU v5
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  List<Domain> domains;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BenefitType that = (BenefitType) o;
    return benefitType.equals(that.benefitType) && period.equals(that.period);
  }

  @Override
  public int hashCode() {
    return Objects.hash(benefitType, period);
  }
}
