package com.cegedimassurances.norme.patient;

import com.cegedimassurances.norme.amc.TypeAmc;
import com.cegedimassurances.norme.amo.TypeAmo;
import com.cegedimassurances.norme.beneficiaire.TypeBeneficiaireDemandeur;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_info_patient complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_info_patient"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AMO" type="{http://norme.cegedimassurances.com/amo}type_amo" minOccurs="0"/&gt;
 *         &lt;element name="AMC" type="{http://norme.cegedimassurances.com/amc}type_amc"/&gt;
 *         &lt;element name="beneficiaire" type="{http://norme.cegedimassurances.com/beneficiaire}type_beneficiaire_demandeur"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_info_patient",
    propOrder = {"amo", "amc", "beneficiaire"})
public class TypeInfoPatient implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "AMO")
  @Valid
  protected TypeAmo amo;

  @XmlElement(name = "AMC", required = true)
  @NotNull
  @Valid
  protected TypeAmc amc;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeBeneficiaireDemandeur beneficiaire;

  /**
   * Obtient la valeur de la propriété amo.
   *
   * @return possible object is {@link TypeAmo }
   */
  public TypeAmo getAMO() {
    return amo;
  }

  /**
   * Définit la valeur de la propriété amo.
   *
   * @param value allowed object is {@link TypeAmo }
   */
  public void setAMO(TypeAmo value) {
    this.amo = value;
  }

  /**
   * Obtient la valeur de la propriété amc.
   *
   * @return possible object is {@link TypeAmc }
   */
  public TypeAmc getAMC() {
    return amc;
  }

  /**
   * Définit la valeur de la propriété amc.
   *
   * @param value allowed object is {@link TypeAmc }
   */
  public void setAMC(TypeAmc value) {
    this.amc = value;
  }

  /**
   * Obtient la valeur de la propriété beneficiaire.
   *
   * @return possible object is {@link TypeBeneficiaireDemandeur }
   */
  public TypeBeneficiaireDemandeur getBeneficiaire() {
    return beneficiaire;
  }

  /**
   * Définit la valeur de la propriété beneficiaire.
   *
   * @param value allowed object is {@link TypeBeneficiaireDemandeur }
   */
  public void setBeneficiaire(TypeBeneficiaireDemandeur value) {
    this.beneficiaire = value;
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
    if (draftCopy instanceof TypeInfoPatient) {
      final TypeInfoPatient copy = ((TypeInfoPatient) draftCopy);
      if (this.amo != null) {
        TypeAmo sourceAMO;
        sourceAMO = this.getAMO();
        TypeAmo copyAMO =
            ((TypeAmo) strategy.copy(LocatorUtils.property(locator, "amo", sourceAMO), sourceAMO));
        copy.setAMO(copyAMO);
      } else {
        copy.amo = null;
      }
      if (this.amc != null) {
        TypeAmc sourceAMC;
        sourceAMC = this.getAMC();
        TypeAmc copyAMC =
            ((TypeAmc) strategy.copy(LocatorUtils.property(locator, "amc", sourceAMC), sourceAMC));
        copy.setAMC(copyAMC);
      } else {
        copy.amc = null;
      }
      if (this.beneficiaire != null) {
        TypeBeneficiaireDemandeur sourceBeneficiaire;
        sourceBeneficiaire = this.getBeneficiaire();
        TypeBeneficiaireDemandeur copyBeneficiaire =
            ((TypeBeneficiaireDemandeur)
                strategy.copy(
                    LocatorUtils.property(locator, "beneficiaire", sourceBeneficiaire),
                    sourceBeneficiaire));
        copy.setBeneficiaire(copyBeneficiaire);
      } else {
        copy.beneficiaire = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInfoPatient();
  }
}
