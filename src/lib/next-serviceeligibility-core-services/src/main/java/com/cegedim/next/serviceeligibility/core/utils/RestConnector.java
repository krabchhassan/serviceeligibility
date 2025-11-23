package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.InvalidParameterException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class RestConnector {

  public static final String NO_DATA_FOUND = "No data found";
  public static final String SERVER_ERROR = "Server error";
  public static final String ACCESS_IS_DENIED = "Access is denied";
  private final Logger logger = LoggerFactory.getLogger(RestConnector.class);
  public static final String FETCH_RESPONSE = "Bad request";
  public static final String EMPTY_RESPONSE = "empty response";

  private final RestOperations apiKeyTokenRestTemplate;

  public RestConnector(RestOperations apiKeyTokenRestTemplate) {
    this.apiKeyTokenRestTemplate = apiKeyTokenRestTemplate;
  }

  @ContinueSpan(log = "fetchArray")
  public JSONArray fetchArray(String url, MultiValueMap<String, String> urlVariables) {
    return new JSONArray(innerFetchArray(url, urlVariables));
  }

  private String innerFetchArray(String url, MultiValueMap<String, String> urlVariables) {
    try {
      return getStringResponseEntity(url, urlVariables).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getRawStatusCode() == 404) {
        throw new GenericNotFoundException(NO_DATA_FOUND, e);
      } else if (e.getRawStatusCode() == 500
          || e.getRawStatusCode() == 503
          || e.getRawStatusCode() == 504) {
        throw new GenericNotFoundException(SERVER_ERROR, e);
      } else if (e.getRawStatusCode() == 403) {
        throw new GenericNotFoundException(ACCESS_IS_DENIED, e);
      }
      throw e;
    }
    // When date string is incorrect we've got an unsigned exception
    catch (Exception e) {
      logger.error("innerFetchArray" + e.getMessage(), e);
      throw new InvalidParameterException(FETCH_RESPONSE, e);
    }
  }

  @ContinueSpan(log = "fetchObject")
  public JSONObject fetchObject(String url, MultiValueMap<String, String> urlVariables) {
    return innerFetchObject(url, urlVariables);
  }

  private JSONObject innerFetchObject(String url, MultiValueMap<String, String> urlVariables) {

    try {
      return new JSONObject(getStringResponseEntity(url, urlVariables).getBody());
    } catch (HttpClientErrorException e) {
      if (e.getRawStatusCode() == 404) {
        throw new GenericNotFoundException(NO_DATA_FOUND, e);
      } else if (e.getRawStatusCode() == 500
          || e.getRawStatusCode() == 503
          || e.getRawStatusCode() == 504) {
        throw new GenericNotFoundException(SERVER_ERROR, e);
      }
      throw e;
    }
    // When date string is incorrect we've got an unsigned exception
    catch (final Exception e) {
      logger.error("innerFetchObject" + e.getMessage(), e);
      throw new InvalidParameterException(FETCH_RESPONSE, e);
    }
  }

  private ResponseEntity<String> getStringResponseEntity(
      String url, MultiValueMap<String, String> urlVariables) throws URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(url);
    for (Map.Entry<String, List<String>> entry : urlVariables.entrySet()) {
      uriBuilder.addParameter(entry.getKey(), entry.getValue().getFirst());
    }

    URI uri = uriBuilder.build();
    RequestEntity<String> entity = new RequestEntity<>(HttpMethod.GET, uri);
    return apiKeyTokenRestTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
  }

  @ContinueSpan(log = "putElasticIndex")
  public JSONObject putElasticIndex(
      JSONObject body,
      String url,
      MultiValueMap<String, String> urlVariables,
      HttpHeaders headers) {
    try {
      return innerPutElasticIndex(body, url, urlVariables, headers);
    } catch (HttpClientErrorException indexError) {
      // Void that error if it's because the index already exists
    }

    return null;
  }

  private JSONObject innerPutElasticIndex(
      JSONObject body,
      String url,
      MultiValueMap<String, String> urlVariables,
      HttpHeaders headers) {

    String uriBuilder =
        UriComponentsBuilder.fromUriString(url)
            .queryParams(urlVariables)
            .buildAndExpand(urlVariables)
            .toUriString();

    HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
    headers.setContentType(MediaType.APPLICATION_JSON);
    ResponseEntity<String> result =
        apiKeyTokenRestTemplate.exchange(uriBuilder, HttpMethod.PUT, entity, String.class);

    return new JSONObject(result.getBody());
  }

  @ContinueSpan(log = "putElasticAlias")
  public JSONObject putElasticAlias(String url, HttpHeaders headers) {
    try {
      return innerPutElasticAlias(url, headers);
    } catch (HttpClientErrorException aliasError) {
      // Void that error if it's because the alias already exists
    }

    return null;
  }

  private JSONObject innerPutElasticAlias(String url, HttpHeaders headers) {

    String uriBuilder = UriComponentsBuilder.fromUriString(url).toUriString();

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> result =
        apiKeyTokenRestTemplate.exchange(uriBuilder, HttpMethod.PUT, entity, String.class);

    return new JSONObject(result.getBody());
  }

  @ContinueSpan(log = "post")
  public void post(String url, MultiValueMap<String, String> urlVariables) {

    try {
      getResponseEntity(url, urlVariables);
    } catch (HttpClientErrorException e) {
      if (e.getRawStatusCode() == 404) {
        throw new GenericNotFoundException(NO_DATA_FOUND, e);
      } else if (e.getRawStatusCode() == 500
          || e.getRawStatusCode() == 503
          || e.getRawStatusCode() == 504) {
        throw new GenericNotFoundException(SERVER_ERROR, e);
      }
      throw e;
    }
    // When date string is incorrect we've got an unsigned exception
    catch (final Exception e) {
      throw new InvalidParameterException(FETCH_RESPONSE, e);
    }
  }

  private void getResponseEntity(String url, MultiValueMap<String, String> urlVariables)
      throws URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(url);
    for (Map.Entry<String, List<String>> entry : urlVariables.entrySet()) {
      uriBuilder.addParameter(entry.getKey(), entry.getValue().get(0));
    }
    URI uri = uriBuilder.build();
    RequestEntity<String> entity = new RequestEntity<>(HttpMethod.POST, uri);
    apiKeyTokenRestTemplate.exchange(uriBuilder.build(), HttpMethod.POST, entity, String.class);
  }
}
