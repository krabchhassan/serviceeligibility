package com.cegedim.next.serviceeligibility.core.utils;

import static com.cegedim.next.serviceeligibility.core.utils.ParametreControllerUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.utility.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import java.io.*;
import org.junit.jupiter.api.*;

class ParametreControllerUtilsTest {

  @Test
  void testPrepareInData_ValidInput_ReturnsParametresDto() throws IOException {
    String type = ParametersEnum.DOMAINE.getType();
    String json =
        "{\"code\": \"OPAU\", \"libelle\": \"Optique et audioprothèse (OPTI + AUDI)\", \"transcodification\": \"OPAU\", \"priorite\": \"1\", \"isCumulGaranties\": false}";

    ParametresDto result = prepareInData(type, json);

    assertTrue(result instanceof ParametresDomaineDto);
    assertDoesNotThrow(() -> prepareInData(type, json));
    assertNotNull(result);
  }

  @Test
  void testPrepareInData_InvalidType_ThrowsResponseStatusException() {
    String invalidType = "prestations§§";
    String json = "{\"code\": \"302\"}";

    assertThrows(RequestValidationException.class, () -> prepareInData(invalidType, json));
  }

  @Test
  void testPrepareInData_JsonWithSpecialCharacters_ReturnCode() throws IOException {
    String type = ParametersEnum.CODES_RENVOI.getType();
    String json =
        "{\"code\": \"42\", \"libelle\": \"PEC Itelis : Optique / Dentaire / Audioprothèse sur www.itelis.fr 50% <= 10 + 100\"}";

    ParametresDto result = prepareInData(type, json);

    assertTrue(result instanceof ParametresDto);
    assertDoesNotThrow(() -> prepareInData(type, json));
    assertNotNull(result);
  }

  @Test
  void testPrepareInData_JsonWithSpecialChracters_Convention() throws IOException {
    String type = ParametersEnum.CONVENTIONNEMENT.getType();
    String json = "{\"code\": \"HD\", \"libelle\": \"Gestion séparée sans DRE / MF\"}";

    ParametresDto result = prepareInData(type, json);

    assertTrue(result instanceof ParametresDto);
    assertDoesNotThrow(() -> prepareInData(type, json));
    assertNotNull(result);
  }
}
