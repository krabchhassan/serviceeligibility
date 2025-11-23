//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoAMC complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoAMC"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NUMERO_B2"
 * type="{http://www.almerys.com/NormeV3}string-10"/&amp;gt; &amp;lt;element name="NUMERO_EDI"
 * type="{http://www.almerys.com/NormeV3}string-19" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NUM_ADHERENT" type="{http://www.almerys.com/NormeV3}string-8" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="NUM_CONTRAT_OS" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_DEBUT_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_FIN_VALIDITE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CODE_ROUTAGE" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt;
 * &amp;lt;element name="IDENT_HOTE" type="{http://www.almerys.com/NormeV3}string-3"/&amp;gt;
 * &amp;lt;element name="NOM_DOMAINE" type="{http://www.almerys.com/NormeV3}string-1-20"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoAMC",
    propOrder = {
      "numerob2",
      "numeroedi",
      "numadherent",
      "numcontratos",
      "datedebutvalidite",
      "datefinvalidite",
      "coderoutage",
      "identhote",
      "nomdomaine"
    })
public class InfoAMC implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NUMERO_B2", required = true)
  protected String numerob2;

  @XmlElement(name = "NUMERO_EDI")
  protected String numeroedi;

  @XmlElement(name = "NUM_ADHERENT")
  protected String numadherent;

  @XmlElement(name = "NUM_CONTRAT_OS")
  protected String numcontratos;

  @XmlElement(name = "DATE_DEBUT_VALIDITE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutvalidite;

  @XmlElement(name = "DATE_FIN_VALIDITE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinvalidite;

  @XmlElement(name = "CODE_ROUTAGE", required = true)
  protected String coderoutage;

  @XmlElement(name = "IDENT_HOTE", required = true)
  protected String identhote;

  @XmlElement(name = "NOM_DOMAINE")
  protected String nomdomaine;

  /**
   * Obtient la valeur de la propriété numerob2.
   *
   * @return possible object is {@link String }
   */
  public String getNUMEROB2() {
    return numerob2;
  }

  /**
   * Définit la valeur de la propriété numerob2.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMEROB2(String value) {
    this.numerob2 = value;
  }

  /**
   * Obtient la valeur de la propriété numeroedi.
   *
   * @return possible object is {@link String }
   */
  public String getNUMEROEDI() {
    return numeroedi;
  }

  /**
   * Définit la valeur de la propriété numeroedi.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMEROEDI(String value) {
    this.numeroedi = value;
  }

  /**
   * Obtient la valeur de la propriété numadherent.
   *
   * @return possible object is {@link String }
   */
  public String getNUMADHERENT() {
    return numadherent;
  }

  /**
   * Définit la valeur de la propriété numadherent.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMADHERENT(String value) {
    this.numadherent = value;
  }

  /**
   * Obtient la valeur de la propriété numcontratos.
   *
   * @return possible object is {@link String }
   */
  public String getNUMCONTRATOS() {
    return numcontratos;
  }

  /**
   * Définit la valeur de la propriété numcontratos.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMCONTRATOS(String value) {
    this.numcontratos = value;
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
   * Obtient la valeur de la propriété coderoutage.
   *
   * @return possible object is {@link String }
   */
  public String getCODEROUTAGE() {
    return coderoutage;
  }

  /**
   * Définit la valeur de la propriété coderoutage.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEROUTAGE(String value) {
    this.coderoutage = value;
  }

  /**
   * Obtient la valeur de la propriété identhote.
   *
   * @return possible object is {@link String }
   */
  public String getIDENTHOTE() {
    return identhote;
  }

  /**
   * Définit la valeur de la propriété identhote.
   *
   * @param value allowed object is {@link String }
   */
  public void setIDENTHOTE(String value) {
    this.identhote = value;
  }

  /**
   * Obtient la valeur de la propriété nomdomaine.
   *
   * @return possible object is {@link String }
   */
  public String getNOMDOMAINE() {
    return nomdomaine;
  }

  /**
   * Définit la valeur de la propriété nomdomaine.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOMDOMAINE(String value) {
    this.nomdomaine = value;
  }
}
