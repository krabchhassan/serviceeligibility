package com.cegedim.next.serviceeligibility.batch635.job.helpers;

import java.util.List;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmcReferenceDateLineErrors implements AmcReferenceDateLineHelper {
  private List<String> errors;
}
