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
 * &lt;p&gt;Classe Java pour codeCarte.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeCarte"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="CA"/&amp;gt;
 * &amp;lt;enumeration value="CC"/&amp;gt; &amp;lt;enumeration value="CS"/&amp;gt;
 * &amp;lt;enumeration value="MC"/&amp;gt; &amp;lt;enumeration value="EE"/&amp;gt;
 * &amp;lt;enumeration value="MS"/&amp;gt; &amp;lt;enumeration value="RC"/&amp;gt;
 * &amp;lt;enumeration value="RS"/&amp;gt; &amp;lt;enumeration value="NP"/&amp;gt;
 * &amp;lt;enumeration value="QD"/&amp;gt; &amp;lt;enumeration value="QP"/&amp;gt;
 * &amp;lt;enumeration value="QT"/&amp;gt; &amp;lt;enumeration value="RD"/&amp;gt;
 * &amp;lt;enumeration value="RP"/&amp;gt; &amp;lt;enumeration value="RT"/&amp;gt;
 * &amp;lt;enumeration value="EP"/&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlType(name = "codeCarte")
@XmlEnum
public enum CodeCarte {
  CA,
  CC,
  CS,
  MC,
  EE,
  MS,
  RC,
  RS,
  NP,
  QD,
  QP,
  QT,
  RD,
  RP,
  RT,
  EP;

  public String value() {
    return name();
  }

  public static CodeCarte fromValue(String v) {
    return valueOf(v);
  }
}
