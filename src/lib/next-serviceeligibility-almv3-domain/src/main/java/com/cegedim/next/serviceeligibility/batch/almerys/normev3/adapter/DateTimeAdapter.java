package com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Adapteur du type DateTime. Elle force l'utilisation du type {@code XMLGregorianCalendar} a la
 * place du type {@code Date}
 */
public class DateTimeAdapter extends XmlAdapter<String, Date> {

  @Override
  public String marshal(Date v) throws Exception {
    SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    return sdfOut.format(v);
  }

  @Override
  public Date unmarshal(String v) throws Exception {
    return null;
  }
}
