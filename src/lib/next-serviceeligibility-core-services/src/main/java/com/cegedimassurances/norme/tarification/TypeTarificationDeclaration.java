package com.cegedimassurances.norme.tarification;

import com.cegedimassurances.norme.base_de_droit.TypeDomaineDroit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 * Classe Java pour type_tarification_declaration complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_tarification_declaration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="declarant_amc" type="{http://norme.cegedimassurances.com/tarification}type_tarification_declarant_amc"/&gt;
 *         &lt;element name="beneficiaire" type="{http://norme.cegedimassurances.com/tarification}type_tarification_beneficiaire"/&gt;
 *         &lt;element name="contrat" type="{http://norme.cegedimassurances.com/tarification}type_tarification_contrat"/&gt;
 *         &lt;element name="domaines_droits" type="{http://norme.cegedimassurances.com/base_de_droit}type_domaine_droit" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_tarification_declaration",
    propOrder = {"declarantAmc", "beneficiaire", "contrat", "domainesDroits"})
public class TypeTarificationDeclaration implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "declarant_amc", required = true)
  @NotNull
  @Valid
  protected TypeTarificationDeclarantAmc declarantAmc;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeTarificationBeneficiaire beneficiaire;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeTarificationContrat contrat;

  @XmlElement(name = "domaines_droits", required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeDomaineDroit> domainesDroits;

  /**
   * Obtient la valeur de la propriété declarantAmc.
   *
   * @return possible object is {@link TypeTarificationDeclarantAmc }
   */
  public TypeTarificationDeclarantAmc getDeclarantAmc() {
    return declarantAmc;
  }

  /**
   * Définit la valeur de la propriété declarantAmc.
   *
   * @param value allowed object is {@link TypeTarificationDeclarantAmc }
   */
  public void setDeclarantAmc(TypeTarificationDeclarantAmc value) {
    this.declarantAmc = value;
  }

  /**
   * Obtient la valeur de la propriété beneficiaire.
   *
   * @return possible object is {@link TypeTarificationBeneficiaire }
   */
  public TypeTarificationBeneficiaire getBeneficiaire() {
    return beneficiaire;
  }

  /**
   * Définit la valeur de la propriété beneficiaire.
   *
   * @param value allowed object is {@link TypeTarificationBeneficiaire }
   */
  public void setBeneficiaire(TypeTarificationBeneficiaire value) {
    this.beneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété contrat.
   *
   * @return possible object is {@link TypeTarificationContrat }
   */
  public TypeTarificationContrat getContrat() {
    return contrat;
  }

  /**
   * Définit la valeur de la propriété contrat.
   *
   * @param value allowed object is {@link TypeTarificationContrat }
   */
  public void setContrat(TypeTarificationContrat value) {
    this.contrat = value;
  }

  /**
   * Gets the value of the domainesDroits property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the domainesDroits property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getDomainesDroits().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeDomaineDroit }
   */
  public List<TypeDomaineDroit> getDomainesDroits() {
    if (domainesDroits == null) {
      domainesDroits = new ArrayList<TypeDomaineDroit>();
    }
    return this.domainesDroits;
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
    if (draftCopy instanceof TypeTarificationDeclaration) {
      final TypeTarificationDeclaration copy = ((TypeTarificationDeclaration) draftCopy);
      if (this.declarantAmc != null) {
        TypeTarificationDeclarantAmc sourceDeclarantAmc;
        sourceDeclarantAmc = this.getDeclarantAmc();
        TypeTarificationDeclarantAmc copyDeclarantAmc =
            ((TypeTarificationDeclarantAmc)
                strategy.copy(
                    LocatorUtils.property(locator, "declarantAmc", sourceDeclarantAmc),
                    sourceDeclarantAmc));
        copy.setDeclarantAmc(copyDeclarantAmc);
      } else {
        copy.declarantAmc = null;
      }
      if (this.beneficiaire != null) {
        TypeTarificationBeneficiaire sourceBeneficiaire;
        sourceBeneficiaire = this.getBeneficiaire();
        TypeTarificationBeneficiaire copyBeneficiaire =
            ((TypeTarificationBeneficiaire)
                strategy.copy(
                    LocatorUtils.property(locator, "beneficiaire", sourceBeneficiaire),
                    sourceBeneficiaire));
        copy.setBeneficiaire(copyBeneficiaire);
      } else {
        copy.beneficiaire = null;
      }
      if (this.contrat != null) {
        TypeTarificationContrat sourceContrat;
        sourceContrat = this.getContrat();
        TypeTarificationContrat copyContrat =
            ((TypeTarificationContrat)
                strategy.copy(
                    LocatorUtils.property(locator, "contrat", sourceContrat), sourceContrat));
        copy.setContrat(copyContrat);
      } else {
        copy.contrat = null;
      }
      if ((this.domainesDroits != null) && (!this.domainesDroits.isEmpty())) {
        List<TypeDomaineDroit> sourceDomainesDroits;
        sourceDomainesDroits =
            (((this.domainesDroits != null) && (!this.domainesDroits.isEmpty()))
                ? this.getDomainesDroits()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeDomaineDroit> copyDomainesDroits =
            ((List<TypeDomaineDroit>)
                strategy.copy(
                    LocatorUtils.property(locator, "domainesDroits", sourceDomainesDroits),
                    sourceDomainesDroits));
        copy.domainesDroits = null;
        if (copyDomainesDroits != null) {
          List<TypeDomaineDroit> uniqueDomainesDroitsl = copy.getDomainesDroits();
          uniqueDomainesDroitsl.addAll(copyDomainesDroits);
        }
      } else {
        copy.domainesDroits = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeTarificationDeclaration();
  }
}
