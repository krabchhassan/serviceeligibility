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
 * &lt;p&gt;Classe Java pour infoCritere complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoCritere"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="CENTRE_GESTION"
 * type="{http://www.almerys.com/NormeV3}infoCentreGestion" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="REPRESENTANT_COMMERCIAL"
 * type="{http://www.almerys.com/NormeV3}infoCourtier" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ENTREPRISE" type="{http://www.almerys.com/NormeV3}infoEntreprise"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="GESTIONNAIRE"
 * type="{http://www.almerys.com/NormeV3}infoGestionnaire" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoCritere",
    propOrder = {"centregestions", "representantcommercials", "entreprises", "gestionnaire"})
public class InfoCritere implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "CENTRE_GESTION")
  protected List<InfoCentreGestion> centregestions;

  @XmlElement(name = "REPRESENTANT_COMMERCIAL")
  protected List<InfoCourtier> representantcommercials;

  @XmlElement(name = "ENTREPRISE")
  protected List<InfoEntreprise> entreprises;

  @XmlElement(name = "GESTIONNAIRE")
  protected InfoGestionnaire gestionnaire;

  /**
   * Gets the value of the centregestions property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the centregestions
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getCENTREGESTIONS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoCentreGestion
   * }
   */
  public List<InfoCentreGestion> getCENTREGESTIONS() {
    if (centregestions == null) {
      centregestions = new ArrayList<InfoCentreGestion>();
    }
    return this.centregestions;
  }

  /**
   * Gets the value of the representantcommercials property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
   * representantcommercials property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getREPRESENTANTCOMMERCIALS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoCourtier }
   */
  public List<InfoCourtier> getREPRESENTANTCOMMERCIALS() {
    if (representantcommercials == null) {
      representantcommercials = new ArrayList<InfoCourtier>();
    }
    return this.representantcommercials;
  }

  /**
   * Gets the value of the entreprises property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the entreprises
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getENTREPRISES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoEntreprise }
   */
  public List<InfoEntreprise> getENTREPRISES() {
    if (entreprises == null) {
      entreprises = new ArrayList<InfoEntreprise>();
    }
    return this.entreprises;
  }

  /**
   * Obtient la valeur de la propriété gestionnaire.
   *
   * @return possible object is {@link InfoGestionnaire }
   */
  public InfoGestionnaire getGESTIONNAIRE() {
    return gestionnaire;
  }

  /**
   * Définit la valeur de la propriété gestionnaire.
   *
   * @param value allowed object is {@link InfoGestionnaire }
   */
  public void setGESTIONNAIRE(InfoGestionnaire value) {
    this.gestionnaire = value;
  }
}
