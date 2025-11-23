package com.cegedimassurances.norme.tarification;

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
 * Classe Java pour type_tarification_contrat complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_tarification_contrat"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_contrat"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
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
 *         &lt;element name="civilite_porteur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
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
    name = "type_tarification_contrat",
    propOrder = {
      "idContrat",
      "dateSouscription",
      "dateResiliation",
      "type",
      "civilitePorteur",
      "qualification",
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
public class TypeTarificationContrat implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_contrat", required = true)
  @NotNull
  @Size(max = 30)
  protected String idContrat;

  @XmlElement(name = "date_souscription", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateSouscription;

  @XmlElement(name = "date_resiliation")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateResiliation;

  @Size(max = 15)
  protected String type;

  @XmlElement(name = "civilite_porteur")
  @Size(max = 45)
  protected String civilitePorteur;

  @Size(max = 50)
  protected String qualification;

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
   * Obtient la valeur de la propriété idContrat.
   *
   * @return possible object is {@link String }
   */
  public String getIdContrat() {
    return idContrat;
  }

  /**
   * Définit la valeur de la propriété idContrat.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdContrat(String value) {
    this.idContrat = value;
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
    if (draftCopy instanceof TypeTarificationContrat) {
      final TypeTarificationContrat copy = ((TypeTarificationContrat) draftCopy);
      if (this.idContrat != null) {
        String sourceIdContrat;
        sourceIdContrat = this.getIdContrat();
        String copyIdContrat =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idContrat", sourceIdContrat), sourceIdContrat));
        copy.setIdContrat(copyIdContrat);
      } else {
        copy.idContrat = null;
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
    return new TypeTarificationContrat();
  }
}
