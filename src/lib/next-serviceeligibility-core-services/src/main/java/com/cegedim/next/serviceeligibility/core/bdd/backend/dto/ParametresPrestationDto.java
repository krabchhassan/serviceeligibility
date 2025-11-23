package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.model.entity.DomainePrestation;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** This Dto is specific to formula parameters */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParametresPrestationDto extends ParametresDto {

  private List<DomainePrestation> domaines;
}
