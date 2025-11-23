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
 * &lt;p&gt;Classe Java pour infoWeb complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoWeb"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="LOGIN"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="PASSWORD"
 * type="{http://www.almerys.com/NormeV3}string-1-40" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="QUESTION_SECRETE" type="{http://www.almerys.com/NormeV3}string-1-100"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="REPONSE_SECRETE"
 * type="{http://www.almerys.com/NormeV3}string-1-50" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoWeb",
    propOrder = {"login", "password", "questionsecrete", "reponsesecrete"})
public class InfoWeb implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "LOGIN", required = true)
  protected String login;

  @XmlElement(name = "PASSWORD")
  protected String password;

  @XmlElement(name = "QUESTION_SECRETE")
  protected String questionsecrete;

  @XmlElement(name = "REPONSE_SECRETE")
  protected String reponsesecrete;

  /**
   * Obtient la valeur de la propriété login.
   *
   * @return possible object is {@link String }
   */
  public String getLOGIN() {
    return login;
  }

  /**
   * Définit la valeur de la propriété login.
   *
   * @param value allowed object is {@link String }
   */
  public void setLOGIN(String value) {
    this.login = value;
  }

  /**
   * Obtient la valeur de la propriété password.
   *
   * @return possible object is {@link String }
   */
  public String getPASSWORD() {
    return password;
  }

  /**
   * Définit la valeur de la propriété password.
   *
   * @param value allowed object is {@link String }
   */
  public void setPASSWORD(String value) {
    this.password = value;
  }

  /**
   * Obtient la valeur de la propriété questionsecrete.
   *
   * @return possible object is {@link String }
   */
  public String getQUESTIONSECRETE() {
    return questionsecrete;
  }

  /**
   * Définit la valeur de la propriété questionsecrete.
   *
   * @param value allowed object is {@link String }
   */
  public void setQUESTIONSECRETE(String value) {
    this.questionsecrete = value;
  }

  /**
   * Obtient la valeur de la propriété reponsesecrete.
   *
   * @return possible object is {@link String }
   */
  public String getREPONSESECRETE() {
    return reponsesecrete;
  }

  /**
   * Définit la valeur de la propriété reponsesecrete.
   *
   * @param value allowed object is {@link String }
   */
  public void setREPONSESECRETE(String value) {
    this.reponsesecrete = value;
  }
}
