package com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;

/**
 * Adapteur du type PositiveInteger. Elle force l'utilisation du type {@code Integer} a la place du
 * type {@code BigInteger}
 */
public class PositiveIntegerAdapter extends XmlAdapter<String, Integer> {

  @Override
  public String marshal(Integer v) throws Exception {
    return BigInteger.valueOf(v).toString();
  }

  @Override
  public Integer unmarshal(String v) throws Exception {
    return null;
  }
}
