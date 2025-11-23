package com.cegedimassurances.norme.tarification_prescription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_reponse_prescription complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_reponse_prescription"&gt;
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
 *         &lt;element name="total_bloc_prescription"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="total_pec_prescription"&gt;
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
 *         &lt;element name="numero_accreditation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="16"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="reponsePrestation" type="{http://norme.cegedimassurances.com/tarification-prescription}type_reponse_prestation" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_reponse_prescription",
    propOrder = {
      "idPrescription",
      "totalBlocPrescription",
      "totalPecPrescription",
      "totalRacPrescription",
      "nombrePrestations",
      "numeroAccreditation",
      "reponsePrestation"
    })
public class TypeReponsePrescription implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_prescription")
  @NotNull
  @Digits(integer = 9, fraction = 0)
  protected int idPrescription;

  @XmlElement(name = "total_bloc_prescription", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalBlocPrescription;

  @XmlElement(name = "total_pec_prescription", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalPecPrescription;

  @XmlElement(name = "total_rac_prescription", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal totalRacPrescription;

  @XmlElement(name = "nombre_prestations")
  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int nombrePrestations;

  @XmlElement(name = "numero_accreditation")
  @Size(min = 16, max = 16)
  protected String numeroAccreditation;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeReponsePrestation> reponsePrestation;

  /** Obtient la valeur de la propriété idPrescription. */
  public int getIdPrescription() {
    return idPrescription;
  }

  /** Définit la valeur de la propriété idPrescription. */
  public void setIdPrescription(int value) {
    this.idPrescription = value;
  }

  /**
   * Obtient la valeur de la propriété totalBlocPrescription.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalBlocPrescription() {
    return totalBlocPrescription;
  }

  /**
   * Définit la valeur de la propriété totalBlocPrescription.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalBlocPrescription(BigDecimal value) {
    this.totalBlocPrescription = value;
  }

  /**
   * Obtient la valeur de la propriété totalPecPrescription.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTotalPecPrescription() {
    return totalPecPrescription;
  }

  /**
   * Définit la valeur de la propriété totalPecPrescription.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTotalPecPrescription(BigDecimal value) {
    this.totalPecPrescription = value;
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
   * Obtient la valeur de la propriété numeroAccreditation.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAccreditation() {
    return numeroAccreditation;
  }

  /**
   * Définit la valeur de la propriété numeroAccreditation.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAccreditation(String value) {
    this.numeroAccreditation = value;
  }

  /**
   * Gets the value of the reponsePrestation property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the reponsePrestation property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getReponsePrestation().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeReponsePrestation }
   */
  public List<TypeReponsePrestation> getReponsePrestation() {
    if (reponsePrestation == null) {
      reponsePrestation = new ArrayList<TypeReponsePrestation>();
    }
    return this.reponsePrestation;
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
    if (draftCopy instanceof TypeReponsePrescription) {
      final TypeReponsePrescription copy = ((TypeReponsePrescription) draftCopy);
      int sourceIdPrescription;
      sourceIdPrescription = (true ? this.getIdPrescription() : 0);
      int copyIdPrescription =
          strategy.copy(
              LocatorUtils.property(locator, "idPrescription", sourceIdPrescription),
              sourceIdPrescription);
      copy.setIdPrescription(copyIdPrescription);
      if (this.totalBlocPrescription != null) {
        BigDecimal sourceTotalBlocPrescription;
        sourceTotalBlocPrescription = this.getTotalBlocPrescription();
        BigDecimal copyTotalBlocPrescription =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "totalBlocPrescription", sourceTotalBlocPrescription),
                    sourceTotalBlocPrescription));
        copy.setTotalBlocPrescription(copyTotalBlocPrescription);
      } else {
        copy.totalBlocPrescription = null;
      }
      if (this.totalPecPrescription != null) {
        BigDecimal sourceTotalPecPrescription;
        sourceTotalPecPrescription = this.getTotalPecPrescription();
        BigDecimal copyTotalPecPrescription =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "totalPecPrescription", sourceTotalPecPrescription),
                    sourceTotalPecPrescription));
        copy.setTotalPecPrescription(copyTotalPecPrescription);
      } else {
        copy.totalPecPrescription = null;
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
      if (this.numeroAccreditation != null) {
        String sourceNumeroAccreditation;
        sourceNumeroAccreditation = this.getNumeroAccreditation();
        String copyNumeroAccreditation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroAccreditation", sourceNumeroAccreditation),
                    sourceNumeroAccreditation));
        copy.setNumeroAccreditation(copyNumeroAccreditation);
      } else {
        copy.numeroAccreditation = null;
      }
      if ((this.reponsePrestation != null) && (!this.reponsePrestation.isEmpty())) {
        List<TypeReponsePrestation> sourceReponsePrestation;
        sourceReponsePrestation =
            (((this.reponsePrestation != null) && (!this.reponsePrestation.isEmpty()))
                ? this.getReponsePrestation()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeReponsePrestation> copyReponsePrestation =
            ((List<TypeReponsePrestation>)
                strategy.copy(
                    LocatorUtils.property(locator, "reponsePrestation", sourceReponsePrestation),
                    sourceReponsePrestation));
        copy.reponsePrestation = null;
        if (copyReponsePrestation != null) {
          List<TypeReponsePrestation> uniqueReponsePrestationl = copy.getReponsePrestation();
          uniqueReponsePrestationl.addAll(copyReponsePrestation);
        }
      } else {
        copy.reponsePrestation = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeReponsePrescription();
  }
}
