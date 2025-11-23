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
 * &lt;p&gt;Classe Java pour infoEntreprise complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoEntreprise"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_ENTREPRISE"
 * type="{http://www.almerys.com/NormeV3}string-1-15"/&amp;gt; &amp;lt;element name="NOM_ENTREPRISE"
 * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NUM_CONTRAT_COLLECTIF" type="{http://www.almerys.com/NormeV3}string-1-30"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="SITE"
 * type="{http://www.almerys.com/NormeV3}infoSite" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoEntreprise",
    propOrder = {"refentreprise", "nomentreprise", "numcontratcollectives", "sites"})
public class InfoEntreprise implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_ENTREPRISE", required = true)
  protected String refentreprise;

  @XmlElement(name = "NOM_ENTREPRISE")
  protected String nomentreprise;

  @XmlElement(name = "NUM_CONTRAT_COLLECTIF")
  protected List<String> numcontratcollectives;

  @XmlElement(name = "SITE")
  protected List<InfoSite> sites;

  /**
   * Obtient la valeur de la propriété refentreprise.
   *
   * @return possible object is {@link String }
   */
  public String getREFENTREPRISE() {
    return refentreprise;
  }

  /**
   * Définit la valeur de la propriété refentreprise.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFENTREPRISE(String value) {
    this.refentreprise = value;
  }

  /**
   * Obtient la valeur de la propriété nomentreprise.
   *
   * @return possible object is {@link String }
   */
  public String getNOMENTREPRISE() {
    return nomentreprise;
  }

  /**
   * Définit la valeur de la propriété nomentreprise.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOMENTREPRISE(String value) {
    this.nomentreprise = value;
  }

  /**
   * Gets the value of the numcontratcollectives property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
   * numcontratcollectives property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getNUMCONTRATCOLLECTIVES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getNUMCONTRATCOLLECTIVES() {
    if (numcontratcollectives == null) {
      numcontratcollectives = new ArrayList<String>();
    }
    return this.numcontratcollectives;
  }

  /**
   * Gets the value of the sites property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the sites property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getSITES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoSite }
   */
  public List<InfoSite> getSITES() {
    if (sites == null) {
      sites = new ArrayList<InfoSite>();
    }
    return this.sites;
  }
}
