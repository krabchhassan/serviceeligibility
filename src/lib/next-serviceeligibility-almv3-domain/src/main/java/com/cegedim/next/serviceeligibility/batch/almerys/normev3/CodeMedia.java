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
 * &lt;p&gt;Classe Java pour codeMedia.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeMedia"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="SM"/&amp;gt;
 * &amp;lt;enumeration value="VB"/&amp;gt; &amp;lt;enumeration value="VF"/&amp;gt;
 * &amp;lt;enumeration value="FA"/&amp;gt; &amp;lt;enumeration value="ME"/&amp;gt;
 * &amp;lt;enumeration value="CO"/&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlType(name = "codeMedia")
@XmlEnum
public enum CodeMedia {
  SM,
  VB,
  VF,
  FA,
  ME,
  CO;

  public String value() {
    return name();
  }

  public static CodeMedia fromValue(String v) {
    return valueOf(v);
  }
}
