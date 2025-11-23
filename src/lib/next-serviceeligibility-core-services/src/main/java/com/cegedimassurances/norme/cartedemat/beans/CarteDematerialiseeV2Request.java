package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
 * Classe Java pour anonymous complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_in" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_header_in"/&gt;
 *         &lt;element name="request" type="{http://norme.cegedimassurances.com/carteDemat/beans}carteDematerialiseeType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"headerIn", "request"})
@XmlRootElement(name = "carteDematerialiseeV2Request")
public class CarteDematerialiseeV2Request implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "header_in", required = true)
  @NotNull
  @Valid
  protected TypeHeaderIn headerIn;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected CarteDematerialiseeType request;

  /**
   * Obtient la valeur de la propriété headerIn.
   *
   * @return possible object is {@link TypeHeaderIn }
   */
  public TypeHeaderIn getHeaderIn() {
    return headerIn;
  }

  /**
   * Définit la valeur de la propriété headerIn.
   *
   * @param value allowed object is {@link TypeHeaderIn }
   */
  public void setHeaderIn(TypeHeaderIn value) {
    this.headerIn = value;
  }

  /**
   * Obtient la valeur de la propriété request.
   *
   * @return possible object is {@link CarteDematerialiseeType }
   */
  public CarteDematerialiseeType getRequest() {
    return request;
  }

  /**
   * Définit la valeur de la propriété request.
   *
   * @param value allowed object is {@link CarteDematerialiseeType }
   */
  public void setRequest(CarteDematerialiseeType value) {
    this.request = value;
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
    if (draftCopy instanceof CarteDematerialiseeV2Request) {
      final CarteDematerialiseeV2Request copy = ((CarteDematerialiseeV2Request) draftCopy);
      if (this.headerIn != null) {
        TypeHeaderIn sourceHeaderIn;
        sourceHeaderIn = this.getHeaderIn();
        TypeHeaderIn copyHeaderIn =
            ((TypeHeaderIn)
                strategy.copy(
                    LocatorUtils.property(locator, "headerIn", sourceHeaderIn), sourceHeaderIn));
        copy.setHeaderIn(copyHeaderIn);
      } else {
        copy.headerIn = null;
      }
      if (this.request != null) {
        CarteDematerialiseeType sourceRequest;
        sourceRequest = this.getRequest();
        CarteDematerialiseeType copyRequest =
            ((CarteDematerialiseeType)
                strategy.copy(
                    LocatorUtils.property(locator, "request", sourceRequest), sourceRequest));
        copy.setRequest(copyRequest);
      } else {
        copy.request = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new CarteDematerialiseeV2Request();
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
