package com.cegedim.next.serviceeligibility.core.utility;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Allows to unserialize automatically in "yyyy-MM-dd" format. Otherwise, Spring Boot would use the
 * regular deserializer and it would be in long format. Ie : 2017-01-01T08:08:000.
 */
public class DateSerializer extends JsonSerializer<XMLGregorianCalendar> {

  @Override
  public void serialize(
      XMLGregorianCalendar value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    value.setTimezone(0);
    var res = value.toXMLFormat();
    if (res.contains("Z")) {
      res = res.replace("Z", "+0000");
    }
    gen.writeString(res);
  }
}
