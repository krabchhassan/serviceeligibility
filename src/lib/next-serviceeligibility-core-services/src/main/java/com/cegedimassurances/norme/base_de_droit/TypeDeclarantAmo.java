package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
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
 * Classe Java pour type_declarant_amo complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_declarant_amo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://norme.cegedimassurances.com/base_de_droit}type_declarant"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="regime" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="caisse" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="centre" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_declarant_amo",
    propOrder = {"regime", "caisse", "centre"})
public class TypeDeclarantAmo extends TypeDeclarant implements Serializable, Cloneable, CopyTo {

  @Size(max = 2)
  protected String regime;

  @Size(max = 3)
  protected String caisse;

  @Size(max = 4)
  protected String centre;

  /**
   * Obtient la valeur de la propriété regime.
   *
   * @return possible object is {@link String }
   */
  public String getRegime() {
    return regime;
  }

  /**
   * Définit la valeur de la propriété regime.
   *
   * @param value allowed object is {@link String }
   */
  public void setRegime(String value) {
    this.regime = value;
  }

  /**
   * Obtient la valeur de la propriété caisse.
   *
   * @return possible object is {@link String }
   */
  public String getCaisse() {
    return caisse;
  }

  /**
   * Définit la valeur de la propriété caisse.
   *
   * @param value allowed object is {@link String }
   */
  public void setCaisse(String value) {
    this.caisse = value;
  }

  /**
   * Obtient la valeur de la propriété centre.
   *
   * @return possible object is {@link String }
   */
  public String getCentre() {
    return centre;
  }

  /**
   * Définit la valeur de la propriété centre.
   *
   * @param value allowed object is {@link String }
   */
  public void setCentre(String value) {
    this.centre = value;
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
    super.copyTo(locator, draftCopy, strategy);
    if (draftCopy instanceof TypeDeclarantAmo) {
      final TypeDeclarantAmo copy = ((TypeDeclarantAmo) draftCopy);
      if (this.regime != null) {
        String sourceRegime;
        sourceRegime = this.getRegime();
        String copyRegime =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regime", sourceRegime), sourceRegime));
        copy.setRegime(copyRegime);
      } else {
        copy.regime = null;
      }
      if (this.caisse != null) {
        String sourceCaisse;
        sourceCaisse = this.getCaisse();
        String copyCaisse =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "caisse", sourceCaisse), sourceCaisse));
        copy.setCaisse(copyCaisse);
      } else {
        copy.caisse = null;
      }
      if (this.centre != null) {
        String sourceCentre;
        sourceCentre = this.getCentre();
        String copyCentre =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "centre", sourceCentre), sourceCentre));
        copy.setCentre(copyCentre);
      } else {
        copy.centre = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDeclarantAmo();
  }
}
