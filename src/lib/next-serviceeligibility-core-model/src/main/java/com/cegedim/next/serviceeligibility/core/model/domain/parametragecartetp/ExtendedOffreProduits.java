package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.model.domain.pw.OffreProduits;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExtendedOffreProduits extends OffreProduits {
  private String versionOffre;
}
