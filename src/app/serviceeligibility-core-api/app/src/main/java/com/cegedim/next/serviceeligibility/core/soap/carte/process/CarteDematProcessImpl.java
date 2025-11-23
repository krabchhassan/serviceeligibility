package com.cegedim.next.serviceeligibility.core.soap.carte.process;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeCarteDemat;
import com.cegedim.next.serviceeligibility.core.business.carte.service.CarteDematService;
import com.cegedim.next.serviceeligibility.core.soap.carte.mapper.MapperCarteDematToWebService;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionCarteDematDateIncorrecte;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionCarteDematNonTrouvee;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreIncorrect;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeRequest;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeResponse;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Processus de consultation des droits. */
@Component
public class CarteDematProcessImpl implements CarteDematProcess {

  private static final Logger LOG = LoggerFactory.getLogger(CarteDematProcessImpl.class);

  private static final String DATE_FORMAT = "yyyy/MM/dd";

  @Autowired private CarteDematService carteDematService;

  @Autowired private MapperCarteDematToWebService mapperCarteDematToWebService;

  /**
   * Execute une recherche de carte sur de la base de droit
   *
   * @param request request envoyé à la base de droit
   * @param response reponse renvoyé par la base de droit
   */
  @Override
  public void execute(CarteDematerialiseeRequest request, CarteDematerialiseeResponse response) {

    List<CarteDematDto> cartesList;

    try {

      DemandeCarteDemat demande = checkRequest(request);
      cartesList = carteDematService.getCartesDemat(demande, false);

      if (cartesList.isEmpty()) {
        throw new ExceptionCarteDematNonTrouvee();
      }
      for (CarteDematDto carteDematDto : cartesList) {
        mapperCarteDematToWebService.mapCarteDematResponse(response, carteDematDto);
      }

      response.setCodeResponse(mapperCarteDematToWebService.createCodeReponseOK());
    } catch (ExceptionConsultationParametreIncorrect e) {
      LOG.debug("Paramètre recherche incorrect", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
    } catch (ExceptionCarteDematDateIncorrecte e) {
      LOG.debug("Date référence incorrecte", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS_DATE_REFERENCE);
    } catch (ExceptionCarteDematNonTrouvee e) {
      LOG.debug("Aucune carte valide trouvée", e);
      throw new ExceptionMetier(CodeReponse.CARTE_DEMAT_NON_TROUVEE, e);
    }
  }

  public void execute(CarteDematerialiseeRequest request, CarteDematerialiseeV2Response response) {

    List<CarteDematDto> cartesList;

    try {

      DemandeCarteDemat demande = checkRequest(request);
      cartesList = carteDematService.getCartesDemat(demande, true);

      if (cartesList.isEmpty()) {
        throw new ExceptionCarteDematNonTrouvee();
      }
      for (CarteDematDto carteDematDto : cartesList) {
        mapperCarteDematToWebService.mapCarteDematResponse(response, carteDematDto);
      }

      response.setCodeResponse(mapperCarteDematToWebService.createCodeReponseOK());
    } catch (ExceptionConsultationParametreIncorrect e) {
      LOG.debug("Paramètre recherche incorrect", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
    } catch (ExceptionCarteDematDateIncorrecte e) {
      LOG.debug("Date référence incorrecte", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS_DATE_REFERENCE);
    } catch (ExceptionCarteDematNonTrouvee e) {
      LOG.debug("Aucune carte valide trouvée", e);
      throw new ExceptionMetier(CodeReponse.CARTE_DEMAT_NON_TROUVEE, e);
    }
  }

  private DemandeCarteDemat checkRequest(CarteDematerialiseeRequest request) {

    LOG.debug(
        "Request : {} {} {}",
        request.getRequest().getNumeroAMC(),
        request.getRequest().getNumeroContrat(),
        request.getRequest().getDateReference());

    if (!request.getRequest().getDateReference().isValid()
        || StringUtils.isEmpty(request.getRequest().getNumeroAMC())
        || StringUtils.isEmpty(request.getRequest().getNumeroContrat())) {
      throw new ExceptionConsultationParametreIncorrect();
    }
    DemandeCarteDemat demande = new DemandeCarteDemat();
    demande.setNumeroAMC(request.getRequest().getNumeroAMC());
    demande.setNumeroContrat(request.getRequest().getNumeroContrat());
    demande.setDateReference(
        DateUtils.convertXmlGregorianToString(
            request.getRequest().getDateReference(), DATE_FORMAT));

    Date date = new Date();
    String dateJour = new SimpleDateFormat(DATE_FORMAT).format(date);
    if (demande.getDateReference().compareTo(dateJour) < 0) {
      throw new ExceptionCarteDematDateIncorrecte();
    }

    return demande;
  }
}
