package com.cegedimassurances.norme.carte;

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
 * Classe Java pour type_renvoi complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_renvoi"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="num_renvoi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="texte_renvoi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_renvoi",
    propOrder = {"numRenvoi", "texteRenvoi"})
public class TypeRenvoi implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "num_renvoi")
  protected String numRenvoi;

  @XmlElement(name = "texte_renvoi")
  protected String texteRenvoi;

  /**
   * Obtient la valeur de la propriété numRenvoi.
   *
   * @return possible object is {@link String }
   */
  public String getNumRenvoi() {
    return numRenvoi;
  }

  /**
   * Définit la valeur de la propriété numRenvoi.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumRenvoi(String value) {
    this.numRenvoi = value;
  }

  /**
   * Obtient la valeur de la propriété texteRenvoi.
   *
   * @return possible object is {@link String }
   */
  public String getTexteRenvoi() {
    return texteRenvoi;
  }

  /**
   * Définit la valeur de la propriété texteRenvoi.
   *
   * @param value allowed object is {@link String }
   */
  public void setTexteRenvoi(String value) {
    this.texteRenvoi = value;
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
    if (draftCopy instanceof TypeRenvoi) {
      final TypeRenvoi copy = ((TypeRenvoi) draftCopy);
      if (this.numRenvoi != null) {
        String sourceNumRenvoi;
        sourceNumRenvoi = this.getNumRenvoi();
        String copyNumRenvoi =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numRenvoi", sourceNumRenvoi), sourceNumRenvoi));
        copy.setNumRenvoi(copyNumRenvoi);
      } else {
        copy.numRenvoi = null;
      }
      if (this.texteRenvoi != null) {
        String sourceTexteRenvoi;
        sourceTexteRenvoi = this.getTexteRenvoi();
        String copyTexteRenvoi =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "texteRenvoi", sourceTexteRenvoi),
                    sourceTexteRenvoi));
        copy.setTexteRenvoi(copyTexteRenvoi);
      } else {
        copy.texteRenvoi = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeRenvoi();
  }
}
