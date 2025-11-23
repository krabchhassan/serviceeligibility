package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour conventions complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="conventions"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="convention" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_convention_domaine" maxOccurs="5" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "conventions",
    propOrder = {"convention"})
public class Conventions implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @Size(min = 0, max = 5)
  @Valid
  protected List<TypeConventionDomaine> convention;

  /**
   * Gets the value of the convention property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the convention property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getConvention().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeConventionDomaine }
   */
  public List<TypeConventionDomaine> getConvention() {
    if (convention == null) {
      convention = new ArrayList<TypeConventionDomaine>();
    }
    return this.convention;
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
    if (draftCopy instanceof Conventions) {
      final Conventions copy = ((Conventions) draftCopy);
      if ((this.convention != null) && (!this.convention.isEmpty())) {
        List<TypeConventionDomaine> sourceConvention;
        sourceConvention =
            (((this.convention != null) && (!this.convention.isEmpty()))
                ? this.getConvention()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeConventionDomaine> copyConvention =
            ((List<TypeConventionDomaine>)
                strategy.copy(
                    LocatorUtils.property(locator, "convention", sourceConvention),
                    sourceConvention));
        copy.convention = null;
        if (copyConvention != null) {
          List<TypeConventionDomaine> uniqueConventionl = copy.getConvention();
          uniqueConventionl.addAll(copyConvention);
        }
      } else {
        copy.convention = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new Conventions();
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
