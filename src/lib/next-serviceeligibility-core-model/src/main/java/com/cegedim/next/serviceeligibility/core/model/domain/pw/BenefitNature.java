package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import java.util.List;
import lombok.Data;

@Data
public class BenefitNature {
  private String code;
  private String label;
  private List<String> domainList;
}
