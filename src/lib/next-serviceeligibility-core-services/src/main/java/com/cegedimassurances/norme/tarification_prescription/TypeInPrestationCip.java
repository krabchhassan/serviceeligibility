package com.cegedimassurances.norme.tarification_prescription;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_in_prestation_cip complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_in_prestation_cip"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_cip" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="type_code_cip"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *               &lt;enumeration value="A"/&gt;
 *               &lt;enumeration value="B"/&gt;
 *               &lt;enumeration value="6"/&gt;
 *               &lt;enumeration value="7"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_cip"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="7"/&gt;
 *               &lt;maxLength value="13"/&gt;
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
 *         &lt;element name="quantite_delivree"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="indic_medicament" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="4"/&gt;
 *               &lt;enumeration value="5"/&gt;
 *               &lt;enumeration value="7"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="indic_substitution" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *               &lt;enumeration value="U"/&gt;
 *               &lt;enumeration value="N"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="top_deconditionnement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *               &lt;enumeration value="D"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="quantite_unite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prix_unitaire_unite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nombre_conditionnement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="mode_prescription" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *               &lt;enumeration value="M"/&gt;
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
    name = "type_in_prestation_cip",
    propOrder = {
      "idCip",
      "typeCodeCip",
      "codeCip",
      "prixUnitaire",
      "quantiteDelivree",
      "indicMedicament",
      "indicSubstitution",
      "topDeconditionnement",
      "quantiteUnite",
      "prixUnitaireUnite",
      "nombreConditionnement",
      "modePrescription"
    })
public class TypeInPrestationCip implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_cip", required = true)
  @NotNull
  protected String idCip;

  @XmlElement(name = "type_code_cip", required = true)
  @NotNull
  @Size(min = 1, max = 1)
  @Pattern.List({
    @Pattern(regexp = "A"),
    @Pattern(regexp = "B"),
    @Pattern(regexp = "6"),
    @Pattern(regexp = "7")
  })
  protected String typeCodeCip;

  @XmlElement(name = "code_cip", required = true)
  @NotNull
  @Size(min = 7, max = 13)
  protected String codeCip;

  @XmlElement(name = "prix_unitaire", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal prixUnitaire;

  @XmlElement(name = "quantite_delivree")
  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int quantiteDelivree;

  @XmlElement(name = "indic_medicament")
  protected Integer indicMedicament;

  @XmlElement(name = "indic_substitution")
  @Size(min = 1, max = 1)
  @Pattern.List({@Pattern(regexp = "U"), @Pattern(regexp = "N")})
  protected String indicSubstitution;

  @XmlElement(name = "top_deconditionnement")
  @Size(min = 1, max = 1)
  @Pattern(regexp = "D")
  protected String topDeconditionnement;

  @XmlElement(name = "quantite_unite")
  @Digits(integer = 3, fraction = 0)
  protected Integer quantiteUnite;

  @XmlElement(name = "prix_unitaire_unite")
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal prixUnitaireUnite;

  @XmlElement(name = "nombre_conditionnement")
  @Digits(integer = 3, fraction = 0)
  protected Integer nombreConditionnement;

  @XmlElement(name = "mode_prescription")
  @Size(min = 1, max = 1)
  @Pattern(regexp = "M")
  protected String modePrescription;

  /**
   * Obtient la valeur de la propriété idCip.
   *
   * @return possible object is {@link String }
   */
  public String getIdCip() {
    return idCip;
  }

  /**
   * Définit la valeur de la propriété idCip.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdCip(String value) {
    this.idCip = value;
  }

  /**
   * Obtient la valeur de la propriété typeCodeCip.
   *
   * @return possible object is {@link String }
   */
  public String getTypeCodeCip() {
    return typeCodeCip;
  }

  /**
   * Définit la valeur de la propriété typeCodeCip.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeCodeCip(String value) {
    this.typeCodeCip = value;
  }

  /**
   * Obtient la valeur de la propriété codeCip.
   *
   * @return possible object is {@link String }
   */
  public String getCodeCip() {
    return codeCip;
  }

  /**
   * Définit la valeur de la propriété codeCip.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeCip(String value) {
    this.codeCip = value;
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

  /** Obtient la valeur de la propriété quantiteDelivree. */
  public int getQuantiteDelivree() {
    return quantiteDelivree;
  }

  /** Définit la valeur de la propriété quantiteDelivree. */
  public void setQuantiteDelivree(int value) {
    this.quantiteDelivree = value;
  }

  /**
   * Obtient la valeur de la propriété indicMedicament.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getIndicMedicament() {
    return indicMedicament;
  }

  /**
   * Définit la valeur de la propriété indicMedicament.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setIndicMedicament(Integer value) {
    this.indicMedicament = value;
  }

  /**
   * Obtient la valeur de la propriété indicSubstitution.
   *
   * @return possible object is {@link String }
   */
  public String getIndicSubstitution() {
    return indicSubstitution;
  }

  /**
   * Définit la valeur de la propriété indicSubstitution.
   *
   * @param value allowed object is {@link String }
   */
  public void setIndicSubstitution(String value) {
    this.indicSubstitution = value;
  }

  /**
   * Obtient la valeur de la propriété topDeconditionnement.
   *
   * @return possible object is {@link String }
   */
  public String getTopDeconditionnement() {
    return topDeconditionnement;
  }

  /**
   * Définit la valeur de la propriété topDeconditionnement.
   *
   * @param value allowed object is {@link String }
   */
  public void setTopDeconditionnement(String value) {
    this.topDeconditionnement = value;
  }

  /**
   * Obtient la valeur de la propriété quantiteUnite.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getQuantiteUnite() {
    return quantiteUnite;
  }

  /**
   * Définit la valeur de la propriété quantiteUnite.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setQuantiteUnite(Integer value) {
    this.quantiteUnite = value;
  }

  /**
   * Obtient la valeur de la propriété prixUnitaireUnite.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getPrixUnitaireUnite() {
    return prixUnitaireUnite;
  }

  /**
   * Définit la valeur de la propriété prixUnitaireUnite.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setPrixUnitaireUnite(BigDecimal value) {
    this.prixUnitaireUnite = value;
  }

  /**
   * Obtient la valeur de la propriété nombreConditionnement.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getNombreConditionnement() {
    return nombreConditionnement;
  }

  /**
   * Définit la valeur de la propriété nombreConditionnement.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setNombreConditionnement(Integer value) {
    this.nombreConditionnement = value;
  }

  /**
   * Obtient la valeur de la propriété modePrescription.
   *
   * @return possible object is {@link String }
   */
  public String getModePrescription() {
    return modePrescription;
  }

  /**
   * Définit la valeur de la propriété modePrescription.
   *
   * @param value allowed object is {@link String }
   */
  public void setModePrescription(String value) {
    this.modePrescription = value;
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
    if (draftCopy instanceof TypeInPrestationCip) {
      final TypeInPrestationCip copy = ((TypeInPrestationCip) draftCopy);
      if (this.idCip != null) {
        String sourceIdCip;
        sourceIdCip = this.getIdCip();
        String copyIdCip =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "idCip", sourceIdCip), sourceIdCip));
        copy.setIdCip(copyIdCip);
      } else {
        copy.idCip = null;
      }
      if (this.typeCodeCip != null) {
        String sourceTypeCodeCip;
        sourceTypeCodeCip = this.getTypeCodeCip();
        String copyTypeCodeCip =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "typeCodeCip", sourceTypeCodeCip),
                    sourceTypeCodeCip));
        copy.setTypeCodeCip(copyTypeCodeCip);
      } else {
        copy.typeCodeCip = null;
      }
      if (this.codeCip != null) {
        String sourceCodeCip;
        sourceCodeCip = this.getCodeCip();
        String copyCodeCip =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeCip", sourceCodeCip), sourceCodeCip));
        copy.setCodeCip(copyCodeCip);
      } else {
        copy.codeCip = null;
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
      int sourceQuantiteDelivree;
      sourceQuantiteDelivree = (true ? this.getQuantiteDelivree() : 0);
      int copyQuantiteDelivree =
          strategy.copy(
              LocatorUtils.property(locator, "quantiteDelivree", sourceQuantiteDelivree),
              sourceQuantiteDelivree);
      copy.setQuantiteDelivree(copyQuantiteDelivree);
      if (this.indicMedicament != null) {
        Integer sourceIndicMedicament;
        sourceIndicMedicament = this.getIndicMedicament();
        Integer copyIndicMedicament =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(locator, "indicMedicament", sourceIndicMedicament),
                    sourceIndicMedicament));
        copy.setIndicMedicament(copyIndicMedicament);
      } else {
        copy.indicMedicament = null;
      }
      if (this.indicSubstitution != null) {
        String sourceIndicSubstitution;
        sourceIndicSubstitution = this.getIndicSubstitution();
        String copyIndicSubstitution =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "indicSubstitution", sourceIndicSubstitution),
                    sourceIndicSubstitution));
        copy.setIndicSubstitution(copyIndicSubstitution);
      } else {
        copy.indicSubstitution = null;
      }
      if (this.topDeconditionnement != null) {
        String sourceTopDeconditionnement;
        sourceTopDeconditionnement = this.getTopDeconditionnement();
        String copyTopDeconditionnement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "topDeconditionnement", sourceTopDeconditionnement),
                    sourceTopDeconditionnement));
        copy.setTopDeconditionnement(copyTopDeconditionnement);
      } else {
        copy.topDeconditionnement = null;
      }
      if (this.quantiteUnite != null) {
        Integer sourceQuantiteUnite;
        sourceQuantiteUnite = this.getQuantiteUnite();
        Integer copyQuantiteUnite =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(locator, "quantiteUnite", sourceQuantiteUnite),
                    sourceQuantiteUnite));
        copy.setQuantiteUnite(copyQuantiteUnite);
      } else {
        copy.quantiteUnite = null;
      }
      if (this.prixUnitaireUnite != null) {
        BigDecimal sourcePrixUnitaireUnite;
        sourcePrixUnitaireUnite = this.getPrixUnitaireUnite();
        BigDecimal copyPrixUnitaireUnite =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "prixUnitaireUnite", sourcePrixUnitaireUnite),
                    sourcePrixUnitaireUnite));
        copy.setPrixUnitaireUnite(copyPrixUnitaireUnite);
      } else {
        copy.prixUnitaireUnite = null;
      }
      if (this.nombreConditionnement != null) {
        Integer sourceNombreConditionnement;
        sourceNombreConditionnement = this.getNombreConditionnement();
        Integer copyNombreConditionnement =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "nombreConditionnement", sourceNombreConditionnement),
                    sourceNombreConditionnement));
        copy.setNombreConditionnement(copyNombreConditionnement);
      } else {
        copy.nombreConditionnement = null;
      }
      if (this.modePrescription != null) {
        String sourceModePrescription;
        sourceModePrescription = this.getModePrescription();
        String copyModePrescription =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "modePrescription", sourceModePrescription),
                    sourceModePrescription));
        copy.setModePrescription(copyModePrescription);
      } else {
        copy.modePrescription = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInPrestationCip();
  }
}
