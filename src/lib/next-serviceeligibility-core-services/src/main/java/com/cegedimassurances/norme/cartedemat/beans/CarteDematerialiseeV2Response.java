package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
 * Classe Java pour anonymous complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header_out" type="{http://norme.cegedimassurances.com/carteDemat/beans}type_header_out"/&gt;
 *         &lt;element name="code_response" type="{http://norme.cegedimassurances.com/carteDemat/beans}code_reponse" minOccurs="0"/&gt;
 *         &lt;element name="cartes" type="{http://norme.cegedimassurances.com/carteDemat/beans}cartesV2" minOccurs="0"/&gt;
 *         &lt;element name="commentaires" type="{http://norme.cegedimassurances.com/carteDemat/beans}commentaires" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"headerOut", "codeResponse", "cartes", "commentaires"})
@XmlRootElement(name = "carteDematerialiseeV2Response")
public class CarteDematerialiseeV2Response implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "header_out", required = true)
  @NotNull
  @Valid
  protected TypeHeaderOut headerOut;

  @XmlElement(name = "code_response")
  @Valid
  protected CodeReponse codeResponse;

  @Valid protected CartesV2 cartes;
  @Valid protected Commentaires commentaires;

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
   * Obtient la valeur de la propriété codeResponse.
   *
   * @return possible object is {@link CodeReponse }
   */
  public CodeReponse getCodeResponse() {
    return codeResponse;
  }

  /**
   * Définit la valeur de la propriété codeResponse.
   *
   * @param value allowed object is {@link CodeReponse }
   */
  public void setCodeResponse(CodeReponse value) {
    this.codeResponse = value;
  }

  /**
   * Obtient la valeur de la propriété cartes.
   *
   * @return possible object is {@link CartesV2 }
   */
  public CartesV2 getCartes() {
    return cartes;
  }

  /**
   * Définit la valeur de la propriété cartes.
   *
   * @param value allowed object is {@link CartesV2 }
   */
  public void setCartes(CartesV2 value) {
    this.cartes = value;
  }

  /**
   * Obtient la valeur de la propriété commentaires.
   *
   * @return possible object is {@link Commentaires }
   */
  public Commentaires getCommentaires() {
    return commentaires;
  }

  /**
   * Définit la valeur de la propriété commentaires.
   *
   * @param value allowed object is {@link Commentaires }
   */
  public void setCommentaires(Commentaires value) {
    this.commentaires = value;
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
    if (draftCopy instanceof CarteDematerialiseeV2Response) {
      final CarteDematerialiseeV2Response copy = ((CarteDematerialiseeV2Response) draftCopy);
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
      if (this.codeResponse != null) {
        CodeReponse sourceCodeResponse;
        sourceCodeResponse = this.getCodeResponse();
        CodeReponse copyCodeResponse =
            ((CodeReponse)
                strategy.copy(
                    LocatorUtils.property(locator, "codeResponse", sourceCodeResponse),
                    sourceCodeResponse));
        copy.setCodeResponse(copyCodeResponse);
      } else {
        copy.codeResponse = null;
      }
      if (this.cartes != null) {
        CartesV2 sourceCartes;
        sourceCartes = this.getCartes();
        CartesV2 copyCartes =
            ((CartesV2)
                strategy.copy(
                    LocatorUtils.property(locator, "cartes", sourceCartes), sourceCartes));
        copy.setCartes(copyCartes);
      } else {
        copy.cartes = null;
      }
      if (this.commentaires != null) {
        Commentaires sourceCommentaires;
        sourceCommentaires = this.getCommentaires();
        Commentaires copyCommentaires =
            ((Commentaires)
                strategy.copy(
                    LocatorUtils.property(locator, "commentaires", sourceCommentaires),
                    sourceCommentaires));
        copy.setCommentaires(copyCommentaires);
      } else {
        copy.commentaires = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new CarteDematerialiseeV2Response();
  }

  /**
   * Generates a String representation of the contents of this type. This is an extension method,
   * produced by the 'ts' xjc plugin
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
  }
}
