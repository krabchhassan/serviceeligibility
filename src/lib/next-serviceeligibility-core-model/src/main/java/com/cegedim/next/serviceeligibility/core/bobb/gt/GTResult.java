package com.cegedim.next.serviceeligibility.core.bobb.gt;

import com.cegedim.next.serviceeligibility.core.bobb.ProductElementDerived;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GTResult {

  @NotNull private String codeInsurer;
  @NotNull private String codeContractElement;
  @NotNull private boolean gtExist;
  private String label;
  private boolean ignored;
  private List<ProductElementDerived> productElements = new ArrayList<>();
}
