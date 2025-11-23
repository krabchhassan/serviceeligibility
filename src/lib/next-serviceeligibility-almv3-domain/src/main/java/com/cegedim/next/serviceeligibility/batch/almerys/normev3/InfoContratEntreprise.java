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
 * &lt;p&gt;Classe Java pour infoContratEntreprise complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoContratEntreprise"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TYPE_DESTINATAIRE" type="{http://www.almerys.com/NormeV3}codeDestinataire"/&amp;gt;
 * &amp;lt;element name="NUM_AVENANT" type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoContratEntreprise",
    propOrder = {"typedestinataire", "numavenant"})
public class InfoContratEntreprise implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "TYPE_DESTINATAIRE", required = true)
  @XmlSchemaType(name = "string")
  protected CodeDestinataire typedestinataire;

  @XmlElement(name = "NUM_AVENANT", required = true)
  protected String numavenant;

  /**
   * Obtient la valeur de la propriété typedestinataire.
   *
   * @return possible object is {@link CodeDestinataire }
   */
  public CodeDestinataire getTYPEDESTINATAIRE() {
    return typedestinataire;
  }

  /**
   * Définit la valeur de la propriété typedestinataire.
   *
   * @param value allowed object is {@link CodeDestinataire }
   */
  public void setTYPEDESTINATAIRE(CodeDestinataire value) {
    this.typedestinataire = value;
  }

  /**
   * Obtient la valeur de la propriété numavenant.
   *
   * @return possible object is {@link String }
   */
  public String getNUMAVENANT() {
    return numavenant;
  }

  /**
   * Définit la valeur de la propriété numavenant.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMAVENANT(String value) {
    this.numavenant = value;
  }
}
