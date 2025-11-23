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
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoRC complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoRC"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;choice&amp;gt; &amp;lt;element name="AMC" type="{http://www.almerys.com/NormeV3}infoAMC"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="MUTUELLE"
 * type="{http://www.almerys.com/NormeV3}infoMutuelle" minOccurs="0"/&amp;gt;
 * &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoRC",
    propOrder = {"mutuelle", "amc"})
@XmlSeeAlso({
  com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoServiceCarteVitale.BENEFRC
      .RC
      .NATURERC
      .class
})
public class InfoRC implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "MUTUELLE")
  protected InfoMutuelle mutuelle;

  @XmlElement(name = "AMC")
  protected InfoAMC amc;

  /**
   * Obtient la valeur de la propriété mutuelle.
   *
   * @return possible object is {@link InfoMutuelle }
   */
  public InfoMutuelle getMUTUELLE() {
    return mutuelle;
  }

  /**
   * Définit la valeur de la propriété mutuelle.
   *
   * @param value allowed object is {@link InfoMutuelle }
   */
  public void setMUTUELLE(InfoMutuelle value) {
    this.mutuelle = value;
  }

  /**
   * Obtient la valeur de la propriété amc.
   *
   * @return possible object is {@link InfoAMC }
   */
  public InfoAMC getAMC() {
    return amc;
  }

  /**
   * Définit la valeur de la propriété amc.
   *
   * @param value allowed object is {@link InfoAMC }
   */
  public void setAMC(InfoAMC value) {
    this.amc = value;
  }
}
