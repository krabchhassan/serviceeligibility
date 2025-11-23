package com.cegedimassurances.norme.carte;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_carteTP complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_carteTP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date_edition" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="info_carte" type="{http://norme.cegedimassurances.com/carte}type_infoCarteTP"/&gt;
 *         &lt;element name="droits_carte" type="{http://norme.cegedimassurances.com/carte}type_droitsCarteTP" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_carteTP",
    propOrder = {"dateEdition", "infoCarte", "droitsCarte"})
public class TypeCarteTP implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "date_edition", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateEdition;

  @XmlElement(name = "info_carte", required = true)
  @NotNull
  @Valid
  protected TypeInfoCarteTP infoCarte;

  @XmlElement(name = "droits_carte", required = true)
  @NotNull
  @Size(min = 1)
  @Valid
  protected List<TypeDroitsCarteTP> droitsCarte;

  /**
   * Obtient la valeur de la propriété dateEdition.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateEdition() {
    return dateEdition;
  }

  /**
   * Définit la valeur de la propriété dateEdition.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateEdition(XMLGregorianCalendar value) {
    this.dateEdition = value;
  }

  /**
   * Obtient la valeur de la propriété infoCarte.
   *
   * @return possible object is {@link TypeInfoCarteTP }
   */
  public TypeInfoCarteTP getInfoCarte() {
    return infoCarte;
  }

  /**
   * Définit la valeur de la propriété infoCarte.
   *
   * @param value allowed object is {@link TypeInfoCarteTP }
   */
  public void setInfoCarte(TypeInfoCarteTP value) {
    this.infoCarte = value;
  }

  /**
   * Gets the value of the droitsCarte property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the droitsCarte property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getDroitsCarte().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeDroitsCarteTP }
   */
  public List<TypeDroitsCarteTP> getDroitsCarte() {
    if (droitsCarte == null) {
      droitsCarte = new ArrayList<TypeDroitsCarteTP>();
    }
    return this.droitsCarte;
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
    if (draftCopy instanceof TypeCarteTP) {
      final TypeCarteTP copy = ((TypeCarteTP) draftCopy);
      if (this.dateEdition != null) {
        XMLGregorianCalendar sourceDateEdition;
        sourceDateEdition = this.getDateEdition();
        XMLGregorianCalendar copyDateEdition =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateEdition", sourceDateEdition),
                    sourceDateEdition));
        copy.setDateEdition(copyDateEdition);
      } else {
        copy.dateEdition = null;
      }
      if (this.infoCarte != null) {
        TypeInfoCarteTP sourceInfoCarte;
        sourceInfoCarte = this.getInfoCarte();
        TypeInfoCarteTP copyInfoCarte =
            ((TypeInfoCarteTP)
                strategy.copy(
                    LocatorUtils.property(locator, "infoCarte", sourceInfoCarte), sourceInfoCarte));
        copy.setInfoCarte(copyInfoCarte);
      } else {
        copy.infoCarte = null;
      }
      if ((this.droitsCarte != null) && (!this.droitsCarte.isEmpty())) {
        List<TypeDroitsCarteTP> sourceDroitsCarte;
        sourceDroitsCarte =
            (((this.droitsCarte != null) && (!this.droitsCarte.isEmpty()))
                ? this.getDroitsCarte()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeDroitsCarteTP> copyDroitsCarte =
            ((List<TypeDroitsCarteTP>)
                strategy.copy(
                    LocatorUtils.property(locator, "droitsCarte", sourceDroitsCarte),
                    sourceDroitsCarte));
        copy.droitsCarte = null;
        if (copyDroitsCarte != null) {
          List<TypeDroitsCarteTP> uniqueDroitsCartel = copy.getDroitsCarte();
          uniqueDroitsCartel.addAll(copyDroitsCarte);
        }
      } else {
        copy.droitsCarte = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeCarteTP();
  }
}
