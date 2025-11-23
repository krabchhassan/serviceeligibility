package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_prestation complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_prestation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_regroupement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="is_edition_risque_carte" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="date_effet" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="formule" type="{http://norme.cegedimassurances.com/base_de_droit}type_formule" minOccurs="0"/&gt;
 *         &lt;element name="formule_metier" type="{http://norme.cegedimassurances.com/base_de_droit}type_formule_metier" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_prestation",
    propOrder = {
      "code",
      "codeRegroupement",
      "libelle",
      "isEditionRisqueCarte",
      "dateEffet",
      "formule",
      "formuleMetier"
    })
public class TypePrestation implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 5)
  protected String code;

  @XmlElement(name = "code_regroupement")
  @Size(max = 5)
  protected String codeRegroupement;

  @Size(max = 20)
  protected String libelle;

  @XmlElement(name = "is_edition_risque_carte")
  protected Boolean isEditionRisqueCarte;

  @XmlElement(name = "date_effet")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateEffet;

  @Valid protected TypeFormule formule;

  @XmlElement(name = "formule_metier")
  @Valid
  protected TypeFormuleMetier formuleMetier;

  /**
   * Obtient la valeur de la propriété code.
   *
   * @return possible object is {@link String }
   */
  public String getCode() {
    return code;
  }

  /**
   * Définit la valeur de la propriété code.
   *
   * @param value allowed object is {@link String }
   */
  public void setCode(String value) {
    this.code = value;
  }

  /**
   * Obtient la valeur de la propriété codeRegroupement.
   *
   * @return possible object is {@link String }
   */
  public String getCodeRegroupement() {
    return codeRegroupement;
  }

  /**
   * Définit la valeur de la propriété codeRegroupement.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeRegroupement(String value) {
    this.codeRegroupement = value;
  }

  /**
   * Obtient la valeur de la propriété libelle.
   *
   * @return possible object is {@link String }
   */
  public String getLibelle() {
    return libelle;
  }

  /**
   * Définit la valeur de la propriété libelle.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelle(String value) {
    this.libelle = value;
  }

  /**
   * Obtient la valeur de la propriété isEditionRisqueCarte.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsEditionRisqueCarte() {
    return isEditionRisqueCarte;
  }

  public Boolean getIsEditionRisqueCarte() {
    return isEditionRisqueCarte;
  }

  /**
   * Définit la valeur de la propriété isEditionRisqueCarte.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setIsEditionRisqueCarte(Boolean value) {
    this.isEditionRisqueCarte = value;
  }

  /**
   * Obtient la valeur de la propriété dateEffet.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateEffet() {
    return dateEffet;
  }

  /**
   * Définit la valeur de la propriété dateEffet.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateEffet(XMLGregorianCalendar value) {
    this.dateEffet = value;
  }

  /**
   * Obtient la valeur de la propriété formule.
   *
   * @return possible object is {@link TypeFormule }
   */
  public TypeFormule getFormule() {
    return formule;
  }

  /**
   * Définit la valeur de la propriété formule.
   *
   * @param value allowed object is {@link TypeFormule }
   */
  public void setFormule(TypeFormule value) {
    this.formule = value;
  }

  /**
   * Obtient la valeur de la propriété formuleMetier.
   *
   * @return possible object is {@link TypeFormuleMetier }
   */
  public TypeFormuleMetier getFormuleMetier() {
    return formuleMetier;
  }

  /**
   * Définit la valeur de la propriété formuleMetier.
   *
   * @param value allowed object is {@link TypeFormuleMetier }
   */
  public void setFormuleMetier(TypeFormuleMetier value) {
    this.formuleMetier = value;
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
    if (draftCopy instanceof TypePrestation) {
      final TypePrestation copy = ((TypePrestation) draftCopy);
      if (this.code != null) {
        String sourceCode;
        sourceCode = this.getCode();
        String copyCode =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "code", sourceCode), sourceCode));
        copy.setCode(copyCode);
      } else {
        copy.code = null;
      }
      if (this.codeRegroupement != null) {
        String sourceCodeRegroupement;
        sourceCodeRegroupement = this.getCodeRegroupement();
        String copyCodeRegroupement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeRegroupement", sourceCodeRegroupement),
                    sourceCodeRegroupement));
        copy.setCodeRegroupement(copyCodeRegroupement);
      } else {
        copy.codeRegroupement = null;
      }
      if (this.libelle != null) {
        String sourceLibelle;
        sourceLibelle = this.getLibelle();
        String copyLibelle =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelle", sourceLibelle), sourceLibelle));
        copy.setLibelle(copyLibelle);
      } else {
        copy.libelle = null;
      }
      if (this.isEditionRisqueCarte != null) {
        Boolean sourceIsEditionRisqueCarte;
        sourceIsEditionRisqueCarte = this.isIsEditionRisqueCarte();
        Boolean copyIsEditionRisqueCarte =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "isEditionRisqueCarte", sourceIsEditionRisqueCarte),
                    sourceIsEditionRisqueCarte));
        copy.setIsEditionRisqueCarte(copyIsEditionRisqueCarte);
      } else {
        copy.isEditionRisqueCarte = null;
      }
      if (this.dateEffet != null) {
        XMLGregorianCalendar sourceDateEffet;
        sourceDateEffet = this.getDateEffet();
        XMLGregorianCalendar copyDateEffet =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateEffet", sourceDateEffet), sourceDateEffet));
        copy.setDateEffet(copyDateEffet);
      } else {
        copy.dateEffet = null;
      }
      if (this.formule != null) {
        TypeFormule sourceFormule;
        sourceFormule = this.getFormule();
        TypeFormule copyFormule =
            ((TypeFormule)
                strategy.copy(
                    LocatorUtils.property(locator, "formule", sourceFormule), sourceFormule));
        copy.setFormule(copyFormule);
      } else {
        copy.formule = null;
      }
      if (this.formuleMetier != null) {
        TypeFormuleMetier sourceFormuleMetier;
        sourceFormuleMetier = this.getFormuleMetier();
        TypeFormuleMetier copyFormuleMetier =
            ((TypeFormuleMetier)
                strategy.copy(
                    LocatorUtils.property(locator, "formuleMetier", sourceFormuleMetier),
                    sourceFormuleMetier));
        copy.setFormuleMetier(copyFormuleMetier);
      } else {
        copy.formuleMetier = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypePrestation();
  }
}
