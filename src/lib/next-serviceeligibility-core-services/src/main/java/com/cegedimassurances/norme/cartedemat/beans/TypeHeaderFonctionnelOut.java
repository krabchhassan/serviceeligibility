package com.cegedimassurances.norme.cartedemat.beans;

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
 *         &lt;element name="num_accreditation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="16"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="idDossier_client" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="idDossier_serveur" minOccurs="0"&gt;
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
    propOrder = {"numAccreditation", "idDossierClient", "idDossierServeur", "numOTP"})
public class TypeHeaderFonctionnelOut implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "num_accreditation")
  @Size(max = 16)
  protected String numAccreditation;

  @XmlElement(name = "idDossier_client")
  @Size(max = 36)
  protected String idDossierClient;

  @XmlElement(name = "idDossier_serveur")
  @Size(max = 36)
  protected String idDossierServeur;

  @XmlElement(name = "num_OTP")
  @Size(max = 8)
  protected String numOTP;

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
   * Obtient la valeur de la propriété idDossierClient.
   *
   * @return possible object is {@link String }
   */
  public String getIdDossierClient() {
    return idDossierClient;
  }

  /**
   * Définit la valeur de la propriété idDossierClient.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdDossierClient(String value) {
    this.idDossierClient = value;
  }

  /**
   * Obtient la valeur de la propriété idDossierServeur.
   *
   * @return possible object is {@link String }
   */
  public String getIdDossierServeur() {
    return idDossierServeur;
  }

  /**
   * Définit la valeur de la propriété idDossierServeur.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdDossierServeur(String value) {
    this.idDossierServeur = value;
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
      if (this.idDossierClient != null) {
        String sourceIdDossierClient;
        sourceIdDossierClient = this.getIdDossierClient();
        String copyIdDossierClient =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idDossierClient", sourceIdDossierClient),
                    sourceIdDossierClient));
        copy.setIdDossierClient(copyIdDossierClient);
      } else {
        copy.idDossierClient = null;
      }
      if (this.idDossierServeur != null) {
        String sourceIdDossierServeur;
        sourceIdDossierServeur = this.getIdDossierServeur();
        String copyIdDossierServeur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idDossierServeur", sourceIdDossierServeur),
                    sourceIdDossierServeur));
        copy.setIdDossierServeur(copyIdDossierServeur);
      } else {
        copy.idDossierServeur = null;
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

  /**
   * Generates a String representation of the contents of this type. This is an extension method,
   * produced by the 'ts' xjc plugin
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
  }
}
