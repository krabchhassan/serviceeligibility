package com.cegedimassurances.norme.amo;

import com.cegedimassurances.norme.ps.TypeMedecinTraitant;
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
 * Classe Java pour type_amo complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_amo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="organisme" type="{http://norme.cegedimassurances.com/amo}type_organisme_amo"/&gt;
 *         &lt;element name="medecin_traitant" type="{http://norme.cegedimassurances.com/ps}type_medecin_traitant" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_amo",
    propOrder = {"organisme", "medecinTraitant"})
public class TypeAmo implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeOrganismeAmo organisme;

  @XmlElement(name = "medecin_traitant")
  @Valid
  protected TypeMedecinTraitant medecinTraitant;

  /**
   * Obtient la valeur de la propriété organisme.
   *
   * @return possible object is {@link TypeOrganismeAmo }
   */
  public TypeOrganismeAmo getOrganisme() {
    return organisme;
  }

  /**
   * Définit la valeur de la propriété organisme.
   *
   * @param value allowed object is {@link TypeOrganismeAmo }
   */
  public void setOrganisme(TypeOrganismeAmo value) {
    this.organisme = value;
  }

  /**
   * Obtient la valeur de la propriété medecinTraitant.
   *
   * @return possible object is {@link TypeMedecinTraitant }
   */
  public TypeMedecinTraitant getMedecinTraitant() {
    return medecinTraitant;
  }

  /**
   * Définit la valeur de la propriété medecinTraitant.
   *
   * @param value allowed object is {@link TypeMedecinTraitant }
   */
  public void setMedecinTraitant(TypeMedecinTraitant value) {
    this.medecinTraitant = value;
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
    if (draftCopy instanceof TypeAmo) {
      final TypeAmo copy = ((TypeAmo) draftCopy);
      if (this.organisme != null) {
        TypeOrganismeAmo sourceOrganisme;
        sourceOrganisme = this.getOrganisme();
        TypeOrganismeAmo copyOrganisme =
            ((TypeOrganismeAmo)
                strategy.copy(
                    LocatorUtils.property(locator, "organisme", sourceOrganisme), sourceOrganisme));
        copy.setOrganisme(copyOrganisme);
      } else {
        copy.organisme = null;
      }
      if (this.medecinTraitant != null) {
        TypeMedecinTraitant sourceMedecinTraitant;
        sourceMedecinTraitant = this.getMedecinTraitant();
        TypeMedecinTraitant copyMedecinTraitant =
            ((TypeMedecinTraitant)
                strategy.copy(
                    LocatorUtils.property(locator, "medecinTraitant", sourceMedecinTraitant),
                    sourceMedecinTraitant));
        copy.setMedecinTraitant(copyMedecinTraitant);
      } else {
        copy.medecinTraitant = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeAmo();
  }
}
