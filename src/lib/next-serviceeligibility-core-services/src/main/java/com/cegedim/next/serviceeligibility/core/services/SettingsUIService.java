package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.SETTINGS_API_URL;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.SE_WAITING_PERIODS;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class SettingsUIService {

  private final Logger logger = LoggerFactory.getLogger(SettingsUIService.class);

  private final RestConnector restConnector;

  private final String settingsUiUrl;

  private final String waitingPeriodsUrl;

  public SettingsUIService(
      RestConnector restConnector, BeyondPropertiesService beyondPropertiesService) {
    this.restConnector = restConnector;
    this.settingsUiUrl =
        beyondPropertiesService
            .getProperty(SETTINGS_API_URL)
            .orElse("http://next-beyond-configuration:8080");
    this.waitingPeriodsUrl =
        beyondPropertiesService.getProperty(SE_WAITING_PERIODS).orElse("/api/carences");
  }

  @ContinueSpan(log = "getWaitingPeriod")
  public JSONArray getWaitingPeriod(String issuerCompany, String offerCode, String productCode) {
    JSONArray productWorkshopResponse = new JSONArray();

    try {

      MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<>();
      urlVariables.put(Constants.SE_PARAM_ISSUER_COMPANY, List.of(issuerCompany));
      urlVariables.put(Constants.SE_OFFER_CODE, List.of(offerCode));
      urlVariables.put(Constants.SE_PARAM_PRODUCT_CODE, List.of(productCode));

      logger.debug(
          String.format(
              "Récupération des paramètres de carences pour la compagnie %s, le code offre %s et le code produit %s",
              issuerCompany, offerCode, productCode));

      productWorkshopResponse = callSettingsUIWaitingPeriod(restConnector, urlVariables);
    } catch (JSONException e) {
      logger.error(e.getLocalizedMessage(), e);
    }

    return productWorkshopResponse;
  }

  private JSONArray callSettingsUIWaitingPeriod(
      RestConnector restConnector, MultiValueMap<String, String> urlVariables) {
    JSONArray productWorkshopResponse;
    try {
      productWorkshopResponse =
          restConnector.fetchArray(settingsUiUrl + waitingPeriodsUrl, urlVariables);
    } catch (GenericNotFoundException e) {
      String message =
          String.format(
              "Le endpoint WaitingPeriod du service Settings-UI  ne répond pas : %s",
              e.getMessage());
      logger.debug(message);
      throw new GenericNotFoundException(message);
    }
    return productWorkshopResponse;
  }
}
