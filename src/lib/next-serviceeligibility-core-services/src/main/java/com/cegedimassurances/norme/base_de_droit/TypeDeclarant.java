package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
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
 * Classe Java pour type_declarant complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_declarant"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nom"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="siret" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="14"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numero_prefectoral" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="declarant_niveau_2" type="{http://norme.cegedimassurances.com/base_de_droit}type_declarant" minOccurs="0"/&gt;
 *         &lt;element name="adresses" type="{http://norme.cegedimassurances.com/base_de_droit}type_adresse" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_declarant",
    propOrder = {"nom", "libelle", "siret", "numeroPrefectoral", "declarantNiveau2", "adresses"})
@XmlSeeAlso({TypeDeclarantAmo.class, TypeDeclarantAmc.class})
public abstract class TypeDeclarant implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 30)
  protected String nom;

  @Size(max = 30)
  protected String libelle;

  @Size(max = 14)
  protected String siret;

  @XmlElement(name = "numero_prefectoral")
  @Size(max = 20)
  protected String numeroPrefectoral;

  @XmlElement(name = "declarant_niveau_2")
  @Valid
  protected TypeDeclarant declarantNiveau2;

  @Valid protected List<TypeAdresse> adresses;

  /**
   * Obtient la valeur de la propriété nom.
   *
   * @return possible object is {@link String }
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit la valeur de la propriété nom.
   *
   * @param value allowed object is {@link String }
   */
  public void setNom(String value) {
    this.nom = value;
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
   * Obtient la valeur de la propriété siret.
   *
   * @return possible object is {@link String }
   */
  public String getSiret() {
    return siret;
  }

  /**
   * Définit la valeur de la propriété siret.
   *
   * @param value allowed object is {@link String }
   */
  public void setSiret(String value) {
    this.siret = value;
  }

  /**
   * Obtient la valeur de la propriété numeroPrefectoral.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroPrefectoral() {
    return numeroPrefectoral;
  }

  /**
   * Définit la valeur de la propriété numeroPrefectoral.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroPrefectoral(String value) {
    this.numeroPrefectoral = value;
  }

  /**
   * Obtient la valeur de la propriété declarantNiveau2.
   *
   * @return possible object is {@link TypeDeclarant }
   */
  public TypeDeclarant getDeclarantNiveau2() {
    return declarantNiveau2;
  }

  /**
   * Définit la valeur de la propriété declarantNiveau2.
   *
   * @param value allowed object is {@link TypeDeclarant }
   */
  public void setDeclarantNiveau2(TypeDeclarant value) {
    this.declarantNiveau2 = value;
  }

  /**
   * Gets the value of the adresses property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the adresses property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getAdresses().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeAdresse }
   */
  public List<TypeAdresse> getAdresses() {
    if (adresses == null) {
      adresses = new ArrayList<TypeAdresse>();
    }
    return this.adresses;
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
    if (null == target) {
      throw new IllegalArgumentException(
          "Target argument must not be null for abstract copyable classes.");
    }
    if (target instanceof TypeDeclarant) {
      final TypeDeclarant copy = ((TypeDeclarant) target);
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
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
      if (this.siret != null) {
        String sourceSiret;
        sourceSiret = this.getSiret();
        String copySiret =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "siret", sourceSiret), sourceSiret));
        copy.setSiret(copySiret);
      } else {
        copy.siret = null;
      }
      if (this.numeroPrefectoral != null) {
        String sourceNumeroPrefectoral;
        sourceNumeroPrefectoral = this.getNumeroPrefectoral();
        String copyNumeroPrefectoral =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroPrefectoral", sourceNumeroPrefectoral),
                    sourceNumeroPrefectoral));
        copy.setNumeroPrefectoral(copyNumeroPrefectoral);
      } else {
        copy.numeroPrefectoral = null;
      }
      if (this.declarantNiveau2 != null) {
        TypeDeclarant sourceDeclarantNiveau2;
        sourceDeclarantNiveau2 = this.getDeclarantNiveau2();
        TypeDeclarant copyDeclarantNiveau2 =
            ((TypeDeclarant)
                strategy.copy(
                    LocatorUtils.property(locator, "declarantNiveau2", sourceDeclarantNiveau2),
                    sourceDeclarantNiveau2));
        copy.setDeclarantNiveau2(copyDeclarantNiveau2);
      } else {
        copy.declarantNiveau2 = null;
      }
      if ((this.adresses != null) && (!this.adresses.isEmpty())) {
        List<TypeAdresse> sourceAdresses;
        sourceAdresses =
            (((this.adresses != null) && (!this.adresses.isEmpty())) ? this.getAdresses() : null);
        @SuppressWarnings("unchecked")
        List<TypeAdresse> copyAdresses =
            ((List<TypeAdresse>)
                strategy.copy(
                    LocatorUtils.property(locator, "adresses", sourceAdresses), sourceAdresses));
        copy.adresses = null;
        if (copyAdresses != null) {
          List<TypeAdresse> uniqueAdressesl = copy.getAdresses();
          uniqueAdressesl.addAll(copyAdresses);
        }
      } else {
        copy.adresses = null;
      }
    }
    return target;
  }
}
