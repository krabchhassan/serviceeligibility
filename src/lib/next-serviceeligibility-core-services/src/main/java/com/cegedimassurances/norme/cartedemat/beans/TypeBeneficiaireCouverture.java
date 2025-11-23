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
 * Resultat de la consolidation des garanties et des taux de couverture
 *
 * <p>Classe Java pour type_beneficiaire_couverture complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_beneficiaire_couverture"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeDomaine"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="tauxRemboursement"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="uniteTauxRemboursement"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="periodeDebut" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="periodeFin" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="codeExterneProduit" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="codeOptionMutualiste" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelleOptionMutualiste" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="codeProduit" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelleProduit" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="codeGarantie"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelleGarantie" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prioriteDroits"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="origineDroits" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dateAdhesionCouverture" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="libelleCodeRenvoi" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="categorieDomaine"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prestations" type="{http://norme.cegedimassurances.com/carteDemat/beans}prestations"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_beneficiaire_couverture",
    propOrder = {
      "codeDomaine",
      "tauxRemboursement",
      "uniteTauxRemboursement",
      "periodeDebut",
      "periodeFin",
      "codeExterneProduit",
      "codeOptionMutualiste",
      "libelleOptionMutualiste",
      "codeProduit",
      "libelleProduit",
      "codeGarantie",
      "libelleGarantie",
      "prioriteDroits",
      "origineDroits",
      "dateAdhesionCouverture",
      "libelleCodeRenvoi",
      "categorieDomaine",
      "prestations"
    })
public class TypeBeneficiaireCouverture implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 5)
  protected String codeDomaine;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 15)
  protected String tauxRemboursement;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 2)
  protected String uniteTauxRemboursement;

  @XmlElement(required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar periodeDebut;

  @XmlElement(required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar periodeFin;

  @Size(min = 1, max = 30)
  protected String codeExterneProduit;

  @Size(min = 1, max = 15)
  protected String codeOptionMutualiste;

  @Size(min = 1, max = 25)
  protected String libelleOptionMutualiste;

  @Size(min = 1, max = 15)
  protected String codeProduit;

  @Size(min = 1, max = 25)
  protected String libelleProduit;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 15)
  protected String codeGarantie;

  @Size(min = 1, max = 25)
  protected String libelleGarantie;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 2)
  protected String prioriteDroits;

  @Size(min = 1, max = 1)
  protected String origineDroits;

  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateAdhesionCouverture;

  @Size(min = 1, max = 50)
  protected String libelleCodeRenvoi;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 15)
  protected String categorieDomaine;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected Prestations prestations;

  /**
   * Obtient la valeur de la propriété codeDomaine.
   *
   * @return possible object is {@link String }
   */
  public String getCodeDomaine() {
    return codeDomaine;
  }

  /**
   * Définit la valeur de la propriété codeDomaine.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeDomaine(String value) {
    this.codeDomaine = value;
  }

  /**
   * Obtient la valeur de la propriété tauxRemboursement.
   *
   * @return possible object is {@link String }
   */
  public String getTauxRemboursement() {
    return tauxRemboursement;
  }

  /**
   * Définit la valeur de la propriété tauxRemboursement.
   *
   * @param value allowed object is {@link String }
   */
  public void setTauxRemboursement(String value) {
    this.tauxRemboursement = value;
  }

  /**
   * Obtient la valeur de la propriété uniteTauxRemboursement.
   *
   * @return possible object is {@link String }
   */
  public String getUniteTauxRemboursement() {
    return uniteTauxRemboursement;
  }

  /**
   * Définit la valeur de la propriété uniteTauxRemboursement.
   *
   * @param value allowed object is {@link String }
   */
  public void setUniteTauxRemboursement(String value) {
    this.uniteTauxRemboursement = value;
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
   * Obtient la valeur de la propriété codeExterneProduit.
   *
   * @return possible object is {@link String }
   */
  public String getCodeExterneProduit() {
    return codeExterneProduit;
  }

  /**
   * Définit la valeur de la propriété codeExterneProduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeExterneProduit(String value) {
    this.codeExterneProduit = value;
  }

  /**
   * Obtient la valeur de la propriété codeOptionMutualiste.
   *
   * @return possible object is {@link String }
   */
  public String getCodeOptionMutualiste() {
    return codeOptionMutualiste;
  }

  /**
   * Définit la valeur de la propriété codeOptionMutualiste.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeOptionMutualiste(String value) {
    this.codeOptionMutualiste = value;
  }

  /**
   * Obtient la valeur de la propriété libelleOptionMutualiste.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleOptionMutualiste() {
    return libelleOptionMutualiste;
  }

  /**
   * Définit la valeur de la propriété libelleOptionMutualiste.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleOptionMutualiste(String value) {
    this.libelleOptionMutualiste = value;
  }

  /**
   * Obtient la valeur de la propriété codeProduit.
   *
   * @return possible object is {@link String }
   */
  public String getCodeProduit() {
    return codeProduit;
  }

  /**
   * Définit la valeur de la propriété codeProduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeProduit(String value) {
    this.codeProduit = value;
  }

  /**
   * Obtient la valeur de la propriété libelleProduit.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleProduit() {
    return libelleProduit;
  }

  /**
   * Définit la valeur de la propriété libelleProduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleProduit(String value) {
    this.libelleProduit = value;
  }

  /**
   * Obtient la valeur de la propriété codeGarantie.
   *
   * @return possible object is {@link String }
   */
  public String getCodeGarantie() {
    return codeGarantie;
  }

  /**
   * Définit la valeur de la propriété codeGarantie.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeGarantie(String value) {
    this.codeGarantie = value;
  }

  /**
   * Obtient la valeur de la propriété libelleGarantie.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleGarantie() {
    return libelleGarantie;
  }

  /**
   * Définit la valeur de la propriété libelleGarantie.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleGarantie(String value) {
    this.libelleGarantie = value;
  }

  /**
   * Obtient la valeur de la propriété prioriteDroits.
   *
   * @return possible object is {@link String }
   */
  public String getPrioriteDroits() {
    return prioriteDroits;
  }

  /**
   * Définit la valeur de la propriété prioriteDroits.
   *
   * @param value allowed object is {@link String }
   */
  public void setPrioriteDroits(String value) {
    this.prioriteDroits = value;
  }

  /**
   * Obtient la valeur de la propriété origineDroits.
   *
   * @return possible object is {@link String }
   */
  public String getOrigineDroits() {
    return origineDroits;
  }

  /**
   * Définit la valeur de la propriété origineDroits.
   *
   * @param value allowed object is {@link String }
   */
  public void setOrigineDroits(String value) {
    this.origineDroits = value;
  }

  /**
   * Obtient la valeur de la propriété dateAdhesionCouverture.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateAdhesionCouverture() {
    return dateAdhesionCouverture;
  }

  /**
   * Définit la valeur de la propriété dateAdhesionCouverture.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateAdhesionCouverture(XMLGregorianCalendar value) {
    this.dateAdhesionCouverture = value;
  }

  /**
   * Obtient la valeur de la propriété libelleCodeRenvoi.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleCodeRenvoi() {
    return libelleCodeRenvoi;
  }

  /**
   * Définit la valeur de la propriété libelleCodeRenvoi.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleCodeRenvoi(String value) {
    this.libelleCodeRenvoi = value;
  }

  /**
   * Obtient la valeur de la propriété categorieDomaine.
   *
   * @return possible object is {@link String }
   */
  public String getCategorieDomaine() {
    return categorieDomaine;
  }

  /**
   * Définit la valeur de la propriété categorieDomaine.
   *
   * @param value allowed object is {@link String }
   */
  public void setCategorieDomaine(String value) {
    this.categorieDomaine = value;
  }

  /**
   * Obtient la valeur de la propriété prestations.
   *
   * @return possible object is {@link Prestations }
   */
  public Prestations getPrestations() {
    return prestations;
  }

  /**
   * Définit la valeur de la propriété prestations.
   *
   * @param value allowed object is {@link Prestations }
   */
  public void setPrestations(Prestations value) {
    this.prestations = value;
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
    if (draftCopy instanceof TypeBeneficiaireCouverture) {
      final TypeBeneficiaireCouverture copy = ((TypeBeneficiaireCouverture) draftCopy);
      if (this.codeDomaine != null) {
        String sourceCodeDomaine;
        sourceCodeDomaine = this.getCodeDomaine();
        String copyCodeDomaine =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeDomaine", sourceCodeDomaine),
                    sourceCodeDomaine));
        copy.setCodeDomaine(copyCodeDomaine);
      } else {
        copy.codeDomaine = null;
      }
      if (this.tauxRemboursement != null) {
        String sourceTauxRemboursement;
        sourceTauxRemboursement = this.getTauxRemboursement();
        String copyTauxRemboursement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "tauxRemboursement", sourceTauxRemboursement),
                    sourceTauxRemboursement));
        copy.setTauxRemboursement(copyTauxRemboursement);
      } else {
        copy.tauxRemboursement = null;
      }
      if (this.uniteTauxRemboursement != null) {
        String sourceUniteTauxRemboursement;
        sourceUniteTauxRemboursement = this.getUniteTauxRemboursement();
        String copyUniteTauxRemboursement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "uniteTauxRemboursement", sourceUniteTauxRemboursement),
                    sourceUniteTauxRemboursement));
        copy.setUniteTauxRemboursement(copyUniteTauxRemboursement);
      } else {
        copy.uniteTauxRemboursement = null;
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
      if (this.codeExterneProduit != null) {
        String sourceCodeExterneProduit;
        sourceCodeExterneProduit = this.getCodeExterneProduit();
        String copyCodeExterneProduit =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeExterneProduit", sourceCodeExterneProduit),
                    sourceCodeExterneProduit));
        copy.setCodeExterneProduit(copyCodeExterneProduit);
      } else {
        copy.codeExterneProduit = null;
      }
      if (this.codeOptionMutualiste != null) {
        String sourceCodeOptionMutualiste;
        sourceCodeOptionMutualiste = this.getCodeOptionMutualiste();
        String copyCodeOptionMutualiste =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "codeOptionMutualiste", sourceCodeOptionMutualiste),
                    sourceCodeOptionMutualiste));
        copy.setCodeOptionMutualiste(copyCodeOptionMutualiste);
      } else {
        copy.codeOptionMutualiste = null;
      }
      if (this.libelleOptionMutualiste != null) {
        String sourceLibelleOptionMutualiste;
        sourceLibelleOptionMutualiste = this.getLibelleOptionMutualiste();
        String copyLibelleOptionMutualiste =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "libelleOptionMutualiste", sourceLibelleOptionMutualiste),
                    sourceLibelleOptionMutualiste));
        copy.setLibelleOptionMutualiste(copyLibelleOptionMutualiste);
      } else {
        copy.libelleOptionMutualiste = null;
      }
      if (this.codeProduit != null) {
        String sourceCodeProduit;
        sourceCodeProduit = this.getCodeProduit();
        String copyCodeProduit =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeProduit", sourceCodeProduit),
                    sourceCodeProduit));
        copy.setCodeProduit(copyCodeProduit);
      } else {
        copy.codeProduit = null;
      }
      if (this.libelleProduit != null) {
        String sourceLibelleProduit;
        sourceLibelleProduit = this.getLibelleProduit();
        String copyLibelleProduit =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleProduit", sourceLibelleProduit),
                    sourceLibelleProduit));
        copy.setLibelleProduit(copyLibelleProduit);
      } else {
        copy.libelleProduit = null;
      }
      if (this.codeGarantie != null) {
        String sourceCodeGarantie;
        sourceCodeGarantie = this.getCodeGarantie();
        String copyCodeGarantie =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeGarantie", sourceCodeGarantie),
                    sourceCodeGarantie));
        copy.setCodeGarantie(copyCodeGarantie);
      } else {
        copy.codeGarantie = null;
      }
      if (this.libelleGarantie != null) {
        String sourceLibelleGarantie;
        sourceLibelleGarantie = this.getLibelleGarantie();
        String copyLibelleGarantie =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleGarantie", sourceLibelleGarantie),
                    sourceLibelleGarantie));
        copy.setLibelleGarantie(copyLibelleGarantie);
      } else {
        copy.libelleGarantie = null;
      }
      if (this.prioriteDroits != null) {
        String sourcePrioriteDroits;
        sourcePrioriteDroits = this.getPrioriteDroits();
        String copyPrioriteDroits =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prioriteDroits", sourcePrioriteDroits),
                    sourcePrioriteDroits));
        copy.setPrioriteDroits(copyPrioriteDroits);
      } else {
        copy.prioriteDroits = null;
      }
      if (this.origineDroits != null) {
        String sourceOrigineDroits;
        sourceOrigineDroits = this.getOrigineDroits();
        String copyOrigineDroits =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "origineDroits", sourceOrigineDroits),
                    sourceOrigineDroits));
        copy.setOrigineDroits(copyOrigineDroits);
      } else {
        copy.origineDroits = null;
      }
      if (this.dateAdhesionCouverture != null) {
        XMLGregorianCalendar sourceDateAdhesionCouverture;
        sourceDateAdhesionCouverture = this.getDateAdhesionCouverture();
        XMLGregorianCalendar copyDateAdhesionCouverture =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "dateAdhesionCouverture", sourceDateAdhesionCouverture),
                    sourceDateAdhesionCouverture));
        copy.setDateAdhesionCouverture(copyDateAdhesionCouverture);
      } else {
        copy.dateAdhesionCouverture = null;
      }
      if (this.libelleCodeRenvoi != null) {
        String sourceLibelleCodeRenvoi;
        sourceLibelleCodeRenvoi = this.getLibelleCodeRenvoi();
        String copyLibelleCodeRenvoi =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleCodeRenvoi", sourceLibelleCodeRenvoi),
                    sourceLibelleCodeRenvoi));
        copy.setLibelleCodeRenvoi(copyLibelleCodeRenvoi);
      } else {
        copy.libelleCodeRenvoi = null;
      }
      if (this.categorieDomaine != null) {
        String sourceCategorieDomaine;
        sourceCategorieDomaine = this.getCategorieDomaine();
        String copyCategorieDomaine =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "categorieDomaine", sourceCategorieDomaine),
                    sourceCategorieDomaine));
        copy.setCategorieDomaine(copyCategorieDomaine);
      } else {
        copy.categorieDomaine = null;
      }
      if (this.prestations != null) {
        Prestations sourcePrestations;
        sourcePrestations = this.getPrestations();
        Prestations copyPrestations =
            ((Prestations)
                strategy.copy(
                    LocatorUtils.property(locator, "prestations", sourcePrestations),
                    sourcePrestations));
        copy.setPrestations(copyPrestations);
      } else {
        copy.prestations = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeBeneficiaireCouverture();
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
