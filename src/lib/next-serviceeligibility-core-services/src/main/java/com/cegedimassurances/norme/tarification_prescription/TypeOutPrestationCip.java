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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_out_prestation_cip complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_out_prestation_cip"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_cip" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="type_code_cip" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="code_cip"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="7"/&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="quantite_pec"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_facture"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="montant_pec"&gt;
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
 *         &lt;element name="type_codeReponse_prestation" type="{http://norme.cegedimassurances.com/tarification-prescription}type_codeReponse_prestation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_out_prestation_cip",
    propOrder = {
      "idCip",
      "typeCodeCip",
      "codeCip",
      "quantitePec",
      "montantFacture",
      "montantPec",
      "montantRac",
      "typeCodeReponsePrestation"
    })
public class TypeOutPrestationCip implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_cip", required = true)
  @NotNull
  protected String idCip;

  @XmlElement(name = "type_code_cip", required = true)
  @NotNull
  protected String typeCodeCip;

  @XmlElement(name = "code_cip", required = true)
  @NotNull
  @Size(min = 7, max = 13)
  protected String codeCip;

  @XmlElement(name = "quantite_pec")
  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int quantitePec;

  @XmlElement(name = "montant_facture", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantFacture;

  @XmlElement(name = "montant_pec", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantPec;

  @XmlElement(name = "montant_rac", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantRac;

  @XmlElement(name = "type_codeReponse_prestation")
  @Valid
  protected TypeCodeReponsePrestation typeCodeReponsePrestation;

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

  /** Obtient la valeur de la propriété quantitePec. */
  public int getQuantitePec() {
    return quantitePec;
  }

  /** Définit la valeur de la propriété quantitePec. */
  public void setQuantitePec(int value) {
    this.quantitePec = value;
  }

  /**
   * Obtient la valeur de la propriété montantFacture.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantFacture() {
    return montantFacture;
  }

  /**
   * Définit la valeur de la propriété montantFacture.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantFacture(BigDecimal value) {
    this.montantFacture = value;
  }

  /**
   * Obtient la valeur de la propriété montantPec.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getMontantPec() {
    return montantPec;
  }

  /**
   * Définit la valeur de la propriété montantPec.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setMontantPec(BigDecimal value) {
    this.montantPec = value;
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
   * Obtient la valeur de la propriété typeCodeReponsePrestation.
   *
   * @return possible object is {@link TypeCodeReponsePrestation }
   */
  public TypeCodeReponsePrestation getTypeCodeReponsePrestation() {
    return typeCodeReponsePrestation;
  }

  /**
   * Définit la valeur de la propriété typeCodeReponsePrestation.
   *
   * @param value allowed object is {@link TypeCodeReponsePrestation }
   */
  public void setTypeCodeReponsePrestation(TypeCodeReponsePrestation value) {
    this.typeCodeReponsePrestation = value;
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
    if (draftCopy instanceof TypeOutPrestationCip) {
      final TypeOutPrestationCip copy = ((TypeOutPrestationCip) draftCopy);
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
      int sourceQuantitePec;
      sourceQuantitePec = (true ? this.getQuantitePec() : 0);
      int copyQuantitePec =
          strategy.copy(
              LocatorUtils.property(locator, "quantitePec", sourceQuantitePec), sourceQuantitePec);
      copy.setQuantitePec(copyQuantitePec);
      if (this.montantFacture != null) {
        BigDecimal sourceMontantFacture;
        sourceMontantFacture = this.getMontantFacture();
        BigDecimal copyMontantFacture =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantFacture", sourceMontantFacture),
                    sourceMontantFacture));
        copy.setMontantFacture(copyMontantFacture);
      } else {
        copy.montantFacture = null;
      }
      if (this.montantPec != null) {
        BigDecimal sourceMontantPec;
        sourceMontantPec = this.getMontantPec();
        BigDecimal copyMontantPec =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(locator, "montantPec", sourceMontantPec),
                    sourceMontantPec));
        copy.setMontantPec(copyMontantPec);
      } else {
        copy.montantPec = null;
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
      if (this.typeCodeReponsePrestation != null) {
        TypeCodeReponsePrestation sourceTypeCodeReponsePrestation;
        sourceTypeCodeReponsePrestation = this.getTypeCodeReponsePrestation();
        TypeCodeReponsePrestation copyTypeCodeReponsePrestation =
            ((TypeCodeReponsePrestation)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeCodeReponsePrestation", sourceTypeCodeReponsePrestation),
                    sourceTypeCodeReponsePrestation));
        copy.setTypeCodeReponsePrestation(copyTypeCodeReponsePrestation);
      } else {
        copy.typeCodeReponsePrestation = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeOutPrestationCip();
  }
}
