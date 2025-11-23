package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PersonAICommun;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonAIV5 extends PersonAICommun {
  private List<ContratV5> contrats;

  public void setContrats(List<ContratV5> newContrats) {
    if (newContrats != null) {
      this.contrats = newContrats;
    }
  }
}
