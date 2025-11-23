package com.cegedimassurances.norme.annulation_accreditation;

import com.cegedimactiv.norme.fsiq.api.ReponseWsFsiq;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import com.cegedimassurances.norme.commun.TypeHeaderOut;
import jakarta.validation.Valid;
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
 * Classe Java pour getAnnulationAccreditation_out complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="getAnnulationAccreditation_out"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_out" type="{http://norme.cegedimassurances.com/commun}type_header_out" minOccurs="0"/&gt;
 *         &lt;element name="codeReponse" type="{http://norme.cegedimassurances.com/commun}type_codeReponse" minOccurs="0"/&gt;
 *         &lt;element name="reponseAnnulation" type="{http://norme.cegedimassurances.com/annulation_accreditation}type_reponse_annulation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "getAnnulationAccreditation_out",
    propOrder = {"headerOut", "codeReponse", "reponseAnnulation"})
public class GetAnnulationAccreditationOut
    implements Serializable, Cloneable, ReponseWsFsiq, CopyTo {

  @XmlElement(name = "header_out")
  @Valid
  protected TypeHeaderOut headerOut;

  @Valid protected TypeCodeReponse codeReponse;
  @Valid protected TypeReponseAnnulation reponseAnnulation;

  /**
   * Obtient la valeur de la propriété headerOut.
   *
   * @return possible object is {@link TypeHeaderOut }
   */
  public TypeHeaderOut getHeaderOut() {
    return headerOut;
  }

  /**
   * Définit la valeur de la propriété headerOut.
   *
   * @param value allowed object is {@link TypeHeaderOut }
   */
  public void setHeaderOut(TypeHeaderOut value) {
    this.headerOut = value;
  }

  /**
   * Obtient la valeur de la propriété codeReponse.
   *
   * @return possible object is {@link TypeCodeReponse }
   */
  public TypeCodeReponse getCodeReponse() {
    return codeReponse;
  }

  /**
   * Définit la valeur de la propriété codeReponse.
   *
   * @param value allowed object is {@link TypeCodeReponse }
   */
  public void setCodeReponse(TypeCodeReponse value) {
    this.codeReponse = value;
  }

  /**
   * Obtient la valeur de la propriété reponseAnnulation.
   *
   * @return possible object is {@link TypeReponseAnnulation }
   */
  public TypeReponseAnnulation getReponseAnnulation() {
    return reponseAnnulation;
  }

  /**
   * Définit la valeur de la propriété reponseAnnulation.
   *
   * @param value allowed object is {@link TypeReponseAnnulation }
   */
  public void setReponseAnnulation(TypeReponseAnnulation value) {
    this.reponseAnnulation = value;
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
    if (draftCopy instanceof GetAnnulationAccreditationOut) {
      final GetAnnulationAccreditationOut copy = ((GetAnnulationAccreditationOut) draftCopy);
      if (this.headerOut != null) {
        TypeHeaderOut sourceHeaderOut;
        sourceHeaderOut = this.getHeaderOut();
        TypeHeaderOut copyHeaderOut =
            ((TypeHeaderOut)
                strategy.copy(
                    LocatorUtils.property(locator, "headerOut", sourceHeaderOut), sourceHeaderOut));
        copy.setHeaderOut(copyHeaderOut);
      } else {
        copy.headerOut = null;
      }
      if (this.codeReponse != null) {
        TypeCodeReponse sourceCodeReponse;
        sourceCodeReponse = this.getCodeReponse();
        TypeCodeReponse copyCodeReponse =
            ((TypeCodeReponse)
                strategy.copy(
                    LocatorUtils.property(locator, "codeReponse", sourceCodeReponse),
                    sourceCodeReponse));
        copy.setCodeReponse(copyCodeReponse);
      } else {
        copy.codeReponse = null;
      }
      if (this.reponseAnnulation != null) {
        TypeReponseAnnulation sourceReponseAnnulation;
        sourceReponseAnnulation = this.getReponseAnnulation();
        TypeReponseAnnulation copyReponseAnnulation =
            ((TypeReponseAnnulation)
                strategy.copy(
                    LocatorUtils.property(locator, "reponseAnnulation", sourceReponseAnnulation),
                    sourceReponseAnnulation));
        copy.setReponseAnnulation(copyReponseAnnulation);
      } else {
        copy.reponseAnnulation = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new GetAnnulationAccreditationOut();
  }
}
