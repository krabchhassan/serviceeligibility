package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenefAIV5 extends PersonAIV5 {
  private List<String> services;
  private String key;
  private String environnement;

  @JsonView(value = Version.Advanced.class)
  private List<SocieteEmettrice> societesEmettrices;

  public void setServices(List<String> newServices) {
    if (newServices != null) {
      this.services = newServices;
    }
  }

  public void setKey(String newKey) {
    if (newKey != null) {
      this.key = newKey;
    }
  }

  public void setEnvironnement(String newEnvironnement) {
    if (newEnvironnement != null) {
      this.environnement = newEnvironnement;
    }
  }

  public void setSocietesEmettrices(List<SocieteEmettrice> newSocietesEmettrices) {
    if (newSocietesEmettrices != null) {
      this.societesEmettrices = newSocietesEmettrices;
    }
  }
}
