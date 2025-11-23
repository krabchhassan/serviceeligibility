package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.Produit;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.InvalidParameterException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Primary
@Slf4j
public class PwService implements IPwService {

  public static final String ANOMALIE_REPONSE_PW = "Anomalie dans la réponse PW";
  public static final String PW_ERROR_MESSAGE_IS_NOT_MANAGING_TP_OFFLINE_CONTEXTS =
      " is not managing TPOffline contexts";

  public static final String PW_ERROR_MESSAGE_NO_COVER_OFFER =
      "Les contextes TP_offline et TP_online ne sont pas couverts par l’offre sur la période pour la société émettrice %s et le code produit %s";

  private static final String MESSAGE = "message";

  private final PwCachedService pwCachedService;

  private final ObjectMapper objectMapper;

  private final boolean pwCacheEnabled;

  public PwService(
      PwCachedService pwCachedService,
      ObjectMapper objectMapper,
      BeyondPropertiesService beyondPropertiesService) {
    this.pwCachedService = pwCachedService;
    this.objectMapper = objectMapper;
    pwCacheEnabled = beyondPropertiesService.getBooleanProperty(PW_CACHE_ENABLED).orElse(false);
  }

  @ContinueSpan(log = "getOfferStructure")
  public JSONArray getOfferStructure(
      String issuerCompany,
      String offerCode,
      String productCode,
      String startDate,
      String endDate,
      String context,
      String version) {
    JSONArray productWorkshopResponse = new JSONArray();

    try {
      if (pwCacheEnabled) {
        productWorkshopResponse =
            pwCachedService.getOfferStructure(
                issuerCompany, offerCode, productCode, startDate, endDate, context, version);
      } else {
        productWorkshopResponse =
            pwCachedService.getOfferStructureWithoutCache(
                issuerCompany, offerCode, productCode, startDate, endDate, context, version);
      }
    } catch (JSONException e) {
      log.error(e.getLocalizedMessage(), e);
    }

    return productWorkshopResponse;
  }

  @ContinueSpan(log = "getDroitsProductsWorkshop (4 params)")
  public List<DroitsTPOfflinePW> getDroitsProductsWorkshop(
      List<String> errorsToFill, Produit produit, String dateNow) throws PwException {
    return getDroitsProductsWorkshop(
        errorsToFill, produit.getCode(), produit.getCodeAmc(), dateNow, null);
  }

  @ContinueSpan(log = "getDroitsProductsWorkshop (6 params)")
  public List<DroitsTPOfflinePW> getDroitsProductsWorkshop(
      List<String> errorsToFill,
      String codeProduit,
      String codeAmc,
      String dateDebut,
      String dateFin)
      throws PwException {
    String error = "Appel PW en erreur";
    try {
      JSONArray productWorkshopResponse =
          pwCachedService.getDetailGarantieTpRightsFromPWV4(
              codeAmc, codeProduit, dateDebut, dateFin);

      if (productWorkshopResponse != null && !productWorkshopResponse.isEmpty()) {
        if (productWorkshopResponse.getJSONObject(0).isNull(Constants.PW_OFFER_CODE)) {
          error = checkErrors(codeAmc, codeProduit, productWorkshopResponse);
        } else {
          Gson gson =
              new GsonBuilder()
                  .registerTypeAdapter(
                      LocalDateTime.class,
                      (JsonDeserializer<LocalDateTime>)
                          (json, typeOfT, context) ->
                              LocalDateTime.parse(
                                  json.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                  .create();
          List<DroitsTPOfflinePW> dtpoList = new ArrayList<>();
          for (int i = 0; i < productWorkshopResponse.length(); i++) {
            // en deux étapes, car le toString sur le JSONObject modifie l'ordre de la sous
            // liste
            JSONObject item = productWorkshopResponse.getJSONObject(i);
            DroitsTPOfflinePW dtpo = gson.fromJson(item.toString(), DroitsTPOfflinePW.class);
            Type mapType = new TypeToken<LinkedTreeMap<String, DetailsByDomain>>() {}.getType();
            dtpo.setDetailsByDomain(gson.fromJson(item.get("detailsByDomain").toString(), mapType));
            dtpoList.add(dtpo);
          }
          return dtpoList;
        }
      }
    } catch (HttpClientErrorException e) {
      error = getErrorMessage(e, codeAmc, codeProduit);
    } catch (InvalidParameterException e) {
      error =
          String.format(
              "Erreur lors de l'appel à PW pour l'AMC %s, le produit %s, et les dates %s,%s",
              codeAmc, codeProduit, dateDebut, dateFin);
    }
    errorsToFill.add(error);
    return Collections.emptyList();
  }

  @ContinueSpan(log = "getDroitsOfflineAndOnlineProductsWorkshop")
  public List<DroitsTPOfflineAndOnlinePW> getDroitsOfflineAndOnlineProductsWorkshop(
      List<String> errorsToFill,
      String codeProduit,
      String codeAmc,
      String dateDebut,
      String dateFin)
      throws PwException {
    String error = "Appel PW en erreur";
    try {
      JSONArray productWorkshopResponse =
          pwCachedService.getDetailGarantieTpRightsFromPWV4(
              codeAmc, codeProduit, dateDebut, dateFin);

      if (productWorkshopResponse != null && !productWorkshopResponse.isEmpty()) {
        Object errors = productWorkshopResponse.getJSONObject(0).opt("errors");
        if (checkIfErrorsNotEmpty(errors)) {
          error = checkErrors(codeAmc, codeProduit, errors);
        } else {
          List<DroitsTPOfflineAndOnlinePW> droitsPWList = new ArrayList<>();
          for (int i = 0; i < productWorkshopResponse.length(); i++) {
            // en deux étapes, car le toString sur le JSONObject modifie l'ordre de la sous
            // liste
            droitsPWList.add(
                objectMapper.readValue(
                    productWorkshopResponse.getJSONObject(i).toString(),
                    DroitsTPOfflineAndOnlinePW.class));
          }
          return droitsPWList;
        }
      }
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      throw new PwException(
          TriggeredBeneficiaryAnomaly.create(Anomaly.PRODUCT_NOT_FOUND, codeAmc, codeProduit));

    } catch (HttpClientErrorException e) {
      error = getErrorMessage(e, codeAmc, codeProduit);
    } catch (InvalidParameterException e) {
      error =
          String.format(
              "Erreur lors de l'appel à PW pour l'AMC %s, le produit %s, et les dates %s,%s",
              codeAmc, codeProduit, dateDebut, dateFin);
    }
    errorsToFill.add(error);
    return Collections.emptyList();
  }

  private String getErrorMessage(HttpClientErrorException e, String codeAmc, String codeProduit)
      throws PwException {
    if (e.getMessage() != null) {
      try {
        JSONObject errors = new JSONObject(e.getResponseBodyAsString());
        return checkErrors(codeAmc, codeProduit, errors.getJSONArray("errors"));
      } catch (JSONException exception) {
        return ANOMALIE_REPONSE_PW;
      }
    }
    return ANOMALIE_REPONSE_PW;
  }

  private String checkErrors(String codeAmc, String codeProduit, Object errors) throws PwException {
    if (errors instanceof JSONArray array) {
      return checkErrors15(codeAmc, codeProduit, array);
    }

    if (errors instanceof JSONObject object) {
      return checkErrors14(codeAmc, codeProduit, object);
    }

    return null;
  }

  private String checkErrors15(String codeAmc, String codeProduit, JSONArray errors)
      throws PwException {
    String error;
    String codeError = errors.getJSONObject(0).optString("error_code");
    String msg = errors.getJSONObject(0).optString(MESSAGE);
    if (Constants.NEXT_ENGINE_CORE_00408_1.equals(codeError)) {
      throw new PwException(
          TriggeredBeneficiaryAnomaly.create(Anomaly.PRODUCT_NOT_FOUND, codeAmc, codeProduit));
    } else if (Constants.NEXT_ENGINE_CORE_00408_2.equals(codeError)) {
      error = msg;
    } else if (msg != null && msg.contains(Constants.NEXT_ENGINE_CORE_00430_3)) {
      error = String.format(PW_ERROR_MESSAGE_NO_COVER_OFFER, codeAmc, codeProduit);
    } else {
      error = errors.getJSONObject(0).optString(MESSAGE, ANOMALIE_REPONSE_PW);
    }
    return error;
  }

  private String checkErrors14(String codeAmc, String codeProduit, JSONObject errors)
      throws PwException {
    String codeError = errors.optString("error_code");
    String msg = errors.optString(MESSAGE);
    if (Constants.NEXT_ENGINE_CORE_00408_1.equals(codeError)) {
      throw new PwException(
          TriggeredBeneficiaryAnomaly.create(Anomaly.PRODUCT_NOT_FOUND, codeAmc, codeProduit));
    } else if (msg != null && msg.contains(Constants.NEXT_ENGINE_CORE_00430_3)) {
      return String.format(PW_ERROR_MESSAGE_NO_COVER_OFFER, codeAmc, codeProduit);
    } else {
      return msg;
    }
  }

  private boolean checkIfErrorsNotEmpty(Object errors) {
    if (errors instanceof JSONArray array) {
      return !array.isEmpty();
    }

    if (errors instanceof JSONObject object) {
      return !object.isEmpty();
    }

    return false;
  }
}
