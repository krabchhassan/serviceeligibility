package com.cegedimassurances.norme.abonnement;

import com.cegedimactiv.norme.fsiq.api.ReponseWsFsiq;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import com.cegedimassurances.norme.commun.TypeHeaderOut;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 * Classe Java pour reponse_abonnement complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="reponse_abonnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_out" type="{http://norme.cegedimassurances.com/commun}type_header_out"/&gt;
 *         &lt;element name="code_reponse" type="{http://norme.cegedimassurances.com/commun}type_codeReponse"/&gt;
 *         &lt;element name="services" type="{http://norme.cegedimassurances.com/abonnement}type_services"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "reponse_abonnement",
    propOrder = {"headerOut", "codeReponse", "services"})
public class ReponseAbonnement implements Serializable, Cloneable, ReponseWsFsiq, CopyTo {

  @XmlElement(name = "header_out", required = true)
  @NotNull
  @Valid
  protected TypeHeaderOut headerOut;

  @XmlElement(name = "code_reponse", required = true)
  @NotNull
  @Valid
  protected TypeCodeReponse codeReponse;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeServices services;

  /**
   * Obtient la valeur de la propriété headerOut.
   *
   * @return possible object is {@link TypeHeaderOut }
   */
  public TypeHeaderOut getHeaderOut() {
    return headerOut;
  }

  /**
   * Définit la valeur de la propriété headerOut.
   *
   * @param value allowed object is {@link TypeHeaderOut }
   */
  public void setHeaderOut(TypeHeaderOut value) {
    this.headerOut = value;
  }

  /**
   * Obtient la valeur de la propriété codeReponse.
   *
   * @return possible object is {@link TypeCodeReponse }
   */
  public TypeCodeReponse getCodeReponse() {
    return codeReponse;
  }

  /**
   * Définit la valeur de la propriété codeReponse.
   *
   * @param value allowed object is {@link TypeCodeReponse }
   */
  public void setCodeReponse(TypeCodeReponse value) {
    this.codeReponse = value;
  }

  /**
   * Obtient la valeur de la propriété services.
   *
   * @return possible object is {@link TypeServices }
   */
  public TypeServices getServices() {
    return services;
  }

  /**
   * Définit la valeur de la propriété services.
   *
   * @param value allowed object is {@link TypeServices }
   */
  public void setServices(TypeServices value) {
    this.services = value;
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
    if (draftCopy instanceof ReponseAbonnement) {
      final ReponseAbonnement copy = ((ReponseAbonnement) draftCopy);
      if (this.headerOut != null) {
        TypeHeaderOut sourceHeaderOut;
        sourceHeaderOut = this.getHeaderOut();
        TypeHeaderOut copyHeaderOut =
            ((TypeHeaderOut)
                strategy.copy(
                    LocatorUtils.property(locator, "headerOut", sourceHeaderOut), sourceHeaderOut));
        copy.setHeaderOut(copyHeaderOut);
      } else {
        copy.headerOut = null;
      }
      if (this.codeReponse != null) {
        TypeCodeReponse sourceCodeReponse;
        sourceCodeReponse = this.getCodeReponse();
        TypeCodeReponse copyCodeReponse =
            ((TypeCodeReponse)
                strategy.copy(
                    LocatorUtils.property(locator, "codeReponse", sourceCodeReponse),
                    sourceCodeReponse));
        copy.setCodeReponse(copyCodeReponse);
      } else {
        copy.codeReponse = null;
      }
      if (this.services != null) {
        TypeServices sourceServices;
        sourceServices = this.getServices();
        TypeServices copyServices =
            ((TypeServices)
                strategy.copy(
                    LocatorUtils.property(locator, "services", sourceServices), sourceServices));
        copy.setServices(copyServices);
      } else {
        copy.services = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new ReponseAbonnement();
  }
}
