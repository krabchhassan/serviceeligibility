package com.cegedimassurances.norme.cartedemat.beans;

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
 * Type de convention par domaine couvert
 *
 * <p>Classe Java pour type_convention_domaine complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_convention_domaine"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="typeConventionnement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prioriteConventionnement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
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
    name = "type_convention_domaine",
    propOrder = {"typeConventionnement", "prioriteConventionnement"})
public class TypeConventionDomaine implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @Size(min = 1, max = 2)
  protected String typeConventionnement;

  @Size(min = 1, max = 2)
  protected String prioriteConventionnement;

  /**
   * Obtient la valeur de la propriété typeConventionnement.
   *
   * @return possible object is {@link String }
   */
  public String getTypeConventionnement() {
    return typeConventionnement;
  }

  /**
   * Définit la valeur de la propriété typeConventionnement.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeConventionnement(String value) {
    this.typeConventionnement = value;
  }

  /**
   * Obtient la valeur de la propriété prioriteConventionnement.
   *
   * @return possible object is {@link String }
   */
  public String getPrioriteConventionnement() {
    return prioriteConventionnement;
  }

  /**
   * Définit la valeur de la propriété prioriteConventionnement.
   *
   * @param value allowed object is {@link String }
   */
  public void setPrioriteConventionnement(String value) {
    this.prioriteConventionnement = value;
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
    if (draftCopy instanceof TypeConventionDomaine) {
      final TypeConventionDomaine copy = ((TypeConventionDomaine) draftCopy);
      if (this.typeConventionnement != null) {
        String sourceTypeConventionnement;
        sourceTypeConventionnement = this.getTypeConventionnement();
        String copyTypeConventionnement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeConventionnement", sourceTypeConventionnement),
                    sourceTypeConventionnement));
        copy.setTypeConventionnement(copyTypeConventionnement);
      } else {
        copy.typeConventionnement = null;
      }
      if (this.prioriteConventionnement != null) {
        String sourcePrioriteConventionnement;
        sourcePrioriteConventionnement = this.getPrioriteConventionnement();
        String copyPrioriteConventionnement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "prioriteConventionnement", sourcePrioriteConventionnement),
                    sourcePrioriteConventionnement));
        copy.setPrioriteConventionnement(copyPrioriteConventionnement);
      } else {
        copy.prioriteConventionnement = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeConventionDomaine();
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
