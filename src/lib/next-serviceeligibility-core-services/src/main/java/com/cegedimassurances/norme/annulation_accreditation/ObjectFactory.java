package com.cegedimassurances.norme.annulation_accreditation;

import jakarta.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.cegedimassurances.norme.annulation_accreditation package.
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
   * for package: com.cegedimassurances.norme.annulation_accreditation
   */
  public ObjectFactory() {}

  /** Create an instance of {@link TypeRequeteAnnulation } */
  public TypeRequeteAnnulation createTypeRequeteAnnulation() {
    return new TypeRequeteAnnulation();
  }

  /** Create an instance of {@link GetAnnulationAccreditationIn } */
  public GetAnnulationAccreditationIn createGetAnnulationAccreditationIn() {
    return new GetAnnulationAccreditationIn();
  }

  /** Create an instance of {@link TypeReponseAnnulation } */
  public TypeReponseAnnulation createTypeReponseAnnulation() {
    return new TypeReponseAnnulation();
  }

  /** Create an instance of {@link GetAnnulationAccreditationOut } */
  public GetAnnulationAccreditationOut createGetAnnulationAccreditationOut() {
    return new GetAnnulationAccreditationOut();
  }
}
