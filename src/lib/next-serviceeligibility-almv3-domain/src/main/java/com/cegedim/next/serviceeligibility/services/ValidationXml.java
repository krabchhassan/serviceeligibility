package com.cegedim.next.serviceeligibility.services;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class ValidationXml {

  private Validator initValidator() throws SAXException {
    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    Source schemaFile = new StreamSource(getFile());
    Schema schema = factory.newSchema(schemaFile);
    return schema.newValidator();
  }

  private File getFile() {
    return new File(
        Objects.requireNonNull(
                getClass().getClassLoader().getResource("xsd/NormeIntegrationMedline.xsd"))
            .getFile());
  }

  public boolean isValid(String xmlPath) throws IOException, SAXException {
    Validator validator = initValidator();
    try {
      validator.validate(new StreamSource(xmlPath));
      return true;
    } catch (SAXException e) {
      return false;
    }
  }
}
