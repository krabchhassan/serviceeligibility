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
 * entete technique de la requete
 *
 * <p>Classe Java pour type_header_tech_out complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_header_tech_out"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_serveur"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="14"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="id_session_serveur"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="id_session_client" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="version_serveur"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
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
    name = "type_header_tech_out",
    propOrder = {"idServeur", "date", "idSessionServeur", "idSessionClient", "versionServeur"})
public class TypeHeaderTechOut implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "id_serveur", required = true)
  @NotNull
  @Size(max = 14)
  protected String idServeur;

  @XmlElement(required = true)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  protected XMLGregorianCalendar date;

  @XmlElement(name = "id_session_serveur", required = true)
  @NotNull
  @Size(max = 36)
  protected String idSessionServeur;

  @XmlElement(name = "id_session_client")
  @Size(max = 36)
  protected String idSessionClient;

  @XmlElement(name = "version_serveur", required = true)
  @NotNull
  @Size(max = 15)
  protected String versionServeur;

  /**
   * Obtient la valeur de la propriété idServeur.
   *
   * @return possible object is {@link String }
   */
  public String getIdServeur() {
    return idServeur;
  }

  /**
   * Définit la valeur de la propriété idServeur.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdServeur(String value) {
    this.idServeur = value;
  }

  /**
   * Obtient la valeur de la propriété date.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDate() {
    return date;
  }

  /**
   * Définit la valeur de la propriété date.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDate(XMLGregorianCalendar value) {
    this.date = value;
  }

  /**
   * Obtient la valeur de la propriété idSessionServeur.
   *
   * @return possible object is {@link String }
   */
  public String getIdSessionServeur() {
    return idSessionServeur;
  }

  /**
   * Définit la valeur de la propriété idSessionServeur.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdSessionServeur(String value) {
    this.idSessionServeur = value;
  }

  /**
   * Obtient la valeur de la propriété idSessionClient.
   *
   * @return possible object is {@link String }
   */
  public String getIdSessionClient() {
    return idSessionClient;
  }

  /**
   * Définit la valeur de la propriété idSessionClient.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdSessionClient(String value) {
    this.idSessionClient = value;
  }

  /**
   * Obtient la valeur de la propriété versionServeur.
   *
   * @return possible object is {@link String }
   */
  public String getVersionServeur() {
    return versionServeur;
  }

  /**
   * Définit la valeur de la propriété versionServeur.
   *
   * @param value allowed object is {@link String }
   */
  public void setVersionServeur(String value) {
    this.versionServeur = value;
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
    if (draftCopy instanceof TypeHeaderTechOut) {
      final TypeHeaderTechOut copy = ((TypeHeaderTechOut) draftCopy);
      if (this.idServeur != null) {
        String sourceIdServeur;
        sourceIdServeur = this.getIdServeur();
        String copyIdServeur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idServeur", sourceIdServeur), sourceIdServeur));
        copy.setIdServeur(copyIdServeur);
      } else {
        copy.idServeur = null;
      }
      if (this.date != null) {
        XMLGregorianCalendar sourceDate;
        sourceDate = this.getDate();
        XMLGregorianCalendar copyDate =
            ((XMLGregorianCalendar)
                strategy.copy(LocatorUtils.property(locator, "date", sourceDate), sourceDate));
        copy.setDate(copyDate);
      } else {
        copy.date = null;
      }
      if (this.idSessionServeur != null) {
        String sourceIdSessionServeur;
        sourceIdSessionServeur = this.getIdSessionServeur();
        String copyIdSessionServeur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idSessionServeur", sourceIdSessionServeur),
                    sourceIdSessionServeur));
        copy.setIdSessionServeur(copyIdSessionServeur);
      } else {
        copy.idSessionServeur = null;
      }
      if (this.idSessionClient != null) {
        String sourceIdSessionClient;
        sourceIdSessionClient = this.getIdSessionClient();
        String copyIdSessionClient =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idSessionClient", sourceIdSessionClient),
                    sourceIdSessionClient));
        copy.setIdSessionClient(copyIdSessionClient);
      } else {
        copy.idSessionClient = null;
      }
      if (this.versionServeur != null) {
        String sourceVersionServeur;
        sourceVersionServeur = this.getVersionServeur();
        String copyVersionServeur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "versionServeur", sourceVersionServeur),
                    sourceVersionServeur));
        copy.setVersionServeur(copyVersionServeur);
      } else {
        copy.versionServeur = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderTechOut();
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
