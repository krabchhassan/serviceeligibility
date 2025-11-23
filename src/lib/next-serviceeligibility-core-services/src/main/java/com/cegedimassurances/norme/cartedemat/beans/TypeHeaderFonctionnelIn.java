package com.cegedimassurances.norme.cartedemat.beans;

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
 *               &lt;maxLength value="14"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="num_accreditation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="16"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="num_otp" minOccurs="0"&gt;
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
    name = "type_header_fonctionnel_in",
    propOrder = {
      "idDossierClient",
      "idDossierServeur",
      "codeProprietaire",
      "codeGestionnaire",
      "numAccreditation",
      "numOtp"
    })
public class TypeHeaderFonctionnelIn implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "idDossier_client")
  @Size(max = 36)
  protected String idDossierClient;

  @XmlElement(name = "idDossier_serveur")
  @Size(max = 36)
  protected String idDossierServeur;

  @XmlElement(name = "code_proprietaire", required = true)
  @NotNull
  @Size(min = 8, max = 10)
  protected String codeProprietaire;

  @XmlElement(name = "code_gestionnaire")
  @Size(max = 14)
  protected String codeGestionnaire;

  @XmlElement(name = "num_accreditation")
  @Size(max = 16)
  protected String numAccreditation;

  @XmlElement(name = "num_otp")
  @Size(max = 8)
  protected String numOtp;

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
   * Obtient la valeur de la propriété numOtp.
   *
   * @return possible object is {@link String }
   */
  public String getNumOtp() {
    return numOtp;
  }

  /**
   * Définit la valeur de la propriété numOtp.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumOtp(String value) {
    this.numOtp = value;
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
      if (this.numOtp != null) {
        String sourceNumOtp;
        sourceNumOtp = this.getNumOtp();
        String copyNumOtp =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numOtp", sourceNumOtp), sourceNumOtp));
        copy.setNumOtp(copyNumOtp);
      } else {
        copy.numOtp = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderFonctionnelIn();
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
