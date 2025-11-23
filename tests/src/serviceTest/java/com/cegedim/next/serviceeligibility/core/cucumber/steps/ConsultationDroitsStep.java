package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.xmlunit.matchers.CompareMatcher;

@NoArgsConstructor
public class ConsultationDroitsStep extends SoapStep {

  GetInfoBddResponse response;

  @When("I send a soap version {string} to the endpoint {string} request from file {string}")
  public void callSoapEndpointFromFile(String version, String endpoint, String fileName) {
    try {
      response = getResponse(endpoint, fileName);
    } catch (JAXBException | XMLStreamException | FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    Assertions.assertNotNull(response);
  }

  @Then("the expected soap response is identical to {string} content")
  public void checkSoapResponse(String fileName) {
    String expectedXML = null;
    try {
      expectedXML = FileUtils.readXmlFile(fileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Assertions.assertEquals(expectedXML, CompareMatcher.isIdenticalTo(response));
  }
}
