package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.Objects;
import lombok.Data;

@Data
public class Period {
  String start;
  String end;

  public Period() {}

  public Period(String debut, String fin) {
    if (debut != null) {
      this.start = debut.replace("/", "-");
    }
    if (fin != null) {
      this.end = fin.replace("/", "-");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Period period = (Period) o;
    return start.equals(period.start) && Objects.equals(end, period.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }
}
