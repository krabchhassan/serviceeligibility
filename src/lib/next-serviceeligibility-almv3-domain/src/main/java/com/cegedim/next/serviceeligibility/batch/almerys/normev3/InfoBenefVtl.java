//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v2.3.3
// Voir https://eclipse-ee4j.github.io/jaxb-ri
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source.
// Généré le : 2025.04.23 à 09:24:28 AM CEST
//

package com.cegedim.next.serviceeligibility.batch.almerys.normev3;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.adapter.BooleanAdapter;
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
 * &lt;p&gt;Classe Java pour infoBenefVtl complex type.
 *
 * <p>&lt;p&gt;Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <p>&lt;pre&gt; &amp;lt;complexType name="infoBenefVtl"&amp;gt; &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt; &amp;lt;element name="REF_INTERNE_OS"
 * type="{http://www.almerys.com/NormeV3}string-1-30"/&amp;gt; &amp;lt;element name="TYPE_BENEF"
 * type="{http://www.almerys.com/NormeV3}codeBenef"/&amp;gt; &amp;lt;element name="RO"
 * type="{http://www.almerys.com/NormeV3}infoROVtl" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="CONTRAT_RESPONSABLE" type="{http://www.w3.org/2001/XMLSchema}boolean"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="PRODUIT"
 * type="{http://www.almerys.com/NormeV3}infoProduit" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="STATUT_NOEMISATION" type="{http://www.almerys.com/NormeV3}infoNoemisation"
 * minOccurs="0"/&amp;gt; &amp;lt;element name="CODE_MOUVEMENT_CARTE"
 * type="{http://www.almerys.com/NormeV3}codeCarte" minOccurs="0"/&amp;gt; &amp;lt;element
 * name="NUMERO_CARTE" type="{http://www.almerys.com/NormeV3}string-1-14" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt; &amp;lt;/restriction&amp;gt; &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt; &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "infoBenefVtl",
    propOrder = {
      "refinterneos",
      "typebenef",
      "ro",
      "contratresponsable",
      "produits",
      "statutnoemisation",
      "codemouvementcarte",
      "numerocarte"
    })
public class InfoBenefVtl implements Serializable {

  private static final long serialVersionUID = -1L;

  @XmlElement(name = "REF_INTERNE_OS", required = true)
  protected String refinterneos;

  @XmlElement(name = "TYPE_BENEF", required = true)
  @XmlSchemaType(name = "string")
  protected CodeBenef typebenef;

  @XmlElement(name = "RO")
  protected InfoROVtl ro;

  @XmlElement(name = "CONTRAT_RESPONSABLE", type = String.class)
  @XmlJavaTypeAdapter(BooleanAdapter.class)
  @XmlSchemaType(name = "boolean")
  protected Boolean contratresponsable;

  @XmlElement(name = "PRODUIT")
  protected List<InfoProduit> produits;

  @XmlElement(name = "STATUT_NOEMISATION")
  protected InfoNoemisation statutnoemisation;

  @XmlElement(name = "CODE_MOUVEMENT_CARTE")
  @XmlSchemaType(name = "string")
  protected CodeCarte codemouvementcarte;

  @XmlElement(name = "NUMERO_CARTE")
  protected String numerocarte;

  /**
   * Obtient la valeur de la propriété refinterneos.
   *
   * @return possible object is {@link String }
   */
  public String getREFINTERNEOS() {
    return refinterneos;
  }

  /**
   * Définit la valeur de la propriété refinterneos.
   *
   * @param value allowed object is {@link String }
   */
  public void setREFINTERNEOS(String value) {
    this.refinterneos = value;
  }

  /**
   * Obtient la valeur de la propriété typebenef.
   *
   * @return possible object is {@link CodeBenef }
   */
  public CodeBenef getTYPEBENEF() {
    return typebenef;
  }

  /**
   * Définit la valeur de la propriété typebenef.
   *
   * @param value allowed object is {@link CodeBenef }
   */
  public void setTYPEBENEF(CodeBenef value) {
    this.typebenef = value;
  }

  /**
   * Obtient la valeur de la propriété ro.
   *
   * @return possible object is {@link InfoROVtl }
   */
  public InfoROVtl getRO() {
    return ro;
  }

  /**
   * Définit la valeur de la propriété ro.
   *
   * @param value allowed object is {@link InfoROVtl }
   */
  public void setRO(InfoROVtl value) {
    this.ro = value;
  }

  /**
   * Obtient la valeur de la propriété contratresponsable.
   *
   * @return possible object is {@link String }
   */
  public Boolean isCONTRATRESPONSABLE() {
    return contratresponsable;
  }

  /**
   * Définit la valeur de la propriété contratresponsable.
   *
   * @param value allowed object is {@link String }
   */
  public void setCONTRATRESPONSABLE(Boolean value) {
    this.contratresponsable = value;
  }

  /**
   * Gets the value of the produits property.
   *
   * <p>&lt;p&gt; This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present inside the JAXB
   * object. This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the produits
   * property.
   *
   * <p>&lt;p&gt; For example, to add a new item, do as follows: &lt;pre&gt;
   * getPRODUITS().add(newItem); &lt;/pre&gt;
   *
   * <p>&lt;p&gt; Objects of the following type(s) are allowed in the list {@link InfoProduit }
   */
  public List<InfoProduit> getPRODUITS() {
    if (produits == null) {
      produits = new ArrayList<InfoProduit>();
    }
    return this.produits;
  }

  /**
   * Obtient la valeur de la propriété statutnoemisation.
   *
   * @return possible object is {@link InfoNoemisation }
   */
  public InfoNoemisation getSTATUTNOEMISATION() {
    return statutnoemisation;
  }

  /**
   * Définit la valeur de la propriété statutnoemisation.
   *
   * @param value allowed object is {@link InfoNoemisation }
   */
  public void setSTATUTNOEMISATION(InfoNoemisation value) {
    this.statutnoemisation = value;
  }

  /**
   * Obtient la valeur de la propriété codemouvementcarte.
   *
   * @return possible object is {@link CodeCarte }
   */
  public CodeCarte getCODEMOUVEMENTCARTE() {
    return codemouvementcarte;
  }

  /**
   * Définit la valeur de la propriété codemouvementcarte.
   *
   * @param value allowed object is {@link CodeCarte }
   */
  public void setCODEMOUVEMENTCARTE(CodeCarte value) {
    this.codemouvementcarte = value;
  }

  /**
   * Obtient la valeur de la propriété numerocarte.
   *
   * @return possible object is {@link String }
   */
  public String getNUMEROCARTE() {
    return numerocarte;
  }

  /**
   * Définit la valeur de la propriété numerocarte.
   *
   * @param value allowed object is {@link String }
   */
  public void setNUMEROCARTE(String value) {
    this.numerocarte = value;
  }
}
