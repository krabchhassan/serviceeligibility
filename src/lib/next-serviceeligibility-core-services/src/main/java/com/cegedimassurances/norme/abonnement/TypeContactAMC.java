package com.cegedimassurances.norme.abonnement;

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
 * Classe Java pour type_contact_AMC complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_contact_AMC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nom"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="64"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="email" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="256"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="telephone" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
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
    name = "type_contact_AMC",
    propOrder = {"nom", "email", "telephone"})
public class TypeContactAMC implements Serializable, Cloneable, CopyTo {

  @XmlElement(required = true)
  @NotNull
  @Size(max = 64)
  protected String nom;

  @Size(max = 256)
  protected String email;

  @Size(max = 30)
  protected String telephone;

  /**
   * Obtient la valeur de la propriété nom.
   *
   * @return possible object is {@link String }
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit la valeur de la propriété nom.
   *
   * @param value allowed object is {@link String }
   */
  public void setNom(String value) {
    this.nom = value;
  }

  /**
   * Obtient la valeur de la propriété email.
   *
   * @return possible object is {@link String }
   */
  public String getEmail() {
    return email;
  }

  /**
   * Définit la valeur de la propriété email.
   *
   * @param value allowed object is {@link String }
   */
  public void setEmail(String value) {
    this.email = value;
  }

  /**
   * Obtient la valeur de la propriété telephone.
   *
   * @return possible object is {@link String }
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * Définit la valeur de la propriété telephone.
   *
   * @param value allowed object is {@link String }
   */
  public void setTelephone(String value) {
    this.telephone = value;
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
    if (draftCopy instanceof TypeContactAMC) {
      final TypeContactAMC copy = ((TypeContactAMC) draftCopy);
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
      }
      if (this.email != null) {
        String sourceEmail;
        sourceEmail = this.getEmail();
        String copyEmail =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "email", sourceEmail), sourceEmail));
        copy.setEmail(copyEmail);
      } else {
        copy.email = null;
      }
      if (this.telephone != null) {
        String sourceTelephone;
        sourceTelephone = this.getTelephone();
        String copyTelephone =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "telephone", sourceTelephone), sourceTelephone));
        copy.setTelephone(copyTelephone);
      } else {
        copy.telephone = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeContactAMC();
  }
}
