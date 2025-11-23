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
import java.util.ArrayList;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoPerimetre complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoPerimetre"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="CODE_PERIMETRE"
 * type="{http://www.almerys.com/NormeV3}string-1-10"/&amp;gt; &amp;lt;element
 * name="LIBELLE_PERIMETRE" type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt;
 * &amp;lt;element name="CRITERE_REGROUPEMENT" type="{http://www.almerys.com/NormeV3}infoCritere"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CONTRAT"
 * type="{http://www.almerys.com/NormeV3}infoContrat" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoPerimetre",
    propOrder = {"codeperimetre", "libelleperimetre", "critereregroupement", "contrats"})
public class InfoPerimetre implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "CODE_PERIMETRE", required = true)
  protected String codeperimetre;

  @XmlElement(name = "LIBELLE_PERIMETRE", required = true)
  protected String libelleperimetre;

  @XmlElement(name = "CRITERE_REGROUPEMENT")
  protected InfoCritere critereregroupement;

  @XmlElement(name = "CONTRAT")
  protected List<InfoContrat> contrats;

  /**
   * Obtient la valeur de la propriété codeperimetre.
   *
   * @return possible object is {@link String }
   */
  public String getCODEPERIMETRE() {
    return codeperimetre;
  }

  /**
   * Définit la valeur de la propriété codeperimetre.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEPERIMETRE(String value) {
    this.codeperimetre = value;
  }

  /**
   * Obtient la valeur de la propriété libelleperimetre.
   *
   * @return possible object is {@link String }
   */
  public String getLIBELLEPERIMETRE() {
    return libelleperimetre;
  }

  /**
   * Définit la valeur de la propriété libelleperimetre.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIBELLEPERIMETRE(String value) {
    this.libelleperimetre = value;
  }

  /**
   * Obtient la valeur de la propriété critereregroupement.
   *
   * @return possible object is {@link InfoCritere }
   */
  public InfoCritere getCRITEREREGROUPEMENT() {
    return critereregroupement;
  }

  /**
   * Définit la valeur de la propriété critereregroupement.
   *
   * @param value allowed object is {@link InfoCritere }
   */
  public void setCRITEREREGROUPEMENT(InfoCritere value) {
    this.critereregroupement = value;
  }

  /**
   * Gets the value of the contrats property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the contrats
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getCONTRATS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoContrat }
   */
  public List<InfoContrat> getCONTRATS() {
    if (contrats == null) {
      contrats = new ArrayList<InfoContrat>();
    }
    return this.contrats;
  }
}
