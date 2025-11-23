package com.cegedimassurances.norme.tarification;

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
 * Classe Java pour type_tarification_prescription complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_tarification_prescription"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="prestations_chiffrees" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="cle" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_tarification_prescription",
    propOrder = {"prestationsChiffrees", "cle"})
public class TypeTarificationPrescription implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "prestations_chiffrees", required = true)
  @NotNull
  protected byte[] prestationsChiffrees;

  @XmlElement(required = true)
  @NotNull
  protected byte[] cle;

  /**
   * Obtient la valeur de la propriété prestationsChiffrees.
   *
   * @return possible object is byte[]
   */
  public byte[] getPrestationsChiffrees() {
    return prestationsChiffrees;
  }

  /**
   * Définit la valeur de la propriété prestationsChiffrees.
   *
   * @param value allowed object is byte[]
   */
  public void setPrestationsChiffrees(byte[] value) {
    this.prestationsChiffrees = value;
  }

  /**
   * Obtient la valeur de la propriété cle.
   *
   * @return possible object is byte[]
   */
  public byte[] getCle() {
    return cle;
  }

  /**
   * Définit la valeur de la propriété cle.
   *
   * @param value allowed object is byte[]
   */
  public void setCle(byte[] value) {
    this.cle = value;
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
    if (draftCopy instanceof TypeTarificationPrescription) {
      final TypeTarificationPrescription copy = ((TypeTarificationPrescription) draftCopy);
      if (this.prestationsChiffrees != null) {
        byte[] sourcePrestationsChiffrees;
        sourcePrestationsChiffrees = this.getPrestationsChiffrees();
        byte[] copyPrestationsChiffrees =
            ((byte[])
                strategy.copy(
                    LocatorUtils.property(
                        locator, "prestationsChiffrees", sourcePrestationsChiffrees),
                    sourcePrestationsChiffrees));
        copy.setPrestationsChiffrees(copyPrestationsChiffrees);
      } else {
        copy.prestationsChiffrees = null;
      }
      if (this.cle != null) {
        byte[] sourceCle;
        sourceCle = this.getCle();
        byte[] copyCle =
            ((byte[]) strategy.copy(LocatorUtils.property(locator, "cle", sourceCle), sourceCle));
        copy.setCle(copyCle);
      } else {
        copy.cle = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeTarificationPrescription();
  }
}
