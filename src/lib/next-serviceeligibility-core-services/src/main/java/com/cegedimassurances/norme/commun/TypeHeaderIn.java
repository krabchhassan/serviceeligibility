package com.cegedimassurances.norme.commun;

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
 *         &lt;element name="headerTech" type="{http://norme.cegedimassurances.com/commun}type_header_tech_in"/&gt;
 *         &lt;element name="headerFonc" type="{http://norme.cegedimassurances.com/commun}type_header_fonctionnel_in" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_header_in",
    propOrder = {"headerTech", "headerFonc"})
public class TypeHeaderIn implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeHeaderTechIn headerTech;

  @Valid protected TypeHeaderFonctionnelIn headerFonc;

  /**
   * Obtient la valeur de la propriété headerTech.
   *
   * @return possible object is {@link TypeHeaderTechIn }
   */
  public TypeHeaderTechIn getHeaderTech() {
    return headerTech;
  }

  /**
   * Définit la valeur de la propriété headerTech.
   *
   * @param value allowed object is {@link TypeHeaderTechIn }
   */
  public void setHeaderTech(TypeHeaderTechIn value) {
    this.headerTech = value;
  }

  /**
   * Obtient la valeur de la propriété headerFonc.
   *
   * @return possible object is {@link TypeHeaderFonctionnelIn }
   */
  public TypeHeaderFonctionnelIn getHeaderFonc() {
    return headerFonc;
  }

  /**
   * Définit la valeur de la propriété headerFonc.
   *
   * @param value allowed object is {@link TypeHeaderFonctionnelIn }
   */
  public void setHeaderFonc(TypeHeaderFonctionnelIn value) {
    this.headerFonc = value;
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
    if (draftCopy instanceof TypeHeaderIn) {
      final TypeHeaderIn copy = ((TypeHeaderIn) draftCopy);
      if (this.headerTech != null) {
        TypeHeaderTechIn sourceHeaderTech;
        sourceHeaderTech = this.getHeaderTech();
        TypeHeaderTechIn copyHeaderTech =
            ((TypeHeaderTechIn)
                strategy.copy(
                    LocatorUtils.property(locator, "headerTech", sourceHeaderTech),
                    sourceHeaderTech));
        copy.setHeaderTech(copyHeaderTech);
      } else {
        copy.headerTech = null;
      }
      if (this.headerFonc != null) {
        TypeHeaderFonctionnelIn sourceHeaderFonc;
        sourceHeaderFonc = this.getHeaderFonc();
        TypeHeaderFonctionnelIn copyHeaderFonc =
            ((TypeHeaderFonctionnelIn)
                strategy.copy(
                    LocatorUtils.property(locator, "headerFonc", sourceHeaderFonc),
                    sourceHeaderFonc));
        copy.setHeaderFonc(copyHeaderFonc);
      } else {
        copy.headerFonc = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderIn();
  }
}
