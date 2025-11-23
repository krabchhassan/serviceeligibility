package com.cegedimassurances.norme.tarification_prescription;

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
 * Classe Java pour type_requete_tarification complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_requete_tarification"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="infoPrescription" type="{http://norme.cegedimassurances.com/tarification-prescription}type_info_prescription" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_requete_tarification",
    propOrder = {"infoPrescription"})
public class TypeRequeteTarification implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeInfoPrescription> infoPrescription;

  /**
   * Gets the value of the infoPrescription property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the infoPrescription property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getInfoPrescription().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeInfoPrescription }
   */
  public List<TypeInfoPrescription> getInfoPrescription() {
    if (infoPrescription == null) {
      infoPrescription = new ArrayList<TypeInfoPrescription>();
    }
    return this.infoPrescription;
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
    if (draftCopy instanceof TypeRequeteTarification) {
      final TypeRequeteTarification copy = ((TypeRequeteTarification) draftCopy);
      if ((this.infoPrescription != null) && (!this.infoPrescription.isEmpty())) {
        List<TypeInfoPrescription> sourceInfoPrescription;
        sourceInfoPrescription =
            (((this.infoPrescription != null) && (!this.infoPrescription.isEmpty()))
                ? this.getInfoPrescription()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeInfoPrescription> copyInfoPrescription =
            ((List<TypeInfoPrescription>)
                strategy.copy(
                    LocatorUtils.property(locator, "infoPrescription", sourceInfoPrescription),
                    sourceInfoPrescription));
        copy.infoPrescription = null;
        if (copyInfoPrescription != null) {
          List<TypeInfoPrescription> uniqueInfoPrescriptionl = copy.getInfoPrescription();
          uniqueInfoPrescriptionl.addAll(copyInfoPrescription);
        }
      } else {
        copy.infoPrescription = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeRequeteTarification();
  }
}
