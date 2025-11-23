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
 * Formule de prestations
 *
 * <p>Classe Java pour type_parametre_formule complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_parametre_formule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroParametres"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="valeurParametres"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="6"/&gt;
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
    name = "type_parametre_formule",
    propOrder = {"numeroParametres", "valeurParametres"})
public class TypeParametreFormule implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 3)
  protected String numeroParametres;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 6)
  protected String valeurParametres;

  /**
   * Obtient la valeur de la propriété numeroParametres.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroParametres() {
    return numeroParametres;
  }

  /**
   * Définit la valeur de la propriété numeroParametres.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroParametres(String value) {
    this.numeroParametres = value;
  }

  /**
   * Obtient la valeur de la propriété valeurParametres.
   *
   * @return possible object is {@link String }
   */
  public String getValeurParametres() {
    return valeurParametres;
  }

  /**
   * Définit la valeur de la propriété valeurParametres.
   *
   * @param value allowed object is {@link String }
   */
  public void setValeurParametres(String value) {
    this.valeurParametres = value;
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
    if (draftCopy instanceof TypeParametreFormule) {
      final TypeParametreFormule copy = ((TypeParametreFormule) draftCopy);
      if (this.numeroParametres != null) {
        String sourceNumeroParametres;
        sourceNumeroParametres = this.getNumeroParametres();
        String copyNumeroParametres =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroParametres", sourceNumeroParametres),
                    sourceNumeroParametres));
        copy.setNumeroParametres(copyNumeroParametres);
      } else {
        copy.numeroParametres = null;
      }
      if (this.valeurParametres != null) {
        String sourceValeurParametres;
        sourceValeurParametres = this.getValeurParametres();
        String copyValeurParametres =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "valeurParametres", sourceValeurParametres),
                    sourceValeurParametres));
        copy.setValeurParametres(copyValeurParametres);
      } else {
        copy.valeurParametres = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeParametreFormule();
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
