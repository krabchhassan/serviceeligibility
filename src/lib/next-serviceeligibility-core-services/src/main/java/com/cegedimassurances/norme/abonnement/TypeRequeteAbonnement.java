package com.cegedimassurances.norme.abonnement;

import com.cegedimassurances.norme.patient.TypeInfoPatient;
import com.cegedimassurances.norme.ps.TypePsDemandeur;
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
 * Classe Java pour type_requete_abonnement complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_requete_abonnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idPS" type="{http://norme.cegedimassurances.com/ps}type_ps_demandeur"/&gt;
 *         &lt;element name="infoPatient" type="{http://norme.cegedimassurances.com/patient}type_info_patient"/&gt;
 *         &lt;element name="infoAbonnement" type="{http://norme.cegedimassurances.com/abonnement}type_info_abonnement"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_requete_abonnement",
    propOrder = {"idPS", "infoPatient", "infoAbonnement"})
public class TypeRequeteAbonnement implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypePsDemandeur idPS;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeInfoPatient infoPatient;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeInfoAbonnement infoAbonnement;

  /**
   * Obtient la valeur de la propriété idPS.
   *
   * @return possible object is {@link TypePsDemandeur }
   */
  public TypePsDemandeur getIdPS() {
    return idPS;
  }

  /**
   * Définit la valeur de la propriété idPS.
   *
   * @param value allowed object is {@link TypePsDemandeur }
   */
  public void setIdPS(TypePsDemandeur value) {
    this.idPS = value;
  }

  /**
   * Obtient la valeur de la propriété infoPatient.
   *
   * @return possible object is {@link TypeInfoPatient }
   */
  public TypeInfoPatient getInfoPatient() {
    return infoPatient;
  }

  /**
   * Définit la valeur de la propriété infoPatient.
   *
   * @param value allowed object is {@link TypeInfoPatient }
   */
  public void setInfoPatient(TypeInfoPatient value) {
    this.infoPatient = value;
  }

  /**
   * Obtient la valeur de la propriété infoAbonnement.
   *
   * @return possible object is {@link TypeInfoAbonnement }
   */
  public TypeInfoAbonnement getInfoAbonnement() {
    return infoAbonnement;
  }

  /**
   * Définit la valeur de la propriété infoAbonnement.
   *
   * @param value allowed object is {@link TypeInfoAbonnement }
   */
  public void setInfoAbonnement(TypeInfoAbonnement value) {
    this.infoAbonnement = value;
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
    if (draftCopy instanceof TypeRequeteAbonnement) {
      final TypeRequeteAbonnement copy = ((TypeRequeteAbonnement) draftCopy);
      if (this.idPS != null) {
        TypePsDemandeur sourceIdPS;
        sourceIdPS = this.getIdPS();
        TypePsDemandeur copyIdPS =
            ((TypePsDemandeur)
                strategy.copy(LocatorUtils.property(locator, "idPS", sourceIdPS), sourceIdPS));
        copy.setIdPS(copyIdPS);
      } else {
        copy.idPS = null;
      }
      if (this.infoPatient != null) {
        TypeInfoPatient sourceInfoPatient;
        sourceInfoPatient = this.getInfoPatient();
        TypeInfoPatient copyInfoPatient =
            ((TypeInfoPatient)
                strategy.copy(
                    LocatorUtils.property(locator, "infoPatient", sourceInfoPatient),
                    sourceInfoPatient));
        copy.setInfoPatient(copyInfoPatient);
      } else {
        copy.infoPatient = null;
      }
      if (this.infoAbonnement != null) {
        TypeInfoAbonnement sourceInfoAbonnement;
        sourceInfoAbonnement = this.getInfoAbonnement();
        TypeInfoAbonnement copyInfoAbonnement =
            ((TypeInfoAbonnement)
                strategy.copy(
                    LocatorUtils.property(locator, "infoAbonnement", sourceInfoAbonnement),
                    sourceInfoAbonnement));
        copy.setInfoAbonnement(copyInfoAbonnement);
      } else {
        copy.infoAbonnement = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeRequeteAbonnement();
  }
}
