package com.cegedimassurances.norme.ps;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * Informations concernant le prescripteur
 *
 * <p>Classe Java pour type_medecin_traitant complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_medecin_traitant"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identification_facturation_PS" type="{http://norme.cegedimassurances.com/ps}type_ps_identifiant"/&gt;
 *         &lt;element name="raison_sociale" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="complement_raison_sociale" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
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
    name = "type_medecin_traitant",
    propOrder = {"identificationFacturationPS", "raisonSociale", "complementRaisonSociale"})
public class TypeMedecinTraitant implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "identification_facturation_PS", required = true)
  @NotNull
  @Valid
  protected TypePsIdentifiant identificationFacturationPS;

  @XmlElement(name = "raison_sociale")
  @Size(max = 45)
  protected String raisonSociale;

  @XmlElement(name = "complement_raison_sociale")
  @Size(max = 45)
  protected String complementRaisonSociale;

  /**
   * Obtient la valeur de la propriété identificationFacturationPS.
   *
   * @return possible object is {@link TypePsIdentifiant }
   */
  public TypePsIdentifiant getIdentificationFacturationPS() {
    return identificationFacturationPS;
  }

  /**
   * Définit la valeur de la propriété identificationFacturationPS.
   *
   * @param value allowed object is {@link TypePsIdentifiant }
   */
  public void setIdentificationFacturationPS(TypePsIdentifiant value) {
    this.identificationFacturationPS = value;
  }

  /**
   * Obtient la valeur de la propriété raisonSociale.
   *
   * @return possible object is {@link String }
   */
  public String getRaisonSociale() {
    return raisonSociale;
  }

  /**
   * Définit la valeur de la propriété raisonSociale.
   *
   * @param value allowed object is {@link String }
   */
  public void setRaisonSociale(String value) {
    this.raisonSociale = value;
  }

  /**
   * Obtient la valeur de la propriété complementRaisonSociale.
   *
   * @return possible object is {@link String }
   */
  public String getComplementRaisonSociale() {
    return complementRaisonSociale;
  }

  /**
   * Définit la valeur de la propriété complementRaisonSociale.
   *
   * @param value allowed object is {@link String }
   */
  public void setComplementRaisonSociale(String value) {
    this.complementRaisonSociale = value;
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
    if (draftCopy instanceof TypeMedecinTraitant) {
      final TypeMedecinTraitant copy = ((TypeMedecinTraitant) draftCopy);
      if (this.identificationFacturationPS != null) {
        TypePsIdentifiant sourceIdentificationFacturationPS;
        sourceIdentificationFacturationPS = this.getIdentificationFacturationPS();
        TypePsIdentifiant copyIdentificationFacturationPS =
            ((TypePsIdentifiant)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "identificationFacturationPS", sourceIdentificationFacturationPS),
                    sourceIdentificationFacturationPS));
        copy.setIdentificationFacturationPS(copyIdentificationFacturationPS);
      } else {
        copy.identificationFacturationPS = null;
      }
      if (this.raisonSociale != null) {
        String sourceRaisonSociale;
        sourceRaisonSociale = this.getRaisonSociale();
        String copyRaisonSociale =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "raisonSociale", sourceRaisonSociale),
                    sourceRaisonSociale));
        copy.setRaisonSociale(copyRaisonSociale);
      } else {
        copy.raisonSociale = null;
      }
      if (this.complementRaisonSociale != null) {
        String sourceComplementRaisonSociale;
        sourceComplementRaisonSociale = this.getComplementRaisonSociale();
        String copyComplementRaisonSociale =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "complementRaisonSociale", sourceComplementRaisonSociale),
                    sourceComplementRaisonSociale));
        copy.setComplementRaisonSociale(copyComplementRaisonSociale);
      } else {
        copy.complementRaisonSociale = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeMedecinTraitant();
  }
}
