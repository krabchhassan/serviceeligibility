package com.cegedimassurances.norme.cartedemat.beans;

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
 * entete technique de la requete
 *
 * <p>Classe Java pour type_header_tech_in complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_header_tech_in"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id_logiciel"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="version_logiciel"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="editeur_logiciel" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_logiciel" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
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
 *         &lt;element name="id_session_serveur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nom_norme"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="version_norme"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
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
    name = "type_header_tech_in",
    propOrder = {
      "idLogiciel",
      "versionLogiciel",
      "editeurLogiciel",
      "nomLogiciel",
      "idSessionClient",
      "idSessionServeur",
      "nomNorme",
      "versionNorme"
    })
public class TypeHeaderTechIn implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;

  @XmlElement(name = "id_logiciel", required = true)
  @NotNull
  @Size(max = 36)
  protected String idLogiciel;

  @XmlElement(name = "version_logiciel", required = true)
  @NotNull
  @Size(max = 15)
  protected String versionLogiciel;

  @XmlElement(name = "editeur_logiciel")
  @Size(max = 50)
  protected String editeurLogiciel;

  @XmlElement(name = "nom_logiciel")
  @Size(max = 50)
  protected String nomLogiciel;

  @XmlElement(name = "id_session_client")
  @Size(max = 36)
  protected String idSessionClient;

  @XmlElement(name = "id_session_serveur")
  @Size(max = 36)
  protected String idSessionServeur;

  @XmlElement(name = "nom_norme", required = true)
  @NotNull
  @Size(max = 10)
  protected String nomNorme;

  @XmlElement(name = "version_norme", required = true)
  @NotNull
  @Size(max = 10)
  protected String versionNorme;

  /**
   * Obtient la valeur de la propriété idLogiciel.
   *
   * @return possible object is {@link String }
   */
  public String getIdLogiciel() {
    return idLogiciel;
  }

  /**
   * Définit la valeur de la propriété idLogiciel.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdLogiciel(String value) {
    this.idLogiciel = value;
  }

  /**
   * Obtient la valeur de la propriété versionLogiciel.
   *
   * @return possible object is {@link String }
   */
  public String getVersionLogiciel() {
    return versionLogiciel;
  }

  /**
   * Définit la valeur de la propriété versionLogiciel.
   *
   * @param value allowed object is {@link String }
   */
  public void setVersionLogiciel(String value) {
    this.versionLogiciel = value;
  }

  /**
   * Obtient la valeur de la propriété editeurLogiciel.
   *
   * @return possible object is {@link String }
   */
  public String getEditeurLogiciel() {
    return editeurLogiciel;
  }

  /**
   * Définit la valeur de la propriété editeurLogiciel.
   *
   * @param value allowed object is {@link String }
   */
  public void setEditeurLogiciel(String value) {
    this.editeurLogiciel = value;
  }

  /**
   * Obtient la valeur de la propriété nomLogiciel.
   *
   * @return possible object is {@link String }
   */
  public String getNomLogiciel() {
    return nomLogiciel;
  }

  /**
   * Définit la valeur de la propriété nomLogiciel.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomLogiciel(String value) {
    this.nomLogiciel = value;
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
   * Obtient la valeur de la propriété nomNorme.
   *
   * @return possible object is {@link String }
   */
  public String getNomNorme() {
    return nomNorme;
  }

  /**
   * Définit la valeur de la propriété nomNorme.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomNorme(String value) {
    this.nomNorme = value;
  }

  /**
   * Obtient la valeur de la propriété versionNorme.
   *
   * @return possible object is {@link String }
   */
  public String getVersionNorme() {
    return versionNorme;
  }

  /**
   * Définit la valeur de la propriété versionNorme.
   *
   * @param value allowed object is {@link String }
   */
  public void setVersionNorme(String value) {
    this.versionNorme = value;
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
    if (draftCopy instanceof TypeHeaderTechIn) {
      final TypeHeaderTechIn copy = ((TypeHeaderTechIn) draftCopy);
      if (this.idLogiciel != null) {
        String sourceIdLogiciel;
        sourceIdLogiciel = this.getIdLogiciel();
        String copyIdLogiciel =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idLogiciel", sourceIdLogiciel),
                    sourceIdLogiciel));
        copy.setIdLogiciel(copyIdLogiciel);
      } else {
        copy.idLogiciel = null;
      }
      if (this.versionLogiciel != null) {
        String sourceVersionLogiciel;
        sourceVersionLogiciel = this.getVersionLogiciel();
        String copyVersionLogiciel =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "versionLogiciel", sourceVersionLogiciel),
                    sourceVersionLogiciel));
        copy.setVersionLogiciel(copyVersionLogiciel);
      } else {
        copy.versionLogiciel = null;
      }
      if (this.editeurLogiciel != null) {
        String sourceEditeurLogiciel;
        sourceEditeurLogiciel = this.getEditeurLogiciel();
        String copyEditeurLogiciel =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "editeurLogiciel", sourceEditeurLogiciel),
                    sourceEditeurLogiciel));
        copy.setEditeurLogiciel(copyEditeurLogiciel);
      } else {
        copy.editeurLogiciel = null;
      }
      if (this.nomLogiciel != null) {
        String sourceNomLogiciel;
        sourceNomLogiciel = this.getNomLogiciel();
        String copyNomLogiciel =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomLogiciel", sourceNomLogiciel),
                    sourceNomLogiciel));
        copy.setNomLogiciel(copyNomLogiciel);
      } else {
        copy.nomLogiciel = null;
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
      if (this.nomNorme != null) {
        String sourceNomNorme;
        sourceNomNorme = this.getNomNorme();
        String copyNomNorme =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomNorme", sourceNomNorme), sourceNomNorme));
        copy.setNomNorme(copyNomNorme);
      } else {
        copy.nomNorme = null;
      }
      if (this.versionNorme != null) {
        String sourceVersionNorme;
        sourceVersionNorme = this.getVersionNorme();
        String copyVersionNorme =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "versionNorme", sourceVersionNorme),
                    sourceVersionNorme));
        copy.setVersionNorme(copyVersionNorme);
      } else {
        copy.versionNorme = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderTechIn();
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
