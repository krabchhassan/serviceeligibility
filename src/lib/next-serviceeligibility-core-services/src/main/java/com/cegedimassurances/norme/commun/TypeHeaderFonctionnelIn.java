package com.cegedimassurances.norme.commun;

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
 * entete fonctionnelle
 *
 * <p>Classe Java pour type_header_fonctionnel_in complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_header_fonctionnel_in"&gt;
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
 *         &lt;element name="delivrance_accreditation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="2"/&gt;
 *               &lt;enumeration value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="delivrance_otp" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
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
 *         &lt;element name="code_proprietaire"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="8"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_gestionnaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="8"/&gt;
 *               &lt;maxLength value="14"/&gt;
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
    name = "type_header_fonctionnel_in",
    propOrder = {
      "codeContexte",
      "delivranceAccreditation",
      "delivranceOtp",
      "version",
      "idDossier",
      "numOTP",
      "codeProprietaire",
      "codeGestionnaire"
    })
public class TypeHeaderFonctionnelIn implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "code_contexte")
  @Size(min = 5, max = 5)
  protected String codeContexte;

  @XmlElement(name = "delivrance_accreditation")
  protected Integer delivranceAccreditation;

  @XmlElement(name = "delivrance_otp")
  protected Integer delivranceOtp;

  @Size(min = 1, max = 1)
  protected String version;

  @Size(max = 36)
  protected String idDossier;

  @XmlElement(name = "num_OTP")
  @Size(max = 8)
  protected String numOTP;

  @XmlElement(name = "code_proprietaire", required = true)
  @NotNull
  @Size(min = 8, max = 10)
  protected String codeProprietaire;

  @XmlElement(name = "code_gestionnaire")
  @Size(min = 8, max = 14)
  protected String codeGestionnaire;

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
   * Obtient la valeur de la propriété delivranceAccreditation.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getDelivranceAccreditation() {
    return delivranceAccreditation;
  }

  /**
   * Définit la valeur de la propriété delivranceAccreditation.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setDelivranceAccreditation(Integer value) {
    this.delivranceAccreditation = value;
  }

  /**
   * Obtient la valeur de la propriété delivranceOtp.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getDelivranceOtp() {
    return delivranceOtp;
  }

  /**
   * Définit la valeur de la propriété delivranceOtp.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setDelivranceOtp(Integer value) {
    this.delivranceOtp = value;
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
   * Obtient la valeur de la propriété codeProprietaire.
   *
   * @return possible object is {@link String }
   */
  public String getCodeProprietaire() {
    return codeProprietaire;
  }

  /**
   * Définit la valeur de la propriété codeProprietaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeProprietaire(String value) {
    this.codeProprietaire = value;
  }

  /**
   * Obtient la valeur de la propriété codeGestionnaire.
   *
   * @return possible object is {@link String }
   */
  public String getCodeGestionnaire() {
    return codeGestionnaire;
  }

  /**
   * Définit la valeur de la propriété codeGestionnaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeGestionnaire(String value) {
    this.codeGestionnaire = value;
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
    if (draftCopy instanceof TypeHeaderFonctionnelIn) {
      final TypeHeaderFonctionnelIn copy = ((TypeHeaderFonctionnelIn) draftCopy);
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
      if (this.delivranceAccreditation != null) {
        Integer sourceDelivranceAccreditation;
        sourceDelivranceAccreditation = this.getDelivranceAccreditation();
        Integer copyDelivranceAccreditation =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "delivranceAccreditation", sourceDelivranceAccreditation),
                    sourceDelivranceAccreditation));
        copy.setDelivranceAccreditation(copyDelivranceAccreditation);
      } else {
        copy.delivranceAccreditation = null;
      }
      if (this.delivranceOtp != null) {
        Integer sourceDelivranceOtp;
        sourceDelivranceOtp = this.getDelivranceOtp();
        Integer copyDelivranceOtp =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(locator, "delivranceOtp", sourceDelivranceOtp),
                    sourceDelivranceOtp));
        copy.setDelivranceOtp(copyDelivranceOtp);
      } else {
        copy.delivranceOtp = null;
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
      if (this.codeProprietaire != null) {
        String sourceCodeProprietaire;
        sourceCodeProprietaire = this.getCodeProprietaire();
        String copyCodeProprietaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeProprietaire", sourceCodeProprietaire),
                    sourceCodeProprietaire));
        copy.setCodeProprietaire(copyCodeProprietaire);
      } else {
        copy.codeProprietaire = null;
      }
      if (this.codeGestionnaire != null) {
        String sourceCodeGestionnaire;
        sourceCodeGestionnaire = this.getCodeGestionnaire();
        String copyCodeGestionnaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeGestionnaire", sourceCodeGestionnaire),
                    sourceCodeGestionnaire));
        copy.setCodeGestionnaire(copyCodeGestionnaire);
      } else {
        copy.codeGestionnaire = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderFonctionnelIn();
  }
}
