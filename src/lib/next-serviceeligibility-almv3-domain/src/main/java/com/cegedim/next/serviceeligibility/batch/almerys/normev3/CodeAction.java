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
 * &lt;p&gt;Classe Java pour codeAction.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeAction"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration
 * value="PAYER"/&amp;gt; &amp;lt;enumeration value="CONTROLER"/&amp;gt; &amp;lt;enumeration
 * value="REJETER"/&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt; &lt;/pre&gt;
 */
@XmlType(name = "codeAction")
@XmlEnum
public enum CodeAction {

  /** Payer */
  PAYER,

  /** Contrôler */
  CONTROLER,

  /** Rejeter */
  REJETER;

  public String value() {
    return name();
  }

  public static CodeAction fromValue(String v) {
    return valueOf(v);
  }
}
