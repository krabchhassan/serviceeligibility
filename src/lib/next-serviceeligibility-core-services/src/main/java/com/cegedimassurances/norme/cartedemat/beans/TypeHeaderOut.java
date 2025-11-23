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
 * <p>Classe Java pour type_header_out complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_header_out"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_technique_out" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_header_tech_out"/&gt;
 *         &lt;element name="header_fonctionnel_out" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_header_fonctionnel_out"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_header_out",
    propOrder = {"headerTechniqueOut", "headerFonctionnelOut"})
public class TypeHeaderOut implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "header_technique_out", required = true)
  @NotNull
  @Valid
  protected TypeHeaderTechOut headerTechniqueOut;

  @XmlElement(name = "header_fonctionnel_out", required = true)
  @NotNull
  @Valid
  protected TypeHeaderFonctionnelOut headerFonctionnelOut;

  /**
   * Obtient la valeur de la propriété headerTechniqueOut.
   *
   * @return possible object is {@link TypeHeaderTechOut }
   */
  public TypeHeaderTechOut getHeaderTechniqueOut() {
    return headerTechniqueOut;
  }

  /**
   * Définit la valeur de la propriété headerTechniqueOut.
   *
   * @param value allowed object is {@link TypeHeaderTechOut }
   */
  public void setHeaderTechniqueOut(TypeHeaderTechOut value) {
    this.headerTechniqueOut = value;
  }

  /**
   * Obtient la valeur de la propriété headerFonctionnelOut.
   *
   * @return possible object is {@link TypeHeaderFonctionnelOut }
   */
  public TypeHeaderFonctionnelOut getHeaderFonctionnelOut() {
    return headerFonctionnelOut;
  }

  /**
   * Définit la valeur de la propriété headerFonctionnelOut.
   *
   * @param value allowed object is {@link TypeHeaderFonctionnelOut }
   */
  public void setHeaderFonctionnelOut(TypeHeaderFonctionnelOut value) {
    this.headerFonctionnelOut = value;
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
    if (draftCopy instanceof TypeHeaderOut) {
      final TypeHeaderOut copy = ((TypeHeaderOut) draftCopy);
      if (this.headerTechniqueOut != null) {
        TypeHeaderTechOut sourceHeaderTechniqueOut;
        sourceHeaderTechniqueOut = this.getHeaderTechniqueOut();
        TypeHeaderTechOut copyHeaderTechniqueOut =
            ((TypeHeaderTechOut)
                strategy.copy(
                    LocatorUtils.property(locator, "headerTechniqueOut", sourceHeaderTechniqueOut),
                    sourceHeaderTechniqueOut));
        copy.setHeaderTechniqueOut(copyHeaderTechniqueOut);
      } else {
        copy.headerTechniqueOut = null;
      }
      if (this.headerFonctionnelOut != null) {
        TypeHeaderFonctionnelOut sourceHeaderFonctionnelOut;
        sourceHeaderFonctionnelOut = this.getHeaderFonctionnelOut();
        TypeHeaderFonctionnelOut copyHeaderFonctionnelOut =
            ((TypeHeaderFonctionnelOut)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "headerFonctionnelOut", sourceHeaderFonctionnelOut),
                    sourceHeaderFonctionnelOut));
        copy.setHeaderFonctionnelOut(copyHeaderFonctionnelOut);
      } else {
        copy.headerFonctionnelOut = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderOut();
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
