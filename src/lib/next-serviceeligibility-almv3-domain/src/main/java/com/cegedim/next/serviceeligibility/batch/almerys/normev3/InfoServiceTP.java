//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoServiceTP complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoServiceTP"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="LISTE_DROIT_ACCES"
 * type="{http://www.almerys.com/NormeV3}infoDroitAcces" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="RIB" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;choice&amp;gt; &amp;lt;element
 * name="PRESTATION" maxOccurs="unbounded"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TITULAIRE" type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="IBAN_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="IBAN_CONTROLE"
 * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="IBAN_BBAN" type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="BIC_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-4"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_PAYS"
 * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="BIC_EMPLACEMENT" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="BIC_BRANCHE" type="{http://www.almerys.com/NormeV3}string-1-3"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NOM_BANQUE"
 * type="{http://www.almerys.com/NormeV3}string-1-100" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-5" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="AGENCE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-5"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NUM_COMPTE"
 * type="{http://www.almerys.com/NormeV3}string-1-11" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CLE_RIB" type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_EFFET" type="{http://www.w3.org/2001/XMLSchema}dateTime"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;element name="COTISATION" maxOccurs="unbounded"&amp;gt; &amp;lt;complexType&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="TITULAIRE" type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="IBAN_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="IBAN_CONTROLE"
 * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="IBAN_BBAN" type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="BIC_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-4"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_PAYS"
 * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="BIC_EMPLACEMENT" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="BIC_BRANCHE" type="{http://www.almerys.com/NormeV3}string-1-3"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NOM_BANQUE"
 * type="{http://www.almerys.com/NormeV3}string-1-100" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CODE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-6" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="AGENCE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-30"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NUM_COMPTE"
 * type="{http://www.almerys.com/NormeV3}string-1-16" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CLE_RIB" type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_EFFET" type="{http://www.w3.org/2001/XMLSchema}dateTime"
 * minOccurs="0"/&amp;gt; &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt;
 * &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element
 * name="COMPENSATION_AUTORISEE" type="{http://www.w3.org/2001/XMLSchema}boolean"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_DEBUT_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date"/&amp;gt; &amp;lt;element name="DATE_FIN_VALIDITE"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_SUSPENSION" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_SUSPENSION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="MOTIF_SUSPENSION"
 * type="{http://www.almerys.com/NormeV3}codeMotifSuspension" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="MOTIF_RESILIATION" type="{http://www.almerys.com/NormeV3}string-1-30"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="JOIGNABILITE"
 * type="{http://www.almerys.com/NormeV3}infoJoignabilite" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="ACTIVATION_DESACTIVATION"
 * type="{http://www.almerys.com/NormeV3}codeActivation"/&amp;gt; &amp;lt;element name="ENVOI"
 * type="{http://www.almerys.com/NormeV3}codeEnvoi"/&amp;gt; &amp;lt;element name="FRAUDE"
 * type="{http://www.almerys.com/NormeV3}infoFraude" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoServiceTP",
    propOrder = {
      "listedroitacces",
      "rib",
      "compensationautorisee",
      "datedebutvalidite",
      "datefinvalidite",
      "datedebutsuspension",
      "datefinsuspension",
      "motifsuspension",
      "motifresiliation",
      "joignabilites",
      "activationdesactivation",
      "envoi",
      "fraude"
    })
public class InfoServiceTP implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "LISTE_DROIT_ACCES")
  protected List<InfoDroitAcces> listedroitacces;

  @XmlElement(name = "RIB")
  protected InfoServiceTP.RIB rib;

  @XmlElement(name = "COMPENSATION_AUTORISEE", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean compensationautorisee;

  @XmlElement(name = "DATE_DEBUT_VALIDITE", required = true)
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutvalidite;

  @XmlElement(name = "DATE_FIN_VALIDITE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinvalidite;

  @XmlElement(name = "DATE_DEBUT_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutsuspension;

  @XmlElement(name = "DATE_FIN_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinsuspension;

  @XmlElement(name = "MOTIF_SUSPENSION")
  @XmlSchemaType(name = "string")
  protected CodeMotifSuspension motifsuspension;

  @XmlElement(name = "MOTIF_RESILIATION")
  protected String motifresiliation;

  @XmlElement(name = "JOIGNABILITE")
  protected List<InfoJoignabilite> joignabilites;

  @XmlElement(name = "ACTIVATION_DESACTIVATION", required = true)
  @XmlSchemaType(name = "string")
  protected CodeActivation activationdesactivation;

  @XmlElement(name = "ENVOI", required = true)
  @XmlSchemaType(name = "string")
  protected CodeEnvoi envoi;

  @XmlElement(name = "FRAUDE")
  protected InfoFraude fraude;

  /**
   * Gets the value of the listedroitacces property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the listedroitacces
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getLISTEDROITACCES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoDroitAcces }
   */
  public List<InfoDroitAcces> getLISTEDROITACCES() {
    if (listedroitacces == null) {
      listedroitacces = new ArrayList<InfoDroitAcces>();
    }
    return this.listedroitacces;
  }

  /**
   * Obtient la valeur de la propriété rib.
   *
   * @return possible object is {@link InfoServiceTP.RIB }
   */
  public InfoServiceTP.RIB getRIB() {
    return rib;
  }

  /**
   * Définit la valeur de la propriété rib.
   *
   * @param value allowed object is {@link InfoServiceTP.RIB }
   */
  public void setRIB(InfoServiceTP.RIB value) {
    this.rib = value;
  }

  /**
   * Obtient la valeur de la propriété compensationautorisee.
   *
   * @return possible object is {@link String }
   */
  public Boolean isCOMPENSATIONAUTORISEE() {
    return compensationautorisee;
  }

  /**
   * Définit la valeur de la propriété compensationautorisee.
   *
   * @param value allowed object is {@link String }
   */
  public void setCOMPENSATIONAUTORISEE(Boolean value) {
    this.compensationautorisee = value;
  }

  /**
   * Obtient la valeur de la propriété datedebutvalidite.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBUTVALIDITE() {
    return datedebutvalidite;
  }

  /**
   * Définit la valeur de la propriété datedebutvalidite.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBUTVALIDITE(String value) {
    this.datedebutvalidite = value;
  }

  /**
   * Obtient la valeur de la propriété datefinvalidite.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFINVALIDITE() {
    return datefinvalidite;
  }

  /**
   * Définit la valeur de la propriété datefinvalidite.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFINVALIDITE(String value) {
    this.datefinvalidite = value;
  }

  /**
   * Obtient la valeur de la propriété datedebutsuspension.
   *
   * @return possible object is {@link String }
   */
  public String getDATEDEBUTSUSPENSION() {
    return datedebutsuspension;
  }

  /**
   * Définit la valeur de la propriété datedebutsuspension.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEDEBUTSUSPENSION(String value) {
    this.datedebutsuspension = value;
  }

  /**
   * Obtient la valeur de la propriété datefinsuspension.
   *
   * @return possible object is {@link String }
   */
  public String getDATEFINSUSPENSION() {
    return datefinsuspension;
  }

  /**
   * Définit la valeur de la propriété datefinsuspension.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEFINSUSPENSION(String value) {
    this.datefinsuspension = value;
  }

  /**
   * Obtient la valeur de la propriété motifsuspension.
   *
   * @return possible object is {@link CodeMotifSuspension }
   */
  public CodeMotifSuspension getMOTIFSUSPENSION() {
    return motifsuspension;
  }

  /**
   * Définit la valeur de la propriété motifsuspension.
   *
   * @param value allowed object is {@link CodeMotifSuspension }
   */
  public void setMOTIFSUSPENSION(CodeMotifSuspension value) {
    this.motifsuspension = value;
  }

  /**
   * Obtient la valeur de la propriété motifresiliation.
   *
   * @return possible object is {@link String }
   */
  public String getMOTIFRESILIATION() {
    return motifresiliation;
  }

  /**
   * Définit la valeur de la propriété motifresiliation.
   *
   * @param value allowed object is {@link String }
   */
  public void setMOTIFRESILIATION(String value) {
    this.motifresiliation = value;
  }

  /**
   * Gets the value of the joignabilites property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the joignabilites
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getJOIGNABILITES().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoJoignabilite }
   */
  public List<InfoJoignabilite> getJOIGNABILITES() {
    if (joignabilites == null) {
      joignabilites = new ArrayList<InfoJoignabilite>();
    }
    return this.joignabilites;
  }

  /**
   * Obtient la valeur de la propriété activationdesactivation.
   *
   * @return possible object is {@link CodeActivation }
   */
  public CodeActivation getACTIVATIONDESACTIVATION() {
    return activationdesactivation;
  }

  /**
   * Définit la valeur de la propriété activationdesactivation.
   *
   * @param value allowed object is {@link CodeActivation }
   */
  public void setACTIVATIONDESACTIVATION(CodeActivation value) {
    this.activationdesactivation = value;
  }

  /**
   * Obtient la valeur de la propriété envoi.
   *
   * @return possible object is {@link CodeEnvoi }
   */
  public CodeEnvoi getENVOI() {
    return envoi;
  }

  /**
   * Définit la valeur de la propriété envoi.
   *
   * @param value allowed object is {@link CodeEnvoi }
   */
  public void setENVOI(CodeEnvoi value) {
    this.envoi = value;
  }

  /**
   * Obtient la valeur de la propriété fraude.
   *
   * @return possible object is {@link InfoFraude }
   */
  public InfoFraude getFRAUDE() {
    return fraude;
  }

  /**
   * Définit la valeur de la propriété fraude.
   *
   * @param value allowed object is {@link InfoFraude }
   */
  public void setFRAUDE(InfoFraude value) {
    this.fraude = value;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;choice&amp;gt; &amp;lt;element
   * name="PRESTATION" maxOccurs="unbounded"&amp;gt; &amp;lt;complexType&amp;gt;
   * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="TITULAIRE" type="{http://www.almerys.com/NormeV3}string-1-80"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="IBAN_PAYS"
   * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="IBAN_CONTROLE" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="IBAN_BBAN" type="{http://www.almerys.com/NormeV3}string-1-30"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_BANQUE"
   * type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="BIC_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="BIC_EMPLACEMENT" type="{http://www.almerys.com/NormeV3}string-1-2"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_BRANCHE"
   * type="{http://www.almerys.com/NormeV3}string-1-3" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="NOM_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-100" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="CODE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-5"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="AGENCE_BANQUE"
   * type="{http://www.almerys.com/NormeV3}string-1-5" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="NUM_COMPTE" type="{http://www.almerys.com/NormeV3}string-1-11" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="CLE_RIB" type="{http://www.almerys.com/NormeV3}string-2"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_EFFET"
   * type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;element name="COTISATION"
   * maxOccurs="unbounded"&amp;gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt;
   * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
   * &amp;lt;sequence&amp;gt; &amp;lt;element name="TITULAIRE"
   * type="{http://www.almerys.com/NormeV3}string-1-80" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="IBAN_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="IBAN_CONTROLE" type="{http://www.almerys.com/NormeV3}string-1-2"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="IBAN_BBAN"
   * type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="BIC_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="BIC_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_EMPLACEMENT"
   * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="BIC_BRANCHE" type="{http://www.almerys.com/NormeV3}string-1-3" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="NOM_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-100"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_BANQUE"
   * type="{http://www.almerys.com/NormeV3}string-1-6" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="AGENCE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="NUM_COMPTE" type="{http://www.almerys.com/NormeV3}string-1-16"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="CLE_RIB"
   * type="{http://www.almerys.com/NormeV3}string-2" minOccurs="0"/&amp;gt; &amp;lt;element
   * name="DATE_EFFET" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
   * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;/choice&amp;gt;
   * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
   * &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"cotisations", "prestations"})
  public static class RIB implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "COTISATION")
    protected List<InfoServiceTP.RIB.COTISATION> cotisations;

    @XmlElement(name = "PRESTATION")
    protected List<InfoServiceTP.RIB.PRESTATION> prestations;

    /**
     * Gets the value of the cotisations property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the cotisations
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getCOTISATIONS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
     * InfoServiceTP.RIB.COTISATION }
     */
    public List<InfoServiceTP.RIB.COTISATION> getCOTISATIONS() {
      if (cotisations == null) {
        cotisations = new ArrayList<InfoServiceTP.RIB.COTISATION>();
      }
      return this.cotisations;
    }

    /**
     * Gets the value of the prestations property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the prestations
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getPRESTATIONS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
     * InfoServiceTP.RIB.PRESTATION }
     */
    public List<InfoServiceTP.RIB.PRESTATION> getPRESTATIONS() {
      if (prestations == null) {
        prestations = new ArrayList<InfoServiceTP.RIB.PRESTATION>();
      }
      return this.prestations;
    }

    /**
     * &lt;p&gt;Classe Java pour anonymous complex type.
     *
     * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
     * classe.
     *
     * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
     * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
     * &amp;lt;element name="TITULAIRE" type="{http://www.almerys.com/NormeV3}string-1-80"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="IBAN_PAYS"
     * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="IBAN_CONTROLE" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="IBAN_BBAN" type="{http://www.almerys.com/NormeV3}string-1-30"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_BANQUE"
     * type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="BIC_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="BIC_EMPLACEMENT" type="{http://www.almerys.com/NormeV3}string-1-2"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_BRANCHE"
     * type="{http://www.almerys.com/NormeV3}string-1-3" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="NOM_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-100" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="CODE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-6"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="AGENCE_BANQUE"
     * type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="NUM_COMPTE" type="{http://www.almerys.com/NormeV3}string-1-16" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="CLE_RIB" type="{http://www.almerys.com/NormeV3}string-2"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_EFFET"
     * type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
     * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
        name = "",
        propOrder = {
          "titulaire",
          "ibanpays",
          "ibancontrole",
          "ibanbban",
          "bicbanque",
          "bicpays",
          "bicemplacement",
          "bicbranche",
          "nombanque",
          "codebanque",
          "agencebanque",
          "numcompte",
          "clerib",
          "dateeffet"
        })
    public static class COTISATION implements Serializable {

      private static final long serialVersionUID = -1L;

      @XmlElement(name = "TITULAIRE")
      protected String titulaire;

      @XmlElement(name = "IBAN_PAYS")
      protected String ibanpays;

      @XmlElement(name = "IBAN_CONTROLE")
      protected String ibancontrole;

      @XmlElement(name = "IBAN_BBAN")
      protected String ibanbban;

      @XmlElement(name = "BIC_BANQUE")
      protected String bicbanque;

      @XmlElement(name = "BIC_PAYS")
      protected String bicpays;

      @XmlElement(name = "BIC_EMPLACEMENT")
      protected String bicemplacement;

      @XmlElement(name = "BIC_BRANCHE")
      protected String bicbranche;

      @XmlElement(name = "NOM_BANQUE")
      protected String nombanque;

      @XmlElement(name = "CODE_BANQUE")
      protected String codebanque;

      @XmlElement(name = "AGENCE_BANQUE")
      protected String agencebanque;

      @XmlElement(name = "NUM_COMPTE")
      protected String numcompte;

      @XmlElement(name = "CLE_RIB")
      protected String clerib;

      @XmlElement(name = "DATE_EFFET", type = String.class)
      @XmlJavaTypeAdapter(DateTimeAdapter.class)
      @XmlSchemaType(name = "dateTime")
      protected Date dateeffet;

      /**
       * Obtient la valeur de la propriété titulaire.
       *
       * @return possible object is {@link String }
       */
      public String getTITULAIRE() {
        return titulaire;
      }

      /**
       * Définit la valeur de la propriété titulaire.
       *
       * @param value allowed object is {@link String }
       */
      public void setTITULAIRE(String value) {
        this.titulaire = value;
      }

      /**
       * Obtient la valeur de la propriété ibanpays.
       *
       * @return possible object is {@link String }
       */
      public String getIBANPAYS() {
        return ibanpays;
      }

      /**
       * Définit la valeur de la propriété ibanpays.
       *
       * @param value allowed object is {@link String }
       */
      public void setIBANPAYS(String value) {
        this.ibanpays = value;
      }

      /**
       * Obtient la valeur de la propriété ibancontrole.
       *
       * @return possible object is {@link String }
       */
      public String getIBANCONTROLE() {
        return ibancontrole;
      }

      /**
       * Définit la valeur de la propriété ibancontrole.
       *
       * @param value allowed object is {@link String }
       */
      public void setIBANCONTROLE(String value) {
        this.ibancontrole = value;
      }

      /**
       * Obtient la valeur de la propriété ibanbban.
       *
       * @return possible object is {@link String }
       */
      public String getIBANBBAN() {
        return ibanbban;
      }

      /**
       * Définit la valeur de la propriété ibanbban.
       *
       * @param value allowed object is {@link String }
       */
      public void setIBANBBAN(String value) {
        this.ibanbban = value;
      }

      /**
       * Obtient la valeur de la propriété bicbanque.
       *
       * @return possible object is {@link String }
       */
      public String getBICBANQUE() {
        return bicbanque;
      }

      /**
       * Définit la valeur de la propriété bicbanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICBANQUE(String value) {
        this.bicbanque = value;
      }

      /**
       * Obtient la valeur de la propriété bicpays.
       *
       * @return possible object is {@link String }
       */
      public String getBICPAYS() {
        return bicpays;
      }

      /**
       * Définit la valeur de la propriété bicpays.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICPAYS(String value) {
        this.bicpays = value;
      }

      /**
       * Obtient la valeur de la propriété bicemplacement.
       *
       * @return possible object is {@link String }
       */
      public String getBICEMPLACEMENT() {
        return bicemplacement;
      }

      /**
       * Définit la valeur de la propriété bicemplacement.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICEMPLACEMENT(String value) {
        this.bicemplacement = value;
      }

      /**
       * Obtient la valeur de la propriété bicbranche.
       *
       * @return possible object is {@link String }
       */
      public String getBICBRANCHE() {
        return bicbranche;
      }

      /**
       * Définit la valeur de la propriété bicbranche.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICBRANCHE(String value) {
        this.bicbranche = value;
      }

      /**
       * Obtient la valeur de la propriété nombanque.
       *
       * @return possible object is {@link String }
       */
      public String getNOMBANQUE() {
        return nombanque;
      }

      /**
       * Définit la valeur de la propriété nombanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setNOMBANQUE(String value) {
        this.nombanque = value;
      }

      /**
       * Obtient la valeur de la propriété codebanque.
       *
       * @return possible object is {@link String }
       */
      public String getCODEBANQUE() {
        return codebanque;
      }

      /**
       * Définit la valeur de la propriété codebanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setCODEBANQUE(String value) {
        this.codebanque = value;
      }

      /**
       * Obtient la valeur de la propriété agencebanque.
       *
       * @return possible object is {@link String }
       */
      public String getAGENCEBANQUE() {
        return agencebanque;
      }

      /**
       * Définit la valeur de la propriété agencebanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setAGENCEBANQUE(String value) {
        this.agencebanque = value;
      }

      /**
       * Obtient la valeur de la propriété numcompte.
       *
       * @return possible object is {@link String }
       */
      public String getNUMCOMPTE() {
        return numcompte;
      }

      /**
       * Définit la valeur de la propriété numcompte.
       *
       * @param value allowed object is {@link String }
       */
      public void setNUMCOMPTE(String value) {
        this.numcompte = value;
      }

      /**
       * Obtient la valeur de la propriété clerib.
       *
       * @return possible object is {@link String }
       */
      public String getCLERIB() {
        return clerib;
      }

      /**
       * Définit la valeur de la propriété clerib.
       *
       * @param value allowed object is {@link String }
       */
      public void setCLERIB(String value) {
        this.clerib = value;
      }

      /**
       * Obtient la valeur de la propriété dateeffet.
       *
       * @return possible object is {@link String }
       */
      public Date getDATEEFFET() {
        return dateeffet;
      }

      /**
       * Définit la valeur de la propriété dateeffet.
       *
       * @param value allowed object is {@link String }
       */
      public void setDATEEFFET(Date value) {
        this.dateeffet = value;
      }
    }

    /**
     * &lt;p&gt;Classe Java pour anonymous complex type.
     *
     * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
     * classe.
     *
     * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
     * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
     * &amp;lt;element name="TITULAIRE" type="{http://www.almerys.com/NormeV3}string-1-80"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="IBAN_PAYS"
     * type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="IBAN_CONTROLE" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="IBAN_BBAN" type="{http://www.almerys.com/NormeV3}string-1-30"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_BANQUE"
     * type="{http://www.almerys.com/NormeV3}string-1-4" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="BIC_PAYS" type="{http://www.almerys.com/NormeV3}string-1-2" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="BIC_EMPLACEMENT" type="{http://www.almerys.com/NormeV3}string-1-2"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="BIC_BRANCHE"
     * type="{http://www.almerys.com/NormeV3}string-1-3" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="NOM_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-100" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="CODE_BANQUE" type="{http://www.almerys.com/NormeV3}string-1-5"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="AGENCE_BANQUE"
     * type="{http://www.almerys.com/NormeV3}string-1-5" minOccurs="0"/&amp;gt; &amp;lt;element
     * name="NUM_COMPTE" type="{http://www.almerys.com/NormeV3}string-1-11" minOccurs="0"/&amp;gt;
     * &amp;lt;element name="CLE_RIB" type="{http://www.almerys.com/NormeV3}string-2"
     * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_EFFET"
     * type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
     * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
        name = "",
        propOrder = {
          "titulaire",
          "ibanpays",
          "ibancontrole",
          "ibanbban",
          "bicbanque",
          "bicpays",
          "bicemplacement",
          "bicbranche",
          "nombanque",
          "codebanque",
          "agencebanque",
          "numcompte",
          "clerib",
          "dateeffet"
        })
    public static class PRESTATION implements Serializable {

      private static final long serialVersionUID = -1L;

      @XmlElement(name = "TITULAIRE")
      protected String titulaire;

      @XmlElement(name = "IBAN_PAYS")
      protected String ibanpays;

      @XmlElement(name = "IBAN_CONTROLE")
      protected String ibancontrole;

      @XmlElement(name = "IBAN_BBAN")
      protected String ibanbban;

      @XmlElement(name = "BIC_BANQUE")
      protected String bicbanque;

      @XmlElement(name = "BIC_PAYS")
      protected String bicpays;

      @XmlElement(name = "BIC_EMPLACEMENT")
      protected String bicemplacement;

      @XmlElement(name = "BIC_BRANCHE")
      protected String bicbranche;

      @XmlElement(name = "NOM_BANQUE")
      protected String nombanque;

      @XmlElement(name = "CODE_BANQUE")
      protected String codebanque;

      @XmlElement(name = "AGENCE_BANQUE")
      protected String agencebanque;

      @XmlElement(name = "NUM_COMPTE")
      protected String numcompte;

      @XmlElement(name = "CLE_RIB")
      protected String clerib;

      @XmlElement(name = "DATE_EFFET", type = String.class)
      @XmlJavaTypeAdapter(DateTimeAdapter.class)
      @XmlSchemaType(name = "dateTime")
      protected Date dateeffet;

      /**
       * Obtient la valeur de la propriété titulaire.
       *
       * @return possible object is {@link String }
       */
      public String getTITULAIRE() {
        return titulaire;
      }

      /**
       * Définit la valeur de la propriété titulaire.
       *
       * @param value allowed object is {@link String }
       */
      public void setTITULAIRE(String value) {
        this.titulaire = value;
      }

      /**
       * Obtient la valeur de la propriété ibanpays.
       *
       * @return possible object is {@link String }
       */
      public String getIBANPAYS() {
        return ibanpays;
      }

      /**
       * Définit la valeur de la propriété ibanpays.
       *
       * @param value allowed object is {@link String }
       */
      public void setIBANPAYS(String value) {
        this.ibanpays = value;
      }

      /**
       * Obtient la valeur de la propriété ibancontrole.
       *
       * @return possible object is {@link String }
       */
      public String getIBANCONTROLE() {
        return ibancontrole;
      }

      /**
       * Définit la valeur de la propriété ibancontrole.
       *
       * @param value allowed object is {@link String }
       */
      public void setIBANCONTROLE(String value) {
        this.ibancontrole = value;
      }

      /**
       * Obtient la valeur de la propriété ibanbban.
       *
       * @return possible object is {@link String }
       */
      public String getIBANBBAN() {
        return ibanbban;
      }

      /**
       * Définit la valeur de la propriété ibanbban.
       *
       * @param value allowed object is {@link String }
       */
      public void setIBANBBAN(String value) {
        this.ibanbban = value;
      }

      /**
       * Obtient la valeur de la propriété bicbanque.
       *
       * @return possible object is {@link String }
       */
      public String getBICBANQUE() {
        return bicbanque;
      }

      /**
       * Définit la valeur de la propriété bicbanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICBANQUE(String value) {
        this.bicbanque = value;
      }

      /**
       * Obtient la valeur de la propriété bicpays.
       *
       * @return possible object is {@link String }
       */
      public String getBICPAYS() {
        return bicpays;
      }

      /**
       * Définit la valeur de la propriété bicpays.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICPAYS(String value) {
        this.bicpays = value;
      }

      /**
       * Obtient la valeur de la propriété bicemplacement.
       *
       * @return possible object is {@link String }
       */
      public String getBICEMPLACEMENT() {
        return bicemplacement;
      }

      /**
       * Définit la valeur de la propriété bicemplacement.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICEMPLACEMENT(String value) {
        this.bicemplacement = value;
      }

      /**
       * Obtient la valeur de la propriété bicbranche.
       *
       * @return possible object is {@link String }
       */
      public String getBICBRANCHE() {
        return bicbranche;
      }

      /**
       * Définit la valeur de la propriété bicbranche.
       *
       * @param value allowed object is {@link String }
       */
      public void setBICBRANCHE(String value) {
        this.bicbranche = value;
      }

      /**
       * Obtient la valeur de la propriété nombanque.
       *
       * @return possible object is {@link String }
       */
      public String getNOMBANQUE() {
        return nombanque;
      }

      /**
       * Définit la valeur de la propriété nombanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setNOMBANQUE(String value) {
        this.nombanque = value;
      }

      /**
       * Obtient la valeur de la propriété codebanque.
       *
       * @return possible object is {@link String }
       */
      public String getCODEBANQUE() {
        return codebanque;
      }

      /**
       * Définit la valeur de la propriété codebanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setCODEBANQUE(String value) {
        this.codebanque = value;
      }

      /**
       * Obtient la valeur de la propriété agencebanque.
       *
       * @return possible object is {@link String }
       */
      public String getAGENCEBANQUE() {
        return agencebanque;
      }

      /**
       * Définit la valeur de la propriété agencebanque.
       *
       * @param value allowed object is {@link String }
       */
      public void setAGENCEBANQUE(String value) {
        this.agencebanque = value;
      }

      /**
       * Obtient la valeur de la propriété numcompte.
       *
       * @return possible object is {@link String }
       */
      public String getNUMCOMPTE() {
        return numcompte;
      }

      /**
       * Définit la valeur de la propriété numcompte.
       *
       * @param value allowed object is {@link String }
       */
      public void setNUMCOMPTE(String value) {
        this.numcompte = value;
      }

      /**
       * Obtient la valeur de la propriété clerib.
       *
       * @return possible object is {@link String }
       */
      public String getCLERIB() {
        return clerib;
      }

      /**
       * Définit la valeur de la propriété clerib.
       *
       * @param value allowed object is {@link String }
       */
      public void setCLERIB(String value) {
        this.clerib = value;
      }

      /**
       * Obtient la valeur de la propriété dateeffet.
       *
       * @return possible object is {@link String }
       */
      public Date getDATEEFFET() {
        return dateeffet;
      }

      /**
       * Définit la valeur de la propriété dateeffet.
       *
       * @param value allowed object is {@link String }
       */
      public void setDATEEFFET(Date value) {
        this.dateeffet = value;
      }
    }
  }
}
