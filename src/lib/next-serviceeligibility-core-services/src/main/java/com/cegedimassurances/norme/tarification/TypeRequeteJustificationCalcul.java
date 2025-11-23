package com.cegedimassurances.norme.tarification;

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
 * Classe Java pour type_requete_justification_calcul complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_requete_justification_calcul"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numero_accreditation"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="16"/&gt;
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
    name = "type_requete_justification_calcul",
    propOrder = {"numeroAccreditation"})
public class TypeRequeteJustificationCalcul implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "numero_accreditation", required = true)
  @NotNull
  @Size(min = 16, max = 16)
  protected String numeroAccreditation;

  /**
   * Obtient la valeur de la propriété numeroAccreditation.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAccreditation() {
    return numeroAccreditation;
  }

  /**
   * Définit la valeur de la propriété numeroAccreditation.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAccreditation(String value) {
    this.numeroAccreditation = value;
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
    if (draftCopy instanceof TypeRequeteJustificationCalcul) {
      final TypeRequeteJustificationCalcul copy = ((TypeRequeteJustificationCalcul) draftCopy);
      if (this.numeroAccreditation != null) {
        String sourceNumeroAccreditation;
        sourceNumeroAccreditation = this.getNumeroAccreditation();
        String copyNumeroAccreditation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroAccreditation", sourceNumeroAccreditation),
                    sourceNumeroAccreditation));
        copy.setNumeroAccreditation(copyNumeroAccreditation);
      } else {
        copy.numeroAccreditation = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeRequeteJustificationCalcul();
  }
}
