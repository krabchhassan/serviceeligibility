package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Proprietes du beneficiaire
 *
 * <p>Classe Java pour type_beneficiaire complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_beneficiaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nomBeneficiaire"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nomPatronymique" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nomMarital" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prenom"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="qualite"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="typeAssure" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="lienFamilial" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rangAdministratif" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nirOd1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cleNirOd1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nirOd2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cleNirOd2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nirBeneficiaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cleNirBeneficiaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dateNaissance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rangNaissance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroPersonne" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="refExternePersonne" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="regimeOD1" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="caisseOD1" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="centreOD1" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="regimeOD2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="caisseOD2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="centreOD2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="hasMedecinTraitant" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="regimeParticulier" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="isBeneficiaireACS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTeleTransmission" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="debutAffiliation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="modePaiementPrestations" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="couvertures" type="{http://norme.cegedimassurances.com/carteDemat/beans}couvertures"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_beneficiaire",
    propOrder = {
      "nomBeneficiaire",
      "nomPatronymique",
      "nomMarital",
      "prenom",
      "qualite",
      "typeAssure",
      "lienFamilial",
      "rangAdministratif",
      "nirOd1",
      "cleNirOd1",
      "nirOd2",
      "cleNirOd2",
      "nirBeneficiaire",
      "cleNirBeneficiaire",
      "dateNaissance",
      "rangNaissance",
      "numeroPersonne",
      "refExternePersonne",
      "regimeOD1",
      "caisseOD1",
      "centreOD1",
      "regimeOD2",
      "caisseOD2",
      "centreOD2",
      "hasMedecinTraitant",
      "regimeParticulier",
      "isBeneficiaireACS",
      "isTeleTransmission",
      "debutAffiliation",
      "modePaiementPrestations",
      "couvertures"
    })
public class TypeBeneficiaire implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 25)
  protected String nomBeneficiaire;

  @Size(min = 1, max = 25)
  protected String nomPatronymique;

  @Size(min = 1, max = 25)
  protected String nomMarital;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 15)
  protected String prenom;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 1)
  protected String qualite;

  @Size(min = 1, max = 10)
  protected String typeAssure;

  @Size(min = 1, max = 1)
  protected String lienFamilial;

  @Size(min = 1, max = 2)
  protected String rangAdministratif;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 13)
  protected String nirOd1;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 2)
  protected String cleNirOd1;

  @Size(min = 1, max = 13)
  protected String nirOd2;

  @Size(min = 1, max = 2)
  protected String cleNirOd2;

  @Size(min = 1, max = 13)
  protected String nirBeneficiaire;

  @Size(min = 1, max = 2)
  protected String cleNirBeneficiaire;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 8)
  protected String dateNaissance;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 1)
  protected String rangNaissance;

  @Size(min = 1, max = 15)
  protected String numeroPersonne;

  @Size(min = 1, max = 30)
  protected String refExternePersonne;

  @Size(min = 1, max = 2)
  protected String regimeOD1;

  @Size(min = 1, max = 3)
  protected String caisseOD1;

  @Size(min = 1, max = 4)
  protected String centreOD1;

  @Size(min = 1, max = 2)
  protected String regimeOD2;

  @Size(min = 1, max = 3)
  protected String caisseOD2;

  @Size(min = 1, max = 4)
  protected String centreOD2;

  protected Boolean hasMedecinTraitant;

  @Size(min = 1, max = 2)
  protected String regimeParticulier;

  protected Boolean isBeneficiaireACS;
  protected Boolean isTeleTransmission;

  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar debutAffiliation;

  @Size(min = 1, max = 2)
  protected String modePaiementPrestations;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected Couvertures couvertures;

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
   * Obtient la valeur de la propriété nomPatronymique.
   *
   * @return possible object is {@link String }
   */
  public String getNomPatronymique() {
    return nomPatronymique;
  }

  /**
   * Définit la valeur de la propriété nomPatronymique.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomPatronymique(String value) {
    this.nomPatronymique = value;
  }

  /**
   * Obtient la valeur de la propriété nomMarital.
   *
   * @return possible object is {@link String }
   */
  public String getNomMarital() {
    return nomMarital;
  }

  /**
   * Définit la valeur de la propriété nomMarital.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomMarital(String value) {
    this.nomMarital = value;
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
   * Obtient la valeur de la propriété typeAssure.
   *
   * @return possible object is {@link String }
   */
  public String getTypeAssure() {
    return typeAssure;
  }

  /**
   * Définit la valeur de la propriété typeAssure.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeAssure(String value) {
    this.typeAssure = value;
  }

  /**
   * Obtient la valeur de la propriété lienFamilial.
   *
   * @return possible object is {@link String }
   */
  public String getLienFamilial() {
    return lienFamilial;
  }

  /**
   * Définit la valeur de la propriété lienFamilial.
   *
   * @param value allowed object is {@link String }
   */
  public void setLienFamilial(String value) {
    this.lienFamilial = value;
  }

  /**
   * Obtient la valeur de la propriété rangAdministratif.
   *
   * @return possible object is {@link String }
   */
  public String getRangAdministratif() {
    return rangAdministratif;
  }

  /**
   * Définit la valeur de la propriété rangAdministratif.
   *
   * @param value allowed object is {@link String }
   */
  public void setRangAdministratif(String value) {
    this.rangAdministratif = value;
  }

  /**
   * Obtient la valeur de la propriété nirOd1.
   *
   * @return possible object is {@link String }
   */
  public String getNirOd1() {
    return nirOd1;
  }

  /**
   * Définit la valeur de la propriété nirOd1.
   *
   * @param value allowed object is {@link String }
   */
  public void setNirOd1(String value) {
    this.nirOd1 = value;
  }

  /**
   * Obtient la valeur de la propriété cleNirOd1.
   *
   * @return possible object is {@link String }
   */
  public String getCleNirOd1() {
    return cleNirOd1;
  }

  /**
   * Définit la valeur de la propriété cleNirOd1.
   *
   * @param value allowed object is {@link String }
   */
  public void setCleNirOd1(String value) {
    this.cleNirOd1 = value;
  }

  /**
   * Obtient la valeur de la propriété nirOd2.
   *
   * @return possible object is {@link String }
   */
  public String getNirOd2() {
    return nirOd2;
  }

  /**
   * Définit la valeur de la propriété nirOd2.
   *
   * @param value allowed object is {@link String }
   */
  public void setNirOd2(String value) {
    this.nirOd2 = value;
  }

  /**
   * Obtient la valeur de la propriété cleNirOd2.
   *
   * @return possible object is {@link String }
   */
  public String getCleNirOd2() {
    return cleNirOd2;
  }

  /**
   * Définit la valeur de la propriété cleNirOd2.
   *
   * @param value allowed object is {@link String }
   */
  public void setCleNirOd2(String value) {
    this.cleNirOd2 = value;
  }

  /**
   * Obtient la valeur de la propriété nirBeneficiaire.
   *
   * @return possible object is {@link String }
   */
  public String getNirBeneficiaire() {
    return nirBeneficiaire;
  }

  /**
   * Définit la valeur de la propriété nirBeneficiaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setNirBeneficiaire(String value) {
    this.nirBeneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété cleNirBeneficiaire.
   *
   * @return possible object is {@link String }
   */
  public String getCleNirBeneficiaire() {
    return cleNirBeneficiaire;
  }

  /**
   * Définit la valeur de la propriété cleNirBeneficiaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setCleNirBeneficiaire(String value) {
    this.cleNirBeneficiaire = value;
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

  /**
   * Obtient la valeur de la propriété rangNaissance.
   *
   * @return possible object is {@link String }
   */
  public String getRangNaissance() {
    return rangNaissance;
  }

  /**
   * Définit la valeur de la propriété rangNaissance.
   *
   * @param value allowed object is {@link String }
   */
  public void setRangNaissance(String value) {
    this.rangNaissance = value;
  }

  /**
   * Obtient la valeur de la propriété numeroPersonne.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroPersonne() {
    return numeroPersonne;
  }

  /**
   * Définit la valeur de la propriété numeroPersonne.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroPersonne(String value) {
    this.numeroPersonne = value;
  }

  /**
   * Obtient la valeur de la propriété refExternePersonne.
   *
   * @return possible object is {@link String }
   */
  public String getRefExternePersonne() {
    return refExternePersonne;
  }

  /**
   * Définit la valeur de la propriété refExternePersonne.
   *
   * @param value allowed object is {@link String }
   */
  public void setRefExternePersonne(String value) {
    this.refExternePersonne = value;
  }

  /**
   * Obtient la valeur de la propriété regimeOD1.
   *
   * @return possible object is {@link String }
   */
  public String getRegimeOD1() {
    return regimeOD1;
  }

  /**
   * Définit la valeur de la propriété regimeOD1.
   *
   * @param value allowed object is {@link String }
   */
  public void setRegimeOD1(String value) {
    this.regimeOD1 = value;
  }

  /**
   * Obtient la valeur de la propriété caisseOD1.
   *
   * @return possible object is {@link String }
   */
  public String getCaisseOD1() {
    return caisseOD1;
  }

  /**
   * Définit la valeur de la propriété caisseOD1.
   *
   * @param value allowed object is {@link String }
   */
  public void setCaisseOD1(String value) {
    this.caisseOD1 = value;
  }

  /**
   * Obtient la valeur de la propriété centreOD1.
   *
   * @return possible object is {@link String }
   */
  public String getCentreOD1() {
    return centreOD1;
  }

  /**
   * Définit la valeur de la propriété centreOD1.
   *
   * @param value allowed object is {@link String }
   */
  public void setCentreOD1(String value) {
    this.centreOD1 = value;
  }

  /**
   * Obtient la valeur de la propriété regimeOD2.
   *
   * @return possible object is {@link String }
   */
  public String getRegimeOD2() {
    return regimeOD2;
  }

  /**
   * Définit la valeur de la propriété regimeOD2.
   *
   * @param value allowed object is {@link String }
   */
  public void setRegimeOD2(String value) {
    this.regimeOD2 = value;
  }

  /**
   * Obtient la valeur de la propriété caisseOD2.
   *
   * @return possible object is {@link String }
   */
  public String getCaisseOD2() {
    return caisseOD2;
  }

  /**
   * Définit la valeur de la propriété caisseOD2.
   *
   * @param value allowed object is {@link String }
   */
  public void setCaisseOD2(String value) {
    this.caisseOD2 = value;
  }

  /**
   * Obtient la valeur de la propriété centreOD2.
   *
   * @return possible object is {@link String }
   */
  public String getCentreOD2() {
    return centreOD2;
  }

  /**
   * Définit la valeur de la propriété centreOD2.
   *
   * @param value allowed object is {@link String }
   */
  public void setCentreOD2(String value) {
    this.centreOD2 = value;
  }

  /**
   * Obtient la valeur de la propriété hasMedecinTraitant.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isHasMedecinTraitant() {
    return hasMedecinTraitant;
  }

  /**
   * Définit la valeur de la propriété hasMedecinTraitant.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setHasMedecinTraitant(Boolean value) {
    this.hasMedecinTraitant = value;
  }

  /**
   * Obtient la valeur de la propriété regimeParticulier.
   *
   * @return possible object is {@link String }
   */
  public String getRegimeParticulier() {
    return regimeParticulier;
  }

  /**
   * Définit la valeur de la propriété regimeParticulier.
   *
   * @param value allowed object is {@link String }
   */
  public void setRegimeParticulier(String value) {
    this.regimeParticulier = value;
  }

  /**
   * Obtient la valeur de la propriété isBeneficiaireACS.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsBeneficiaireACS() {
    return isBeneficiaireACS;
  }

  /**
   * Définit la valeur de la propriété isBeneficiaireACS.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setIsBeneficiaireACS(Boolean value) {
    this.isBeneficiaireACS = value;
  }

  /**
   * Obtient la valeur de la propriété isTeleTransmission.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsTeleTransmission() {
    return isTeleTransmission;
  }

  /**
   * Définit la valeur de la propriété isTeleTransmission.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setIsTeleTransmission(Boolean value) {
    this.isTeleTransmission = value;
  }

  /**
   * Obtient la valeur de la propriété debutAffiliation.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDebutAffiliation() {
    return debutAffiliation;
  }

  /**
   * Définit la valeur de la propriété debutAffiliation.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDebutAffiliation(XMLGregorianCalendar value) {
    this.debutAffiliation = value;
  }

  /**
   * Obtient la valeur de la propriété modePaiementPrestations.
   *
   * @return possible object is {@link String }
   */
  public String getModePaiementPrestations() {
    return modePaiementPrestations;
  }

  /**
   * Définit la valeur de la propriété modePaiementPrestations.
   *
   * @param value allowed object is {@link String }
   */
  public void setModePaiementPrestations(String value) {
    this.modePaiementPrestations = value;
  }

  /**
   * Obtient la valeur de la propriété couvertures.
   *
   * @return possible object is {@link Couvertures }
   */
  public Couvertures getCouvertures() {
    return couvertures;
  }

  /**
   * Définit la valeur de la propriété couvertures.
   *
   * @param value allowed object is {@link Couvertures }
   */
  public void setCouvertures(Couvertures value) {
    this.couvertures = value;
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
    if (draftCopy instanceof TypeBeneficiaire) {
      final TypeBeneficiaire copy = ((TypeBeneficiaire) draftCopy);
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
      if (this.nomPatronymique != null) {
        String sourceNomPatronymique;
        sourceNomPatronymique = this.getNomPatronymique();
        String copyNomPatronymique =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomPatronymique", sourceNomPatronymique),
                    sourceNomPatronymique));
        copy.setNomPatronymique(copyNomPatronymique);
      } else {
        copy.nomPatronymique = null;
      }
      if (this.nomMarital != null) {
        String sourceNomMarital;
        sourceNomMarital = this.getNomMarital();
        String copyNomMarital =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomMarital", sourceNomMarital),
                    sourceNomMarital));
        copy.setNomMarital(copyNomMarital);
      } else {
        copy.nomMarital = null;
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
      if (this.typeAssure != null) {
        String sourceTypeAssure;
        sourceTypeAssure = this.getTypeAssure();
        String copyTypeAssure =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "typeAssure", sourceTypeAssure),
                    sourceTypeAssure));
        copy.setTypeAssure(copyTypeAssure);
      } else {
        copy.typeAssure = null;
      }
      if (this.lienFamilial != null) {
        String sourceLienFamilial;
        sourceLienFamilial = this.getLienFamilial();
        String copyLienFamilial =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "lienFamilial", sourceLienFamilial),
                    sourceLienFamilial));
        copy.setLienFamilial(copyLienFamilial);
      } else {
        copy.lienFamilial = null;
      }
      if (this.rangAdministratif != null) {
        String sourceRangAdministratif;
        sourceRangAdministratif = this.getRangAdministratif();
        String copyRangAdministratif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "rangAdministratif", sourceRangAdministratif),
                    sourceRangAdministratif));
        copy.setRangAdministratif(copyRangAdministratif);
      } else {
        copy.rangAdministratif = null;
      }
      if (this.nirOd1 != null) {
        String sourceNirOd1;
        sourceNirOd1 = this.getNirOd1();
        String copyNirOd1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirOd1", sourceNirOd1), sourceNirOd1));
        copy.setNirOd1(copyNirOd1);
      } else {
        copy.nirOd1 = null;
      }
      if (this.cleNirOd1 != null) {
        String sourceCleNirOd1;
        sourceCleNirOd1 = this.getCleNirOd1();
        String copyCleNirOd1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirOd1", sourceCleNirOd1), sourceCleNirOd1));
        copy.setCleNirOd1(copyCleNirOd1);
      } else {
        copy.cleNirOd1 = null;
      }
      if (this.nirOd2 != null) {
        String sourceNirOd2;
        sourceNirOd2 = this.getNirOd2();
        String copyNirOd2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirOd2", sourceNirOd2), sourceNirOd2));
        copy.setNirOd2(copyNirOd2);
      } else {
        copy.nirOd2 = null;
      }
      if (this.cleNirOd2 != null) {
        String sourceCleNirOd2;
        sourceCleNirOd2 = this.getCleNirOd2();
        String copyCleNirOd2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirOd2", sourceCleNirOd2), sourceCleNirOd2));
        copy.setCleNirOd2(copyCleNirOd2);
      } else {
        copy.cleNirOd2 = null;
      }
      if (this.nirBeneficiaire != null) {
        String sourceNirBeneficiaire;
        sourceNirBeneficiaire = this.getNirBeneficiaire();
        String copyNirBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirBeneficiaire", sourceNirBeneficiaire),
                    sourceNirBeneficiaire));
        copy.setNirBeneficiaire(copyNirBeneficiaire);
      } else {
        copy.nirBeneficiaire = null;
      }
      if (this.cleNirBeneficiaire != null) {
        String sourceCleNirBeneficiaire;
        sourceCleNirBeneficiaire = this.getCleNirBeneficiaire();
        String copyCleNirBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirBeneficiaire", sourceCleNirBeneficiaire),
                    sourceCleNirBeneficiaire));
        copy.setCleNirBeneficiaire(copyCleNirBeneficiaire);
      } else {
        copy.cleNirBeneficiaire = null;
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
      if (this.rangNaissance != null) {
        String sourceRangNaissance;
        sourceRangNaissance = this.getRangNaissance();
        String copyRangNaissance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "rangNaissance", sourceRangNaissance),
                    sourceRangNaissance));
        copy.setRangNaissance(copyRangNaissance);
      } else {
        copy.rangNaissance = null;
      }
      if (this.numeroPersonne != null) {
        String sourceNumeroPersonne;
        sourceNumeroPersonne = this.getNumeroPersonne();
        String copyNumeroPersonne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroPersonne", sourceNumeroPersonne),
                    sourceNumeroPersonne));
        copy.setNumeroPersonne(copyNumeroPersonne);
      } else {
        copy.numeroPersonne = null;
      }
      if (this.refExternePersonne != null) {
        String sourceRefExternePersonne;
        sourceRefExternePersonne = this.getRefExternePersonne();
        String copyRefExternePersonne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "refExternePersonne", sourceRefExternePersonne),
                    sourceRefExternePersonne));
        copy.setRefExternePersonne(copyRefExternePersonne);
      } else {
        copy.refExternePersonne = null;
      }
      if (this.regimeOD1 != null) {
        String sourceRegimeOD1;
        sourceRegimeOD1 = this.getRegimeOD1();
        String copyRegimeOD1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regimeOD1", sourceRegimeOD1), sourceRegimeOD1));
        copy.setRegimeOD1(copyRegimeOD1);
      } else {
        copy.regimeOD1 = null;
      }
      if (this.caisseOD1 != null) {
        String sourceCaisseOD1;
        sourceCaisseOD1 = this.getCaisseOD1();
        String copyCaisseOD1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "caisseOD1", sourceCaisseOD1), sourceCaisseOD1));
        copy.setCaisseOD1(copyCaisseOD1);
      } else {
        copy.caisseOD1 = null;
      }
      if (this.centreOD1 != null) {
        String sourceCentreOD1;
        sourceCentreOD1 = this.getCentreOD1();
        String copyCentreOD1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "centreOD1", sourceCentreOD1), sourceCentreOD1));
        copy.setCentreOD1(copyCentreOD1);
      } else {
        copy.centreOD1 = null;
      }
      if (this.regimeOD2 != null) {
        String sourceRegimeOD2;
        sourceRegimeOD2 = this.getRegimeOD2();
        String copyRegimeOD2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regimeOD2", sourceRegimeOD2), sourceRegimeOD2));
        copy.setRegimeOD2(copyRegimeOD2);
      } else {
        copy.regimeOD2 = null;
      }
      if (this.caisseOD2 != null) {
        String sourceCaisseOD2;
        sourceCaisseOD2 = this.getCaisseOD2();
        String copyCaisseOD2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "caisseOD2", sourceCaisseOD2), sourceCaisseOD2));
        copy.setCaisseOD2(copyCaisseOD2);
      } else {
        copy.caisseOD2 = null;
      }
      if (this.centreOD2 != null) {
        String sourceCentreOD2;
        sourceCentreOD2 = this.getCentreOD2();
        String copyCentreOD2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "centreOD2", sourceCentreOD2), sourceCentreOD2));
        copy.setCentreOD2(copyCentreOD2);
      } else {
        copy.centreOD2 = null;
      }
      if (this.hasMedecinTraitant != null) {
        Boolean sourceHasMedecinTraitant;
        sourceHasMedecinTraitant = this.isHasMedecinTraitant();
        Boolean copyHasMedecinTraitant =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(locator, "hasMedecinTraitant", sourceHasMedecinTraitant),
                    sourceHasMedecinTraitant));
        copy.setHasMedecinTraitant(copyHasMedecinTraitant);
      } else {
        copy.hasMedecinTraitant = null;
      }
      if (this.regimeParticulier != null) {
        String sourceRegimeParticulier;
        sourceRegimeParticulier = this.getRegimeParticulier();
        String copyRegimeParticulier =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regimeParticulier", sourceRegimeParticulier),
                    sourceRegimeParticulier));
        copy.setRegimeParticulier(copyRegimeParticulier);
      } else {
        copy.regimeParticulier = null;
      }
      if (this.isBeneficiaireACS != null) {
        Boolean sourceIsBeneficiaireACS;
        sourceIsBeneficiaireACS = this.isIsBeneficiaireACS();
        Boolean copyIsBeneficiaireACS =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(locator, "isBeneficiaireACS", sourceIsBeneficiaireACS),
                    sourceIsBeneficiaireACS));
        copy.setIsBeneficiaireACS(copyIsBeneficiaireACS);
      } else {
        copy.isBeneficiaireACS = null;
      }
      if (this.isTeleTransmission != null) {
        Boolean sourceIsTeleTransmission;
        sourceIsTeleTransmission = this.isIsTeleTransmission();
        Boolean copyIsTeleTransmission =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(locator, "isTeleTransmission", sourceIsTeleTransmission),
                    sourceIsTeleTransmission));
        copy.setIsTeleTransmission(copyIsTeleTransmission);
      } else {
        copy.isTeleTransmission = null;
      }
      if (this.debutAffiliation != null) {
        XMLGregorianCalendar sourceDebutAffiliation;
        sourceDebutAffiliation = this.getDebutAffiliation();
        XMLGregorianCalendar copyDebutAffiliation =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "debutAffiliation", sourceDebutAffiliation),
                    sourceDebutAffiliation));
        copy.setDebutAffiliation(copyDebutAffiliation);
      } else {
        copy.debutAffiliation = null;
      }
      if (this.modePaiementPrestations != null) {
        String sourceModePaiementPrestations;
        sourceModePaiementPrestations = this.getModePaiementPrestations();
        String copyModePaiementPrestations =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "modePaiementPrestations", sourceModePaiementPrestations),
                    sourceModePaiementPrestations));
        copy.setModePaiementPrestations(copyModePaiementPrestations);
      } else {
        copy.modePaiementPrestations = null;
      }
      if (this.couvertures != null) {
        Couvertures sourceCouvertures;
        sourceCouvertures = this.getCouvertures();
        Couvertures copyCouvertures =
            ((Couvertures)
                strategy.copy(
                    LocatorUtils.property(locator, "couvertures", sourceCouvertures),
                    sourceCouvertures));
        copy.setCouvertures(copyCouvertures);
      } else {
        copy.couvertures = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeBeneficiaire();
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
