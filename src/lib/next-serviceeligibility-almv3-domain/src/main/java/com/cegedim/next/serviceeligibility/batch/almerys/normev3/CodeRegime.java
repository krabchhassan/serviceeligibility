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
 * &lt;p&gt;Classe Java pour codeRegime.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeRegime"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="RO"/&amp;gt;
 * &amp;lt;enumeration value="RC"/&amp;gt; &amp;lt;enumeration value="R+"/&amp;gt;
 * &amp;lt;enumeration value="CU"/&amp;gt; &amp;lt;enumeration value="NA"/&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt; &lt;/pre&gt;
 */
@XmlType(name = "codeRegime")
@XmlEnum
public enum CodeRegime {
  RO("RO"),
  RC("RC"),
  @XmlEnumValue("R+")
  R("R+"),
  CU("CU"),
  NA("NA");
  private final String value;

  CodeRegime(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static CodeRegime fromValue(String v) {
    for (CodeRegime c : CodeRegime.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
