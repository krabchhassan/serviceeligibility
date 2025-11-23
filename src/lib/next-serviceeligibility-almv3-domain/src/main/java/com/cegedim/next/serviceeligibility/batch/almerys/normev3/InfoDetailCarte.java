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
import java.util.ArrayList;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoDetailCarte complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoDetailCarte"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="FOND_CARTE"
 * type="{http://www.almerys.com/NormeV3}string-1-10" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_ANNEXE" type="{http://www.almerys.com/NormeV3}string-1-10" maxOccurs="2"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoDetailCarte",
    propOrder = {"fondcarte", "codeannexes"})
public class InfoDetailCarte implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "FOND_CARTE")
  protected String fondcarte;

  @XmlElement(name = "CODE_ANNEXE")
  protected List<String> codeannexes;

  /**
   * Obtient la valeur de la propriété fondcarte.
   *
   * @return possible object is {@link String }
   */
  public String getFONDCARTE() {
    return fondcarte;
  }

  /**
   * Définit la valeur de la propriété fondcarte.
   *
   * @param value allowed object is {@link String }
   */
  public void setFONDCARTE(String value) {
    this.fondcarte = value;
  }

  /**
   * Gets the value of the codeannexes property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the codeannexes
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getCODEANNEXES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getCODEANNEXES() {
    if (codeannexes == null) {
      codeannexes = new ArrayList<String>();
    }
    return this.codeannexes;
  }
}
