package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;
import jakarta.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import lombok.NoArgsConstructor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@NoArgsConstructor
public class SoapStep extends WebServiceGatewaySupport {

  protected static final String CORE_API = "serviceeligibility/core/api";

  public GetInfoBddResponse getResponse(String endpoint, String fileName)
      throws XMLStreamException, FileNotFoundException, JAXBException {

    GetInfoBddRequestDto request = FileUtils.readXmlRequestFile(fileName);

    //    GetInfoBddResponse response =
    //        (GetInfoBddResponse)
    //            getWebServiceTemplate()
    //                .marshalSendAndReceive(CORE_API + "/services/" + endpoint, request);

    String uri = CORE_API + "/services/" + endpoint;
    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setDefaultUri(uri);
    return (GetInfoBddResponse) webServiceTemplate.marshalSendAndReceive(request);
  }
}
