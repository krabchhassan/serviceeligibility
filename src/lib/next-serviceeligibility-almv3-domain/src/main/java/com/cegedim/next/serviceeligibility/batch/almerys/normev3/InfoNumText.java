//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.PositiveIntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoNumText complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoNumText"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NUM_LIGNE"
 * type="{http://www.almerys.com/NormeV3}positiveInteger-2"/&amp;gt; &amp;lt;element name="LIBELLE"
 * type="{http://www.almerys.com/NormeV3}string-1-96"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoNumText",
    propOrder = {"numligne", "libelle"})
public class InfoNumText implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NUM_LIGNE", required = true, type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer numligne;

  @XmlElement(name = "LIBELLE", required = true)
  protected String libelle;

  /**
   * Obtient la valeur de la propriété numligne.
   *
   * @return possible object is {@link String }
   */
  public Integer getNUMLIGNE() {
    return numligne;
  }

  /**
   * Définit la valeur de la propriété numligne.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMLIGNE(Integer value) {
    this.numligne = value;
  }

  /**
   * Obtient la valeur de la propriété libelle.
   *
   * @return possible object is {@link String }
   */
  public String getLIBELLE() {
    return libelle;
  }

  /**
   * Définit la valeur de la propriété libelle.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIBELLE(String value) {
    this.libelle = value;
  }
}
