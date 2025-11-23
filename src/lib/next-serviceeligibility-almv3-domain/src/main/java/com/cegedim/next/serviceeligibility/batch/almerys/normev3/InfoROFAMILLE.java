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
 * &lt;p&gt;Classe Java pour infoRO_FAMILLE complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoRO_FAMILLE"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NIR_OD"
 * type="{http://www.almerys.com/NormeV3}string-1-13" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_CERTIFICATION_NIR_OD" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DROITS_FAMILLE"
 * type="{http://www.almerys.com/NormeV3}infoDroitFamille" maxOccurs="3"/&amp;gt; &amp;lt;element
 * name="SERVICE_AMO_FAMILLE" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="CODE_SERVICE_AMO" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_AMO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_AMO" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;element name="CODE_GRAND_REGIME" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt;
 * &amp;lt;element name="CODE_GESTION" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt;
 * &amp;lt;element name="TYPE_REGIME" type="{http://www.almerys.com/NormeV3}string-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_CAISSE_RO"
 * type="{http://www.almerys.com/NormeV3}string-1-3"/&amp;gt; &amp;lt;element name="CODE_GUICHET"
 * type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_ORGANISME" type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CENTRE_SS" type="{http://www.almerys.com/NormeV3}string-1-4"/&amp;gt;
 * &amp;lt;element name="ETM_FAMILLE" type="{http://www.almerys.com/NormeV3}infoETMFamille"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="E112_FAMILLE" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TYPE_EXPERIMENTATION" type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt;
 * &amp;lt;element name="ACTIVITE" type="{http://www.almerys.com/NormeV3}string-1"/&amp;gt;
 * &amp;lt;element name="ARTICLE_REGLEMENT" type="{http://www.almerys.com/NormeV3}string-3"/&amp;gt;
 * &amp;lt;element name="DATE_DEBUT_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
 * &amp;lt;element name="DATE_ETABLISSEMENT" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element name="ENCART_FAMILLE"
 * type="{http://www.almerys.com/NormeV3}infoAttestation" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoRO_FAMILLE",
    propOrder = {
      "nirod",
      "datecertificationnirod",
      "droitsfamilles",
      "serviceamofamille",
      "codegrandregime",
      "codegestion",
      "typeregime",
      "codecaissero",
      "codeguichet",
      "codeorganisme",
      "centress",
      "etmfamille",
      "e112FAMILLE",
      "encartfamille"
    })
public class InfoROFAMILLE implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NIR_OD")
  protected String nirod;

  @XmlElement(name = "DATE_CERTIFICATION_NIR_OD")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datecertificationnirod;

  @XmlElement(name = "DROITS_FAMILLE", required = true)
  protected List<InfoDroitFamille> droitsfamilles;

  @XmlElement(name = "SERVICE_AMO_FAMILLE")
  protected InfoROFAMILLE.SERVICEAMOFAMILLE serviceamofamille;

  @XmlElement(name = "CODE_GRAND_REGIME", required = true)
  protected String codegrandregime;

  @XmlElement(name = "CODE_GESTION", required = true)
  protected String codegestion;

  @XmlElement(name = "TYPE_REGIME")
  protected String typeregime;

  @XmlElement(name = "CODE_CAISSE_RO", required = true)
  protected String codecaissero;

  @XmlElement(name = "CODE_GUICHET")
  protected String codeguichet;

  @XmlElement(name = "CODE_ORGANISME")
  protected String codeorganisme;

  @XmlElement(name = "CENTRE_SS", required = true)
  protected String centress;

  @XmlElement(name = "ETM_FAMILLE")
  protected InfoETMFamille etmfamille;

  @XmlElement(name = "E112_FAMILLE")
  protected InfoROFAMILLE.E112FAMILLE e112FAMILLE;

  @XmlElement(name = "ENCART_FAMILLE")
  protected InfoAttestation encartfamille;

  /**
   * Obtient la valeur de la propriété nirod.
   *
   * @return possible object is {@link String }
   */
  public String getNIROD() {
    return nirod;
  }

  /**
   * Définit la valeur de la propriété nirod.
   *
   * @param value allowed object is {@link String }
   */
  public void setNIROD(String value) {
    this.nirod = value;
  }

  /**
   * Obtient la valeur de la propriété datecertificationnirod.
   *
   * @return possible object is {@link String }
   */
  public String getDATECERTIFICATIONNIROD() {
    return datecertificationnirod;
  }

  /**
   * Définit la valeur de la propriété datecertificationnirod.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATECERTIFICATIONNIROD(String value) {
    this.datecertificationnirod = value;
  }

  /**
   * Gets the value of the droitsfamilles property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the droitsfamilles
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getDROITSFAMILLES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoDroitFamille }
   */
  public List<InfoDroitFamille> getDROITSFAMILLES() {
    if (droitsfamilles == null) {
      droitsfamilles = new ArrayList<InfoDroitFamille>();
    }
    return this.droitsfamilles;
  }

  /**
   * Obtient la valeur de la propriété serviceamofamille.
   *
   * @return possible object is {@link InfoROFAMILLE.SERVICEAMOFAMILLE }
   */
  public InfoROFAMILLE.SERVICEAMOFAMILLE getSERVICEAMOFAMILLE() {
    return serviceamofamille;
  }

  /**
   * Définit la valeur de la propriété serviceamofamille.
   *
   * @param value allowed object is {@link InfoROFAMILLE.SERVICEAMOFAMILLE }
   */
  public void setSERVICEAMOFAMILLE(InfoROFAMILLE.SERVICEAMOFAMILLE value) {
    this.serviceamofamille = value;
  }

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
   * Obtient la valeur de la propriété etmfamille.
   *
   * @return possible object is {@link InfoETMFamille }
   */
  public InfoETMFamille getETMFAMILLE() {
    return etmfamille;
  }

  /**
   * Définit la valeur de la propriété etmfamille.
   *
   * @param value allowed object is {@link InfoETMFamille }
   */
  public void setETMFAMILLE(InfoETMFamille value) {
    this.etmfamille = value;
  }

  /**
   * Obtient la valeur de la propriété e112FAMILLE.
   *
   * @return possible object is {@link InfoROFAMILLE.E112FAMILLE }
   */
  public InfoROFAMILLE.E112FAMILLE getE112FAMILLE() {
    return e112FAMILLE;
  }

  /**
   * Définit la valeur de la propriété e112FAMILLE.
   *
   * @param value allowed object is {@link InfoROFAMILLE.E112FAMILLE }
   */
  public void setE112FAMILLE(InfoROFAMILLE.E112FAMILLE value) {
    this.e112FAMILLE = value;
  }

  /**
   * Obtient la valeur de la propriété encartfamille.
   *
   * @return possible object is {@link InfoAttestation }
   */
  public InfoAttestation getENCARTFAMILLE() {
    return encartfamille;
  }

  /**
   * Définit la valeur de la propriété encartfamille.
   *
   * @param value allowed object is {@link InfoAttestation }
   */
  public void setENCARTFAMILLE(InfoAttestation value) {
    this.encartfamille = value;
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
  public static class E112FAMILLE implements Serializable {

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
  public static class SERVICEAMOFAMILLE implements Serializable {

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
