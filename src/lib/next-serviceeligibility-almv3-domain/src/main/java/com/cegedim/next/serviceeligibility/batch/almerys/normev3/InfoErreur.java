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
 * &lt;p&gt;Classe Java pour InfoErreur complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="InfoErreur"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="CODE_ERREUR"
 * type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element name="LIBELLE_ERREUR"
 * type="{http://www.almerys.com/NormeV3}string-1-10"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "InfoErreur",
    propOrder = {"codeerreur", "libelleerreur"})
public class InfoErreur implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "CODE_ERREUR", required = true)
  protected String codeerreur;

  @XmlElement(name = "LIBELLE_ERREUR", required = true)
  protected String libelleerreur;

  /**
   * Obtient la valeur de la propriété codeerreur.
   *
   * @return possible object is {@link String }
   */
  public String getCODEERREUR() {
    return codeerreur;
  }

  /**
   * Définit la valeur de la propriété codeerreur.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEERREUR(String value) {
    this.codeerreur = value;
  }

  /**
   * Obtient la valeur de la propriété libelleerreur.
   *
   * @return possible object is {@link String }
   */
  public String getLIBELLEERREUR() {
    return libelleerreur;
  }

  /**
   * Définit la valeur de la propriété libelleerreur.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIBELLEERREUR(String value) {
    this.libelleerreur = value;
  }
}
