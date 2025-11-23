package com.cegedimassurances.norme.annulation_accreditation;

import jakarta.validation.constraints.Digits;
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
 * Classe Java pour type_reponse_annulation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_reponse_annulation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_prescription"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="9"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
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
    name = "type_reponse_annulation",
    propOrder = {"idPrescription", "numeroAccreditation"})
public class TypeReponseAnnulation implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_prescription")
  @NotNull
  @Digits(integer = 9, fraction = 0)
  protected int idPrescription;

  @XmlElement(name = "numero_accreditation", required = true)
  @NotNull
  @Size(min = 16, max = 16)
  protected String numeroAccreditation;

  /** Obtient la valeur de la propriété idPrescription. */
  public int getIdPrescription() {
    return idPrescription;
  }

  /** Définit la valeur de la propriété idPrescription. */
  public void setIdPrescription(int value) {
    this.idPrescription = value;
  }

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
    if (draftCopy instanceof TypeReponseAnnulation) {
      final TypeReponseAnnulation copy = ((TypeReponseAnnulation) draftCopy);
      int sourceIdPrescription;
      sourceIdPrescription = (true ? this.getIdPrescription() : 0);
      int copyIdPrescription =
          strategy.copy(
              LocatorUtils.property(locator, "idPrescription", sourceIdPrescription),
              sourceIdPrescription);
      copy.setIdPrescription(copyIdPrescription);
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
    return new TypeReponseAnnulation();
  }
}
