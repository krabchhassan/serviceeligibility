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
 * &lt;p&gt;Classe Java pour infoTeamLive complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoTeamLive"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="SERVICE_TLPRO"
 * type="{http://www.almerys.com/NormeV3}infoServiceTLPRO" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="SERVICE_TLMED" type="{http://www.almerys.com/NormeV3}infoServiceTLMED"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_TLCLUB"
 * type="{http://www.almerys.com/NormeV3}infoServiceTLCLUB" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoTeamLive",
    propOrder = {"servicetlpro", "servicetlmed", "servicetlclub"})
public class InfoTeamLive implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "SERVICE_TLPRO")
  protected InfoServiceTLPRO servicetlpro;

  @XmlElement(name = "SERVICE_TLMED")
  protected InfoServiceTLMED servicetlmed;

  @XmlElement(name = "SERVICE_TLCLUB")
  protected InfoServiceTLCLUB servicetlclub;

  /**
   * Obtient la valeur de la propriété servicetlpro.
   *
   * @return possible object is {@link InfoServiceTLPRO }
   */
  public InfoServiceTLPRO getSERVICETLPRO() {
    return servicetlpro;
  }

  /**
   * Définit la valeur de la propriété servicetlpro.
   *
   * @param value allowed object is {@link InfoServiceTLPRO }
   */
  public void setSERVICETLPRO(InfoServiceTLPRO value) {
    this.servicetlpro = value;
  }

  /**
   * Obtient la valeur de la propriété servicetlmed.
   *
   * @return possible object is {@link InfoServiceTLMED }
   */
  public InfoServiceTLMED getSERVICETLMED() {
    return servicetlmed;
  }

  /**
   * Définit la valeur de la propriété servicetlmed.
   *
   * @param value allowed object is {@link InfoServiceTLMED }
   */
  public void setSERVICETLMED(InfoServiceTLMED value) {
    this.servicetlmed = value;
  }

  /**
   * Obtient la valeur de la propriété servicetlclub.
   *
   * @return possible object is {@link InfoServiceTLCLUB }
   */
  public InfoServiceTLCLUB getSERVICETLCLUB() {
    return servicetlclub;
  }

  /**
   * Définit la valeur de la propriété servicetlclub.
   *
   * @param value allowed object is {@link InfoServiceTLCLUB }
   */
  public void setSERVICETLCLUB(InfoServiceTLCLUB value) {
    this.servicetlclub = value;
  }
}
