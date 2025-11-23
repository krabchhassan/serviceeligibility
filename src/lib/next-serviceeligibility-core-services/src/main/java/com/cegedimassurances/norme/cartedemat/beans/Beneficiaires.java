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
 * Classe Java pour beneficiaires complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="beneficiaires"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="beneficiaire" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_beneficiaire" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "beneficiaires",
    propOrder = {"beneficiaire"})
public class Beneficiaires implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeBeneficiaire> beneficiaire;

  /**
   * Gets the value of the beneficiaire property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the beneficiaire property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getBeneficiaire().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeBeneficiaire }
   */
  public List<TypeBeneficiaire> getBeneficiaire() {
    if (beneficiaire == null) {
      beneficiaire = new ArrayList<>();
    }
    return this.beneficiaire;
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
    if (draftCopy instanceof Beneficiaires) {
      final Beneficiaires copy = ((Beneficiaires) draftCopy);
      if ((this.beneficiaire != null) && (!this.beneficiaire.isEmpty())) {
        List<TypeBeneficiaire> sourceBeneficiaire;
        sourceBeneficiaire =
            (((this.beneficiaire != null) && (!this.beneficiaire.isEmpty()))
                ? this.getBeneficiaire()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeBeneficiaire> copyBeneficiaire =
            ((List<TypeBeneficiaire>)
                strategy.copy(
                    LocatorUtils.property(locator, "beneficiaire", sourceBeneficiaire),
                    sourceBeneficiaire));
        copy.beneficiaire = null;
        if (copyBeneficiaire != null) {
          List<TypeBeneficiaire> uniqueBeneficiairel = copy.getBeneficiaire();
          uniqueBeneficiairel.addAll(copyBeneficiaire);
        }
      } else {
        copy.beneficiaire = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new Beneficiaires();
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
