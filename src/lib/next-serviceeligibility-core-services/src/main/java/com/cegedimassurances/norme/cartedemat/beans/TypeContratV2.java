package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
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
 * Proprietes du contrat V2
 *
 * <p>Classe Java pour type_contratV2 complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_contratV2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://norme.cegedimassurances.com/carteDemat/beans}type_contrat"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annexe1Carte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="annexe2Carte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fondCarte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_contratV2",
    propOrder = {"annexe1Carte", "annexe2Carte", "fondCarte"})
public class TypeContratV2 extends TypeContrat implements Serializable, Cloneable, CopyTo {

  private static final long serialVersionUID = -11L;
  protected String annexe1Carte;
  protected String annexe2Carte;
  protected String fondCarte;

  /**
   * Obtient la valeur de la propriété annexe1Carte.
   *
   * @return possible object is {@link String }
   */
  public String getAnnexe1Carte() {
    return annexe1Carte;
  }

  /**
   * Définit la valeur de la propriété annexe1Carte.
   *
   * @param value allowed object is {@link String }
   */
  public void setAnnexe1Carte(String value) {
    this.annexe1Carte = value;
  }

  /**
   * Obtient la valeur de la propriété annexe2Carte.
   *
   * @return possible object is {@link String }
   */
  public String getAnnexe2Carte() {
    return annexe2Carte;
  }

  /**
   * Définit la valeur de la propriété annexe2Carte.
   *
   * @param value allowed object is {@link String }
   */
  public void setAnnexe2Carte(String value) {
    this.annexe2Carte = value;
  }

  /**
   * Obtient la valeur de la propriété fondCarte.
   *
   * @return possible object is {@link String }
   */
  public String getFondCarte() {
    return fondCarte;
  }

  /**
   * Définit la valeur de la propriété fondCarte.
   *
   * @param value allowed object is {@link String }
   */
  public void setFondCarte(String value) {
    this.fondCarte = value;
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
    super.copyTo(locator, draftCopy, strategy);
    if (draftCopy instanceof TypeContratV2) {
      final TypeContratV2 copy = ((TypeContratV2) draftCopy);
      if (this.annexe1Carte != null) {
        String sourceAnnexe1Carte;
        sourceAnnexe1Carte = this.getAnnexe1Carte();
        String copyAnnexe1Carte =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "annexe1Carte", sourceAnnexe1Carte),
                    sourceAnnexe1Carte));
        copy.setAnnexe1Carte(copyAnnexe1Carte);
      } else {
        copy.annexe1Carte = null;
      }
      if (this.annexe2Carte != null) {
        String sourceAnnexe2Carte;
        sourceAnnexe2Carte = this.getAnnexe2Carte();
        String copyAnnexe2Carte =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "annexe2Carte", sourceAnnexe2Carte),
                    sourceAnnexe2Carte));
        copy.setAnnexe2Carte(copyAnnexe2Carte);
      } else {
        copy.annexe2Carte = null;
      }
      if (this.fondCarte != null) {
        String sourceFondCarte;
        sourceFondCarte = this.getFondCarte();
        String copyFondCarte =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "fondCarte", sourceFondCarte), sourceFondCarte));
        copy.setFondCarte(copyFondCarte);
      } else {
        copy.fondCarte = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeContratV2();
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
