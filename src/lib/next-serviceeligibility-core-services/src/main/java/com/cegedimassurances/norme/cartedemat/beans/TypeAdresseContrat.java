package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Adresse contrat
 *
 * <p>Classe Java pour type_adresse_contrat complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_adresse_contrat"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="typeAdresse"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne3" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne4"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne5" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne6"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ligne7" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="codePostal" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="telephone" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="email" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_adresse_contrat",
    propOrder = {
      "typeAdresse",
      "ligne1",
      "ligne2",
      "ligne3",
      "ligne4",
      "ligne5",
      "ligne6",
      "ligne7",
      "codePostal",
      "telephone",
      "email"
    })
public class TypeAdresseContrat implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 2)
  protected String typeAdresse;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 50)
  protected String ligne1;

  @Size(min = 1, max = 50)
  protected String ligne2;

  @Size(min = 1, max = 50)
  protected String ligne3;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 50)
  protected String ligne4;

  @Size(min = 1, max = 50)
  protected String ligne5;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 50)
  protected String ligne6;

  @Size(min = 1, max = 50)
  protected String ligne7;

  @Size(min = 1, max = 10)
  protected String codePostal;

  @Size(min = 1, max = 50)
  protected String telephone;

  @Size(min = 1, max = 50)
  protected String email;

  /**
   * Obtient la valeur de la propriété typeAdresse.
   *
   * @return possible object is {@link String }
   */
  public String getTypeAdresse() {
    return typeAdresse;
  }

  /**
   * Définit la valeur de la propriété typeAdresse.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeAdresse(String value) {
    this.typeAdresse = value;
  }

  /**
   * Obtient la valeur de la propriété ligne1.
   *
   * @return possible object is {@link String }
   */
  public String getLigne1() {
    return ligne1;
  }

  /**
   * Définit la valeur de la propriété ligne1.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne1(String value) {
    this.ligne1 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne2.
   *
   * @return possible object is {@link String }
   */
  public String getLigne2() {
    return ligne2;
  }

  /**
   * Définit la valeur de la propriété ligne2.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne2(String value) {
    this.ligne2 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne3.
   *
   * @return possible object is {@link String }
   */
  public String getLigne3() {
    return ligne3;
  }

  /**
   * Définit la valeur de la propriété ligne3.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne3(String value) {
    this.ligne3 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne4.
   *
   * @return possible object is {@link String }
   */
  public String getLigne4() {
    return ligne4;
  }

  /**
   * Définit la valeur de la propriété ligne4.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne4(String value) {
    this.ligne4 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne5.
   *
   * @return possible object is {@link String }
   */
  public String getLigne5() {
    return ligne5;
  }

  /**
   * Définit la valeur de la propriété ligne5.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne5(String value) {
    this.ligne5 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne6.
   *
   * @return possible object is {@link String }
   */
  public String getLigne6() {
    return ligne6;
  }

  /**
   * Définit la valeur de la propriété ligne6.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne6(String value) {
    this.ligne6 = value;
  }

  /**
   * Obtient la valeur de la propriété ligne7.
   *
   * @return possible object is {@link String }
   */
  public String getLigne7() {
    return ligne7;
  }

  /**
   * Définit la valeur de la propriété ligne7.
   *
   * @param value allowed object is {@link String }
   */
  public void setLigne7(String value) {
    this.ligne7 = value;
  }

  /**
   * Obtient la valeur de la propriété codePostal.
   *
   * @return possible object is {@link String }
   */
  public String getCodePostal() {
    return codePostal;
  }

  /**
   * Définit la valeur de la propriété codePostal.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodePostal(String value) {
    this.codePostal = value;
  }

  /**
   * Obtient la valeur de la propriété telephone.
   *
   * @return possible object is {@link String }
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * Définit la valeur de la propriété telephone.
   *
   * @param value allowed object is {@link String }
   */
  public void setTelephone(String value) {
    this.telephone = value;
  }

  /**
   * Obtient la valeur de la propriété email.
   *
   * @return possible object is {@link String }
   */
  public String getEmail() {
    return email;
  }

  /**
   * Définit la valeur de la propriété email.
   *
   * @param value allowed object is {@link String }
   */
  public void setEmail(String value) {
    this.email = value;
  }

  public Object clone() {
    return copyTo(createNewInstance());
  }

  public Object copyTo(Object target) {
    final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
    return copyTo(null, target, strategy);
  }

  public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
    final Object draftCopy = ((target == null) ? createNewInstance() : target);
    if (draftCopy instanceof TypeAdresseContrat) {
      final TypeAdresseContrat copy = ((TypeAdresseContrat) draftCopy);
      if (this.typeAdresse != null) {
        String sourceTypeAdresse;
        sourceTypeAdresse = this.getTypeAdresse();
        String copyTypeAdresse =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "typeAdresse", sourceTypeAdresse),
                    sourceTypeAdresse));
        copy.setTypeAdresse(copyTypeAdresse);
      } else {
        copy.typeAdresse = null;
      }
      if (this.ligne1 != null) {
        String sourceLigne1;
        sourceLigne1 = this.getLigne1();
        String copyLigne1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne1", sourceLigne1), sourceLigne1));
        copy.setLigne1(copyLigne1);
      } else {
        copy.ligne1 = null;
      }
      if (this.ligne2 != null) {
        String sourceLigne2;
        sourceLigne2 = this.getLigne2();
        String copyLigne2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne2", sourceLigne2), sourceLigne2));
        copy.setLigne2(copyLigne2);
      } else {
        copy.ligne2 = null;
      }
      if (this.ligne3 != null) {
        String sourceLigne3;
        sourceLigne3 = this.getLigne3();
        String copyLigne3 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne3", sourceLigne3), sourceLigne3));
        copy.setLigne3(copyLigne3);
      } else {
        copy.ligne3 = null;
      }
      if (this.ligne4 != null) {
        String sourceLigne4;
        sourceLigne4 = this.getLigne4();
        String copyLigne4 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne4", sourceLigne4), sourceLigne4));
        copy.setLigne4(copyLigne4);
      } else {
        copy.ligne4 = null;
      }
      if (this.ligne5 != null) {
        String sourceLigne5;
        sourceLigne5 = this.getLigne5();
        String copyLigne5 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne5", sourceLigne5), sourceLigne5));
        copy.setLigne5(copyLigne5);
      } else {
        copy.ligne5 = null;
      }
      if (this.ligne6 != null) {
        String sourceLigne6;
        sourceLigne6 = this.getLigne6();
        String copyLigne6 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne6", sourceLigne6), sourceLigne6));
        copy.setLigne6(copyLigne6);
      } else {
        copy.ligne6 = null;
      }
      if (this.ligne7 != null) {
        String sourceLigne7;
        sourceLigne7 = this.getLigne7();
        String copyLigne7 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne7", sourceLigne7), sourceLigne7));
        copy.setLigne7(copyLigne7);
      } else {
        copy.ligne7 = null;
      }
      if (this.codePostal != null) {
        String sourceCodePostal;
        sourceCodePostal = this.getCodePostal();
        String copyCodePostal =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codePostal", sourceCodePostal),
                    sourceCodePostal));
        copy.setCodePostal(copyCodePostal);
      } else {
        copy.codePostal = null;
      }
      if (this.telephone != null) {
        String sourceTelephone;
        sourceTelephone = this.getTelephone();
        String copyTelephone =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "telephone", sourceTelephone), sourceTelephone));
        copy.setTelephone(copyTelephone);
      } else {
        copy.telephone = null;
      }
      if (this.email != null) {
        String sourceEmail;
        sourceEmail = this.getEmail();
        String copyEmail =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "email", sourceEmail), sourceEmail));
        copy.setEmail(copyEmail);
      } else {
        copy.email = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeAdresseContrat();
  }

  /**
   * Generates a String representation of the contents of this type. This is an extension method,
   * produced by the 'ts' xjc plugin
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
  }
}
