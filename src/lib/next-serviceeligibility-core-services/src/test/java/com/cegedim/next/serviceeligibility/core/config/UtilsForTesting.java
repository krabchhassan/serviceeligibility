package com.cegedim.next.serviceeligibility.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONException;
import org.json.JSONObject;

public final class UtilsForTesting {

  private static final ObjectMapper jsonParser = new ObjectMapper();

  public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
    String content = new String(Files.readAllBytes(Paths.get(filename)));
    return new JSONObject(content);
  }

  public static <T> T createTFromJson(String filePath, Class<T> clazz) throws IOException {
    String json = Files.readString(Paths.get(filePath));
    return jsonParser.readValue(json, clazz);
  }

  public static String toJson(Object obj) {
    ObjectWriter ow = jsonParser.writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
