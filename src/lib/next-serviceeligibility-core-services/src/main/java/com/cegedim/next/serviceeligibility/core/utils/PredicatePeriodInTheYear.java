package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.function.Predicate;

public class PredicatePeriodInTheYear implements Predicate<Periode> {

  public PredicatePeriodInTheYear(String currentYear) {
    this.currentYear = currentYear;
  }

  private String currentYear;

  public boolean test(Periode period) {
    return currentYear.equals(period.getDebut().substring(0, 4))
        && (period.getFin() == null || currentYear.equals(period.getFin().substring(0, 4)));
  }
}
