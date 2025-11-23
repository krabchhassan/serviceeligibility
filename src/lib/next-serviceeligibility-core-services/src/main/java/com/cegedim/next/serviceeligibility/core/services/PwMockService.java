package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflineAndOnlinePW;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PwMockService implements IPwService {

  private final Logger logger = LoggerFactory.getLogger(PwMockService.class);

  @Autowired private ObjectMapper objectMapper;

  @ContinueSpan(log = "getDroitsOfflineAndOnlineProductsWorkshop Mock")
  public List<DroitsTPOfflineAndOnlinePW> getDroitsOfflineAndOnlineProductsWorkshop(
      List<String> errorsToFill,
      String codeProduit,
      String codeAmc,
      String dateDebut,
      String dateFin) {
    List<DroitsTPOfflineAndOnlinePW> droitsPWList = new ArrayList<>();
    try {
      JSONObject productWorkshopResponse2;
      URL resource = this.getClass().getClassLoader().getResource("ResponsePWMockLocal.json");
      Path path = Paths.get(resource.toURI());
      String content = new String(Files.readAllBytes(path));
      productWorkshopResponse2 = new JSONObject(content);
      JSONArray result = new JSONArray();
      String offerCode = productWorkshopResponse2.getString(Constants.PW_OFFER_CODE);
      for (int i = 0;
          i < productWorkshopResponse2.getJSONArray(Constants.VERSION_OFFRES).length();
          i++) {
        JSONObject offerVersion =
            productWorkshopResponse2.getJSONArray(Constants.VERSION_OFFRES).getJSONObject(i);
        offerVersion.put(Constants.PW_OFFER_CODE, offerCode);
        result.put(i, offerVersion);
      }

      // For each object in ProductWorkshop's response...

      for (int i = 0; i < result.length(); i++) {
        // en deux étapes car le toString sur le JSONObject modifie l'ordre de la sous
        // liste
        droitsPWList.add(
            objectMapper.readValue(
                result.getJSONObject(i).toString(), DroitsTPOfflineAndOnlinePW.class));
      }
    } catch (IOException e) {
      String message = "Problème d ouverture du fichier ResponsePWTpRigthsNaturePrestations.json";
      logger.error(e.getMessage(), e);
      throw new GenericNotFoundException(message);
    } catch (JSONException | GenericNotFoundException e) {
      String message = String.format("Le service Settings UI ne répond pas : %s", e.getMessage());
      logger.error(message, e);
      throw new GenericNotFoundException(message);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return droitsPWList;
  }

  @Override
  @ContinueSpan(log = "getOfferStructure Mock")
  public JSONArray getOfferStructure(
      String issuerCompany,
      String offerCode,
      String productCode,
      String startDate,
      String endDate,
      String context,
      String version) {
    try {
      URL resource;
      if ("FSC71232".equals(offerCode)) {
        resource =
            this.getClass().getClassLoader().getResource("ReponseOfferStructureMockLocal.json");
      } else {
        resource =
            this.getClass().getClassLoader().getResource("ReponseOfferStructure2MockLocal.json");
      }
      Path path = Paths.get(resource.toURI());
      String content = new String(Files.readAllBytes(path));
      return new JSONArray(content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
