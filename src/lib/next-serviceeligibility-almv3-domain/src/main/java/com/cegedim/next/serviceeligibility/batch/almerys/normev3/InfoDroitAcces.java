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
 * &lt;p&gt;Classe Java pour infoDroitAcces complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoDroitAcces"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_OS_ACCEDANT"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="REF_OS_ACCEDE"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="MOTIF"
 * type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoDroitAcces",
    propOrder = {"refosaccedant", "refosaccede", "motif"})
public class InfoDroitAcces implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_OS_ACCEDANT", required = true)
  protected String refosaccedant;

  @XmlElement(name = "REF_OS_ACCEDE", required = true)
  protected String refosaccede;

  @XmlElement(name = "MOTIF")
  protected String motif;

  /**
   * Obtient la valeur de la propriété refosaccedant.
   *
   * @return possible object is {@link String }
   */
  public String getREFOSACCEDANT() {
    return refosaccedant;
  }

  /**
   * Définit la valeur de la propriété refosaccedant.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFOSACCEDANT(String value) {
    this.refosaccedant = value;
  }

  /**
   * Obtient la valeur de la propriété refosaccede.
   *
   * @return possible object is {@link String }
   */
  public String getREFOSACCEDE() {
    return refosaccede;
  }

  /**
   * Définit la valeur de la propriété refosaccede.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFOSACCEDE(String value) {
    this.refosaccede = value;
  }

  /**
   * Obtient la valeur de la propriété motif.
   *
   * @return possible object is {@link String }
   */
  public String getMOTIF() {
    return motif;
  }

  /**
   * Définit la valeur de la propriété motif.
   *
   * @param value allowed object is {@link String }
   */
  public void setMOTIF(String value) {
    this.motif = value;
  }
}
