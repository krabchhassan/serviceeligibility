package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** This Dto is specific to formula parameters */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParametresFormuleDto extends ParametresDto {

  private Boolean param1;
  private Boolean param2;
  private Boolean param3;
  private Boolean param4;
  private Boolean param5;
  private Boolean param6;
  private Boolean param7;
  private Boolean param8;
  private Boolean param9;
  private Boolean param10;
}
