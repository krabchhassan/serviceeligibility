package com.cegedim.next.serviceeligibility.core.almerysProductRef;

import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class LotAlmerys {
  @Id private String id;
  private String code;
  private String dateAjout;
  private String dateSuppressionLogique;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    LotAlmerys that = (LotAlmerys) o;
    return Objects.equals(id, that.id) && Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code);
  }
}
