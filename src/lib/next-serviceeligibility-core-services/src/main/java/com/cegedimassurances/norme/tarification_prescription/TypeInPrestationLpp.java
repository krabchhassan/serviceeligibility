package com.cegedimassurances.norme.tarification_prescription;

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
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_in_prestation_lpp complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_in_prestation_lpp"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_lpp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="code_lpp"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="7"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="type_prestation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *               &lt;enumeration value="A"/&gt;
 *               &lt;enumeration value="E"/&gt;
 *               &lt;enumeration value="L"/&gt;
 *               &lt;enumeration value="P"/&gt;
 *               &lt;enumeration value="S"/&gt;
 *               &lt;enumeration value="R"/&gt;
 *               &lt;enumeration value="V"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_debut_facturation" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="date_fin_facturation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="tarif_lpp"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nombre_unites"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="prix_unitaire_lpp"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_total_lpp"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_total_remise"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
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
    name = "type_in_prestation_lpp",
    propOrder = {
      "idLpp",
      "codeLpp",
      "typePrestation",
      "dateDebutFacturation",
      "dateFinFacturation",
      "tarifLpp",
      "nombreUnites",
      "prixUnitaireLpp",
      "montantTotalLpp",
      "montantTotalRemise"
    })
public class TypeInPrestationLpp implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_lpp", required = true)
  @NotNull
  protected String idLpp;

  @XmlElement(name = "code_lpp", required = true)
  @NotNull
  @Size(min = 7, max = 7)
  protected String codeLpp;

  @XmlElement(name = "type_prestation")
  @Size(min = 1, max = 1)
  @Pattern.List({
    @Pattern(regexp = "A"),
    @Pattern(regexp = "E"),
    @Pattern(regexp = "L"),
    @Pattern(regexp = "P"),
    @Pattern(regexp = "S"),
    @Pattern(regexp = "R"),
    @Pattern(regexp = "V")
  })
  protected String typePrestation;

  @XmlElement(name = "date_debut_facturation", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateDebutFacturation;

  @XmlElement(name = "date_fin_facturation")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateFinFacturation;

  @XmlElement(name = "tarif_lpp", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal tarifLpp;

  @XmlElement(name = "nombre_unites")
  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int nombreUnites;

  @XmlElement(name = "prix_unitaire_lpp", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal prixUnitaireLpp;

  @XmlElement(name = "montant_total_lpp", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantTotalLpp;

  @XmlElement(name = "montant_total_remise", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantTotalRemise;

  /**
   * Obtient la valeur de la propriété idLpp.
   *
   * @return possible object is {@link String }
   */
  public String getIdLpp() {
    return idLpp;
  }

  /**
   * Définit la valeur de la propriété idLpp.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdLpp(String value) {
    this.idLpp = value;
  }

  /**
   * Obtient la valeur de la propriété codeLpp.
   *
   * @return possible object is {@link String }
   */
  public String getCodeLpp() {
    return codeLpp;
  }

  /**
   * Définit la valeur de la propriété codeLpp.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeLpp(String value) {
    this.codeLpp = value;
  }

  /**
   * Obtient la valeur de la propriété typePrestation.
   *
   * @return possible object is {@link String }
   */
  public String getTypePrestation() {
    return typePrestation;
  }

  /**
   * Définit la valeur de la propriété typePrestation.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypePrestation(String value) {
    this.typePrestation = value;
  }

  /**
   * Obtient la valeur de la propriété dateDebutFacturation.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateDebutFacturation() {
    return dateDebutFacturation;
  }

  /**
   * Définit la valeur de la propriété dateDebutFacturation.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateDebutFacturation(XMLGregorianCalendar value) {
    this.dateDebutFacturation = value;
  }

  /**
   * Obtient la valeur de la propriété dateFinFacturation.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateFinFacturation() {
    return dateFinFacturation;
  }

  /**
   * Définit la valeur de la propriété dateFinFacturation.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateFinFacturation(XMLGregorianCalendar value) {
    this.dateFinFacturation = value;
  }

  /**
   * Obtient la valeur de la propriété tarifLpp.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTarifLpp() {
    return tarifLpp;
  }

  /**
   * Définit la valeur de la propriété tarifLpp.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTarifLpp(BigDecimal value) {
    this.tarifLpp = value;
  }

  /** Obtient la valeur de la propriété nombreUnites. */
  public int getNombreUnites() {
    return nombreUnites;
  }

  /** Définit la valeur de la propriété nombreUnites. */
  public void setNombreUnites(int value) {
    this.nombreUnites = value;
  }

  /**
   * Obtient la valeur de la propriété prixUnitaireLpp.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getPrixUnitaireLpp() {
    return prixUnitaireLpp;
  }

  /**
   * Définit la valeur de la propriété prixUnitaireLpp.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setPrixUnitaireLpp(BigDecimal value) {
    this.prixUnitaireLpp = value;
  }

  /**
   * Obtient la valeur de la propriété montantTotalLpp.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantTotalLpp() {
    return montantTotalLpp;
  }

  /**
   * Définit la valeur de la propriété montantTotalLpp.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantTotalLpp(BigDecimal value) {
    this.montantTotalLpp = value;
  }

  /**
   * Obtient la valeur de la propriété montantTotalRemise.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantTotalRemise() {
    return montantTotalRemise;
  }

  /**
   * Définit la valeur de la propriété montantTotalRemise.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantTotalRemise(BigDecimal value) {
    this.montantTotalRemise = value;
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
    if (draftCopy instanceof TypeInPrestationLpp) {
      final TypeInPrestationLpp copy = ((TypeInPrestationLpp) draftCopy);
      if (this.idLpp != null) {
        String sourceIdLpp;
        sourceIdLpp = this.getIdLpp();
        String copyIdLpp =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "idLpp", sourceIdLpp), sourceIdLpp));
        copy.setIdLpp(copyIdLpp);
      } else {
        copy.idLpp = null;
      }
      if (this.codeLpp != null) {
        String sourceCodeLpp;
        sourceCodeLpp = this.getCodeLpp();
        String copyCodeLpp =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeLpp", sourceCodeLpp), sourceCodeLpp));
        copy.setCodeLpp(copyCodeLpp);
      } else {
        copy.codeLpp = null;
      }
      if (this.typePrestation != null) {
        String sourceTypePrestation;
        sourceTypePrestation = this.getTypePrestation();
        String copyTypePrestation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "typePrestation", sourceTypePrestation),
                    sourceTypePrestation));
        copy.setTypePrestation(copyTypePrestation);
      } else {
        copy.typePrestation = null;
      }
      if (this.dateDebutFacturation != null) {
        XMLGregorianCalendar sourceDateDebutFacturation;
        sourceDateDebutFacturation = this.getDateDebutFacturation();
        XMLGregorianCalendar copyDateDebutFacturation =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "dateDebutFacturation", sourceDateDebutFacturation),
                    sourceDateDebutFacturation));
        copy.setDateDebutFacturation(copyDateDebutFacturation);
      } else {
        copy.dateDebutFacturation = null;
      }
      if (this.dateFinFacturation != null) {
        XMLGregorianCalendar sourceDateFinFacturation;
        sourceDateFinFacturation = this.getDateFinFacturation();
        XMLGregorianCalendar copyDateFinFacturation =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateFinFacturation", sourceDateFinFacturation),
                    sourceDateFinFacturation));
        copy.setDateFinFacturation(copyDateFinFacturation);
      } else {
        copy.dateFinFacturation = null;
      }
      if (this.tarifLpp != null) {
        BigDecimal sourceTarifLpp;
        sourceTarifLpp = this.getTarifLpp();
        BigDecimal copyTarifLpp =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "tarifLpp", sourceTarifLpp), sourceTarifLpp));
        copy.setTarifLpp(copyTarifLpp);
      } else {
        copy.tarifLpp = null;
      }
      int sourceNombreUnites;
      sourceNombreUnites = (true ? this.getNombreUnites() : 0);
      int copyNombreUnites =
          strategy.copy(
              LocatorUtils.property(locator, "nombreUnites", sourceNombreUnites),
              sourceNombreUnites);
      copy.setNombreUnites(copyNombreUnites);
      if (this.prixUnitaireLpp != null) {
        BigDecimal sourcePrixUnitaireLpp;
        sourcePrixUnitaireLpp = this.getPrixUnitaireLpp();
        BigDecimal copyPrixUnitaireLpp =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "prixUnitaireLpp", sourcePrixUnitaireLpp),
                    sourcePrixUnitaireLpp));
        copy.setPrixUnitaireLpp(copyPrixUnitaireLpp);
      } else {
        copy.prixUnitaireLpp = null;
      }
      if (this.montantTotalLpp != null) {
        BigDecimal sourceMontantTotalLpp;
        sourceMontantTotalLpp = this.getMontantTotalLpp();
        BigDecimal copyMontantTotalLpp =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantTotalLpp", sourceMontantTotalLpp),
                    sourceMontantTotalLpp));
        copy.setMontantTotalLpp(copyMontantTotalLpp);
      } else {
        copy.montantTotalLpp = null;
      }
      if (this.montantTotalRemise != null) {
        BigDecimal sourceMontantTotalRemise;
        sourceMontantTotalRemise = this.getMontantTotalRemise();
        BigDecimal copyMontantTotalRemise =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantTotalRemise", sourceMontantTotalRemise),
                    sourceMontantTotalRemise));
        copy.setMontantTotalRemise(copyMontantTotalRemise);
      } else {
        copy.montantTotalRemise = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInPrestationLpp();
  }
}
