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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour anonymous complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="ENTETE" type="{http://www.almerys.com/NormeV3}infoEntete"/&amp;gt; &amp;lt;element
 * name="OFFREUR_SERVICE" type="{http://www.almerys.com/NormeV3}infoOS"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"entete", "offreurservice"})
@XmlRootElement(name = "FICHIER")
public class FICHIER implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "ENTETE", required = true)
  protected InfoEntete entete;

  @XmlElement(name = "OFFREUR_SERVICE", required = true)
  protected InfoOS offreurservice;

  /**
   * Obtient la valeur de la propriété entete.
   *
   * @return possible object is {@link InfoEntete }
   */
  public InfoEntete getENTETE() {
    return entete;
  }

  /**
   * Définit la valeur de la propriété entete.
   *
   * @param value allowed object is {@link InfoEntete }
   */
  public void setENTETE(InfoEntete value) {
    this.entete = value;
  }

  /**
   * Obtient la valeur de la propriété offreurservice.
   *
   * @return possible object is {@link InfoOS }
   */
  public InfoOS getOFFREURSERVICE() {
    return offreurservice;
  }

  /**
   * Définit la valeur de la propriété offreurservice.
   *
   * @param value allowed object is {@link InfoOS }
   */
  public void setOFFREURSERVICE(InfoOS value) {
    this.offreurservice = value;
  }
}
