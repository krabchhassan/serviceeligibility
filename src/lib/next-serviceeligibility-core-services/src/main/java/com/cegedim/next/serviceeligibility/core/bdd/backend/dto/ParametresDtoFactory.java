package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.utility.ParametersEnum;

/** Factory to get the good kind of dto parameter */
public class ParametresDtoFactory {

  public ParametresDto getParametresDto(String type) {
    if (ParametersEnum.DOMAINE.getType().equals(type)
        || ParametersEnum.DOMAINE_IS.getType().equals(type)
        || ParametersEnum.DOMAINE_SP.getType().equals(type)) {
      return new ParametresDomaineDto();
    } else if (ParametersEnum.FORMULE.getType().equals(type)) {
      return new ParametresFormuleDto();
    } else if (ParametersEnum.SERVICES_METIERS.getType().equals(type)) {
      return new ParametresServicesMetiersDto();
    } else if (ParametersEnum.PRESTATIONS.getType().equals(type)) {
      return new ParametresPrestationDto();
    }
    return new ParametresDto();
  }
}
