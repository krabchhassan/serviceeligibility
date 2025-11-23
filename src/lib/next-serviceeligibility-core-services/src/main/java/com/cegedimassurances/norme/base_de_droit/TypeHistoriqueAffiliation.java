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
 * Classe Java pour type_historique_affiliation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_historique_affiliation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nom" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_patronymique"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_marital" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prenom" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="civilite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="periode_debut" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="periode_fin" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="qualite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="regime_OD1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="caisse_OD1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="centre_OD1" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="regime_OD2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="caisse_OD2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="centre_OD2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="medecin_traitant" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="regime_particulier" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="is_beneficiaire_ACS" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_historique_affiliation",
    propOrder = {
      "nom",
      "nomPatronymique",
      "nomMarital",
      "prenom",
      "civilite",
      "periodeDebut",
      "periodeFin",
      "qualite",
      "regimeOD1",
      "caisseOD1",
      "centreOD1",
      "regimeOD2",
      "caisseOD2",
      "centreOD2",
      "medecinTraitant",
      "regimeParticulier",
      "isBeneficiaireACS"
    })
public class TypeHistoriqueAffiliation implements Serializable, Cloneable, CopyTo {

  @Size(max = 50)
  protected String nom;

  @XmlElement(name = "nom_patronymique", required = true)
  @NotNull
  @Size(max = 50)
  protected String nomPatronymique;

  @XmlElement(name = "nom_marital")
  @Size(max = 50)
  protected String nomMarital;

  @Size(max = 50)
  protected String prenom;

  @Size(max = 45)
  protected String civilite;

  @XmlElement(name = "periode_debut", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar periodeDebut;

  @XmlElement(name = "periode_fin")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar periodeFin;

  @Size(max = 1)
  protected String qualite;

  @XmlElement(name = "regime_OD1", required = true)
  @NotNull
  @Size(max = 2)
  protected String regimeOD1;

  @XmlElement(name = "caisse_OD1", required = true)
  @NotNull
  @Size(max = 3)
  protected String caisseOD1;

  @XmlElement(name = "centre_OD1")
  @Size(max = 4)
  protected String centreOD1;

  @XmlElement(name = "regime_OD2")
  @Size(max = 2)
  protected String regimeOD2;

  @XmlElement(name = "caisse_OD2")
  @Size(max = 3)
  protected String caisseOD2;

  @XmlElement(name = "centre_OD2")
  @Size(max = 4)
  protected String centreOD2;

  @XmlElement(name = "medecin_traitant")
  @Size(max = 15)
  protected String medecinTraitant;

  @XmlElement(name = "regime_particulier")
  @Size(max = 2)
  protected String regimeParticulier;

  @XmlElement(name = "is_beneficiaire_ACS")
  @NotNull
  protected boolean isBeneficiaireACS;

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
   * Obtient la valeur de la propriété civilite.
   *
   * @return possible object is {@link String }
   */
  public String getCivilite() {
    return civilite;
  }

  /**
   * Définit la valeur de la propriété civilite.
   *
   * @param value allowed object is {@link String }
   */
  public void setCivilite(String value) {
    this.civilite = value;
  }

  /**
   * Obtient la valeur de la propriété periodeDebut.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getPeriodeDebut() {
    return periodeDebut;
  }

  /**
   * Définit la valeur de la propriété periodeDebut.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setPeriodeDebut(XMLGregorianCalendar value) {
    this.periodeDebut = value;
  }

  /**
   * Obtient la valeur de la propriété periodeFin.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getPeriodeFin() {
    return periodeFin;
  }

  /**
   * Définit la valeur de la propriété periodeFin.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setPeriodeFin(XMLGregorianCalendar value) {
    this.periodeFin = value;
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
   * Obtient la valeur de la propriété medecinTraitant.
   *
   * @return possible object is {@link String }
   */
  public String getMedecinTraitant() {
    return medecinTraitant;
  }

  /**
   * Définit la valeur de la propriété medecinTraitant.
   *
   * @param value allowed object is {@link String }
   */
  public void setMedecinTraitant(String value) {
    this.medecinTraitant = value;
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

  /** Obtient la valeur de la propriété isBeneficiaireACS. */
  public boolean isIsBeneficiaireACS() {
    return isBeneficiaireACS;
  }

  /** Définit la valeur de la propriété isBeneficiaireACS. */
  public void setIsBeneficiaireACS(boolean value) {
    this.isBeneficiaireACS = value;
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
    if (draftCopy instanceof TypeHistoriqueAffiliation) {
      final TypeHistoriqueAffiliation copy = ((TypeHistoriqueAffiliation) draftCopy);
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
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
      if (this.civilite != null) {
        String sourceCivilite;
        sourceCivilite = this.getCivilite();
        String copyCivilite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "civilite", sourceCivilite), sourceCivilite));
        copy.setCivilite(copyCivilite);
      } else {
        copy.civilite = null;
      }
      if (this.periodeDebut != null) {
        XMLGregorianCalendar sourcePeriodeDebut;
        sourcePeriodeDebut = this.getPeriodeDebut();
        XMLGregorianCalendar copyPeriodeDebut =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "periodeDebut", sourcePeriodeDebut),
                    sourcePeriodeDebut));
        copy.setPeriodeDebut(copyPeriodeDebut);
      } else {
        copy.periodeDebut = null;
      }
      if (this.periodeFin != null) {
        XMLGregorianCalendar sourcePeriodeFin;
        sourcePeriodeFin = this.getPeriodeFin();
        XMLGregorianCalendar copyPeriodeFin =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "periodeFin", sourcePeriodeFin),
                    sourcePeriodeFin));
        copy.setPeriodeFin(copyPeriodeFin);
      } else {
        copy.periodeFin = null;
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
      if (this.medecinTraitant != null) {
        String sourceMedecinTraitant;
        sourceMedecinTraitant = this.getMedecinTraitant();
        String copyMedecinTraitant =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "medecinTraitant", sourceMedecinTraitant),
                    sourceMedecinTraitant));
        copy.setMedecinTraitant(copyMedecinTraitant);
      } else {
        copy.medecinTraitant = null;
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
      boolean sourceIsBeneficiaireACS;
      sourceIsBeneficiaireACS = (true ? this.isIsBeneficiaireACS() : false);
      boolean copyIsBeneficiaireACS =
          strategy.copy(
              LocatorUtils.property(locator, "isBeneficiaireACS", sourceIsBeneficiaireACS),
              sourceIsBeneficiaireACS);
      copy.setIsBeneficiaireACS(copyIsBeneficiaireACS);
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHistoriqueAffiliation();
  }
}
