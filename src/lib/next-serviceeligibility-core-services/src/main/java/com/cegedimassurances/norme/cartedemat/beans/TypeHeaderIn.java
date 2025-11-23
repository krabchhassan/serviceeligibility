package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * entete de la requete
 *
 * <p>Classe Java pour type_header_in complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_header_in"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_technique_in" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_header_tech_in"/&gt;
 *         &lt;element name="header_fonctionnel_in" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_header_fonctionnel_in"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_header_in",
    propOrder = {"headerTechniqueIn", "headerFonctionnelIn"})
public class TypeHeaderIn implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "header_technique_in", required = true)
  @NotNull
  @Valid
  protected TypeHeaderTechIn headerTechniqueIn;

  @XmlElement(name = "header_fonctionnel_in", required = true)
  @NotNull
  @Valid
  protected TypeHeaderFonctionnelIn headerFonctionnelIn;

  /**
   * Obtient la valeur de la propriété headerTechniqueIn.
   *
   * @return possible object is {@link TypeHeaderTechIn }
   */
  public TypeHeaderTechIn getHeaderTechniqueIn() {
    return headerTechniqueIn;
  }

  /**
   * Définit la valeur de la propriété headerTechniqueIn.
   *
   * @param value allowed object is {@link TypeHeaderTechIn }
   */
  public void setHeaderTechniqueIn(TypeHeaderTechIn value) {
    this.headerTechniqueIn = value;
  }

  /**
   * Obtient la valeur de la propriété headerFonctionnelIn.
   *
   * @return possible object is {@link TypeHeaderFonctionnelIn }
   */
  public TypeHeaderFonctionnelIn getHeaderFonctionnelIn() {
    return headerFonctionnelIn;
  }

  /**
   * Définit la valeur de la propriété headerFonctionnelIn.
   *
   * @param value allowed object is {@link TypeHeaderFonctionnelIn }
   */
  public void setHeaderFonctionnelIn(TypeHeaderFonctionnelIn value) {
    this.headerFonctionnelIn = value;
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
    if (draftCopy instanceof TypeHeaderIn) {
      final TypeHeaderIn copy = ((TypeHeaderIn) draftCopy);
      if (this.headerTechniqueIn != null) {
        TypeHeaderTechIn sourceHeaderTechniqueIn;
        sourceHeaderTechniqueIn = this.getHeaderTechniqueIn();
        TypeHeaderTechIn copyHeaderTechniqueIn =
            ((TypeHeaderTechIn)
                strategy.copy(
                    LocatorUtils.property(locator, "headerTechniqueIn", sourceHeaderTechniqueIn),
                    sourceHeaderTechniqueIn));
        copy.setHeaderTechniqueIn(copyHeaderTechniqueIn);
      } else {
        copy.headerTechniqueIn = null;
      }
      if (this.headerFonctionnelIn != null) {
        TypeHeaderFonctionnelIn sourceHeaderFonctionnelIn;
        sourceHeaderFonctionnelIn = this.getHeaderFonctionnelIn();
        TypeHeaderFonctionnelIn copyHeaderFonctionnelIn =
            ((TypeHeaderFonctionnelIn)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "headerFonctionnelIn", sourceHeaderFonctionnelIn),
                    sourceHeaderFonctionnelIn));
        copy.setHeaderFonctionnelIn(copyHeaderFonctionnelIn);
      } else {
        copy.headerFonctionnelIn = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderIn();
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
