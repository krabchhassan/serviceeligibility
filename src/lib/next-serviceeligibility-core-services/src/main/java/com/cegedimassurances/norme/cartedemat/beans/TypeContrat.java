package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
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
 * Proprietes du contrat
 *
 * <p>Classe Java pour type_contrat complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_contrat"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroAMC"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nomAMC"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelleAMC" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="debutPeriode" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="finPeriode" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="numeroAMCEchanges" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroOperateur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numero"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroAdherent"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroAdherentComplet" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroExterneContratIndividuel" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroContratCollectif" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numeroExterneContratCollectif" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="editeurCarte" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="gestionnaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="groupeAssures" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="typeConvention" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="critereSecondaireDetaille" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="critereSecondaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nomPorteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prenomPorteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="civilitePorteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dateSouscription" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="qualification"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="isContratResponsable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isContratCMU" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="situationParticuliere" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="1"/&gt;
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
    name = "type_contrat",
    propOrder = {
      "numeroAMC",
      "nomAMC",
      "libelleAMC",
      "debutPeriode",
      "finPeriode",
      "numeroAMCEchanges",
      "numeroOperateur",
      "numero",
      "numeroAdherent",
      "numeroAdherentComplet",
      "numeroExterneContratIndividuel",
      "numeroContratCollectif",
      "numeroExterneContratCollectif",
      "editeurCarte",
      "gestionnaire",
      "groupeAssures",
      "typeConvention",
      "critereSecondaireDetaille",
      "critereSecondaire",
      "nomPorteur",
      "prenomPorteur",
      "civilitePorteur",
      "dateSouscription",
      "qualification",
      "isContratResponsable",
      "isContratCMU",
      "situationParticuliere"
    })
@XmlSeeAlso({TypeContratV2.class})
public class TypeContrat implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 10)
  protected String numeroAMC;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 45)
  protected String nomAMC;

  @Size(min = 1, max = 45)
  protected String libelleAMC;

  @XmlElement(required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar debutPeriode;

  @XmlElement(required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar finPeriode;

  @Size(min = 1, max = 10)
  protected String numeroAMCEchanges;

  @Size(min = 1, max = 10)
  protected String numeroOperateur;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 20)
  protected String numero;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 8)
  protected String numeroAdherent;

  @Size(min = 1, max = 15)
  protected String numeroAdherentComplet;

  @Size(min = 1, max = 20)
  protected String numeroExterneContratIndividuel;

  @Size(min = 1, max = 20)
  protected String numeroContratCollectif;

  @Size(min = 1, max = 20)
  protected String numeroExterneContratCollectif;

  @Size(min = 1, max = 2)
  protected String editeurCarte;

  @Size(min = 1, max = 10)
  protected String gestionnaire;

  @Size(min = 1, max = 20)
  protected String groupeAssures;

  @Size(min = 1, max = 2)
  protected String typeConvention;

  @Size(min = 1, max = 15)
  protected String critereSecondaireDetaille;

  @Size(min = 1, max = 3)
  protected String critereSecondaire;

  @Size(min = 1, max = 50)
  protected String nomPorteur;

  @Size(min = 1, max = 25)
  protected String prenomPorteur;

  @Size(min = 1, max = 15)
  protected String civilitePorteur;

  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateSouscription;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 1)
  protected String qualification;

  protected Boolean isContratResponsable;
  protected Boolean isContratCMU;

  @Size(min = 1, max = 1)
  protected String situationParticuliere;

  /**
   * Obtient la valeur de la propriété numeroAMC.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMC() {
    return numeroAMC;
  }

  /**
   * Définit la valeur de la propriété numeroAMC.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMC(String value) {
    this.numeroAMC = value;
  }

  /**
   * Obtient la valeur de la propriété nomAMC.
   *
   * @return possible object is {@link String }
   */
  public String getNomAMC() {
    return nomAMC;
  }

  /**
   * Définit la valeur de la propriété nomAMC.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAMC(String value) {
    this.nomAMC = value;
  }

  /**
   * Obtient la valeur de la propriété libelleAMC.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleAMC() {
    return libelleAMC;
  }

  /**
   * Définit la valeur de la propriété libelleAMC.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleAMC(String value) {
    this.libelleAMC = value;
  }

  /**
   * Obtient la valeur de la propriété debutPeriode.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDebutPeriode() {
    return debutPeriode;
  }

  /**
   * Définit la valeur de la propriété debutPeriode.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDebutPeriode(XMLGregorianCalendar value) {
    this.debutPeriode = value;
  }

  /**
   * Obtient la valeur de la propriété finPeriode.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getFinPeriode() {
    return finPeriode;
  }

  /**
   * Définit la valeur de la propriété finPeriode.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setFinPeriode(XMLGregorianCalendar value) {
    this.finPeriode = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAMCEchanges.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMCEchanges() {
    return numeroAMCEchanges;
  }

  /**
   * Définit la valeur de la propriété numeroAMCEchanges.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMCEchanges(String value) {
    this.numeroAMCEchanges = value;
  }

  /**
   * Obtient la valeur de la propriété numeroOperateur.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroOperateur() {
    return numeroOperateur;
  }

  /**
   * Définit la valeur de la propriété numeroOperateur.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroOperateur(String value) {
    this.numeroOperateur = value;
  }

  /**
   * Obtient la valeur de la propriété numero.
   *
   * @return possible object is {@link String }
   */
  public String getNumero() {
    return numero;
  }

  /**
   * Définit la valeur de la propriété numero.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumero(String value) {
    this.numero = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAdherent.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAdherent() {
    return numeroAdherent;
  }

  /**
   * Définit la valeur de la propriété numeroAdherent.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAdherent(String value) {
    this.numeroAdherent = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAdherentComplet.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAdherentComplet() {
    return numeroAdherentComplet;
  }

  /**
   * Définit la valeur de la propriété numeroAdherentComplet.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAdherentComplet(String value) {
    this.numeroAdherentComplet = value;
  }

  /**
   * Obtient la valeur de la propriété numeroExterneContratIndividuel.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroExterneContratIndividuel() {
    return numeroExterneContratIndividuel;
  }

  /**
   * Définit la valeur de la propriété numeroExterneContratIndividuel.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroExterneContratIndividuel(String value) {
    this.numeroExterneContratIndividuel = value;
  }

  /**
   * Obtient la valeur de la propriété numeroContratCollectif.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroContratCollectif() {
    return numeroContratCollectif;
  }

  /**
   * Définit la valeur de la propriété numeroContratCollectif.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroContratCollectif(String value) {
    this.numeroContratCollectif = value;
  }

  /**
   * Obtient la valeur de la propriété numeroExterneContratCollectif.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroExterneContratCollectif() {
    return numeroExterneContratCollectif;
  }

  /**
   * Définit la valeur de la propriété numeroExterneContratCollectif.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroExterneContratCollectif(String value) {
    this.numeroExterneContratCollectif = value;
  }

  /**
   * Obtient la valeur de la propriété editeurCarte.
   *
   * @return possible object is {@link String }
   */
  public String getEditeurCarte() {
    return editeurCarte;
  }

  /**
   * Définit la valeur de la propriété editeurCarte.
   *
   * @param value allowed object is {@link String }
   */
  public void setEditeurCarte(String value) {
    this.editeurCarte = value;
  }

  /**
   * Obtient la valeur de la propriété gestionnaire.
   *
   * @return possible object is {@link String }
   */
  public String getGestionnaire() {
    return gestionnaire;
  }

  /**
   * Définit la valeur de la propriété gestionnaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setGestionnaire(String value) {
    this.gestionnaire = value;
  }

  /**
   * Obtient la valeur de la propriété groupeAssures.
   *
   * @return possible object is {@link String }
   */
  public String getGroupeAssures() {
    return groupeAssures;
  }

  /**
   * Définit la valeur de la propriété groupeAssures.
   *
   * @param value allowed object is {@link String }
   */
  public void setGroupeAssures(String value) {
    this.groupeAssures = value;
  }

  /**
   * Obtient la valeur de la propriété typeConvention.
   *
   * @return possible object is {@link String }
   */
  public String getTypeConvention() {
    return typeConvention;
  }

  /**
   * Définit la valeur de la propriété typeConvention.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeConvention(String value) {
    this.typeConvention = value;
  }

  /**
   * Obtient la valeur de la propriété critereSecondaireDetaille.
   *
   * @return possible object is {@link String }
   */
  public String getCritereSecondaireDetaille() {
    return critereSecondaireDetaille;
  }

  /**
   * Définit la valeur de la propriété critereSecondaireDetaille.
   *
   * @param value allowed object is {@link String }
   */
  public void setCritereSecondaireDetaille(String value) {
    this.critereSecondaireDetaille = value;
  }

  /**
   * Obtient la valeur de la propriété critereSecondaire.
   *
   * @return possible object is {@link String }
   */
  public String getCritereSecondaire() {
    return critereSecondaire;
  }

  /**
   * Définit la valeur de la propriété critereSecondaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setCritereSecondaire(String value) {
    this.critereSecondaire = value;
  }

  /**
   * Obtient la valeur de la propriété nomPorteur.
   *
   * @return possible object is {@link String }
   */
  public String getNomPorteur() {
    return nomPorteur;
  }

  /**
   * Définit la valeur de la propriété nomPorteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomPorteur(String value) {
    this.nomPorteur = value;
  }

  /**
   * Obtient la valeur de la propriété prenomPorteur.
   *
   * @return possible object is {@link String }
   */
  public String getPrenomPorteur() {
    return prenomPorteur;
  }

  /**
   * Définit la valeur de la propriété prenomPorteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setPrenomPorteur(String value) {
    this.prenomPorteur = value;
  }

  /**
   * Obtient la valeur de la propriété civilitePorteur.
   *
   * @return possible object is {@link String }
   */
  public String getCivilitePorteur() {
    return civilitePorteur;
  }

  /**
   * Définit la valeur de la propriété civilitePorteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setCivilitePorteur(String value) {
    this.civilitePorteur = value;
  }

  /**
   * Obtient la valeur de la propriété dateSouscription.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateSouscription() {
    return dateSouscription;
  }

  /**
   * Définit la valeur de la propriété dateSouscription.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateSouscription(XMLGregorianCalendar value) {
    this.dateSouscription = value;
  }

  /**
   * Obtient la valeur de la propriété qualification.
   *
   * @return possible object is {@link String }
   */
  public String getQualification() {
    return qualification;
  }

  /**
   * Définit la valeur de la propriété qualification.
   *
   * @param value allowed object is {@link String }
   */
  public void setQualification(String value) {
    this.qualification = value;
  }

  /**
   * Obtient la valeur de la propriété isContratResponsable.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsContratResponsable() {
    return isContratResponsable;
  }

  /**
   * Définit la valeur de la propriété isContratResponsable.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setIsContratResponsable(Boolean value) {
    this.isContratResponsable = value;
  }

  /**
   * Obtient la valeur de la propriété isContratCMU.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsContratCMU() {
    return isContratCMU;
  }

  /**
   * Définit la valeur de la propriété isContratCMU.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setIsContratCMU(Boolean value) {
    this.isContratCMU = value;
  }

  /**
   * Obtient la valeur de la propriété situationParticuliere.
   *
   * @return possible object is {@link String }
   */
  public String getSituationParticuliere() {
    return situationParticuliere;
  }

  /**
   * Définit la valeur de la propriété situationParticuliere.
   *
   * @param value allowed object is {@link String }
   */
  public void setSituationParticuliere(String value) {
    this.situationParticuliere = value;
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
    if (draftCopy instanceof TypeContrat) {
      final TypeContrat copy = ((TypeContrat) draftCopy);
      if (this.numeroAMC != null) {
        String sourceNumeroAMC;
        sourceNumeroAMC = this.getNumeroAMC();
        String copyNumeroAMC =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAMC", sourceNumeroAMC), sourceNumeroAMC));
        copy.setNumeroAMC(copyNumeroAMC);
      } else {
        copy.numeroAMC = null;
      }
      if (this.nomAMC != null) {
        String sourceNomAMC;
        sourceNomAMC = this.getNomAMC();
        String copyNomAMC =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomAMC", sourceNomAMC), sourceNomAMC));
        copy.setNomAMC(copyNomAMC);
      } else {
        copy.nomAMC = null;
      }
      if (this.libelleAMC != null) {
        String sourceLibelleAMC;
        sourceLibelleAMC = this.getLibelleAMC();
        String copyLibelleAMC =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleAMC", sourceLibelleAMC),
                    sourceLibelleAMC));
        copy.setLibelleAMC(copyLibelleAMC);
      } else {
        copy.libelleAMC = null;
      }
      if (this.debutPeriode != null) {
        XMLGregorianCalendar sourceDebutPeriode;
        sourceDebutPeriode = this.getDebutPeriode();
        XMLGregorianCalendar copyDebutPeriode =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "debutPeriode", sourceDebutPeriode),
                    sourceDebutPeriode));
        copy.setDebutPeriode(copyDebutPeriode);
      } else {
        copy.debutPeriode = null;
      }
      if (this.finPeriode != null) {
        XMLGregorianCalendar sourceFinPeriode;
        sourceFinPeriode = this.getFinPeriode();
        XMLGregorianCalendar copyFinPeriode =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "finPeriode", sourceFinPeriode),
                    sourceFinPeriode));
        copy.setFinPeriode(copyFinPeriode);
      } else {
        copy.finPeriode = null;
      }
      if (this.numeroAMCEchanges != null) {
        String sourceNumeroAMCEchanges;
        sourceNumeroAMCEchanges = this.getNumeroAMCEchanges();
        String copyNumeroAMCEchanges =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAMCEchanges", sourceNumeroAMCEchanges),
                    sourceNumeroAMCEchanges));
        copy.setNumeroAMCEchanges(copyNumeroAMCEchanges);
      } else {
        copy.numeroAMCEchanges = null;
      }
      if (this.numeroOperateur != null) {
        String sourceNumeroOperateur;
        sourceNumeroOperateur = this.getNumeroOperateur();
        String copyNumeroOperateur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroOperateur", sourceNumeroOperateur),
                    sourceNumeroOperateur));
        copy.setNumeroOperateur(copyNumeroOperateur);
      } else {
        copy.numeroOperateur = null;
      }
      if (this.numero != null) {
        String sourceNumero;
        sourceNumero = this.getNumero();
        String copyNumero =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numero", sourceNumero), sourceNumero));
        copy.setNumero(copyNumero);
      } else {
        copy.numero = null;
      }
      if (this.numeroAdherent != null) {
        String sourceNumeroAdherent;
        sourceNumeroAdherent = this.getNumeroAdherent();
        String copyNumeroAdherent =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAdherent", sourceNumeroAdherent),
                    sourceNumeroAdherent));
        copy.setNumeroAdherent(copyNumeroAdherent);
      } else {
        copy.numeroAdherent = null;
      }
      if (this.numeroAdherentComplet != null) {
        String sourceNumeroAdherentComplet;
        sourceNumeroAdherentComplet = this.getNumeroAdherentComplet();
        String copyNumeroAdherentComplet =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroAdherentComplet", sourceNumeroAdherentComplet),
                    sourceNumeroAdherentComplet));
        copy.setNumeroAdherentComplet(copyNumeroAdherentComplet);
      } else {
        copy.numeroAdherentComplet = null;
      }
      if (this.numeroExterneContratIndividuel != null) {
        String sourceNumeroExterneContratIndividuel;
        sourceNumeroExterneContratIndividuel = this.getNumeroExterneContratIndividuel();
        String copyNumeroExterneContratIndividuel =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator,
                        "numeroExterneContratIndividuel",
                        sourceNumeroExterneContratIndividuel),
                    sourceNumeroExterneContratIndividuel));
        copy.setNumeroExterneContratIndividuel(copyNumeroExterneContratIndividuel);
      } else {
        copy.numeroExterneContratIndividuel = null;
      }
      if (this.numeroContratCollectif != null) {
        String sourceNumeroContratCollectif;
        sourceNumeroContratCollectif = this.getNumeroContratCollectif();
        String copyNumeroContratCollectif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroContratCollectif", sourceNumeroContratCollectif),
                    sourceNumeroContratCollectif));
        copy.setNumeroContratCollectif(copyNumeroContratCollectif);
      } else {
        copy.numeroContratCollectif = null;
      }
      if (this.numeroExterneContratCollectif != null) {
        String sourceNumeroExterneContratCollectif;
        sourceNumeroExterneContratCollectif = this.getNumeroExterneContratCollectif();
        String copyNumeroExterneContratCollectif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator,
                        "numeroExterneContratCollectif",
                        sourceNumeroExterneContratCollectif),
                    sourceNumeroExterneContratCollectif));
        copy.setNumeroExterneContratCollectif(copyNumeroExterneContratCollectif);
      } else {
        copy.numeroExterneContratCollectif = null;
      }
      if (this.editeurCarte != null) {
        String sourceEditeurCarte;
        sourceEditeurCarte = this.getEditeurCarte();
        String copyEditeurCarte =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "editeurCarte", sourceEditeurCarte),
                    sourceEditeurCarte));
        copy.setEditeurCarte(copyEditeurCarte);
      } else {
        copy.editeurCarte = null;
      }
      if (this.gestionnaire != null) {
        String sourceGestionnaire;
        sourceGestionnaire = this.getGestionnaire();
        String copyGestionnaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "gestionnaire", sourceGestionnaire),
                    sourceGestionnaire));
        copy.setGestionnaire(copyGestionnaire);
      } else {
        copy.gestionnaire = null;
      }
      if (this.groupeAssures != null) {
        String sourceGroupeAssures;
        sourceGroupeAssures = this.getGroupeAssures();
        String copyGroupeAssures =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "groupeAssures", sourceGroupeAssures),
                    sourceGroupeAssures));
        copy.setGroupeAssures(copyGroupeAssures);
      } else {
        copy.groupeAssures = null;
      }
      if (this.typeConvention != null) {
        String sourceTypeConvention;
        sourceTypeConvention = this.getTypeConvention();
        String copyTypeConvention =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "typeConvention", sourceTypeConvention),
                    sourceTypeConvention));
        copy.setTypeConvention(copyTypeConvention);
      } else {
        copy.typeConvention = null;
      }
      if (this.critereSecondaireDetaille != null) {
        String sourceCritereSecondaireDetaille;
        sourceCritereSecondaireDetaille = this.getCritereSecondaireDetaille();
        String copyCritereSecondaireDetaille =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "critereSecondaireDetaille", sourceCritereSecondaireDetaille),
                    sourceCritereSecondaireDetaille));
        copy.setCritereSecondaireDetaille(copyCritereSecondaireDetaille);
      } else {
        copy.critereSecondaireDetaille = null;
      }
      if (this.critereSecondaire != null) {
        String sourceCritereSecondaire;
        sourceCritereSecondaire = this.getCritereSecondaire();
        String copyCritereSecondaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "critereSecondaire", sourceCritereSecondaire),
                    sourceCritereSecondaire));
        copy.setCritereSecondaire(copyCritereSecondaire);
      } else {
        copy.critereSecondaire = null;
      }
      if (this.nomPorteur != null) {
        String sourceNomPorteur;
        sourceNomPorteur = this.getNomPorteur();
        String copyNomPorteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomPorteur", sourceNomPorteur),
                    sourceNomPorteur));
        copy.setNomPorteur(copyNomPorteur);
      } else {
        copy.nomPorteur = null;
      }
      if (this.prenomPorteur != null) {
        String sourcePrenomPorteur;
        sourcePrenomPorteur = this.getPrenomPorteur();
        String copyPrenomPorteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prenomPorteur", sourcePrenomPorteur),
                    sourcePrenomPorteur));
        copy.setPrenomPorteur(copyPrenomPorteur);
      } else {
        copy.prenomPorteur = null;
      }
      if (this.civilitePorteur != null) {
        String sourceCivilitePorteur;
        sourceCivilitePorteur = this.getCivilitePorteur();
        String copyCivilitePorteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "civilitePorteur", sourceCivilitePorteur),
                    sourceCivilitePorteur));
        copy.setCivilitePorteur(copyCivilitePorteur);
      } else {
        copy.civilitePorteur = null;
      }
      if (this.dateSouscription != null) {
        XMLGregorianCalendar sourceDateSouscription;
        sourceDateSouscription = this.getDateSouscription();
        XMLGregorianCalendar copyDateSouscription =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateSouscription", sourceDateSouscription),
                    sourceDateSouscription));
        copy.setDateSouscription(copyDateSouscription);
      } else {
        copy.dateSouscription = null;
      }
      if (this.qualification != null) {
        String sourceQualification;
        sourceQualification = this.getQualification();
        String copyQualification =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "qualification", sourceQualification),
                    sourceQualification));
        copy.setQualification(copyQualification);
      } else {
        copy.qualification = null;
      }
      if (this.isContratResponsable != null) {
        Boolean sourceIsContratResponsable;
        sourceIsContratResponsable = this.isIsContratResponsable();
        Boolean copyIsContratResponsable =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "isContratResponsable", sourceIsContratResponsable),
                    sourceIsContratResponsable));
        copy.setIsContratResponsable(copyIsContratResponsable);
      } else {
        copy.isContratResponsable = null;
      }
      if (this.isContratCMU != null) {
        Boolean sourceIsContratCMU;
        sourceIsContratCMU = this.isIsContratCMU();
        Boolean copyIsContratCMU =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(locator, "isContratCMU", sourceIsContratCMU),
                    sourceIsContratCMU));
        copy.setIsContratCMU(copyIsContratCMU);
      } else {
        copy.isContratCMU = null;
      }
      if (this.situationParticuliere != null) {
        String sourceSituationParticuliere;
        sourceSituationParticuliere = this.getSituationParticuliere();
        String copySituationParticuliere =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "situationParticuliere", sourceSituationParticuliere),
                    sourceSituationParticuliere));
        copy.setSituationParticuliere(copySituationParticuliere);
      } else {
        copy.situationParticuliere = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeContrat();
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
