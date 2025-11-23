package com.cegedimassurances.norme.commun;

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
 *         &lt;element name="id_logiciel_client"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="36"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="version_logiciel_client"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
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
 *         &lt;element name="mode_asynchrone" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="replyto_endPoint" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="65"/&gt;
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
      "idLogicielClient",
      "versionLogicielClient",
      "idSessionClient",
      "idSessionServeur",
      "modeAsynchrone",
      "replytoEndPoint"
    })
public class TypeHeaderTechIn implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "id_logiciel_client", required = true)
  @NotNull
  @Size(max = 36)
  protected String idLogicielClient;

  @XmlElement(name = "version_logiciel_client", required = true)
  @NotNull
  @Size(max = 15)
  protected String versionLogicielClient;

  @XmlElement(name = "id_session_client")
  @Size(max = 36)
  protected String idSessionClient;

  @XmlElement(name = "id_session_serveur")
  @Size(max = 36)
  protected String idSessionServeur;

  @XmlElement(name = "mode_asynchrone")
  protected Boolean modeAsynchrone;

  @XmlElement(name = "replyto_endPoint")
  @Size(max = 65)
  protected String replytoEndPoint;

  /**
   * Obtient la valeur de la propriété idLogicielClient.
   *
   * @return possible object is {@link String }
   */
  public String getIdLogicielClient() {
    return idLogicielClient;
  }

  /**
   * Définit la valeur de la propriété idLogicielClient.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdLogicielClient(String value) {
    this.idLogicielClient = value;
  }

  /**
   * Obtient la valeur de la propriété versionLogicielClient.
   *
   * @return possible object is {@link String }
   */
  public String getVersionLogicielClient() {
    return versionLogicielClient;
  }

  /**
   * Définit la valeur de la propriété versionLogicielClient.
   *
   * @param value allowed object is {@link String }
   */
  public void setVersionLogicielClient(String value) {
    this.versionLogicielClient = value;
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
   * Obtient la valeur de la propriété modeAsynchrone.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isModeAsynchrone() {
    return modeAsynchrone;
  }

  /**
   * Définit la valeur de la propriété modeAsynchrone.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setModeAsynchrone(Boolean value) {
    this.modeAsynchrone = value;
  }

  /**
   * Obtient la valeur de la propriété replytoEndPoint.
   *
   * @return possible object is {@link String }
   */
  public String getReplytoEndPoint() {
    return replytoEndPoint;
  }

  /**
   * Définit la valeur de la propriété replytoEndPoint.
   *
   * @param value allowed object is {@link String }
   */
  public void setReplytoEndPoint(String value) {
    this.replytoEndPoint = value;
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
    if (draftCopy instanceof TypeHeaderTechIn) {
      final TypeHeaderTechIn copy = ((TypeHeaderTechIn) draftCopy);
      if (this.idLogicielClient != null) {
        String sourceIdLogicielClient;
        sourceIdLogicielClient = this.getIdLogicielClient();
        String copyIdLogicielClient =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idLogicielClient", sourceIdLogicielClient),
                    sourceIdLogicielClient));
        copy.setIdLogicielClient(copyIdLogicielClient);
      } else {
        copy.idLogicielClient = null;
      }
      if (this.versionLogicielClient != null) {
        String sourceVersionLogicielClient;
        sourceVersionLogicielClient = this.getVersionLogicielClient();
        String copyVersionLogicielClient =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "versionLogicielClient", sourceVersionLogicielClient),
                    sourceVersionLogicielClient));
        copy.setVersionLogicielClient(copyVersionLogicielClient);
      } else {
        copy.versionLogicielClient = null;
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
      if (this.modeAsynchrone != null) {
        Boolean sourceModeAsynchrone;
        sourceModeAsynchrone = this.isModeAsynchrone();
        Boolean copyModeAsynchrone =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(locator, "modeAsynchrone", sourceModeAsynchrone),
                    sourceModeAsynchrone));
        copy.setModeAsynchrone(copyModeAsynchrone);
      } else {
        copy.modeAsynchrone = null;
      }
      if (this.replytoEndPoint != null) {
        String sourceReplytoEndPoint;
        sourceReplytoEndPoint = this.getReplytoEndPoint();
        String copyReplytoEndPoint =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "replytoEndPoint", sourceReplytoEndPoint),
                    sourceReplytoEndPoint));
        copy.setReplytoEndPoint(copyReplytoEndPoint);
      } else {
        copy.replytoEndPoint = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHeaderTechIn();
  }
}
