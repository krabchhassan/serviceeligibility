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
 * &lt;p&gt;Classe Java pour infoOS complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoOS"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="LIBELLE_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="NUM_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-16"/&amp;gt; &amp;lt;element
 * name="PERIMETRE_SERVICE" type="{http://www.almerys.com/NormeV3}infoPerimetre"
 * maxOccurs="unbounded"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoOS",
    propOrder = {"libelleos", "numos", "perimetreservices"})
public class InfoOS implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "LIBELLE_OS", required = true)
  protected String libelleos;

  @XmlElement(name = "NUM_OS", required = true)
  protected String numos;

  @XmlElement(name = "PERIMETRE_SERVICE", required = true)
  protected List<InfoPerimetre> perimetreservices;

  /**
   * Obtient la valeur de la propriété libelleos.
   *
   * @return possible object is {@link String }
   */
  public String getLIBELLEOS() {
    return libelleos;
  }

  /**
   * Définit la valeur de la propriété libelleos.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIBELLEOS(String value) {
    this.libelleos = value;
  }

  /**
   * Obtient la valeur de la propriété numos.
   *
   * @return possible object is {@link String }
   */
  public String getNUMOS() {
    return numos;
  }

  /**
   * Définit la valeur de la propriété numos.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMOS(String value) {
    this.numos = value;
  }

  /**
   * Gets the value of the perimetreservices property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
   * perimetreservices property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getPERIMETRESERVICES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoPerimetre }
   */
  public List<InfoPerimetre> getPERIMETRESERVICES() {
    if (perimetreservices == null) {
      perimetreservices = new ArrayList<InfoPerimetre>();
    }
    return this.perimetreservices;
  }
}
