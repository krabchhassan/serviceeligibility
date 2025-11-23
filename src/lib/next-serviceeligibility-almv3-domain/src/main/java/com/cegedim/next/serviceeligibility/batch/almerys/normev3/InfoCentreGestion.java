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
 * &lt;p&gt;Classe Java pour infoCentreGestion complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoCentreGestion"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="REF_INTERNE_CG" type="{http://www.almerys.com/NormeV3}string-1-15"/&amp;gt; &amp;lt;element
 * name="ADRESSE_CG" type="{http://www.almerys.com/NormeV3}infoAdresse"/&amp;gt; &amp;lt;element
 * name="INFO_CARTE" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="LIGNE1" type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="LIGNE2" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="LIGNE3"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="LIGNE4" type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="LIGNE5" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="LIGNE6"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element name="TYPE_GESTIONNAIRE"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="GESTIONNAIRE_CONTRAT" type="{http://www.almerys.com/NormeV3}string-1-30"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="JOIGNABILITE"
 * type="{http://www.almerys.com/NormeV3}infoJoignabilite" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="HORAIRE"
 * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="TARIF" type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="OUVERTURE" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="OUV1" type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="OUV2" type="{http://www.almerys.com/NormeV3}string-1-80"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="OUV3"
 * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="OUV4" type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="OUV5" type="{http://www.almerys.com/NormeV3}string-1-80"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoCentreGestion",
    propOrder = {
      "refinternecg",
      "adressecg",
      "infocarte",
      "typegestionnaire",
      "gestionnairecontrat",
      "joignabilites",
      "horaire",
      "tarif",
      "ouverture"
    })
public class InfoCentreGestion implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_INTERNE_CG", required = true)
  protected String refinternecg;

  @XmlElement(name = "ADRESSE_CG", required = true)
  protected InfoAdresse adressecg;

  @XmlElement(name = "INFO_CARTE")
  protected InfoCentreGestion.INFOCARTE infocarte;

  @XmlElement(name = "TYPE_GESTIONNAIRE")
  protected String typegestionnaire;

  @XmlElement(name = "GESTIONNAIRE_CONTRAT")
  protected String gestionnairecontrat;

  @XmlElement(name = "JOIGNABILITE")
  protected List<InfoJoignabilite> joignabilites;

  @XmlElement(name = "HORAIRE")
  protected String horaire;

  @XmlElement(name = "TARIF")
  protected String tarif;

  @XmlElement(name = "OUVERTURE")
  protected InfoCentreGestion.OUVERTURE ouverture;

  /**
   * Obtient la valeur de la propriété refinternecg.
   *
   * @return possible object is {@link String }
   */
  public String getREFINTERNECG() {
    return refinternecg;
  }

  /**
   * Définit la valeur de la propriété refinternecg.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFINTERNECG(String value) {
    this.refinternecg = value;
  }

  /**
   * Obtient la valeur de la propriété adressecg.
   *
   * @return possible object is {@link InfoAdresse }
   */
  public InfoAdresse getADRESSECG() {
    return adressecg;
  }

  /**
   * Définit la valeur de la propriété adressecg.
   *
   * @param value allowed object is {@link InfoAdresse }
   */
  public void setADRESSECG(InfoAdresse value) {
    this.adressecg = value;
  }

  /**
   * Obtient la valeur de la propriété infocarte.
   *
   * @return possible object is {@link InfoCentreGestion.INFOCARTE }
   */
  public InfoCentreGestion.INFOCARTE getINFOCARTE() {
    return infocarte;
  }

  /**
   * Définit la valeur de la propriété infocarte.
   *
   * @param value allowed object is {@link InfoCentreGestion.INFOCARTE }
   */
  public void setINFOCARTE(InfoCentreGestion.INFOCARTE value) {
    this.infocarte = value;
  }

  /**
   * Obtient la valeur de la propriété typegestionnaire.
   *
   * @return possible object is {@link String }
   */
  public String getTYPEGESTIONNAIRE() {
    return typegestionnaire;
  }

  /**
   * Définit la valeur de la propriété typegestionnaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setTYPEGESTIONNAIRE(String value) {
    this.typegestionnaire = value;
  }

  /**
   * Obtient la valeur de la propriété gestionnairecontrat.
   *
   * @return possible object is {@link String }
   */
  public String getGESTIONNAIRECONTRAT() {
    return gestionnairecontrat;
  }

  /**
   * Définit la valeur de la propriété gestionnairecontrat.
   *
   * @param value allowed object is {@link String }
   */
  public void setGESTIONNAIRECONTRAT(String value) {
    this.gestionnairecontrat = value;
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
   * Obtient la valeur de la propriété horaire.
   *
   * @return possible object is {@link String }
   */
  public String getHORAIRE() {
    return horaire;
  }

  /**
   * Définit la valeur de la propriété horaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setHORAIRE(String value) {
    this.horaire = value;
  }

  /**
   * Obtient la valeur de la propriété tarif.
   *
   * @return possible object is {@link String }
   */
  public String getTARIF() {
    return tarif;
  }

  /**
   * Définit la valeur de la propriété tarif.
   *
   * @param value allowed object is {@link String }
   */
  public void setTARIF(String value) {
    this.tarif = value;
  }

  /**
   * Obtient la valeur de la propriété ouverture.
   *
   * @return possible object is {@link InfoCentreGestion.OUVERTURE }
   */
  public InfoCentreGestion.OUVERTURE getOUVERTURE() {
    return ouverture;
  }

  /**
   * Définit la valeur de la propriété ouverture.
   *
   * @param value allowed object is {@link InfoCentreGestion.OUVERTURE }
   */
  public void setOUVERTURE(InfoCentreGestion.OUVERTURE value) {
    this.ouverture = value;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="LIGNE1" type="{http://www.almerys.com/NormeV3}string-1-50"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="LIGNE2"
   * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="LIGNE3" type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="LIGNE4" type="{http://www.almerys.com/NormeV3}string-1-50"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="LIGNE5"
   * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="LIGNE6" type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"ligne1", "ligne2", "ligne3", "ligne4", "ligne5", "ligne6"})
  public static class INFOCARTE implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "LIGNE1")
    protected String ligne1;

    @XmlElement(name = "LIGNE2")
    protected String ligne2;

    @XmlElement(name = "LIGNE3")
    protected String ligne3;

    @XmlElement(name = "LIGNE4")
    protected String ligne4;

    @XmlElement(name = "LIGNE5")
    protected String ligne5;

    @XmlElement(name = "LIGNE6")
    protected String ligne6;

    /**
     * Obtient la valeur de la propriété ligne1.
     *
     * @return possible object is {@link String }
     */
    public String getLIGNE1() {
      return ligne1;
    }

    /**
     * Définit la valeur de la propriété ligne1.
     *
     * @param value allowed object is {@link String }
     */
    public void setLIGNE1(String value) {
      this.ligne1 = value;
    }

    /**
     * Obtient la valeur de la propriété ligne2.
     *
     * @return possible object is {@link String }
     */
    public String getLIGNE2() {
      return ligne2;
    }

    /**
     * Définit la valeur de la propriété ligne2.
     *
     * @param value allowed object is {@link String }
     */
    public void setLIGNE2(String value) {
      this.ligne2 = value;
    }

    /**
     * Obtient la valeur de la propriété ligne3.
     *
     * @return possible object is {@link String }
     */
    public String getLIGNE3() {
      return ligne3;
    }

    /**
     * Définit la valeur de la propriété ligne3.
     *
     * @param value allowed object is {@link String }
     */
    public void setLIGNE3(String value) {
      this.ligne3 = value;
    }

    /**
     * Obtient la valeur de la propriété ligne4.
     *
     * @return possible object is {@link String }
     */
    public String getLIGNE4() {
      return ligne4;
    }

    /**
     * Définit la valeur de la propriété ligne4.
     *
     * @param value allowed object is {@link String }
     */
    public void setLIGNE4(String value) {
      this.ligne4 = value;
    }

    /**
     * Obtient la valeur de la propriété ligne5.
     *
     * @return possible object is {@link String }
     */
    public String getLIGNE5() {
      return ligne5;
    }

    /**
     * Définit la valeur de la propriété ligne5.
     *
     * @param value allowed object is {@link String }
     */
    public void setLIGNE5(String value) {
      this.ligne5 = value;
    }

    /**
     * Obtient la valeur de la propriété ligne6.
     *
     * @return possible object is {@link String }
     */
    public String getLIGNE6() {
      return ligne6;
    }

    /**
     * Définit la valeur de la propriété ligne6.
     *
     * @param value allowed object is {@link String }
     */
    public void setLIGNE6(String value) {
      this.ligne6 = value;
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
   * &amp;lt;element name="OUV1" type="{http://www.almerys.com/NormeV3}string-1-80"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="OUV2"
   * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="OUV3" type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="OUV4" type="{http://www.almerys.com/NormeV3}string-1-80"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="OUV5"
   * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"ouv1", "ouv2", "ouv3", "ouv4", "ouv5"})
  public static class OUVERTURE implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "OUV1")
    protected String ouv1;

    @XmlElement(name = "OUV2")
    protected String ouv2;

    @XmlElement(name = "OUV3")
    protected String ouv3;

    @XmlElement(name = "OUV4")
    protected String ouv4;

    @XmlElement(name = "OUV5")
    protected String ouv5;

    /**
     * Obtient la valeur de la propriété ouv1.
     *
     * @return possible object is {@link String }
     */
    public String getOUV1() {
      return ouv1;
    }

    /**
     * Définit la valeur de la propriété ouv1.
     *
     * @param value allowed object is {@link String }
     */
    public void setOUV1(String value) {
      this.ouv1 = value;
    }

    /**
     * Obtient la valeur de la propriété ouv2.
     *
     * @return possible object is {@link String }
     */
    public String getOUV2() {
      return ouv2;
    }

    /**
     * Définit la valeur de la propriété ouv2.
     *
     * @param value allowed object is {@link String }
     */
    public void setOUV2(String value) {
      this.ouv2 = value;
    }

    /**
     * Obtient la valeur de la propriété ouv3.
     *
     * @return possible object is {@link String }
     */
    public String getOUV3() {
      return ouv3;
    }

    /**
     * Définit la valeur de la propriété ouv3.
     *
     * @param value allowed object is {@link String }
     */
    public void setOUV3(String value) {
      this.ouv3 = value;
    }

    /**
     * Obtient la valeur de la propriété ouv4.
     *
     * @return possible object is {@link String }
     */
    public String getOUV4() {
      return ouv4;
    }

    /**
     * Définit la valeur de la propriété ouv4.
     *
     * @param value allowed object is {@link String }
     */
    public void setOUV4(String value) {
      this.ouv4 = value;
    }

    /**
     * Obtient la valeur de la propriété ouv5.
     *
     * @return possible object is {@link String }
     */
    public String getOUV5() {
      return ouv5;
    }

    /**
     * Définit la valeur de la propriété ouv5.
     *
     * @param value allowed object is {@link String }
     */
    public void setOUV5(String value) {
      this.ouv5 = value;
    }
  }
}
