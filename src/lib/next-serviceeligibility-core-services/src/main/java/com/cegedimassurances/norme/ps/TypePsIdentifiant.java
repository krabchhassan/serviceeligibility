package com.cegedimassurances.norme.ps;

import jakarta.validation.constraints.Digits;
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
 * Classe Java pour type_ps_identifiant complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_ps_identifiant"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="num_FINESS_geo"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *                 &lt;totalDigits value="9"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="num_FINESS_juridique"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *                 &lt;totalDigits value="9"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="numero_PS_emetteur"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;length value="9"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="identifiant_RPPS" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="11"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="conventionne" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="indicateur_cas" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
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
    name = "type_ps_identifiant",
    propOrder = {
      "numFINESSGeo",
      "numFINESSJuridique",
      "numeroPSEmetteur",
      "identifiantRPPS",
      "conventionne",
      "indicateurCas"
    })
public class TypePsIdentifiant implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "num_FINESS_geo")
  @Digits(integer = 9, fraction = 0)
  protected Integer numFINESSGeo;

  @XmlElement(name = "num_FINESS_juridique")
  @Digits(integer = 9, fraction = 0)
  protected Integer numFINESSJuridique;

  @XmlElement(name = "numero_PS_emetteur")
  @Size(min = 9, max = 9)
  protected String numeroPSEmetteur;

  @XmlElement(name = "identifiant_RPPS")
  @Size(min = 11, max = 11)
  protected String identifiantRPPS;

  @Size(min = 1, max = 1)
  protected String conventionne;

  @XmlElement(name = "indicateur_cas")
  @Size(min = 1, max = 1)
  protected String indicateurCas;

  /**
   * Obtient la valeur de la propriété numFINESSGeo.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getNumFINESSGeo() {
    return numFINESSGeo;
  }

  /**
   * Définit la valeur de la propriété numFINESSGeo.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setNumFINESSGeo(Integer value) {
    this.numFINESSGeo = value;
  }

  /**
   * Obtient la valeur de la propriété numFINESSJuridique.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getNumFINESSJuridique() {
    return numFINESSJuridique;
  }

  /**
   * Définit la valeur de la propriété numFINESSJuridique.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setNumFINESSJuridique(Integer value) {
    this.numFINESSJuridique = value;
  }

  /**
   * Obtient la valeur de la propriété numeroPSEmetteur.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroPSEmetteur() {
    return numeroPSEmetteur;
  }

  /**
   * Définit la valeur de la propriété numeroPSEmetteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroPSEmetteur(String value) {
    this.numeroPSEmetteur = value;
  }

  /**
   * Obtient la valeur de la propriété identifiantRPPS.
   *
   * @return possible object is {@link String }
   */
  public String getIdentifiantRPPS() {
    return identifiantRPPS;
  }

  /**
   * Définit la valeur de la propriété identifiantRPPS.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdentifiantRPPS(String value) {
    this.identifiantRPPS = value;
  }

  /**
   * Obtient la valeur de la propriété conventionne.
   *
   * @return possible object is {@link String }
   */
  public String getConventionne() {
    return conventionne;
  }

  /**
   * Définit la valeur de la propriété conventionne.
   *
   * @param value allowed object is {@link String }
   */
  public void setConventionne(String value) {
    this.conventionne = value;
  }

  /**
   * Obtient la valeur de la propriété indicateurCas.
   *
   * @return possible object is {@link String }
   */
  public String getIndicateurCas() {
    return indicateurCas;
  }

  /**
   * Définit la valeur de la propriété indicateurCas.
   *
   * @param value allowed object is {@link String }
   */
  public void setIndicateurCas(String value) {
    this.indicateurCas = value;
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
    if (draftCopy instanceof TypePsIdentifiant) {
      final TypePsIdentifiant copy = ((TypePsIdentifiant) draftCopy);
      if (this.numFINESSGeo != null) {
        Integer sourceNumFINESSGeo;
        sourceNumFINESSGeo = this.getNumFINESSGeo();
        Integer copyNumFINESSGeo =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(locator, "numFINESSGeo", sourceNumFINESSGeo),
                    sourceNumFINESSGeo));
        copy.setNumFINESSGeo(copyNumFINESSGeo);
      } else {
        copy.numFINESSGeo = null;
      }
      if (this.numFINESSJuridique != null) {
        Integer sourceNumFINESSJuridique;
        sourceNumFINESSJuridique = this.getNumFINESSJuridique();
        Integer copyNumFINESSJuridique =
            ((Integer)
                strategy.copy(
                    LocatorUtils.property(locator, "numFINESSJuridique", sourceNumFINESSJuridique),
                    sourceNumFINESSJuridique));
        copy.setNumFINESSJuridique(copyNumFINESSJuridique);
      } else {
        copy.numFINESSJuridique = null;
      }
      if (this.numeroPSEmetteur != null) {
        String sourceNumeroPSEmetteur;
        sourceNumeroPSEmetteur = this.getNumeroPSEmetteur();
        String copyNumeroPSEmetteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroPSEmetteur", sourceNumeroPSEmetteur),
                    sourceNumeroPSEmetteur));
        copy.setNumeroPSEmetteur(copyNumeroPSEmetteur);
      } else {
        copy.numeroPSEmetteur = null;
      }
      if (this.identifiantRPPS != null) {
        String sourceIdentifiantRPPS;
        sourceIdentifiantRPPS = this.getIdentifiantRPPS();
        String copyIdentifiantRPPS =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "identifiantRPPS", sourceIdentifiantRPPS),
                    sourceIdentifiantRPPS));
        copy.setIdentifiantRPPS(copyIdentifiantRPPS);
      } else {
        copy.identifiantRPPS = null;
      }
      if (this.conventionne != null) {
        String sourceConventionne;
        sourceConventionne = this.getConventionne();
        String copyConventionne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "conventionne", sourceConventionne),
                    sourceConventionne));
        copy.setConventionne(copyConventionne);
      } else {
        copy.conventionne = null;
      }
      if (this.indicateurCas != null) {
        String sourceIndicateurCas;
        sourceIndicateurCas = this.getIndicateurCas();
        String copyIndicateurCas =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "indicateurCas", sourceIndicateurCas),
                    sourceIndicateurCas));
        copy.setIndicateurCas(copyIndicateurCas);
      } else {
        copy.indicateurCas = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypePsIdentifiant();
  }
}
