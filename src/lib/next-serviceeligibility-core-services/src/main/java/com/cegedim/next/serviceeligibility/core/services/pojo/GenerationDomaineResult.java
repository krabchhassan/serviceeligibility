package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerationDomaineResult {

  List<DomaineDroit> domaineDroitList;
  private boolean isWarning;
}
