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
 * &lt;p&gt;Classe Java pour infoETMFamille complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoETMFamille"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="CODE_ETM"
 * type="{http://www.almerys.com/NormeV3}codeEtm"/&amp;gt; &amp;lt;element name="DATE_DEBUT_ETM"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_FIN_ETM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_PROLONGATION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoETMFamille",
    propOrder = {"codeetm", "datedebutetm", "datefinetm", "dateprolongation"})
public class InfoETMFamille implements Serializable {

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
