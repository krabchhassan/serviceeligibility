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
 * &lt;p&gt;Classe Java pour infoCourtier complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoCourtier"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_COURTIER"
 * type="{http://www.almerys.com/NormeV3}string-1-15"/&amp;gt; &amp;lt;element name="NOM_COURTIER"
 * type="{http://www.almerys.com/NormeV3}string-1-80"/&amp;gt; &amp;lt;element
 * name="PRENOM_COURTIER" type="{http://www.almerys.com/NormeV3}string-1-40" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ADRESSE_COURTIER"
 * type="{http://www.almerys.com/NormeV3}infoAdresse"/&amp;gt; &amp;lt;element name="IDENTITE_WEB"
 * type="{http://www.almerys.com/NormeV3}infoWeb" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="JOIGNABILITE" type="{http://www.almerys.com/NormeV3}infoJoignabilite" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoCourtier",
    propOrder = {
      "refcourtier",
      "nomcourtier",
      "prenomcourtier",
      "adressecourtier",
      "identiteweb",
      "joignabilites"
    })
public class InfoCourtier implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_COURTIER", required = true)
  protected String refcourtier;

  @XmlElement(name = "NOM_COURTIER", required = true)
  protected String nomcourtier;

  @XmlElement(name = "PRENOM_COURTIER")
  protected String prenomcourtier;

  @XmlElement(name = "ADRESSE_COURTIER", required = true)
  protected InfoAdresse adressecourtier;

  @XmlElement(name = "IDENTITE_WEB")
  protected InfoWeb identiteweb;

  @XmlElement(name = "JOIGNABILITE")
  protected List<InfoJoignabilite> joignabilites;

  /**
   * Obtient la valeur de la propriété refcourtier.
   *
   * @return possible object is {@link String }
   */
  public String getREFCOURTIER() {
    return refcourtier;
  }

  /**
   * Définit la valeur de la propriété refcourtier.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFCOURTIER(String value) {
    this.refcourtier = value;
  }

  /**
   * Obtient la valeur de la propriété nomcourtier.
   *
   * @return possible object is {@link String }
   */
  public String getNOMCOURTIER() {
    return nomcourtier;
  }

  /**
   * Définit la valeur de la propriété nomcourtier.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOMCOURTIER(String value) {
    this.nomcourtier = value;
  }

  /**
   * Obtient la valeur de la propriété prenomcourtier.
   *
   * @return possible object is {@link String }
   */
  public String getPRENOMCOURTIER() {
    return prenomcourtier;
  }

  /**
   * Définit la valeur de la propriété prenomcourtier.
   *
   * @param value allowed object is {@link String }
   */
  public void setPRENOMCOURTIER(String value) {
    this.prenomcourtier = value;
  }

  /**
   * Obtient la valeur de la propriété adressecourtier.
   *
   * @return possible object is {@link InfoAdresse }
   */
  public InfoAdresse getADRESSECOURTIER() {
    return adressecourtier;
  }

  /**
   * Définit la valeur de la propriété adressecourtier.
   *
   * @param value allowed object is {@link InfoAdresse }
   */
  public void setADRESSECOURTIER(InfoAdresse value) {
    this.adressecourtier = value;
  }

  /**
   * Obtient la valeur de la propriété identiteweb.
   *
   * @return possible object is {@link InfoWeb }
   */
  public InfoWeb getIDENTITEWEB() {
    return identiteweb;
  }

  /**
   * Définit la valeur de la propriété identiteweb.
   *
   * @param value allowed object is {@link InfoWeb }
   */
  public void setIDENTITEWEB(InfoWeb value) {
    this.identiteweb = value;
  }

  /**
   * Gets the value of the joignabilites property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the joignabilites
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getJOIGNABILITES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoJoignabilite }
   */
  public List<InfoJoignabilite> getJOIGNABILITES() {
    if (joignabilites == null) {
      joignabilites = new ArrayList<InfoJoignabilite>();
    }
    return this.joignabilites;
  }
}
