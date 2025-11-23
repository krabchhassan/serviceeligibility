package com.cegedimassurances.norme.abonnement;

import com.cegedimactiv.norme.fsiq.api.RequeteWsFsiq;
import com.cegedimassurances.norme.commun.TypeHeaderIn;
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
 * Classe Java pour getAbonnement_in complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="getAbonnement_in"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_in" type="{http://norme.cegedimassurances.com/commun}type_header_in"/&gt;
 *         &lt;element name="requete_abonnement" type="{http://norme.cegedimassurances.com/abonnement}type_requete_abonnement"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "getAbonnement_in",
    propOrder = {"headerIn", "requeteAbonnement"})
public class GetAbonnementIn implements Serializable, Cloneable, RequeteWsFsiq, CopyTo {

  @XmlElement(name = "header_in", required = true)
  @NotNull
  @Valid
  protected TypeHeaderIn headerIn;

  @XmlElement(name = "requete_abonnement", required = true)
  @NotNull
  @Valid
  protected TypeRequeteAbonnement requeteAbonnement;

  /**
   * Obtient la valeur de la propriété headerIn.
   *
   * @return possible object is {@link TypeHeaderIn }
   */
  public TypeHeaderIn getHeaderIn() {
    return headerIn;
  }

  /**
   * Définit la valeur de la propriété headerIn.
   *
   * @param value allowed object is {@link TypeHeaderIn }
   */
  public void setHeaderIn(TypeHeaderIn value) {
    this.headerIn = value;
  }

  /**
   * Obtient la valeur de la propriété requeteAbonnement.
   *
   * @return possible object is {@link TypeRequeteAbonnement }
   */
  public TypeRequeteAbonnement getRequeteAbonnement() {
    return requeteAbonnement;
  }

  /**
   * Définit la valeur de la propriété requeteAbonnement.
   *
   * @param value allowed object is {@link TypeRequeteAbonnement }
   */
  public void setRequeteAbonnement(TypeRequeteAbonnement value) {
    this.requeteAbonnement = value;
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
    if (draftCopy instanceof GetAbonnementIn) {
      final GetAbonnementIn copy = ((GetAbonnementIn) draftCopy);
      if (this.headerIn != null) {
        TypeHeaderIn sourceHeaderIn;
        sourceHeaderIn = this.getHeaderIn();
        TypeHeaderIn copyHeaderIn =
            ((TypeHeaderIn)
                strategy.copy(
                    LocatorUtils.property(locator, "headerIn", sourceHeaderIn), sourceHeaderIn));
        copy.setHeaderIn(copyHeaderIn);
      } else {
        copy.headerIn = null;
      }
      if (this.requeteAbonnement != null) {
        TypeRequeteAbonnement sourceRequeteAbonnement;
        sourceRequeteAbonnement = this.getRequeteAbonnement();
        TypeRequeteAbonnement copyRequeteAbonnement =
            ((TypeRequeteAbonnement)
                strategy.copy(
                    LocatorUtils.property(locator, "requeteAbonnement", sourceRequeteAbonnement),
                    sourceRequeteAbonnement));
        copy.setRequeteAbonnement(copyRequeteAbonnement);
      } else {
        copy.requeteAbonnement = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new GetAbonnementIn();
  }
}
