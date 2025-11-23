//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateTimeAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.PositiveIntegerAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * &lt;p&gt;Classe Java pour infoEntete complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoEntete"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NUM_FICHIER"
 * type="{http://www.almerys.com/NormeV3}positiveInteger-6"/&amp;gt; &amp;lt;element
 * name="DATE_CREATION" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&amp;gt; &amp;lt;element
 * name="VERSION_NORME" type="{http://www.almerys.com/NormeV3}string-1-10"/&amp;gt; &amp;lt;element
 * name="NUM_OS_EMETTEUR" type="{http://www.almerys.com/NormeV3}positiveInteger-14"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoEntete",
    propOrder = {"numfichier", "datecreation", "versionnorme", "numosemetteur"})
public class InfoEntete implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NUM_FICHIER", required = true, type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer numfichier;

  @XmlElement(name = "DATE_CREATION", required = true, type = String.class)
  @XmlJavaTypeAdapter(DateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  protected Date datecreation;

  @XmlElement(name = "VERSION_NORME", required = true)
  protected String versionnorme;

  @XmlElement(name = "NUM_OS_EMETTEUR", required = true, type = String.class)
  @XmlJavaTypeAdapter(PositiveIntegerAdapter.class)
  @XmlSchemaType(name = "positiveInteger")
  protected Integer numosemetteur;

  /**
   * Obtient la valeur de la propriété numfichier.
   *
   * @return possible object is {@link String }
   */
  public Integer getNUMFICHIER() {
    return numfichier;
  }

  /**
   * Définit la valeur de la propriété numfichier.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMFICHIER(Integer value) {
    this.numfichier = value;
  }

  /**
   * Obtient la valeur de la propriété datecreation.
   *
   * @return possible object is {@link String }
   */
  public Date getDATECREATION() {
    return datecreation;
  }

  /**
   * Définit la valeur de la propriété datecreation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATECREATION(Date value) {
    this.datecreation = value;
  }

  /**
   * Obtient la valeur de la propriété versionnorme.
   *
   * @return possible object is {@link String }
   */
  public String getVERSIONNORME() {
    return versionnorme;
  }

  /**
   * Définit la valeur de la propriété versionnorme.
   *
   * @param value allowed object is {@link String }
   */
  public void setVERSIONNORME(String value) {
    this.versionnorme = value;
  }

  /**
   * Obtient la valeur de la propriété numosemetteur.
   *
   * @return possible object is {@link String }
   */
  public Integer getNUMOSEMETTEUR() {
    return numosemetteur;
  }

  /**
   * Définit la valeur de la propriété numosemetteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMOSEMETTEUR(Integer value) {
    this.numosemetteur = value;
  }
}
