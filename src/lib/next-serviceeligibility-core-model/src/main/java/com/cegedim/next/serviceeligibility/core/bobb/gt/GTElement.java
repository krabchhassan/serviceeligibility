package com.cegedim.next.serviceeligibility.core.bobb.gt;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GTElement {

  @NotNull private String codeInsurer;
  @NotNull private String codeContractElement;
}
