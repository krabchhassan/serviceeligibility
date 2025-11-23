package com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;

/**
 * Adapteur du type Date. Elle force l'utilisation du type {@code String} a la place du type {@code
 * Date}
 */
public class DateAdapter extends XmlAdapter<String, String> {

  @Override
  public String marshal(String v) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
    return sdfOut.format(sdf.parse(v));
  }

  @Override
  public String unmarshal(String v) throws Exception {
    return null;
  }
}
