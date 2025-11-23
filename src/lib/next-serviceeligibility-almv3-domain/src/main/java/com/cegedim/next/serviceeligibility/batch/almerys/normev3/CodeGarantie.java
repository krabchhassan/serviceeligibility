//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * &lt;p&gt;Classe Java pour codeGarantie.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeGarantie"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="SE"/&amp;gt;
 * &amp;lt;enumeration value="FE"/&amp;gt; &amp;lt;enumeration value="SC"/&amp;gt;
 * &amp;lt;enumeration value="FC"/&amp;gt; &amp;lt;enumeration value="SI"/&amp;gt;
 * &amp;lt;enumeration value="FI"/&amp;gt; &amp;lt;enumeration value="ST"/&amp;gt;
 * &amp;lt;enumeration value="FT"/&amp;gt; &amp;lt;enumeration value="UA"/&amp;gt;
 * &amp;lt;enumeration value="M1"/&amp;gt; &amp;lt;enumeration value="M2"/&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt; &lt;/pre&gt;
 */
@XmlType(name = "codeGarantie")
@XmlEnum
public enum CodeGarantie {
  SE("SE"),
  FE("FE"),
  SC("SC"),
  FC("FC"),
  SI("SI"),
  FI("FI"),
  ST("ST"),
  FT("FT"),
  UA("UA"),
  @XmlEnumValue("M1")
  M_1("M1"),
  @XmlEnumValue("M2")
  M_2("M2");
  private final String value;

  CodeGarantie(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static CodeGarantie fromValue(String v) {
    for (CodeGarantie c : CodeGarantie.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
