package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** This Dto is specific to domain parameters */
@Data()
@EqualsAndHashCode(callSuper = true)
public class ParametresDomaineDto extends ParametresDto {

  private String transcodification;
  private String categorie;
  private String priorite = "0";
  private Boolean isCumulGaranties = false;
  private Boolean isCumulPlafonne = false;
}
