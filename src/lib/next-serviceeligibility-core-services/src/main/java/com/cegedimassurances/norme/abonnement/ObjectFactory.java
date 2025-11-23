package com.cegedimassurances.norme.abonnement;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.cegedimassurances.norme.abonnement package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _GetAbonnementIn_QNAME =
      new QName("http://norme.cegedimassurances.com/abonnement/", "getAbonnementIn");
  private static final QName _GetAbonnementOut_QNAME =
      new QName("http://norme.cegedimassurances.com/abonnement/", "getAbonnementOut");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.cegedimassurances.norme.abonnement
   */
  public ObjectFactory() {}

  /** Create an instance of {@link TypeInfoAbonnement } */
  public TypeInfoAbonnement createTypeInfoAbonnement() {
    return new TypeInfoAbonnement();
  }

  /** Create an instance of {@link TypeRequeteAbonnement } */
  public TypeRequeteAbonnement createTypeRequeteAbonnement() {
    return new TypeRequeteAbonnement();
  }

  /** Create an instance of {@link GetAbonnementIn } */
  public GetAbonnementIn createGetAbonnementIn() {
    return new GetAbonnementIn();
  }

  /** Create an instance of {@link TypeService } */
  public TypeService createTypeService() {
    return new TypeService();
  }

  /** Create an instance of {@link TypeServices } */
  public TypeServices createTypeServices() {
    return new TypeServices();
  }

  /** Create an instance of {@link TypeContactAMC } */
  public TypeContactAMC createTypeContactAMC() {
    return new TypeContactAMC();
  }

  /** Create an instance of {@link TypeReponseAbonnement } */
  public TypeReponseAbonnement createTypeReponseAbonnement() {
    return new TypeReponseAbonnement();
  }

  /** Create an instance of {@link GetAbonnementOut } */
  public GetAbonnementOut createGetAbonnementOut() {
    return new GetAbonnementOut();
  }

  /** Create an instance of {@link JAXBElement }{@code <}{@link GetAbonnementIn }{@code >}} */
  @XmlElementDecl(
      namespace = "http://norme.cegedimassurances.com/abonnement/",
      name = "getAbonnementIn")
  public JAXBElement<GetAbonnementIn> createGetAbonnementIn(GetAbonnementIn value) {
    return new JAXBElement<GetAbonnementIn>(
        _GetAbonnementIn_QNAME, GetAbonnementIn.class, null, value);
  }

  /** Create an instance of {@link JAXBElement }{@code <}{@link GetAbonnementOut }{@code >}} */
  @XmlElementDecl(
      namespace = "http://norme.cegedimassurances.com/abonnement/",
      name = "getAbonnementOut")
  public JAXBElement<GetAbonnementOut> createGetAbonnementOut(GetAbonnementOut value) {
    return new JAXBElement<GetAbonnementOut>(
        _GetAbonnementOut_QNAME, GetAbonnementOut.class, null, value);
  }
}
