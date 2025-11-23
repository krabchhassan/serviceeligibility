package com.cegedim.next.serviceeligibility.core.soap.carte.ws;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.soap.carte.mapper.MapperCarteDematToWebService;
import com.cegedim.next.serviceeligibility.core.soap.carte.process.CarteDematProcess;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeRequest;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeResponse;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2Request;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2Response;
import com.cegedimassurances.norme.cartedemat.beans.CodeReponse;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderIn;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/** Implementation du web service de restitution pour la carte dematerialisee */
@Endpoint
public class CardController {

  private final I18NService i18NService;
  private static final String NS = "http://norme.cegedimassurances.com/carteDemat/beans";

  @Autowired private CarteDematProcess carteDematProcess;

  @Autowired private MapperCarteDematToWebService mapperCarteDematToWebService;

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

  public CardController() {
    i18NService = new I18NService();
  }

  @PayloadRoot(namespace = NS, localPart = "carteDematerialiseeRequest")
  @ResponsePayload
  @PreAuthorize(READ_PERMISSION)
  public CarteDematerialiseeResponse carteDematerialisee(
      @RequestPayload CarteDematerialiseeRequest request) {
    CarteDematerialiseeResponse carteDematerialiseeResponse =
        this.creerReponseVide(request.getHeaderIn());

    try {
      carteDematProcess.execute(request, carteDematerialiseeResponse);

    } catch (ExceptionMetier e) {
      CodeReponse codeReponse = carteDematerialiseeResponse.getCodeResponse();
      codeReponse.setCode(e.getCodeReponse().getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode(), e.getParams()));
      LOGGER.info("Erreur métier lors de la récupération de la carte dématérialisée");

    } catch (Exception e) {
      CodeReponse codeReponse = carteDematerialiseeResponse.getCodeResponse();
      codeReponse.setCode(
          com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse.KO
              .getCode());
      codeReponse.setLibelle(
          i18NService.getMessage(codeReponse.getCode(), new Object[] {e.getMessage()}));
      LOGGER.error("Erreur lors de la récupération de la carte dématérialisée", e);
    }
    return carteDematerialiseeResponse;
  }

  @PayloadRoot(namespace = NS, localPart = "carteDematerialiseeV2Request")
  @ResponsePayload
  @PreAuthorize(READ_PERMISSION)
  public CarteDematerialiseeV2Response carteDematerialiseeV2(
      @RequestPayload CarteDematerialiseeV2Request requestNew) {
    CarteDematerialiseeRequest request = new CarteDematerialiseeRequest();
    request.setHeaderIn(requestNew.getHeaderIn());
    request.setRequest(requestNew.getRequest());

    CarteDematerialiseeV2Response carteDematerialiseeResponse =
        this.creerReponseVideV2(request.getHeaderIn());

    try {
      carteDematProcess.execute(request, carteDematerialiseeResponse);

    } catch (ExceptionMetier e) {
      CodeReponse codeReponse = carteDematerialiseeResponse.getCodeResponse();
      codeReponse.setCode(e.getCodeReponse().getCode());
      codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode(), e.getParams()));
      LOGGER.info("Erreur métier lors de la récupération de la carte dématérialisée");

    } catch (Exception e) {
      CodeReponse codeReponse = carteDematerialiseeResponse.getCodeResponse();
      codeReponse.setCode(
          com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse.KO
              .getCode());
      codeReponse.setLibelle(
          i18NService.getMessage(codeReponse.getCode(), new Object[] {e.getMessage()}));
      LOGGER.error("Erreur lors de la récupération de la carte dématérialisée", e);
    }

    return carteDematerialiseeResponse;
  }

  /**
   * Initialise un objet reponse.
   *
   * @param headerIn header envoyé par le web service
   * @return l'objet reponse avec le header et le code reponse initialisé
   */
  private CarteDematerialiseeResponse creerReponseVide(final TypeHeaderIn headerIn) {
    CarteDematerialiseeResponse carteDematerialiseeResponse = new CarteDematerialiseeResponse();
    carteDematerialiseeResponse.setHeaderOut(mapperCarteDematToWebService.mapHeaderOut(headerIn));

    if (carteDematerialiseeResponse.getHeaderOut().getHeaderTechniqueOut().getIdSessionServeur()
            == null
        || carteDematerialiseeResponse
            .getHeaderOut()
            .getHeaderTechniqueOut()
            .getIdSessionServeur()
            .isEmpty()) {
      carteDematerialiseeResponse
          .getHeaderOut()
          .getHeaderTechniqueOut()
          .setIdSessionServeur(UUID.randomUUID().toString());
    }
    carteDematerialiseeResponse.setCodeResponse(mapperCarteDematToWebService.createCodeReponseOK());
    carteDematerialiseeResponse.setCommentaires(
        mapperCarteDematToWebService.createListeCommentairesVide());
    return carteDematerialiseeResponse;
  }

  private CarteDematerialiseeV2Response creerReponseVideV2(final TypeHeaderIn headerIn) {
    CarteDematerialiseeV2Response carteDematerialiseeResponse = new CarteDematerialiseeV2Response();
    carteDematerialiseeResponse.setHeaderOut(mapperCarteDematToWebService.mapHeaderOut(headerIn));

    if (carteDematerialiseeResponse.getHeaderOut().getHeaderTechniqueOut().getIdSessionServeur()
            == null
        || carteDematerialiseeResponse
            .getHeaderOut()
            .getHeaderTechniqueOut()
            .getIdSessionServeur()
            .isEmpty()) {
      carteDematerialiseeResponse
          .getHeaderOut()
          .getHeaderTechniqueOut()
          .setIdSessionServeur(UUID.randomUUID().toString());
    }
    carteDematerialiseeResponse.setCodeResponse(mapperCarteDematToWebService.createCodeReponseOK());
    carteDematerialiseeResponse.setCommentaires(
        mapperCarteDematToWebService.createListeCommentairesVide());
    return carteDematerialiseeResponse;
  }
}
