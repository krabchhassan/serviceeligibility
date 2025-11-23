package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** This Dto is specific to domain parameters */
@Data()
@EqualsAndHashCode(callSuper = true)
public class ParametresServicesMetiersDto extends ParametresDto {

  private String ordre;
  private String icone;
}
