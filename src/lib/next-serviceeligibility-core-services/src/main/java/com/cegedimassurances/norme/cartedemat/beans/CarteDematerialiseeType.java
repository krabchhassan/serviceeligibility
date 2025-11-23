package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Contenu de la requete Carte Dematerialisee
 *
 * <p>Classe Java pour carteDematerialiseeType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="carteDematerialiseeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroAMC"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nomAMC" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dateReference" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="numeroContrat"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="20"/&gt;
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
    name = "carteDematerialiseeType",
    propOrder = {"numeroAMC", "nomAMC", "dateReference", "numeroContrat"})
public class CarteDematerialiseeType implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 10)
  protected String numeroAMC;

  @Size(min = 1, max = 45)
  protected String nomAMC;

  @XmlElement(required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateReference;

  @XmlElement(required = true)
  @NotNull
  @Size(min = 1, max = 20)
  protected String numeroContrat;

  /**
   * Obtient la valeur de la propriété numeroAMC.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMC() {
    return numeroAMC;
  }

  /**
   * Définit la valeur de la propriété numeroAMC.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMC(String value) {
    this.numeroAMC = value;
  }

  /**
   * Obtient la valeur de la propriété nomAMC.
   *
   * @return possible object is {@link String }
   */
  public String getNomAMC() {
    return nomAMC;
  }

  /**
   * Définit la valeur de la propriété nomAMC.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAMC(String value) {
    this.nomAMC = value;
  }

  /**
   * Obtient la valeur de la propriété dateReference.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateReference() {
    return dateReference;
  }

  /**
   * Définit la valeur de la propriété dateReference.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateReference(XMLGregorianCalendar value) {
    this.dateReference = value;
  }

  /**
   * Obtient la valeur de la propriété numeroContrat.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroContrat() {
    return numeroContrat;
  }

  /**
   * Définit la valeur de la propriété numeroContrat.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroContrat(String value) {
    this.numeroContrat = value;
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
    if (draftCopy instanceof CarteDematerialiseeType) {
      final CarteDematerialiseeType copy = ((CarteDematerialiseeType) draftCopy);
      if (this.numeroAMC != null) {
        String sourceNumeroAMC;
        sourceNumeroAMC = this.getNumeroAMC();
        String copyNumeroAMC =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAMC", sourceNumeroAMC), sourceNumeroAMC));
        copy.setNumeroAMC(copyNumeroAMC);
      } else {
        copy.numeroAMC = null;
      }
      if (this.nomAMC != null) {
        String sourceNomAMC;
        sourceNomAMC = this.getNomAMC();
        String copyNomAMC =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomAMC", sourceNomAMC), sourceNomAMC));
        copy.setNomAMC(copyNomAMC);
      } else {
        copy.nomAMC = null;
      }
      if (this.dateReference != null) {
        XMLGregorianCalendar sourceDateReference;
        sourceDateReference = this.getDateReference();
        XMLGregorianCalendar copyDateReference =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateReference", sourceDateReference),
                    sourceDateReference));
        copy.setDateReference(copyDateReference);
      } else {
        copy.dateReference = null;
      }
      if (this.numeroContrat != null) {
        String sourceNumeroContrat;
        sourceNumeroContrat = this.getNumeroContrat();
        String copyNumeroContrat =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroContrat", sourceNumeroContrat),
                    sourceNumeroContrat));
        copy.setNumeroContrat(copyNumeroContrat);
      } else {
        copy.numeroContrat = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new CarteDematerialiseeType();
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
