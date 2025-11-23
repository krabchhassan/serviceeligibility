package com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapteur du type Boolean. Elle force l'utilisation du type {@code Integer} en {@code 1} ou {@code
 * 0} a la place du type {@code Boolean} {@code true} ou {@code false}.
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean> {

  @Override
  public String marshal(Boolean v) throws Exception {
    if (v != null && v) {
      return "1";
    }
    return "0";
  }

  @Override
  public Boolean unmarshal(String v) throws Exception {
    return null;
  }
}
