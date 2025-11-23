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
import java.util.ArrayList;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoROVtl complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoROVtl"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="TEXTE_AMO"
 * type="{http://www.almerys.com/NormeV3}infoTexteAMO" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="SERVICE_AMO" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="CODE_SERVICE_AMO" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_AMO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_AMO" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;element name="SPECIFIQUE_SNCF_CAMAC" type="{http://www.almerys.com/NormeV3}string-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="PERMANENT"
 * type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DROITS" type="{http://www.almerys.com/NormeV3}infoDroitBenef" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ETM" maxOccurs="3" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="CODE_ETM" type="{http://www.almerys.com/NormeV3}codeEtm"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_ETM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_ETM" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_PROLONGATION"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;element name="E112" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TYPE_EXPERIMENTATION" type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt;
 * &amp;lt;element name="ACTIVITE" type="{http://www.almerys.com/NormeV3}string-1"/&amp;gt;
 * &amp;lt;element name="ARTICLE_REGLEMENT" type="{http://www.almerys.com/NormeV3}string-3"/&amp;gt;
 * &amp;lt;element name="DATE_DEBUT_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
 * &amp;lt;element name="DATE_ETABLISSEMENT" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element name="ACCIDENT_DU_TRAVAIL"
 * maxOccurs="3" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="IDENTIFIANT_AT"
 * type="{http://www.almerys.com/NormeV3}string-9"/&amp;gt; &amp;lt;element name="DROIT_SPECIFIQUE"
 * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_GR" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element
 * name="CODE_CAISSE" type="{http://www.almerys.com/NormeV3}string-3"/&amp;gt; &amp;lt;element
 * name="CODE_CENTRE" type="{http://www.almerys.com/NormeV3}string-4"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoROVtl",
    propOrder = {
      "texteamo",
      "serviceamo",
      "specifiquesncfcamac",
      "permanent",
      "droits",
      "etms",
      "e112",
      "accidentdutravails"
    })
public class InfoROVtl implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "TEXTE_AMO")
  protected InfoTexteAMO texteamo;

  @XmlElement(name = "SERVICE_AMO")
  protected InfoROVtl.SERVICEAMO serviceamo;

  @XmlElement(name = "SPECIFIQUE_SNCF_CAMAC")
  protected String specifiquesncfcamac;

  @XmlElement(name = "PERMANENT", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean permanent;

  @XmlElement(name = "DROITS")
  protected InfoDroitBenef droits;

  @XmlElement(name = "ETM")
  protected List<InfoROVtl.ETM> etms;

  @XmlElement(name = "E112")
  protected InfoROVtl.E112 e112;

  @XmlElement(name = "ACCIDENT_DU_TRAVAIL")
  protected List<InfoROVtl.ACCIDENTDUTRAVAIL> accidentdutravails;

  /**
   * Obtient la valeur de la propriété texteamo.
   *
   * @return possible object is {@link InfoTexteAMO }
   */
  public InfoTexteAMO getTEXTEAMO() {
    return texteamo;
  }

  /**
   * Définit la valeur de la propriété texteamo.
   *
   * @param value allowed object is {@link InfoTexteAMO }
   */
  public void setTEXTEAMO(InfoTexteAMO value) {
    this.texteamo = value;
  }

  /**
   * Obtient la valeur de la propriété serviceamo.
   *
   * @return possible object is {@link InfoROVtl.SERVICEAMO }
   */
  public InfoROVtl.SERVICEAMO getSERVICEAMO() {
    return serviceamo;
  }

  /**
   * Définit la valeur de la propriété serviceamo.
   *
   * @param value allowed object is {@link InfoROVtl.SERVICEAMO }
   */
  public void setSERVICEAMO(InfoROVtl.SERVICEAMO value) {
    this.serviceamo = value;
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
   * Obtient la valeur de la propriété droits.
   *
   * @return possible object is {@link InfoDroitBenef }
   */
  public InfoDroitBenef getDROITS() {
    return droits;
  }

  /**
   * Définit la valeur de la propriété droits.
   *
   * @param value allowed object is {@link InfoDroitBenef }
   */
  public void setDROITS(InfoDroitBenef value) {
    this.droits = value;
  }

  /**
   * Gets the value of the etms property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the etms property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt; getETMS().add(newItem);
   * &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoROVtl.ETM }
   */
  public List<InfoROVtl.ETM> getETMS() {
    if (etms == null) {
      etms = new ArrayList<InfoROVtl.ETM>();
    }
    return this.etms;
  }

  /**
   * Obtient la valeur de la propriété e112.
   *
   * @return possible object is {@link InfoROVtl.E112 }
   */
  public InfoROVtl.E112 getE112() {
    return e112;
  }

  /**
   * Définit la valeur de la propriété e112.
   *
   * @param value allowed object is {@link InfoROVtl.E112 }
   */
  public void setE112(InfoROVtl.E112 value) {
    this.e112 = value;
  }

  /**
   * Gets the value of the accidentdutravails property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
   * accidentdutravails property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getACCIDENTDUTRAVAILS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
   * InfoROVtl.ACCIDENTDUTRAVAIL }
   */
  public List<InfoROVtl.ACCIDENTDUTRAVAIL> getACCIDENTDUTRAVAILS() {
    if (accidentdutravails == null) {
      accidentdutravails = new ArrayList<InfoROVtl.ACCIDENTDUTRAVAIL>();
    }
    return this.accidentdutravails;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="IDENTIFIANT_AT" type="{http://www.almerys.com/NormeV3}string-9"/&amp;gt;
   * &amp;lt;element name="DROIT_SPECIFIQUE" type="{http://www.almerys.com/NormeV3}string-2"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_GR"
   * type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element name="CODE_CAISSE"
   * type="{http://www.almerys.com/NormeV3}string-3"/&amp;gt; &amp;lt;element name="CODE_CENTRE"
   * type="{http://www.almerys.com/NormeV3}string-4"/&amp;gt; &amp;lt;/sequence&amp;gt;
   * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
   * &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"identifiantat", "droitspecifique", "codegr", "codecaisse", "codecentre"})
  public static class ACCIDENTDUTRAVAIL implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "IDENTIFIANT_AT", required = true)
    protected String identifiantat;

    @XmlElement(name = "DROIT_SPECIFIQUE")
    protected String droitspecifique;

    @XmlElement(name = "CODE_GR", required = true)
    protected String codegr;

    @XmlElement(name = "CODE_CAISSE", required = true)
    protected String codecaisse;

    @XmlElement(name = "CODE_CENTRE", required = true)
    protected String codecentre;

    /**
     * Obtient la valeur de la propriété identifiantat.
     *
     * @return possible object is {@link String }
     */
    public String getIDENTIFIANTAT() {
      return identifiantat;
    }

    /**
     * Définit la valeur de la propriété identifiantat.
     *
     * @param value allowed object is {@link String }
     */
    public void setIDENTIFIANTAT(String value) {
      this.identifiantat = value;
    }

    /**
     * Obtient la valeur de la propriété droitspecifique.
     *
     * @return possible object is {@link String }
     */
    public String getDROITSPECIFIQUE() {
      return droitspecifique;
    }

    /**
     * Définit la valeur de la propriété droitspecifique.
     *
     * @param value allowed object is {@link String }
     */
    public void setDROITSPECIFIQUE(String value) {
      this.droitspecifique = value;
    }

    /**
     * Obtient la valeur de la propriété codegr.
     *
     * @return possible object is {@link String }
     */
    public String getCODEGR() {
      return codegr;
    }

    /**
     * Définit la valeur de la propriété codegr.
     *
     * @param value allowed object is {@link String }
     */
    public void setCODEGR(String value) {
      this.codegr = value;
    }

    /**
     * Obtient la valeur de la propriété codecaisse.
     *
     * @return possible object is {@link String }
     */
    public String getCODECAISSE() {
      return codecaisse;
    }

    /**
     * Définit la valeur de la propriété codecaisse.
     *
     * @param value allowed object is {@link String }
     */
    public void setCODECAISSE(String value) {
      this.codecaisse = value;
    }

    /**
     * Obtient la valeur de la propriété codecentre.
     *
     * @return possible object is {@link String }
     */
    public String getCODECENTRE() {
      return codecentre;
    }

    /**
     * Définit la valeur de la propriété codecentre.
     *
     * @param value allowed object is {@link String }
     */
    public void setCODECENTRE(String value) {
      this.codecentre = value;
    }
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="TYPE_EXPERIMENTATION"
   * type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt; &amp;lt;element
   * name="ACTIVITE" type="{http://www.almerys.com/NormeV3}string-1"/&amp;gt; &amp;lt;element
   * name="ARTICLE_REGLEMENT" type="{http://www.almerys.com/NormeV3}string-3"/&amp;gt;
   * &amp;lt;element name="DATE_DEBUT_VALIDITE"
   * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element name="DATE_FIN_VALIDITE"
   * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element
   * name="DATE_ETABLISSEMENT" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {
        "typeexperimentation",
        "activite",
        "articlereglement",
        "datedebutvalidite",
        "datefinvalidite",
        "dateetablissement"
      })
  public static class E112 implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "TYPE_EXPERIMENTATION", required = true, type = String.class)
    @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer typeexperimentation;

    @XmlElement(name = "ACTIVITE", required = true)
    protected String activite;

    @XmlElement(name = "ARTICLE_REGLEMENT", required = true)
    protected String articlereglement;

    @XmlElement(name = "DATE_DEBUT_VALIDITE", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datedebutvalidite;

    @XmlElement(name = "DATE_FIN_VALIDITE", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datefinvalidite;

    @XmlElement(name = "DATE_ETABLISSEMENT", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String dateetablissement;

    /**
     * Obtient la valeur de la propriété typeexperimentation.
     *
     * @return possible object is {@link String }
     */
    public Integer getTYPEEXPERIMENTATION() {
      return typeexperimentation;
    }

    /**
     * Définit la valeur de la propriété typeexperimentation.
     *
     * @param value allowed object is {@link String }
     */
    public void setTYPEEXPERIMENTATION(Integer value) {
      this.typeexperimentation = value;
    }

    /**
     * Obtient la valeur de la propriété activite.
     *
     * @return possible object is {@link String }
     */
    public String getACTIVITE() {
      return activite;
    }

    /**
     * Définit la valeur de la propriété activite.
     *
     * @param value allowed object is {@link String }
     */
    public void setACTIVITE(String value) {
      this.activite = value;
    }

    /**
     * Obtient la valeur de la propriété articlereglement.
     *
     * @return possible object is {@link String }
     */
    public String getARTICLEREGLEMENT() {
      return articlereglement;
    }

    /**
     * Définit la valeur de la propriété articlereglement.
     *
     * @param value allowed object is {@link String }
     */
    public void setARTICLEREGLEMENT(String value) {
      this.articlereglement = value;
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
     * Obtient la valeur de la propriété dateetablissement.
     *
     * @return possible object is {@link String }
     */
    public String getDATEETABLISSEMENT() {
      return dateetablissement;
    }

    /**
     * Définit la valeur de la propriété dateetablissement.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEETABLISSEMENT(String value) {
      this.dateetablissement = value;
    }
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="CODE_ETM" type="{http://www.almerys.com/NormeV3}codeEtm"/&amp;gt;
   * &amp;lt;element name="DATE_DEBUT_ETM" type="{http://www.w3.org/2001/XMLSchema}date"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_FIN_ETM"
   * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="DATE_PROLONGATION" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"codeetm", "datedebutetm", "datefinetm", "dateprolongation"})
  public static class ETM implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "CODE_ETM", required = true)
    protected String codeetm;

    @XmlElement(name = "DATE_DEBUT_ETM")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datedebutetm;

    @XmlElement(name = "DATE_FIN_ETM")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datefinetm;

    @XmlElement(name = "DATE_PROLONGATION")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String dateprolongation;

    /**
     * Obtient la valeur de la propriété codeetm.
     *
     * @return possible object is {@link String }
     */
    public String getCODEETM() {
      return codeetm;
    }

    /**
     * Définit la valeur de la propriété codeetm.
     *
     * @param value allowed object is {@link String }
     */
    public void setCODEETM(String value) {
      this.codeetm = value;
    }

    /**
     * Obtient la valeur de la propriété datedebutetm.
     *
     * @return possible object is {@link String }
     */
    public String getDATEDEBUTETM() {
      return datedebutetm;
    }

    /**
     * Définit la valeur de la propriété datedebutetm.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEDEBUTETM(String value) {
      this.datedebutetm = value;
    }

    /**
     * Obtient la valeur de la propriété datefinetm.
     *
     * @return possible object is {@link String }
     */
    public String getDATEFINETM() {
      return datefinetm;
    }

    /**
     * Définit la valeur de la propriété datefinetm.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEFINETM(String value) {
      this.datefinetm = value;
    }

    /**
     * Obtient la valeur de la propriété dateprolongation.
     *
     * @return possible object is {@link String }
     */
    public String getDATEPROLONGATION() {
      return dateprolongation;
    }

    /**
     * Définit la valeur de la propriété dateprolongation.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEPROLONGATION(String value) {
      this.dateprolongation = value;
    }
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="CODE_SERVICE_AMO"
   * type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element name="DATE_DEBUT_AMO"
   * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="DATE_FIN_AMO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"codeserviceamo", "datedebutamo", "datefinamo"})
  public static class SERVICEAMO implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "CODE_SERVICE_AMO", required = true)
    protected String codeserviceamo;

    @XmlElement(name = "DATE_DEBUT_AMO")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datedebutamo;

    @XmlElement(name = "DATE_FIN_AMO")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datefinamo;

    /**
     * Obtient la valeur de la propriété codeserviceamo.
     *
     * @return possible object is {@link String }
     */
    public String getCODESERVICEAMO() {
      return codeserviceamo;
    }

    /**
     * Définit la valeur de la propriété codeserviceamo.
     *
     * @param value allowed object is {@link String }
     */
    public void setCODESERVICEAMO(String value) {
      this.codeserviceamo = value;
    }

    /**
     * Obtient la valeur de la propriété datedebutamo.
     *
     * @return possible object is {@link String }
     */
    public String getDATEDEBUTAMO() {
      return datedebutamo;
    }

    /**
     * Définit la valeur de la propriété datedebutamo.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEDEBUTAMO(String value) {
      this.datedebutamo = value;
    }

    /**
     * Obtient la valeur de la propriété datefinamo.
     *
     * @return possible object is {@link String }
     */
    public String getDATEFINAMO() {
      return datefinamo;
    }

    /**
     * Définit la valeur de la propriété datefinamo.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEFINAMO(String value) {
      this.datefinamo = value;
    }
  }
}
