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
 * &lt;p&gt;Classe Java pour infoIndividuOs complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoIndividuOs"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_INTERNE_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="NIS"
 * type="{http://www.almerys.com/NormeV3}string-16" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_CERTIFICATION_NIS" type="{http://www.almerys.com/NormeV3}string-10"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_NAISSANCE"
 * type="{http://www.almerys.com/NormeV3}string-10"/&amp;gt; &amp;lt;element name="RANG_NAISSANCE"
 * type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="COMMUNE_NAISSANCE" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NOM_PATRONIMIQUE"
 * type="{http://www.almerys.com/NormeV3}string-1-80"/&amp;gt; &amp;lt;element name="NOM_USAGE"
 * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="PRENOM" type="{http://www.almerys.com/NormeV3}string-1-40"/&amp;gt; &amp;lt;element
 * name="CODE_SEXE" type="{http://www.almerys.com/NormeV3}codeSexe" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="PROFESSION" type="{http://www.almerys.com/NormeV3}infoProfession"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="MEDECIN_TRAITANT"
 * type="{http://www.almerys.com/NormeV3}nonNegativeInteger-1" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="IDENTITE_WEB" type="{http://www.almerys.com/NormeV3}infoWeb"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="AUTRE" maxOccurs="unbounded" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TYPE" type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt; &amp;lt;element
 * name="VALEUR" type="{http://www.almerys.com/NormeV3}string-1-50"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element name="PORTEUR_RISQUE"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="REF_INTERNE_ALMERYS" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_CAL"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="TYPE_CAL" type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoIndividuOs",
    propOrder = {
      "refinterneos",
      "nis",
      "datecertificationnis",
      "datenaissance",
      "rangnaissance",
      "communenaissance",
      "nompatronimique",
      "nomusage",
      "prenom",
      "codesexe",
      "profession",
      "medecintraitant",
      "identiteweb",
      "autres",
      "porteurrisque",
      "refinternealmerys",
      "datecal",
      "typecal"
    })
public class InfoIndividuOs implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_INTERNE_OS", required = true)
  protected String refinterneos;

  @XmlElement(name = "NIS")
  protected String nis;

  @XmlElement(name = "DATE_CERTIFICATION_NIS")
  protected String datecertificationnis;

  @XmlElement(name = "DATE_NAISSANCE", required = true)
  protected String datenaissance;

  @XmlElement(name = "RANG_NAISSANCE", type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer rangnaissance;

  @XmlElement(name = "COMMUNE_NAISSANCE")
  protected String communenaissance;

  @XmlElement(name = "NOM_PATRONIMIQUE", required = true)
  protected String nompatronimique;

  @XmlElement(name = "NOM_USAGE")
  protected String nomusage;

  @XmlElement(name = "PRENOM", required = true)
  protected String prenom;

  @XmlElement(name = "CODE_SEXE")
  @XmlSchemaType(name = "string")
  protected CodeSexe codesexe;

  @XmlElement(name = "PROFESSION")
  protected InfoProfession profession;

  @XmlElement(name = "MEDECIN_TRAITANT", type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "nonNegativeInteger")
  protected Integer medecintraitant;

  @XmlElement(name = "IDENTITE_WEB")
  protected InfoWeb identiteweb;

  @XmlElement(name = "AUTRE")
  protected List<InfoIndividuOs.AUTRE> autres;

  @XmlElement(name = "PORTEUR_RISQUE")
  protected String porteurrisque;

  @XmlElement(name = "REF_INTERNE_ALMERYS")
  protected String refinternealmerys;

  @XmlElement(name = "DATE_CAL")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datecal;

  @XmlElement(name = "TYPE_CAL")
  protected String typecal;

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
   * Obtient la valeur de la propriété datecertificationnis.
   *
   * @return possible object is {@link String }
   */
  public String getDATECERTIFICATIONNIS() {
    return datecertificationnis;
  }

  /**
   * Définit la valeur de la propriété datecertificationnis.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATECERTIFICATIONNIS(String value) {
    this.datecertificationnis = value;
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

  /**
   * Obtient la valeur de la propriété rangnaissance.
   *
   * @return possible object is {@link String }
   */
  public Integer getRANGNAISSANCE() {
    return rangnaissance;
  }

  /**
   * Définit la valeur de la propriété rangnaissance.
   *
   * @param value allowed object is {@link String }
   */
  public void setRANGNAISSANCE(Integer value) {
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
   * @return possible object is {@link CodeSexe }
   */
  public CodeSexe getCODESEXE() {
    return codesexe;
  }

  /**
   * Définit la valeur de la propriété codesexe.
   *
   * @param value allowed object is {@link CodeSexe }
   */
  public void setCODESEXE(CodeSexe value) {
    this.codesexe = value;
  }

  /**
   * Obtient la valeur de la propriété profession.
   *
   * @return possible object is {@link InfoProfession }
   */
  public InfoProfession getPROFESSION() {
    return profession;
  }

  /**
   * Définit la valeur de la propriété profession.
   *
   * @param value allowed object is {@link InfoProfession }
   */
  public void setPROFESSION(InfoProfession value) {
    this.profession = value;
  }

  /**
   * Obtient la valeur de la propriété medecintraitant.
   *
   * @return possible object is {@link String }
   */
  public Integer getMEDECINTRAITANT() {
    return medecintraitant;
  }

  /**
   * Définit la valeur de la propriété medecintraitant.
   *
   * @param value allowed object is {@link String }
   */
  public void setMEDECINTRAITANT(Integer value) {
    this.medecintraitant = value;
  }

  /**
   * Obtient la valeur de la propriété identiteweb.
   *
   * @return possible object is {@link InfoWeb }
   */
  public InfoWeb getIDENTITEWEB() {
    return identiteweb;
  }

  /**
   * Définit la valeur de la propriété identiteweb.
   *
   * @param value allowed object is {@link InfoWeb }
   */
  public void setIDENTITEWEB(InfoWeb value) {
    this.identiteweb = value;
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
   * InfoIndividuOs.AUTRE }
   */
  public List<InfoIndividuOs.AUTRE> getAUTRES() {
    if (autres == null) {
      autres = new ArrayList<InfoIndividuOs.AUTRE>();
    }
    return this.autres;
  }

  /**
   * Obtient la valeur de la propriété porteurrisque.
   *
   * @return possible object is {@link String }
   */
  public String getPORTEURRISQUE() {
    return porteurrisque;
  }

  /**
   * Définit la valeur de la propriété porteurrisque.
   *
   * @param value allowed object is {@link String }
   */
  public void setPORTEURRISQUE(String value) {
    this.porteurrisque = value;
  }

  /**
   * Obtient la valeur de la propriété refinternealmerys.
   *
   * @return possible object is {@link String }
   */
  public String getREFINTERNEALMERYS() {
    return refinternealmerys;
  }

  /**
   * Définit la valeur de la propriété refinternealmerys.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFINTERNEALMERYS(String value) {
    this.refinternealmerys = value;
  }

  /**
   * Obtient la valeur de la propriété datecal.
   *
   * @return possible object is {@link String }
   */
  public String getDATECAL() {
    return datecal;
  }

  /**
   * Définit la valeur de la propriété datecal.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATECAL(String value) {
    this.datecal = value;
  }

  /**
   * Obtient la valeur de la propriété typecal.
   *
   * @return possible object is {@link String }
   */
  public String getTYPECAL() {
    return typecal;
  }

  /**
   * Définit la valeur de la propriété typecal.
   *
   * @param value allowed object is {@link String }
   */
  public void setTYPECAL(String value) {
    this.typecal = value;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="TYPE" type="{http://www.almerys.com/NormeV3}positiveInteger-1"/&amp;gt;
   * &amp;lt;element name="VALEUR" type="{http://www.almerys.com/NormeV3}string-1-50"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"type", "valeur"})
  public static class AUTRE implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "TYPE", required = true, type = String.class)
    @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer type;

    @XmlElement(name = "VALEUR", required = true)
    protected String valeur;

    /**
     * Obtient la valeur de la propriété type.
     *
     * @return possible object is {@link String }
     */
    public Integer getTYPE() {
      return type;
    }

    /**
     * Définit la valeur de la propriété type.
     *
     * @param value allowed object is {@link String }
     */
    public void setTYPE(Integer value) {
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
