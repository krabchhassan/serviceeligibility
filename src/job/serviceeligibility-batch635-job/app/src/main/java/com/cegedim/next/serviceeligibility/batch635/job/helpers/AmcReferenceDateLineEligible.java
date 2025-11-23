package com.cegedim.next.serviceeligibility.batch635.job.helpers;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmcReferenceDateLineEligible implements AmcReferenceDateLineHelper {
  private String amc;
  private String referenceDate;
}
