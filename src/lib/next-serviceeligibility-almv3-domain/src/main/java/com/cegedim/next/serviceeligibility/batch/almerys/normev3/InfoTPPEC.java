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
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoTP_PEC complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoTP_PEC"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="BENEFICIAIRE"
 * type="{http://www.almerys.com/NormeV3}infoBenef"/&amp;gt; &amp;lt;element name="SERVICE_TP"
 * type="{http://www.almerys.com/NormeV3}infoServiceTP" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoTP_PEC",
    propOrder = {"beneficiaire", "servicetp"})
public class InfoTPPEC implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "BENEFICIAIRE", required = true)
  protected InfoBenef beneficiaire;

  @XmlElement(name = "SERVICE_TP")
  protected InfoServiceTP servicetp;

  /**
   * Obtient la valeur de la propriété beneficiaire.
   *
   * @return possible object is {@link InfoBenef }
   */
  public InfoBenef getBENEFICIAIRE() {
    return beneficiaire;
  }

  /**
   * Définit la valeur de la propriété beneficiaire.
   *
   * @param value allowed object is {@link InfoBenef }
   */
  public void setBENEFICIAIRE(InfoBenef value) {
    this.beneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété servicetp.
   *
   * @return possible object is {@link InfoServiceTP }
   */
  public InfoServiceTP getSERVICETP() {
    return servicetp;
  }

  /**
   * Définit la valeur de la propriété servicetp.
   *
   * @param value allowed object is {@link InfoServiceTP }
   */
  public void setSERVICETP(InfoServiceTP value) {
    this.servicetp = value;
  }
}
