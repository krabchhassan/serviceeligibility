package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.utility.ParametersEnum;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import org.springframework.http.HttpStatus;

public class ParametreControllerUtils {

  private ParametreControllerUtils() {}

  private static final ObjectMapper MAPPER = new ObjectMapper();
  public static final String PARAMETRE_TYPE_REGEX = "^[a-zA-Z_]+$";

  /**
   * Préparation des données entrante
   *
   * @param type
   * @param json
   * @return
   * @throws IOException
   * @throws JsonParseException
   * @throws JsonMappingException
   */
  public static ParametresDto prepareInData(String type, String json) throws IOException {
    // Vérifier si le type contient des caractères spéciaux interdits
    if (!isValidType(type)) {
      throw new RequestValidationException(
          "Type de paramètre contient des caractères spéciaux interdits.", HttpStatus.BAD_REQUEST);
    }
    // We need to store json data on object in order to transform node to
    // destination object
    ObjectNode node = MAPPER.readValue(json, ObjectNode.class);
    ParametresDto parametresDto;

    // Transforming to destination object
    if (ParametersEnum.DOMAINE.getType().equals(type)
        || ParametersEnum.DOMAINE_IS.getType().equals(type)
        || ParametersEnum.DOMAINE_SP.getType().equals(type)) {
      parametresDto = MAPPER.convertValue(node, ParametresDomaineDto.class);
    } else if (ParametersEnum.FORMULE.getType().equals(type)) {
      parametresDto = MAPPER.convertValue(node, ParametresFormuleDto.class);
    } else if (ParametersEnum.SERVICES_METIERS.getType().equals(type)) {
      parametresDto = MAPPER.convertValue(node, ParametresServicesMetiersDto.class);
    } else if (ParametersEnum.PRESTATIONS.getType().equals(type)) {
      parametresDto = MAPPER.convertValue(node, ParametresPrestationDto.class);
    } else {
      parametresDto = MAPPER.convertValue(node, ParametresDto.class);
    }
    return parametresDto;
  }

  // Méthode de validation pour le type de paramètre
  private static boolean isValidType(String type) {
    if (type == null) return false;
    return type.matches(PARAMETRE_TYPE_REGEX);
  }
}
