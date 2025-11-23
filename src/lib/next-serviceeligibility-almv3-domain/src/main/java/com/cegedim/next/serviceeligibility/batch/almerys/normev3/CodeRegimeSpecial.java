//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

/**
 * &lt;p&gt;Classe Java pour codeRegimeSpecial.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeRegimeSpecial"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="MO"/&amp;gt;
 * &amp;lt;enumeration value="AM"/&amp;gt; &amp;lt;enumeration value="AU"/&amp;gt;
 * &amp;lt;enumeration value="UN"/&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlType(name = "codeRegimeSpecial")
@XmlEnum
public enum CodeRegimeSpecial {
  MO,
  AM,
  AU,
  UN;

  public String value() {
    return name();
  }

  public static CodeRegimeSpecial fromValue(String v) {
    return valueOf(v);
  }
}
