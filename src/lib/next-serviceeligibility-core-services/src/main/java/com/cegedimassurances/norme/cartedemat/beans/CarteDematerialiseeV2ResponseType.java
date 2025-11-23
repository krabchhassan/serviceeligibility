package com.cegedimassurances.norme.cartedemat.beans;

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
 * Classe Java pour carteDematerialiseeV2ResponseType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="carteDematerialiseeV2ResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contrat" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_contratV2"/&gt;
 *         &lt;element name="adresse_contrat" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_adresse_contrat" minOccurs="0"/&gt;
 *         &lt;element name="domaines" type="{http://norme.cegedimassurances.com/carteDemat/beans}domaines"/&gt;
 *         &lt;element name="beneficiaires" type="{http://norme.cegedimassurances.com/carteDemat/beans}beneficiaires"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "carteDematerialiseeV2ResponseType",
    propOrder = {"contrat", "adresseContrat", "domaines", "beneficiaires"})
public class CarteDematerialiseeV2ResponseType implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeContratV2 contrat;

  @XmlElement(name = "adresse_contrat")
  @Valid
  protected TypeAdresseContrat adresseContrat;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected Domaines domaines;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected Beneficiaires beneficiaires;

  /**
   * Obtient la valeur de la propriété contrat.
   *
   * @return possible object is {@link TypeContratV2 }
   */
  public TypeContratV2 getContrat() {
    return contrat;
  }

  /**
   * Définit la valeur de la propriété contrat.
   *
   * @param value allowed object is {@link TypeContratV2 }
   */
  public void setContrat(TypeContratV2 value) {
    this.contrat = value;
  }

  /**
   * Obtient la valeur de la propriété adresseContrat.
   *
   * @return possible object is {@link TypeAdresseContrat }
   */
  public TypeAdresseContrat getAdresseContrat() {
    return adresseContrat;
  }

  /**
   * Définit la valeur de la propriété adresseContrat.
   *
   * @param value allowed object is {@link TypeAdresseContrat }
   */
  public void setAdresseContrat(TypeAdresseContrat value) {
    this.adresseContrat = value;
  }

  /**
   * Obtient la valeur de la propriété domaines.
   *
   * @return possible object is {@link Domaines }
   */
  public Domaines getDomaines() {
    return domaines;
  }

  /**
   * Définit la valeur de la propriété domaines.
   *
   * @param value allowed object is {@link Domaines }
   */
  public void setDomaines(Domaines value) {
    this.domaines = value;
  }

  /**
   * Obtient la valeur de la propriété beneficiaires.
   *
   * @return possible object is {@link Beneficiaires }
   */
  public Beneficiaires getBeneficiaires() {
    return beneficiaires;
  }

  /**
   * Définit la valeur de la propriété beneficiaires.
   *
   * @param value allowed object is {@link Beneficiaires }
   */
  public void setBeneficiaires(Beneficiaires value) {
    this.beneficiaires = value;
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
    if (draftCopy instanceof CarteDematerialiseeV2ResponseType) {
      final CarteDematerialiseeV2ResponseType copy =
          ((CarteDematerialiseeV2ResponseType) draftCopy);
      if (this.contrat != null) {
        TypeContratV2 sourceContrat;
        sourceContrat = this.getContrat();
        TypeContratV2 copyContrat =
            ((TypeContratV2)
                strategy.copy(
                    LocatorUtils.property(locator, "contrat", sourceContrat), sourceContrat));
        copy.setContrat(copyContrat);
      } else {
        copy.contrat = null;
      }
      if (this.adresseContrat != null) {
        TypeAdresseContrat sourceAdresseContrat;
        sourceAdresseContrat = this.getAdresseContrat();
        TypeAdresseContrat copyAdresseContrat =
            ((TypeAdresseContrat)
                strategy.copy(
                    LocatorUtils.property(locator, "adresseContrat", sourceAdresseContrat),
                    sourceAdresseContrat));
        copy.setAdresseContrat(copyAdresseContrat);
      } else {
        copy.adresseContrat = null;
      }
      if (this.domaines != null) {
        Domaines sourceDomaines;
        sourceDomaines = this.getDomaines();
        Domaines copyDomaines =
            ((Domaines)
                strategy.copy(
                    LocatorUtils.property(locator, "domaines", sourceDomaines), sourceDomaines));
        copy.setDomaines(copyDomaines);
      } else {
        copy.domaines = null;
      }
      if (this.beneficiaires != null) {
        Beneficiaires sourceBeneficiaires;
        sourceBeneficiaires = this.getBeneficiaires();
        Beneficiaires copyBeneficiaires =
            ((Beneficiaires)
                strategy.copy(
                    LocatorUtils.property(locator, "beneficiaires", sourceBeneficiaires),
                    sourceBeneficiaires));
        copy.setBeneficiaires(copyBeneficiaires);
      } else {
        copy.beneficiaires = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new CarteDematerialiseeV2ResponseType();
  }

  /**
   * Generates a String representation of the contents of this type. This is an extension method,
   * produced by the 'ts' xjc plugin
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
  }
}
