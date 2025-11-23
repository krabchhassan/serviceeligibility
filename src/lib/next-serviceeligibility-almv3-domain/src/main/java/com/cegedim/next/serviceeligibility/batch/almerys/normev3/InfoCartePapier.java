//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
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
 * &lt;p&gt;Classe Java pour InfoCartePapier complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="InfoCartePapier"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_INTERNE_OS_PORTEUR"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element
 * name="REF_INTERNE_OS_PORTE" type="{http://www.almerys.com/NormeV3}string-1-30"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="NNI_PORTEUR"
 * type="{http://www.almerys.com/NormeV3}string-1-13"/&amp;gt; &amp;lt;element
 * name="ACTIVATION_DESACTIVATION" type="{http://www.almerys.com/NormeV3}codeActivation"/&amp;gt;
 * &amp;lt;element name="DATE_RESTITUTION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="MOTIF_RESTITUTION"
 * type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="ENVOI" type="{http://www.almerys.com/NormeV3}codeEnvoi"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "InfoCartePapier",
    propOrder = {
      "refinterneosporteur",
      "refinterneosportes",
      "nniporteur",
      "activationdesactivation",
      "daterestitution",
      "motifrestitution",
      "envoi"
    })
public class InfoCartePapier implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_INTERNE_OS_PORTEUR", required = true)
  protected String refinterneosporteur;

  @XmlElement(name = "REF_INTERNE_OS_PORTE")
  protected List<String> refinterneosportes;

  @XmlElement(name = "NNI_PORTEUR", required = true)
  protected String nniporteur;

  @XmlElement(name = "ACTIVATION_DESACTIVATION", required = true)
  @XmlSchemaType(name = "string")
  protected CodeActivation activationdesactivation;

  @XmlElement(name = "DATE_RESTITUTION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String daterestitution;

  @XmlElement(name = "MOTIF_RESTITUTION")
  protected String motifrestitution;

  @XmlElement(name = "ENVOI", required = true)
  @XmlSchemaType(name = "string")
  protected CodeEnvoi envoi;

  /**
   * Obtient la valeur de la propriété refinterneosporteur.
   *
   * @return possible object is {@link String }
   */
  public String getREFINTERNEOSPORTEUR() {
    return refinterneosporteur;
  }

  /**
   * Définit la valeur de la propriété refinterneosporteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFINTERNEOSPORTEUR(String value) {
    this.refinterneosporteur = value;
  }

  /**
   * Gets the value of the refinterneosportes property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
   * refinterneosportes property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getREFINTERNEOSPORTES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getREFINTERNEOSPORTES() {
    if (refinterneosportes == null) {
      refinterneosportes = new ArrayList<String>();
    }
    return this.refinterneosportes;
  }

  /**
   * Obtient la valeur de la propriété nniporteur.
   *
   * @return possible object is {@link String }
   */
  public String getNNIPORTEUR() {
    return nniporteur;
  }

  /**
   * Définit la valeur de la propriété nniporteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setNNIPORTEUR(String value) {
    this.nniporteur = value;
  }

  /**
   * Obtient la valeur de la propriété activationdesactivation.
   *
   * @return possible object is {@link CodeActivation }
   */
  public CodeActivation getACTIVATIONDESACTIVATION() {
    return activationdesactivation;
  }

  /**
   * Définit la valeur de la propriété activationdesactivation.
   *
   * @param value allowed object is {@link CodeActivation }
   */
  public void setACTIVATIONDESACTIVATION(CodeActivation value) {
    this.activationdesactivation = value;
  }

  /**
   * Obtient la valeur de la propriété daterestitution.
   *
   * @return possible object is {@link String }
   */
  public String getDATERESTITUTION() {
    return daterestitution;
  }

  /**
   * Définit la valeur de la propriété daterestitution.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATERESTITUTION(String value) {
    this.daterestitution = value;
  }

  /**
   * Obtient la valeur de la propriété motifrestitution.
   *
   * @return possible object is {@link String }
   */
  public String getMOTIFRESTITUTION() {
    return motifrestitution;
  }

  /**
   * Définit la valeur de la propriété motifrestitution.
   *
   * @param value allowed object is {@link String }
   */
  public void setMOTIFRESTITUTION(String value) {
    this.motifrestitution = value;
  }

  /**
   * Obtient la valeur de la propriété envoi.
   *
   * @return possible object is {@link CodeEnvoi }
   */
  public CodeEnvoi getENVOI() {
    return envoi;
  }

  /**
   * Définit la valeur de la propriété envoi.
   *
   * @param value allowed object is {@link CodeEnvoi }
   */
  public void setENVOI(CodeEnvoi value) {
    this.envoi = value;
  }
}
