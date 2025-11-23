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
 * &lt;p&gt;Classe Java pour infoDroitFamille complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoDroitFamille"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="DATE_DEBUT"
 * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element name="DATE_FIN"
 * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoDroitFamille",
    propOrder = {"datedebut", "datefin"})
public class InfoDroitFamille implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "DATE_DEBUT", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebut;

  @XmlElement(name = "DATE_FIN", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefin;

  /**
   * Obtient la valeur de la propriété datedebut.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBUT() {
    return datedebut;
  }

  /**
   * Définit la valeur de la propriété datedebut.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBUT(String value) {
    this.datedebut = value;
  }

  /**
   * Obtient la valeur de la propriété datefin.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFIN() {
    return datefin;
  }

  /**
   * Définit la valeur de la propriété datefin.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFIN(String value) {
    this.datefin = value;
  }
}
