package com.cegedimassurances.norme.tarification_prescription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_info_prestation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_info_prestation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_prestation"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="6"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_execution" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="lieu_execution"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_prestation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_justification"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;enumeration value="3"/&gt;
 *               &lt;enumeration value="4"/&gt;
 *               &lt;enumeration value="6"/&gt;
 *               &lt;enumeration value="7"/&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="5"/&gt;
 *               &lt;enumeration value="9"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_honoraires"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="qualificatif_depense"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="D"/&gt;
 *               &lt;enumeration value="E"/&gt;
 *               &lt;enumeration value="F"/&gt;
 *               &lt;enumeration value="G"/&gt;
 *               &lt;enumeration value="N"/&gt;
 *               &lt;enumeration value="A"/&gt;
 *               &lt;enumeration value="M"/&gt;
 *               &lt;enumeration value="B"/&gt;
 *               &lt;enumeration value="C"/&gt;
 *               &lt;enumeration value=" "/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="coefficient" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="5"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="quantite"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="denombrement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prix_unitaire"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="base_remboursement"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="taux_ro"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_ro"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_rc"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_rac"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="detail_prestation_cip" type="{http://norme.cegedimassurances.com/tarification-prescription}type_in_prestation_cip" minOccurs="0"/&gt;
 *         &lt;element name="detail_prestation_lpp" type="{http://norme.cegedimassurances.com/tarification-prescription}type_in_prestation_lpp" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="detail_prestation_acte" type="{http://norme.cegedimassurances.com/tarification-prescription}type_in_prestation_acte" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_info_prestation",
    propOrder = {
      "idPrestation",
      "dateExecution",
      "lieuExecution",
      "codePrestation",
      "codeJustification",
      "montantHonoraires",
      "qualificatifDepense",
      "coefficient",
      "quantite",
      "denombrement",
      "prixUnitaire",
      "baseRemboursement",
      "tauxRo",
      "montantRo",
      "montantRc",
      "montantRac",
      "detailPrestationCip",
      "detailPrestationLpp",
      "detailPrestationActe"
    })
public class TypeInfoPrestation implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_prestation")
  @NotNull
  @Digits(integer = 6, fraction = 0)
  protected int idPrestation;

  @XmlElement(name = "date_execution", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateExecution;

  @XmlElement(name = "lieu_execution")
  @NotNull
  protected int lieuExecution;

  @XmlElement(name = "code_prestation")
  @Size(min = 3, max = 3)
  protected String codePrestation;

  @XmlElement(name = "code_justification")
  @NotNull
  protected int codeJustification;

  @XmlElement(name = "montant_honoraires", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantHonoraires;

  @XmlElement(name = "qualificatif_depense", required = true)
  @NotNull
  @Pattern.List({
    @Pattern(regexp = "D"),
    @Pattern(regexp = "E"),
    @Pattern(regexp = "F"),
    @Pattern(regexp = "G"),
    @Pattern(regexp = "N"),
    @Pattern(regexp = "A"),
    @Pattern(regexp = "M"),
    @Pattern(regexp = "B"),
    @Pattern(regexp = "C"),
    @Pattern(regexp = " ")
  })
  protected String qualificatifDepense;

  @Digits(integer = 5, fraction = 2)
  protected BigDecimal coefficient;

  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int quantite;

  @Digits(integer = 3, fraction = 0)
  protected Integer denombrement;

  @XmlElement(name = "prix_unitaire", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal prixUnitaire;

  @XmlElement(name = "base_remboursement", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal baseRemboursement;

  @XmlElement(name = "taux_ro")
  @NotNull
  @DecimalMax("100")
  @DecimalMin("0")
  protected int tauxRo;

  @XmlElement(name = "montant_ro", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantRo;

  @XmlElement(name = "montant_rc", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantRc;

  @XmlElement(name = "montant_rac", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantRac;

  @XmlElement(name = "detail_prestation_cip")
  @Valid
  protected TypeInPrestationCip detailPrestationCip;

  @XmlElement(name = "detail_prestation_lpp")
  @Valid
  protected java.util.List<TypeInPrestationLpp> detailPrestationLpp;

  @XmlElement(name = "detail_prestation_acte")
  @Valid
  protected TypeInPrestationActe detailPrestationActe;

  /** Obtient la valeur de la propriété idPrestation. */
  public int getIdPrestation() {
    return idPrestation;
  }

  /** Définit la valeur de la propriété idPrestation. */
  public void setIdPrestation(int value) {
    this.idPrestation = value;
  }

  /**
   * Obtient la valeur de la propriété dateExecution.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateExecution() {
    return dateExecution;
  }

  /**
   * Définit la valeur de la propriété dateExecution.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateExecution(XMLGregorianCalendar value) {
    this.dateExecution = value;
  }

  /** Obtient la valeur de la propriété lieuExecution. */
  public int getLieuExecution() {
    return lieuExecution;
  }

  /** Définit la valeur de la propriété lieuExecution. */
  public void setLieuExecution(int value) {
    this.lieuExecution = value;
  }

  /**
   * Obtient la valeur de la propriété codePrestation.
   *
   * @return possible object is {@link String }
   */
  public String getCodePrestation() {
    return codePrestation;
  }

  /**
   * Définit la valeur de la propriété codePrestation.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodePrestation(String value) {
    this.codePrestation = value;
  }

  /** Obtient la valeur de la propriété codeJustification. */
  public int getCodeJustification() {
    return codeJustification;
  }

  /** Définit la valeur de la propriété codeJustification. */
  public void setCodeJustification(int value) {
    this.codeJustification = value;
  }

  /**
   * Obtient la valeur de la propriété montantHonoraires.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantHonoraires() {
    return montantHonoraires;
  }

  /**
   * Définit la valeur de la propriété montantHonoraires.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantHonoraires(BigDecimal value) {
    this.montantHonoraires = value;
  }

  /**
   * Obtient la valeur de la propriété qualificatifDepense.
   *
   * @return possible object is {@link String }
   */
  public String getQualificatifDepense() {
    return qualificatifDepense;
  }

  /**
   * Définit la valeur de la propriété qualificatifDepense.
   *
   * @param value allowed object is {@link String }
   */
  public void setQualificatifDepense(String value) {
    this.qualificatifDepense = value;
  }

  /**
   * Obtient la valeur de la propriété coefficient.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getCoefficient() {
    return coefficient;
  }

  /**
   * Définit la valeur de la propriété coefficient.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setCoefficient(BigDecimal value) {
    this.coefficient = value;
  }

  /** Obtient la valeur de la propriété quantite. */
  public int getQuantite() {
    return quantite;
  }

  /** Définit la valeur de la propriété quantite. */
  public void setQuantite(int value) {
    this.quantite = value;
  }

  /**
   * Obtient la valeur de la propriété denombrement.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getDenombrement() {
    return denombrement;
  }

  /**
   * Définit la valeur de la propriété denombrement.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setDenombrement(Integer value) {
    this.denombrement = value;
  }

  /**
   * Obtient la valeur de la propriété prixUnitaire.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getPrixUnitaire() {
    return prixUnitaire;
  }

  /**
   * Définit la valeur de la propriété prixUnitaire.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setPrixUnitaire(BigDecimal value) {
    this.prixUnitaire = value;
  }

  /**
   * Obtient la valeur de la propriété baseRemboursement.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getBaseRemboursement() {
    return baseRemboursement;
  }

  /**
   * Définit la valeur de la propriété baseRemboursement.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setBaseRemboursement(BigDecimal value) {
    this.baseRemboursement = value;
  }

  /** Obtient la valeur de la propriété tauxRo. */
  public int getTauxRo() {
    return tauxRo;
  }

  /** Définit la valeur de la propriété tauxRo. */
  public void setTauxRo(int value) {
    this.tauxRo = value;
  }

  /**
   * Obtient la valeur de la propriété montantRo.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantRo() {
    return montantRo;
  }

  /**
   * Définit la valeur de la propriété montantRo.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantRo(BigDecimal value) {
    this.montantRo = value;
  }

  /**
   * Obtient la valeur de la propriété montantRc.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantRc() {
    return montantRc;
  }

  /**
   * Définit la valeur de la propriété montantRc.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantRc(BigDecimal value) {
    this.montantRc = value;
  }

  /**
   * Obtient la valeur de la propriété montantRac.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantRac() {
    return montantRac;
  }

  /**
   * Définit la valeur de la propriété montantRac.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantRac(BigDecimal value) {
    this.montantRac = value;
  }

  /**
   * Obtient la valeur de la propriété detailPrestationCip.
   *
   * @return possible object is {@link TypeInPrestationCip }
   */
  public TypeInPrestationCip getDetailPrestationCip() {
    return detailPrestationCip;
  }

  /**
   * Définit la valeur de la propriété detailPrestationCip.
   *
   * @param value allowed object is {@link TypeInPrestationCip }
   */
  public void setDetailPrestationCip(TypeInPrestationCip value) {
    this.detailPrestationCip = value;
  }

  /**
   * Gets the value of the detailPrestationLpp property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the detailPrestationLpp property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getDetailPrestationLpp().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeInPrestationLpp }
   */
  public java.util.List<TypeInPrestationLpp> getDetailPrestationLpp() {
    if (detailPrestationLpp == null) {
      detailPrestationLpp = new ArrayList<TypeInPrestationLpp>();
    }
    return this.detailPrestationLpp;
  }

  /**
   * Obtient la valeur de la propriété detailPrestationActe.
   *
   * @return possible object is {@link TypeInPrestationActe }
   */
  public TypeInPrestationActe getDetailPrestationActe() {
    return detailPrestationActe;
  }

  /**
   * Définit la valeur de la propriété detailPrestationActe.
   *
   * @param value allowed object is {@link TypeInPrestationActe }
   */
  public void setDetailPrestationActe(TypeInPrestationActe value) {
    this.detailPrestationActe = value;
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
    if (draftCopy instanceof TypeInfoPrestation) {
      final TypeInfoPrestation copy = ((TypeInfoPrestation) draftCopy);
      int sourceIdPrestation;
      sourceIdPrestation = (true ? this.getIdPrestation() : 0);
      int copyIdPrestation =
          strategy.copy(
              LocatorUtils.property(locator, "idPrestation", sourceIdPrestation),
              sourceIdPrestation);
      copy.setIdPrestation(copyIdPrestation);
      if (this.dateExecution != null) {
        XMLGregorianCalendar sourceDateExecution;
        sourceDateExecution = this.getDateExecution();
        XMLGregorianCalendar copyDateExecution =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateExecution", sourceDateExecution),
                    sourceDateExecution));
        copy.setDateExecution(copyDateExecution);
      } else {
        copy.dateExecution = null;
      }
      int sourceLieuExecution;
      sourceLieuExecution = (true ? this.getLieuExecution() : 0);
      int copyLieuExecution =
          strategy.copy(
              LocatorUtils.property(locator, "lieuExecution", sourceLieuExecution),
              sourceLieuExecution);
      copy.setLieuExecution(copyLieuExecution);
      if (this.codePrestation != null) {
        String sourceCodePrestation;
        sourceCodePrestation = this.getCodePrestation();
        String copyCodePrestation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codePrestation", sourceCodePrestation),
                    sourceCodePrestation));
        copy.setCodePrestation(copyCodePrestation);
      } else {
        copy.codePrestation = null;
      }
      int sourceCodeJustification;
      sourceCodeJustification = (true ? this.getCodeJustification() : 0);
      int copyCodeJustification =
          strategy.copy(
              LocatorUtils.property(locator, "codeJustification", sourceCodeJustification),
              sourceCodeJustification);
      copy.setCodeJustification(copyCodeJustification);
      if (this.montantHonoraires != null) {
        BigDecimal sourceMontantHonoraires;
        sourceMontantHonoraires = this.getMontantHonoraires();
        BigDecimal copyMontantHonoraires =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantHonoraires", sourceMontantHonoraires),
                    sourceMontantHonoraires));
        copy.setMontantHonoraires(copyMontantHonoraires);
      } else {
        copy.montantHonoraires = null;
      }
      if (this.qualificatifDepense != null) {
        String sourceQualificatifDepense;
        sourceQualificatifDepense = this.getQualificatifDepense();
        String copyQualificatifDepense =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "qualificatifDepense", sourceQualificatifDepense),
                    sourceQualificatifDepense));
        copy.setQualificatifDepense(copyQualificatifDepense);
      } else {
        copy.qualificatifDepense = null;
      }
      if (this.coefficient != null) {
        BigDecimal sourceCoefficient;
        sourceCoefficient = this.getCoefficient();
        BigDecimal copyCoefficient =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "coefficient", sourceCoefficient),
                    sourceCoefficient));
        copy.setCoefficient(copyCoefficient);
      } else {
        copy.coefficient = null;
      }
      int sourceQuantite;
      sourceQuantite = (true ? this.getQuantite() : 0);
      int copyQuantite =
          strategy.copy(LocatorUtils.property(locator, "quantite", sourceQuantite), sourceQuantite);
      copy.setQuantite(copyQuantite);
      if (this.denombrement != null) {
        Integer sourceDenombrement;
        sourceDenombrement = this.getDenombrement();
        Integer copyDenombrement =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(locator, "denombrement", sourceDenombrement),
                    sourceDenombrement));
        copy.setDenombrement(copyDenombrement);
      } else {
        copy.denombrement = null;
      }
      if (this.prixUnitaire != null) {
        BigDecimal sourcePrixUnitaire;
        sourcePrixUnitaire = this.getPrixUnitaire();
        BigDecimal copyPrixUnitaire =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "prixUnitaire", sourcePrixUnitaire),
                    sourcePrixUnitaire));
        copy.setPrixUnitaire(copyPrixUnitaire);
      } else {
        copy.prixUnitaire = null;
      }
      if (this.baseRemboursement != null) {
        BigDecimal sourceBaseRemboursement;
        sourceBaseRemboursement = this.getBaseRemboursement();
        BigDecimal copyBaseRemboursement =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "baseRemboursement", sourceBaseRemboursement),
                    sourceBaseRemboursement));
        copy.setBaseRemboursement(copyBaseRemboursement);
      } else {
        copy.baseRemboursement = null;
      }
      int sourceTauxRo;
      sourceTauxRo = (true ? this.getTauxRo() : 0);
      int copyTauxRo =
          strategy.copy(LocatorUtils.property(locator, "tauxRo", sourceTauxRo), sourceTauxRo);
      copy.setTauxRo(copyTauxRo);
      if (this.montantRo != null) {
        BigDecimal sourceMontantRo;
        sourceMontantRo = this.getMontantRo();
        BigDecimal copyMontantRo =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantRo", sourceMontantRo), sourceMontantRo));
        copy.setMontantRo(copyMontantRo);
      } else {
        copy.montantRo = null;
      }
      if (this.montantRc != null) {
        BigDecimal sourceMontantRc;
        sourceMontantRc = this.getMontantRc();
        BigDecimal copyMontantRc =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantRc", sourceMontantRc), sourceMontantRc));
        copy.setMontantRc(copyMontantRc);
      } else {
        copy.montantRc = null;
      }
      if (this.montantRac != null) {
        BigDecimal sourceMontantRac;
        sourceMontantRac = this.getMontantRac();
        BigDecimal copyMontantRac =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantRac", sourceMontantRac),
                    sourceMontantRac));
        copy.setMontantRac(copyMontantRac);
      } else {
        copy.montantRac = null;
      }
      if (this.detailPrestationCip != null) {
        TypeInPrestationCip sourceDetailPrestationCip;
        sourceDetailPrestationCip = this.getDetailPrestationCip();
        TypeInPrestationCip copyDetailPrestationCip =
            ((TypeInPrestationCip)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "detailPrestationCip", sourceDetailPrestationCip),
                    sourceDetailPrestationCip));
        copy.setDetailPrestationCip(copyDetailPrestationCip);
      } else {
        copy.detailPrestationCip = null;
      }
      if ((this.detailPrestationLpp != null) && (!this.detailPrestationLpp.isEmpty())) {
        java.util.List<TypeInPrestationLpp> sourceDetailPrestationLpp;
        sourceDetailPrestationLpp =
            (((this.detailPrestationLpp != null) && (!this.detailPrestationLpp.isEmpty()))
                ? this.getDetailPrestationLpp()
                : null);
        @SuppressWarnings("unchecked")
        java.util.List<TypeInPrestationLpp> copyDetailPrestationLpp =
            ((java.util.List<TypeInPrestationLpp>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "detailPrestationLpp", sourceDetailPrestationLpp),
                    sourceDetailPrestationLpp));
        copy.detailPrestationLpp = null;
        if (copyDetailPrestationLpp != null) {
          java.util.List<TypeInPrestationLpp> uniqueDetailPrestationLppl =
              copy.getDetailPrestationLpp();
          uniqueDetailPrestationLppl.addAll(copyDetailPrestationLpp);
        }
      } else {
        copy.detailPrestationLpp = null;
      }
      if (this.detailPrestationActe != null) {
        TypeInPrestationActe sourceDetailPrestationActe;
        sourceDetailPrestationActe = this.getDetailPrestationActe();
        TypeInPrestationActe copyDetailPrestationActe =
            ((TypeInPrestationActe)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "detailPrestationActe", sourceDetailPrestationActe),
                    sourceDetailPrestationActe));
        copy.setDetailPrestationActe(copyDetailPrestationActe);
      } else {
        copy.detailPrestationActe = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInfoPrestation();
  }
}
