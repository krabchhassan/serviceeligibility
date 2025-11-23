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
 * Domaine de droits couvert pour le contrat
 *
 * <p>Classe Java pour type_domaine_droits complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_domaine_droits"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeDomaine"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="conventions" type="{http://norme.cegedimassurances.com/carteDemat/beans}conventions" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_domaine_droits",
    propOrder = {"codeDomaine", "conventions"})
public class TypeDomaineDroits implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 5)
  protected String codeDomaine;

  @Valid protected Conventions conventions;

  /**
   * Obtient la valeur de la propriété codeDomaine.
   *
   * @return possible object is {@link String }
   */
  public String getCodeDomaine() {
    return codeDomaine;
  }

  /**
   * Définit la valeur de la propriété codeDomaine.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeDomaine(String value) {
    this.codeDomaine = value;
  }

  /**
   * Obtient la valeur de la propriété conventions.
   *
   * @return possible object is {@link Conventions }
   */
  public Conventions getConventions() {
    return conventions;
  }

  /**
   * Définit la valeur de la propriété conventions.
   *
   * @param value allowed object is {@link Conventions }
   */
  public void setConventions(Conventions value) {
    this.conventions = value;
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
    if (draftCopy instanceof TypeDomaineDroits) {
      final TypeDomaineDroits copy = ((TypeDomaineDroits) draftCopy);
      if (this.codeDomaine != null) {
        String sourceCodeDomaine;
        sourceCodeDomaine = this.getCodeDomaine();
        String copyCodeDomaine =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeDomaine", sourceCodeDomaine),
                    sourceCodeDomaine));
        copy.setCodeDomaine(copyCodeDomaine);
      } else {
        copy.codeDomaine = null;
      }
      if (this.conventions != null) {
        Conventions sourceConventions;
        sourceConventions = this.getConventions();
        Conventions copyConventions =
            ((Conventions)
                strategy.copy(
                    LocatorUtils.property(locator, "conventions", sourceConventions),
                    sourceConventions));
        copy.setConventions(copyConventions);
      } else {
        copy.conventions = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDomaineDroits();
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
