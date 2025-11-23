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
 * Classe Java pour parametresFormule complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="parametresFormule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parametreFormule" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_parametre_formule" maxOccurs="10" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "parametresFormule",
    propOrder = {"parametreFormule"})
public class ParametresFormule implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @Size(min = 0, max = 10)
  @Valid
  protected List<TypeParametreFormule> parametreFormule;

  /**
   * Gets the value of the parametreFormule property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the parametreFormule property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getParametreFormule().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeParametreFormule }
   */
  public List<TypeParametreFormule> getParametreFormule() {
    if (parametreFormule == null) {
      parametreFormule = new ArrayList<TypeParametreFormule>();
    }
    return this.parametreFormule;
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
    if (draftCopy instanceof ParametresFormule) {
      final ParametresFormule copy = ((ParametresFormule) draftCopy);
      if ((this.parametreFormule != null) && (!this.parametreFormule.isEmpty())) {
        List<TypeParametreFormule> sourceParametreFormule;
        sourceParametreFormule =
            (((this.parametreFormule != null) && (!this.parametreFormule.isEmpty()))
                ? this.getParametreFormule()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeParametreFormule> copyParametreFormule =
            ((List<TypeParametreFormule>)
                strategy.copy(
                    LocatorUtils.property(locator, "parametreFormule", sourceParametreFormule),
                    sourceParametreFormule));
        copy.parametreFormule = null;
        if (copyParametreFormule != null) {
          List<TypeParametreFormule> uniqueParametreFormulel = copy.getParametreFormule();
          uniqueParametreFormulel.addAll(copyParametreFormule);
        }
      } else {
        copy.parametreFormule = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new ParametresFormule();
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
