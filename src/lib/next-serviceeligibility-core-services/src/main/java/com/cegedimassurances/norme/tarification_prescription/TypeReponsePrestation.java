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
 * Classe Java pour type_reponse_prestation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_reponse_prestation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_prestation"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="3"/&gt;
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
 *         &lt;element name="code_ebs" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="type_out_prestation_cip" type="{http://norme.cegedimassurances.com/tarification-prescription}type_out_prestation_cip" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="type_out_prestation_lpp" type="{http://norme.cegedimassurances.com/tarification-prescription}type_out_prestation_lpp" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_reponse_prestation",
    propOrder = {
      "idPrestation",
      "montantPec",
      "montantRac",
      "codeEbs",
      "typeOutPrestationCip",
      "typeOutPrestationLpp"
    })
public class TypeReponsePrestation implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_prestation")
  @NotNull
  @Digits(integer = 3, fraction = 0)
  protected int idPrestation;

  @XmlElement(name = "montant_pec", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantPec;

  @XmlElement(name = "montant_rac", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal montantRac;

  @XmlElement(name = "code_ebs")
  @Size(max = 30)
  protected String codeEbs;

  @XmlElement(name = "type_out_prestation_cip")
  @Valid
  protected List<TypeOutPrestationCip> typeOutPrestationCip;

  @XmlElement(name = "type_out_prestation_lpp")
  @Valid
  protected List<TypeOutPrestationLpp> typeOutPrestationLpp;

  /** Obtient la valeur de la propriété idPrestation. */
  public int getIdPrestation() {
    return idPrestation;
  }

  /** Définit la valeur de la propriété idPrestation. */
  public void setIdPrestation(int value) {
    this.idPrestation = value;
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
   * Obtient la valeur de la propriété codeEbs.
   *
   * @return possible object is {@link String }
   */
  public String getCodeEbs() {
    return codeEbs;
  }

  /**
   * Définit la valeur de la propriété codeEbs.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeEbs(String value) {
    this.codeEbs = value;
  }

  /**
   * Gets the value of the typeOutPrestationCip property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the typeOutPrestationCip property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getTypeOutPrestationCip().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeOutPrestationCip }
   */
  public List<TypeOutPrestationCip> getTypeOutPrestationCip() {
    if (typeOutPrestationCip == null) {
      typeOutPrestationCip = new ArrayList<TypeOutPrestationCip>();
    }
    return this.typeOutPrestationCip;
  }

  /**
   * Gets the value of the typeOutPrestationLpp property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the typeOutPrestationLpp property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getTypeOutPrestationLpp().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeOutPrestationLpp }
   */
  public List<TypeOutPrestationLpp> getTypeOutPrestationLpp() {
    if (typeOutPrestationLpp == null) {
      typeOutPrestationLpp = new ArrayList<TypeOutPrestationLpp>();
    }
    return this.typeOutPrestationLpp;
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
    if (draftCopy instanceof TypeReponsePrestation) {
      final TypeReponsePrestation copy = ((TypeReponsePrestation) draftCopy);
      int sourceIdPrestation;
      sourceIdPrestation = (true ? this.getIdPrestation() : 0);
      int copyIdPrestation =
          strategy.copy(
              LocatorUtils.property(locator, "idPrestation", sourceIdPrestation),
              sourceIdPrestation);
      copy.setIdPrestation(copyIdPrestation);
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
      if (this.codeEbs != null) {
        String sourceCodeEbs;
        sourceCodeEbs = this.getCodeEbs();
        String copyCodeEbs =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeEbs", sourceCodeEbs), sourceCodeEbs));
        copy.setCodeEbs(copyCodeEbs);
      } else {
        copy.codeEbs = null;
      }
      if ((this.typeOutPrestationCip != null) && (!this.typeOutPrestationCip.isEmpty())) {
        List<TypeOutPrestationCip> sourceTypeOutPrestationCip;
        sourceTypeOutPrestationCip =
            (((this.typeOutPrestationCip != null) && (!this.typeOutPrestationCip.isEmpty()))
                ? this.getTypeOutPrestationCip()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeOutPrestationCip> copyTypeOutPrestationCip =
            ((List<TypeOutPrestationCip>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeOutPrestationCip", sourceTypeOutPrestationCip),
                    sourceTypeOutPrestationCip));
        copy.typeOutPrestationCip = null;
        if (copyTypeOutPrestationCip != null) {
          List<TypeOutPrestationCip> uniqueTypeOutPrestationCipl = copy.getTypeOutPrestationCip();
          uniqueTypeOutPrestationCipl.addAll(copyTypeOutPrestationCip);
        }
      } else {
        copy.typeOutPrestationCip = null;
      }
      if ((this.typeOutPrestationLpp != null) && (!this.typeOutPrestationLpp.isEmpty())) {
        List<TypeOutPrestationLpp> sourceTypeOutPrestationLpp;
        sourceTypeOutPrestationLpp =
            (((this.typeOutPrestationLpp != null) && (!this.typeOutPrestationLpp.isEmpty()))
                ? this.getTypeOutPrestationLpp()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeOutPrestationLpp> copyTypeOutPrestationLpp =
            ((List<TypeOutPrestationLpp>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeOutPrestationLpp", sourceTypeOutPrestationLpp),
                    sourceTypeOutPrestationLpp));
        copy.typeOutPrestationLpp = null;
        if (copyTypeOutPrestationLpp != null) {
          List<TypeOutPrestationLpp> uniqueTypeOutPrestationLppl = copy.getTypeOutPrestationLpp();
          uniqueTypeOutPrestationLppl.addAll(copyTypeOutPrestationLpp);
        }
      } else {
        copy.typeOutPrestationLpp = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeReponsePrestation();
  }
}
