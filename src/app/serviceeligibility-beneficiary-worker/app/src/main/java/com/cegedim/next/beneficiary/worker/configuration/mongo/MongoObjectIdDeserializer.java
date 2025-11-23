package com.cegedim.next.beneficiary.worker.configuration.mongo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class MongoObjectIdDeserializer extends JsonDeserializer<String> {
  public static final MongoObjectIdDeserializer INSTANCE = new MongoObjectIdDeserializer();

  @Override
  public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);
    if (node.has("$oid")) {
      return node.get("$oid").asText();
    }
    return node.asText();
  }
}
