package com.cegedimassurances.norme.base_de_droit;

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
 * Classe Java pour type_historique_periode_droit complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_historique_periode_droit"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="periode_debut" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="periode_fin" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="motif_evenement"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="libelle_evenement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date_evenemnt" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="mode_obtention" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
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
    name = "type_historique_periode_droit",
    propOrder = {
      "periodeDebut",
      "periodeFin",
      "motifEvenement",
      "libelleEvenement",
      "dateEvenemnt",
      "modeObtention"
    })
public class TypeHistoriquePeriodeDroit implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "periode_debut", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar periodeDebut;

  @XmlElement(name = "periode_fin")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar periodeFin;

  @XmlElement(name = "motif_evenement", required = true)
  @NotNull
  @Size(max = 2)
  protected String motifEvenement;

  @XmlElement(name = "libelle_evenement")
  @Size(max = 45)
  protected String libelleEvenement;

  @XmlElement(name = "date_evenemnt")
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar dateEvenemnt;

  @XmlElement(name = "mode_obtention")
  @Size(max = 2)
  protected String modeObtention;

  /**
   * Obtient la valeur de la propriété periodeDebut.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getPeriodeDebut() {
    return periodeDebut;
  }

  /**
   * Définit la valeur de la propriété periodeDebut.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setPeriodeDebut(XMLGregorianCalendar value) {
    this.periodeDebut = value;
  }

  /**
   * Obtient la valeur de la propriété periodeFin.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getPeriodeFin() {
    return periodeFin;
  }

  /**
   * Définit la valeur de la propriété periodeFin.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setPeriodeFin(XMLGregorianCalendar value) {
    this.periodeFin = value;
  }

  /**
   * Obtient la valeur de la propriété motifEvenement.
   *
   * @return possible object is {@link String }
   */
  public String getMotifEvenement() {
    return motifEvenement;
  }

  /**
   * Définit la valeur de la propriété motifEvenement.
   *
   * @param value allowed object is {@link String }
   */
  public void setMotifEvenement(String value) {
    this.motifEvenement = value;
  }

  /**
   * Obtient la valeur de la propriété libelleEvenement.
   *
   * @return possible object is {@link String }
   */
  public String getLibelleEvenement() {
    return libelleEvenement;
  }

  /**
   * Définit la valeur de la propriété libelleEvenement.
   *
   * @param value allowed object is {@link String }
   */
  public void setLibelleEvenement(String value) {
    this.libelleEvenement = value;
  }

  /**
   * Obtient la valeur de la propriété dateEvenemnt.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateEvenemnt() {
    return dateEvenemnt;
  }

  /**
   * Définit la valeur de la propriété dateEvenemnt.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateEvenemnt(XMLGregorianCalendar value) {
    this.dateEvenemnt = value;
  }

  /**
   * Obtient la valeur de la propriété modeObtention.
   *
   * @return possible object is {@link String }
   */
  public String getModeObtention() {
    return modeObtention;
  }

  /**
   * Définit la valeur de la propriété modeObtention.
   *
   * @param value allowed object is {@link String }
   */
  public void setModeObtention(String value) {
    this.modeObtention = value;
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
    if (draftCopy instanceof TypeHistoriquePeriodeDroit) {
      final TypeHistoriquePeriodeDroit copy = ((TypeHistoriquePeriodeDroit) draftCopy);
      if (this.periodeDebut != null) {
        XMLGregorianCalendar sourcePeriodeDebut;
        sourcePeriodeDebut = this.getPeriodeDebut();
        XMLGregorianCalendar copyPeriodeDebut =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "periodeDebut", sourcePeriodeDebut),
                    sourcePeriodeDebut));
        copy.setPeriodeDebut(copyPeriodeDebut);
      } else {
        copy.periodeDebut = null;
      }
      if (this.periodeFin != null) {
        XMLGregorianCalendar sourcePeriodeFin;
        sourcePeriodeFin = this.getPeriodeFin();
        XMLGregorianCalendar copyPeriodeFin =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "periodeFin", sourcePeriodeFin),
                    sourcePeriodeFin));
        copy.setPeriodeFin(copyPeriodeFin);
      } else {
        copy.periodeFin = null;
      }
      if (this.motifEvenement != null) {
        String sourceMotifEvenement;
        sourceMotifEvenement = this.getMotifEvenement();
        String copyMotifEvenement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "motifEvenement", sourceMotifEvenement),
                    sourceMotifEvenement));
        copy.setMotifEvenement(copyMotifEvenement);
      } else {
        copy.motifEvenement = null;
      }
      if (this.libelleEvenement != null) {
        String sourceLibelleEvenement;
        sourceLibelleEvenement = this.getLibelleEvenement();
        String copyLibelleEvenement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "libelleEvenement", sourceLibelleEvenement),
                    sourceLibelleEvenement));
        copy.setLibelleEvenement(copyLibelleEvenement);
      } else {
        copy.libelleEvenement = null;
      }
      if (this.dateEvenemnt != null) {
        XMLGregorianCalendar sourceDateEvenemnt;
        sourceDateEvenemnt = this.getDateEvenemnt();
        XMLGregorianCalendar copyDateEvenemnt =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateEvenemnt", sourceDateEvenemnt),
                    sourceDateEvenemnt));
        copy.setDateEvenemnt(copyDateEvenemnt);
      } else {
        copy.dateEvenemnt = null;
      }
      if (this.modeObtention != null) {
        String sourceModeObtention;
        sourceModeObtention = this.getModeObtention();
        String copyModeObtention =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "modeObtention", sourceModeObtention),
                    sourceModeObtention));
        copy.setModeObtention(copyModeObtention);
      } else {
        copy.modeObtention = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHistoriquePeriodeDroit();
  }
}
