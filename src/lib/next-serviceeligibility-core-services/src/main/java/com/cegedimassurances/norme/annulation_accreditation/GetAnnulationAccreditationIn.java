package com.cegedimassurances.norme.annulation_accreditation;

import com.cegedimactiv.norme.fsiq.api.RequeteWsFsiq;
import com.cegedimassurances.norme.commun.TypeHeaderIn;
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
 * Classe Java pour getAnnulationAccreditation_in complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="getAnnulationAccreditation_in"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_in" type="{http://norme.cegedimassurances.com/commun}type_header_in"/&gt;
 *         &lt;element name="requeteAnnulation" type="{http://norme.cegedimassurances.com/annulation_accreditation}type_requete_annulation"/&gt;
 *         &lt;element name="numero_AMC_prefectoral"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *               &lt;minLength value="8"/&gt;
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
    name = "getAnnulationAccreditation_in",
    propOrder = {"headerIn", "requeteAnnulation", "numeroAMCPrefectoral"})
public class GetAnnulationAccreditationIn
    implements Serializable, Cloneable, RequeteWsFsiq, CopyTo {

  @XmlElement(name = "header_in", required = true)
  @NotNull
  @Valid
  protected TypeHeaderIn headerIn;

  @XmlElement(required = true)
  @NotNull
  @Valid
  protected TypeRequeteAnnulation requeteAnnulation;

  @XmlElement(name = "numero_AMC_prefectoral", required = true)
  @NotNull
  @Size(min = 8, max = 10)
  protected String numeroAMCPrefectoral;

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
   * Obtient la valeur de la propriété requeteAnnulation.
   *
   * @return possible object is {@link TypeRequeteAnnulation }
   */
  public TypeRequeteAnnulation getRequeteAnnulation() {
    return requeteAnnulation;
  }

  /**
   * Définit la valeur de la propriété requeteAnnulation.
   *
   * @param value allowed object is {@link TypeRequeteAnnulation }
   */
  public void setRequeteAnnulation(TypeRequeteAnnulation value) {
    this.requeteAnnulation = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAMCPrefectoral.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMCPrefectoral() {
    return numeroAMCPrefectoral;
  }

  /**
   * Définit la valeur de la propriété numeroAMCPrefectoral.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMCPrefectoral(String value) {
    this.numeroAMCPrefectoral = value;
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
    if (draftCopy instanceof GetAnnulationAccreditationIn) {
      final GetAnnulationAccreditationIn copy = ((GetAnnulationAccreditationIn) draftCopy);
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
      if (this.requeteAnnulation != null) {
        TypeRequeteAnnulation sourceRequeteAnnulation;
        sourceRequeteAnnulation = this.getRequeteAnnulation();
        TypeRequeteAnnulation copyRequeteAnnulation =
            ((TypeRequeteAnnulation)
                strategy.copy(
                    LocatorUtils.property(locator, "requeteAnnulation", sourceRequeteAnnulation),
                    sourceRequeteAnnulation));
        copy.setRequeteAnnulation(copyRequeteAnnulation);
      } else {
        copy.requeteAnnulation = null;
      }
      if (this.numeroAMCPrefectoral != null) {
        String sourceNumeroAMCPrefectoral;
        sourceNumeroAMCPrefectoral = this.getNumeroAMCPrefectoral();
        String copyNumeroAMCPrefectoral =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroAMCPrefectoral", sourceNumeroAMCPrefectoral),
                    sourceNumeroAMCPrefectoral));
        copy.setNumeroAMCPrefectoral(copyNumeroAMCPrefectoral);
      } else {
        copy.numeroAMCPrefectoral = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new GetAnnulationAccreditationIn();
  }
}
