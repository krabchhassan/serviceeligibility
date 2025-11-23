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
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_declaration complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_declaration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_interne" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="reference_externe" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_etat" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="effet_debut" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="declarant_amc" type="{http://norme.cegedimassurances.com/base_de_droit}type_declarant_amc" minOccurs="0"/&gt;
 *         &lt;element name="declarant_amo" type="{http://norme.cegedimassurances.com/base_de_droit}type_declarant_amo" minOccurs="0"/&gt;
 *         &lt;element name="beneficiaire" type="{http://norme.cegedimassurances.com/base_de_droit}type_beneficiaire"/&gt;
 *         &lt;element name="contrat" type="{http://norme.cegedimassurances.com/base_de_droit}type_contrat"/&gt;
 *         &lt;element name="domaine_droits" type="{http://norme.cegedimassurances.com/base_de_droit}type_domaine_droit" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_declaration",
    propOrder = {
      "idInterne",
      "referenceExterne",
      "codeEtat",
      "effetDebut",
      "declarantAmc",
      "declarantAmo",
      "beneficiaire",
      "contrat",
      "domaineDroits"
    })
public class TypeDeclaration implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_interne")
  @Size(max = 3)
  protected String idInterne;

  @XmlElement(name = "reference_externe")
  @Size(max = 45)
  protected String referenceExterne;

  @XmlElement(name = "code_etat")
  @Size(max = 1)
  protected String codeEtat;

  @XmlElement(name = "effet_debut", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar effetDebut;

  @XmlElement(name = "declarant_amc")
  @Valid
  protected TypeDeclarantAmc declarantAmc;

  @XmlElement(name = "declarant_amo")
  @Valid
  protected TypeDeclarantAmo declarantAmo;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeBeneficiaire beneficiaire;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeContrat contrat;

  @XmlElement(name = "domaine_droits")
  @Valid
  protected List<TypeDomaineDroit> domaineDroits;

  /**
   * Obtient la valeur de la propriété idInterne.
   *
   * @return possible object is {@link String }
   */
  public String getIdInterne() {
    return idInterne;
  }

  /**
   * Définit la valeur de la propriété idInterne.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdInterne(String value) {
    this.idInterne = value;
  }

  /**
   * Obtient la valeur de la propriété referenceExterne.
   *
   * @return possible object is {@link String }
   */
  public String getReferenceExterne() {
    return referenceExterne;
  }

  /**
   * Définit la valeur de la propriété referenceExterne.
   *
   * @param value allowed object is {@link String }
   */
  public void setReferenceExterne(String value) {
    this.referenceExterne = value;
  }

  /**
   * Obtient la valeur de la propriété codeEtat.
   *
   * @return possible object is {@link String }
   */
  public String getCodeEtat() {
    return codeEtat;
  }

  /**
   * Définit la valeur de la propriété codeEtat.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeEtat(String value) {
    this.codeEtat = value;
  }

  /**
   * Obtient la valeur de la propriété effetDebut.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getEffetDebut() {
    return effetDebut;
  }

  /**
   * Définit la valeur de la propriété effetDebut.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setEffetDebut(XMLGregorianCalendar value) {
    this.effetDebut = value;
  }

  /**
   * Obtient la valeur de la propriété declarantAmc.
   *
   * @return possible object is {@link TypeDeclarantAmc }
   */
  public TypeDeclarantAmc getDeclarantAmc() {
    return declarantAmc;
  }

  /**
   * Définit la valeur de la propriété declarantAmc.
   *
   * @param value allowed object is {@link TypeDeclarantAmc }
   */
  public void setDeclarantAmc(TypeDeclarantAmc value) {
    this.declarantAmc = value;
  }

  /**
   * Obtient la valeur de la propriété declarantAmo.
   *
   * @return possible object is {@link TypeDeclarantAmo }
   */
  public TypeDeclarantAmo getDeclarantAmo() {
    return declarantAmo;
  }

  /**
   * Définit la valeur de la propriété declarantAmo.
   *
   * @param value allowed object is {@link TypeDeclarantAmo }
   */
  public void setDeclarantAmo(TypeDeclarantAmo value) {
    this.declarantAmo = value;
  }

  /**
   * Obtient la valeur de la propriété beneficiaire.
   *
   * @return possible object is {@link TypeBeneficiaire }
   */
  public TypeBeneficiaire getBeneficiaire() {
    return beneficiaire;
  }

  /**
   * Définit la valeur de la propriété beneficiaire.
   *
   * @param value allowed object is {@link TypeBeneficiaire }
   */
  public void setBeneficiaire(TypeBeneficiaire value) {
    this.beneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété contrat.
   *
   * @return possible object is {@link TypeContrat }
   */
  public TypeContrat getContrat() {
    return contrat;
  }

  /**
   * Définit la valeur de la propriété contrat.
   *
   * @param value allowed object is {@link TypeContrat }
   */
  public void setContrat(TypeContrat value) {
    this.contrat = value;
  }

  /**
   * Gets the value of the domaineDroits property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the domaineDroits property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getDomaineDroits().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeDomaineDroit }
   */
  public List<TypeDomaineDroit> getDomaineDroits() {
    if (domaineDroits == null) {
      domaineDroits = new ArrayList<TypeDomaineDroit>();
    }
    return this.domaineDroits;
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
    if (draftCopy instanceof TypeDeclaration) {
      final TypeDeclaration copy = ((TypeDeclaration) draftCopy);
      if (this.idInterne != null) {
        String sourceIdInterne;
        sourceIdInterne = this.getIdInterne();
        String copyIdInterne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idInterne", sourceIdInterne), sourceIdInterne));
        copy.setIdInterne(copyIdInterne);
      } else {
        copy.idInterne = null;
      }
      if (this.referenceExterne != null) {
        String sourceReferenceExterne;
        sourceReferenceExterne = this.getReferenceExterne();
        String copyReferenceExterne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "referenceExterne", sourceReferenceExterne),
                    sourceReferenceExterne));
        copy.setReferenceExterne(copyReferenceExterne);
      } else {
        copy.referenceExterne = null;
      }
      if (this.codeEtat != null) {
        String sourceCodeEtat;
        sourceCodeEtat = this.getCodeEtat();
        String copyCodeEtat =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeEtat", sourceCodeEtat), sourceCodeEtat));
        copy.setCodeEtat(copyCodeEtat);
      } else {
        copy.codeEtat = null;
      }
      if (this.effetDebut != null) {
        XMLGregorianCalendar sourceEffetDebut;
        sourceEffetDebut = this.getEffetDebut();
        XMLGregorianCalendar copyEffetDebut =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "effetDebut", sourceEffetDebut),
                    sourceEffetDebut));
        copy.setEffetDebut(copyEffetDebut);
      } else {
        copy.effetDebut = null;
      }
      if (this.declarantAmc != null) {
        TypeDeclarantAmc sourceDeclarantAmc;
        sourceDeclarantAmc = this.getDeclarantAmc();
        TypeDeclarantAmc copyDeclarantAmc =
            ((TypeDeclarantAmc)
                strategy.copy(
                    LocatorUtils.property(locator, "declarantAmc", sourceDeclarantAmc),
                    sourceDeclarantAmc));
        copy.setDeclarantAmc(copyDeclarantAmc);
      } else {
        copy.declarantAmc = null;
      }
      if (this.declarantAmo != null) {
        TypeDeclarantAmo sourceDeclarantAmo;
        sourceDeclarantAmo = this.getDeclarantAmo();
        TypeDeclarantAmo copyDeclarantAmo =
            ((TypeDeclarantAmo)
                strategy.copy(
                    LocatorUtils.property(locator, "declarantAmo", sourceDeclarantAmo),
                    sourceDeclarantAmo));
        copy.setDeclarantAmo(copyDeclarantAmo);
      } else {
        copy.declarantAmo = null;
      }
      if (this.beneficiaire != null) {
        TypeBeneficiaire sourceBeneficiaire;
        sourceBeneficiaire = this.getBeneficiaire();
        TypeBeneficiaire copyBeneficiaire =
            ((TypeBeneficiaire)
                strategy.copy(
                    LocatorUtils.property(locator, "beneficiaire", sourceBeneficiaire),
                    sourceBeneficiaire));
        copy.setBeneficiaire(copyBeneficiaire);
      } else {
        copy.beneficiaire = null;
      }
      if (this.contrat != null) {
        TypeContrat sourceContrat;
        sourceContrat = this.getContrat();
        TypeContrat copyContrat =
            ((TypeContrat)
                strategy.copy(
                    LocatorUtils.property(locator, "contrat", sourceContrat), sourceContrat));
        copy.setContrat(copyContrat);
      } else {
        copy.contrat = null;
      }
      if ((this.domaineDroits != null) && (!this.domaineDroits.isEmpty())) {
        List<TypeDomaineDroit> sourceDomaineDroits;
        sourceDomaineDroits =
            (((this.domaineDroits != null) && (!this.domaineDroits.isEmpty()))
                ? this.getDomaineDroits()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeDomaineDroit> copyDomaineDroits =
            ((List<TypeDomaineDroit>)
                strategy.copy(
                    LocatorUtils.property(locator, "domaineDroits", sourceDomaineDroits),
                    sourceDomaineDroits));
        copy.domaineDroits = null;
        if (copyDomaineDroits != null) {
          List<TypeDomaineDroit> uniqueDomaineDroitsl = copy.getDomaineDroits();
          uniqueDomaineDroitsl.addAll(copyDomaineDroits);
        }
      } else {
        copy.domaineDroits = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDeclaration();
  }
}
