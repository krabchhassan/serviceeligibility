package com.cegedimassurances.norme.tarification_prescription;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
 * Classe Java pour type_codeReponse_prestation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_codeReponse_prestation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="150"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_codeReponse_prestation",
    propOrder = {"code", "libelle"})
public class TypeCodeReponsePrestation implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 3)
  protected String code;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 150)
  protected String libelle;

  /**
   * Obtient la valeur de la propriété code.
   *
   * @return possible object is {@link String }
   */
  public String getCode() {
    return code;
  }

  /**
   * Définit la valeur de la propriété code.
   *
   * @param value allowed object is {@link String }
   */
  public void setCode(String value) {
    this.code = value;
  }

  /**
   * Obtient la valeur de la propriété libelle.
   *
   * @return possible object is {@link String }
   */
  public String getLibelle() {
    return libelle;
  }

  /**
   * Définit la valeur de la propriété libelle.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelle(String value) {
    this.libelle = value;
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
    if (draftCopy instanceof TypeCodeReponsePrestation) {
      final TypeCodeReponsePrestation copy = ((TypeCodeReponsePrestation) draftCopy);
      if (this.code != null) {
        String sourceCode;
        sourceCode = this.getCode();
        String copyCode =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "code", sourceCode), sourceCode));
        copy.setCode(copyCode);
      } else {
        copy.code = null;
      }
      if (this.libelle != null) {
        String sourceLibelle;
        sourceLibelle = this.getLibelle();
        String copyLibelle =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelle", sourceLibelle), sourceLibelle));
        copy.setLibelle(copyLibelle);
      } else {
        copy.libelle = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeCodeReponsePrestation();
  }
}
