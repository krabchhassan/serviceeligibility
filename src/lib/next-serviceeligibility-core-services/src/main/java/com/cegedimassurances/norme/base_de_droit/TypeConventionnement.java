package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigInteger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_conventionnement complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_conventionnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="priorite" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="type_conventionnement" type="{http://norme.cegedimassurances.com/base_de_droit}type_type_conventionnement"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_conventionnement",
    propOrder = {"priorite", "typeConventionnement"})
public class TypeConventionnement implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  protected BigInteger priorite;

  @XmlElement(name = "type_conventionnement", required = true)
  @NotNull
  @Valid
  protected TypeTypeConventionnement typeConventionnement;

  /**
   * Obtient la valeur de la propriété priorite.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getPriorite() {
    return priorite;
  }

  /**
   * Définit la valeur de la propriété priorite.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setPriorite(BigInteger value) {
    this.priorite = value;
  }

  /**
   * Obtient la valeur de la propriété typeConventionnement.
   *
   * @return possible object is {@link TypeTypeConventionnement }
   */
  public TypeTypeConventionnement getTypeConventionnement() {
    return typeConventionnement;
  }

  /**
   * Définit la valeur de la propriété typeConventionnement.
   *
   * @param value allowed object is {@link TypeTypeConventionnement }
   */
  public void setTypeConventionnement(TypeTypeConventionnement value) {
    this.typeConventionnement = value;
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
    if (draftCopy instanceof TypeConventionnement) {
      final TypeConventionnement copy = ((TypeConventionnement) draftCopy);
      if (this.priorite != null) {
        BigInteger sourcePriorite;
        sourcePriorite = this.getPriorite();
        BigInteger copyPriorite =
            ((BigInteger)
                strategy.copy(
                    LocatorUtils.property(locator, "priorite", sourcePriorite), sourcePriorite));
        copy.setPriorite(copyPriorite);
      } else {
        copy.priorite = null;
      }
      if (this.typeConventionnement != null) {
        TypeTypeConventionnement sourceTypeConventionnement;
        sourceTypeConventionnement = this.getTypeConventionnement();
        TypeTypeConventionnement copyTypeConventionnement =
            ((TypeTypeConventionnement)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeConventionnement", sourceTypeConventionnement),
                    sourceTypeConventionnement));
        copy.setTypeConventionnement(copyTypeConventionnement);
      } else {
        copy.typeConventionnement = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeConventionnement();
  }
}
