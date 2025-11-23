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
 * &lt;p&gt;Classe Java pour codeDemande.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * &lt;pre&gt; &amp;lt;simpleType name="codeDemande"&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt; &amp;lt;enumeration value="PN"/&amp;gt;
 * &amp;lt;enumeration value="PU"/&amp;gt; &amp;lt;enumeration value="TJ"/&amp;gt;
 * &amp;lt;enumeration value="ME"/&amp;gt; &amp;lt;enumeration value="MR"/&amp;gt;
 * &amp;lt;enumeration value="MS"/&amp;gt; &amp;lt;enumeration value="IN"/&amp;gt;
 * &amp;lt;enumeration value="BL"/&amp;gt; &amp;lt;enumeration value="DE"/&amp;gt;
 * &amp;lt;enumeration value="GE"/&amp;gt; &amp;lt;enumeration value="GS"/&amp;gt;
 * &amp;lt;enumeration value="RM"/&amp;gt; &amp;lt;enumeration value="AN"/&amp;gt;
 * &amp;lt;enumeration value="RC"/&amp;gt; &amp;lt;enumeration value="TC"/&amp;gt;
 * &amp;lt;enumeration value="IT"/&amp;gt; &amp;lt;enumeration value="ED"/&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/simpleType&amp;gt; &lt;/pre&gt;
 */
@XmlType(name = "codeDemande")
@XmlEnum
public enum CodeDemande {
  PN,
  PU,
  TJ,
  ME,
  MR,
  MS,
  IN,
  BL,
  DE,
  GE,
  GS,
  RM,
  AN,
  RC,
  TC,
  IT,
  ED;

  public String value() {
    return name();
  }

  public static CodeDemande fromValue(String v) {
    return valueOf(v);
  }
}
