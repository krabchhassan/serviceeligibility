//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
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
 * &lt;p&gt;Classe Java pour infoCarteVitale complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoCarteVitale"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="PROFIL_PRODUCTION" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TYPE_CARTE" type="{http://www.almerys.com/NormeV3}codeCarteVitale"/&amp;gt; &amp;lt;element
 * name="TYPE_CARTE_A_PRODUIRE" type="{http://www.almerys.com/NormeV3}CodeProduction"/&amp;gt;
 * &amp;lt;element name="NATURE" type="{http://www.almerys.com/NormeV3}CodeNature"/&amp;gt;
 * &amp;lt;element name="CODE_PROFIL" type="{http://www.almerys.com/NormeV3}string-9"/&amp;gt;
 * &amp;lt;element name="LOCALISATION_POSTALE"
 * type="{http://www.almerys.com/NormeV3}nonNegativeInteger-1"/&amp;gt; &amp;lt;element
 * name="ORIGINE" type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt; &amp;lt;element
 * name="CARTE_ACTIVE" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;element
 * name="INDICATEUR_ENCART" type="{http://www.almerys.com/NormeV3}positiveInteger-1"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="PHOTO"
 * type="{http://www.almerys.com/NormeV3}string-30"/&amp;gt; &amp;lt;element
 * name="SERIGRAPHIE"&amp;gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NOM_PORTEUR"
 * type="{http://www.almerys.com/NormeV3}string-1-80"/&amp;gt; &amp;lt;element name="PRENOM_PORTEUR"
 * type="{http://www.almerys.com/NormeV3}string-1-40"/&amp;gt; &amp;lt;element name="NNI_PORTEUR"
 * type="{http://www.almerys.com/NormeV3}string-1-13"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;element name="NUM_LOT_OS" type="{http://www.almerys.com/NormeV3}string-1-16"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NUM_SERIE"
 * type="{http://www.almerys.com/NormeV3}string-1-16" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="REF_INTERNE_OS_PORTEUR" type="{http://www.almerys.com/NormeV3}string-1-30"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="REF_INTERNE_OS_PORTE"
 * type="{http://www.almerys.com/NormeV3}string-1-30" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ACTIVATION_DESACTIVATION"
 * type="{http://www.almerys.com/NormeV3}codeActivation"/&amp;gt; &amp;lt;element name="ENVOI"
 * type="{http://www.almerys.com/NormeV3}codeEnvoi"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoCarteVitale",
    propOrder = {
      "profilproduction",
      "numlotos",
      "numserie",
      "refinterneosporteur",
      "refinterneosportes",
      "activationdesactivation",
      "envoi"
    })
public class InfoCarteVitale implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "PROFIL_PRODUCTION")
  protected InfoCarteVitale.PROFILPRODUCTION profilproduction;

  @XmlElement(name = "NUM_LOT_OS")
  protected String numlotos;

  @XmlElement(name = "NUM_SERIE")
  protected String numserie;

  @XmlElement(name = "REF_INTERNE_OS_PORTEUR")
  protected String refinterneosporteur;

  @XmlElement(name = "REF_INTERNE_OS_PORTE")
  protected List<String> refinterneosportes;

  @XmlElement(name = "ACTIVATION_DESACTIVATION", required = true)
  @XmlSchemaType(name = "string")
  protected CodeActivation activationdesactivation;

  @XmlElement(name = "ENVOI", required = true)
  @XmlSchemaType(name = "string")
  protected CodeEnvoi envoi;

  /**
   * Obtient la valeur de la propriété profilproduction.
   *
   * @return possible object is {@link InfoCarteVitale.PROFILPRODUCTION }
   */
  public InfoCarteVitale.PROFILPRODUCTION getPROFILPRODUCTION() {
    return profilproduction;
  }

  /**
   * Définit la valeur de la propriété profilproduction.
   *
   * @param value allowed object is {@link InfoCarteVitale.PROFILPRODUCTION }
   */
  public void setPROFILPRODUCTION(InfoCarteVitale.PROFILPRODUCTION value) {
    this.profilproduction = value;
  }

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
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="TYPE_CARTE"
   * type="{http://www.almerys.com/NormeV3}codeCarteVitale"/&amp;gt; &amp;lt;element
   * name="TYPE_CARTE_A_PRODUIRE" type="{http://www.almerys.com/NormeV3}CodeProduction"/&amp;gt;
   * &amp;lt;element name="NATURE" type="{http://www.almerys.com/NormeV3}CodeNature"/&amp;gt;
   * &amp;lt;element name="CODE_PROFIL" type="{http://www.almerys.com/NormeV3}string-9"/&amp;gt;
   * &amp;lt;element name="LOCALISATION_POSTALE"
   * type="{http://www.almerys.com/NormeV3}nonNegativeInteger-1"/&amp;gt; &amp;lt;element
   * name="ORIGINE" type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt;
   * &amp;lt;element name="CARTE_ACTIVE" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
   * &amp;lt;element name="INDICATEUR_ENCART"
   * type="{http://www.almerys.com/NormeV3}positiveInteger-1" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="PHOTO" type="{http://www.almerys.com/NormeV3}string-30"/&amp;gt; &amp;lt;element
   * name="SERIGRAPHIE"&amp;gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt;
   * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
   * &amp;lt;sequence&amp;gt; &amp;lt;element name="NOM_PORTEUR"
   * type="{http://www.almerys.com/NormeV3}string-1-80"/&amp;gt; &amp;lt;element
   * name="PRENOM_PORTEUR" type="{http://www.almerys.com/NormeV3}string-1-40"/&amp;gt;
   * &amp;lt;element name="NNI_PORTEUR" type="{http://www.almerys.com/NormeV3}string-1-13"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt;
   * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
   * &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {
        "typecarte",
        "typecarteaproduire",
        "nature",
        "codeprofil",
        "localisationpostale",
        "origine",
        "carteactive",
        "indicateurencart",
        "photo",
        "serigraphie"
      })
  public static class PROFILPRODUCTION implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "TYPE_CARTE", required = true)
    protected String typecarte;

    @XmlElement(name = "TYPE_CARTE_A_PRODUIRE", required = true)
    protected String typecarteaproduire;

    @XmlElement(name = "NATURE", required = true)
    @XmlSchemaType(name = "string")
    protected CodeNature nature;

    @XmlElement(name = "CODE_PROFIL", required = true)
    protected String codeprofil;

    @XmlElement(name = "LOCALISATION_POSTALE", required = true, type = String.class)
    @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer localisationpostale;

    @XmlElement(name = "ORIGINE", required = true, type = String.class)
    @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer origine;

    @XmlElement(name = "CARTE_ACTIVE", required = true, type = String.class)
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    @XmlSchemaType(name = "boolean")
    protected Boolean carteactive;

    @XmlElement(name = "INDICATEUR_ENCART", type = String.class)
    @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer indicateurencart;

    @XmlElement(name = "PHOTO", required = true)
    protected String photo;

    @XmlElement(name = "SERIGRAPHIE", required = true)
    protected InfoCarteVitale.PROFILPRODUCTION.SERIGRAPHIE serigraphie;

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
     * Obtient la valeur de la propriété typecarteaproduire.
     *
     * @return possible object is {@link String }
     */
    public String getTYPECARTEAPRODUIRE() {
      return typecarteaproduire;
    }

    /**
     * Définit la valeur de la propriété typecarteaproduire.
     *
     * @param value allowed object is {@link String }
     */
    public void setTYPECARTEAPRODUIRE(String value) {
      this.typecarteaproduire = value;
    }

    /**
     * Obtient la valeur de la propriété nature.
     *
     * @return possible object is {@link CodeNature }
     */
    public CodeNature getNATURE() {
      return nature;
    }

    /**
     * Définit la valeur de la propriété nature.
     *
     * @param value allowed object is {@link CodeNature }
     */
    public void setNATURE(CodeNature value) {
      this.nature = value;
    }

    /**
     * Obtient la valeur de la propriété codeprofil.
     *
     * @return possible object is {@link String }
     */
    public String getCODEPROFIL() {
      return codeprofil;
    }

    /**
     * Définit la valeur de la propriété codeprofil.
     *
     * @param value allowed object is {@link String }
     */
    public void setCODEPROFIL(String value) {
      this.codeprofil = value;
    }

    /**
     * Obtient la valeur de la propriété localisationpostale.
     *
     * @return possible object is {@link String }
     */
    public Integer getLOCALISATIONPOSTALE() {
      return localisationpostale;
    }

    /**
     * Définit la valeur de la propriété localisationpostale.
     *
     * @param value allowed object is {@link String }
     */
    public void setLOCALISATIONPOSTALE(Integer value) {
      this.localisationpostale = value;
    }

    /**
     * Obtient la valeur de la propriété origine.
     *
     * @return possible object is {@link String }
     */
    public Integer getORIGINE() {
      return origine;
    }

    /**
     * Définit la valeur de la propriété origine.
     *
     * @param value allowed object is {@link String }
     */
    public void setORIGINE(Integer value) {
      this.origine = value;
    }

    /**
     * Obtient la valeur de la propriété carteactive.
     *
     * @return possible object is {@link String }
     */
    public Boolean isCARTEACTIVE() {
      return carteactive;
    }

    /**
     * Définit la valeur de la propriété carteactive.
     *
     * @param value allowed object is {@link String }
     */
    public void setCARTEACTIVE(Boolean value) {
      this.carteactive = value;
    }

    /**
     * Obtient la valeur de la propriété indicateurencart.
     *
     * @return possible object is {@link String }
     */
    public Integer getINDICATEURENCART() {
      return indicateurencart;
    }

    /**
     * Définit la valeur de la propriété indicateurencart.
     *
     * @param value allowed object is {@link String }
     */
    public void setINDICATEURENCART(Integer value) {
      this.indicateurencart = value;
    }

    /**
     * Obtient la valeur de la propriété photo.
     *
     * @return possible object is {@link String }
     */
    public String getPHOTO() {
      return photo;
    }

    /**
     * Définit la valeur de la propriété photo.
     *
     * @param value allowed object is {@link String }
     */
    public void setPHOTO(String value) {
      this.photo = value;
    }

    /**
     * Obtient la valeur de la propriété serigraphie.
     *
     * @return possible object is {@link InfoCarteVitale.PROFILPRODUCTION.SERIGRAPHIE }
     */
    public InfoCarteVitale.PROFILPRODUCTION.SERIGRAPHIE getSERIGRAPHIE() {
      return serigraphie;
    }

    /**
     * Définit la valeur de la propriété serigraphie.
     *
     * @param value allowed object is {@link InfoCarteVitale.PROFILPRODUCTION.SERIGRAPHIE }
     */
    public void setSERIGRAPHIE(InfoCarteVitale.PROFILPRODUCTION.SERIGRAPHIE value) {
      this.serigraphie = value;
    }

    /**
     * &lt;p&gt;Classe Java pour anonymous complex type.
     *
     * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
     * classe.
     *
     * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
     * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
     * &amp;lt;element name="NOM_PORTEUR"
     * type="{http://www.almerys.com/NormeV3}string-1-80"/&amp;gt; &amp;lt;element
     * name="PRENOM_PORTEUR" type="{http://www.almerys.com/NormeV3}string-1-40"/&amp;gt;
     * &amp;lt;element name="NNI_PORTEUR"
     * type="{http://www.almerys.com/NormeV3}string-1-13"/&amp;gt; &amp;lt;/sequence&amp;gt;
     * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
     * &lt;/pre&gt;
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
        name = "",
        propOrder = {"nomporteur", "prenomporteur", "nniporteur"})
    public static class SERIGRAPHIE implements Serializable {

      private static final long serialVersionUID = -1L;

      @XmlElement(name = "NOM_PORTEUR", required = true)
      protected String nomporteur;

      @XmlElement(name = "PRENOM_PORTEUR", required = true)
      protected String prenomporteur;

      @XmlElement(name = "NNI_PORTEUR", required = true)
      protected String nniporteur;

      /**
       * Obtient la valeur de la propriété nomporteur.
       *
       * @return possible object is {@link String }
       */
      public String getNOMPORTEUR() {
        return nomporteur;
      }

      /**
       * Définit la valeur de la propriété nomporteur.
       *
       * @param value allowed object is {@link String }
       */
      public void setNOMPORTEUR(String value) {
        this.nomporteur = value;
      }

      /**
       * Obtient la valeur de la propriété prenomporteur.
       *
       * @return possible object is {@link String }
       */
      public String getPRENOMPORTEUR() {
        return prenomporteur;
      }

      /**
       * Définit la valeur de la propriété prenomporteur.
       *
       * @param value allowed object is {@link String }
       */
      public void setPRENOMPORTEUR(String value) {
        this.prenomporteur = value;
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
    }
  }
}
