package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 * Classe Java pour prestations complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="prestations"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="prestation" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_prestation" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "prestations",
    propOrder = {"prestation"})
public class Prestations implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypePrestation> prestation;

  /**
   * Gets the value of the prestation property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the prestation property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getPrestation().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypePrestation }
   */
  public List<TypePrestation> getPrestation() {
    if (prestation == null) {
      prestation = new ArrayList<TypePrestation>();
    }
    return this.prestation;
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
    if (draftCopy instanceof Prestations) {
      final Prestations copy = ((Prestations) draftCopy);
      if ((this.prestation != null) && (!this.prestation.isEmpty())) {
        List<TypePrestation> sourcePrestation;
        sourcePrestation =
            (((this.prestation != null) && (!this.prestation.isEmpty()))
                ? this.getPrestation()
                : null);
        @SuppressWarnings("unchecked")
        List<TypePrestation> copyPrestation =
            ((List<TypePrestation>)
                strategy.copy(
                    LocatorUtils.property(locator, "prestation", sourcePrestation),
                    sourcePrestation));
        copy.prestation = null;
        if (copyPrestation != null) {
          List<TypePrestation> uniquePrestationl = copy.getPrestation();
          uniquePrestationl.addAll(copyPrestation);
        }
      } else {
        copy.prestation = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new Prestations();
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
