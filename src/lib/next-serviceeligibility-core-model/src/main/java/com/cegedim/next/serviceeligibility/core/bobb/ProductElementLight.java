package com.cegedim.next.serviceeligibility.core.bobb;

import java.io.Serializable;
import lombok.Data;

@Data
public class ProductElementLight implements Serializable {
  private static final long serialVersionUID = 1L;
  private String codeOffer;
  private String codeProduct;
  private String codeAmc;
}
