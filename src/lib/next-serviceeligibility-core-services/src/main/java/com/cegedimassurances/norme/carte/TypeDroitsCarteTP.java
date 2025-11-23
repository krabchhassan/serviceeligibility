package com.cegedimassurances.norme.carte;

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
 * Classe Java pour type_droitsCarteTP complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_droitsCarteTP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rang_beneficiaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prenom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="date_naissance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nir"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cle_nir"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rang_gemellaire" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="code_qualite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="code_produit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="liste_garanties" type="{http://norme.cegedimassurances.com/carte}type_depense" maxOccurs="12" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_droitsCarteTP",
    propOrder = {
      "rangBeneficiaire",
      "prenom",
      "nom",
      "dateNaissance",
      "nir",
      "cleNir",
      "rangGemellaire",
      "codeQualite",
      "codeProduit",
      "listeGaranties"
    })
public class TypeDroitsCarteTP implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "rang_beneficiaire")
  protected String rangBeneficiaire;

  protected String prenom;
  protected String nom;

  @XmlElement(name = "date_naissance", required = true)
  @NotNull
  @Size(max = 8)
  protected String dateNaissance;

  @XmlElement(required = true)
  @NotNull
  @Size(max = 13)
  protected String nir;

  @XmlElement(name = "cle_nir", required = true)
  @NotNull
  @Size(max = 2)
  protected String cleNir;

  @XmlElement(name = "rang_gemellaire")
  @NotNull
  protected int rangGemellaire;

  @XmlElement(name = "code_qualite")
  protected String codeQualite;

  @XmlElement(name = "code_produit")
  protected String codeProduit;

  @XmlElement(name = "liste_garanties")
  @Size(min = 0, max = 12)
  @Valid
  protected List<TypeDepense> listeGaranties;

  /**
   * Obtient la valeur de la propriété rangBeneficiaire.
   *
   * @return possible object is {@link String }
   */
  public String getRangBeneficiaire() {
    return rangBeneficiaire;
  }

  /**
   * Définit la valeur de la propriété rangBeneficiaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setRangBeneficiaire(String value) {
    this.rangBeneficiaire = value;
  }

  /**
   * Obtient la valeur de la propriété prenom.
   *
   * @return possible object is {@link String }
   */
  public String getPrenom() {
    return prenom;
  }

  /**
   * Définit la valeur de la propriété prenom.
   *
   * @param value allowed object is {@link String }
   */
  public void setPrenom(String value) {
    this.prenom = value;
  }

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
   * Obtient la valeur de la propriété dateNaissance.
   *
   * @return possible object is {@link String }
   */
  public String getDateNaissance() {
    return dateNaissance;
  }

  /**
   * Définit la valeur de la propriété dateNaissance.
   *
   * @param value allowed object is {@link String }
   */
  public void setDateNaissance(String value) {
    this.dateNaissance = value;
  }

  /**
   * Obtient la valeur de la propriété nir.
   *
   * @return possible object is {@link String }
   */
  public String getNir() {
    return nir;
  }

  /**
   * Définit la valeur de la propriété nir.
   *
   * @param value allowed object is {@link String }
   */
  public void setNir(String value) {
    this.nir = value;
  }

  /**
   * Obtient la valeur de la propriété cleNir.
   *
   * @return possible object is {@link String }
   */
  public String getCleNir() {
    return cleNir;
  }

  /**
   * Définit la valeur de la propriété cleNir.
   *
   * @param value allowed object is {@link String }
   */
  public void setCleNir(String value) {
    this.cleNir = value;
  }

  /** Obtient la valeur de la propriété rangGemellaire. */
  public int getRangGemellaire() {
    return rangGemellaire;
  }

  /** Définit la valeur de la propriété rangGemellaire. */
  public void setRangGemellaire(int value) {
    this.rangGemellaire = value;
  }

  /**
   * Obtient la valeur de la propriété codeQualite.
   *
   * @return possible object is {@link String }
   */
  public String getCodeQualite() {
    return codeQualite;
  }

  /**
   * Définit la valeur de la propriété codeQualite.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeQualite(String value) {
    this.codeQualite = value;
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
   * Gets the value of the listeGaranties property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the listeGaranties property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getListeGaranties().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeDepense }
   */
  public List<TypeDepense> getListeGaranties() {
    if (listeGaranties == null) {
      listeGaranties = new ArrayList<TypeDepense>();
    }
    return this.listeGaranties;
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
    if (draftCopy instanceof TypeDroitsCarteTP) {
      final TypeDroitsCarteTP copy = ((TypeDroitsCarteTP) draftCopy);
      if (this.rangBeneficiaire != null) {
        String sourceRangBeneficiaire;
        sourceRangBeneficiaire = this.getRangBeneficiaire();
        String copyRangBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "rangBeneficiaire", sourceRangBeneficiaire),
                    sourceRangBeneficiaire));
        copy.setRangBeneficiaire(copyRangBeneficiaire);
      } else {
        copy.rangBeneficiaire = null;
      }
      if (this.prenom != null) {
        String sourcePrenom;
        sourcePrenom = this.getPrenom();
        String copyPrenom =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prenom", sourcePrenom), sourcePrenom));
        copy.setPrenom(copyPrenom);
      } else {
        copy.prenom = null;
      }
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
      }
      if (this.dateNaissance != null) {
        String sourceDateNaissance;
        sourceDateNaissance = this.getDateNaissance();
        String copyDateNaissance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "dateNaissance", sourceDateNaissance),
                    sourceDateNaissance));
        copy.setDateNaissance(copyDateNaissance);
      } else {
        copy.dateNaissance = null;
      }
      if (this.nir != null) {
        String sourceNir;
        sourceNir = this.getNir();
        String copyNir =
            ((String) strategy.copy(LocatorUtils.property(locator, "nir", sourceNir), sourceNir));
        copy.setNir(copyNir);
      } else {
        copy.nir = null;
      }
      if (this.cleNir != null) {
        String sourceCleNir;
        sourceCleNir = this.getCleNir();
        String copyCleNir =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNir", sourceCleNir), sourceCleNir));
        copy.setCleNir(copyCleNir);
      } else {
        copy.cleNir = null;
      }
      int sourceRangGemellaire;
      sourceRangGemellaire = (true ? this.getRangGemellaire() : 0);
      int copyRangGemellaire =
          strategy.copy(
              LocatorUtils.property(locator, "rangGemellaire", sourceRangGemellaire),
              sourceRangGemellaire);
      copy.setRangGemellaire(copyRangGemellaire);
      if (this.codeQualite != null) {
        String sourceCodeQualite;
        sourceCodeQualite = this.getCodeQualite();
        String copyCodeQualite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeQualite", sourceCodeQualite),
                    sourceCodeQualite));
        copy.setCodeQualite(copyCodeQualite);
      } else {
        copy.codeQualite = null;
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
      if ((this.listeGaranties != null) && (!this.listeGaranties.isEmpty())) {
        List<TypeDepense> sourceListeGaranties;
        sourceListeGaranties =
            (((this.listeGaranties != null) && (!this.listeGaranties.isEmpty()))
                ? this.getListeGaranties()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeDepense> copyListeGaranties =
            ((List<TypeDepense>)
                strategy.copy(
                    LocatorUtils.property(locator, "listeGaranties", sourceListeGaranties),
                    sourceListeGaranties));
        copy.listeGaranties = null;
        if (copyListeGaranties != null) {
          List<TypeDepense> uniqueListeGarantiesl = copy.getListeGaranties();
          uniqueListeGarantiesl.addAll(copyListeGaranties);
        }
      } else {
        copy.listeGaranties = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDroitsCarteTP();
  }
}
