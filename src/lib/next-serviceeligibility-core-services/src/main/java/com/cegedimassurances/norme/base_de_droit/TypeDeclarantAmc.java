package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
 * Classe Java pour type_declarant_amc complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_declarant_amc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://norme.cegedimassurances.com/base_de_droit}type_declarant"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroRNM"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="isInscritDansAnnuaireAMC" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_declarant_amc",
    propOrder = {"numeroRNM", "isInscritDansAnnuaireAMC"})
public class TypeDeclarantAmc extends TypeDeclarant implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 10)
  protected String numeroRNM;

  @NotNull protected boolean isInscritDansAnnuaireAMC;

  /**
   * Obtient la valeur de la propriété numeroRNM.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroRNM() {
    return numeroRNM;
  }

  /**
   * Définit la valeur de la propriété numeroRNM.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroRNM(String value) {
    this.numeroRNM = value;
  }

  /** Obtient la valeur de la propriété isInscritDansAnnuaireAMC. */
  public boolean isIsInscritDansAnnuaireAMC() {
    return isInscritDansAnnuaireAMC;
  }

  /** Définit la valeur de la propriété isInscritDansAnnuaireAMC. */
  public void setIsInscritDansAnnuaireAMC(boolean value) {
    this.isInscritDansAnnuaireAMC = value;
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
    super.copyTo(locator, draftCopy, strategy);
    if (draftCopy instanceof TypeDeclarantAmc) {
      final TypeDeclarantAmc copy = ((TypeDeclarantAmc) draftCopy);
      if (this.numeroRNM != null) {
        String sourceNumeroRNM;
        sourceNumeroRNM = this.getNumeroRNM();
        String copyNumeroRNM =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroRNM", sourceNumeroRNM), sourceNumeroRNM));
        copy.setNumeroRNM(copyNumeroRNM);
      } else {
        copy.numeroRNM = null;
      }
      boolean sourceIsInscritDansAnnuaireAMC;
      sourceIsInscritDansAnnuaireAMC = (true ? this.isIsInscritDansAnnuaireAMC() : false);
      boolean copyIsInscritDansAnnuaireAMC =
          strategy.copy(
              LocatorUtils.property(
                  locator, "isInscritDansAnnuaireAMC", sourceIsInscritDansAnnuaireAMC),
              sourceIsInscritDansAnnuaireAMC);
      copy.setIsInscritDansAnnuaireAMC(copyIsInscritDansAnnuaireAMC);
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDeclarantAmc();
  }
}
