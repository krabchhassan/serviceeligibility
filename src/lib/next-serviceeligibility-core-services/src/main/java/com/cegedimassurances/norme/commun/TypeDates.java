package com.cegedimassurances.norme.commun;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
 * Classe Java pour type_dates complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_dates"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date_reference" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="type_date_reference"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;totalDigits value="1"/&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="2"/&gt;
 *               &lt;enumeration value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_debut" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="date_fin" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_dates",
    propOrder = {"dateReference", "typeDateReference", "dateDebut", "dateFin"})
public class TypeDates implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "date_reference", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateReference;

  @XmlElement(name = "type_date_reference", defaultValue = "0")
  @NotNull
  @Digits(integer = 1, fraction = 0)
  protected int typeDateReference;

  @XmlElement(name = "date_debut")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateDebut;

  @XmlElement(name = "date_fin")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateFin;

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

  /** Obtient la valeur de la propriété typeDateReference. */
  public int getTypeDateReference() {
    return typeDateReference;
  }

  /** Définit la valeur de la propriété typeDateReference. */
  public void setTypeDateReference(int value) {
    this.typeDateReference = value;
  }

  /**
   * Obtient la valeur de la propriété dateDebut.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateDebut() {
    return dateDebut;
  }

  /**
   * Définit la valeur de la propriété dateDebut.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateDebut(XMLGregorianCalendar value) {
    this.dateDebut = value;
  }

  /**
   * Obtient la valeur de la propriété dateFin.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateFin() {
    return dateFin;
  }

  /**
   * Définit la valeur de la propriété dateFin.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateFin(XMLGregorianCalendar value) {
    this.dateFin = value;
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
    if (draftCopy instanceof TypeDates) {
      final TypeDates copy = ((TypeDates) draftCopy);
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
      int sourceTypeDateReference;
      sourceTypeDateReference = (true ? this.getTypeDateReference() : 0);
      int copyTypeDateReference =
          strategy.copy(
              LocatorUtils.property(locator, "typeDateReference", sourceTypeDateReference),
              sourceTypeDateReference);
      copy.setTypeDateReference(copyTypeDateReference);
      if (this.dateDebut != null) {
        XMLGregorianCalendar sourceDateDebut;
        sourceDateDebut = this.getDateDebut();
        XMLGregorianCalendar copyDateDebut =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateDebut", sourceDateDebut), sourceDateDebut));
        copy.setDateDebut(copyDateDebut);
      } else {
        copy.dateDebut = null;
      }
      if (this.dateFin != null) {
        XMLGregorianCalendar sourceDateFin;
        sourceDateFin = this.getDateFin();
        XMLGregorianCalendar copyDateFin =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateFin", sourceDateFin), sourceDateFin));
        copy.setDateFin(copyDateFin);
      } else {
        copy.dateFin = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeDates();
  }
}
