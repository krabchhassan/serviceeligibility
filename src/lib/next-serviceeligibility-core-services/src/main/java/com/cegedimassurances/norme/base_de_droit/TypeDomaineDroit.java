package com.cegedimassurances.norme.base_de_droit;

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
 * Classe Java pour type_domaine_droit complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_domaine_droit"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_externe" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_externe_produit" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="35"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle_externe" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_option_mutualiste" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle_option_mutualiste" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_produit" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle_produit" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_garantie" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle_garantie" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="taux_remboursement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle_code_renvoi" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="reference_couverture" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="priorite_droit" type="{http://norme.cegedimassurances.com/base_de_droit}type_priorite_droit"/&gt;
 *         &lt;element name="historique_periode_droits" type="{http://norme.cegedimassurances.com/base_de_droit}type_historique_periode_droit" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="conventionnements" type="{http://norme.cegedimassurances.com/base_de_droit}type_conventionnement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="prestations" type="{http://norme.cegedimassurances.com/base_de_droit}type_prestation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_domaine_droit",
    propOrder = {
      "code",
      "codeExterne",
      "libelle",
      "codeExterneProduit",
      "libelleExterne",
      "codeOptionMutualiste",
      "libelleOptionMutualiste",
      "codeProduit",
      "libelleProduit",
      "codeGarantie",
      "libelleGarantie",
      "tauxRemboursement",
      "libelleCodeRenvoi",
      "referenceCouverture",
      "prioriteDroit",
      "historiquePeriodeDroits",
      "conventionnements",
      "prestations"
    })
public class TypeDomaineDroit implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 8)
  protected String code;

  @XmlElement(name = "code_externe")
  @Size(max = 8)
  protected String codeExterne;

  @Size(max = 45)
  protected String libelle;

  @XmlElement(name = "code_externe_produit")
  @Size(max = 35)
  protected String codeExterneProduit;

  @XmlElement(name = "libelle_externe")
  @Size(max = 45)
  protected String libelleExterne;

  @XmlElement(name = "code_option_mutualiste")
  @Size(max = 45)
  protected String codeOptionMutualiste;

  @XmlElement(name = "libelle_option_mutualiste")
  @Size(max = 50)
  protected String libelleOptionMutualiste;

  @XmlElement(name = "code_produit")
  @Size(max = 45)
  protected String codeProduit;

  @XmlElement(name = "libelle_produit")
  @Size(max = 50)
  protected String libelleProduit;

  @XmlElement(name = "code_garantie")
  @Size(max = 45)
  protected String codeGarantie;

  @XmlElement(name = "libelle_garantie")
  @Size(max = 50)
  protected String libelleGarantie;

  @XmlElement(name = "taux_remboursement")
  @Size(max = 15)
  protected String tauxRemboursement;

  @XmlElement(name = "libelle_code_renvoi")
  @Size(max = 50)
  protected String libelleCodeRenvoi;

  @XmlElement(name = "reference_couverture")
  @Size(max = 50)
  protected String referenceCouverture;

  @XmlElement(name = "priorite_droit", required = true)
  @NotNull
  @Valid
  protected TypePrioriteDroit prioriteDroit;

  @XmlElement(name = "historique_periode_droits")
  @Valid
  protected List<TypeHistoriquePeriodeDroit> historiquePeriodeDroits;

  @Valid protected List<TypeConventionnement> conventionnements;
  @Valid protected List<TypePrestation> prestations;

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
   * Obtient la valeur de la propriété codeExterne.
   *
   * @return possible object is {@link String }
   */
  public String getCodeExterne() {
    return codeExterne;
  }

  /**
   * Définit la valeur de la propriété codeExterne.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeExterne(String value) {
    this.codeExterne = value;
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
   * Obtient la valeur de la propriété codeExterneProduit.
   *
   * @return possible object is {@link String }
   */
  public String getCodeExterneProduit() {
    return codeExterneProduit;
  }

  /**
   * Définit la valeur de la propriété codeExterneProduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeExterneProduit(String value) {
    this.codeExterneProduit = value;
  }

  /**
   * Obtient la valeur de la propriété libelleExterne.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleExterne() {
    return libelleExterne;
  }

  /**
   * Définit la valeur de la propriété libelleExterne.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleExterne(String value) {
    this.libelleExterne = value;
  }

  /**
   * Obtient la valeur de la propriété codeOptionMutualiste.
   *
   * @return possible object is {@link String }
   */
  public String getCodeOptionMutualiste() {
    return codeOptionMutualiste;
  }

  /**
   * Définit la valeur de la propriété codeOptionMutualiste.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeOptionMutualiste(String value) {
    this.codeOptionMutualiste = value;
  }

  /**
   * Obtient la valeur de la propriété libelleOptionMutualiste.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleOptionMutualiste() {
    return libelleOptionMutualiste;
  }

  /**
   * Définit la valeur de la propriété libelleOptionMutualiste.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleOptionMutualiste(String value) {
    this.libelleOptionMutualiste = value;
  }

  /**
   * Obtient la valeur de la propriété codeProduit.
   *
   * @return possible object is {@link String }
   */
  public String getCodeProduit() {
    return codeProduit;
  }

  /**
   * Définit la valeur de la propriété codeProduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeProduit(String value) {
    this.codeProduit = value;
  }

  /**
   * Obtient la valeur de la propriété libelleProduit.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleProduit() {
    return libelleProduit;
  }

  /**
   * Définit la valeur de la propriété libelleProduit.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleProduit(String value) {
    this.libelleProduit = value;
  }

  /**
   * Obtient la valeur de la propriété codeGarantie.
   *
   * @return possible object is {@link String }
   */
  public String getCodeGarantie() {
    return codeGarantie;
  }

  /**
   * Définit la valeur de la propriété codeGarantie.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeGarantie(String value) {
    this.codeGarantie = value;
  }

  /**
   * Obtient la valeur de la propriété libelleGarantie.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleGarantie() {
    return libelleGarantie;
  }

  /**
   * Définit la valeur de la propriété libelleGarantie.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleGarantie(String value) {
    this.libelleGarantie = value;
  }

  /**
   * Obtient la valeur de la propriété tauxRemboursement.
   *
   * @return possible object is {@link String }
   */
  public String getTauxRemboursement() {
    return tauxRemboursement;
  }

  /**
   * Définit la valeur de la propriété tauxRemboursement.
   *
   * @param value allowed object is {@link String }
   */
  public void setTauxRemboursement(String value) {
    this.tauxRemboursement = value;
  }

  /**
   * Obtient la valeur de la propriété libelleCodeRenvoi.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleCodeRenvoi() {
    return libelleCodeRenvoi;
  }

  /**
   * Définit la valeur de la propriété libelleCodeRenvoi.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleCodeRenvoi(String value) {
    this.libelleCodeRenvoi = value;
  }

  /**
   * Obtient la valeur de la propriété referenceCouverture.
   *
   * @return possible object is {@link String }
   */
  public String getReferenceCouverture() {
    return referenceCouverture;
  }

  /**
   * Définit la valeur de la propriété referenceCouverture.
   *
   * @param value allowed object is {@link String }
   */
  public void setReferenceCouverture(String value) {
    this.referenceCouverture = value;
  }

  /**
   * Obtient la valeur de la propriété prioriteDroit.
   *
   * @return possible object is {@link TypePrioriteDroit }
   */
  public TypePrioriteDroit getPrioriteDroit() {
    return prioriteDroit;
  }

  /**
   * Définit la valeur de la propriété prioriteDroit.
   *
   * @param value allowed object is {@link TypePrioriteDroit }
   */
  public void setPrioriteDroit(TypePrioriteDroit value) {
    this.prioriteDroit = value;
  }

  /**
   * Gets the value of the historiquePeriodeDroits property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the historiquePeriodeDroits property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getHistoriquePeriodeDroits().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeHistoriquePeriodeDroit }
   */
  public List<TypeHistoriquePeriodeDroit> getHistoriquePeriodeDroits() {
    if (historiquePeriodeDroits == null) {
      historiquePeriodeDroits = new ArrayList<TypeHistoriquePeriodeDroit>();
    }
    return this.historiquePeriodeDroits;
  }

  /**
   * Gets the value of the conventionnements property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the conventionnements property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getConventionnements().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeConventionnement }
   */
  public List<TypeConventionnement> getConventionnements() {
    if (conventionnements == null) {
      conventionnements = new ArrayList<TypeConventionnement>();
    }
    return this.conventionnements;
  }

  /**
   * Gets the value of the prestations property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the prestations property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getPrestations().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypePrestation }
   */
  public List<TypePrestation> getPrestations() {
    if (prestations == null) {
      prestations = new ArrayList<TypePrestation>();
    }
    return this.prestations;
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
    if (draftCopy instanceof TypeDomaineDroit) {
      final TypeDomaineDroit copy = ((TypeDomaineDroit) draftCopy);
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
      if (this.codeExterne != null) {
        String sourceCodeExterne;
        sourceCodeExterne = this.getCodeExterne();
        String copyCodeExterne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeExterne", sourceCodeExterne),
                    sourceCodeExterne));
        copy.setCodeExterne(copyCodeExterne);
      } else {
        copy.codeExterne = null;
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
      if (this.codeExterneProduit != null) {
        String sourceCodeExterneProduit;
        sourceCodeExterneProduit = this.getCodeExterneProduit();
        String copyCodeExterneProduit =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeExterneProduit", sourceCodeExterneProduit),
                    sourceCodeExterneProduit));
        copy.setCodeExterneProduit(copyCodeExterneProduit);
      } else {
        copy.codeExterneProduit = null;
      }
      if (this.libelleExterne != null) {
        String sourceLibelleExterne;
        sourceLibelleExterne = this.getLibelleExterne();
        String copyLibelleExterne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleExterne", sourceLibelleExterne),
                    sourceLibelleExterne));
        copy.setLibelleExterne(copyLibelleExterne);
      } else {
        copy.libelleExterne = null;
      }
      if (this.codeOptionMutualiste != null) {
        String sourceCodeOptionMutualiste;
        sourceCodeOptionMutualiste = this.getCodeOptionMutualiste();
        String copyCodeOptionMutualiste =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "codeOptionMutualiste", sourceCodeOptionMutualiste),
                    sourceCodeOptionMutualiste));
        copy.setCodeOptionMutualiste(copyCodeOptionMutualiste);
      } else {
        copy.codeOptionMutualiste = null;
      }
      if (this.libelleOptionMutualiste != null) {
        String sourceLibelleOptionMutualiste;
        sourceLibelleOptionMutualiste = this.getLibelleOptionMutualiste();
        String copyLibelleOptionMutualiste =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "libelleOptionMutualiste", sourceLibelleOptionMutualiste),
                    sourceLibelleOptionMutualiste));
        copy.setLibelleOptionMutualiste(copyLibelleOptionMutualiste);
      } else {
        copy.libelleOptionMutualiste = null;
      }
      if (this.codeProduit != null) {
        String sourceCodeProduit;
        sourceCodeProduit = this.getCodeProduit();
        String copyCodeProduit =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeProduit", sourceCodeProduit),
                    sourceCodeProduit));
        copy.setCodeProduit(copyCodeProduit);
      } else {
        copy.codeProduit = null;
      }
      if (this.libelleProduit != null) {
        String sourceLibelleProduit;
        sourceLibelleProduit = this.getLibelleProduit();
        String copyLibelleProduit =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleProduit", sourceLibelleProduit),
                    sourceLibelleProduit));
        copy.setLibelleProduit(copyLibelleProduit);
      } else {
        copy.libelleProduit = null;
      }
      if (this.codeGarantie != null) {
        String sourceCodeGarantie;
        sourceCodeGarantie = this.getCodeGarantie();
        String copyCodeGarantie =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeGarantie", sourceCodeGarantie),
                    sourceCodeGarantie));
        copy.setCodeGarantie(copyCodeGarantie);
      } else {
        copy.codeGarantie = null;
      }
      if (this.libelleGarantie != null) {
        String sourceLibelleGarantie;
        sourceLibelleGarantie = this.getLibelleGarantie();
        String copyLibelleGarantie =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleGarantie", sourceLibelleGarantie),
                    sourceLibelleGarantie));
        copy.setLibelleGarantie(copyLibelleGarantie);
      } else {
        copy.libelleGarantie = null;
      }
      if (this.tauxRemboursement != null) {
        String sourceTauxRemboursement;
        sourceTauxRemboursement = this.getTauxRemboursement();
        String copyTauxRemboursement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "tauxRemboursement", sourceTauxRemboursement),
                    sourceTauxRemboursement));
        copy.setTauxRemboursement(copyTauxRemboursement);
      } else {
        copy.tauxRemboursement = null;
      }
      if (this.libelleCodeRenvoi != null) {
        String sourceLibelleCodeRenvoi;
        sourceLibelleCodeRenvoi = this.getLibelleCodeRenvoi();
        String copyLibelleCodeRenvoi =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleCodeRenvoi", sourceLibelleCodeRenvoi),
                    sourceLibelleCodeRenvoi));
        copy.setLibelleCodeRenvoi(copyLibelleCodeRenvoi);
      } else {
        copy.libelleCodeRenvoi = null;
      }
      if (this.referenceCouverture != null) {
        String sourceReferenceCouverture;
        sourceReferenceCouverture = this.getReferenceCouverture();
        String copyReferenceCouverture =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "referenceCouverture", sourceReferenceCouverture),
                    sourceReferenceCouverture));
        copy.setReferenceCouverture(copyReferenceCouverture);
      } else {
        copy.referenceCouverture = null;
      }
      if (this.prioriteDroit != null) {
        TypePrioriteDroit sourcePrioriteDroit;
        sourcePrioriteDroit = this.getPrioriteDroit();
        TypePrioriteDroit copyPrioriteDroit =
            ((TypePrioriteDroit)
                strategy.copy(
                    LocatorUtils.property(locator, "prioriteDroit", sourcePrioriteDroit),
                    sourcePrioriteDroit));
        copy.setPrioriteDroit(copyPrioriteDroit);
      } else {
        copy.prioriteDroit = null;
      }
      if ((this.historiquePeriodeDroits != null) && (!this.historiquePeriodeDroits.isEmpty())) {
        List<TypeHistoriquePeriodeDroit> sourceHistoriquePeriodeDroits;
        sourceHistoriquePeriodeDroits =
            (((this.historiquePeriodeDroits != null) && (!this.historiquePeriodeDroits.isEmpty()))
                ? this.getHistoriquePeriodeDroits()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeHistoriquePeriodeDroit> copyHistoriquePeriodeDroits =
            ((List<TypeHistoriquePeriodeDroit>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "historiquePeriodeDroits", sourceHistoriquePeriodeDroits),
                    sourceHistoriquePeriodeDroits));
        copy.historiquePeriodeDroits = null;
        if (copyHistoriquePeriodeDroits != null) {
          List<TypeHistoriquePeriodeDroit> uniqueHistoriquePeriodeDroitsl =
              copy.getHistoriquePeriodeDroits();
          uniqueHistoriquePeriodeDroitsl.addAll(copyHistoriquePeriodeDroits);
        }
      } else {
        copy.historiquePeriodeDroits = null;
      }
      if ((this.conventionnements != null) && (!this.conventionnements.isEmpty())) {
        List<TypeConventionnement> sourceConventionnements;
        sourceConventionnements =
            (((this.conventionnements != null) && (!this.conventionnements.isEmpty()))
                ? this.getConventionnements()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeConventionnement> copyConventionnements =
            ((List<TypeConventionnement>)
                strategy.copy(
                    LocatorUtils.property(locator, "conventionnements", sourceConventionnements),
                    sourceConventionnements));
        copy.conventionnements = null;
        if (copyConventionnements != null) {
          List<TypeConventionnement> uniqueConventionnementsl = copy.getConventionnements();
          uniqueConventionnementsl.addAll(copyConventionnements);
        }
      } else {
        copy.conventionnements = null;
      }
      if ((this.prestations != null) && (!this.prestations.isEmpty())) {
        List<TypePrestation> sourcePrestations;
        sourcePrestations =
            (((this.prestations != null) && (!this.prestations.isEmpty()))
                ? this.getPrestations()
                : null);
        @SuppressWarnings("unchecked")
        List<TypePrestation> copyPrestations =
            ((List<TypePrestation>)
                strategy.copy(
                    LocatorUtils.property(locator, "prestations", sourcePrestations),
                    sourcePrestations));
        copy.prestations = null;
        if (copyPrestations != null) {
          List<TypePrestation> uniquePrestationsl = copy.getPrestations();
          uniquePrestationsl.addAll(copyPrestations);
        }
      } else {
        copy.prestations = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDomaineDroit();
  }
}
