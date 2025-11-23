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
 * &lt;p&gt;Classe Java pour infoIndividuOsRMS complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoIndividuOsRMS"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="REF_ADHERENT_OS" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt; &amp;lt;element
 * name="NIS" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="DATE_NAISSANCE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="RANG_NAISSANCE" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt; &amp;lt;element
 * name="COMMUNE_NAISSANCE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="NOM_PATRONIMIQUE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="PRENOM" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="CODE_SEXE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element
 * name="NOM_USAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="AUTRE" maxOccurs="unbounded" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TYPE" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt; &amp;lt;element name="VALEUR"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoIndividuOsRMS",
    propOrder = {
      "refadherentos",
      "nis",
      "datenaissance",
      "rangnaissance",
      "communenaissance",
      "nompatronimique",
      "prenom",
      "codesexe",
      "nomusage",
      "autres"
    })
public class InfoIndividuOsRMS implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_ADHERENT_OS")
  protected int refadherentos;

  @XmlElement(name = "NIS", required = true)
  protected String nis;

  @XmlElement(name = "DATE_NAISSANCE", required = true)
  protected String datenaissance;

  @XmlElement(name = "RANG_NAISSANCE")
  protected int rangnaissance;

  @XmlElement(name = "COMMUNE_NAISSANCE", required = true)
  protected String communenaissance;

  @XmlElement(name = "NOM_PATRONIMIQUE", required = true)
  protected String nompatronimique;

  @XmlElement(name = "PRENOM", required = true)
  protected String prenom;

  @XmlElement(name = "CODE_SEXE", required = true)
  protected String codesexe;

  @XmlElement(name = "NOM_USAGE")
  protected String nomusage;

  @XmlElement(name = "AUTRE")
  protected List<InfoIndividuOsRMS.AUTRE> autres;

  /** Obtient la valeur de la propriété refadherentos. */
  public int getREFADHERENTOS() {
    return refadherentos;
  }

  /** Définit la valeur de la propriété refadherentos. */
  public void setREFADHERENTOS(int value) {
    this.refadherentos = value;
  }

  /**
   * Obtient la valeur de la propriété nis.
   *
   * @return possible object is {@link String }
   */
  public String getNIS() {
    return nis;
  }

  /**
   * Définit la valeur de la propriété nis.
   *
   * @param value allowed object is {@link String }
   */
  public void setNIS(String value) {
    this.nis = value;
  }

  /**
   * Obtient la valeur de la propriété datenaissance.
   *
   * @return possible object is {@link String }
   */
  public String getDATENAISSANCE() {
    return datenaissance;
  }

  /**
   * Définit la valeur de la propriété datenaissance.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATENAISSANCE(String value) {
    this.datenaissance = value;
  }

  /** Obtient la valeur de la propriété rangnaissance. */
  public int getRANGNAISSANCE() {
    return rangnaissance;
  }

  /** Définit la valeur de la propriété rangnaissance. */
  public void setRANGNAISSANCE(int value) {
    this.rangnaissance = value;
  }

  /**
   * Obtient la valeur de la propriété communenaissance.
   *
   * @return possible object is {@link String }
   */
  public String getCOMMUNENAISSANCE() {
    return communenaissance;
  }

  /**
   * Définit la valeur de la propriété communenaissance.
   *
   * @param value allowed object is {@link String }
   */
  public void setCOMMUNENAISSANCE(String value) {
    this.communenaissance = value;
  }

  /**
   * Obtient la valeur de la propriété nompatronimique.
   *
   * @return possible object is {@link String }
   */
  public String getNOMPATRONIMIQUE() {
    return nompatronimique;
  }

  /**
   * Définit la valeur de la propriété nompatronimique.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOMPATRONIMIQUE(String value) {
    this.nompatronimique = value;
  }

  /**
   * Obtient la valeur de la propriété prenom.
   *
   * @return possible object is {@link String }
   */
  public String getPRENOM() {
    return prenom;
  }

  /**
   * Définit la valeur de la propriété prenom.
   *
   * @param value allowed object is {@link String }
   */
  public void setPRENOM(String value) {
    this.prenom = value;
  }

  /**
   * Obtient la valeur de la propriété codesexe.
   *
   * @return possible object is {@link String }
   */
  public String getCODESEXE() {
    return codesexe;
  }

  /**
   * Définit la valeur de la propriété codesexe.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODESEXE(String value) {
    this.codesexe = value;
  }

  /**
   * Obtient la valeur de la propriété nomusage.
   *
   * @return possible object is {@link String }
   */
  public String getNOMUSAGE() {
    return nomusage;
  }

  /**
   * Définit la valeur de la propriété nomusage.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOMUSAGE(String value) {
    this.nomusage = value;
  }

  /**
   * Gets the value of the autres property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the autres property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getAUTRES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
   * InfoIndividuOsRMS.AUTRE }
   */
  public List<InfoIndividuOsRMS.AUTRE> getAUTRES() {
    if (autres == null) {
      autres = new ArrayList<InfoIndividuOsRMS.AUTRE>();
    }
    return this.autres;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
   * &amp;lt;element name="VALEUR" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"type", "valeur"})
  public static class AUTRE implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "TYPE")
    protected int type;

    @XmlElement(name = "VALEUR", required = true)
    protected String valeur;

    /** Obtient la valeur de la propriété type. */
    public int getTYPE() {
      return type;
    }

    /** Définit la valeur de la propriété type. */
    public void setTYPE(int value) {
      this.type = value;
    }

    /**
     * Obtient la valeur de la propriété valeur.
     *
     * @return possible object is {@link String }
     */
    public String getVALEUR() {
      return valeur;
    }

    /**
     * Définit la valeur de la propriété valeur.
     *
     * @param value allowed object is {@link String }
     */
    public void setVALEUR(String value) {
      this.valeur = value;
    }
  }
}
