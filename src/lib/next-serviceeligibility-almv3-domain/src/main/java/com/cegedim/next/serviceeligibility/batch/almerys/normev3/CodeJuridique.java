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
 * &lt;p&gt;Classe Java pour codeJuridique.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeJuridique"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="AU"/&amp;gt;
 * &amp;lt;enumeration value="EN"/&amp;gt; &amp;lt;enumeration value="CJ"/&amp;gt;
 * &amp;lt;enumeration value="CO"/&amp;gt; &amp;lt;enumeration value="PA"/&amp;gt;
 * &amp;lt;enumeration value="TU"/&amp;gt; &amp;lt;enumeration value="CU"/&amp;gt;
 * &amp;lt;enumeration value="AE"/&amp;gt; &amp;lt;enumeration value="AA"/&amp;gt;
 * &amp;lt;enumeration value="PC"/&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlType(name = "codeJuridique")
@XmlEnum
public enum CodeJuridique {
  AU,
  EN,
  CJ,
  CO,
  PA,
  TU,
  CU,
  AE,
  AA,
  PC;

  public String value() {
    return name();
  }

  public static CodeJuridique fromValue(String v) {
    return valueOf(v);
  }
}
