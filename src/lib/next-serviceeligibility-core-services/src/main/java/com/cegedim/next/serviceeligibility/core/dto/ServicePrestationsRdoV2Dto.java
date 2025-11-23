package com.cegedim.next.serviceeligibility.core.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServicePrestationsRdoV2Dto extends ServicePrestationsRdoDto implements Serializable {
  private String key;
}
