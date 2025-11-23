package com.cegedim.next.serviceeligibility.core.soap.consultation.ws;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperBaseDeDroitToWebService;
import com.cegedim.next.serviceeligibility.core.soap.consultation.process.ConsultationBaseDeDroitProcess;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddRequest;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import com.cegedimassurances.norme.commun.TypeHeaderIn;
import jakarta.xml.bind.JAXBElement;
import java.util.UUID;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/** Implementation du web service de consultation des droits pour la base de droit. */
@Endpoint
public class ConsultDroitController {

  private static final String NS = "http://ws.cegedimassurance.com/basededroit/wsdl";
  private final I18NService i18NService;

  @Autowired private ConsultationBaseDeDroitProcess consultationBaseDeDroitProcess;

  @Autowired private MapperBaseDeDroitToWebService mapperBaseDeDroitToWebService;

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ConsultDroitController.class);

  public ConsultDroitController() {
    i18NService = new I18NService();
  }

  @PayloadRoot(namespace = NS, localPart = "getInfoBddRequest")
  @ResponsePayload
  @PreAuthorize(READ_PERMISSION)
  public JAXBElement<GetInfoBddResponse> getInfoBddSoap(
      @RequestPayload final JAXBElement<GetInfoBddRequest> requete2) {
    GetInfoBddRequest requete = requete2.getValue();
    GetInfoBddResponse response = getInfoBdd(requete);
    QName coreElement = new QName(NS, "getInfoBddResponse", "ns5");
    return new JAXBElement<>(coreElement, GetInfoBddResponse.class, response);
  }

  public GetInfoBddResponse getInfoBdd(final GetInfoBddRequest requete) {

    GetInfoBddResponse response = this.creerReponseVide(requete.getHeaderIn());
    TypeCodeReponse codeReponse = response.getCodeReponse();
    try {
      consultationBaseDeDroitProcess.execute(requete, response);
    } catch (ExceptionMetier e) {
      // Demande speciale du Test FSIQ de changer Libelle du code 6000 en
      // passant par le code 6004 - liste segments recherche vide
      LOGGER.debug("Traitement code réponse", e);
      if ("6004".equals(e.getCodeReponse().getCode())) {
        codeReponse.setCode("6000");
        codeReponse.setLibelle(i18NService.getMessage(e.getCodeReponse().getCode()));
      } else {
        codeReponse.setCode(e.getCodeReponse().getCode());
        codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode(), e.getParams()));
      }
    } catch (Exception e) {
      codeReponse.setCode(CodeReponse.KO.getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode()));
      LOGGER.error("Erreur lors de la récupération des droits", e);
    }
    if (response.getHeaderOut() != null
        && (response.getHeaderOut().getHeaderTech().getIdSessionServeur() == null
            || response.getHeaderOut().getHeaderTech().getIdSessionServeur().isEmpty())) {
      response.getHeaderOut().getHeaderTech().setIdSessionServeur(UUID.randomUUID().toString());
    }
    return response;
  }

  /**
   * Initialise un objet reponse.
   *
   * @param headerIn header envoyé par le web service
   * @return l'objet reponse avec le header et le code reponse initialisé
   */
  private GetInfoBddResponse creerReponseVide(final TypeHeaderIn headerIn) {
    GetInfoBddResponse getInfoBddResponse = new GetInfoBddResponse();
    if (headerIn != null) {
      getInfoBddResponse.setHeaderOut(mapperBaseDeDroitToWebService.mapHeaderOut(headerIn));
    }
    getInfoBddResponse.setCodeReponse(mapperBaseDeDroitToWebService.createCodeReponseOK());
    return getInfoBddResponse;
  }
}
