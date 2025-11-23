package com.cegedimassurances.norme.abonnement;

import com.cegedimassurances.norme.commun.TypeDates;
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
 * Classe Java pour type_info_abonnement complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_info_abonnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date_reference" type="{http://norme.cegedimassurances.com/commun}type_dates"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_info_abonnement",
    propOrder = {"dateReference"})
public class TypeInfoAbonnement implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "date_reference", required = true)
  @NotNull
  @Valid
  protected TypeDates dateReference;

  /**
   * Obtient la valeur de la propriété dateReference.
   *
   * @return possible object is {@link TypeDates }
   */
  public TypeDates getDateReference() {
    return dateReference;
  }

  /**
   * Définit la valeur de la propriété dateReference.
   *
   * @param value allowed object is {@link TypeDates }
   */
  public void setDateReference(TypeDates value) {
    this.dateReference = value;
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
    if (draftCopy instanceof TypeInfoAbonnement) {
      final TypeInfoAbonnement copy = ((TypeInfoAbonnement) draftCopy);
      if (this.dateReference != null) {
        TypeDates sourceDateReference;
        sourceDateReference = this.getDateReference();
        TypeDates copyDateReference =
            ((TypeDates)
                strategy.copy(
                    LocatorUtils.property(locator, "dateReference", sourceDateReference),
                    sourceDateReference));
        copy.setDateReference(copyDateReference);
      } else {
        copy.dateReference = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInfoAbonnement();
  }
}
