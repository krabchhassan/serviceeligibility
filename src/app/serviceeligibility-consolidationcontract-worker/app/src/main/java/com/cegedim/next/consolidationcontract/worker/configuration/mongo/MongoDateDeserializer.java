package com.cegedim.next.consolidationcontract.worker.configuration.mongo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import java.io.IOException;
import java.util.Date;

public class MongoDateDeserializer extends JsonDeserializer<Date> {
  public static final MongoDateDeserializer INSTANCE = new MongoDateDeserializer();

  @Override
  public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);
    if (node.has("$date")) {
      long dateValue = node.get("$date").asLong();
      return new Date(dateValue);
    }
    return DateDeserializers.DateDeserializer.instance.deserialize(p, ctxt);
  }
}
