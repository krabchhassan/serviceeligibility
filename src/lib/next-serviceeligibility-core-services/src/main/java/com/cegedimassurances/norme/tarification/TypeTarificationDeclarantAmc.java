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
 * Classe Java pour type_tarification_declarant_amc complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_tarification_declarant_amc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nom"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroRNM"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroPrefectoral" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
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
    name = "type_tarification_declarant_amc",
    propOrder = {"nom", "numeroRNM", "numeroPrefectoral"})
public class TypeTarificationDeclarantAmc implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 30)
  protected String nom;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 10)
  protected String numeroRNM;

  @Size(max = 8)
  protected String numeroPrefectoral;

  /**
   * Obtient la valeur de la propriété nom.
   *
   * @return possible object is {@link String }
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit la valeur de la propriété nom.
   *
   * @param value allowed object is {@link String }
   */
  public void setNom(String value) {
    this.nom = value;
  }

  /**
   * Obtient la valeur de la propriété numeroRNM.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroRNM() {
    return numeroRNM;
  }

  /**
   * Définit la valeur de la propriété numeroRNM.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroRNM(String value) {
    this.numeroRNM = value;
  }

  /**
   * Obtient la valeur de la propriété numeroPrefectoral.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroPrefectoral() {
    return numeroPrefectoral;
  }

  /**
   * Définit la valeur de la propriété numeroPrefectoral.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroPrefectoral(String value) {
    this.numeroPrefectoral = value;
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
    if (draftCopy instanceof TypeTarificationDeclarantAmc) {
      final TypeTarificationDeclarantAmc copy = ((TypeTarificationDeclarantAmc) draftCopy);
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
      }
      if (this.numeroRNM != null) {
        String sourceNumeroRNM;
        sourceNumeroRNM = this.getNumeroRNM();
        String copyNumeroRNM =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroRNM", sourceNumeroRNM), sourceNumeroRNM));
        copy.setNumeroRNM(copyNumeroRNM);
      } else {
        copy.numeroRNM = null;
      }
      if (this.numeroPrefectoral != null) {
        String sourceNumeroPrefectoral;
        sourceNumeroPrefectoral = this.getNumeroPrefectoral();
        String copyNumeroPrefectoral =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroPrefectoral", sourceNumeroPrefectoral),
                    sourceNumeroPrefectoral));
        copy.setNumeroPrefectoral(copyNumeroPrefectoral);
      } else {
        copy.numeroPrefectoral = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeTarificationDeclarantAmc();
  }
}
