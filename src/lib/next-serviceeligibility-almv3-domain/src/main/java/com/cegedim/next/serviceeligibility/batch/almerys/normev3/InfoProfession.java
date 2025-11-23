//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoProfession complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoProfession"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="EXERCICE_PROFESSIONEL"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="STATUT" type="{http://www.almerys.com/NormeV3}codeStatut"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoProfession",
    propOrder = {"exerciceprofessionel", "statut"})
public class InfoProfession implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "EXERCICE_PROFESSIONEL")
  protected String exerciceprofessionel;

  @XmlElement(name = "STATUT", required = true)
  @XmlSchemaType(name = "string")
  protected CodeStatut statut;

  /**
   * Obtient la valeur de la propriété exerciceprofessionel.
   *
   * @return possible object is {@link String }
   */
  public String getEXERCICEPROFESSIONEL() {
    return exerciceprofessionel;
  }

  /**
   * Définit la valeur de la propriété exerciceprofessionel.
   *
   * @param value allowed object is {@link String }
   */
  public void setEXERCICEPROFESSIONEL(String value) {
    this.exerciceprofessionel = value;
  }

  /**
   * Obtient la valeur de la propriété statut.
   *
   * @return possible object is {@link CodeStatut }
   */
  public CodeStatut getSTATUT() {
    return statut;
  }

  /**
   * Définit la valeur de la propriété statut.
   *
   * @param value allowed object is {@link CodeStatut }
   */
  public void setSTATUT(CodeStatut value) {
    this.statut = value;
  }
}
