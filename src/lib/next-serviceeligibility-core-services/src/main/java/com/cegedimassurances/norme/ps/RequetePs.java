package com.cegedimassurances.norme.ps;

import com.cegedimactiv.norme.fsiq.api.RequeteWsFsiq;
import com.cegedimassurances.norme.amc.TypeAmc;
import com.cegedimassurances.norme.beneficiaire.TypeBeneficiaireDemandeur;
import com.cegedimassurances.norme.commun.TypeDates;
import com.cegedimassurances.norme.commun.TypeHeaderIn;
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
 * Classe Java pour requete_ps complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="requete_ps"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_in" type="{http://norme.cegedimassurances.com/commun}type_header_in"/&gt;
 *         &lt;element name="ps" type="{http://norme.cegedimassurances.com/ps}type_ps_identifiant"/&gt;
 *         &lt;element name="amc" type="{http://norme.cegedimassurances.com/amc}type_amc"/&gt;
 *         &lt;element name="beneficiaire" type="{http://norme.cegedimassurances.com/beneficiaire}type_beneficiaire_demandeur" minOccurs="0"/&gt;
 *         &lt;element name="dates" type="{http://norme.cegedimassurances.com/commun}type_dates"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "requete_ps",
    propOrder = {"headerIn", "ps", "amc", "beneficiaire", "dates"})
public class RequetePs implements Serializable, Cloneable, RequeteWsFsiq, CopyTo {

  @XmlElement(name = "header_in", required = true)
  @NotNull
  @Valid
  protected TypeHeaderIn headerIn;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypePsIdentifiant ps;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeAmc amc;

  @Valid protected TypeBeneficiaireDemandeur beneficiaire;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeDates dates;

  /**
   * Obtient la valeur de la propriété headerIn.
   *
   * @return possible object is {@link TypeHeaderIn }
   */
  public TypeHeaderIn getHeaderIn() {
    return headerIn;
  }

  /**
   * Définit la valeur de la propriété headerIn.
   *
   * @param value allowed object is {@link TypeHeaderIn }
   */
  public void setHeaderIn(TypeHeaderIn value) {
    this.headerIn = value;
  }

  /**
   * Obtient la valeur de la propriété ps.
   *
   * @return possible object is {@link TypePsIdentifiant }
   */
  public TypePsIdentifiant getPs() {
    return ps;
  }

  /**
   * Définit la valeur de la propriété ps.
   *
   * @param value allowed object is {@link TypePsIdentifiant }
   */
  public void setPs(TypePsIdentifiant value) {
    this.ps = value;
  }

  /**
   * Obtient la valeur de la propriété amc.
   *
   * @return possible object is {@link TypeAmc }
   */
  public TypeAmc getAmc() {
    return amc;
  }

  /**
   * Définit la valeur de la propriété amc.
   *
   * @param value allowed object is {@link TypeAmc }
   */
  public void setAmc(TypeAmc value) {
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
   * Obtient la valeur de la propriété dates.
   *
   * @return possible object is {@link TypeDates }
   */
  public TypeDates getDates() {
    return dates;
  }

  /**
   * Définit la valeur de la propriété dates.
   *
   * @param value allowed object is {@link TypeDates }
   */
  public void setDates(TypeDates value) {
    this.dates = value;
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
    if (draftCopy instanceof RequetePs) {
      final RequetePs copy = ((RequetePs) draftCopy);
      if (this.headerIn != null) {
        TypeHeaderIn sourceHeaderIn;
        sourceHeaderIn = this.getHeaderIn();
        TypeHeaderIn copyHeaderIn =
            ((TypeHeaderIn)
                strategy.copy(
                    LocatorUtils.property(locator, "headerIn", sourceHeaderIn), sourceHeaderIn));
        copy.setHeaderIn(copyHeaderIn);
      } else {
        copy.headerIn = null;
      }
      if (this.ps != null) {
        TypePsIdentifiant sourcePs;
        sourcePs = this.getPs();
        TypePsIdentifiant copyPs =
            ((TypePsIdentifiant)
                strategy.copy(LocatorUtils.property(locator, "ps", sourcePs), sourcePs));
        copy.setPs(copyPs);
      } else {
        copy.ps = null;
      }
      if (this.amc != null) {
        TypeAmc sourceAmc;
        sourceAmc = this.getAmc();
        TypeAmc copyAmc =
            ((TypeAmc) strategy.copy(LocatorUtils.property(locator, "amc", sourceAmc), sourceAmc));
        copy.setAmc(copyAmc);
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
      if (this.dates != null) {
        TypeDates sourceDates;
        sourceDates = this.getDates();
        TypeDates copyDates =
            ((TypeDates)
                strategy.copy(LocatorUtils.property(locator, "dates", sourceDates), sourceDates));
        copy.setDates(copyDates);
      } else {
        copy.dates = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new RequetePs();
  }
}
