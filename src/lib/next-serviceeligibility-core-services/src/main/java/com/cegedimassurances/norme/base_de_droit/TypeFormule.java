package com.cegedimassurances.norme.base_de_droit;

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
 * Classe Java pour type_formule complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_formule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numero" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="parametres" type="{http://norme.cegedimassurances.com/base_de_droit}type_parametre" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_formule",
    propOrder = {"numero", "libelle", "parametres"})
public class TypeFormule implements Serializable, Cloneable, CopyTo {

  @Size(max = 3)
  protected String numero;

  @Size(max = 45)
  protected String libelle;

  @Valid protected List<TypeParametre> parametres;

  /**
   * Obtient la valeur de la propriété numero.
   *
   * @return possible object is {@link String }
   */
  public String getNumero() {
    return numero;
  }

  /**
   * Définit la valeur de la propriété numero.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumero(String value) {
    this.numero = value;
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
   * Gets the value of the parametres property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the parametres property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getParametres().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeParametre }
   */
  public List<TypeParametre> getParametres() {
    if (parametres == null) {
      parametres = new ArrayList<TypeParametre>();
    }
    return this.parametres;
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
    if (draftCopy instanceof TypeFormule) {
      final TypeFormule copy = ((TypeFormule) draftCopy);
      if (this.numero != null) {
        String sourceNumero;
        sourceNumero = this.getNumero();
        String copyNumero =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numero", sourceNumero), sourceNumero));
        copy.setNumero(copyNumero);
      } else {
        copy.numero = null;
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
      if ((this.parametres != null) && (!this.parametres.isEmpty())) {
        List<TypeParametre> sourceParametres;
        sourceParametres =
            (((this.parametres != null) && (!this.parametres.isEmpty()))
                ? this.getParametres()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeParametre> copyParametres =
            ((List<TypeParametre>)
                strategy.copy(
                    LocatorUtils.property(locator, "parametres", sourceParametres),
                    sourceParametres));
        copy.parametres = null;
        if (copyParametres != null) {
          List<TypeParametre> uniqueParametresl = copy.getParametres();
          uniqueParametresl.addAll(copyParametres);
        }
      } else {
        copy.parametres = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeFormule();
  }
}
