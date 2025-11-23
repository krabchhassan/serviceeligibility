package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
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
 * Resultat de la consolidation des gararnties et des taux de couverture
 *
 * <p>Classe Java pour type_prestation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_prestation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codePrestations"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="codeFormule"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="parametresFormule" type="{http://norme.cegedimassurances.com/carteDemat/beans}parametresFormule" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_prestation",
    propOrder = {"codePrestations", "codeFormule", "parametresFormule"})
public class TypePrestation implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 5)
  protected String codePrestations;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 3)
  protected String codeFormule;

  @Valid protected ParametresFormule parametresFormule;

  /**
   * Obtient la valeur de la propriété codePrestations.
   *
   * @return possible object is {@link String }
   */
  public String getCodePrestations() {
    return codePrestations;
  }

  /**
   * Définit la valeur de la propriété codePrestations.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodePrestations(String value) {
    this.codePrestations = value;
  }

  /**
   * Obtient la valeur de la propriété codeFormule.
   *
   * @return possible object is {@link String }
   */
  public String getCodeFormule() {
    return codeFormule;
  }

  /**
   * Définit la valeur de la propriété codeFormule.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeFormule(String value) {
    this.codeFormule = value;
  }

  /**
   * Obtient la valeur de la propriété parametresFormule.
   *
   * @return possible object is {@link ParametresFormule }
   */
  public ParametresFormule getParametresFormule() {
    return parametresFormule;
  }

  /**
   * Définit la valeur de la propriété parametresFormule.
   *
   * @param value allowed object is {@link ParametresFormule }
   */
  public void setParametresFormule(ParametresFormule value) {
    this.parametresFormule = value;
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
    if (draftCopy instanceof TypePrestation) {
      final TypePrestation copy = ((TypePrestation) draftCopy);
      if (this.codePrestations != null) {
        String sourceCodePrestations;
        sourceCodePrestations = this.getCodePrestations();
        String copyCodePrestations =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codePrestations", sourceCodePrestations),
                    sourceCodePrestations));
        copy.setCodePrestations(copyCodePrestations);
      } else {
        copy.codePrestations = null;
      }
      if (this.codeFormule != null) {
        String sourceCodeFormule;
        sourceCodeFormule = this.getCodeFormule();
        String copyCodeFormule =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeFormule", sourceCodeFormule),
                    sourceCodeFormule));
        copy.setCodeFormule(copyCodeFormule);
      } else {
        copy.codeFormule = null;
      }
      if (this.parametresFormule != null) {
        ParametresFormule sourceParametresFormule;
        sourceParametresFormule = this.getParametresFormule();
        ParametresFormule copyParametresFormule =
            ((ParametresFormule)
                strategy.copy(
                    LocatorUtils.property(locator, "parametresFormule", sourceParametresFormule),
                    sourceParametresFormule));
        copy.setParametresFormule(copyParametresFormule);
      } else {
        copy.parametresFormule = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypePrestation();
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
