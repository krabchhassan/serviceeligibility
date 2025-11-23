//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.PositiveIntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoRO complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoRO"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="CODE_GRAND_REGIME"
 * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_GESTION" type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="TYPE_REGIME" type="{http://www.almerys.com/NormeV3}string-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NNI"
 * type="{http://www.almerys.com/NormeV3}string-1-13" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_CAISSE_RO" type="{http://www.almerys.com/NormeV3}string-1-3" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CODE_GUICHET" type="{http://www.almerys.com/NormeV3}string-1-4"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_ORGANISME"
 * type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CENTRE_SS" type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CODE_SERVICE_AMO" type="{http://www.almerys.com/NormeV3}positiveInteger-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SPECIFIQUE_SNCF_CAMAC"
 * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="PERMANENT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_FOMULAIRE_E112" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoRO",
    propOrder = {
      "codegrandregime",
      "codegestion",
      "typeregime",
      "nni",
      "codecaissero",
      "codeguichet",
      "codeorganisme",
      "centress",
      "codeserviceamo",
      "specifiquesncfcamac",
      "permanent",
      "datefomulairee112"
    })
public class InfoRO implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "CODE_GRAND_REGIME")
  protected String codegrandregime;

  @XmlElement(name = "CODE_GESTION")
  protected String codegestion;

  @XmlElement(name = "TYPE_REGIME")
  protected String typeregime;

  @XmlElement(name = "NNI")
  protected String nni;

  @XmlElement(name = "CODE_CAISSE_RO")
  protected String codecaissero;

  @XmlElement(name = "CODE_GUICHET")
  protected String codeguichet;

  @XmlElement(name = "CODE_ORGANISME")
  protected String codeorganisme;

  @XmlElement(name = "CENTRE_SS")
  protected String centress;

  @XmlElement(name = "CODE_SERVICE_AMO", type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer codeserviceamo;

  @XmlElement(name = "SPECIFIQUE_SNCF_CAMAC")
  protected String specifiquesncfcamac;

  @XmlElement(name = "PERMANENT", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean permanent;

  @XmlElement(name = "DATE_FOMULAIRE_E112")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefomulairee112;

  /**
   * Obtient la valeur de la propriété codegrandregime.
   *
   * @return possible object is {@link String }
   */
  public String getCODEGRANDREGIME() {
    return codegrandregime;
  }

  /**
   * Définit la valeur de la propriété codegrandregime.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEGRANDREGIME(String value) {
    this.codegrandregime = value;
  }

  /**
   * Obtient la valeur de la propriété codegestion.
   *
   * @return possible object is {@link String }
   */
  public String getCODEGESTION() {
    return codegestion;
  }

  /**
   * Définit la valeur de la propriété codegestion.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEGESTION(String value) {
    this.codegestion = value;
  }

  /**
   * Obtient la valeur de la propriété typeregime.
   *
   * @return possible object is {@link String }
   */
  public String getTYPEREGIME() {
    return typeregime;
  }

  /**
   * Définit la valeur de la propriété typeregime.
   *
   * @param value allowed object is {@link String }
   */
  public void setTYPEREGIME(String value) {
    this.typeregime = value;
  }

  /**
   * Obtient la valeur de la propriété nni.
   *
   * @return possible object is {@link String }
   */
  public String getNNI() {
    return nni;
  }

  /**
   * Définit la valeur de la propriété nni.
   *
   * @param value allowed object is {@link String }
   */
  public void setNNI(String value) {
    this.nni = value;
  }

  /**
   * Obtient la valeur de la propriété codecaissero.
   *
   * @return possible object is {@link String }
   */
  public String getCODECAISSERO() {
    return codecaissero;
  }

  /**
   * Définit la valeur de la propriété codecaissero.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODECAISSERO(String value) {
    this.codecaissero = value;
  }

  /**
   * Obtient la valeur de la propriété codeguichet.
   *
   * @return possible object is {@link String }
   */
  public String getCODEGUICHET() {
    return codeguichet;
  }

  /**
   * Définit la valeur de la propriété codeguichet.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEGUICHET(String value) {
    this.codeguichet = value;
  }

  /**
   * Obtient la valeur de la propriété codeorganisme.
   *
   * @return possible object is {@link String }
   */
  public String getCODEORGANISME() {
    return codeorganisme;
  }

  /**
   * Définit la valeur de la propriété codeorganisme.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEORGANISME(String value) {
    this.codeorganisme = value;
  }

  /**
   * Obtient la valeur de la propriété centress.
   *
   * @return possible object is {@link String }
   */
  public String getCENTRESS() {
    return centress;
  }

  /**
   * Définit la valeur de la propriété centress.
   *
   * @param value allowed object is {@link String }
   */
  public void setCENTRESS(String value) {
    this.centress = value;
  }

  /**
   * Obtient la valeur de la propriété codeserviceamo.
   *
   * @return possible object is {@link String }
   */
  public Integer getCODESERVICEAMO() {
    return codeserviceamo;
  }

  /**
   * Définit la valeur de la propriété codeserviceamo.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODESERVICEAMO(Integer value) {
    this.codeserviceamo = value;
  }

  /**
   * Obtient la valeur de la propriété specifiquesncfcamac.
   *
   * @return possible object is {@link String }
   */
  public String getSPECIFIQUESNCFCAMAC() {
    return specifiquesncfcamac;
  }

  /**
   * Définit la valeur de la propriété specifiquesncfcamac.
   *
   * @param value allowed object is {@link String }
   */
  public void setSPECIFIQUESNCFCAMAC(String value) {
    this.specifiquesncfcamac = value;
  }

  /**
   * Obtient la valeur de la propriété permanent.
   *
   * @return possible object is {@link String }
   */
  public Boolean isPERMANENT() {
    return permanent;
  }

  /**
   * Définit la valeur de la propriété permanent.
   *
   * @param value allowed object is {@link String }
   */
  public void setPERMANENT(Boolean value) {
    this.permanent = value;
  }

  /**
   * Obtient la valeur de la propriété datefomulairee112.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFOMULAIREE112() {
    return datefomulairee112;
  }

  /**
   * Définit la valeur de la propriété datefomulairee112.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFOMULAIREE112(String value) {
    this.datefomulairee112 = value;
  }
}
