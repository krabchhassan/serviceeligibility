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
 * Classe Java pour type_reponse_tarification complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_reponse_tarification"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="reponsePrescription" type="{http://norme.cegedimassurances.com/tarification-prescription}type_reponse_prescription" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_reponse_tarification",
    propOrder = {"reponsePrescription"})
public class TypeReponseTarification implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeReponsePrescription> reponsePrescription;

  /**
   * Gets the value of the reponsePrescription property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the reponsePrescription property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getReponsePrescription().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeReponsePrescription }
   */
  public List<TypeReponsePrescription> getReponsePrescription() {
    if (reponsePrescription == null) {
      reponsePrescription = new ArrayList<TypeReponsePrescription>();
    }
    return this.reponsePrescription;
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
    if (draftCopy instanceof TypeReponseTarification) {
      final TypeReponseTarification copy = ((TypeReponseTarification) draftCopy);
      if ((this.reponsePrescription != null) && (!this.reponsePrescription.isEmpty())) {
        List<TypeReponsePrescription> sourceReponsePrescription;
        sourceReponsePrescription =
            (((this.reponsePrescription != null) && (!this.reponsePrescription.isEmpty()))
                ? this.getReponsePrescription()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeReponsePrescription> copyReponsePrescription =
            ((List<TypeReponsePrescription>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "reponsePrescription", sourceReponsePrescription),
                    sourceReponsePrescription));
        copy.reponsePrescription = null;
        if (copyReponsePrescription != null) {
          List<TypeReponsePrescription> uniqueReponsePrescriptionl = copy.getReponsePrescription();
          uniqueReponsePrescriptionl.addAll(copyReponsePrescription);
        }
      } else {
        copy.reponsePrescription = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeReponseTarification();
  }
}
