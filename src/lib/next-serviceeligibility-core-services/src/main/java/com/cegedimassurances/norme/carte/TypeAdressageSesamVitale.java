package com.cegedimassurances.norme.carte;

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
 * Adressage sesam vitale : 1. Code STS sur 1 caractere alphanumerique 2. Indicateur de traitement
 * sur 2 caracteres numeriques 3. Code routage sur 2 caracteres alphanumeriques 4. Hote sur 3
 * caracteres alphanumeriques maximum 5. Nom de domaine sur 20 caracteres alphanumeriques maximum
 * Chaque donnee etant separee par le caractere / . Exemple : M/33/OC/1/seine.fr
 *
 * <p>Classe Java pour type_adressage_sesam_vitale complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_adressage_sesam_vitale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code_STS" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="indicateur_traitement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_routage" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_hote" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_domaine" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_adressage_sesam_vitale",
    propOrder = {"codeSTS", "indicateurTraitement", "codeRoutage", "codeHote", "nomDomaine"})
public class TypeAdressageSesamVitale implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "code_STS")
  @Size(min = 1, max = 1)
  protected String codeSTS;

  @XmlElement(name = "indicateur_traitement")
  @Size(min = 2, max = 2)
  protected String indicateurTraitement;

  @XmlElement(name = "code_routage")
  @Size(min = 2, max = 2)
  protected String codeRoutage;

  @XmlElement(name = "code_hote")
  @Size(min = 1, max = 3)
  protected String codeHote;

  @XmlElement(name = "nom_domaine")
  @Size(min = 1, max = 20)
  protected String nomDomaine;

  /**
   * Obtient la valeur de la propriété codeSTS.
   *
   * @return possible object is {@link String }
   */
  public String getCodeSTS() {
    return codeSTS;
  }

  /**
   * Définit la valeur de la propriété codeSTS.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeSTS(String value) {
    this.codeSTS = value;
  }

  /**
   * Obtient la valeur de la propriété indicateurTraitement.
   *
   * @return possible object is {@link String }
   */
  public String getIndicateurTraitement() {
    return indicateurTraitement;
  }

  /**
   * Définit la valeur de la propriété indicateurTraitement.
   *
   * @param value allowed object is {@link String }
   */
  public void setIndicateurTraitement(String value) {
    this.indicateurTraitement = value;
  }

  /**
   * Obtient la valeur de la propriété codeRoutage.
   *
   * @return possible object is {@link String }
   */
  public String getCodeRoutage() {
    return codeRoutage;
  }

  /**
   * Définit la valeur de la propriété codeRoutage.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeRoutage(String value) {
    this.codeRoutage = value;
  }

  /**
   * Obtient la valeur de la propriété codeHote.
   *
   * @return possible object is {@link String }
   */
  public String getCodeHote() {
    return codeHote;
  }

  /**
   * Définit la valeur de la propriété codeHote.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeHote(String value) {
    this.codeHote = value;
  }

  /**
   * Obtient la valeur de la propriété nomDomaine.
   *
   * @return possible object is {@link String }
   */
  public String getNomDomaine() {
    return nomDomaine;
  }

  /**
   * Définit la valeur de la propriété nomDomaine.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomDomaine(String value) {
    this.nomDomaine = value;
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
    if (draftCopy instanceof TypeAdressageSesamVitale) {
      final TypeAdressageSesamVitale copy = ((TypeAdressageSesamVitale) draftCopy);
      if (this.codeSTS != null) {
        String sourceCodeSTS;
        sourceCodeSTS = this.getCodeSTS();
        String copyCodeSTS =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeSTS", sourceCodeSTS), sourceCodeSTS));
        copy.setCodeSTS(copyCodeSTS);
      } else {
        copy.codeSTS = null;
      }
      if (this.indicateurTraitement != null) {
        String sourceIndicateurTraitement;
        sourceIndicateurTraitement = this.getIndicateurTraitement();
        String copyIndicateurTraitement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "indicateurTraitement", sourceIndicateurTraitement),
                    sourceIndicateurTraitement));
        copy.setIndicateurTraitement(copyIndicateurTraitement);
      } else {
        copy.indicateurTraitement = null;
      }
      if (this.codeRoutage != null) {
        String sourceCodeRoutage;
        sourceCodeRoutage = this.getCodeRoutage();
        String copyCodeRoutage =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeRoutage", sourceCodeRoutage),
                    sourceCodeRoutage));
        copy.setCodeRoutage(copyCodeRoutage);
      } else {
        copy.codeRoutage = null;
      }
      if (this.codeHote != null) {
        String sourceCodeHote;
        sourceCodeHote = this.getCodeHote();
        String copyCodeHote =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeHote", sourceCodeHote), sourceCodeHote));
        copy.setCodeHote(copyCodeHote);
      } else {
        copy.codeHote = null;
      }
      if (this.nomDomaine != null) {
        String sourceNomDomaine;
        sourceNomDomaine = this.getNomDomaine();
        String copyNomDomaine =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomDomaine", sourceNomDomaine),
                    sourceNomDomaine));
        copy.setNomDomaine(copyNomDomaine);
      } else {
        copy.nomDomaine = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeAdressageSesamVitale();
  }
}
