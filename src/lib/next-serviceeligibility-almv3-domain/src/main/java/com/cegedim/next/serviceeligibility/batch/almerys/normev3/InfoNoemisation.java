//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoNoemisation complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoNoemisation"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;choice&amp;gt; &amp;lt;element name="DMD_NOEMISATION"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="DATE_DEBUT" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element
 * name="DATE_FIN" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element name="REFUS_NOEMISATION"
 * type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="TYPE_CONTRAT" type="{http://www.almerys.com/NormeV3}typeContrat"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="A_NOEMISER"
 * type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NOEMISE" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_NOEMISATION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_FIN_NOEMISATION"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoNoemisation",
    propOrder = {
      "typecontrat",
      "anoemiser",
      "noemise",
      "datedebutnoemisation",
      "datefinnoemisation",
      "refusnoemisation",
      "dmdnoemisation"
    })
public class InfoNoemisation implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "TYPE_CONTRAT")
  protected String typecontrat;

  @XmlElement(name = "A_NOEMISER", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean anoemiser;

  @XmlElement(name = "NOEMISE", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean noemise;

  @XmlElement(name = "DATE_DEBUT_NOEMISATION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutnoemisation;

  @XmlElement(name = "DATE_FIN_NOEMISATION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinnoemisation;

  @XmlElement(name = "REFUS_NOEMISATION", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean refusnoemisation;

  @XmlElement(name = "DMD_NOEMISATION")
  protected InfoNoemisation.DMDNOEMISATION dmdnoemisation;

  /**
   * Obtient la valeur de la propriété typecontrat.
   *
   * @return possible object is {@link String }
   */
  public String getTYPECONTRAT() {
    return typecontrat;
  }

  /**
   * Définit la valeur de la propriété typecontrat.
   *
   * @param value allowed object is {@link String }
   */
  public void setTYPECONTRAT(String value) {
    this.typecontrat = value;
  }

  /**
   * Obtient la valeur de la propriété anoemiser.
   *
   * @return possible object is {@link String }
   */
  public Boolean isANOEMISER() {
    return anoemiser;
  }

  /**
   * Définit la valeur de la propriété anoemiser.
   *
   * @param value allowed object is {@link String }
   */
  public void setANOEMISER(Boolean value) {
    this.anoemiser = value;
  }

  /**
   * Obtient la valeur de la propriété noemise.
   *
   * @return possible object is {@link String }
   */
  public Boolean isNOEMISE() {
    return noemise;
  }

  /**
   * Définit la valeur de la propriété noemise.
   *
   * @param value allowed object is {@link String }
   */
  public void setNOEMISE(Boolean value) {
    this.noemise = value;
  }

  /**
   * Obtient la valeur de la propriété datedebutnoemisation.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBUTNOEMISATION() {
    return datedebutnoemisation;
  }

  /**
   * Définit la valeur de la propriété datedebutnoemisation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBUTNOEMISATION(String value) {
    this.datedebutnoemisation = value;
  }

  /**
   * Obtient la valeur de la propriété datefinnoemisation.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFINNOEMISATION() {
    return datefinnoemisation;
  }

  /**
   * Définit la valeur de la propriété datefinnoemisation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFINNOEMISATION(String value) {
    this.datefinnoemisation = value;
  }

  /**
   * Obtient la valeur de la propriété refusnoemisation.
   *
   * @return possible object is {@link String }
   */
  public Boolean isREFUSNOEMISATION() {
    return refusnoemisation;
  }

  /**
   * Définit la valeur de la propriété refusnoemisation.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFUSNOEMISATION(Boolean value) {
    this.refusnoemisation = value;
  }

  /**
   * Obtient la valeur de la propriété dmdnoemisation.
   *
   * @return possible object is {@link InfoNoemisation.DMDNOEMISATION }
   */
  public InfoNoemisation.DMDNOEMISATION getDMDNOEMISATION() {
    return dmdnoemisation;
  }

  /**
   * Définit la valeur de la propriété dmdnoemisation.
   *
   * @param value allowed object is {@link InfoNoemisation.DMDNOEMISATION }
   */
  public void setDMDNOEMISATION(InfoNoemisation.DMDNOEMISATION value) {
    this.dmdnoemisation = value;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="DATE_DEBUT" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt;
   * &amp;lt;element name="DATE_FIN" type="{http://www.w3.org/2001/XMLSchema}date"
   * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
   * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"datedebut", "datefin"})
  public static class DMDNOEMISATION implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "DATE_DEBUT", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datedebut;

    @XmlElement(name = "DATE_FIN")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected String datefin;

    /**
     * Obtient la valeur de la propriété datedebut.
     *
     * @return possible object is {@link String }
     */
    public String getDATEDEBUT() {
      return datedebut;
    }

    /**
     * Définit la valeur de la propriété datedebut.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEDEBUT(String value) {
      this.datedebut = value;
    }

    /**
     * Obtient la valeur de la propriété datefin.
     *
     * @return possible object is {@link String }
     */
    public String getDATEFIN() {
      return datefin;
    }

    /**
     * Définit la valeur de la propriété datefin.
     *
     * @param value allowed object is {@link String }
     */
    public void setDATEFIN(String value) {
      this.datefin = value;
    }
  }
}
