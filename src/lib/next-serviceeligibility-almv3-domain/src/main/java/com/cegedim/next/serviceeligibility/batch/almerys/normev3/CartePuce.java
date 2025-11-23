//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
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
 * &lt;p&gt;Classe Java pour CartePuce complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="CartePuce"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NUM_LOT_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-16" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="ENCART" type="{http://www.almerys.com/NormeV3}infoEncart" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ENCART_CHIFFRE"
 * type="{http://www.almerys.com/NormeV3}infoAdresseAgregeeParticulierChiffree"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="ADRESSE_GROUPEE"
 * type="{http://www.almerys.com/NormeV3}infoAdresseAgregeeEntreprise" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="REF_INTERNE_OS_PORTEUR"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element
 * name="REF_INTERNE_OS_PORTE" type="{http://www.almerys.com/NormeV3}string-1-30"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="ACTIVATION_DESACTIVATION"
 * type="{http://www.almerys.com/NormeV3}codeActivation"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element
 * name="DATE_FIN_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_DEBUT_SUSPENSION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_FIN_SUSPENSION"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_REVOCATION" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="MOTIF_REVOCATION" type="{http://www.almerys.com/NormeV3}string-1-30"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="ENVOI"
 * type="{http://www.almerys.com/NormeV3}codeEnvoi"/&amp;gt; &amp;lt;element name="TYPE_CARTE"
 * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DROITS_AMC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CartePuce",
    propOrder = {
      "numlotos",
      "encart",
      "encartchiffre",
      "adressegroupee",
      "refinterneosporteur",
      "refinterneosportes",
      "activationdesactivation",
      "datedebutvalidite",
      "datefinvalidite",
      "datedebutsuspension",
      "datefinsuspension",
      "daterevocation",
      "motifrevocation",
      "envoi",
      "typecarte",
      "droitsamc"
    })
public class CartePuce implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NUM_LOT_OS")
  protected String numlotos;

  @XmlElement(name = "ENCART")
  protected InfoEncart encart;

  @XmlElement(name = "ENCART_CHIFFRE")
  protected InfoAdresseAgregeeParticulierChiffree encartchiffre;

  @XmlElement(name = "ADRESSE_GROUPEE")
  protected InfoAdresseAgregeeEntreprise adressegroupee;

  @XmlElement(name = "REF_INTERNE_OS_PORTEUR", required = true)
  protected String refinterneosporteur;

  @XmlElement(name = "REF_INTERNE_OS_PORTE")
  protected List<String> refinterneosportes;

  @XmlElement(name = "ACTIVATION_DESACTIVATION", required = true)
  @XmlSchemaType(name = "string")
  protected CodeActivation activationdesactivation;

  @XmlElement(name = "DATE_DEBUT_VALIDITE", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutvalidite;

  @XmlElement(name = "DATE_FIN_VALIDITE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinvalidite;

  @XmlElement(name = "DATE_DEBUT_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutsuspension;

  @XmlElement(name = "DATE_FIN_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinsuspension;

  @XmlElement(name = "DATE_REVOCATION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String daterevocation;

  @XmlElement(name = "MOTIF_REVOCATION")
  protected String motifrevocation;

  @XmlElement(name = "ENVOI", required = true)
  @XmlSchemaType(name = "string")
  protected CodeEnvoi envoi;

  @XmlElement(name = "TYPE_CARTE")
  protected String typecarte;

  @XmlElement(name = "DROITS_AMC", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean droitsamc;

  /**
   * Obtient la valeur de la propriété numlotos.
   *
   * @return possible object is {@link String }
   */
  public String getNUMLOTOS() {
    return numlotos;
  }

  /**
   * Définit la valeur de la propriété numlotos.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMLOTOS(String value) {
    this.numlotos = value;
  }

  /**
   * Obtient la valeur de la propriété encart.
   *
   * @return possible object is {@link InfoEncart }
   */
  public InfoEncart getENCART() {
    return encart;
  }

  /**
   * Définit la valeur de la propriété encart.
   *
   * @param value allowed object is {@link InfoEncart }
   */
  public void setENCART(InfoEncart value) {
    this.encart = value;
  }

  /**
   * Obtient la valeur de la propriété encartchiffre.
   *
   * @return possible object is {@link InfoAdresseAgregeeParticulierChiffree }
   */
  public InfoAdresseAgregeeParticulierChiffree getENCARTCHIFFRE() {
    return encartchiffre;
  }

  /**
   * Définit la valeur de la propriété encartchiffre.
   *
   * @param value allowed object is {@link InfoAdresseAgregeeParticulierChiffree }
   */
  public void setENCARTCHIFFRE(InfoAdresseAgregeeParticulierChiffree value) {
    this.encartchiffre = value;
  }

  /**
   * Obtient la valeur de la propriété adressegroupee.
   *
   * @return possible object is {@link InfoAdresseAgregeeEntreprise }
   */
  public InfoAdresseAgregeeEntreprise getADRESSEGROUPEE() {
    return adressegroupee;
  }

  /**
   * Définit la valeur de la propriété adressegroupee.
   *
   * @param value allowed object is {@link InfoAdresseAgregeeEntreprise }
   */
  public void setADRESSEGROUPEE(InfoAdresseAgregeeEntreprise value) {
    this.adressegroupee = value;
  }

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
   * Obtient la valeur de la propriété daterevocation.
   *
   * @return possible object is {@link String }
   */
  public String getDATEREVOCATION() {
    return daterevocation;
  }

  /**
   * Définit la valeur de la propriété daterevocation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEREVOCATION(String value) {
    this.daterevocation = value;
  }

  /**
   * Obtient la valeur de la propriété motifrevocation.
   *
   * @return possible object is {@link String }
   */
  public String getMOTIFREVOCATION() {
    return motifrevocation;
  }

  /**
   * Définit la valeur de la propriété motifrevocation.
   *
   * @param value allowed object is {@link String }
   */
  public void setMOTIFREVOCATION(String value) {
    this.motifrevocation = value;
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

  /**
   * Obtient la valeur de la propriété typecarte.
   *
   * @return possible object is {@link String }
   */
  public String getTYPECARTE() {
    return typecarte;
  }

  /**
   * Définit la valeur de la propriété typecarte.
   *
   * @param value allowed object is {@link String }
   */
  public void setTYPECARTE(String value) {
    this.typecarte = value;
  }

  /**
   * Obtient la valeur de la propriété droitsamc.
   *
   * @return possible object is {@link String }
   */
  public Boolean isDROITSAMC() {
    return droitsamc;
  }

  /**
   * Définit la valeur de la propriété droitsamc.
   *
   * @param value allowed object is {@link String }
   */
  public void setDROITSAMC(Boolean value) {
    this.droitsamc = value;
  }
}
