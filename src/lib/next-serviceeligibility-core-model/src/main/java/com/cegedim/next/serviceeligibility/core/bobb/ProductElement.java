package com.cegedim.next.serviceeligibility.core.bobb;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductElement implements Serializable {
  @EqualsAndHashCode.Include @NotNull private String codeOffer;
  @EqualsAndHashCode.Include @NotNull private String codeProduct;
  @EqualsAndHashCode.Include @NotNull private String codeBenefitNature;
  @EqualsAndHashCode.Include @NotNull private String codeAmc;

  private LocalDateTime from;
  private LocalDateTime to;
  private LocalDateTime effectiveDate;
}
