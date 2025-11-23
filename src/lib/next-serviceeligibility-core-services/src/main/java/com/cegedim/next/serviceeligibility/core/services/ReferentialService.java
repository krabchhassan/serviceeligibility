package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.REFERENTIAL_API_URL;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.referential.Referential;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.InvalidParameterException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ReferentialGenericException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
public class ReferentialService {
  // controles_service_prestations
  private static final String REFERENTIAL_FIND_BY_SOMETHING = "/v1/referentialDataWithoutPaging";
  private final SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.YYYY_MM_DD);

  private final RestConnector restConnector;

  private final String referentialUrl;

  public ReferentialService(
      RestConnector restConnector, BeyondPropertiesService beyondPropertiesService) {
    this.restConnector = restConnector;
    this.referentialUrl =
        beyondPropertiesService
            .getProperty(REFERENTIAL_API_URL)
            .orElse("http://next-referential-core-api:8080");
  }

  @Cacheable(value = "referentialCache", key = "#code")
  @ContinueSpan(log = "getReferential")
  public List<Referential> getReferential(String code) {
    return getReferentialList(code);
  }

  /** Methode utilisee par la RDO pour avoir un cache de temps infini */
  @Cacheable(value = "referentialCacheRDO", key = "#code")
  @ContinueSpan(log = "getReferentialRDO")
  public List<Referential> getReferentialRDO(String code) {
    log.debug("Demande de referentiel {} - {}", code, LocalDateTime.now());
    return getReferentialList(code);
  }

  private List<Referential> getReferentialList(String code) {

    try {
      // Récupération du referential
      MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<>();
      urlVariables.put("code", List.of(code));
      urlVariables.put("referenceDate", List.of(formatter.format(new Date())));
      JSONObject referentialResponse = callReferential(urlVariables);
      JSONArray referentials = referentialResponse.getJSONArray("data");
      List<Referential> rs = new ArrayList<>();
      if (referentials != null && !referentials.isEmpty()) {
        for (int i = 0; i < referentials.length(); i++) {
          JSONObject responseReferential = referentials.getJSONObject(i).getJSONObject("value");
          Referential referential = new Referential();
          referential.setCode(responseReferential.getString("code"));
          List<String> values =
              new ArrayList<>(
                  List.of(
                      StringUtils.stripAll(
                          responseReferential.getString("authorizedValues").split(","))));
          referential.setAuthorizedValues(values);
          rs.add(referential);
        }
      }
      return rs;
    } catch (GenericNotFoundException e) {
      String message = "Impossible de trouver le referentiel";
      log.error(message, e);
      throw new ReferentialGenericException(message, e);
    } catch (InvalidParameterException e) {
      String message = "Paramètres d'appel incorrectes lors de l'appel à Referential";
      log.error(message, e);
      throw new ReferentialGenericException(message, e);
    } catch (Exception e) {
      String message = String.format("Impossible de trouver le referentiel : %s", e.getMessage());
      log.error(message, e);
      throw new ReferentialGenericException(message, e);
    }
  }

  private JSONObject callReferential(MultiValueMap<String, String> urlVariables) {
    JSONObject referentials;
    try {
      referentials =
          restConnector.fetchObject(referentialUrl + REFERENTIAL_FIND_BY_SOMETHING, urlVariables);
    } catch (GenericNotFoundException e) {
      String message = "Le service Referential ne répond pas";
      log.debug(message);
      throw new ReferentialGenericException(message);
    }
    return referentials;
  }
}
