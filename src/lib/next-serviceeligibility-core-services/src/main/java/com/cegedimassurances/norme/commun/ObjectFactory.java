package com.cegedimassurances.norme.commun;

import jakarta.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.cegedimassurances.norme.commun package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.cegedimassurances.norme.commun
   */
  public ObjectFactory() {}

  /** Create an instance of {@link TypeHeaderTechIn } */
  public TypeHeaderTechIn createTypeHeaderTechIn() {
    return new TypeHeaderTechIn();
  }

  /** Create an instance of {@link TypeHeaderFonctionnelIn } */
  public TypeHeaderFonctionnelIn createTypeHeaderFonctionnelIn() {
    return new TypeHeaderFonctionnelIn();
  }

  /** Create an instance of {@link TypeHeaderIn } */
  public TypeHeaderIn createTypeHeaderIn() {
    return new TypeHeaderIn();
  }

  /** Create an instance of {@link TypeCodeReponse } */
  public TypeCodeReponse createTypeCodeReponse() {
    return new TypeCodeReponse();
  }

  /** Create an instance of {@link TypeHeaderTechOut } */
  public TypeHeaderTechOut createTypeHeaderTechOut() {
    return new TypeHeaderTechOut();
  }

  /** Create an instance of {@link TypeHeaderFonctionnelOut } */
  public TypeHeaderFonctionnelOut createTypeHeaderFonctionnelOut() {
    return new TypeHeaderFonctionnelOut();
  }

  /** Create an instance of {@link TypeHeaderOut } */
  public TypeHeaderOut createTypeHeaderOut() {
    return new TypeHeaderOut();
  }
}
