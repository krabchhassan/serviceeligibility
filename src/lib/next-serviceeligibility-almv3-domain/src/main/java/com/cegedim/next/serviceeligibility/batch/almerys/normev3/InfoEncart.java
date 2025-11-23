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
 * &lt;p&gt;Classe Java pour infoEncart complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoEncart"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="CODE_ENCART"
 * type="{http://www.almerys.com/NormeV3}string-1-10" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_MAILER" type="{http://www.almerys.com/NormeV3}string-1-10" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CODE_ANNEXE" type="{http://www.almerys.com/NormeV3}string-1-10"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_ENVOI"
 * type="{http://www.almerys.com/NormeV3}string-1-8" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoEncart",
    propOrder = {"codeencart", "codemailer", "codeannexe", "codeenvoi"})
public class InfoEncart implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "CODE_ENCART")
  protected String codeencart;

  @XmlElement(name = "CODE_MAILER")
  protected String codemailer;

  @XmlElement(name = "CODE_ANNEXE")
  protected String codeannexe;

  @XmlElement(name = "CODE_ENVOI")
  protected String codeenvoi;

  /**
   * Obtient la valeur de la propriété codeencart.
   *
   * @return possible object is {@link String }
   */
  public String getCODEENCART() {
    return codeencart;
  }

  /**
   * Définit la valeur de la propriété codeencart.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEENCART(String value) {
    this.codeencart = value;
  }

  /**
   * Obtient la valeur de la propriété codemailer.
   *
   * @return possible object is {@link String }
   */
  public String getCODEMAILER() {
    return codemailer;
  }

  /**
   * Définit la valeur de la propriété codemailer.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEMAILER(String value) {
    this.codemailer = value;
  }

  /**
   * Obtient la valeur de la propriété codeannexe.
   *
   * @return possible object is {@link String }
   */
  public String getCODEANNEXE() {
    return codeannexe;
  }

  /**
   * Définit la valeur de la propriété codeannexe.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEANNEXE(String value) {
    this.codeannexe = value;
  }

  /**
   * Obtient la valeur de la propriété codeenvoi.
   *
   * @return possible object is {@link String }
   */
  public String getCODEENVOI() {
    return codeenvoi;
  }

  /**
   * Définit la valeur de la propriété codeenvoi.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEENVOI(String value) {
    this.codeenvoi = value;
  }
}
