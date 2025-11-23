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
import jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * &lt;p&gt;Classe Java pour infoAdresseAgregeeParticulierChiffree complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoAdresseAgregeeParticulierChiffree"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="LIGNE1" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt; &amp;lt;element
 * name="LIGNE2" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt; &amp;lt;element
 * name="LIGNE3" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt; &amp;lt;element
 * name="LIGNE4" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt; &amp;lt;element
 * name="LIGNE5" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt; &amp;lt;element
 * name="LIGNE6" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt; &amp;lt;element
 * name="LIGNE7" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoAdresseAgregeeParticulierChiffree",
    propOrder = {"ligne1", "ligne2", "ligne3", "ligne4", "ligne5", "ligne6", "ligne7"})
public class InfoAdresseAgregeeParticulierChiffree implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "LIGNE1", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne1;

  @XmlElement(name = "LIGNE2", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne2;

  @XmlElement(name = "LIGNE3", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne3;

  @XmlElement(name = "LIGNE4", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne4;

  @XmlElement(name = "LIGNE5", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne5;

  @XmlElement(name = "LIGNE6", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne6;

  @XmlElement(name = "LIGNE7", required = true, type = String.class)
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  @XmlSchemaType(name = "hexBinary")
  protected byte[] ligne7;

  /**
   * Obtient la valeur de la propriété ligne1.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE1() {
    return ligne1;
  }

  /**
   * Définit la valeur de la propriété ligne1.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE1(byte[] value) {
    this.ligne1 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne2.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE2() {
    return ligne2;
  }

  /**
   * Définit la valeur de la propriété ligne2.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE2(byte[] value) {
    this.ligne2 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne3.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE3() {
    return ligne3;
  }

  /**
   * Définit la valeur de la propriété ligne3.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE3(byte[] value) {
    this.ligne3 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne4.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE4() {
    return ligne4;
  }

  /**
   * Définit la valeur de la propriété ligne4.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE4(byte[] value) {
    this.ligne4 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne5.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE5() {
    return ligne5;
  }

  /**
   * Définit la valeur de la propriété ligne5.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE5(byte[] value) {
    this.ligne5 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne6.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE6() {
    return ligne6;
  }

  /**
   * Définit la valeur de la propriété ligne6.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE6(byte[] value) {
    this.ligne6 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne7.
   *
   * @return possible object is {@link String }
   */
  public byte[] getLIGNE7() {
    return ligne7;
  }

  /**
   * Définit la valeur de la propriété ligne7.
   *
   * @param value allowed object is {@link String }
   */
  public void setLIGNE7(byte[] value) {
    this.ligne7 = value;
  }
}
