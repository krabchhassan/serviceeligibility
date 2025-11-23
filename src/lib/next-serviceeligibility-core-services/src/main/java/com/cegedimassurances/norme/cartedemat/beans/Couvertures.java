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
 * Classe Java pour couvertures complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="couvertures"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="couverture" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_beneficiaire_couverture" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "couvertures",
    propOrder = {"couverture"})
public class Couvertures implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeBeneficiaireCouverture> couverture;

  /**
   * Gets the value of the couverture property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the couverture property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getCouverture().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeBeneficiaireCouverture }
   */
  public List<TypeBeneficiaireCouverture> getCouverture() {
    if (couverture == null) {
      couverture = new ArrayList<TypeBeneficiaireCouverture>();
    }
    return this.couverture;
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
    if (draftCopy instanceof Couvertures) {
      final Couvertures copy = ((Couvertures) draftCopy);
      if ((this.couverture != null) && (!this.couverture.isEmpty())) {
        List<TypeBeneficiaireCouverture> sourceCouverture;
        sourceCouverture =
            (((this.couverture != null) && (!this.couverture.isEmpty()))
                ? this.getCouverture()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeBeneficiaireCouverture> copyCouverture =
            ((List<TypeBeneficiaireCouverture>)
                strategy.copy(
                    LocatorUtils.property(locator, "couverture", sourceCouverture),
                    sourceCouverture));
        copy.couverture = null;
        if (copyCouverture != null) {
          List<TypeBeneficiaireCouverture> uniqueCouverturel = copy.getCouverture();
          uniqueCouverturel.addAll(copyCouverture);
        }
      } else {
        copy.couverture = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new Couvertures();
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
