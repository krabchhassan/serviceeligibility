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

/**
 * &lt;p&gt;Classe Java pour infoIndividuOsTP complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoIndividuOsTP"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_INTERNE_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="NIS"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element name="DATE_NAISSANCE"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element name="RANG_NAISSANCE"
 * type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt; &amp;lt;element name="COMMUNE_NAISSANCE"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element name="NOM_PATRONIMIQUE"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element name="PRENOM"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element name="NOM_USAGE"
 * type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoIndividuOsTP",
    propOrder = {
      "refinterneos",
      "nis",
      "datenaissance",
      "rangnaissance",
      "communenaissance",
      "nompatronimique",
      "prenom",
      "nomusage"
    })
public class InfoIndividuOsTP implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_INTERNE_OS", required = true)
  protected String refinterneos;

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

  @XmlElement(name = "NOM_USAGE")
  protected Integer nomusage;

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
   * Obtient la valeur de la propriété nomusage.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getNOMUSAGE() {
    return nomusage;
  }

  /**
   * Définit la valeur de la propriété nomusage.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setNOMUSAGE(Integer value) {
    this.nomusage = value;
  }
}
