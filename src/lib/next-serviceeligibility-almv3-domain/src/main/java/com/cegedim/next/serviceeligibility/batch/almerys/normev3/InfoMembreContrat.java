//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.DateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * &lt;p&gt;Classe Java pour infoMembreContrat complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoMembreContrat"&amp;gt;
 * &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="SOUSCRIPTEUR" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt; &amp;lt;element
 * name="POSITION" type="{http://www.almerys.com/NormeV3}string-2"/&amp;gt; &amp;lt;element
 * name="TYPE_REGIME" type="{http://www.almerys.com/NormeV3}codeRegime"/&amp;gt; &amp;lt;element
 * name="DATE_ENTREE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_INSCRIPTION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="INDIVIDU"
 * type="{http://www.almerys.com/NormeV3}infoIndividuOs"/&amp;gt; &amp;lt;element
 * name="ADRESSE_MEMBRE" type="{http://www.almerys.com/NormeV3}infoAdresseParticulier"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="MEDIA_ENVOI"
 * type="{http://www.almerys.com/NormeV3}infoMediaEnvoi" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="JOIGNABILITE" type="{http://www.almerys.com/NormeV3}infoJoignabilite" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NUM_ADHESION"
 * type="{http://www.almerys.com/NormeV3}string-1-20" maxOccurs="2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="AUTONOME" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 * &amp;lt;element name="MODE_PAIEMENT" type="{http://www.almerys.com/NormeV3}codeModePaiement"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NNI"
 * type="{http://www.almerys.com/NormeV3}string-1-13" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NNI_RATT" type="{http://www.almerys.com/NormeV3}string-1-13" maxOccurs="2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="REGIME_SPECIAL"
 * type="{http://www.almerys.com/NormeV3}codeRegimeSpecial" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="VIP" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_CERTIFICATION_NNI" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_CERTIFICATION_NNI_RATT"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_DEBUT_SUSPENSION" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_FIN_SUSPENSION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_RADIATION"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="MOTIF_RADIATION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoMembreContrat",
    propOrder = {
      "souscripteur",
      "position",
      "typeregime",
      "dateentree",
      "dateinscription",
      "individu",
      "adressemembre",
      "mediaenvoi",
      "joignabilites",
      "numadhesions",
      "autonome",
      "modepaiement",
      "nni",
      "nniratts",
      "regimespecial",
      "vip",
      "datecertificationnni",
      "datecertificationnniratt",
      "datedebutsuspension",
      "datefinsuspension",
      "dateradiation",
      "motifradiation"
    })
public class InfoMembreContrat implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "SOUSCRIPTEUR", required = true, type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean souscripteur;

  @XmlElement(name = "POSITION", required = true)
  protected String position;

  @XmlElement(name = "TYPE_REGIME", required = true)
  @XmlSchemaType(name = "string")
  protected CodeRegime typeregime;

  @XmlElement(name = "DATE_ENTREE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dateentree;

  @XmlElement(name = "DATE_INSCRIPTION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dateinscription;

  @XmlElement(name = "INDIVIDU", required = true)
  protected InfoIndividuOs individu;

  @XmlElement(name = "ADRESSE_MEMBRE")
  protected InfoAdresseParticulier adressemembre;

  @XmlElement(name = "MEDIA_ENVOI")
  protected InfoMediaEnvoi mediaenvoi;

  @XmlElement(name = "JOIGNABILITE")
  protected List<InfoJoignabilite> joignabilites;

  @XmlElement(name = "NUM_ADHESION")
  protected List<String> numadhesions;

  @XmlElement(name = "AUTONOME", required = true, type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean autonome;

  @XmlElement(name = "MODE_PAIEMENT")
  @XmlSchemaType(name = "string")
  protected CodeModePaiement modepaiement;

  @XmlElement(name = "NNI")
  protected String nni;

  @XmlElement(name = "NNI_RATT")
  protected List<String> nniratts;

  @XmlElement(name = "REGIME_SPECIAL")
  @XmlSchemaType(name = "string")
  protected CodeRegimeSpecial regimespecial;

  @XmlElement(name = "VIP", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean vip;

  @XmlElement(name = "DATE_CERTIFICATION_NNI")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datecertificationnni;

  @XmlElement(name = "DATE_CERTIFICATION_NNI_RATT")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datecertificationnniratt;

  @XmlElement(name = "DATE_DEBUT_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datedebutsuspension;

  @XmlElement(name = "DATE_FIN_SUSPENSION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datefinsuspension;

  @XmlElement(name = "DATE_RADIATION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dateradiation;

  @XmlElement(name = "MOTIF_RADIATION")
  protected String motifradiation;

  /**
   * Obtient la valeur de la propriété souscripteur.
   *
   * @return possible object is {@link String }
   */
  public Boolean isSOUSCRIPTEUR() {
    return souscripteur;
  }

  /**
   * Définit la valeur de la propriété souscripteur.
   *
   * @param value allowed object is {@link String }
   */
  public void setSOUSCRIPTEUR(Boolean value) {
    this.souscripteur = value;
  }

  /**
   * Obtient la valeur de la propriété position.
   *
   * @return possible object is {@link String }
   */
  public String getPOSITION() {
    return position;
  }

  /**
   * Définit la valeur de la propriété position.
   *
   * @param value allowed object is {@link String }
   */
  public void setPOSITION(String value) {
    this.position = value;
  }

  /**
   * Obtient la valeur de la propriété typeregime.
   *
   * @return possible object is {@link CodeRegime }
   */
  public CodeRegime getTYPEREGIME() {
    return typeregime;
  }

  /**
   * Définit la valeur de la propriété typeregime.
   *
   * @param value allowed object is {@link CodeRegime }
   */
  public void setTYPEREGIME(CodeRegime value) {
    this.typeregime = value;
  }

  /**
   * Obtient la valeur de la propriété dateentree.
   *
   * @return possible object is {@link String }
   */
  public String getDATEENTREE() {
    return dateentree;
  }

  /**
   * Définit la valeur de la propriété dateentree.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEENTREE(String value) {
    this.dateentree = value;
  }

  /**
   * Obtient la valeur de la propriété dateinscription.
   *
   * @return possible object is {@link String }
   */
  public String getDATEINSCRIPTION() {
    return dateinscription;
  }

  /**
   * Définit la valeur de la propriété dateinscription.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEINSCRIPTION(String value) {
    this.dateinscription = value;
  }

  /**
   * Obtient la valeur de la propriété individu.
   *
   * @return possible object is {@link InfoIndividuOs }
   */
  public InfoIndividuOs getINDIVIDU() {
    return individu;
  }

  /**
   * Définit la valeur de la propriété individu.
   *
   * @param value allowed object is {@link InfoIndividuOs }
   */
  public void setINDIVIDU(InfoIndividuOs value) {
    this.individu = value;
  }

  /**
   * Obtient la valeur de la propriété adressemembre.
   *
   * @return possible object is {@link InfoAdresseParticulier }
   */
  public InfoAdresseParticulier getADRESSEMEMBRE() {
    return adressemembre;
  }

  /**
   * Définit la valeur de la propriété adressemembre.
   *
   * @param value allowed object is {@link InfoAdresseParticulier }
   */
  public void setADRESSEMEMBRE(InfoAdresseParticulier value) {
    this.adressemembre = value;
  }

  /**
   * Obtient la valeur de la propriété mediaenvoi.
   *
   * @return possible object is {@link InfoMediaEnvoi }
   */
  public InfoMediaEnvoi getMEDIAENVOI() {
    return mediaenvoi;
  }

  /**
   * Définit la valeur de la propriété mediaenvoi.
   *
   * @param value allowed object is {@link InfoMediaEnvoi }
   */
  public void setMEDIAENVOI(InfoMediaEnvoi value) {
    this.mediaenvoi = value;
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
   * Gets the value of the numadhesions property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the numadhesions
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getNUMADHESIONS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getNUMADHESIONS() {
    if (numadhesions == null) {
      numadhesions = new ArrayList<String>();
    }
    return this.numadhesions;
  }

  /**
   * Obtient la valeur de la propriété autonome.
   *
   * @return possible object is {@link String }
   */
  public Boolean isAUTONOME() {
    return autonome;
  }

  /**
   * Définit la valeur de la propriété autonome.
   *
   * @param value allowed object is {@link String }
   */
  public void setAUTONOME(Boolean value) {
    this.autonome = value;
  }

  /**
   * Obtient la valeur de la propriété modepaiement.
   *
   * @return possible object is {@link CodeModePaiement }
   */
  public CodeModePaiement getMODEPAIEMENT() {
    return modepaiement;
  }

  /**
   * Définit la valeur de la propriété modepaiement.
   *
   * @param value allowed object is {@link CodeModePaiement }
   */
  public void setMODEPAIEMENT(CodeModePaiement value) {
    this.modepaiement = value;
  }

  /**
   * Obtient la valeur de la propriété nni.
   *
   * @return possible object is {@link String }
   */
  public String getNNI() {
    return nni;
  }

  /**
   * Définit la valeur de la propriété nni.
   *
   * @param value allowed object is {@link String }
   */
  public void setNNI(String value) {
    this.nni = value;
  }

  /**
   * Gets the value of the nniratts property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the nniratts
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getNNIRATTS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getNNIRATTS() {
    if (nniratts == null) {
      nniratts = new ArrayList<String>();
    }
    return this.nniratts;
  }

  /**
   * Obtient la valeur de la propriété regimespecial.
   *
   * @return possible object is {@link CodeRegimeSpecial }
   */
  public CodeRegimeSpecial getREGIMESPECIAL() {
    return regimespecial;
  }

  /**
   * Définit la valeur de la propriété regimespecial.
   *
   * @param value allowed object is {@link CodeRegimeSpecial }
   */
  public void setREGIMESPECIAL(CodeRegimeSpecial value) {
    this.regimespecial = value;
  }

  /**
   * Obtient la valeur de la propriété vip.
   *
   * @return possible object is {@link String }
   */
  public Boolean isVIP() {
    return vip;
  }

  /**
   * Définit la valeur de la propriété vip.
   *
   * @param value allowed object is {@link String }
   */
  public void setVIP(Boolean value) {
    this.vip = value;
  }

  /**
   * Obtient la valeur de la propriété datecertificationnni.
   *
   * @return possible object is {@link String }
   */
  public String getDATECERTIFICATIONNNI() {
    return datecertificationnni;
  }

  /**
   * Définit la valeur de la propriété datecertificationnni.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATECERTIFICATIONNNI(String value) {
    this.datecertificationnni = value;
  }

  /**
   * Obtient la valeur de la propriété datecertificationnniratt.
   *
   * @return possible object is {@link String }
   */
  public String getDATECERTIFICATIONNNIRATT() {
    return datecertificationnniratt;
  }

  /**
   * Définit la valeur de la propriété datecertificationnniratt.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATECERTIFICATIONNNIRATT(String value) {
    this.datecertificationnniratt = value;
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
   * Obtient la valeur de la propriété dateradiation.
   *
   * @return possible object is {@link String }
   */
  public String getDATERADIATION() {
    return dateradiation;
  }

  /**
   * Définit la valeur de la propriété dateradiation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATERADIATION(String value) {
    this.dateradiation = value;
  }

  /**
   * Obtient la valeur de la propriété motifradiation.
   *
   * @return possible object is {@link String }
   */
  public String getMOTIFRADIATION() {
    return motifradiation;
  }

  /**
   * Définit la valeur de la propriété motifradiation.
   *
   * @param value allowed object is {@link String }
   */
  public void setMOTIFRADIATION(String value) {
    this.motifradiation = value;
  }
}
