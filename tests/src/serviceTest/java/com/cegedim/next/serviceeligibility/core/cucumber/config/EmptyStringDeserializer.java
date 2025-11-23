package com.cegedim.next.serviceeligibility.core.cucumber.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.io.IOException;

public class EmptyStringDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

    if (jp.getCurrentToken() == JsonToken.VALUE_STRING && "".equals(jp.getText())) {
      return null;
    }

    return StringDeserializer.instance.deserialize(jp, ctxt);
  }
}
