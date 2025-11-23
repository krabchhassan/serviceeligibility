package com.cegedimassurances.norme.tarification_prescription;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_in_prestation_acte complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_in_prestation_acte"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code_lieu"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="mode_traitement" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="discipline_prestation" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="indicateur_parcours_soins" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="tarif_responsabilite"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;totalDigits value="7"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_modificateur" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
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
    name = "type_in_prestation_acte",
    propOrder = {
      "codeLieu",
      "modeTraitement",
      "disciplinePrestation",
      "indicateurParcoursSoins",
      "tarifResponsabilite",
      "codeModificateur"
    })
public class TypeInPrestationActe implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "code_lieu")
  @NotNull
  protected int codeLieu;

  @XmlElement(name = "mode_traitement")
  @Size(min = 2, max = 2)
  protected String modeTraitement;

  @XmlElement(name = "discipline_prestation")
  @Size(min = 3, max = 3)
  protected String disciplinePrestation;

  @XmlElement(name = "indicateur_parcours_soins")
  @Size(min = 1, max = 1)
  protected String indicateurParcoursSoins;

  @XmlElement(name = "tarif_responsabilite", required = true)
  @NotNull
  @Digits(integer = 7, fraction = 2)
  protected BigDecimal tarifResponsabilite;

  @XmlElement(name = "code_modificateur")
  @Size(min = 1, max = 1)
  protected String codeModificateur;

  /** Obtient la valeur de la propriété codeLieu. */
  public int getCodeLieu() {
    return codeLieu;
  }

  /** Définit la valeur de la propriété codeLieu. */
  public void setCodeLieu(int value) {
    this.codeLieu = value;
  }

  /**
   * Obtient la valeur de la propriété modeTraitement.
   *
   * @return possible object is {@link String }
   */
  public String getModeTraitement() {
    return modeTraitement;
  }

  /**
   * Définit la valeur de la propriété modeTraitement.
   *
   * @param value allowed object is {@link String }
   */
  public void setModeTraitement(String value) {
    this.modeTraitement = value;
  }

  /**
   * Obtient la valeur de la propriété disciplinePrestation.
   *
   * @return possible object is {@link String }
   */
  public String getDisciplinePrestation() {
    return disciplinePrestation;
  }

  /**
   * Définit la valeur de la propriété disciplinePrestation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDisciplinePrestation(String value) {
    this.disciplinePrestation = value;
  }

  /**
   * Obtient la valeur de la propriété indicateurParcoursSoins.
   *
   * @return possible object is {@link String }
   */
  public String getIndicateurParcoursSoins() {
    return indicateurParcoursSoins;
  }

  /**
   * Définit la valeur de la propriété indicateurParcoursSoins.
   *
   * @param value allowed object is {@link String }
   */
  public void setIndicateurParcoursSoins(String value) {
    this.indicateurParcoursSoins = value;
  }

  /**
   * Obtient la valeur de la propriété tarifResponsabilite.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getTarifResponsabilite() {
    return tarifResponsabilite;
  }

  /**
   * Définit la valeur de la propriété tarifResponsabilite.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setTarifResponsabilite(BigDecimal value) {
    this.tarifResponsabilite = value;
  }

  /**
   * Obtient la valeur de la propriété codeModificateur.
   *
   * @return possible object is {@link String }
   */
  public String getCodeModificateur() {
    return codeModificateur;
  }

  /**
   * Définit la valeur de la propriété codeModificateur.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeModificateur(String value) {
    this.codeModificateur = value;
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
    if (draftCopy instanceof TypeInPrestationActe) {
      final TypeInPrestationActe copy = ((TypeInPrestationActe) draftCopy);
      int sourceCodeLieu;
      sourceCodeLieu = (true ? this.getCodeLieu() : 0);
      int copyCodeLieu =
          strategy.copy(LocatorUtils.property(locator, "codeLieu", sourceCodeLieu), sourceCodeLieu);
      copy.setCodeLieu(copyCodeLieu);
      if (this.modeTraitement != null) {
        String sourceModeTraitement;
        sourceModeTraitement = this.getModeTraitement();
        String copyModeTraitement =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "modeTraitement", sourceModeTraitement),
                    sourceModeTraitement));
        copy.setModeTraitement(copyModeTraitement);
      } else {
        copy.modeTraitement = null;
      }
      if (this.disciplinePrestation != null) {
        String sourceDisciplinePrestation;
        sourceDisciplinePrestation = this.getDisciplinePrestation();
        String copyDisciplinePrestation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "disciplinePrestation", sourceDisciplinePrestation),
                    sourceDisciplinePrestation));
        copy.setDisciplinePrestation(copyDisciplinePrestation);
      } else {
        copy.disciplinePrestation = null;
      }
      if (this.indicateurParcoursSoins != null) {
        String sourceIndicateurParcoursSoins;
        sourceIndicateurParcoursSoins = this.getIndicateurParcoursSoins();
        String copyIndicateurParcoursSoins =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "indicateurParcoursSoins", sourceIndicateurParcoursSoins),
                    sourceIndicateurParcoursSoins));
        copy.setIndicateurParcoursSoins(copyIndicateurParcoursSoins);
      } else {
        copy.indicateurParcoursSoins = null;
      }
      if (this.tarifResponsabilite != null) {
        BigDecimal sourceTarifResponsabilite;
        sourceTarifResponsabilite = this.getTarifResponsabilite();
        BigDecimal copyTarifResponsabilite =
            ((BigDecimal)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "tarifResponsabilite", sourceTarifResponsabilite),
                    sourceTarifResponsabilite));
        copy.setTarifResponsabilite(copyTarifResponsabilite);
      } else {
        copy.tarifResponsabilite = null;
      }
      if (this.codeModificateur != null) {
        String sourceCodeModificateur;
        sourceCodeModificateur = this.getCodeModificateur();
        String copyCodeModificateur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeModificateur", sourceCodeModificateur),
                    sourceCodeModificateur));
        copy.setCodeModificateur(copyCodeModificateur);
      } else {
        copy.codeModificateur = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInPrestationActe();
  }
}
