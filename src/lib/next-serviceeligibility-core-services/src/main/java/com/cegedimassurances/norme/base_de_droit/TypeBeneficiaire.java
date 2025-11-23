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
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_beneficiaire complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_beneficiaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date_naissance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rang_naissance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nir_beneficiaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cle_nir_beneficiaire" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nir_od_1" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cle_nir_od_1" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nir_od_2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cle_nir_od2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="insc" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="25"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="numero_personne" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="historique_affiliations" type="{http://norme.cegedimassurances.com/base_de_droit}type_historique_affiliation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="adresses" type="{http://norme.cegedimassurances.com/base_de_droit}type_adresse" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_beneficiaire",
    propOrder = {
      "dateNaissance",
      "rangNaissance",
      "nirBeneficiaire",
      "cleNirBeneficiaire",
      "nirOd1",
      "cleNirOd1",
      "nirOd2",
      "cleNirOd2",
      "insc",
      "numeroPersonne",
      "historiqueAffiliations",
      "adresses"
    })
public class TypeBeneficiaire implements Serializable, Cloneable, CopyTo {

  /**
   * -- GETTER -- Obtient la valeur de la propriété dateNaissance.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété dateNaissance.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "date_naissance", required = true)
  @NotNull
  @Size(max = 8)
  protected String dateNaissance;

  /**
   * -- GETTER -- Obtient la valeur de la propriété rangNaissance.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété rangNaissance.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "rang_naissance", required = true)
  @NotNull
  @Size(max = 1)
  protected String rangNaissance;

  /**
   * -- GETTER -- Obtient la valeur de la propriété nirBeneficiaire.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété nirBeneficiaire.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "nir_beneficiaire")
  @Size(max = 13)
  protected String nirBeneficiaire;

  /**
   * -- GETTER -- Obtient la valeur de la propriété cleNirBeneficiaire.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété cleNirBeneficiaire.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "cle_nir_beneficiaire")
  @Size(max = 2)
  protected String cleNirBeneficiaire;

  /**
   * -- GETTER -- Obtient la valeur de la propriété nirOd1.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété nirOd1.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "nir_od_1")
  @Size(max = 13)
  protected String nirOd1;

  /**
   * -- GETTER -- Obtient la valeur de la propriété cleNirOd1.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété cleNirOd1.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "cle_nir_od_1")
  @Size(max = 2)
  protected String cleNirOd1;

  /**
   * -- GETTER -- Obtient la valeur de la propriété nirOd2.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété nirOd2.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "nir_od_2")
  @Size(max = 13)
  protected String nirOd2;

  /**
   * -- GETTER -- Obtient la valeur de la propriété cleNirOd2.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété cleNirOd2.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "cle_nir_od2")
  @Size(max = 2)
  protected String cleNirOd2;

  /**
   * -- SETTER -- Définit la valeur de la propriété insc.
   *
   * <p>-- GETTER -- Obtient la valeur de la propriété insc.
   *
   * @param value allowed object is {@link String }
   * @return possible object is {@link String }
   */
  @Getter
  @Setter
  @Size(max = 25)
  protected String insc;

  /**
   * -- GETTER -- Obtient la valeur de la propriété numeroPersonne.
   *
   * <p>-- SETTER -- Définit la valeur de la propriété numeroPersonne.
   *
   * @return possible object is {@link String }
   * @param value allowed object is {@link String }
   */
  @Setter
  @Getter
  @XmlElement(name = "numero_personne")
  @Size(max = 15)
  protected String numeroPersonne;

  @XmlElement(name = "historique_affiliations")
  @Valid
  protected List<TypeHistoriqueAffiliation> historiqueAffiliations;

  @Valid protected List<TypeAdresse> adresses;

  /**
   * Gets the value of the historiqueAffiliations property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the historiqueAffiliations property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getHistoriqueAffiliations().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeHistoriqueAffiliation }
   */
  public List<TypeHistoriqueAffiliation> getHistoriqueAffiliations() {
    if (historiqueAffiliations == null) {
      historiqueAffiliations = new ArrayList<TypeHistoriqueAffiliation>();
    }
    return this.historiqueAffiliations;
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
    final Object draftCopy = ((target == null) ? createNewInstance() : target);
    if (draftCopy instanceof TypeBeneficiaire copy) {
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
      if (this.rangNaissance != null) {
        String sourceRangNaissance;
        sourceRangNaissance = this.getRangNaissance();
        String copyRangNaissance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "rangNaissance", sourceRangNaissance),
                    sourceRangNaissance));
        copy.setRangNaissance(copyRangNaissance);
      } else {
        copy.rangNaissance = null;
      }
      if (this.nirBeneficiaire != null) {
        String sourceNirBeneficiaire;
        sourceNirBeneficiaire = this.getNirBeneficiaire();
        String copyNirBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirBeneficiaire", sourceNirBeneficiaire),
                    sourceNirBeneficiaire));
        copy.setNirBeneficiaire(copyNirBeneficiaire);
      } else {
        copy.nirBeneficiaire = null;
      }
      if (this.cleNirBeneficiaire != null) {
        String sourceCleNirBeneficiaire;
        sourceCleNirBeneficiaire = this.getCleNirBeneficiaire();
        String copyCleNirBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirBeneficiaire", sourceCleNirBeneficiaire),
                    sourceCleNirBeneficiaire));
        copy.setCleNirBeneficiaire(copyCleNirBeneficiaire);
      } else {
        copy.cleNirBeneficiaire = null;
      }
      if (this.nirOd1 != null) {
        String sourceNirOd1;
        sourceNirOd1 = this.getNirOd1();
        String copyNirOd1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirOd1", sourceNirOd1), sourceNirOd1));
        copy.setNirOd1(copyNirOd1);
      } else {
        copy.nirOd1 = null;
      }
      if (this.cleNirOd1 != null) {
        String sourceCleNirOd1;
        sourceCleNirOd1 = this.getCleNirOd1();
        String copyCleNirOd1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirOd1", sourceCleNirOd1), sourceCleNirOd1));
        copy.setCleNirOd1(copyCleNirOd1);
      } else {
        copy.cleNirOd1 = null;
      }
      if (this.nirOd2 != null) {
        String sourceNirOd2;
        sourceNirOd2 = this.getNirOd2();
        String copyNirOd2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirOd2", sourceNirOd2), sourceNirOd2));
        copy.setNirOd2(copyNirOd2);
      } else {
        copy.nirOd2 = null;
      }
      if (this.cleNirOd2 != null) {
        String sourceCleNirOd2;
        sourceCleNirOd2 = this.getCleNirOd2();
        String copyCleNirOd2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirOd2", sourceCleNirOd2), sourceCleNirOd2));
        copy.setCleNirOd2(copyCleNirOd2);
      } else {
        copy.cleNirOd2 = null;
      }
      if (this.insc != null) {
        String sourceInsc;
        sourceInsc = this.getInsc();
        String copyInsc =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "insc", sourceInsc), sourceInsc));
        copy.setInsc(copyInsc);
      } else {
        copy.insc = null;
      }
      if (this.numeroPersonne != null) {
        String sourceNumeroPersonne;
        sourceNumeroPersonne = this.getNumeroPersonne();
        String copyNumeroPersonne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroPersonne", sourceNumeroPersonne),
                    sourceNumeroPersonne));
        copy.setNumeroPersonne(copyNumeroPersonne);
      } else {
        copy.numeroPersonne = null;
      }
      if ((this.historiqueAffiliations != null) && (!this.historiqueAffiliations.isEmpty())) {
        List<TypeHistoriqueAffiliation> sourceHistoriqueAffiliations;
        sourceHistoriqueAffiliations = this.getHistoriqueAffiliations();
        @SuppressWarnings("unchecked")
        List<TypeHistoriqueAffiliation> copyHistoriqueAffiliations =
            ((List<TypeHistoriqueAffiliation>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "historiqueAffiliations", sourceHistoriqueAffiliations),
                    sourceHistoriqueAffiliations));
        copy.historiqueAffiliations = null;
        if (copyHistoriqueAffiliations != null) {
          List<TypeHistoriqueAffiliation> uniqueHistoriqueAffiliationsl =
              copy.getHistoriqueAffiliations();
          uniqueHistoriqueAffiliationsl.addAll(copyHistoriqueAffiliations);
        }
      } else {
        copy.historiqueAffiliations = null;
      }
      if ((this.adresses != null) && (!this.adresses.isEmpty())) {
        List<TypeAdresse> sourceAdresses = this.getAdresses();
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
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeBeneficiaire();
  }
}
