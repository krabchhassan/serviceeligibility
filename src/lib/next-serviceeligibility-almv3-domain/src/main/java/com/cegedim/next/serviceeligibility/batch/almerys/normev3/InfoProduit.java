//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.PositiveIntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoProduit complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoProduit"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="ORDRE"
 * type="{http://www.almerys.com/NormeV3}positiveInteger-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="REFERENCE_PRODUIT" type="{http://www.almerys.com/NormeV3}string-1-80"/&amp;gt;
 * &amp;lt;element name="DATE_SOUSCRIPTION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_ENTREE_PRODUIT"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_SORTIE_PRODUIT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CARENCE" type="{http://www.almerys.com/NormeV3}infoCadreExercice"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoProduit",
    propOrder = {
      "ordre",
      "referenceproduit",
      "datesouscription",
      "dateentreeproduit",
      "datesortieproduit",
      "carences"
    })
public class InfoProduit implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "ORDRE", type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer ordre;

  @XmlElement(name = "REFERENCE_PRODUIT", required = true)
  protected String referenceproduit;

  @XmlElement(name = "DATE_SOUSCRIPTION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datesouscription;

  @XmlElement(name = "DATE_ENTREE_PRODUIT")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dateentreeproduit;

  @XmlElement(name = "DATE_SORTIE_PRODUIT")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datesortieproduit;

  @XmlElement(name = "CARENCE")
  protected List<InfoCadreExercice> carences;

  /**
   * Obtient la valeur de la propriété ordre.
   *
   * @return possible object is {@link String }
   */
  public Integer getORDRE() {
    return ordre;
  }

  /**
   * Définit la valeur de la propriété ordre.
   *
   * @param value allowed object is {@link String }
   */
  public void setORDRE(Integer value) {
    this.ordre = value;
  }

  /**
   * Obtient la valeur de la propriété referenceproduit.
   *
   * @return possible object is {@link String }
   */
  public String getREFERENCEPRODUIT() {
    return referenceproduit;
  }

  /**
   * Définit la valeur de la propriété referenceproduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFERENCEPRODUIT(String value) {
    this.referenceproduit = value;
  }

  /**
   * Obtient la valeur de la propriété datesouscription.
   *
   * @return possible object is {@link String }
   */
  public String getDATESOUSCRIPTION() {
    return datesouscription;
  }

  /**
   * Définit la valeur de la propriété datesouscription.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATESOUSCRIPTION(String value) {
    this.datesouscription = value;
  }

  /**
   * Obtient la valeur de la propriété dateentreeproduit.
   *
   * @return possible object is {@link String }
   */
  public String getDATEENTREEPRODUIT() {
    return dateentreeproduit;
  }

  /**
   * Définit la valeur de la propriété dateentreeproduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEENTREEPRODUIT(String value) {
    this.dateentreeproduit = value;
  }

  /**
   * Obtient la valeur de la propriété datesortieproduit.
   *
   * @return possible object is {@link String }
   */
  public String getDATESORTIEPRODUIT() {
    return datesortieproduit;
  }

  /**
   * Définit la valeur de la propriété datesortieproduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATESORTIEPRODUIT(String value) {
    this.datesortieproduit = value;
  }

  /**
   * Gets the value of the carences property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the carences
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getCARENCES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoCadreExercice
   * }
   */
  public List<InfoCadreExercice> getCARENCES() {
    if (carences == null) {
      carences = new ArrayList<InfoCadreExercice>();
    }
    return this.carences;
  }
}
