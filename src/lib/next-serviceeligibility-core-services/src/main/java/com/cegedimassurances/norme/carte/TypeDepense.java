package com.cegedimassurances.norme.carte;

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
 * Classe Java pour type_depense complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_depense"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code_libelle" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="code_convention" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="code_formule" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_depense",
    propOrder = {"codeLibelle", "codeConvention", "codeFormule"})
public class TypeDepense implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "code_libelle", required = true)
  @NotNull
  protected String codeLibelle;

  @XmlElement(name = "code_convention")
  protected String codeConvention;

  @XmlElement(name = "code_formule", required = true)
  @NotNull
  protected String codeFormule;

  /**
   * Obtient la valeur de la propriété codeLibelle.
   *
   * @return possible object is {@link String }
   */
  public String getCodeLibelle() {
    return codeLibelle;
  }

  /**
   * Définit la valeur de la propriété codeLibelle.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeLibelle(String value) {
    this.codeLibelle = value;
  }

  /**
   * Obtient la valeur de la propriété codeConvention.
   *
   * @return possible object is {@link String }
   */
  public String getCodeConvention() {
    return codeConvention;
  }

  /**
   * Définit la valeur de la propriété codeConvention.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeConvention(String value) {
    this.codeConvention = value;
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
    if (draftCopy instanceof TypeDepense) {
      final TypeDepense copy = ((TypeDepense) draftCopy);
      if (this.codeLibelle != null) {
        String sourceCodeLibelle;
        sourceCodeLibelle = this.getCodeLibelle();
        String copyCodeLibelle =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeLibelle", sourceCodeLibelle),
                    sourceCodeLibelle));
        copy.setCodeLibelle(copyCodeLibelle);
      } else {
        copy.codeLibelle = null;
      }
      if (this.codeConvention != null) {
        String sourceCodeConvention;
        sourceCodeConvention = this.getCodeConvention();
        String copyCodeConvention =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeConvention", sourceCodeConvention),
                    sourceCodeConvention));
        copy.setCodeConvention(copyCodeConvention);
      } else {
        copy.codeConvention = null;
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
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDepense();
  }
}
