package com.cegedimassurances.norme.base_de_droit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * Classe Java pour type_info_bdd complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_info_bdd"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type_garanties" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="type_recherche_benef"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="type_recherche_segment"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="2"/&gt;
 *               &lt;enumeration value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="type_segment_recherche" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="8"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="liste_segment_recherche" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence maxOccurs="unbounded"&gt;
 *                   &lt;element name="segment_recherche" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="type_profondeur_recherche"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="2"/&gt;
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
    name = "type_info_bdd",
    propOrder = {
      "typeGaranties",
      "typeRechercheBenef",
      "typeRechercheSegment",
      "typeSegmentRecherche",
      "listeSegmentRecherche",
      "typeProfondeurRecherche"
    })
public class TypeInfoBdd implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "type_garanties")
  protected String typeGaranties;

  @XmlElement(name = "type_recherche_benef", required = true)
  @NotNull
  protected TypeInfoBdd.TypeRechercheBeneficiaire typeRechercheBenef;

  @XmlElement(name = "type_recherche_segment", required = true)
  @NotNull
  protected TypeInfoBdd.TypeRechercheSegment typeRechercheSegment;

  @XmlElement(name = "type_segment_recherche")
  @Size(max = 8)
  protected String typeSegmentRecherche;

  @XmlElement(name = "liste_segment_recherche")
  @Valid
  protected TypeInfoBdd.ListeSegmentRecherche listeSegmentRecherche;

  @XmlElement(name = "type_profondeur_recherche", required = true)
  @NotNull
  protected TypeInfoBdd.TypeProfondeurRecherche typeProfondeurRecherche;

  /**
   * Obtient la valeur de la propriété typeGaranties.
   *
   * @return possible object is {@link String }
   */
  public String getTypeGaranties() {
    return typeGaranties;
  }

  /**
   * Définit la valeur de la propriété typeGaranties.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeGaranties(String value) {
    this.typeGaranties = value;
  }

  /**
   * Obtient la valeur de la propriété typeRechercheBenef.
   *
   * @return possible object is {@link TypeRechercheBeneficiaire }
   */
  public TypeRechercheBeneficiaire getTypeRechercheBenef() {
    return typeRechercheBenef;
  }

  /**
   * Définit la valeur de la propriété typeRechercheBenef.
   *
   * @param value allowed object is {@link TypeRechercheBeneficiaire }
   */
  public void setTypeRechercheBenef(TypeRechercheBeneficiaire value) {
    this.typeRechercheBenef = value;
  }

  /**
   * Obtient la valeur de la propriété typeRechercheSegment.
   *
   * @return possible object is {@link TypeRechercheSegment }
   */
  public TypeRechercheSegment getTypeRechercheSegment() {
    return typeRechercheSegment;
  }

  /**
   * Définit la valeur de la propriété typeRechercheSegment.
   *
   * @param value allowed object is {@link TypeRechercheSegment }
   */
  public void setTypeRechercheSegment(TypeRechercheSegment value) {
    this.typeRechercheSegment = value;
  }

  /**
   * Obtient la valeur de la propriété typeSegmentRecherche.
   *
   * @return possible object is {@link String }
   */
  public String getTypeSegmentRecherche() {
    return typeSegmentRecherche;
  }

  /**
   * Définit la valeur de la propriété typeSegmentRecherche.
   *
   * @param value allowed object is {@link String }
   */
  public void setTypeSegmentRecherche(String value) {
    this.typeSegmentRecherche = value;
  }

  /**
   * Obtient la valeur de la propriété listeSegmentRecherche.
   *
   * @return possible object is {@link ListeSegmentRecherche }
   */
  public ListeSegmentRecherche getListeSegmentRecherche() {
    return listeSegmentRecherche;
  }

  /**
   * Définit la valeur de la propriété listeSegmentRecherche.
   *
   * @param value allowed object is {@link ListeSegmentRecherche }
   */
  public void setListeSegmentRecherche(ListeSegmentRecherche value) {
    this.listeSegmentRecherche = value;
  }

  /**
   * Obtient la valeur de la propriété typeProfondeurRecherche.
   *
   * @return possible object is {@link TypeProfondeurRecherche }
   */
  public TypeProfondeurRecherche getTypeProfondeurRecherche() {
    return typeProfondeurRecherche;
  }

  /**
   * Définit la valeur de la propriété typeProfondeurRecherche.
   *
   * @param value allowed object is {@link TypeProfondeurRecherche }
   */
  public void setTypeProfondeurRecherche(TypeProfondeurRecherche value) {
    this.typeProfondeurRecherche = value;
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
    if (draftCopy instanceof TypeInfoBdd) {
      final TypeInfoBdd copy = ((TypeInfoBdd) draftCopy);
      if (this.typeGaranties != null) {
        String sourceTypeGaranties;
        sourceTypeGaranties = this.getTypeGaranties();
        String copyTypeGaranties =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "typeGaranties", sourceTypeGaranties),
                    sourceTypeGaranties));
        copy.setTypeGaranties(copyTypeGaranties);
      } else {
        copy.typeGaranties = null;
      }
      if (this.typeRechercheBenef != null) {
        TypeRechercheBeneficiaire sourceTypeRechercheBenef;
        sourceTypeRechercheBenef = this.getTypeRechercheBenef();
        TypeRechercheBeneficiaire copyTypeRechercheBenef =
            ((TypeRechercheBeneficiaire)
                strategy.copy(
                    LocatorUtils.property(locator, "typeRechercheBenef", sourceTypeRechercheBenef),
                    sourceTypeRechercheBenef));
        copy.setTypeRechercheBenef(copyTypeRechercheBenef);
      } else {
        copy.typeRechercheBenef = null;
      }
      if (this.typeRechercheSegment != null) {
        TypeRechercheSegment sourceTypeRechercheSegment;
        sourceTypeRechercheSegment = this.getTypeRechercheSegment();
        TypeRechercheSegment copyTypeRechercheSegment =
            ((TypeRechercheSegment)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeRechercheSegment", sourceTypeRechercheSegment),
                    sourceTypeRechercheSegment));
        copy.setTypeRechercheSegment(copyTypeRechercheSegment);
      } else {
        copy.typeRechercheSegment = null;
      }
      if (this.typeSegmentRecherche != null) {
        String sourceTypeSegmentRecherche;
        sourceTypeSegmentRecherche = this.getTypeSegmentRecherche();
        String copyTypeSegmentRecherche =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeSegmentRecherche", sourceTypeSegmentRecherche),
                    sourceTypeSegmentRecherche));
        copy.setTypeSegmentRecherche(copyTypeSegmentRecherche);
      } else {
        copy.typeSegmentRecherche = null;
      }
      if (this.listeSegmentRecherche != null) {
        ListeSegmentRecherche sourceListeSegmentRecherche;
        sourceListeSegmentRecherche = this.getListeSegmentRecherche();
        ListeSegmentRecherche copyListeSegmentRecherche =
            ((ListeSegmentRecherche)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "listeSegmentRecherche", sourceListeSegmentRecherche),
                    sourceListeSegmentRecherche));
        copy.setListeSegmentRecherche(copyListeSegmentRecherche);
      } else {
        copy.listeSegmentRecherche = null;
      }
      if (this.typeProfondeurRecherche != null) {
        TypeProfondeurRecherche sourceTypeProfondeurRecherche;
        sourceTypeProfondeurRecherche = this.getTypeProfondeurRecherche();
        TypeProfondeurRecherche copyTypeProfondeurRecherche =
            ((TypeProfondeurRecherche)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "typeProfondeurRecherche", sourceTypeProfondeurRecherche),
                    sourceTypeProfondeurRecherche));
        copy.setTypeProfondeurRecherche(copyTypeProfondeurRecherche);
      } else {
        copy.typeProfondeurRecherche = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInfoBdd();
  }

  /**
   * Classe Java pour anonymous complex type.
   *
   * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence maxOccurs="unbounded"&gt;
   *         &lt;element name="segment_recherche" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"segmentRecherche"})
  public static class ListeSegmentRecherche implements Serializable, Cloneable, CopyTo {

    @XmlElement(name = "segment_recherche", required = true)
    @NotNull
    @Size(min = 1)
    protected java.util.List<String> segmentRecherche;

    /**
     * Gets the value of the segmentRecherche property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the segmentRecherche property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getSegmentRecherche().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link String }
     */
    public java.util.List<String> getSegmentRecherche() {
      if (segmentRecherche == null) {
        segmentRecherche = new ArrayList<String>();
      }
      return this.segmentRecherche;
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
      if (draftCopy instanceof ListeSegmentRecherche) {
        final ListeSegmentRecherche copy = ((ListeSegmentRecherche) draftCopy);
        if ((this.segmentRecherche != null) && (!this.segmentRecherche.isEmpty())) {
          java.util.List<String> sourceSegmentRecherche;
          sourceSegmentRecherche =
              (((this.segmentRecherche != null) && (!this.segmentRecherche.isEmpty()))
                  ? this.getSegmentRecherche()
                  : null);
          @SuppressWarnings("unchecked")
          java.util.List<String> copySegmentRecherche =
              ((java.util.List<String>)
                  strategy.copy(
                      LocatorUtils.property(locator, "segmentRecherche", sourceSegmentRecherche),
                      sourceSegmentRecherche));
          copy.segmentRecherche = null;
          if (copySegmentRecherche != null) {
            java.util.List<String> uniqueSegmentRecherchel = copy.getSegmentRecherche();
            uniqueSegmentRecherchel.addAll(copySegmentRecherche);
          }
        } else {
          copy.segmentRecherche = null;
        }
      }
      return draftCopy;
    }

    public Object createNewInstance() {
      return new ListeSegmentRecherche();
    }
  }

  /**
   * Classe Java pour null.
   *
   * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
   *
   * <p>
   *
   * <pre>
   * &lt;simpleType&gt;
   *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
   *     &lt;enumeration value="0"/&gt;
   *     &lt;enumeration value="1"/&gt;
   *     &lt;enumeration value="2"/&gt;
   *   &lt;/restriction&gt;
   * &lt;/simpleType&gt;
   * </pre>
   */
  @XmlType(name = "")
  @XmlEnum
  public enum TypeProfondeurRecherche {
    @XmlEnumValue("0")
    AVEC_FORMULES("0"),
    @XmlEnumValue("1")
    SANS_FORMULES("1"),
    @XmlEnumValue("2")
    SANS_DOMAINES("2");

    private final String value;

    TypeProfondeurRecherche(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    public static TypeProfondeurRecherche fromValue(String v) {
      for (TypeProfondeurRecherche c : TypeProfondeurRecherche.values()) {
        if (c.value.equals(v)) {
          return c;
        }
      }
      throw new IllegalArgumentException(v);
    }
  }

  /**
   * Classe Java pour null.
   *
   * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
   *
   * <p>
   *
   * <pre>
   * &lt;simpleType&gt;
   *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
   *     &lt;enumeration value="0"/&gt;
   *     &lt;enumeration value="1"/&gt;
   *   &lt;/restriction&gt;
   * &lt;/simpleType&gt;
   * </pre>
   */
  @XmlType(name = "")
  @XmlEnum
  public enum TypeRechercheBeneficiaire {
    @XmlEnumValue("0")
    BENEFICIAIRE("0"),
    @XmlEnumValue("1")
    CARTE_FAMILLE("1");

    private final String value;

    TypeRechercheBeneficiaire(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    public static TypeRechercheBeneficiaire fromValue(String v) {
      for (TypeRechercheBeneficiaire c : TypeRechercheBeneficiaire.values()) {
        if (c.value.equals(v)) {
          return c;
        }
      }
      throw new IllegalArgumentException(v);
    }
  }

  /**
   * Classe Java pour null.
   *
   * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
   *
   * <p>
   *
   * <pre>
   * &lt;simpleType&gt;
   *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
   *     &lt;enumeration value="0"/&gt;
   *     &lt;enumeration value="1"/&gt;
   *     &lt;enumeration value="2"/&gt;
   *     &lt;enumeration value="3"/&gt;
   *   &lt;/restriction&gt;
   * &lt;/simpleType&gt;
   * </pre>
   */
  @XmlType(name = "")
  @XmlEnum
  public enum TypeRechercheSegment {
    @XmlEnumValue("0")
    PAS_DE_RECHERCHE_SEGMENT("0"),
    @XmlEnumValue("1")
    MONO_SEGMENT("1"),
    @XmlEnumValue("2")
    TOUT_SEGMENT("2"),
    @XmlEnumValue("3")
    LISTE_SEGMENT("3");

    private final String value;

    TypeRechercheSegment(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    public static TypeRechercheSegment fromValue(String v) {
      for (TypeRechercheSegment c : TypeRechercheSegment.values()) {
        if (c.value.equals(v)) {
          return c;
        }
      }
      throw new IllegalArgumentException(v);
    }
  }
}
