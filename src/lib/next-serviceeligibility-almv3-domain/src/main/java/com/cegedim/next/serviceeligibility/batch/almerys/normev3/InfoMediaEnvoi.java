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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoMediaEnvoi complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoMediaEnvoi"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="RELEVE_PRESTA"
 * type="{http://www.almerys.com/NormeV3}codeRelevePresta" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="COURRIER_GESTION" type="{http://www.almerys.com/NormeV3}codeCourrierGestion"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoMediaEnvoi",
    propOrder = {"relevepresta", "courriergestion"})
public class InfoMediaEnvoi implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "RELEVE_PRESTA")
  @XmlSchemaType(name = "string")
  protected CodeRelevePresta relevepresta;

  @XmlElement(name = "COURRIER_GESTION")
  @XmlSchemaType(name = "string")
  protected CodeCourrierGestion courriergestion;

  /**
   * Obtient la valeur de la propriété relevepresta.
   *
   * @return possible object is {@link CodeRelevePresta }
   */
  public CodeRelevePresta getRELEVEPRESTA() {
    return relevepresta;
  }

  /**
   * Définit la valeur de la propriété relevepresta.
   *
   * @param value allowed object is {@link CodeRelevePresta }
   */
  public void setRELEVEPRESTA(CodeRelevePresta value) {
    this.relevepresta = value;
  }

  /**
   * Obtient la valeur de la propriété courriergestion.
   *
   * @return possible object is {@link CodeCourrierGestion }
   */
  public CodeCourrierGestion getCOURRIERGESTION() {
    return courriergestion;
  }

  /**
   * Définit la valeur de la propriété courriergestion.
   *
   * @param value allowed object is {@link CodeCourrierGestion }
   */
  public void setCOURRIERGESTION(CodeCourrierGestion value) {
    this.courriergestion = value;
  }
}
