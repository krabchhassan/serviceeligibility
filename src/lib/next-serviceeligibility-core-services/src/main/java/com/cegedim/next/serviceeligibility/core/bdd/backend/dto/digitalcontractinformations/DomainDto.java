package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations;

import java.util.List;
import lombok.Data;

@Data
public class DomainDto {
  private String domainCode;
  private String domainLabel;
  private List<ConventionDto> conventions;
  private List<RegroupedDomainDto> regroupedDomains;
}
