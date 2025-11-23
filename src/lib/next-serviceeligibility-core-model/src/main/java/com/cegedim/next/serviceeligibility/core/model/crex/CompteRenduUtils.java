package com.cegedim.next.serviceeligibility.core.model.crex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompteRenduUtils {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  // Transform List<String> to String[]
  public static String[] listToArray(List<String> list) {
    String[] array = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  public static String objToString(Object obj) {
    try {
      MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      return MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
