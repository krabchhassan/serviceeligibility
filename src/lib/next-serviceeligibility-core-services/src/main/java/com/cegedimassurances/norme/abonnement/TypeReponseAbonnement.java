package com.cegedimassurances.norme.abonnement;

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
 * Classe Java pour type_reponse_abonnement complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_reponse_abonnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="service" type="{http://norme.cegedimassurances.com/abonnement}type_services" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="numAmc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomAMC" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="contactAMC" type="{http://norme.cegedimassurances.com/abonnement}type_contact_AMC" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_reponse_abonnement",
    propOrder = {"service", "numAmc", "nomAMC", "contactAMC"})
public class TypeReponseAbonnement implements Serializable, Cloneable, CopyTo {

  @Valid protected List<TypeServices> service;
  protected String numAmc;

  @Size(max = 45)
  protected String nomAMC;

  @Valid protected TypeContactAMC contactAMC;

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
   * <p>Objects of the following type(s) are allowed in the list {@link TypeServices }
   */
  public List<TypeServices> getService() {
    if (service == null) {
      service = new ArrayList<TypeServices>();
    }
    return this.service;
  }

  /**
   * Obtient la valeur de la propriété numAmc.
   *
   * @return possible object is {@link String }
   */
  public String getNumAmc() {
    return numAmc;
  }

  /**
   * Définit la valeur de la propriété numAmc.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumAmc(String value) {
    this.numAmc = value;
  }

  /**
   * Obtient la valeur de la propriété nomAMC.
   *
   * @return possible object is {@link String }
   */
  public String getNomAMC() {
    return nomAMC;
  }

  /**
   * Définit la valeur de la propriété nomAMC.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAMC(String value) {
    this.nomAMC = value;
  }

  /**
   * Obtient la valeur de la propriété contactAMC.
   *
   * @return possible object is {@link TypeContactAMC }
   */
  public TypeContactAMC getContactAMC() {
    return contactAMC;
  }

  /**
   * Définit la valeur de la propriété contactAMC.
   *
   * @param value allowed object is {@link TypeContactAMC }
   */
  public void setContactAMC(TypeContactAMC value) {
    this.contactAMC = value;
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
    if (draftCopy instanceof TypeReponseAbonnement) {
      final TypeReponseAbonnement copy = ((TypeReponseAbonnement) draftCopy);
      if ((this.service != null) && (!this.service.isEmpty())) {
        List<TypeServices> sourceService;
        sourceService =
            (((this.service != null) && (!this.service.isEmpty())) ? this.getService() : null);
        @SuppressWarnings("unchecked")
        List<TypeServices> copyService =
            ((List<TypeServices>)
                strategy.copy(
                    LocatorUtils.property(locator, "service", sourceService), sourceService));
        copy.service = null;
        if (copyService != null) {
          List<TypeServices> uniqueServicel = copy.getService();
          uniqueServicel.addAll(copyService);
        }
      } else {
        copy.service = null;
      }
      if (this.numAmc != null) {
        String sourceNumAmc;
        sourceNumAmc = this.getNumAmc();
        String copyNumAmc =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numAmc", sourceNumAmc), sourceNumAmc));
        copy.setNumAmc(copyNumAmc);
      } else {
        copy.numAmc = null;
      }
      if (this.nomAMC != null) {
        String sourceNomAMC;
        sourceNomAMC = this.getNomAMC();
        String copyNomAMC =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomAMC", sourceNomAMC), sourceNomAMC));
        copy.setNomAMC(copyNomAMC);
      } else {
        copy.nomAMC = null;
      }
      if (this.contactAMC != null) {
        TypeContactAMC sourceContactAMC;
        sourceContactAMC = this.getContactAMC();
        TypeContactAMC copyContactAMC =
            ((TypeContactAMC)
                strategy.copy(
                    LocatorUtils.property(locator, "contactAMC", sourceContactAMC),
                    sourceContactAMC));
        copy.setContactAMC(copyContactAMC);
      } else {
        copy.contactAMC = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeReponseAbonnement();
  }
}
