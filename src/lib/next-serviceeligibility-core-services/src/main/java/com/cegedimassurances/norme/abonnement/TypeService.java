package com.cegedimassurances.norme.abonnement;

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
 * Classe Java pour type_service complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_service"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code_service_externe" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="actif" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="lib_commercial_service" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_service",
    propOrder = {"codeServiceExterne", "actif", "libCommercialService"})
public class TypeService implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "code_service_externe", required = true)
  @NotNull
  protected String codeServiceExterne;

  @NotNull protected boolean actif;

  @XmlElement(name = "lib_commercial_service")
  protected String libCommercialService;

  /**
   * Obtient la valeur de la propriété codeServiceExterne.
   *
   * @return possible object is {@link String }
   */
  public String getCodeServiceExterne() {
    return codeServiceExterne;
  }

  /**
   * Définit la valeur de la propriété codeServiceExterne.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeServiceExterne(String value) {
    this.codeServiceExterne = value;
  }

  /** Obtient la valeur de la propriété actif. */
  public boolean isActif() {
    return actif;
  }

  /** Définit la valeur de la propriété actif. */
  public void setActif(boolean value) {
    this.actif = value;
  }

  /**
   * Obtient la valeur de la propriété libCommercialService.
   *
   * @return possible object is {@link String }
   */
  public String getLibCommercialService() {
    return libCommercialService;
  }

  /**
   * Définit la valeur de la propriété libCommercialService.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibCommercialService(String value) {
    this.libCommercialService = value;
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
    if (draftCopy instanceof TypeService) {
      final TypeService copy = ((TypeService) draftCopy);
      if (this.codeServiceExterne != null) {
        String sourceCodeServiceExterne;
        sourceCodeServiceExterne = this.getCodeServiceExterne();
        String copyCodeServiceExterne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeServiceExterne", sourceCodeServiceExterne),
                    sourceCodeServiceExterne));
        copy.setCodeServiceExterne(copyCodeServiceExterne);
      } else {
        copy.codeServiceExterne = null;
      }
      boolean sourceActif;
      sourceActif = (true ? this.isActif() : false);
      boolean copyActif =
          strategy.copy(LocatorUtils.property(locator, "actif", sourceActif), sourceActif);
      copy.setActif(copyActif);
      if (this.libCommercialService != null) {
        String sourceLibCommercialService;
        sourceLibCommercialService = this.getLibCommercialService();
        String copyLibCommercialService =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "libCommercialService", sourceLibCommercialService),
                    sourceLibCommercialService));
        copy.setLibCommercialService(copyLibCommercialService);
      } else {
        copy.libCommercialService = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeService();
  }
}
