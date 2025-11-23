package com.cegedimassurances.norme.ps;

import jakarta.validation.Valid;
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
 * Classe Java pour type_ps_demandeur complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_ps_demandeur"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identification_PS" type="{http://norme.cegedimassurances.com/ps}type_ps_identifiant"/&gt;
 *         &lt;element name="nom" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prenom" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_specialite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_activite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_postal" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="5"/&gt;
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
    name = "type_ps_demandeur",
    propOrder = {
      "identificationPS",
      "nom",
      "prenom",
      "codeSpecialite",
      "codeActivite",
      "codePostal"
    })
public class TypePsDemandeur implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "identification_PS", required = true)
  @NotNull
  @Valid
  protected TypePsIdentifiant identificationPS;

  @Size(max = 45)
  protected String nom;

  @Size(max = 45)
  protected String prenom;

  @XmlElement(name = "code_specialite")
  @Size(min = 2, max = 2)
  protected String codeSpecialite;

  @XmlElement(name = "code_activite")
  @Size(max = 10)
  protected String codeActivite;

  @XmlElement(name = "code_postal")
  @Size(max = 5)
  protected String codePostal;

  /**
   * Obtient la valeur de la propriété identificationPS.
   *
   * @return possible object is {@link TypePsIdentifiant }
   */
  public TypePsIdentifiant getIdentificationPS() {
    return identificationPS;
  }

  /**
   * Définit la valeur de la propriété identificationPS.
   *
   * @param value allowed object is {@link TypePsIdentifiant }
   */
  public void setIdentificationPS(TypePsIdentifiant value) {
    this.identificationPS = value;
  }

  /**
   * Obtient la valeur de la propriété nom.
   *
   * @return possible object is {@link String }
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit la valeur de la propriété nom.
   *
   * @param value allowed object is {@link String }
   */
  public void setNom(String value) {
    this.nom = value;
  }

  /**
   * Obtient la valeur de la propriété prenom.
   *
   * @return possible object is {@link String }
   */
  public String getPrenom() {
    return prenom;
  }

  /**
   * Définit la valeur de la propriété prenom.
   *
   * @param value allowed object is {@link String }
   */
  public void setPrenom(String value) {
    this.prenom = value;
  }

  /**
   * Obtient la valeur de la propriété codeSpecialite.
   *
   * @return possible object is {@link String }
   */
  public String getCodeSpecialite() {
    return codeSpecialite;
  }

  /**
   * Définit la valeur de la propriété codeSpecialite.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeSpecialite(String value) {
    this.codeSpecialite = value;
  }

  /**
   * Obtient la valeur de la propriété codeActivite.
   *
   * @return possible object is {@link String }
   */
  public String getCodeActivite() {
    return codeActivite;
  }

  /**
   * Définit la valeur de la propriété codeActivite.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeActivite(String value) {
    this.codeActivite = value;
  }

  /**
   * Obtient la valeur de la propriété codePostal.
   *
   * @return possible object is {@link String }
   */
  public String getCodePostal() {
    return codePostal;
  }

  /**
   * Définit la valeur de la propriété codePostal.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodePostal(String value) {
    this.codePostal = value;
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
    if (draftCopy instanceof TypePsDemandeur) {
      final TypePsDemandeur copy = ((TypePsDemandeur) draftCopy);
      if (this.identificationPS != null) {
        TypePsIdentifiant sourceIdentificationPS;
        sourceIdentificationPS = this.getIdentificationPS();
        TypePsIdentifiant copyIdentificationPS =
            ((TypePsIdentifiant)
                strategy.copy(
                    LocatorUtils.property(locator, "identificationPS", sourceIdentificationPS),
                    sourceIdentificationPS));
        copy.setIdentificationPS(copyIdentificationPS);
      } else {
        copy.identificationPS = null;
      }
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
      }
      if (this.prenom != null) {
        String sourcePrenom;
        sourcePrenom = this.getPrenom();
        String copyPrenom =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prenom", sourcePrenom), sourcePrenom));
        copy.setPrenom(copyPrenom);
      } else {
        copy.prenom = null;
      }
      if (this.codeSpecialite != null) {
        String sourceCodeSpecialite;
        sourceCodeSpecialite = this.getCodeSpecialite();
        String copyCodeSpecialite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeSpecialite", sourceCodeSpecialite),
                    sourceCodeSpecialite));
        copy.setCodeSpecialite(copyCodeSpecialite);
      } else {
        copy.codeSpecialite = null;
      }
      if (this.codeActivite != null) {
        String sourceCodeActivite;
        sourceCodeActivite = this.getCodeActivite();
        String copyCodeActivite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeActivite", sourceCodeActivite),
                    sourceCodeActivite));
        copy.setCodeActivite(copyCodeActivite);
      } else {
        copy.codeActivite = null;
      }
      if (this.codePostal != null) {
        String sourceCodePostal;
        sourceCodePostal = this.getCodePostal();
        String copyCodePostal =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codePostal", sourceCodePostal),
                    sourceCodePostal));
        copy.setCodePostal(copyCodePostal);
      } else {
        copy.codePostal = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypePsDemandeur();
  }
}
