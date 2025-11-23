package com.cegedim.next.serviceeligibility.core.features.utils;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

public class ControllerUtils {
  private ControllerUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * @param obj
   * @return
   */
  public static String prepareOutData(Object obj) {
    // On transforme le retour en ObjectMapper afin d'enlever les objets
    // null
    ObjectMapper om = new ObjectMapper();
    om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    String a = null;
    try {
      a = om.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      String message = "Erreur lors de l'écriture du retour " + e.getMessage();
      throw new RequestValidationException(message, HttpStatus.BAD_REQUEST);
    }
    return a;
  }
}
