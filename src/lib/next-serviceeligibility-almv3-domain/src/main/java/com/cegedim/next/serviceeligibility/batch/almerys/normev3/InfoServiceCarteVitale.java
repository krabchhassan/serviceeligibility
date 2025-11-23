//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateTimeAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.PositiveIntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoServiceCarteVitale complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoServiceCarteVitale"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="NATURE_DEMANDE" type="{http://www.almerys.com/NormeV3}codeDemande"/&amp;gt; &amp;lt;element
 * name="CODE_EVENEMENT_CARTE" type="{http://www.almerys.com/NormeV3}string-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_SERVICE"
 * type="{http://www.almerys.com/NormeV3}positiveInteger-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="RO_FAMILLE" type="{http://www.almerys.com/NormeV3}infoRO_FAMILLE" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="BENEF_RC" maxOccurs="unbounded" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="BENEFICIAIRE" type="{http://www.almerys.com/NormeV3}infoBenefVtl"/&amp;gt; &amp;lt;element
 * name="RC" maxOccurs="unbounded" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TEXTE_MUTUELLE" type="{http://www.almerys.com/NormeV3}infoTexteMutuelle"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NATURE_RC"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;extension
 * base="{http://www.almerys.com/NormeV3}infoRC"&amp;gt; &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;element name="HABILITATION" type="{http://www.w3.org/2001/XMLSchema}integer"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="INDICATEUR_TRAITEMENT"
 * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="LIBL_DIALOGUE" type="{http://www.almerys.com/NormeV3}string-1-16"/&amp;gt; &amp;lt;element
 * name="LIBL_DIALOGUE_LONG" type="{http://www.almerys.com/NormeV3}string-1-32"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_ASSOCIE"
 * type="{http://www.almerys.com/NormeV3}string-1-17" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="TYPE_SERVICE" type="{http://www.almerys.com/NormeV3}string-1" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ZONE_COMMUNE" type="{http://www.almerys.com/NormeV3}string-1-115"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_INSCRIPTION_CARTE"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_AIGUILLAGE_STS" type="{http://www.almerys.com/NormeV3}string-1" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;element name="CARTE"
 * type="{http://www.almerys.com/NormeV3}infoCarteVitale"/&amp;gt; &amp;lt;element
 * name="CARTE_A_REMPLACER" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="NUM_SERIE" type="{http://www.almerys.com/NormeV3}string-1-16"/&amp;gt; &amp;lt;element
 * name="DERNIERE_OPERATION" type="{http://www.w3.org/2001/XMLSchema}dateTime"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;element name="MUTATION" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="CODE_GRAND_REGIME" type="{http://www.almerys.com/NormeV3}string-1-6"/&amp;gt;
 * &amp;lt;element name="CODE_CAISSE_RO" type="{http://www.almerys.com/NormeV3}string-1-3"/&amp;gt;
 * &amp;lt;element name="CENTRE_SS" type="{http://www.almerys.com/NormeV3}string-1-4"/&amp;gt;
 * &amp;lt;element name="DATE_PERSONNALISATION"
 * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;element name="DATE_DEBUT_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element name="DATE_FIN_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoServiceCarteVitale",
    propOrder = {
      "naturedemande",
      "codeevenementcarte",
      "codeservice",
      "rofamille",
      "benefrcs",
      "carte",
      "cartearemplacer",
      "mutation",
      "datedebutvalidite",
      "datefinvalidite"
    })
public class InfoServiceCarteVitale implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NATURE_DEMANDE", required = true)
  @XmlSchemaType(name = "string")
  protected CodeDemande naturedemande;

  @XmlElement(name = "CODE_EVENEMENT_CARTE")
  protected String codeevenementcarte;

  @XmlElement(name = "CODE_SERVICE", type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer codeservice;

  @XmlElement(name = "RO_FAMILLE")
  protected InfoROFAMILLE rofamille;

  @XmlElement(name = "BENEF_RC")
  protected List<InfoServiceCarteVitale.BENEFRC> benefrcs;

  @XmlElement(name = "CARTE", required = true)
  protected InfoCarteVitale carte;

  @XmlElement(name = "CARTE_A_REMPLACER")
  protected InfoServiceCarteVitale.CARTEAREMPLACER cartearemplacer;

  @XmlElement(name = "MUTATION")
  protected InfoServiceCarteVitale.MUTATION mutation;

  @XmlElement(name = "DATE_DEBUT_VALIDITE", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutvalidite;

  @XmlElement(name = "DATE_FIN_VALIDITE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinvalidite;

  /**
   * Obtient la valeur de la propriété naturedemande.
   *
   * @return possible object is {@link CodeDemande }
   */
  public CodeDemande getNATUREDEMANDE() {
    return naturedemande;
  }

  /**
   * Définit la valeur de la propriété naturedemande.
   *
   * @param value allowed object is {@link CodeDemande }
   */
  public void setNATUREDEMANDE(CodeDemande value) {
    this.naturedemande = value;
  }

  /**
   * Obtient la valeur de la propriété codeevenementcarte.
   *
   * @return possible object is {@link String }
   */
  public String getCODEEVENEMENTCARTE() {
    return codeevenementcarte;
  }

  /**
   * Définit la valeur de la propriété codeevenementcarte.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEEVENEMENTCARTE(String value) {
    this.codeevenementcarte = value;
  }

  /**
   * Obtient la valeur de la propriété codeservice.
   *
   * @return possible object is {@link String }
   */
  public Integer getCODESERVICE() {
    return codeservice;
  }

  /**
   * Définit la valeur de la propriété codeservice.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODESERVICE(Integer value) {
    this.codeservice = value;
  }

  /**
   * Obtient la valeur de la propriété rofamille.
   *
   * @return possible object is {@link InfoROFAMILLE }
   */
  public InfoROFAMILLE getROFAMILLE() {
    return rofamille;
  }

  /**
   * Définit la valeur de la propriété rofamille.
   *
   * @param value allowed object is {@link InfoROFAMILLE }
   */
  public void setROFAMILLE(InfoROFAMILLE value) {
    this.rofamille = value;
  }

  /**
   * Gets the value of the benefrcs property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the benefrcs
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getBENEFRCS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
   * InfoServiceCarteVitale.BENEFRC }
   */
  public List<InfoServiceCarteVitale.BENEFRC> getBENEFRCS() {
    if (benefrcs == null) {
      benefrcs = new ArrayList<InfoServiceCarteVitale.BENEFRC>();
    }
    return this.benefrcs;
  }

  /**
   * Obtient la valeur de la propriété carte.
   *
   * @return possible object is {@link InfoCarteVitale }
   */
  public InfoCarteVitale getCARTE() {
    return carte;
  }

  /**
   * Définit la valeur de la propriété carte.
   *
   * @param value allowed object is {@link InfoCarteVitale }
   */
  public void setCARTE(InfoCarteVitale value) {
    this.carte = value;
  }

  /**
   * Obtient la valeur de la propriété cartearemplacer.
   *
   * @return possible object is {@link InfoServiceCarteVitale.CARTEAREMPLACER }
   */
  public InfoServiceCarteVitale.CARTEAREMPLACER getCARTEAREMPLACER() {
    return cartearemplacer;
  }

  /**
   * Définit la valeur de la propriété cartearemplacer.
   *
   * @param value allowed object is {@link InfoServiceCarteVitale.CARTEAREMPLACER }
   */
  public void setCARTEAREMPLACER(InfoServiceCarteVitale.CARTEAREMPLACER value) {
    this.cartearemplacer = value;
  }

  /**
   * Obtient la valeur de la propriété mutation.
   *
   * @return possible object is {@link InfoServiceCarteVitale.MUTATION }
   */
  public InfoServiceCarteVitale.MUTATION getMUTATION() {
    return mutation;
  }

  /**
   * Définit la valeur de la propriété mutation.
   *
   * @param value allowed object is {@link InfoServiceCarteVitale.MUTATION }
   */
  public void setMUTATION(InfoServiceCarteVitale.MUTATION value) {
    this.mutation = value;
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
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="BENEFICIAIRE"
   * type="{http://www.almerys.com/NormeV3}infoBenefVtl"/&amp;gt; &amp;lt;element name="RC"
   * maxOccurs="unbounded" minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt;
   * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="TEXTE_MUTUELLE" type="{http://www.almerys.com/NormeV3}infoTexteMutuelle"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="NATURE_RC"&amp;gt; &amp;lt;complexType&amp;gt;
   * &amp;lt;complexContent&amp;gt; &amp;lt;extension
   * base="{http://www.almerys.com/NormeV3}infoRC"&amp;gt; &amp;lt;/extension&amp;gt;
   * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
   * &amp;lt;element name="HABILITATION" type="{http://www.w3.org/2001/XMLSchema}integer"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="INDICATEUR_TRAITEMENT"
   * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="LIBL_DIALOGUE" type="{http://www.almerys.com/NormeV3}string-1-16"/&amp;gt;
   * &amp;lt;element name="LIBL_DIALOGUE_LONG" type="{http://www.almerys.com/NormeV3}string-1-32"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_ASSOCIE"
   * type="{http://www.almerys.com/NormeV3}string-1-17" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="TYPE_SERVICE" type="{http://www.almerys.com/NormeV3}string-1" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="ZONE_COMMUNE" type="{http://www.almerys.com/NormeV3}string-1-115"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_INSCRIPTION_CARTE"
   * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="CODE_AIGUILLAGE_STS" type="{http://www.almerys.com/NormeV3}string-1"
   * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
   * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"beneficiaire", "rcs"})
  public static class BENEFRC implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "BENEFICIAIRE", required = true)
    protected InfoBenefVtl beneficiaire;

    @XmlElement(name = "RC")
    protected List<InfoServiceCarteVitale.BENEFRC.RC> rcs;

    /**
     * Obtient la valeur de la propriété beneficiaire.
     *
     * @return possible object is {@link InfoBenefVtl }
     */
    public InfoBenefVtl getBENEFICIAIRE() {
      return beneficiaire;
    }

    /**
     * Définit la valeur de la propriété beneficiaire.
     *
     * @param value allowed object is {@link InfoBenefVtl }
     */
    public void setBENEFICIAIRE(InfoBenefVtl value) {
      this.beneficiaire = value;
    }

    /**
     * Gets the value of the rcs property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the rcs property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getRCS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
     * InfoServiceCarteVitale.BENEFRC.RC }
     */
    public List<InfoServiceCarteVitale.BENEFRC.RC> getRCS() {
      if (rcs == null) {
        rcs = new ArrayList<InfoServiceCarteVitale.BENEFRC.RC>();
      }
      return this.rcs;
    }

    /**
     * &lt;p&gt;Classe Java pour anonymous complex type.
     *
     * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
     * classe.
     *
     * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
     * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
     * &amp;lt;element name="TEXTE_MUTUELLE"
     * type="{http://www.almerys.com/NormeV3}infoTexteMutuelle" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="NATURE_RC"&amp;gt; &amp;lt;complexType&amp;gt;
     * &amp;lt;complexContent&amp;gt; &amp;lt;extension
     * base="{http://www.almerys.com/NormeV3}infoRC"&amp;gt; &amp;lt;/extension&amp;gt;
     * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
     * &amp;lt;element name="HABILITATION" type="{http://www.w3.org/2001/XMLSchema}integer"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="INDICATEUR_TRAITEMENT"
     * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="LIBL_DIALOGUE" type="{http://www.almerys.com/NormeV3}string-1-16"/&amp;gt;
     * &amp;lt;element name="LIBL_DIALOGUE_LONG" type="{http://www.almerys.com/NormeV3}string-1-32"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_ASSOCIE"
     * type="{http://www.almerys.com/NormeV3}string-1-17" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="TYPE_SERVICE" type="{http://www.almerys.com/NormeV3}string-1" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="ZONE_COMMUNE" type="{http://www.almerys.com/NormeV3}string-1-115"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_INSCRIPTION_CARTE"
     * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="CODE_AIGUILLAGE_STS" type="{http://www.almerys.com/NormeV3}string-1"
     * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
     * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
        name = "",
        propOrder = {
          "textemutuelle",
          "naturerc",
          "habilitation",
          "indicateurtraitement",
          "libldialogue",
          "libldialoguelong",
          "serviceassocie",
          "typeservice",
          "zonecommune",
          "dateinscriptioncarte",
          "codeaiguillagests"
        })
    public static class RC implements Serializable {

      private static final long serialVersionUID = -1L;

      @XmlElement(name = "TEXTE_MUTUELLE")
      protected InfoTexteMutuelle textemutuelle;

      @XmlElement(name = "NATURE_RC", required = true)
      protected InfoServiceCarteVitale.BENEFRC.RC.NATURERC naturerc;

      @XmlElement(name = "HABILITATION")
      protected BigInteger habilitation;

      @XmlElement(name = "INDICATEUR_TRAITEMENT")
      protected String indicateurtraitement;

      @XmlElement(name = "LIBL_DIALOGUE", required = true)
      protected String libldialogue;

      @XmlElement(name = "LIBL_DIALOGUE_LONG")
      protected String libldialoguelong;

      @XmlElement(name = "SERVICE_ASSOCIE")
      protected String serviceassocie;

      @XmlElement(name = "TYPE_SERVICE")
      protected String typeservice;

      @XmlElement(name = "ZONE_COMMUNE")
      protected String zonecommune;

      @XmlElement(name = "DATE_INSCRIPTION_CARTE")
      @XmlJavaTypeAdapter(DateAdapter.class)
      @XmlSchemaType(name = "date")
      protected String dateinscriptioncarte;

      @XmlElement(name = "CODE_AIGUILLAGE_STS")
      protected String codeaiguillagests;

      /**
       * Obtient la valeur de la propriété textemutuelle.
       *
       * @return possible object is {@link InfoTexteMutuelle }
       */
      public InfoTexteMutuelle getTEXTEMUTUELLE() {
        return textemutuelle;
      }

      /**
       * Définit la valeur de la propriété textemutuelle.
       *
       * @param value allowed object is {@link InfoTexteMutuelle }
       */
      public void setTEXTEMUTUELLE(InfoTexteMutuelle value) {
        this.textemutuelle = value;
      }

      /**
       * Obtient la valeur de la propriété naturerc.
       *
       * @return possible object is {@link InfoServiceCarteVitale.BENEFRC.RC.NATURERC }
       */
      public InfoServiceCarteVitale.BENEFRC.RC.NATURERC getNATURERC() {
        return naturerc;
      }

      /**
       * Définit la valeur de la propriété naturerc.
       *
       * @param value allowed object is {@link InfoServiceCarteVitale.BENEFRC.RC.NATURERC }
       */
      public void setNATURERC(InfoServiceCarteVitale.BENEFRC.RC.NATURERC value) {
        this.naturerc = value;
      }

      /**
       * Obtient la valeur de la propriété habilitation.
       *
       * @return possible object is {@link BigInteger }
       */
      public BigInteger getHABILITATION() {
        return habilitation;
      }

      /**
       * Définit la valeur de la propriété habilitation.
       *
       * @param value allowed object is {@link BigInteger }
       */
      public void setHABILITATION(BigInteger value) {
        this.habilitation = value;
      }

      /**
       * Obtient la valeur de la propriété indicateurtraitement.
       *
       * @return possible object is {@link String }
       */
      public String getINDICATEURTRAITEMENT() {
        return indicateurtraitement;
      }

      /**
       * Définit la valeur de la propriété indicateurtraitement.
       *
       * @param value allowed object is {@link String }
       */
      public void setINDICATEURTRAITEMENT(String value) {
        this.indicateurtraitement = value;
      }

      /**
       * Obtient la valeur de la propriété libldialogue.
       *
       * @return possible object is {@link String }
       */
      public String getLIBLDIALOGUE() {
        return libldialogue;
      }

      /**
       * Définit la valeur de la propriété libldialogue.
       *
       * @param value allowed object is {@link String }
       */
      public void setLIBLDIALOGUE(String value) {
        this.libldialogue = value;
      }

      /**
       * Obtient la valeur de la propriété libldialoguelong.
       *
       * @return possible object is {@link String }
       */
      public String getLIBLDIALOGUELONG() {
        return libldialoguelong;
      }

      /**
       * Définit la valeur de la propriété libldialoguelong.
       *
       * @param value allowed object is {@link String }
       */
      public void setLIBLDIALOGUELONG(String value) {
        this.libldialoguelong = value;
      }

      /**
       * Obtient la valeur de la propriété serviceassocie.
       *
       * @return possible object is {@link String }
       */
      public String getSERVICEASSOCIE() {
        return serviceassocie;
      }

      /**
       * Définit la valeur de la propriété serviceassocie.
       *
       * @param value allowed object is {@link String }
       */
      public void setSERVICEASSOCIE(String value) {
        this.serviceassocie = value;
      }

      /**
       * Obtient la valeur de la propriété typeservice.
       *
       * @return possible object is {@link String }
       */
      public String getTYPESERVICE() {
        return typeservice;
      }

      /**
       * Définit la valeur de la propriété typeservice.
       *
       * @param value allowed object is {@link String }
       */
      public void setTYPESERVICE(String value) {
        this.typeservice = value;
      }

      /**
       * Obtient la valeur de la propriété zonecommune.
       *
       * @return possible object is {@link String }
       */
      public String getZONECOMMUNE() {
        return zonecommune;
      }

      /**
       * Définit la valeur de la propriété zonecommune.
       *
       * @param value allowed object is {@link String }
       */
      public void setZONECOMMUNE(String value) {
        this.zonecommune = value;
      }

      /**
       * Obtient la valeur de la propriété dateinscriptioncarte.
       *
       * @return possible object is {@link String }
       */
      public String getDATEINSCRIPTIONCARTE() {
        return dateinscriptioncarte;
      }

      /**
       * Définit la valeur de la propriété dateinscriptioncarte.
       *
       * @param value allowed object is {@link String }
       */
      public void setDATEINSCRIPTIONCARTE(String value) {
        this.dateinscriptioncarte = value;
      }

      /**
       * Obtient la valeur de la propriété codeaiguillagests.
       *
       * @return possible object is {@link String }
       */
      public String getCODEAIGUILLAGESTS() {
        return codeaiguillagests;
      }

      /**
       * Définit la valeur de la propriété codeaiguillagests.
       *
       * @param value allowed object is {@link String }
       */
      public void setCODEAIGUILLAGESTS(String value) {
        this.codeaiguillagests = value;
      }

      /**
       * &lt;p&gt;Classe Java pour anonymous complex type.
       *
       * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
       * classe.
       *
       * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;extension
       * base="{http://www.almerys.com/NormeV3}infoRC"&amp;gt; &amp;lt;/extension&amp;gt;
       * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
       */
      @XmlAccessorType(XmlAccessType.FIELD)
      @XmlType(name = "")
      public static class NATURERC extends InfoRC implements Serializable {

        private static final long serialVersionUID = -1L;
      }
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
   * &amp;lt;element name="NUM_SERIE" type="{http://www.almerys.com/NormeV3}string-1-16"/&amp;gt;
   * &amp;lt;element name="DERNIERE_OPERATION" type="{http://www.w3.org/2001/XMLSchema}dateTime"
   * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
   * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"numserie", "derniereoperation"})
  public static class CARTEAREMPLACER implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "NUM_SERIE", required = true)
    protected String numserie;

    @XmlElement(name = "DERNIERE_OPERATION", type = String.class)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    protected Date derniereoperation;

    /**
     * Obtient la valeur de la propriété numserie.
     *
     * @return possible object is {@link String }
     */
    public String getNUMSERIE() {
      return numserie;
    }

    /**
     * Définit la valeur de la propriété numserie.
     *
     * @param value allowed object is {@link String }
     */
    public void setNUMSERIE(String value) {
      this.numserie = value;
    }

    /**
     * Obtient la valeur de la propriété derniereoperation.
     *
     * @return possible object is {@link String }
     */
    public Date getDERNIEREOPERATION() {
      return derniereoperation;
    }

    /**
     * Définit la valeur de la propriété derniereoperation.
     *
     * @param value allowed object is {@link String }
     */
    public void setDERNIEREOPERATION(Date value) {
      this.derniereoperation = value;
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
   * &amp;lt;element name="CODE_GRAND_REGIME"
   * type="{http://www.almerys.com/NormeV3}string-1-6"/&amp;gt; &amp;lt;element
   * name="CODE_CAISSE_RO" type="{http://www.almerys.com/NormeV3}string-1-3"/&amp;gt;
   * &amp;lt;element name="CENTRE_SS" type="{http://www.almerys.com/NormeV3}string-1-4"/&amp;gt;
   * &amp;lt;element name="DATE_PERSONNALISATION"
   * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;/sequence&amp;gt;
   * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
   * &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"codegrandregime", "codecaissero", "centress", "datepersonnalisation"})
  public static class MUTATION implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "CODE_GRAND_REGIME", required = true)
    protected String codegrandregime;

    @XmlElement(name = "CODE_CAISSE_RO", required = true)
    protected String codecaissero;

    @XmlElement(name = "CENTRE_SS", required = true)
    protected String centress;

    @XmlElement(name = "DATE_PERSONNALISATION", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datepersonnalisation;

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
     * Obtient la valeur de la propriété datepersonnalisation.
     *
     * @return possible object is {@link String }
     */
    public String getDATEPERSONNALISATION() {
      return datepersonnalisation;
    }

    /**
     * Définit la valeur de la propriété datepersonnalisation.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEPERSONNALISATION(String value) {
      this.datepersonnalisation = value;
    }
  }
}
