//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * &lt;p&gt;Classe Java pour infoAdresseDecomposeeEntreprise complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoAdresseDecomposeeEntreprise"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="RAISON_SOCIALE" type="{http://www.almerys.com/NormeV3}string-1-50"/&amp;gt; &amp;lt;element
 * name="DENOMINATION_COMMERCIALE" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DESTINATAIRE"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="SERVICE_ENTREPRISE" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="COMPLEMENT_CONSTRUCTION"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NUM_VOIE" type="{http://www.almerys.com/NormeV3}integer-5" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CODE_BIS" type="{http://www.almerys.com/NormeV3}string-1"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="TYPE_VOIE"
 * type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NOM_VOIE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="SERVICE_DISTRIBUTION" type="{http://www.almerys.com/NormeV3}string-1-38"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="COMPLEMENT_VOIE"
 * type="{http://www.almerys.com/NormeV3}string-1-38" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_POSTAL" type="{http://www.almerys.com/NormeV3}string-1-5"/&amp;gt; &amp;lt;element
 * name="COMMUNE" type="{http://www.almerys.com/NormeV3}string-1-38" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="PAYS" type="{http://www.almerys.com/NormeV3}string-1-38"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_CEDEX"
 * type="{http://www.almerys.com/NormeV3}string-1-5" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="LIBELLE_CEDEX" type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_EFFET" type="{http://www.w3.org/2001/XMLSchema}dateTime"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoAdresseDecomposeeEntreprise",
    propOrder = {
      "raisonsociale",
      "denominationcommerciale",
      "destinataire",
      "serviceentreprise",
      "complementconstruction",
      "numvoie",
      "codebis",
      "typevoie",
      "nomvoie",
      "servicedistribution",
      "complementvoie",
      "codepostal",
      "commune",
      "pays",
      "codecedex",
      "libellecedex",
      "dateeffet"
    })
public class InfoAdresseDecomposeeEntreprise implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "RAISON_SOCIALE", required = true)
  protected String raisonsociale;

  @XmlElement(name = "DENOMINATION_COMMERCIALE")
  protected String denominationcommerciale;

  @XmlElement(name = "DESTINATAIRE")
  protected String destinataire;

  @XmlElement(name = "SERVICE_ENTREPRISE")
  protected String serviceentreprise;

  @XmlElement(name = "COMPLEMENT_CONSTRUCTION")
  protected String complementconstruction;

  @XmlElement(name = "NUM_VOIE")
  protected BigInteger numvoie;

  @XmlElement(name = "CODE_BIS")
  protected String codebis;

  @XmlElement(name = "TYPE_VOIE")
  protected String typevoie;

  @XmlElement(name = "NOM_VOIE", required = true)
  protected String nomvoie;

  @XmlElement(name = "SERVICE_DISTRIBUTION")
  protected String servicedistribution;

  @XmlElement(name = "COMPLEMENT_VOIE")
  protected String complementvoie;

  @XmlElement(name = "CODE_POSTAL", required = true)
  protected String codepostal;

  @XmlElement(name = "COMMUNE")
  protected String commune;

  @XmlElement(name = "PAYS")
  protected String pays;

  @XmlElement(name = "CODE_CEDEX")
  protected String codecedex;

  @XmlElement(name = "LIBELLE_CEDEX")
  protected String libellecedex;

  @XmlElement(name = "DATE_EFFET", type = String.class)
  @XmlJavaTypeAdapter(DateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  protected Date dateeffet;

  /**
   * Obtient la valeur de la propriété raisonsociale.
   *
   * @return possible object is {@link String }
   */
  public String getRAISONSOCIALE() {
    return raisonsociale;
  }

  /**
   * Définit la valeur de la propriété raisonsociale.
   *
   * @param value allowed object is {@link String }
   */
  public void setRAISONSOCIALE(String value) {
    this.raisonsociale = value;
  }

  /**
   * Obtient la valeur de la propriété denominationcommerciale.
   *
   * @return possible object is {@link String }
   */
  public String getDENOMINATIONCOMMERCIALE() {
    return denominationcommerciale;
  }

  /**
   * Définit la valeur de la propriété denominationcommerciale.
   *
   * @param value allowed object is {@link String }
   */
  public void setDENOMINATIONCOMMERCIALE(String value) {
    this.denominationcommerciale = value;
  }

  /**
   * Obtient la valeur de la propriété destinataire.
   *
   * @return possible object is {@link String }
   */
  public String getDESTINATAIRE() {
    return destinataire;
  }

  /**
   * Définit la valeur de la propriété destinataire.
   *
   * @param value allowed object is {@link String }
   */
  public void setDESTINATAIRE(String value) {
    this.destinataire = value;
  }

  /**
   * Obtient la valeur de la propriété serviceentreprise.
   *
   * @return possible object is {@link String }
   */
  public String getSERVICEENTREPRISE() {
    return serviceentreprise;
  }

  /**
   * Définit la valeur de la propriété serviceentreprise.
   *
   * @param value allowed object is {@link String }
   */
  public void setSERVICEENTREPRISE(String value) {
    this.serviceentreprise = value;
  }

  /**
   * Obtient la valeur de la propriété complementconstruction.
   *
   * @return possible object is {@link String }
   */
  public String getCOMPLEMENTCONSTRUCTION() {
    return complementconstruction;
  }

  /**
   * Définit la valeur de la propriété complementconstruction.
   *
   * @param value allowed object is {@link String }
   */
  public void setCOMPLEMENTCONSTRUCTION(String value) {
    this.complementconstruction = value;
  }

  /**
   * Obtient la valeur de la propriété numvoie.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getNUMVOIE() {
    return numvoie;
  }

  /**
   * Définit la valeur de la propriété numvoie.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setNUMVOIE(BigInteger value) {
    this.numvoie = value;
  }

  /**
   * Obtient la valeur de la propriété codebis.
   *
   * @return possible object is {@link String }
   */
  public String getCODEBIS() {
    return codebis;
  }

  /**
   * Définit la valeur de la propriété codebis.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEBIS(String value) {
    this.codebis = value;
  }

  /**
   * Obtient la valeur de la propriété typevoie.
   *
   * @return possible object is {@link String }
   */
  public String getTYPEVOIE() {
    return typevoie;
  }

  /**
   * Définit la valeur de la propriété typevoie.
   *
   * @param value allowed object is {@link String }
   */
  public void setTYPEVOIE(String value) {
    this.typevoie = value;
  }

  /**
   * Obtient la valeur de la propriété nomvoie.
   *
   * @return possible object is {@link String }
   */
  public String getNOMVOIE() {
    return nomvoie;
  }

  /**
   * Définit la valeur de la propriété nomvoie.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOMVOIE(String value) {
    this.nomvoie = value;
  }

  /**
   * Obtient la valeur de la propriété servicedistribution.
   *
   * @return possible object is {@link String }
   */
  public String getSERVICEDISTRIBUTION() {
    return servicedistribution;
  }

  /**
   * Définit la valeur de la propriété servicedistribution.
   *
   * @param value allowed object is {@link String }
   */
  public void setSERVICEDISTRIBUTION(String value) {
    this.servicedistribution = value;
  }

  /**
   * Obtient la valeur de la propriété complementvoie.
   *
   * @return possible object is {@link String }
   */
  public String getCOMPLEMENTVOIE() {
    return complementvoie;
  }

  /**
   * Définit la valeur de la propriété complementvoie.
   *
   * @param value allowed object is {@link String }
   */
  public void setCOMPLEMENTVOIE(String value) {
    this.complementvoie = value;
  }

  /**
   * Obtient la valeur de la propriété codepostal.
   *
   * @return possible object is {@link String }
   */
  public String getCODEPOSTAL() {
    return codepostal;
  }

  /**
   * Définit la valeur de la propriété codepostal.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEPOSTAL(String value) {
    this.codepostal = value;
  }

  /**
   * Obtient la valeur de la propriété commune.
   *
   * @return possible object is {@link String }
   */
  public String getCOMMUNE() {
    return commune;
  }

  /**
   * Définit la valeur de la propriété commune.
   *
   * @param value allowed object is {@link String }
   */
  public void setCOMMUNE(String value) {
    this.commune = value;
  }

  /**
   * Obtient la valeur de la propriété pays.
   *
   * @return possible object is {@link String }
   */
  public String getPAYS() {
    return pays;
  }

  /**
   * Définit la valeur de la propriété pays.
   *
   * @param value allowed object is {@link String }
   */
  public void setPAYS(String value) {
    this.pays = value;
  }

  /**
   * Obtient la valeur de la propriété codecedex.
   *
   * @return possible object is {@link String }
   */
  public String getCODECEDEX() {
    return codecedex;
  }

  /**
   * Définit la valeur de la propriété codecedex.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODECEDEX(String value) {
    this.codecedex = value;
  }

  /**
   * Obtient la valeur de la propriété libellecedex.
   *
   * @return possible object is {@link String }
   */
  public String getLIBELLECEDEX() {
    return libellecedex;
  }

  /**
   * Définit la valeur de la propriété libellecedex.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIBELLECEDEX(String value) {
    this.libellecedex = value;
  }

  /**
   * Obtient la valeur de la propriété dateeffet.
   *
   * @return possible object is {@link String }
   */
  public Date getDATEEFFET() {
    return dateeffet;
  }

  /**
   * Définit la valeur de la propriété dateeffet.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEEFFET(Date value) {
    this.dateeffet = value;
  }
}
