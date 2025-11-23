//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

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
 * &lt;p&gt;Classe Java pour infoContrat complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoContrat"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="NUM_CONTRAT"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element
 * name="REF_NUM_CONTRAT" type="{http://www.almerys.com/NormeV3}string-1-30" maxOccurs="2"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="REF_ADH_CONTRAT"
 * type="{http://www.almerys.com/NormeV3}string-1-20" maxOccurs="2" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ETAT_CONTRAT" type="{http://www.almerys.com/NormeV3}codeContrat"/&amp;gt;
 * &amp;lt;element name="DATE_SOUSCRIPTION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="DATE_IMMAT"
 * type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="DATE_RENOUVELLEMENT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_RESILIATION" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="MOTIF_RESILIATION"
 * type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="GRP_ASSURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATE_GRP_ASSURE" type="{http://www.w3.org/2001/XMLSchema}date"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="ADRESSE_CONTRAT"
 * type="{http://www.almerys.com/NormeV3}infoAdresseParticulier" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="MEMBRE_CONTRAT" type="{http://www.almerys.com/NormeV3}infoMembreContrat"
 * maxOccurs="unbounded"/&amp;gt; &amp;lt;element name="RATTACHEMENT" maxOccurs="unbounded"
 * minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_OS_RATTACHANT"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element
 * name="REF_OS_RATTACHE" type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt;
 * &amp;lt;element name="LIEN_JURIDIQUE"
 * type="{http://www.almerys.com/NormeV3}codeJuridique"/&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;element name="SERVICE" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt; &amp;lt;element
 * name="SERVICE_CV" type="{http://www.almerys.com/NormeV3}infoServiceCV" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_CD"
 * type="{http://www.almerys.com/NormeV3}infoServiceCD" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SERVICE_CR" type="{http://www.almerys.com/NormeV3}infoServiceCR"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_PREVOYANCE"
 * type="{http://www.almerys.com/NormeV3}infoServicePrev" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_TP_PEC"
 * type="{http://www.almerys.com/NormeV3}infoTP_PEC" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SERVICE_DECOMPTE" type="{http://www.almerys.com/NormeV3}InfoDecompte"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="CARTE_A_PUCE"
 * type="{http://www.almerys.com/NormeV3}InfoCartePuce" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CARTE_VITALE" type="{http://www.almerys.com/NormeV3}infoServiceCarteVitale"
 * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_RMS"
 * type="{http://www.almerys.com/NormeV3}infoServiceRMS" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_COFFRE"
 * type="{http://www.almerys.com/NormeV3}infoServiceCoffre" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_NOMADE"
 * type="{http://www.almerys.com/NormeV3}infoServiceNMD" maxOccurs="unbounded"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_TEAMLIVE"
 * type="{http://www.almerys.com/NormeV3}infoTeamLive" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="CARTE_RMS" maxOccurs="unbounded" minOccurs="0"&amp;gt;
 * &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;choice&amp;gt; &amp;lt;element
 * name="CARTE_INDIVIDUELLE" type="{http://www.w3.org/2001/XMLSchema}anyType"/&amp;gt;
 * &amp;lt;element name="CARTE_COLLECTIVE" type="{http://www.w3.org/2001/XMLSchema}anyType"/&amp;gt;
 * &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
 * &amp;lt;/element&amp;gt; &amp;lt;element name="REF_INTERNE_CG"
 * type="{http://www.almerys.com/NormeV3}string-1-15" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="REF_COURTIER" type="{http://www.almerys.com/NormeV3}string-1-15" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="REF_ENTREPRISE" type="{http://www.almerys.com/NormeV3}string-1-15"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="NUM_CONTRAT_COLLECTIF"
 * type="{http://www.almerys.com/NormeV3}string-1-30" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="REF_COLLEGE" type="{http://www.almerys.com/NormeV3}string-1-20" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="REF_SITE" type="{http://www.almerys.com/NormeV3}string-1-15"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="REF_GESTIONNAIRE"
 * type="{http://www.almerys.com/NormeV3}string-1-15" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoContrat",
    propOrder = {
      "numcontrat",
      "refnumcontrats",
      "refadhcontrats",
      "etatcontrat",
      "datesouscription",
      "dateimmat",
      "daterenouvellement",
      "dateresiliation",
      "motifresiliation",
      "grpassure",
      "dategrpassure",
      "adressecontrat",
      "membrecontrats",
      "rattachements",
      "service",
      "refinternecg",
      "refcourtier",
      "refentreprise",
      "numcontratcollectif",
      "refcollege",
      "refsite",
      "refgestionnaire"
    })
public class InfoContrat implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "NUM_CONTRAT", required = true)
  protected String numcontrat;

  @XmlElement(name = "REF_NUM_CONTRAT")
  protected List<String> refnumcontrats;

  @XmlElement(name = "REF_ADH_CONTRAT")
  protected List<String> refadhcontrats;

  @XmlElement(name = "ETAT_CONTRAT", required = true)
  @XmlSchemaType(name = "string")
  protected CodeContrat etatcontrat;

  @XmlElement(name = "DATE_SOUSCRIPTION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String datesouscription;

  @XmlElement(name = "DATE_IMMAT")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dateimmat;

  @XmlElement(name = "DATE_RENOUVELLEMENT")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String daterenouvellement;

  @XmlElement(name = "DATE_RESILIATION")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dateresiliation;

  @XmlElement(name = "MOTIF_RESILIATION")
  protected String motifresiliation;

  @XmlElement(name = "GRP_ASSURE")
  protected String grpassure;

  @XmlElement(name = "DATE_GRP_ASSURE")
  @XmlJavaTypeAdapter(DateAdapter.class)
  @XmlSchemaType(name = "date")
  protected String dategrpassure;

  @XmlElement(name = "ADRESSE_CONTRAT")
  protected InfoAdresseParticulier adressecontrat;

  @XmlElement(name = "MEMBRE_CONTRAT", required = true)
  protected List<InfoMembreContrat> membrecontrats;

  @XmlElement(name = "RATTACHEMENT")
  protected List<InfoContrat.RATTACHEMENT> rattachements;

  @XmlElement(name = "SERVICE")
  protected InfoContrat.SERVICE service;

  @XmlElement(name = "REF_INTERNE_CG")
  protected String refinternecg;

  @XmlElement(name = "REF_COURTIER")
  protected String refcourtier;

  @XmlElement(name = "REF_ENTREPRISE")
  protected String refentreprise;

  @XmlElement(name = "NUM_CONTRAT_COLLECTIF")
  protected String numcontratcollectif;

  @XmlElement(name = "REF_COLLEGE")
  protected String refcollege;

  @XmlElement(name = "REF_SITE")
  protected String refsite;

  @XmlElement(name = "REF_GESTIONNAIRE")
  protected String refgestionnaire;

  /**
   * Obtient la valeur de la propriété numcontrat.
   *
   * @return possible object is {@link String }
   */
  public String getNUMCONTRAT() {
    return numcontrat;
  }

  /**
   * Définit la valeur de la propriété numcontrat.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMCONTRAT(String value) {
    this.numcontrat = value;
  }

  /**
   * Gets the value of the refnumcontrats property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the refnumcontrats
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getREFNUMCONTRATS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getREFNUMCONTRATS() {
    if (refnumcontrats == null) {
      refnumcontrats = new ArrayList<String>();
    }
    return this.refnumcontrats;
  }

  /**
   * Gets the value of the refadhcontrats property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the refadhcontrats
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getREFADHCONTRATS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getREFADHCONTRATS() {
    if (refadhcontrats == null) {
      refadhcontrats = new ArrayList<String>();
    }
    return this.refadhcontrats;
  }

  /**
   * Obtient la valeur de la propriété etatcontrat.
   *
   * @return possible object is {@link CodeContrat }
   */
  public CodeContrat getETATCONTRAT() {
    return etatcontrat;
  }

  /**
   * Définit la valeur de la propriété etatcontrat.
   *
   * @param value allowed object is {@link CodeContrat }
   */
  public void setETATCONTRAT(CodeContrat value) {
    this.etatcontrat = value;
  }

  /**
   * Obtient la valeur de la propriété datesouscription.
   *
   * @return possible object is {@link String }
   */
  public String getDATESOUSCRIPTION() {
    return datesouscription;
  }

  /**
   * Définit la valeur de la propriété datesouscription.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATESOUSCRIPTION(String value) {
    this.datesouscription = value;
  }

  /**
   * Obtient la valeur de la propriété dateimmat.
   *
   * @return possible object is {@link String }
   */
  public String getDATEIMMAT() {
    return dateimmat;
  }

  /**
   * Définit la valeur de la propriété dateimmat.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEIMMAT(String value) {
    this.dateimmat = value;
  }

  /**
   * Obtient la valeur de la propriété daterenouvellement.
   *
   * @return possible object is {@link String }
   */
  public String getDATERENOUVELLEMENT() {
    return daterenouvellement;
  }

  /**
   * Définit la valeur de la propriété daterenouvellement.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATERENOUVELLEMENT(String value) {
    this.daterenouvellement = value;
  }

  /**
   * Obtient la valeur de la propriété dateresiliation.
   *
   * @return possible object is {@link String }
   */
  public String getDATERESILIATION() {
    return dateresiliation;
  }

  /**
   * Définit la valeur de la propriété dateresiliation.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATERESILIATION(String value) {
    this.dateresiliation = value;
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
   * Obtient la valeur de la propriété grpassure.
   *
   * @return possible object is {@link String }
   */
  public String getGRPASSURE() {
    return grpassure;
  }

  /**
   * Définit la valeur de la propriété grpassure.
   *
   * @param value allowed object is {@link String }
   */
  public void setGRPASSURE(String value) {
    this.grpassure = value;
  }

  /**
   * Obtient la valeur de la propriété dategrpassure.
   *
   * @return possible object is {@link String }
   */
  public String getDATEGRPASSURE() {
    return dategrpassure;
  }

  /**
   * Définit la valeur de la propriété dategrpassure.
   *
   * @param value allowed object is {@link String }
   */
  public void setDATEGRPASSURE(String value) {
    this.dategrpassure = value;
  }

  /**
   * Obtient la valeur de la propriété adressecontrat.
   *
   * @return possible object is {@link InfoAdresseParticulier }
   */
  public InfoAdresseParticulier getADRESSECONTRAT() {
    return adressecontrat;
  }

  /**
   * Définit la valeur de la propriété adressecontrat.
   *
   * @param value allowed object is {@link InfoAdresseParticulier }
   */
  public void setADRESSECONTRAT(InfoAdresseParticulier value) {
    this.adressecontrat = value;
  }

  /**
   * Gets the value of the membrecontrats property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the membrecontrats
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getMEMBRECONTRATS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoMembreContrat
   * }
   */
  public List<InfoMembreContrat> getMEMBRECONTRATS() {
    if (membrecontrats == null) {
      membrecontrats = new ArrayList<InfoMembreContrat>();
    }
    return this.membrecontrats;
  }

  /**
   * Gets the value of the rattachements property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the rattachements
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getRATTACHEMENTS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
   * InfoContrat.RATTACHEMENT }
   */
  public List<InfoContrat.RATTACHEMENT> getRATTACHEMENTS() {
    if (rattachements == null) {
      rattachements = new ArrayList<InfoContrat.RATTACHEMENT>();
    }
    return this.rattachements;
  }

  /**
   * Obtient la valeur de la propriété service.
   *
   * @return possible object is {@link InfoContrat.SERVICE }
   */
  public InfoContrat.SERVICE getSERVICE() {
    return service;
  }

  /**
   * Définit la valeur de la propriété service.
   *
   * @param value allowed object is {@link InfoContrat.SERVICE }
   */
  public void setSERVICE(InfoContrat.SERVICE value) {
    this.service = value;
  }

  /**
   * Obtient la valeur de la propriété refinternecg.
   *
   * @return possible object is {@link String }
   */
  public String getREFINTERNECG() {
    return refinternecg;
  }

  /**
   * Définit la valeur de la propriété refinternecg.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFINTERNECG(String value) {
    this.refinternecg = value;
  }

  /**
   * Obtient la valeur de la propriété refcourtier.
   *
   * @return possible object is {@link String }
   */
  public String getREFCOURTIER() {
    return refcourtier;
  }

  /**
   * Définit la valeur de la propriété refcourtier.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFCOURTIER(String value) {
    this.refcourtier = value;
  }

  /**
   * Obtient la valeur de la propriété refentreprise.
   *
   * @return possible object is {@link String }
   */
  public String getREFENTREPRISE() {
    return refentreprise;
  }

  /**
   * Définit la valeur de la propriété refentreprise.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFENTREPRISE(String value) {
    this.refentreprise = value;
  }

  /**
   * Obtient la valeur de la propriété numcontratcollectif.
   *
   * @return possible object is {@link String }
   */
  public String getNUMCONTRATCOLLECTIF() {
    return numcontratcollectif;
  }

  /**
   * Définit la valeur de la propriété numcontratcollectif.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMCONTRATCOLLECTIF(String value) {
    this.numcontratcollectif = value;
  }

  /**
   * Obtient la valeur de la propriété refcollege.
   *
   * @return possible object is {@link String }
   */
  public String getREFCOLLEGE() {
    return refcollege;
  }

  /**
   * Définit la valeur de la propriété refcollege.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFCOLLEGE(String value) {
    this.refcollege = value;
  }

  /**
   * Obtient la valeur de la propriété refsite.
   *
   * @return possible object is {@link String }
   */
  public String getREFSITE() {
    return refsite;
  }

  /**
   * Définit la valeur de la propriété refsite.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFSITE(String value) {
    this.refsite = value;
  }

  /**
   * Obtient la valeur de la propriété refgestionnaire.
   *
   * @return possible object is {@link String }
   */
  public String getREFGESTIONNAIRE() {
    return refgestionnaire;
  }

  /**
   * Définit la valeur de la propriété refgestionnaire.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFGESTIONNAIRE(String value) {
    this.refgestionnaire = value;
  }

  /**
   * &lt;p&gt;Classe Java pour anonymous complex type.
   *
   * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
   * classe.
   *
   * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
   * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;sequence&amp;gt;
   * &amp;lt;element name="REF_OS_RATTACHANT"
   * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element
   * name="REF_OS_RATTACHE" type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt;
   * &amp;lt;element name="LIEN_JURIDIQUE"
   * type="{http://www.almerys.com/NormeV3}codeJuridique"/&amp;gt; &amp;lt;/sequence&amp;gt;
   * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
   * &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {"refosrattachant", "refosrattache", "lienjuridique"})
  public static class RATTACHEMENT implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "REF_OS_RATTACHANT", required = true)
    protected String refosrattachant;

    @XmlElement(name = "REF_OS_RATTACHE", required = true)
    protected String refosrattache;

    @XmlElement(name = "LIEN_JURIDIQUE", required = true)
    @XmlSchemaType(name = "string")
    protected CodeJuridique lienjuridique;

    /**
     * Obtient la valeur de la propriété refosrattachant.
     *
     * @return possible object is {@link String }
     */
    public String getREFOSRATTACHANT() {
      return refosrattachant;
    }

    /**
     * Définit la valeur de la propriété refosrattachant.
     *
     * @param value allowed object is {@link String }
     */
    public void setREFOSRATTACHANT(String value) {
      this.refosrattachant = value;
    }

    /**
     * Obtient la valeur de la propriété refosrattache.
     *
     * @return possible object is {@link String }
     */
    public String getREFOSRATTACHE() {
      return refosrattache;
    }

    /**
     * Définit la valeur de la propriété refosrattache.
     *
     * @param value allowed object is {@link String }
     */
    public void setREFOSRATTACHE(String value) {
      this.refosrattache = value;
    }

    /**
     * Obtient la valeur de la propriété lienjuridique.
     *
     * @return possible object is {@link CodeJuridique }
     */
    public CodeJuridique getLIENJURIDIQUE() {
      return lienjuridique;
    }

    /**
     * Définit la valeur de la propriété lienjuridique.
     *
     * @param value allowed object is {@link CodeJuridique }
     */
    public void setLIENJURIDIQUE(CodeJuridique value) {
      this.lienjuridique = value;
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
   * &amp;lt;element name="SERVICE_CV" type="{http://www.almerys.com/NormeV3}infoServiceCV"
   * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_CD"
   * type="{http://www.almerys.com/NormeV3}infoServiceCD" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_CR"
   * type="{http://www.almerys.com/NormeV3}infoServiceCR" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_PREVOYANCE"
   * type="{http://www.almerys.com/NormeV3}infoServicePrev" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_TP_PEC"
   * type="{http://www.almerys.com/NormeV3}infoTP_PEC" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
   * &amp;lt;element name="SERVICE_DECOMPTE" type="{http://www.almerys.com/NormeV3}InfoDecompte"
   * maxOccurs="unbounded" minOccurs="0"/&amp;gt; &amp;lt;element name="CARTE_A_PUCE"
   * type="{http://www.almerys.com/NormeV3}InfoCartePuce" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="CARTE_VITALE"
   * type="{http://www.almerys.com/NormeV3}infoServiceCarteVitale" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_RMS"
   * type="{http://www.almerys.com/NormeV3}infoServiceRMS" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_COFFRE"
   * type="{http://www.almerys.com/NormeV3}infoServiceCoffre" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_NOMADE"
   * type="{http://www.almerys.com/NormeV3}infoServiceNMD" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="SERVICE_TEAMLIVE"
   * type="{http://www.almerys.com/NormeV3}infoTeamLive" maxOccurs="unbounded"
   * minOccurs="0"/&amp;gt; &amp;lt;element name="CARTE_RMS" maxOccurs="unbounded"
   * minOccurs="0"&amp;gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt;
   * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
   * &amp;lt;choice&amp;gt; &amp;lt;element name="CARTE_INDIVIDUELLE"
   * type="{http://www.w3.org/2001/XMLSchema}anyType"/&amp;gt; &amp;lt;element
   * name="CARTE_COLLECTIVE" type="{http://www.w3.org/2001/XMLSchema}anyType"/&amp;gt;
   * &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
   * &amp;lt;/complexType&amp;gt; &amp;lt;/element&amp;gt; &amp;lt;/sequence&amp;gt;
   * &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt; &amp;lt;/complexType&amp;gt;
   * &lt;/pre&gt;
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "",
      propOrder = {
        "servicecvs",
        "servicecds",
        "servicecrs",
        "serviceprevoyances",
        "servicetppecs",
        "servicedecomptes",
        "carteapuces",
        "cartevitales",
        "servicerms",
        "servicecoffres",
        "servicenomades",
        "serviceteamlives",
        "carterms"
      })
  public static class SERVICE implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "SERVICE_CV")
    protected List<InfoServiceCV> servicecvs;

    @XmlElement(name = "SERVICE_CD")
    protected List<InfoServiceCD> servicecds;

    @XmlElement(name = "SERVICE_CR")
    protected List<InfoServiceCR> servicecrs;

    @XmlElement(name = "SERVICE_PREVOYANCE")
    protected List<InfoServicePrev> serviceprevoyances;

    @XmlElement(name = "SERVICE_TP_PEC")
    protected List<InfoTPPEC> servicetppecs;

    @XmlElement(name = "SERVICE_DECOMPTE")
    protected List<InfoDecompte> servicedecomptes;

    @XmlElement(name = "CARTE_A_PUCE")
    protected List<InfoCartePuce> carteapuces;

    @XmlElement(name = "CARTE_VITALE")
    protected List<InfoServiceCarteVitale> cartevitales;

    @XmlElement(name = "SERVICE_RMS")
    protected List<InfoServiceRMS> servicerms;

    @XmlElement(name = "SERVICE_COFFRE")
    protected List<InfoServiceCoffre> servicecoffres;

    @XmlElement(name = "SERVICE_NOMADE")
    protected List<InfoServiceNMD> servicenomades;

    @XmlElement(name = "SERVICE_TEAMLIVE")
    protected List<InfoTeamLive> serviceteamlives;

    @XmlElement(name = "CARTE_RMS")
    protected List<InfoContrat.SERVICE.CARTERMS> carterms;

    /**
     * Gets the value of the servicecvs property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicecvs
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICECVS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoServiceCV }
     */
    public List<InfoServiceCV> getSERVICECVS() {
      if (servicecvs == null) {
        servicecvs = new ArrayList<InfoServiceCV>();
      }
      return this.servicecvs;
    }

    /**
     * Gets the value of the servicecds property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicecds
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICECDS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoServiceCD }
     */
    public List<InfoServiceCD> getSERVICECDS() {
      if (servicecds == null) {
        servicecds = new ArrayList<InfoServiceCD>();
      }
      return this.servicecds;
    }

    /**
     * Gets the value of the servicecrs property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicecrs
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICECRS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoServiceCR }
     */
    public List<InfoServiceCR> getSERVICECRS() {
      if (servicecrs == null) {
        servicecrs = new ArrayList<InfoServiceCR>();
      }
      return this.servicecrs;
    }

    /**
     * Gets the value of the serviceprevoyances property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
     * serviceprevoyances property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICEPREVOYANCES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoServicePrev
     * }
     */
    public List<InfoServicePrev> getSERVICEPREVOYANCES() {
      if (serviceprevoyances == null) {
        serviceprevoyances = new ArrayList<InfoServicePrev>();
      }
      return this.serviceprevoyances;
    }

    /**
     * Gets the value of the servicetppecs property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicetppecs
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICETPPECS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoTPPEC }
     */
    public List<InfoTPPEC> getSERVICETPPECS() {
      if (servicetppecs == null) {
        servicetppecs = new ArrayList<InfoTPPEC>();
      }
      return this.servicetppecs;
    }

    /**
     * Gets the value of the servicedecomptes property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
     * servicedecomptes property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICEDECOMPTES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoDecompte }
     */
    public List<InfoDecompte> getSERVICEDECOMPTES() {
      if (servicedecomptes == null) {
        servicedecomptes = new ArrayList<InfoDecompte>();
      }
      return this.servicedecomptes;
    }

    /**
     * Gets the value of the carteapuces property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the carteapuces
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getCARTEAPUCES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoCartePuce }
     */
    public List<InfoCartePuce> getCARTEAPUCES() {
      if (carteapuces == null) {
        carteapuces = new ArrayList<InfoCartePuce>();
      }
      return this.carteapuces;
    }

    /**
     * Gets the value of the cartevitales property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the cartevitales
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getCARTEVITALES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
     * InfoServiceCarteVitale }
     */
    public List<InfoServiceCarteVitale> getCARTEVITALES() {
      if (cartevitales == null) {
        cartevitales = new ArrayList<InfoServiceCarteVitale>();
      }
      return this.cartevitales;
    }

    /**
     * Gets the value of the servicerms property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicerms
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICERMS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoServiceRMS }
     */
    public List<InfoServiceRMS> getSERVICERMS() {
      if (servicerms == null) {
        servicerms = new ArrayList<InfoServiceRMS>();
      }
      return this.servicerms;
    }

    /**
     * Gets the value of the servicecoffres property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicecoffres
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICECOFFRES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
     * InfoServiceCoffre }
     */
    public List<InfoServiceCoffre> getSERVICECOFFRES() {
      if (servicecoffres == null) {
        servicecoffres = new ArrayList<InfoServiceCoffre>();
      }
      return this.servicecoffres;
    }

    /**
     * Gets the value of the servicenomades property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the servicenomades
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICENOMADES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoServiceNMD }
     */
    public List<InfoServiceNMD> getSERVICENOMADES() {
      if (servicenomades == null) {
        servicenomades = new ArrayList<InfoServiceNMD>();
      }
      return this.servicenomades;
    }

    /**
     * Gets the value of the serviceteamlives property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the
     * serviceteamlives property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getSERVICETEAMLIVES().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoTeamLive }
     */
    public List<InfoTeamLive> getSERVICETEAMLIVES() {
      if (serviceteamlives == null) {
        serviceteamlives = new ArrayList<InfoTeamLive>();
      }
      return this.serviceteamlives;
    }

    /**
     * Gets the value of the carterms property.
     *
     * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present inside the JAXB
     * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the carterms
     * property.
     *
     * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
     * getCARTERMS().add(newItem); &lt;/pre&gt;
     *
     * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link
     * InfoContrat.SERVICE.CARTERMS }
     */
    public List<InfoContrat.SERVICE.CARTERMS> getCARTERMS() {
      if (carterms == null) {
        carterms = new ArrayList<InfoContrat.SERVICE.CARTERMS>();
      }
      return this.carterms;
    }

    /**
     * &lt;p&gt;Classe Java pour anonymous complex type.
     *
     * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette
     * classe.
     *
     * <p>&lt;pre&gt; &amp;lt;complexType&amp;gt; &amp;lt;complexContent&amp;gt; &amp;lt;restriction
     * base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt; &amp;lt;choice&amp;gt;
     * &amp;lt;element name="CARTE_INDIVIDUELLE"
     * type="{http://www.w3.org/2001/XMLSchema}anyType"/&amp;gt; &amp;lt;element
     * name="CARTE_COLLECTIVE" type="{http://www.w3.org/2001/XMLSchema}anyType"/&amp;gt;
     * &amp;lt;/choice&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
        name = "",
        propOrder = {"cartecollective", "carteindividuelle"})
    public static class CARTERMS implements Serializable {

      private static final long serialVersionUID = -1L;

      @XmlElement(name = "CARTE_COLLECTIVE")
      protected Object cartecollective;

      @XmlElement(name = "CARTE_INDIVIDUELLE")
      protected Object carteindividuelle;

      /**
       * Obtient la valeur de la propriété cartecollective.
       *
       * @return possible object is {@link Object }
       */
      public Object getCARTECOLLECTIVE() {
        return cartecollective;
      }

      /**
       * Définit la valeur de la propriété cartecollective.
       *
       * @param value allowed object is {@link Object }
       */
      public void setCARTECOLLECTIVE(Object value) {
        this.cartecollective = value;
      }

      /**
       * Obtient la valeur de la propriété carteindividuelle.
       *
       * @return possible object is {@link Object }
       */
      public Object getCARTEINDIVIDUELLE() {
        return carteindividuelle;
      }

      /**
       * Définit la valeur de la propriété carteindividuelle.
       *
       * @param value allowed object is {@link Object }
       */
      public void setCARTEINDIVIDUELLE(Object value) {
        this.carteindividuelle = value;
      }
    }
  }
}
