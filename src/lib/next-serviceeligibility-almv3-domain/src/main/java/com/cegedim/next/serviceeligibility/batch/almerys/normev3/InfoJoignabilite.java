//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoJoignabilite complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoJoignabilite"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="MEDIA"
 * type="{http://www.almerys.com/NormeV3}codeMedia"/&amp;gt; &amp;lt;element name="ADRESSE_MEDIA"
 * type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt; &amp;lt;element name="ACTIF"
 * type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoJoignabilite",
    propOrder = {"media", "adressemedia", "actif"})
public class InfoJoignabilite implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "MEDIA", required = true)
  @XmlSchemaType(name = "string")
  protected CodeMedia media;

  @XmlElement(name = "ADRESSE_MEDIA", required = true)
  protected String adressemedia;

  @XmlElement(name = "ACTIF", required = true, type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean actif;

  /**
   * Obtient la valeur de la propriété media.
   *
   * @return possible object is {@link CodeMedia }
   */
  public CodeMedia getMEDIA() {
    return media;
  }

  /**
   * Définit la valeur de la propriété media.
   *
   * @param value allowed object is {@link CodeMedia }
   */
  public void setMEDIA(CodeMedia value) {
    this.media = value;
  }

  /**
   * Obtient la valeur de la propriété adressemedia.
   *
   * @return possible object is {@link String }
   */
  public String getADRESSEMEDIA() {
    return adressemedia;
  }

  /**
   * Définit la valeur de la propriété adressemedia.
   *
   * @param value allowed object is {@link String }
   */
  public void setADRESSEMEDIA(String value) {
    this.adressemedia = value;
  }

  /**
   * Obtient la valeur de la propriété actif.
   *
   * @return possible object is {@link String }
   */
  public Boolean isACTIF() {
    return actif;
  }

  /**
   * Définit la valeur de la propriété actif.
   *
   * @param value allowed object is {@link String }
   */
  public void setACTIF(Boolean value) {
    this.actif = value;
  }
}
