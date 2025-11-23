package com.cegedimassurance.ws.annulationaccreditation.wsdl;

import com.cegedimassurances.norme.annulation_accreditation.GetAnnulationAccreditationIn;
import com.cegedimassurances.norme.annulation_accreditation.GetAnnulationAccreditationOut;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.cegedimassurance.ws.annulationaccreditation.wsdl package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _AnnulePEC_QNAME =
      new QName("http://ws.cegedimassurance.com/annulationaccreditation/wsdl", "annulePEC");
  private static final QName _PECannulee_QNAME =
      new QName("http://ws.cegedimassurance.com/annulationaccreditation/wsdl", "PECannulee");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.cegedimassurance.ws.annulationaccreditation.wsdl
   */
  public ObjectFactory() {}

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link GetAnnulationAccreditationIn }{@code
   * >}}
   */
  @XmlElementDecl(
      namespace = "http://ws.cegedimassurance.com/annulationaccreditation/wsdl",
      name = "annulePEC")
  public JAXBElement<GetAnnulationAccreditationIn> createAnnulePEC(
      GetAnnulationAccreditationIn value) {
    return new JAXBElement<GetAnnulationAccreditationIn>(
        _AnnulePEC_QNAME, GetAnnulationAccreditationIn.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link GetAnnulationAccreditationOut }{@code
   * >}}
   */
  @XmlElementDecl(
      namespace = "http://ws.cegedimassurance.com/annulationaccreditation/wsdl",
      name = "PECannulee")
  public JAXBElement<GetAnnulationAccreditationOut> createPECannulee(
      GetAnnulationAccreditationOut value) {
    return new JAXBElement<GetAnnulationAccreditationOut>(
        _PECannulee_QNAME, GetAnnulationAccreditationOut.class, null, value);
  }
}
