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
 * &lt;p&gt;Classe Java pour infoServiceTLPRO complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoServiceTLPRO"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_INTERNE_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element
 * name="LISTE_DROIT_ACCES" type="{http://www.almerys.com/NormeV3}infoDroitAcces"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="JOIGNABILITE"
 * type="{http://www.almerys.com/NormeV3}infoJoignabilite" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_DEBUT_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element name="DATE_FIN_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="ACTIVATION_DESACTIVATION" type="{http://www.almerys.com/NormeV3}codeActivation"/&amp;gt;
 * &amp;lt;element name="DATE_DEBUT_SUSPENSION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_FIN_SUSPENSION"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="MOTIF_RESILIATION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ENVOI" type="{http://www.almerys.com/NormeV3}codeEnvoi"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoServiceTLPRO",
    propOrder = {
      "refinterneos",
      "listedroitacces",
      "joignabilites",
      "datedebutvalidite",
      "datefinvalidite",
      "activationdesactivation",
      "datedebutsuspension",
      "datefinsuspension",
      "motifresiliation",
      "envoi"
    })
public class InfoServiceTLPRO implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_INTERNE_OS", required = true)
  protected String refinterneos;

  @XmlElement(name = "LISTE_DROIT_ACCES")
  protected List<InfoDroitAcces> listedroitacces;

  @XmlElement(name = "JOIGNABILITE")
  protected List<InfoJoignabilite> joignabilites;

  @XmlElement(name = "DATE_DEBUT_VALIDITE", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutvalidite;

  @XmlElement(name = "DATE_FIN_VALIDITE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinvalidite;

  @XmlElement(name = "ACTIVATION_DESACTIVATION", required = true)
  @XmlSchemaType(name = "string")
  protected CodeActivation activationdesactivation;

  @XmlElement(name = "DATE_DEBUT_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutsuspension;

  @XmlElement(name = "DATE_FIN_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinsuspension;

  @XmlElement(name = "MOTIF_RESILIATION")
  protected String motifresiliation;

  @XmlElement(name = "ENVOI", required = true)
  @XmlSchemaType(name = "string")
  protected CodeEnvoi envoi;

  /**
   * Obtient la valeur de la propriété refinterneos.
   *
   * @return possible object is {@link String }
   */
  public String getREFINTERNEOS() {
    return refinterneos;
  }

  /**
   * Définit la valeur de la propriété refinterneos.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFINTERNEOS(String value) {
    this.refinterneos = value;
  }

  /**
   * Gets the value of the listedroitacces property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the listedroitacces
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getLISTEDROITACCES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoDroitAcces }
   */
  public List<InfoDroitAcces> getLISTEDROITACCES() {
    if (listedroitacces == null) {
      listedroitacces = new ArrayList<InfoDroitAcces>();
    }
    return this.listedroitacces;
  }

  /**
   * Gets the value of the joignabilites property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the joignabilites
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getJOIGNABILITES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoJoignabilite }
   */
  public List<InfoJoignabilite> getJOIGNABILITES() {
    if (joignabilites == null) {
      joignabilites = new ArrayList<InfoJoignabilite>();
    }
    return this.joignabilites;
  }

  /**
   * Obtient la valeur de la propriété datedebutvalidite.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBUTVALIDITE() {
    return datedebutvalidite;
  }

  /**
   * Définit la valeur de la propriété datedebutvalidite.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBUTVALIDITE(String value) {
    this.datedebutvalidite = value;
  }

  /**
   * Obtient la valeur de la propriété datefinvalidite.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFINVALIDITE() {
    return datefinvalidite;
  }

  /**
   * Définit la valeur de la propriété datefinvalidite.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFINVALIDITE(String value) {
    this.datefinvalidite = value;
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
   * Obtient la valeur de la propriété datedebutsuspension.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBUTSUSPENSION() {
    return datedebutsuspension;
  }

  /**
   * Définit la valeur de la propriété datedebutsuspension.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBUTSUSPENSION(String value) {
    this.datedebutsuspension = value;
  }

  /**
   * Obtient la valeur de la propriété datefinsuspension.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFINSUSPENSION() {
    return datefinsuspension;
  }

  /**
   * Définit la valeur de la propriété datefinsuspension.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFINSUSPENSION(String value) {
    this.datefinsuspension = value;
  }

  /**
   * Obtient la valeur de la propriété motifresiliation.
   *
   * @return possible object is {@link String }
   */
  public String getMOTIFRESILIATION() {
    return motifresiliation;
  }

  /**
   * Définit la valeur de la propriété motifresiliation.
   *
   * @param value allowed object is {@link String }
   */
  public void setMOTIFRESILIATION(String value) {
    this.motifresiliation = value;
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
