package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.*;
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
 * Classe Java pour type_contrat complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_contrat"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numero" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_souscription" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="date_resiliation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="type" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_porteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prenom_porteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="civilite_porteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numero_adherent" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="qualification" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numero_contrat_collectif" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rang_administratif" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="is_contrat_responsable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="is_contrat_CMU" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="destinataire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="individuel_ou_collectif" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="situation_debut" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="situationfin" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="motif_fin_situation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="lien_familial"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="categorie_sociale" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="12"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="situation_particuliere" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
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
      "numero",
      "dateSouscription",
      "dateResiliation",
      "type",
      "nomPorteur",
      "prenomPorteur",
      "civilitePorteur",
      "numeroAdherent",
      "qualification",
      "numeroContratCollectif",
      "rangAdministratif",
      "isContratResponsable",
      "isContratCMU",
      "destinataire",
      "individuelOuCollectif",
      "situationDebut",
      "situationfin",
      "motifFinSituation",
      "lienFamilial",
      "categorieSociale",
      "situationParticuliere"
    })
public class TypeContrat implements Serializable, Cloneable, CopyTo {

  @Size(max = 45)
  protected String numero;

  @XmlElement(name = "date_souscription", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateSouscription;

  @XmlElement(name = "date_resiliation")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateResiliation;

  @Size(max = 15)
  protected String type;

  @XmlElement(name = "nom_porteur")
  @Size(max = 50)
  protected String nomPorteur;

  @XmlElement(name = "prenom_porteur")
  @Size(max = 50)
  protected String prenomPorteur;

  @XmlElement(name = "civilite_porteur")
  @Size(max = 45)
  protected String civilitePorteur;

  @XmlElement(name = "numero_adherent")
  @Size(max = 15)
  protected String numeroAdherent;

  @Size(max = 50)
  protected String qualification;

  @XmlElement(name = "numero_contrat_collectif")
  @Size(max = 45)
  protected String numeroContratCollectif;

  @XmlElement(name = "rang_administratif")
  @Size(max = 2)
  protected String rangAdministratif;

  @XmlElement(name = "is_contrat_responsable")
  protected Boolean isContratResponsable;

  @XmlElement(name = "is_contrat_CMU")
  protected Boolean isContratCMU;

  @Size(max = 45)
  protected String destinataire;

  @XmlElement(name = "individuel_ou_collectif")
  @Size(max = 2)
  protected String individuelOuCollectif;

  @XmlElement(name = "situation_debut")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar situationDebut;

  @XmlElement(name = "situation_fin")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar situationfin;

  @XmlElement(name = "motif_fin_situation")
  @Size(max = 45)
  protected String motifFinSituation;

  @XmlElement(name = "lien_familial", required = true)
  @NotNull
  @Size(max = 10)
  protected String lienFamilial;

  @XmlElement(name = "categorie_sociale")
  @Size(max = 12)
  protected String categorieSociale;

  @XmlElement(name = "situation_particuliere")
  @Size(max = 4)
  protected String situationParticuliere;

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
   * Obtient la valeur de la propriété dateResiliation.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateResiliation() {
    return dateResiliation;
  }

  /**
   * Définit la valeur de la propriété dateResiliation.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateResiliation(XMLGregorianCalendar value) {
    this.dateResiliation = value;
  }

  /**
   * Obtient la valeur de la propriété type.
   *
   * @return possible object is {@link String }
   */
  public String getType() {
    return type;
  }

  /**
   * Définit la valeur de la propriété type.
   *
   * @param value allowed object is {@link String }
   */
  public void setType(String value) {
    this.type = value;
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
   * Obtient la valeur de la propriété isContratResponsable.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsContratResponsable() {
    return isContratResponsable;
  }

  public Boolean getIsContratResponsable() {
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

  public Boolean getIsContratCMU() {
    // this code should be added even though it is generated ( somebody needs to
    // look at how to fix JAXB to generated this field with get instead of is
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
   * Obtient la valeur de la propriété destinataire.
   *
   * @return possible object is {@link String }
   */
  public String getDestinataire() {
    return destinataire;
  }

  /**
   * Définit la valeur de la propriété destinataire.
   *
   * @param value allowed object is {@link String }
   */
  public void setDestinataire(String value) {
    this.destinataire = value;
  }

  /**
   * Obtient la valeur de la propriété individuelOuCollectif.
   *
   * @return possible object is {@link String }
   */
  public String getIndividuelOuCollectif() {
    return individuelOuCollectif;
  }

  /**
   * Définit la valeur de la propriété individuelOuCollectif.
   *
   * @param value allowed object is {@link String }
   */
  public void setIndividuelOuCollectif(String value) {
    this.individuelOuCollectif = value;
  }

  /**
   * Obtient la valeur de la propriété situationDebut.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getSituationDebut() {
    return situationDebut;
  }

  /**
   * Définit la valeur de la propriété situationDebut.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setSituationDebut(XMLGregorianCalendar value) {
    this.situationDebut = value;
  }

  /**
   * Obtient la valeur de la propriété situationfin.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getSituationfin() {
    return situationfin;
  }

  /**
   * Définit la valeur de la propriété situationfin.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setSituationfin(XMLGregorianCalendar value) {
    this.situationfin = value;
  }

  /**
   * Obtient la valeur de la propriété motifFinSituation.
   *
   * @return possible object is {@link String }
   */
  public String getMotifFinSituation() {
    return motifFinSituation;
  }

  /**
   * Définit la valeur de la propriété motifFinSituation.
   *
   * @param value allowed object is {@link String }
   */
  public void setMotifFinSituation(String value) {
    this.motifFinSituation = value;
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
   * Obtient la valeur de la propriété categorieSociale.
   *
   * @return possible object is {@link String }
   */
  public String getCategorieSociale() {
    return categorieSociale;
  }

  /**
   * Définit la valeur de la propriété categorieSociale.
   *
   * @param value allowed object is {@link String }
   */
  public void setCategorieSociale(String value) {
    this.categorieSociale = value;
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
    if (draftCopy instanceof TypeContrat) {
      final TypeContrat copy = ((TypeContrat) draftCopy);
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
      if (this.dateResiliation != null) {
        XMLGregorianCalendar sourceDateResiliation;
        sourceDateResiliation = this.getDateResiliation();
        XMLGregorianCalendar copyDateResiliation =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateResiliation", sourceDateResiliation),
                    sourceDateResiliation));
        copy.setDateResiliation(copyDateResiliation);
      } else {
        copy.dateResiliation = null;
      }
      if (this.type != null) {
        String sourceType;
        sourceType = this.getType();
        String copyType =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
        copy.setType(copyType);
      } else {
        copy.type = null;
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
      if (this.destinataire != null) {
        String sourceDestinataire;
        sourceDestinataire = this.getDestinataire();
        String copyDestinataire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "destinataire", sourceDestinataire),
                    sourceDestinataire));
        copy.setDestinataire(copyDestinataire);
      } else {
        copy.destinataire = null;
      }
      if (this.individuelOuCollectif != null) {
        String sourceIndividuelOuCollectif;
        sourceIndividuelOuCollectif = this.getIndividuelOuCollectif();
        String copyIndividuelOuCollectif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "individuelOuCollectif", sourceIndividuelOuCollectif),
                    sourceIndividuelOuCollectif));
        copy.setIndividuelOuCollectif(copyIndividuelOuCollectif);
      } else {
        copy.individuelOuCollectif = null;
      }
      if (this.situationDebut != null) {
        XMLGregorianCalendar sourceSituationDebut;
        sourceSituationDebut = this.getSituationDebut();
        XMLGregorianCalendar copySituationDebut =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "situationDebut", sourceSituationDebut),
                    sourceSituationDebut));
        copy.setSituationDebut(copySituationDebut);
      } else {
        copy.situationDebut = null;
      }
      if (this.situationfin != null) {
        XMLGregorianCalendar sourceSituationfin;
        sourceSituationfin = this.getSituationfin();
        XMLGregorianCalendar copySituationfin =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "situationfin", sourceSituationfin),
                    sourceSituationfin));
        copy.setSituationfin(copySituationfin);
      } else {
        copy.situationfin = null;
      }
      if (this.motifFinSituation != null) {
        String sourceMotifFinSituation;
        sourceMotifFinSituation = this.getMotifFinSituation();
        String copyMotifFinSituation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "motifFinSituation", sourceMotifFinSituation),
                    sourceMotifFinSituation));
        copy.setMotifFinSituation(copyMotifFinSituation);
      } else {
        copy.motifFinSituation = null;
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
      if (this.categorieSociale != null) {
        String sourceCategorieSociale;
        sourceCategorieSociale = this.getCategorieSociale();
        String copyCategorieSociale =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "categorieSociale", sourceCategorieSociale),
                    sourceCategorieSociale));
        copy.setCategorieSociale(copyCategorieSociale);
      } else {
        copy.categorieSociale = null;
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
}
