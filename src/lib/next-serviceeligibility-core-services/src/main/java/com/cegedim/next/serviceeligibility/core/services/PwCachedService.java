package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// the reason to split this service and PwService is if we want to use cache, the call can't be
// internal because while using cache a new proxy class is created that uses caching
@Service
public class PwCachedService {

  private static final String SEPARATOR_CACHE_DATE = "/";

  private final Logger logger = LoggerFactory.getLogger(PwCachedService.class);

  public static final String NO_ANSWER_PRODUCTWORSHOP =
      "Le service ProductWorkshop ne répond pas : %s";

  private final RestConnector restConnector;

  private final CacheManager cacheManager;

  private final boolean pwCacheEnabled;

  private final String pwApiUrl;

  private final String endPointTpRightV4;

  private final String endPointOfferStructure;

  private final String endPointOfferStructureV5;

  public PwCachedService(
      RestConnector restConnector,
      CacheManager cacheManager,
      BeyondPropertiesService beyondPropertiesService) {
    this.restConnector = restConnector;
    this.cacheManager = cacheManager;
    pwCacheEnabled = beyondPropertiesService.getBooleanProperty(PW_CACHE_ENABLED).orElse(true);
    pwApiUrl =
        beyondPropertiesService.getProperty(PW_API_URL).orElse("http://next-engine-core-api:8080");

    endPointTpRightV4 =
        beyondPropertiesService.getProperty(PW_ENDPOINT_TPRIGHTS_V4).orElse("/v4/tpRights");
    endPointOfferStructure =
        beyondPropertiesService
            .getProperty(PW_ENDPOINT_OFFERSTRUCTURE)
            .orElse("/v4/offerStructure");
    endPointOfferStructureV5 =
        beyondPropertiesService
            .getProperty(PW_ENDPOINT_OFFERSTRUCTURE)
            .orElse("/v5/offerStructure");
  }

  @ContinueSpan(log = "getDetailGarantieTpRightsFromPWV4")
  @SuppressWarnings("unchecked")
  public JSONArray getDetailGarantieTpRightsFromPWV4(
      String codeOC, String codeProduct, String dateDebut, String dateFin) {
    logger.debug(
        "Récupération des garanties pour l'OC {}, le code produit {} de la date {} a la date {}",
        codeOC,
        codeProduct,
        dateDebut,
        dateFin);
    if (pwCacheEnabled) {
      Cache cache = cacheManager.getCache("pwCache");
      if (cache != null) {
        Cache.ValueWrapper valueWrapper = cache.get(codeOC + codeProduct);
        Map<String, JSONObject> dataInCache = new HashMap<>();
        if (valueWrapper != null && valueWrapper.get() != null) {
          dataInCache = (Map<String, JSONObject>) valueWrapper.get();
          // NOSONAR, c'est testé juste au dessus !
          JSONArray resultFromCache = getDataInCache(dateDebut, dateFin, dataInCache);
          if (!resultFromCache.isEmpty()) {
            return resultFromCache;
          }
        }
        return getObjectFromPW(codeOC, codeProduct, dateDebut, dateFin, cache, dataInCache);
      } else {
        // If no cache (TU)
        return getObjectFromPW(codeOC, codeProduct, dateDebut, dateFin, null, null);
      }
    } else {
      return getObjectFromPW(codeOC, codeProduct, dateDebut, dateFin, null, null);
    }
  }

  private JSONArray getObjectFromPW(
      String codeOC,
      String codeProduct,
      String dateDebut,
      String dateFin,
      Cache cache,
      Map<String, JSONObject> dataInCache) {
    MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<>();
    urlVariables.put(Constants.PW_PARAM_ISSUER_COMPANY, List.of(codeOC));
    urlVariables.put(Constants.PW_PARAM_PRODUCT_CODE, List.of(codeProduct));
    urlVariables.put(Constants.PW_PARAM_DATE, List.of(dateDebut));
    if (StringUtils.isNotBlank(dateFin)) {
      urlVariables.put(Constants.PW_PARAM_DATE_FIN, List.of(dateFin));
    }
    JSONArray result = new JSONArray();
    try {
      JSONObject productWorkshopResponse =
          restConnector.fetchObject(pwApiUrl + endPointTpRightV4, urlVariables);
      if (productWorkshopResponse == null) {
        return null;
      }
      if (productWorkshopResponse.isNull(Constants.PW_OFFER_CODE)) {
        return getErrorsArray(productWorkshopResponse);
      }

      String offerCode = productWorkshopResponse.getString(Constants.PW_OFFER_CODE);
      for (int i = 0;
          i < productWorkshopResponse.getJSONArray(Constants.VERSION_OFFRES).length();
          i++) {
        JSONObject offerVersion =
            productWorkshopResponse.getJSONArray(Constants.VERSION_OFFRES).getJSONObject(i);
        offerVersion.put(Constants.PW_OFFER_CODE, offerCode);
        result.put(i, offerVersion);
        if (dataInCache != null) {
          String key = offerVersion.getString(Constants.PW_VALIDITY_DATE);
          if (offerVersion.has(Constants.PW_END_VALIDITY_DATE)) {
            key += SEPARATOR_CACHE_DATE + offerVersion.getString(Constants.PW_END_VALIDITY_DATE);
          }
          dataInCache.put(key, offerVersion);
        }
      }
      if (dataInCache != null) {
        cache.put(codeOC + codeProduct, dataInCache);
      }
    } catch (GenericNotFoundException e) {
      String message = String.format(NO_ANSWER_PRODUCTWORSHOP, e.getMessage());
      logger.debug(message);
      throw new GenericNotFoundException(message);
    }
    return result;
  }

  JSONArray getDataInCache(
      String dateDebutInterrogation,
      String dateFinInterrogation,
      Map<String, JSONObject> dataInCache) {
    JSONArray matchingDataInCache = new JSONArray();
    LocalDate localDateDebutInterrogation = LocalDate.parse(dateDebutInterrogation);
    LocalDate localDateFinInterrogation = null;
    if (StringUtils.isNotBlank(dateFinInterrogation)) {
      localDateFinInterrogation = LocalDate.parse(dateFinInterrogation);
    }

    List<Map.Entry<String, JSONObject>> entrySet =
        dataInCache.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
    // récupère la première date de début d'interrogation.
    LocalDate dateControle = LocalDate.parse(dateDebutInterrogation);

    for (Map.Entry<String, JSONObject> entry : entrySet) {
      String[] keySplit = entry.getKey().split(SEPARATOR_CACHE_DATE);
      LocalDate localDateFinInCache = null;
      LocalDate localDateDebutInCache = LocalDate.parse(keySplit[0]);
      if (keySplit.length > 1) {
        localDateFinInCache = LocalDate.parse(keySplit[1]);
      }
      if (DateUtils.isOverlapping(
          localDateDebutInterrogation,
          localDateFinInterrogation,
          localDateDebutInCache,
          localDateFinInCache)) {
        matchingDataInCache.put(entry.getValue());
      }
      // si la date d'interro est après la date début, on remplace la date d'interro
      // par la date de fin de cache courant + 1
      if (dateControle != null && !dateControle.isBefore(localDateDebutInCache)) {
        dateControle = localDateFinInCache == null ? null : localDateFinInCache.plusDays(1);
      }
    }
    // si la date de fin d'interrogation est après ou égale à la date de contrôle,
    // le cache n'est pas couvert.
    if (dateControle != null
        && localDateFinInterrogation != null
        && !localDateFinInterrogation.isBefore(dateControle)) {
      matchingDataInCache.clear();
    }
    return matchingDataInCache;
  }

  @Cacheable(
      value = "offerStructureCache",
      key = "{#issuerCompany,#offerCode,#productCode,#context,#startDate,#endDate}")
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
      productWorkshopResponse =
          getJsonArrayFromOfferStructure(
              issuerCompany, offerCode, productCode, startDate, endDate, context, version);
    } catch (JSONException e) {
      logger.error(e.getLocalizedMessage(), e);
    }

    return productWorkshopResponse;
  }

  private JSONArray getJsonArrayFromOfferStructure(
      String issuerCompany,
      String offerCode,
      String productCode,
      String startDate,
      String endDate,
      String context,
      String version) {
    MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<>();
    urlVariables.put(Constants.PW_PARAM_ISSUER_COMPANY, List.of(issuerCompany));
    if (offerCode != null) {
      urlVariables.put(Constants.PW_OFFER_CODE, List.of(offerCode));
    }
    if (productCode != null) {
      urlVariables.put(Constants.PW_PRODUCT_CODE, List.of(productCode));
    }
    urlVariables.put(Constants.PW_PARAM_DATE, List.of(startDate));
    if (endDate != null) {
      urlVariables.put(Constants.PW_PARAM_DATE_END, List.of(endDate));
    }
    urlVariables.put(Constants.PW_CONTEXT, List.of(context));
    return callPWOfferStructure(restConnector, urlVariables, version);
  }

  @ContinueSpan(log = "getOfferStructureWithoutCache")
  public JSONArray getOfferStructureWithoutCache(
      String issuerCompany,
      String offerCode,
      String productCode,
      String startDate,
      String endDate,
      String context,
      String version) {
    JSONArray productWorkshopResponse = new JSONArray();
    try {
      productWorkshopResponse =
          getJsonArrayFromOfferStructure(
              issuerCompany, offerCode, productCode, startDate, endDate, context, version);
    } catch (JSONException e) {
      logger.error(e.getLocalizedMessage(), e);
    }

    return productWorkshopResponse;
  }

  protected JSONArray callPWOfferStructure(
      RestConnector restConnector, MultiValueMap<String, String> urlVariables, String version) {
    JSONArray productWorkshopResponse;
    try {
      productWorkshopResponse =
          restConnector.fetchArray(
              pwApiUrl + ("V5".equals(version) ? endPointOfferStructureV5 : endPointOfferStructure),
              urlVariables);
    } catch (GenericNotFoundException e) {
      String message = String.format(NO_ANSWER_PRODUCTWORSHOP, e.getMessage());
      logger.debug(message);
      throw new GenericNotFoundException(message);
    }
    return productWorkshopResponse;
  }

  private JSONArray getErrorsArray(JSONObject productWorkshopResponse) {
    Object object = productWorkshopResponse.opt("errors");
    if (object instanceof JSONArray array) {
      return array;
    }

    if (object instanceof JSONObject) {
      JSONArray errorsArray = new JSONArray();
      errorsArray.put(object);
      return errorsArray;
    }

    return null;
  }
}
