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
 * &lt;p&gt;Classe Java pour infoFraude complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoFraude"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="ACTION_TPS"
 * type="{http://www.almerys.com/NormeV3}infoAction" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="ACTION_TPC" type="{http://www.almerys.com/NormeV3}infoAction" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ACTION_HTP" type="{http://www.almerys.com/NormeV3}infoAction"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoFraude",
    propOrder = {"actiontps", "actiontpc", "actionhtp"})
public class InfoFraude implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "ACTION_TPS")
  protected InfoAction actiontps;

  @XmlElement(name = "ACTION_TPC")
  protected InfoAction actiontpc;

  @XmlElement(name = "ACTION_HTP")
  protected InfoAction actionhtp;

  /**
   * Obtient la valeur de la propriété actiontps.
   *
   * @return possible object is {@link InfoAction }
   */
  public InfoAction getACTIONTPS() {
    return actiontps;
  }

  /**
   * Définit la valeur de la propriété actiontps.
   *
   * @param value allowed object is {@link InfoAction }
   */
  public void setACTIONTPS(InfoAction value) {
    this.actiontps = value;
  }

  /**
   * Obtient la valeur de la propriété actiontpc.
   *
   * @return possible object is {@link InfoAction }
   */
  public InfoAction getACTIONTPC() {
    return actiontpc;
  }

  /**
   * Définit la valeur de la propriété actiontpc.
   *
   * @param value allowed object is {@link InfoAction }
   */
  public void setACTIONTPC(InfoAction value) {
    this.actiontpc = value;
  }

  /**
   * Obtient la valeur de la propriété actionhtp.
   *
   * @return possible object is {@link InfoAction }
   */
  public InfoAction getACTIONHTP() {
    return actionhtp;
  }

  /**
   * Définit la valeur de la propriété actionhtp.
   *
   * @param value allowed object is {@link InfoAction }
   */
  public void setACTIONHTP(InfoAction value) {
    this.actionhtp = value;
  }
}
