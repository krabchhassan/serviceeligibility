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
 * Classe Java pour type_infoCarteTP complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="type_infoCarteTP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="titre_attestation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="num_amc_RNM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="num_amc_prefectoral" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nom_adresse_amc_ligne1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nom_adresse_amc_ligne2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nom_adresse_amc_ligne3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nom_adresse_amc_ligne4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="logo_assureur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="code_regime" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="0"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="code_gestion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="num_contrat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="num_adherent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="date_debut_droits" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="date_fin_droits" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="adressage_SV" type="{http://norme.cegedimassurances.com/carte}type_adressage_sesam_vitale" minOccurs="0"/&gt;
 *         &lt;element name="critere_secondaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="logo_operateur_TP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="logo_promoteur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="liste_renvois" type="{http://norme.cegedimassurances.com/carte}type_renvoi" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "type_infoCarteTP",
    propOrder = {
      "titreAttestation",
      "numAmcRNM",
      "numAmcPrefectoral",
      "nomAdresseAmcLigne1",
      "nomAdresseAmcLigne2",
      "nomAdresseAmcLigne3",
      "nomAdresseAmcLigne4",
      "logoAssureur",
      "codeRegime",
      "codeGestion",
      "numContrat",
      "numAdherent",
      "dateDebutDroits",
      "dateFinDroits",
      "adressageSV",
      "critereSecondaire",
      "logoOperateurTP",
      "logoPromoteur",
      "listeRenvois"
    })
public class TypeInfoCarteTP implements Serializable, Cloneable, CopyTo {

  @XmlElement(name = "titre_attestation")
  protected String titreAttestation;

  @XmlElement(name = "num_amc_RNM")
  protected String numAmcRNM;

  @XmlElement(name = "num_amc_prefectoral", required = true)
  @NotNull
  protected String numAmcPrefectoral;

  @XmlElement(name = "nom_adresse_amc_ligne1")
  protected String nomAdresseAmcLigne1;

  @XmlElement(name = "nom_adresse_amc_ligne2")
  protected String nomAdresseAmcLigne2;

  @XmlElement(name = "nom_adresse_amc_ligne3")
  protected String nomAdresseAmcLigne3;

  @XmlElement(name = "nom_adresse_amc_ligne4")
  protected String nomAdresseAmcLigne4;

  @XmlElement(name = "logo_assureur")
  protected String logoAssureur;

  @XmlElement(name = "code_regime")
  @Size(min = 0)
  protected String codeRegime;

  @XmlElement(name = "code_gestion")
  protected String codeGestion;

  @XmlElement(name = "num_contrat")
  protected String numContrat;

  @XmlElement(name = "num_adherent")
  protected String numAdherent;

  @XmlElement(name = "date_debut_droits", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateDebutDroits;

  @XmlElement(name = "date_fin_droits", required = true)
  @XmlSchemaType(name = "date")
  @NotNull
  protected XMLGregorianCalendar dateFinDroits;

  @XmlElement(name = "adressage_SV")
  @Valid
  protected TypeAdressageSesamVitale adressageSV;

  @XmlElement(name = "critere_secondaire")
  protected String critereSecondaire;

  @XmlElement(name = "logo_operateur_TP")
  protected String logoOperateurTP;

  @XmlElement(name = "logo_promoteur")
  protected String logoPromoteur;

  @XmlElement(name = "liste_renvois")
  @Valid
  protected List<TypeRenvoi> listeRenvois;

  /**
   * Obtient la valeur de la propriété titreAttestation.
   *
   * @return possible object is {@link String }
   */
  public String getTitreAttestation() {
    return titreAttestation;
  }

  /**
   * Définit la valeur de la propriété titreAttestation.
   *
   * @param value allowed object is {@link String }
   */
  public void setTitreAttestation(String value) {
    this.titreAttestation = value;
  }

  /**
   * Obtient la valeur de la propriété numAmcRNM.
   *
   * @return possible object is {@link String }
   */
  public String getNumAmcRNM() {
    return numAmcRNM;
  }

  /**
   * Définit la valeur de la propriété numAmcRNM.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumAmcRNM(String value) {
    this.numAmcRNM = value;
  }

  /**
   * Obtient la valeur de la propriété numAmcPrefectoral.
   *
   * @return possible object is {@link String }
   */
  public String getNumAmcPrefectoral() {
    return numAmcPrefectoral;
  }

  /**
   * Définit la valeur de la propriété numAmcPrefectoral.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumAmcPrefectoral(String value) {
    this.numAmcPrefectoral = value;
  }

  /**
   * Obtient la valeur de la propriété nomAdresseAmcLigne1.
   *
   * @return possible object is {@link String }
   */
  public String getNomAdresseAmcLigne1() {
    return nomAdresseAmcLigne1;
  }

  /**
   * Définit la valeur de la propriété nomAdresseAmcLigne1.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAdresseAmcLigne1(String value) {
    this.nomAdresseAmcLigne1 = value;
  }

  /**
   * Obtient la valeur de la propriété nomAdresseAmcLigne2.
   *
   * @return possible object is {@link String }
   */
  public String getNomAdresseAmcLigne2() {
    return nomAdresseAmcLigne2;
  }

  /**
   * Définit la valeur de la propriété nomAdresseAmcLigne2.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAdresseAmcLigne2(String value) {
    this.nomAdresseAmcLigne2 = value;
  }

  /**
   * Obtient la valeur de la propriété nomAdresseAmcLigne3.
   *
   * @return possible object is {@link String }
   */
  public String getNomAdresseAmcLigne3() {
    return nomAdresseAmcLigne3;
  }

  /**
   * Définit la valeur de la propriété nomAdresseAmcLigne3.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAdresseAmcLigne3(String value) {
    this.nomAdresseAmcLigne3 = value;
  }

  /**
   * Obtient la valeur de la propriété nomAdresseAmcLigne4.
   *
   * @return possible object is {@link String }
   */
  public String getNomAdresseAmcLigne4() {
    return nomAdresseAmcLigne4;
  }

  /**
   * Définit la valeur de la propriété nomAdresseAmcLigne4.
   *
   * @param value allowed object is {@link String }
   */
  public void setNomAdresseAmcLigne4(String value) {
    this.nomAdresseAmcLigne4 = value;
  }

  /**
   * Obtient la valeur de la propriété logoAssureur.
   *
   * @return possible object is {@link String }
   */
  public String getLogoAssureur() {
    return logoAssureur;
  }

  /**
   * Définit la valeur de la propriété logoAssureur.
   *
   * @param value allowed object is {@link String }
   */
  public void setLogoAssureur(String value) {
    this.logoAssureur = value;
  }

  /**
   * Obtient la valeur de la propriété codeRegime.
   *
   * @return possible object is {@link String }
   */
  public String getCodeRegime() {
    return codeRegime;
  }

  /**
   * Définit la valeur de la propriété codeRegime.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeRegime(String value) {
    this.codeRegime = value;
  }

  /**
   * Obtient la valeur de la propriété codeGestion.
   *
   * @return possible object is {@link String }
   */
  public String getCodeGestion() {
    return codeGestion;
  }

  /**
   * Définit la valeur de la propriété codeGestion.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeGestion(String value) {
    this.codeGestion = value;
  }

  /**
   * Obtient la valeur de la propriété numContrat.
   *
   * @return possible object is {@link String }
   */
  public String getNumContrat() {
    return numContrat;
  }

  /**
   * Définit la valeur de la propriété numContrat.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumContrat(String value) {
    this.numContrat = value;
  }

  /**
   * Obtient la valeur de la propriété numAdherent.
   *
   * @return possible object is {@link String }
   */
  public String getNumAdherent() {
    return numAdherent;
  }

  /**
   * Définit la valeur de la propriété numAdherent.
   *
   * @param value allowed object is {@link String }
   */
  public void setNumAdherent(String value) {
    this.numAdherent = value;
  }

  /**
   * Obtient la valeur de la propriété dateDebutDroits.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateDebutDroits() {
    return dateDebutDroits;
  }

  /**
   * Définit la valeur de la propriété dateDebutDroits.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateDebutDroits(XMLGregorianCalendar value) {
    this.dateDebutDroits = value;
  }

  /**
   * Obtient la valeur de la propriété dateFinDroits.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getDateFinDroits() {
    return dateFinDroits;
  }

  /**
   * Définit la valeur de la propriété dateFinDroits.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  public void setDateFinDroits(XMLGregorianCalendar value) {
    this.dateFinDroits = value;
  }

  /**
   * Obtient la valeur de la propriété adressageSV.
   *
   * @return possible object is {@link TypeAdressageSesamVitale }
   */
  public TypeAdressageSesamVitale getAdressageSV() {
    return adressageSV;
  }

  /**
   * Définit la valeur de la propriété adressageSV.
   *
   * @param value allowed object is {@link TypeAdressageSesamVitale }
   */
  public void setAdressageSV(TypeAdressageSesamVitale value) {
    this.adressageSV = value;
  }

  /**
   * Obtient la valeur de la propriété critereSecondaire.
   *
   * @return possible object is {@link String }
   */
  public String getCritereSecondaire() {
    return critereSecondaire;
  }

  /**
   * Définit la valeur de la propriété critereSecondaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setCritereSecondaire(String value) {
    this.critereSecondaire = value;
  }

  /**
   * Obtient la valeur de la propriété logoOperateurTP.
   *
   * @return possible object is {@link String }
   */
  public String getLogoOperateurTP() {
    return logoOperateurTP;
  }

  /**
   * Définit la valeur de la propriété logoOperateurTP.
   *
   * @param value allowed object is {@link String }
   */
  public void setLogoOperateurTP(String value) {
    this.logoOperateurTP = value;
  }

  /**
   * Obtient la valeur de la propriété logoPromoteur.
   *
   * @return possible object is {@link String }
   */
  public String getLogoPromoteur() {
    return logoPromoteur;
  }

  /**
   * Définit la valeur de la propriété logoPromoteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setLogoPromoteur(String value) {
    this.logoPromoteur = value;
  }

  /**
   * Gets the value of the listeRenvois property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the listeRenvois property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getListeRenvois().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeRenvoi }
   */
  public List<TypeRenvoi> getListeRenvois() {
    if (listeRenvois == null) {
      listeRenvois = new ArrayList<TypeRenvoi>();
    }
    return this.listeRenvois;
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
    if (draftCopy instanceof TypeInfoCarteTP) {
      final TypeInfoCarteTP copy = ((TypeInfoCarteTP) draftCopy);
      if (this.titreAttestation != null) {
        String sourceTitreAttestation;
        sourceTitreAttestation = this.getTitreAttestation();
        String copyTitreAttestation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "titreAttestation", sourceTitreAttestation),
                    sourceTitreAttestation));
        copy.setTitreAttestation(copyTitreAttestation);
      } else {
        copy.titreAttestation = null;
      }
      if (this.numAmcRNM != null) {
        String sourceNumAmcRNM;
        sourceNumAmcRNM = this.getNumAmcRNM();
        String copyNumAmcRNM =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numAmcRNM", sourceNumAmcRNM), sourceNumAmcRNM));
        copy.setNumAmcRNM(copyNumAmcRNM);
      } else {
        copy.numAmcRNM = null;
      }
      if (this.numAmcPrefectoral != null) {
        String sourceNumAmcPrefectoral;
        sourceNumAmcPrefectoral = this.getNumAmcPrefectoral();
        String copyNumAmcPrefectoral =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numAmcPrefectoral", sourceNumAmcPrefectoral),
                    sourceNumAmcPrefectoral));
        copy.setNumAmcPrefectoral(copyNumAmcPrefectoral);
      } else {
        copy.numAmcPrefectoral = null;
      }
      if (this.nomAdresseAmcLigne1 != null) {
        String sourceNomAdresseAmcLigne1;
        sourceNomAdresseAmcLigne1 = this.getNomAdresseAmcLigne1();
        String copyNomAdresseAmcLigne1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "nomAdresseAmcLigne1", sourceNomAdresseAmcLigne1),
                    sourceNomAdresseAmcLigne1));
        copy.setNomAdresseAmcLigne1(copyNomAdresseAmcLigne1);
      } else {
        copy.nomAdresseAmcLigne1 = null;
      }
      if (this.nomAdresseAmcLigne2 != null) {
        String sourceNomAdresseAmcLigne2;
        sourceNomAdresseAmcLigne2 = this.getNomAdresseAmcLigne2();
        String copyNomAdresseAmcLigne2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "nomAdresseAmcLigne2", sourceNomAdresseAmcLigne2),
                    sourceNomAdresseAmcLigne2));
        copy.setNomAdresseAmcLigne2(copyNomAdresseAmcLigne2);
      } else {
        copy.nomAdresseAmcLigne2 = null;
      }
      if (this.nomAdresseAmcLigne3 != null) {
        String sourceNomAdresseAmcLigne3;
        sourceNomAdresseAmcLigne3 = this.getNomAdresseAmcLigne3();
        String copyNomAdresseAmcLigne3 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "nomAdresseAmcLigne3", sourceNomAdresseAmcLigne3),
                    sourceNomAdresseAmcLigne3));
        copy.setNomAdresseAmcLigne3(copyNomAdresseAmcLigne3);
      } else {
        copy.nomAdresseAmcLigne3 = null;
      }
      if (this.nomAdresseAmcLigne4 != null) {
        String sourceNomAdresseAmcLigne4;
        sourceNomAdresseAmcLigne4 = this.getNomAdresseAmcLigne4();
        String copyNomAdresseAmcLigne4 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "nomAdresseAmcLigne4", sourceNomAdresseAmcLigne4),
                    sourceNomAdresseAmcLigne4));
        copy.setNomAdresseAmcLigne4(copyNomAdresseAmcLigne4);
      } else {
        copy.nomAdresseAmcLigne4 = null;
      }
      if (this.logoAssureur != null) {
        String sourceLogoAssureur;
        sourceLogoAssureur = this.getLogoAssureur();
        String copyLogoAssureur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "logoAssureur", sourceLogoAssureur),
                    sourceLogoAssureur));
        copy.setLogoAssureur(copyLogoAssureur);
      } else {
        copy.logoAssureur = null;
      }
      if (this.codeRegime != null) {
        String sourceCodeRegime;
        sourceCodeRegime = this.getCodeRegime();
        String copyCodeRegime =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeRegime", sourceCodeRegime),
                    sourceCodeRegime));
        copy.setCodeRegime(copyCodeRegime);
      } else {
        copy.codeRegime = null;
      }
      if (this.codeGestion != null) {
        String sourceCodeGestion;
        sourceCodeGestion = this.getCodeGestion();
        String copyCodeGestion =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codeGestion", sourceCodeGestion),
                    sourceCodeGestion));
        copy.setCodeGestion(copyCodeGestion);
      } else {
        copy.codeGestion = null;
      }
      if (this.numContrat != null) {
        String sourceNumContrat;
        sourceNumContrat = this.getNumContrat();
        String copyNumContrat =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numContrat", sourceNumContrat),
                    sourceNumContrat));
        copy.setNumContrat(copyNumContrat);
      } else {
        copy.numContrat = null;
      }
      if (this.numAdherent != null) {
        String sourceNumAdherent;
        sourceNumAdherent = this.getNumAdherent();
        String copyNumAdherent =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numAdherent", sourceNumAdherent),
                    sourceNumAdherent));
        copy.setNumAdherent(copyNumAdherent);
      } else {
        copy.numAdherent = null;
      }
      if (this.dateDebutDroits != null) {
        XMLGregorianCalendar sourceDateDebutDroits;
        sourceDateDebutDroits = this.getDateDebutDroits();
        XMLGregorianCalendar copyDateDebutDroits =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateDebutDroits", sourceDateDebutDroits),
                    sourceDateDebutDroits));
        copy.setDateDebutDroits(copyDateDebutDroits);
      } else {
        copy.dateDebutDroits = null;
      }
      if (this.dateFinDroits != null) {
        XMLGregorianCalendar sourceDateFinDroits;
        sourceDateFinDroits = this.getDateFinDroits();
        XMLGregorianCalendar copyDateFinDroits =
            ((XMLGregorianCalendar)
                strategy.copy(
                    LocatorUtils.property(locator, "dateFinDroits", sourceDateFinDroits),
                    sourceDateFinDroits));
        copy.setDateFinDroits(copyDateFinDroits);
      } else {
        copy.dateFinDroits = null;
      }
      if (this.adressageSV != null) {
        TypeAdressageSesamVitale sourceAdressageSV;
        sourceAdressageSV = this.getAdressageSV();
        TypeAdressageSesamVitale copyAdressageSV =
            ((TypeAdressageSesamVitale)
                strategy.copy(
                    LocatorUtils.property(locator, "adressageSV", sourceAdressageSV),
                    sourceAdressageSV));
        copy.setAdressageSV(copyAdressageSV);
      } else {
        copy.adressageSV = null;
      }
      if (this.critereSecondaire != null) {
        String sourceCritereSecondaire;
        sourceCritereSecondaire = this.getCritereSecondaire();
        String copyCritereSecondaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "critereSecondaire", sourceCritereSecondaire),
                    sourceCritereSecondaire));
        copy.setCritereSecondaire(copyCritereSecondaire);
      } else {
        copy.critereSecondaire = null;
      }
      if (this.logoOperateurTP != null) {
        String sourceLogoOperateurTP;
        sourceLogoOperateurTP = this.getLogoOperateurTP();
        String copyLogoOperateurTP =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "logoOperateurTP", sourceLogoOperateurTP),
                    sourceLogoOperateurTP));
        copy.setLogoOperateurTP(copyLogoOperateurTP);
      } else {
        copy.logoOperateurTP = null;
      }
      if (this.logoPromoteur != null) {
        String sourceLogoPromoteur;
        sourceLogoPromoteur = this.getLogoPromoteur();
        String copyLogoPromoteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "logoPromoteur", sourceLogoPromoteur),
                    sourceLogoPromoteur));
        copy.setLogoPromoteur(copyLogoPromoteur);
      } else {
        copy.logoPromoteur = null;
      }
      if ((this.listeRenvois != null) && (!this.listeRenvois.isEmpty())) {
        List<TypeRenvoi> sourceListeRenvois;
        sourceListeRenvois =
            (((this.listeRenvois != null) && (!this.listeRenvois.isEmpty()))
                ? this.getListeRenvois()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeRenvoi> copyListeRenvois =
            ((List<TypeRenvoi>)
                strategy.copy(
                    LocatorUtils.property(locator, "listeRenvois", sourceListeRenvois),
                    sourceListeRenvois));
        copy.listeRenvois = null;
        if (copyListeRenvois != null) {
          List<TypeRenvoi> uniqueListeRenvoisl = copy.getListeRenvois();
          uniqueListeRenvoisl.addAll(copyListeRenvois);
        }
      } else {
        copy.listeRenvois = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeInfoCarteTP();
  }
}
