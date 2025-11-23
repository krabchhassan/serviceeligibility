package com.cegedimassurances.norme.tarification;

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
 * Classe Java pour type_tarification_beneficiaire complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_tarification_beneficiaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_beneficiaire"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="age"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="sexe"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
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
    name = "type_tarification_beneficiaire",
    propOrder = {"idBeneficiaire", "age", "sexe"})
public class TypeTarificationBeneficiaire implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_beneficiaire", required = true)
  @NotNull
  @Size(max = 30)
  protected String idBeneficiaire;

  @NotNull protected int age;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 10)
  protected String sexe;

  /**
   * Obtient la valeur de la propriété idBeneficiaire.
   *
   * @return possible object is {@link String }
   */
  public String getIdBeneficiaire() {
    return idBeneficiaire;
  }

  /**
   * Définit la valeur de la propriété idBeneficiaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdBeneficiaire(String value) {
    this.idBeneficiaire = value;
  }

  /** Obtient la valeur de la propriété age. */
  public int getAge() {
    return age;
  }

  /** Définit la valeur de la propriété age. */
  public void setAge(int value) {
    this.age = value;
  }

  /**
   * Obtient la valeur de la propriété sexe.
   *
   * @return possible object is {@link String }
   */
  public String getSexe() {
    return sexe;
  }

  /**
   * Définit la valeur de la propriété sexe.
   *
   * @param value allowed object is {@link String }
   */
  public void setSexe(String value) {
    this.sexe = value;
  }

  /**
   * Generates a String representation of the contents of this type. This is an extension method,
   * produced by the 'ts' xjc plugin
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
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
    if (draftCopy instanceof TypeTarificationBeneficiaire) {
      final TypeTarificationBeneficiaire copy = ((TypeTarificationBeneficiaire) draftCopy);
      if (this.idBeneficiaire != null) {
        String sourceIdBeneficiaire;
        sourceIdBeneficiaire = this.getIdBeneficiaire();
        String copyIdBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idBeneficiaire", sourceIdBeneficiaire),
                    sourceIdBeneficiaire));
        copy.setIdBeneficiaire(copyIdBeneficiaire);
      } else {
        copy.idBeneficiaire = null;
      }
      int sourceAge;
      sourceAge = (true ? this.getAge() : 0);
      int copyAge = strategy.copy(LocatorUtils.property(locator, "age", sourceAge), sourceAge);
      copy.setAge(copyAge);
      if (this.sexe != null) {
        String sourceSexe;
        sourceSexe = this.getSexe();
        String copySexe =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "sexe", sourceSexe), sourceSexe));
        copy.setSexe(copySexe);
      } else {
        copy.sexe = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeTarificationBeneficiaire();
  }
}
