package com.cegedimassurances.norme.tarification_prescription;

import jakarta.validation.Valid;
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
 * Classe Java pour type_info_prescription complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_info_prescription"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_prescription"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="9"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_prescription" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="ps_prescripteur"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="9"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="origine_prescription" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *               &lt;enumeration value="T"/&gt;
 *               &lt;enumeration value="O"/&gt;
 *               &lt;enumeration value="P"/&gt;
 *               &lt;enumeration value="S"/&gt;
 *               &lt;enumeration value="A"/&gt;
 *               &lt;enumeration value="I"/&gt;
 *               &lt;enumeration value="H"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dispositif_prevention" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="date_dispositif_prevention" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="nature_operation"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_facturation" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="nature_assurance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="total_facture"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="total_ro_prescription"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="total_rc_prescription"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="total_rac_prescription"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nombre_prestations"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="infoPrestation" type="{http://norme.cegedimassurances.com/tarification-prescription}type_info_prestation" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_info_prescription",
    propOrder = {
      "idPrescription",
      "datePrescription",
      "psPrescripteur",
      "originePrescription",
      "dispositifPrevention",
      "dateDispositifPrevention",
      "natureOperation",
      "dateFacturation",
      "natureAssurance",
      "totalFacture",
      "totalRoPrescription",
      "totalRcPrescription",
      "totalRacPrescription",
      "nombrePrestations",
      "infoPrestation"
    })
public class TypeInfoPrescription implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_prescription")
  @NotNull
  @Digits(integer = 9, fraction = 0)
  protected int idPrescription;

  @XmlElement(name = "date_prescription")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar datePrescription;

  @XmlElement(name = "ps_prescripteur", required = true)
  @NotNull
  @Size(min = 9, max = 9)
  protected String psPrescripteur;

  @XmlElement(name = "origine_prescription")
  @Size(min = 1, max = 1)
  @Pattern.List({
    @Pattern(regexp = "T"),
    @Pattern(regexp = "O"),
    @Pattern(regexp = "P"),
    @Pattern(regexp = "S"),
    @Pattern(regexp = "A"),
    @Pattern(regexp = "I"),
    @Pattern(regexp = "H")
  })
  protected String originePrescription;

  @XmlElement(name = "dispositif_prevention")
  protected String dispositifPrevention;

  @XmlElement(name = "date_dispositif_prevention")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateDispositifPrevention;

  @XmlElement(name = "nature_operation", required = true, defaultValue = "1")
  @NotNull
  @Size(min = 1, max = 1)
  protected String natureOperation;

  @XmlElement(name = "date_facturation", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateFacturation;

  @XmlElement(name = "nature_assurance", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  protected String natureAssurance;

  @XmlElement(name = "total_facture", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalFacture;

  @XmlElement(name = "total_ro_prescription", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalRoPrescription;

  @XmlElement(name = "total_rc_prescription", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalRcPrescription;

  @XmlElement(name = "total_rac_prescription", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalRacPrescription;

  @XmlElement(name = "nombre_prestations")
  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int nombrePrestations;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected java.util.List<TypeInfoPrestation> infoPrestation;

  /** Obtient la valeur de la propriété idPrescription. */
  public int getIdPrescription() {
    return idPrescription;
  }

  /** Définit la valeur de la propriété idPrescription. */
  public void setIdPrescription(int value) {
    this.idPrescription = value;
  }

  /**
   * Obtient la valeur de la propriété datePrescription.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDatePrescription() {
    return datePrescription;
  }

  /**
   * Définit la valeur de la propriété datePrescription.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDatePrescription(XMLGregorianCalendar value) {
    this.datePrescription = value;
  }

  /**
   * Obtient la valeur de la propriété psPrescripteur.
   *
   * @return possible object is {@link String }
   */
  public String getPsPrescripteur() {
    return psPrescripteur;
  }

  /**
   * Définit la valeur de la propriété psPrescripteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setPsPrescripteur(String value) {
    this.psPrescripteur = value;
  }

  /**
   * Obtient la valeur de la propriété originePrescription.
   *
   * @return possible object is {@link String }
   */
  public String getOriginePrescription() {
    return originePrescription;
  }

  /**
   * Définit la valeur de la propriété originePrescription.
   *
   * @param value allowed object is {@link String }
   */
  public void setOriginePrescription(String value) {
    this.originePrescription = value;
  }

  /**
   * Obtient la valeur de la propriété dispositifPrevention.
   *
   * @return possible object is {@link String }
   */
  public String getDispositifPrevention() {
    return dispositifPrevention;
  }

  /**
   * Définit la valeur de la propriété dispositifPrevention.
   *
   * @param value allowed object is {@link String }
   */
  public void setDispositifPrevention(String value) {
    this.dispositifPrevention = value;
  }

  /**
   * Obtient la valeur de la propriété dateDispositifPrevention.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateDispositifPrevention() {
    return dateDispositifPrevention;
  }

  /**
   * Définit la valeur de la propriété dateDispositifPrevention.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateDispositifPrevention(XMLGregorianCalendar value) {
    this.dateDispositifPrevention = value;
  }

  /**
   * Obtient la valeur de la propriété natureOperation.
   *
   * @return possible object is {@link String }
   */
  public String getNatureOperation() {
    return natureOperation;
  }

  /**
   * Définit la valeur de la propriété natureOperation.
   *
   * @param value allowed object is {@link String }
   */
  public void setNatureOperation(String value) {
    this.natureOperation = value;
  }

  /**
   * Obtient la valeur de la propriété dateFacturation.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateFacturation() {
    return dateFacturation;
  }

  /**
   * Définit la valeur de la propriété dateFacturation.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateFacturation(XMLGregorianCalendar value) {
    this.dateFacturation = value;
  }

  /**
   * Obtient la valeur de la propriété natureAssurance.
   *
   * @return possible object is {@link String }
   */
  public String getNatureAssurance() {
    return natureAssurance;
  }

  /**
   * Définit la valeur de la propriété natureAssurance.
   *
   * @param value allowed object is {@link String }
   */
  public void setNatureAssurance(String value) {
    this.natureAssurance = value;
  }

  /**
   * Obtient la valeur de la propriété totalFacture.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalFacture() {
    return totalFacture;
  }

  /**
   * Définit la valeur de la propriété totalFacture.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalFacture(BigDecimal value) {
    this.totalFacture = value;
  }

  /**
   * Obtient la valeur de la propriété totalRoPrescription.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalRoPrescription() {
    return totalRoPrescription;
  }

  /**
   * Définit la valeur de la propriété totalRoPrescription.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalRoPrescription(BigDecimal value) {
    this.totalRoPrescription = value;
  }

  /**
   * Obtient la valeur de la propriété totalRcPrescription.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalRcPrescription() {
    return totalRcPrescription;
  }

  /**
   * Définit la valeur de la propriété totalRcPrescription.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalRcPrescription(BigDecimal value) {
    this.totalRcPrescription = value;
  }

  /**
   * Obtient la valeur de la propriété totalRacPrescription.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalRacPrescription() {
    return totalRacPrescription;
  }

  /**
   * Définit la valeur de la propriété totalRacPrescription.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalRacPrescription(BigDecimal value) {
    this.totalRacPrescription = value;
  }

  /** Obtient la valeur de la propriété nombrePrestations. */
  public int getNombrePrestations() {
    return nombrePrestations;
  }

  /** Définit la valeur de la propriété nombrePrestations. */
  public void setNombrePrestations(int value) {
    this.nombrePrestations = value;
  }

  /**
   * Gets the value of the infoPrestation property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the infoPrestation property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getInfoPrestation().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeInfoPrestation }
   */
  public java.util.List<TypeInfoPrestation> getInfoPrestation() {
    if (infoPrestation == null) {
      infoPrestation = new ArrayList<TypeInfoPrestation>();
    }
    return this.infoPrestation;
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
    if (draftCopy instanceof TypeInfoPrescription) {
      final TypeInfoPrescription copy = ((TypeInfoPrescription) draftCopy);
      int sourceIdPrescription;
      sourceIdPrescription = (true ? this.getIdPrescription() : 0);
      int copyIdPrescription =
          strategy.copy(
              LocatorUtils.property(locator, "idPrescription", sourceIdPrescription),
              sourceIdPrescription);
      copy.setIdPrescription(copyIdPrescription);
      if (this.datePrescription != null) {
        XMLGregorianCalendar sourceDatePrescription;
        sourceDatePrescription = this.getDatePrescription();
        XMLGregorianCalendar copyDatePrescription =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "datePrescription", sourceDatePrescription),
                    sourceDatePrescription));
        copy.setDatePrescription(copyDatePrescription);
      } else {
        copy.datePrescription = null;
      }
      if (this.psPrescripteur != null) {
        String sourcePsPrescripteur;
        sourcePsPrescripteur = this.getPsPrescripteur();
        String copyPsPrescripteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "psPrescripteur", sourcePsPrescripteur),
                    sourcePsPrescripteur));
        copy.setPsPrescripteur(copyPsPrescripteur);
      } else {
        copy.psPrescripteur = null;
      }
      if (this.originePrescription != null) {
        String sourceOriginePrescription;
        sourceOriginePrescription = this.getOriginePrescription();
        String copyOriginePrescription =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "originePrescription", sourceOriginePrescription),
                    sourceOriginePrescription));
        copy.setOriginePrescription(copyOriginePrescription);
      } else {
        copy.originePrescription = null;
      }
      if (this.dispositifPrevention != null) {
        String sourceDispositifPrevention;
        sourceDispositifPrevention = this.getDispositifPrevention();
        String copyDispositifPrevention =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "dispositifPrevention", sourceDispositifPrevention),
                    sourceDispositifPrevention));
        copy.setDispositifPrevention(copyDispositifPrevention);
      } else {
        copy.dispositifPrevention = null;
      }
      if (this.dateDispositifPrevention != null) {
        XMLGregorianCalendar sourceDateDispositifPrevention;
        sourceDateDispositifPrevention = this.getDateDispositifPrevention();
        XMLGregorianCalendar copyDateDispositifPrevention =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "dateDispositifPrevention", sourceDateDispositifPrevention),
                    sourceDateDispositifPrevention));
        copy.setDateDispositifPrevention(copyDateDispositifPrevention);
      } else {
        copy.dateDispositifPrevention = null;
      }
      if (this.natureOperation != null) {
        String sourceNatureOperation;
        sourceNatureOperation = this.getNatureOperation();
        String copyNatureOperation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "natureOperation", sourceNatureOperation),
                    sourceNatureOperation));
        copy.setNatureOperation(copyNatureOperation);
      } else {
        copy.natureOperation = null;
      }
      if (this.dateFacturation != null) {
        XMLGregorianCalendar sourceDateFacturation;
        sourceDateFacturation = this.getDateFacturation();
        XMLGregorianCalendar copyDateFacturation =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateFacturation", sourceDateFacturation),
                    sourceDateFacturation));
        copy.setDateFacturation(copyDateFacturation);
      } else {
        copy.dateFacturation = null;
      }
      if (this.natureAssurance != null) {
        String sourceNatureAssurance;
        sourceNatureAssurance = this.getNatureAssurance();
        String copyNatureAssurance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "natureAssurance", sourceNatureAssurance),
                    sourceNatureAssurance));
        copy.setNatureAssurance(copyNatureAssurance);
      } else {
        copy.natureAssurance = null;
      }
      if (this.totalFacture != null) {
        BigDecimal sourceTotalFacture;
        sourceTotalFacture = this.getTotalFacture();
        BigDecimal copyTotalFacture =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "totalFacture", sourceTotalFacture),
                    sourceTotalFacture));
        copy.setTotalFacture(copyTotalFacture);
      } else {
        copy.totalFacture = null;
      }
      if (this.totalRoPrescription != null) {
        BigDecimal sourceTotalRoPrescription;
        sourceTotalRoPrescription = this.getTotalRoPrescription();
        BigDecimal copyTotalRoPrescription =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "totalRoPrescription", sourceTotalRoPrescription),
                    sourceTotalRoPrescription));
        copy.setTotalRoPrescription(copyTotalRoPrescription);
      } else {
        copy.totalRoPrescription = null;
      }
      if (this.totalRcPrescription != null) {
        BigDecimal sourceTotalRcPrescription;
        sourceTotalRcPrescription = this.getTotalRcPrescription();
        BigDecimal copyTotalRcPrescription =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "totalRcPrescription", sourceTotalRcPrescription),
                    sourceTotalRcPrescription));
        copy.setTotalRcPrescription(copyTotalRcPrescription);
      } else {
        copy.totalRcPrescription = null;
      }
      if (this.totalRacPrescription != null) {
        BigDecimal sourceTotalRacPrescription;
        sourceTotalRacPrescription = this.getTotalRacPrescription();
        BigDecimal copyTotalRacPrescription =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "totalRacPrescription", sourceTotalRacPrescription),
                    sourceTotalRacPrescription));
        copy.setTotalRacPrescription(copyTotalRacPrescription);
      } else {
        copy.totalRacPrescription = null;
      }
      int sourceNombrePrestations;
      sourceNombrePrestations = (true ? this.getNombrePrestations() : 0);
      int copyNombrePrestations =
          strategy.copy(
              LocatorUtils.property(locator, "nombrePrestations", sourceNombrePrestations),
              sourceNombrePrestations);
      copy.setNombrePrestations(copyNombrePrestations);
      if ((this.infoPrestation != null) && (!this.infoPrestation.isEmpty())) {
        java.util.List<TypeInfoPrestation> sourceInfoPrestation;
        sourceInfoPrestation =
            (((this.infoPrestation != null) && (!this.infoPrestation.isEmpty()))
                ? this.getInfoPrestation()
                : null);
        @SuppressWarnings("unchecked")
        java.util.List<TypeInfoPrestation> copyInfoPrestation =
            ((java.util.List<TypeInfoPrestation>)
                strategy.copy(
                    LocatorUtils.property(locator, "infoPrestation", sourceInfoPrestation),
                    sourceInfoPrestation));
        copy.infoPrestation = null;
        if (copyInfoPrestation != null) {
          java.util.List<TypeInfoPrestation> uniqueInfoPrestationl = copy.getInfoPrestation();
          uniqueInfoPrestationl.addAll(copyInfoPrestation);
        }
      } else {
        copy.infoPrestation = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInfoPrescription();
  }
}
