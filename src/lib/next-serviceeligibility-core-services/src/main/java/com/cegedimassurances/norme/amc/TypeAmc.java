package com.cegedimassurances.norme.amc;

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
 * Classe Java pour type_amc complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_amc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nom" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="45"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="operateur_tp" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="14"/&gt;
 *               &lt;minLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="adherent" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="id_patient_gs" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;choice&gt;
 *           &lt;element name="numero_AMC_RNM"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;length value="9"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="numero_AMC_SIRET"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;length value="14"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="numero_AMC_prefectoral"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;maxLength value="10"/&gt;
 *                 &lt;minLength value="8"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_amc",
    propOrder = {
      "nom",
      "operateurTp",
      "adherent",
      "idPatientGs",
      "numeroAMCRNM",
      "numeroAMCSIRET",
      "numeroAMCPrefectoral"
    })
public class TypeAmc implements Serializable, Cloneable, CopyTo {

  @Size(max = 45)
  protected String nom;

  @XmlElement(name = "operateur_tp")
  @Size(min = 10, max = 14)
  protected String operateurTp;

  @Size(max = 30)
  protected String adherent;

  @XmlElement(name = "id_patient_gs")
  @Size(max = 15)
  protected String idPatientGs;

  @XmlElement(name = "numero_AMC_RNM")
  @Size(min = 9, max = 9)
  protected String numeroAMCRNM;

  @XmlElement(name = "numero_AMC_SIRET")
  @Size(min = 14, max = 14)
  protected String numeroAMCSIRET;

  @XmlElement(name = "numero_AMC_prefectoral")
  @Size(min = 8, max = 10)
  protected String numeroAMCPrefectoral;

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
   * Obtient la valeur de la propriété operateurTp.
   *
   * @return possible object is {@link String }
   */
  public String getOperateurTp() {
    return operateurTp;
  }

  /**
   * Définit la valeur de la propriété operateurTp.
   *
   * @param value allowed object is {@link String }
   */
  public void setOperateurTp(String value) {
    this.operateurTp = value;
  }

  /**
   * Obtient la valeur de la propriété adherent.
   *
   * @return possible object is {@link String }
   */
  public String getAdherent() {
    return adherent;
  }

  /**
   * Définit la valeur de la propriété adherent.
   *
   * @param value allowed object is {@link String }
   */
  public void setAdherent(String value) {
    this.adherent = value;
  }

  /**
   * Obtient la valeur de la propriété idPatientGs.
   *
   * @return possible object is {@link String }
   */
  public String getIdPatientGs() {
    return idPatientGs;
  }

  /**
   * Définit la valeur de la propriété idPatientGs.
   *
   * @param value allowed object is {@link String }
   */
  public void setIdPatientGs(String value) {
    this.idPatientGs = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAMCRNM.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMCRNM() {
    return numeroAMCRNM;
  }

  /**
   * Définit la valeur de la propriété numeroAMCRNM.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMCRNM(String value) {
    this.numeroAMCRNM = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAMCSIRET.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMCSIRET() {
    return numeroAMCSIRET;
  }

  /**
   * Définit la valeur de la propriété numeroAMCSIRET.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMCSIRET(String value) {
    this.numeroAMCSIRET = value;
  }

  /**
   * Obtient la valeur de la propriété numeroAMCPrefectoral.
   *
   * @return possible object is {@link String }
   */
  public String getNumeroAMCPrefectoral() {
    return numeroAMCPrefectoral;
  }

  /**
   * Définit la valeur de la propriété numeroAMCPrefectoral.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumeroAMCPrefectoral(String value) {
    this.numeroAMCPrefectoral = value;
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
    if (draftCopy instanceof TypeAmc) {
      final TypeAmc copy = ((TypeAmc) draftCopy);
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
      }
      if (this.operateurTp != null) {
        String sourceOperateurTp;
        sourceOperateurTp = this.getOperateurTp();
        String copyOperateurTp =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "operateurTp", sourceOperateurTp),
                    sourceOperateurTp));
        copy.setOperateurTp(copyOperateurTp);
      } else {
        copy.operateurTp = null;
      }
      if (this.adherent != null) {
        String sourceAdherent;
        sourceAdherent = this.getAdherent();
        String copyAdherent =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "adherent", sourceAdherent), sourceAdherent));
        copy.setAdherent(copyAdherent);
      } else {
        copy.adherent = null;
      }
      if (this.idPatientGs != null) {
        String sourceIdPatientGs;
        sourceIdPatientGs = this.getIdPatientGs();
        String copyIdPatientGs =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "idPatientGs", sourceIdPatientGs),
                    sourceIdPatientGs));
        copy.setIdPatientGs(copyIdPatientGs);
      } else {
        copy.idPatientGs = null;
      }
      if (this.numeroAMCRNM != null) {
        String sourceNumeroAMCRNM;
        sourceNumeroAMCRNM = this.getNumeroAMCRNM();
        String copyNumeroAMCRNM =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAMCRNM", sourceNumeroAMCRNM),
                    sourceNumeroAMCRNM));
        copy.setNumeroAMCRNM(copyNumeroAMCRNM);
      } else {
        copy.numeroAMCRNM = null;
      }
      if (this.numeroAMCSIRET != null) {
        String sourceNumeroAMCSIRET;
        sourceNumeroAMCSIRET = this.getNumeroAMCSIRET();
        String copyNumeroAMCSIRET =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAMCSIRET", sourceNumeroAMCSIRET),
                    sourceNumeroAMCSIRET));
        copy.setNumeroAMCSIRET(copyNumeroAMCSIRET);
      } else {
        copy.numeroAMCSIRET = null;
      }
      if (this.numeroAMCPrefectoral != null) {
        String sourceNumeroAMCPrefectoral;
        sourceNumeroAMCPrefectoral = this.getNumeroAMCPrefectoral();
        String copyNumeroAMCPrefectoral =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroAMCPrefectoral", sourceNumeroAMCPrefectoral),
                    sourceNumeroAMCPrefectoral));
        copy.setNumeroAMCPrefectoral(copyNumeroAMCPrefectoral);
      } else {
        copy.numeroAMCPrefectoral = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeAmc();
  }
}
