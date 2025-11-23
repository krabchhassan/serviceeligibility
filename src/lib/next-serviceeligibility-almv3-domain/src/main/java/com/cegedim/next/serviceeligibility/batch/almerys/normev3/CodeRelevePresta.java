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
 * &lt;p&gt;Classe Java pour codeRelevePresta.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeRelevePresta"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration
 * value="PAPIER"/&amp;gt; &amp;lt;enumeration value="EMAIL"/&amp;gt; &amp;lt;enumeration
 * value="AUCUN"/&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt; &lt;/pre&gt;
 */
@XmlType(name = "codeRelevePresta")
@XmlEnum
public enum CodeRelevePresta {

  /** PAPIER : envoi postal */
  PAPIER,

  /** EMAIL : messagerie électronique */
  EMAIL,

  /** AUCUN : pas d'envoi */
  AUCUN;

  public String value() {
    return name();
  }

  public static CodeRelevePresta fromValue(String v) {
    return valueOf(v);
  }
}
