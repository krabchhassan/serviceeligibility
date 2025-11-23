//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.PositiveIntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoMutuelle complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoMutuelle"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NUM_MUTUELLE"
 * type="{http://www.almerys.com/NormeV3}positiveInteger-8"/&amp;gt; &amp;lt;element
 * name="CODE_GARANTIE" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element
 * name="DATE_DEB_DROITS_MUT" type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element
 * name="DATE_FIN_DROITS_MUT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DUREE_PROLONGATION_MUT" type="{http://www.almerys.com/NormeV3}string-1-50"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoMutuelle",
    propOrder = {
      "nummutuelle",
      "codegarantie",
      "datedebdroitsmut",
      "datefindroitsmut",
      "dureeprolongationmut"
    })
public class InfoMutuelle implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NUM_MUTUELLE", required = true, type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer nummutuelle;

  @XmlElement(name = "CODE_GARANTIE", required = true)
  protected String codegarantie;

  @XmlElement(name = "DATE_DEB_DROITS_MUT", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebdroitsmut;

  @XmlElement(name = "DATE_FIN_DROITS_MUT")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefindroitsmut;

  @XmlElement(name = "DUREE_PROLONGATION_MUT")
  protected String dureeprolongationmut;

  /**
   * Obtient la valeur de la propriété nummutuelle.
   *
   * @return possible object is {@link String }
   */
  public Integer getNUMMUTUELLE() {
    return nummutuelle;
  }

  /**
   * Définit la valeur de la propriété nummutuelle.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMMUTUELLE(Integer value) {
    this.nummutuelle = value;
  }

  /**
   * Obtient la valeur de la propriété codegarantie.
   *
   * @return possible object is {@link String }
   */
  public String getCODEGARANTIE() {
    return codegarantie;
  }

  /**
   * Définit la valeur de la propriété codegarantie.
   *
   * @param value allowed object is {@link String }
   */
  public void setCODEGARANTIE(String value) {
    this.codegarantie = value;
  }

  /**
   * Obtient la valeur de la propriété datedebdroitsmut.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBDROITSMUT() {
    return datedebdroitsmut;
  }

  /**
   * Définit la valeur de la propriété datedebdroitsmut.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBDROITSMUT(String value) {
    this.datedebdroitsmut = value;
  }

  /**
   * Obtient la valeur de la propriété datefindroitsmut.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFINDROITSMUT() {
    return datefindroitsmut;
  }

  /**
   * Définit la valeur de la propriété datefindroitsmut.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFINDROITSMUT(String value) {
    this.datefindroitsmut = value;
  }

  /**
   * Obtient la valeur de la propriété dureeprolongationmut.
   *
   * @return possible object is {@link String }
   */
  public String getDUREEPROLONGATIONMUT() {
    return dureeprolongationmut;
  }

  /**
   * Définit la valeur de la propriété dureeprolongationmut.
   *
   * @param value allowed object is {@link String }
   */
  public void setDUREEPROLONGATIONMUT(String value) {
    this.dureeprolongationmut = value;
  }
}
