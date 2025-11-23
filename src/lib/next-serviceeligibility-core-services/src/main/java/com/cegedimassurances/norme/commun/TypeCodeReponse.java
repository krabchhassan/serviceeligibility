package com.cegedimassurances.norme.commun;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_codeReponse complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_codeReponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="150"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="detail_erreur" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="150"/&gt;
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
    name = "type_codeReponse",
    propOrder = {"code", "libelle", "detailErreur"})
public class TypeCodeReponse implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 10)
  protected String code;

  @Size(max = 150)
  protected String libelle;

  @XmlElement(name = "detail_erreur")
  protected List<String> detailErreur;

  /**
   * Obtient la valeur de la propriété code.
   *
   * @return possible object is {@link String }
   */
  public String getCode() {
    return code;
  }

  /**
   * Définit la valeur de la propriété code.
   *
   * @param value allowed object is {@link String }
   */
  public void setCode(String value) {
    this.code = value;
  }

  /**
   * Obtient la valeur de la propriété libelle.
   *
   * @return possible object is {@link String }
   */
  public String getLibelle() {
    return libelle;
  }

  /**
   * Définit la valeur de la propriété libelle.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelle(String value) {
    this.libelle = value;
  }

  /**
   * Gets the value of the detailErreur property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the detailErreur property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getDetailErreur().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getDetailErreur() {
    if (detailErreur == null) {
      detailErreur = new ArrayList<String>();
    }
    return this.detailErreur;
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
    if (draftCopy instanceof TypeCodeReponse) {
      final TypeCodeReponse copy = ((TypeCodeReponse) draftCopy);
      if (this.code != null) {
        String sourceCode;
        sourceCode = this.getCode();
        String copyCode =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "code", sourceCode), sourceCode));
        copy.setCode(copyCode);
      } else {
        copy.code = null;
      }
      if (this.libelle != null) {
        String sourceLibelle;
        sourceLibelle = this.getLibelle();
        String copyLibelle =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelle", sourceLibelle), sourceLibelle));
        copy.setLibelle(copyLibelle);
      } else {
        copy.libelle = null;
      }
      if ((this.detailErreur != null) && (!this.detailErreur.isEmpty())) {
        List<String> sourceDetailErreur;
        sourceDetailErreur =
            (((this.detailErreur != null) && (!this.detailErreur.isEmpty()))
                ? this.getDetailErreur()
                : null);
        @SuppressWarnings("unchecked")
        List<String> copyDetailErreur =
            ((List<String>)
                strategy.copy(
                    LocatorUtils.property(locator, "detailErreur", sourceDetailErreur),
                    sourceDetailErreur));
        copy.detailErreur = null;
        if (copyDetailErreur != null) {
          List<String> uniqueDetailErreurl = copy.getDetailErreur();
          uniqueDetailErreurl.addAll(copyDetailErreur);
        }
      } else {
        copy.detailErreur = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeCodeReponse();
  }
}
