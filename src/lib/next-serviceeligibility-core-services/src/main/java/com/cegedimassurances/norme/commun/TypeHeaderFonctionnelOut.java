package com.cegedimassurances.norme.commun;

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
 * entete fonctionnelle
 *
 * <p>Classe Java pour type_header_fonctionnel_out complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_header_fonctionnel_out"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code_contexte" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="version" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="num_accreditation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="16"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="idDossier" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="num_OTP" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
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
    name = "type_header_fonctionnel_out",
    propOrder = {"codeContexte", "version", "numAccreditation", "idDossier", "numOTP"})
public class TypeHeaderFonctionnelOut implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "code_contexte")
  @Size(min = 5, max = 5)
  protected String codeContexte;

  @Size(min = 1, max = 1)
  protected String version;

  @XmlElement(name = "num_accreditation")
  @Size(min = 16, max = 16)
  protected String numAccreditation;

  @Size(max = 36)
  protected String idDossier;

  @XmlElement(name = "num_OTP")
  @Size(max = 8)
  protected String numOTP;

  /**
   * Obtient la valeur de la propriété codeContexte.
   *
   * @return possible object is {@link String }
   */
  public String getCodeContexte() {
    return codeContexte;
  }

  /**
   * Définit la valeur de la propriété codeContexte.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeContexte(String value) {
    this.codeContexte = value;
  }

  /**
   * Obtient la valeur de la propriété version.
   *
   * @return possible object is {@link String }
   */
  public String getVersion() {
    return version;
  }

  /**
   * Définit la valeur de la propriété version.
   *
   * @param value allowed object is {@link String }
   */
  public void setVersion(String value) {
    this.version = value;
  }

  /**
   * Obtient la valeur de la propriété numAccreditation.
   *
   * @return possible object is {@link String }
   */
  public String getNumAccreditation() {
    return numAccreditation;
  }

  /**
   * Définit la valeur de la propriété numAccreditation.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumAccreditation(String value) {
    this.numAccreditation = value;
  }

  /**
   * Obtient la valeur de la propriété idDossier.
   *
   * @return possible object is {@link String }
   */
  public String getIdDossier() {
    return idDossier;
  }

  /**
   * Définit la valeur de la propriété idDossier.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdDossier(String value) {
    this.idDossier = value;
  }

  /**
   * Obtient la valeur de la propriété numOTP.
   *
   * @return possible object is {@link String }
   */
  public String getNumOTP() {
    return numOTP;
  }

  /**
   * Définit la valeur de la propriété numOTP.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumOTP(String value) {
    this.numOTP = value;
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
    if (draftCopy instanceof TypeHeaderFonctionnelOut) {
      final TypeHeaderFonctionnelOut copy = ((TypeHeaderFonctionnelOut) draftCopy);
      if (this.codeContexte != null) {
        String sourceCodeContexte;
        sourceCodeContexte = this.getCodeContexte();
        String copyCodeContexte =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeContexte", sourceCodeContexte),
                    sourceCodeContexte));
        copy.setCodeContexte(copyCodeContexte);
      } else {
        copy.codeContexte = null;
      }
      if (this.version != null) {
        String sourceVersion;
        sourceVersion = this.getVersion();
        String copyVersion =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "version", sourceVersion), sourceVersion));
        copy.setVersion(copyVersion);
      } else {
        copy.version = null;
      }
      if (this.numAccreditation != null) {
        String sourceNumAccreditation;
        sourceNumAccreditation = this.getNumAccreditation();
        String copyNumAccreditation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numAccreditation", sourceNumAccreditation),
                    sourceNumAccreditation));
        copy.setNumAccreditation(copyNumAccreditation);
      } else {
        copy.numAccreditation = null;
      }
      if (this.idDossier != null) {
        String sourceIdDossier;
        sourceIdDossier = this.getIdDossier();
        String copyIdDossier =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idDossier", sourceIdDossier), sourceIdDossier));
        copy.setIdDossier(copyIdDossier);
      } else {
        copy.idDossier = null;
      }
      if (this.numOTP != null) {
        String sourceNumOTP;
        sourceNumOTP = this.getNumOTP();
        String copyNumOTP =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numOTP", sourceNumOTP), sourceNumOTP));
        copy.setNumOTP(copyNumOTP);
      } else {
        copy.numOTP = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderFonctionnelOut();
  }
}
