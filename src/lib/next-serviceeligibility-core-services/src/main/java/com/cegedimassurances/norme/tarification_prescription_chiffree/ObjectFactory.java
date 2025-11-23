package com.cegedimassurances.norme.tarification_prescription_chiffree;

import jakarta.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.cegedimassurances.norme.tarification_prescription_chiffree package.
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
   * for package: com.cegedimassurances.norme.tarification_prescription_chiffree
   */
  public ObjectFactory() {}

  /** Create an instance of {@link TypeTarificationPrescription } */
  public TypeTarificationPrescription createTypeTarificationPrescription() {
    return new TypeTarificationPrescription();
  }

  /** Create an instance of {@link ReponseTarification } */
  public ReponseTarification createReponseTarification() {
    return new ReponseTarification();
  }

  /** Create an instance of {@link RequeteTarification } */
  public RequeteTarification createRequeteTarification() {
    return new RequeteTarification();
  }
}
