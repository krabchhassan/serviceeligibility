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
import java.util.ArrayList;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoMembreContratTP complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoMembreContratTP"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="POSITION" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt; &amp;lt;element
 * name="SOUSCRIPTEUR" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;element
 * name="JOIGNABILITE" type="{http://www.almerys.com/NormeV3}infoJoignabilite"
 * maxOccurs="unbounded"/&amp;gt; &amp;lt;element name="CODE_MOUVEMENT"
 * type="{http://www.almerys.com/NormeV3}codeMVT"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoMembreContratTP",
    propOrder = {"position", "souscripteur", "joignabilites", "codemouvement"})
public class InfoMembreContratTP implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "POSITION")
  protected int position;

  @XmlElement(name = "SOUSCRIPTEUR", required = true, type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean souscripteur;

  @XmlElement(name = "JOIGNABILITE", required = true)
  protected List<InfoJoignabilite> joignabilites;

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
