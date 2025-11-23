package com.cegedim.next.serviceeligibility.core.bobb;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductElementDerived implements Serializable {
  private static final long serialVersionUID = 1L;
  private String codeOffer;
  private String codeProduct;
  private String codeBenefitNature;
  private String codeAmc;
  private LocalDateTime from;
  private LocalDateTime to;
}
