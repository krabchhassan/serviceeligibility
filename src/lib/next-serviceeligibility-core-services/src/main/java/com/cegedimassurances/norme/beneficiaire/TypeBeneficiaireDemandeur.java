package com.cegedimassurances.norme.beneficiaire;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
 * Classe Java pour type_beneficiaire_demandeur complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_beneficiaire_demandeur"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NIR_certifie"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cle_NIR" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-9][0-9]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_naissance" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rang_gemellaire" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="INS_C" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_beneficiaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prenom_beneficiaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="qualite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
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
    name = "type_beneficiaire_demandeur",
    propOrder = {
      "nirCertifie",
      "cleNIR",
      "dateNaissance",
      "rangGemellaire",
      "insc",
      "nomBeneficiaire",
      "prenomBeneficiaire",
      "qualite"
    })
public class TypeBeneficiaireDemandeur implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "NIR_certifie", required = true)
  @NotNull
  @Size(min = 13, max = 13)
  protected String nirCertifie;

  @XmlElement(name = "cle_NIR")
  @Pattern(regexp = "[0-9][0-9]")
  protected String cleNIR;

  @XmlElement(name = "date_naissance")
  @Size(min = 8, max = 8)
  protected String dateNaissance;

  @XmlElement(name = "rang_gemellaire", defaultValue = "1")
  @NotNull
  protected int rangGemellaire;

  @XmlElement(name = "INS_C")
  @Size(min = 25, max = 25)
  protected String insc;

  @XmlElement(name = "nom_beneficiaire")
  @Size(max = 45)
  protected String nomBeneficiaire;

  @XmlElement(name = "prenom_beneficiaire")
  @Size(max = 45)
  protected String prenomBeneficiaire;

  @Size(min = 1, max = 1)
  protected String qualite;

  /**
   * Obtient la valeur de la propriété nirCertifie.
   *
   * @return possible object is {@link String }
   */
  public String getNIRCertifie() {
    return nirCertifie;
  }

  /**
   * Définit la valeur de la propriété nirCertifie.
   *
   * @param value allowed object is {@link String }
   */
  public void setNIRCertifie(String value) {
    this.nirCertifie = value;
  }

  /**
   * Obtient la valeur de la propriété cleNIR.
   *
   * @return possible object is {@link String }
   */
  public String getCleNIR() {
    return cleNIR;
  }

  /**
   * Définit la valeur de la propriété cleNIR.
   *
   * @param value allowed object is {@link String }
   */
  public void setCleNIR(String value) {
    this.cleNIR = value;
  }

  /**
   * Obtient la valeur de la propriété dateNaissance.
   *
   * @return possible object is {@link String }
   */
  public String getDateNaissance() {
    return dateNaissance;
  }

  /**
   * Définit la valeur de la propriété dateNaissance.
   *
   * @param value allowed object is {@link String }
   */
  public void setDateNaissance(String value) {
    this.dateNaissance = value;
  }

  /** Obtient la valeur de la propriété rangGemellaire. */
  public int getRangGemellaire() {
    return rangGemellaire;
  }

  /** Définit la valeur de la propriété rangGemellaire. */
  public void setRangGemellaire(int value) {
    this.rangGemellaire = value;
  }

  /**
   * Obtient la valeur de la propriété insc.
   *
   * @return possible object is {@link String }
   */
  public String getINSC() {
    return insc;
  }

  /**
   * Définit la valeur de la propriété insc.
   *
   * @param value allowed object is {@link String }
   */
  public void setINSC(String value) {
    this.insc = value;
  }

  /**
   * Obtient la valeur de la propriété nomBeneficiaire.
   *
   * @return possible object is {@link String }
   */
  public String getNomBeneficiaire() {
    return nomBeneficiaire;
  }

  /**
   * Définit la valeur de la propriété nomBeneficiaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomBeneficiaire(String value) {
    this.nomBeneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété prenomBeneficiaire.
   *
   * @return possible object is {@link String }
   */
  public String getPrenomBeneficiaire() {
    return prenomBeneficiaire;
  }

  /**
   * Définit la valeur de la propriété prenomBeneficiaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setPrenomBeneficiaire(String value) {
    this.prenomBeneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété qualite.
   *
   * @return possible object is {@link String }
   */
  public String getQualite() {
    return qualite;
  }

  /**
   * Définit la valeur de la propriété qualite.
   *
   * @param value allowed object is {@link String }
   */
  public void setQualite(String value) {
    this.qualite = value;
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
    if (draftCopy instanceof TypeBeneficiaireDemandeur) {
      final TypeBeneficiaireDemandeur copy = ((TypeBeneficiaireDemandeur) draftCopy);
      if (this.nirCertifie != null) {
        String sourceNIRCertifie;
        sourceNIRCertifie = this.getNIRCertifie();
        String copyNIRCertifie =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirCertifie", sourceNIRCertifie),
                    sourceNIRCertifie));
        copy.setNIRCertifie(copyNIRCertifie);
      } else {
        copy.nirCertifie = null;
      }
      if (this.cleNIR != null) {
        String sourceCleNIR;
        sourceCleNIR = this.getCleNIR();
        String copyCleNIR =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNIR", sourceCleNIR), sourceCleNIR));
        copy.setCleNIR(copyCleNIR);
      } else {
        copy.cleNIR = null;
      }
      if (this.dateNaissance != null) {
        String sourceDateNaissance;
        sourceDateNaissance = this.getDateNaissance();
        String copyDateNaissance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "dateNaissance", sourceDateNaissance),
                    sourceDateNaissance));
        copy.setDateNaissance(copyDateNaissance);
      } else {
        copy.dateNaissance = null;
      }
      int sourceRangGemellaire;
      sourceRangGemellaire = (true ? this.getRangGemellaire() : 0);
      int copyRangGemellaire =
          strategy.copy(
              LocatorUtils.property(locator, "rangGemellaire", sourceRangGemellaire),
              sourceRangGemellaire);
      copy.setRangGemellaire(copyRangGemellaire);
      if (this.insc != null) {
        String sourceINSC;
        sourceINSC = this.getINSC();
        String copyINSC =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "insc", sourceINSC), sourceINSC));
        copy.setINSC(copyINSC);
      } else {
        copy.insc = null;
      }
      if (this.nomBeneficiaire != null) {
        String sourceNomBeneficiaire;
        sourceNomBeneficiaire = this.getNomBeneficiaire();
        String copyNomBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomBeneficiaire", sourceNomBeneficiaire),
                    sourceNomBeneficiaire));
        copy.setNomBeneficiaire(copyNomBeneficiaire);
      } else {
        copy.nomBeneficiaire = null;
      }
      if (this.prenomBeneficiaire != null) {
        String sourcePrenomBeneficiaire;
        sourcePrenomBeneficiaire = this.getPrenomBeneficiaire();
        String copyPrenomBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prenomBeneficiaire", sourcePrenomBeneficiaire),
                    sourcePrenomBeneficiaire));
        copy.setPrenomBeneficiaire(copyPrenomBeneficiaire);
      } else {
        copy.prenomBeneficiaire = null;
      }
      if (this.qualite != null) {
        String sourceQualite;
        sourceQualite = this.getQualite();
        String copyQualite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "qualite", sourceQualite), sourceQualite));
        copy.setQualite(copyQualite);
      } else {
        copy.qualite = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeBeneficiaireDemandeur();
  }
}
