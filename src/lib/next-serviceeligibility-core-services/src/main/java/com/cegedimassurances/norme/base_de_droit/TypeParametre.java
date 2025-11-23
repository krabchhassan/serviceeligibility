package com.cegedimassurances.norme.base_de_droit;

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
 * Classe Java pour type_parametre complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_parametre"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numero"&gt;
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
 *         &lt;element name="valeur"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="7"/&gt;
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
    name = "type_parametre",
    propOrder = {"numero", "libelle", "valeur"})
public class TypeParametre implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 3)
  protected String numero;

  @Size(max = 45)
  protected String libelle;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 7)
  protected String valeur;

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
   * Obtient la valeur de la propriété valeur.
   *
   * @return possible object is {@link String }
   */
  public String getValeur() {
    return valeur;
  }

  /**
   * Définit la valeur de la propriété valeur.
   *
   * @param value allowed object is {@link String }
   */
  public void setValeur(String value) {
    this.valeur = value;
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
    if (draftCopy instanceof TypeParametre) {
      final TypeParametre copy = ((TypeParametre) draftCopy);
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
      if (this.valeur != null) {
        String sourceValeur;
        sourceValeur = this.getValeur();
        String copyValeur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "valeur", sourceValeur), sourceValeur));
        copy.setValeur(copyValeur);
      } else {
        copy.valeur = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeParametre();
  }
}
