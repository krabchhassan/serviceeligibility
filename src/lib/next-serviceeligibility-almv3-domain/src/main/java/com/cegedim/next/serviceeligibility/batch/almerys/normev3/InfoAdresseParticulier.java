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
 * &lt;p&gt;Classe Java pour infoAdresseParticulier complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoAdresseParticulier"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;choice&amp;gt; &amp;lt;element
 * name="ADRESSE_AGREGEE" type="{http://www.almerys.com/NormeV3}infoAdresseAgregeeParticulier"
 * maxOccurs="unbounded"/&amp;gt; &amp;lt;element name="ADRESSE_DECOMPOSEE"
 * type="{http://www.almerys.com/NormeV3}infoAdresseDecomposeeParticulier"
 * maxOccurs="unbounded"/&amp;gt; &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoAdresseParticulier",
    propOrder = {"adressedecomposees", "adresseagregees"})
public class InfoAdresseParticulier implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "ADRESSE_DECOMPOSEE")
  protected List<InfoAdresseDecomposeeParticulier> adressedecomposees;

  @XmlElement(name = "ADRESSE_AGREGEE")
  protected List<InfoAdresseAgregeeParticulier> adresseagregees;

  /**
   * Gets the value of the adressedecomposees property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
   * adressedecomposees property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getADRESSEDECOMPOSEES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
   * InfoAdresseDecomposeeParticulier }
   */
  public List<InfoAdresseDecomposeeParticulier> getADRESSEDECOMPOSEES() {
    if (adressedecomposees == null) {
      adressedecomposees = new ArrayList<InfoAdresseDecomposeeParticulier>();
    }
    return this.adressedecomposees;
  }

  /**
   * Gets the value of the adresseagregees property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the adresseagregees
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getADRESSEAGREGEES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
   * InfoAdresseAgregeeParticulier }
   */
  public List<InfoAdresseAgregeeParticulier> getADRESSEAGREGEES() {
    if (adresseagregees == null) {
      adresseagregees = new ArrayList<InfoAdresseAgregeeParticulier>();
    }
    return this.adresseagregees;
  }
}
