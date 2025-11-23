package com.cegedimassurances.norme.cartedemat.beans;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.cegedimassurances.norme.cartedemat.beans package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _HeaderOut_QNAME =
      new QName("http://norme.cegedimassurances.com/carteDemat/beans", "header_out");
  private static final QName _HeaderIn_QNAME =
      new QName("http://norme.cegedimassurances.com/carteDemat/beans", "header_in");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.cegedimassurances.norme.cartedemat.beans
   */
  public ObjectFactory() {}

  /** Create an instance of {@link TypeHeaderOut } */
  public TypeHeaderOut createTypeHeaderOut() {
    return new TypeHeaderOut();
  }

  /** Create an instance of {@link CarteDematerialiseeV2Response } */
  public CarteDematerialiseeV2Response createCarteDematerialiseeV2Response() {
    return new CarteDematerialiseeV2Response();
  }

  /** Create an instance of {@link CodeReponse } */
  public CodeReponse createCodeReponse() {
    return new CodeReponse();
  }

  /** Create an instance of {@link CartesV2 } */
  public CartesV2 createCartesV2() {
    return new CartesV2();
  }

  /** Create an instance of {@link Commentaires } */
  public Commentaires createCommentaires() {
    return new Commentaires();
  }

  /** Create an instance of {@link TypeHeaderIn } */
  public TypeHeaderIn createTypeHeaderIn() {
    return new TypeHeaderIn();
  }

  /** Create an instance of {@link CarteDematerialiseeV2Request } */
  public CarteDematerialiseeV2Request createCarteDematerialiseeV2Request() {
    return new CarteDematerialiseeV2Request();
  }

  /** Create an instance of {@link CarteDematerialiseeType } */
  public CarteDematerialiseeType createCarteDematerialiseeType() {
    return new CarteDematerialiseeType();
  }

  /** Create an instance of {@link TypeAdresseContrat } */
  public TypeAdresseContrat createTypeAdresseContrat() {
    return new TypeAdresseContrat();
  }

  /** Create an instance of {@link TypeConventionDomaine } */
  public TypeConventionDomaine createTypeConventionDomaine() {
    return new TypeConventionDomaine();
  }

  /** Create an instance of {@link TypeDomaineDroits } */
  public TypeDomaineDroits createTypeDomaineDroits() {
    return new TypeDomaineDroits();
  }

  /** Create an instance of {@link Conventions } */
  public Conventions createConventions() {
    return new Conventions();
  }

  /** Create an instance of {@link TypeContrat } */
  public TypeContrat createTypeContrat() {
    return new TypeContrat();
  }

  /** Create an instance of {@link TypeContratV2 } */
  public TypeContratV2 createTypeContratV2() {
    return new TypeContratV2();
  }

  /** Create an instance of {@link TypeParametreFormule } */
  public TypeParametreFormule createTypeParametreFormule() {
    return new TypeParametreFormule();
  }

  /** Create an instance of {@link TypePrestation } */
  public TypePrestation createTypePrestation() {
    return new TypePrestation();
  }

  /** Create an instance of {@link ParametresFormule } */
  public ParametresFormule createParametresFormule() {
    return new ParametresFormule();
  }

  /** Create an instance of {@link TypeBeneficiaireCouverture } */
  public TypeBeneficiaireCouverture createTypeBeneficiaireCouverture() {
    return new TypeBeneficiaireCouverture();
  }

  /** Create an instance of {@link Prestations } */
  public Prestations createPrestations() {
    return new Prestations();
  }

  /** Create an instance of {@link TypeBeneficiaire } */
  public TypeBeneficiaire createTypeBeneficiaire() {
    return new TypeBeneficiaire();
  }

  /** Create an instance of {@link Couvertures } */
  public Couvertures createCouvertures() {
    return new Couvertures();
  }

  /** Create an instance of {@link CarteDematerialiseeV2ResponseType } */
  public CarteDematerialiseeV2ResponseType createCarteDematerialiseeV2ResponseType() {
    return new CarteDematerialiseeV2ResponseType();
  }

  /** Create an instance of {@link Domaines } */
  public Domaines createDomaines() {
    return new Domaines();
  }

  /** Create an instance of {@link Beneficiaires } */
  public Beneficiaires createBeneficiaires() {
    return new Beneficiaires();
  }

  /** Create an instance of {@link TypeHeaderTechOut } */
  public TypeHeaderTechOut createTypeHeaderTechOut() {
    return new TypeHeaderTechOut();
  }

  /** Create an instance of {@link TypeHeaderFonctionnelOut } */
  public TypeHeaderFonctionnelOut createTypeHeaderFonctionnelOut() {
    return new TypeHeaderFonctionnelOut();
  }

  /** Create an instance of {@link TypeHeaderTechIn } */
  public TypeHeaderTechIn createTypeHeaderTechIn() {
    return new TypeHeaderTechIn();
  }

  /** Create an instance of {@link TypeHeaderFonctionnelIn } */
  public TypeHeaderFonctionnelIn createTypeHeaderFonctionnelIn() {
    return new TypeHeaderFonctionnelIn();
  }

  /** Create an instance of {@link JAXBElement }{@code <}{@link TypeHeaderOut }{@code >}} */
  @XmlElementDecl(
      namespace = "http://norme.cegedimassurances.com/carteDemat/beans",
      name = "header_out")
  public JAXBElement<TypeHeaderOut> createHeaderOut(TypeHeaderOut value) {
    return new JAXBElement<TypeHeaderOut>(_HeaderOut_QNAME, TypeHeaderOut.class, null, value);
  }

  /** Create an instance of {@link JAXBElement }{@code <}{@link TypeHeaderIn }{@code >}} */
  @XmlElementDecl(
      namespace = "http://norme.cegedimassurances.com/carteDemat/beans",
      name = "header_in")
  public JAXBElement<TypeHeaderIn> createHeaderIn(TypeHeaderIn value) {
    return new JAXBElement<TypeHeaderIn>(_HeaderIn_QNAME, TypeHeaderIn.class, null, value);
  }
}
