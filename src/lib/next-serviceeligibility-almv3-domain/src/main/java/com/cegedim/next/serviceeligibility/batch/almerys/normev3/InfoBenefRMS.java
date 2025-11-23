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
 * &lt;p&gt;Classe Java pour infoBenefRMS complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoBenefRMS"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="POSITION"
 * type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt; &amp;lt;element name="SOUSCRIPTEUR"
 * type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;element name="INDIVIDU_OS"
 * type="{http://www.almerys.com/NormeV3}infoIndividuOsRMS"/&amp;gt; &amp;lt;element
 * name="ADRESSE_BENEF" type="{http://www.almerys.com/NormeV3}infoAdresseParticulier"/&amp;gt;
 * &amp;lt;element name="CODE_MOUVEMENT" type="{http://www.almerys.com/NormeV3}codeMVT"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoBenefRMS",
    propOrder = {"position", "souscripteur", "individuos", "adressebenef", "codemouvement"})
public class InfoBenefRMS implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "POSITION")
  protected int position;

  @XmlElement(name = "SOUSCRIPTEUR", required = true, type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean souscripteur;

  @XmlElement(name = "INDIVIDU_OS", required = true)
  protected InfoIndividuOsRMS individuos;

  @XmlElement(name = "ADRESSE_BENEF", required = true)
  protected InfoAdresseParticulier adressebenef;

  @XmlElement(name = "CODE_MOUVEMENT", required = true)
  @XmlSchemaType(name = "string")
  protected CodeMVT codemouvement;

  /** Obtient la valeur de la propriété position. */
  public int getPOSITION() {
    return position;
  }

  /** Définit la valeur de la propriété position. */
  public void setPOSITION(int value) {
    this.position = value;
  }

  /**
   * Obtient la valeur de la propriété souscripteur.
   *
   * @return possible object is {@link String }
   */
  public Boolean isSOUSCRIPTEUR() {
    return souscripteur;
  }

  /**
   * Définit la valeur de la propriété souscripteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setSOUSCRIPTEUR(Boolean value) {
    this.souscripteur = value;
  }

  /**
   * Obtient la valeur de la propriété individuos.
   *
   * @return possible object is {@link InfoIndividuOsRMS }
   */
  public InfoIndividuOsRMS getINDIVIDUOS() {
    return individuos;
  }

  /**
   * Définit la valeur de la propriété individuos.
   *
   * @param value allowed object is {@link InfoIndividuOsRMS }
   */
  public void setINDIVIDUOS(InfoIndividuOsRMS value) {
    this.individuos = value;
  }

  /**
   * Obtient la valeur de la propriété adressebenef.
   *
   * @return possible object is {@link InfoAdresseParticulier }
   */
  public InfoAdresseParticulier getADRESSEBENEF() {
    return adressebenef;
  }

  /**
   * Définit la valeur de la propriété adressebenef.
   *
   * @param value allowed object is {@link InfoAdresseParticulier }
   */
  public void setADRESSEBENEF(InfoAdresseParticulier value) {
    this.adressebenef = value;
  }

  /**
   * Obtient la valeur de la propriété codemouvement.
   *
   * @return possible object is {@link CodeMVT }
   */
  public CodeMVT getCODEMOUVEMENT() {
    return codemouvement;
  }

  /**
   * Définit la valeur de la propriété codemouvement.
   *
   * @param value allowed object is {@link CodeMVT }
   */
  public void setCODEMOUVEMENT(CodeMVT value) {
    this.codemouvement = value;
  }
}
