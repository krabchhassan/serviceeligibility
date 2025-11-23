package com.cegedimassurances.norme.abonnement;

import jakarta.validation.Valid;
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
 * Classe Java pour type_services complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_services"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="service" type="{http://norme.cegedimassurances.com/abonnement}type_service" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_services",
    propOrder = {"service"})
public class TypeServices implements Serializable, Cloneable, CopyTo {

  @Valid protected List<TypeService> service;

  /**
   * Gets the value of the service property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the service property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getService().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeService }
   */
  public List<TypeService> getService() {
    if (service == null) {
      service = new ArrayList<TypeService>();
    }
    return this.service;
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
    if (draftCopy instanceof TypeServices) {
      final TypeServices copy = ((TypeServices) draftCopy);
      if ((this.service != null) && (!this.service.isEmpty())) {
        List<TypeService> sourceService;
        sourceService =
            (((this.service != null) && (!this.service.isEmpty())) ? this.getService() : null);
        @SuppressWarnings("unchecked")
        List<TypeService> copyService =
            ((List<TypeService>)
                strategy.copy(
                    LocatorUtils.property(locator, "service", sourceService), sourceService));
        copy.service = null;
        if (copyService != null) {
          List<TypeService> uniqueServicel = copy.getService();
          uniqueServicel.addAll(copyService);
        }
      } else {
        copy.service = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeServices();
  }
}
