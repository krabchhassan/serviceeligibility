package com.cegedim.next.serviceeligibility.core.utility.rest;

import com.cegedim.next.serviceeligibility.core.utility.error.BeneficiaryNotFoundException;
import com.cegedim.next.serviceeligibility.core.utility.error.InvalidParameterException;
import com.cegedim.next.serviceeligibility.core.utility.error.UnexpectedFileException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class RestConnectorUtils {

  public static final String FETCH_RESPONSE = "Bad request";
  public static final String EMPTY_RESPONSE = "empty response";

  // Private referential utils
  private RestConnectorUtils() {}

  public static RestTemplate connect() {
    return connect(null);
  }

  public static RestTemplate connect(final RestTemplate defaultClient) {
    RestTemplate client = defaultClient;
    if (client == null) {
      final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
          new HttpComponentsClientHttpRequestFactory(
              HttpClientBuilder.create().useSystemProperties().build());
      client = new RestTemplate(clientHttpRequestFactory);
      client
          .getMessageConverters()
          .addFirst(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    final DefaultUriBuilderFactory uriTemplateHandler = new DefaultUriBuilderFactory();
    uriTemplateHandler.setParsePath(true);
    client.setUriTemplateHandler(uriTemplateHandler);

    return client;
  }

  public static JSONObject fetchWithoutValidation(
      final String url, final MultiValueMap<String, String> urlVariables) {
    return innerFetch(url, urlVariables, false);
  }

  public static JSONObject fetch(
      final String url, final MultiValueMap<String, String> urlVariables) {
    return innerFetch(url, urlVariables, true);
  }

  private static JSONObject innerFetch(
      final String url, final MultiValueMap<String, String> urlVariables, final boolean validate) {
    final RestTemplate connection = RestConnectorUtils.connect();

    try {

      String uriBuilder =
          UriComponentsBuilder.fromUriString(url)
              .queryParams(urlVariables)
              .buildAndExpand(urlVariables)
              .toUriString();

      final ResponseEntity<String> result = connection.getForEntity(uriBuilder, String.class);

      final JSONObject parsedObject = new JSONObject(new JSONTokener(result.getBody()));
      if (validate) {
        validate(parsedObject);
      }

      return parsedObject;
    } catch (HttpClientErrorException e) {
      if (e.getRawStatusCode() == 404) {
        throw new BeneficiaryNotFoundException("Beneficiary not found", e);
      }
      throw e;
    }
    // When date string is incorrect we've got an unsigned exception
    catch (final Exception e) {
      throw new InvalidParameterException(FETCH_RESPONSE, e);
    }
  }

  public static ResponseEntity<byte[]> postForRawContent(
      final String url,
      final Map<String, String> urlVariables,
      final Object body,
      String authHeader) {

    final RestTemplate connection = RestConnectorUtils.connect();

    final Map<String, String> vars = new HashMap<>();

    if (urlVariables != null) {
      vars.putAll(urlVariables);
    }

    try {
      final HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.AUTHORIZATION, authHeader);
      final HttpEntity<Object> obj = new HttpEntity<>(body, headers);
      final ResponseEntity<byte[]> result = connection.postForEntity(url, obj, byte[].class, vars);

      if (result.getStatusCode() == HttpStatus.OK || result.getStatusCode() == HttpStatus.CREATED) {
        return result;
      }
    } catch (final Exception e) {
      throw new InvalidParameterException(FETCH_RESPONSE, e);
    }

    return new ResponseEntity<>(new byte[0], HttpStatus.NO_CONTENT);
  }

  private static void validate(final JSONObject parsedObject) {
    final JSONArray data = (JSONArray) parsedObject.get("data");
    if (data.isEmpty()) {
      throw new InvalidParameterException(EMPTY_RESPONSE);
    }
  }

  public static ResponseEntity<String> sendExcelFile(
      final String url, final InputStream is, String authHeader) {

    try {
      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      headers.add(HttpHeaders.AUTHORIZATION, authHeader);

      final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      final File excelFile = new File(System.currentTimeMillis() + "_temp.xlsx");
      final FileOutputStream fos = new FileOutputStream(excelFile);
      FileCopyUtils.copy(is, fos);

      body.add("uploadedFile", new FileSystemResource(excelFile.getAbsolutePath()));

      final HttpEntity<MultiValueMap<String, Object>> requestEntity =
          new HttpEntity<>(body, headers);

      final RestTemplate connection = RestConnectorUtils.connect();
      final ResponseEntity<String> responseEntity =
          connection.exchange(url, HttpMethod.POST, requestEntity, String.class);

      final boolean deleted = excelFile.delete();
      log.info(String.format("The file delete() result: %s", deleted));

      return responseEntity;
    } catch (final Exception e) {
      throw new UnexpectedFileException("Error in post excel file", e);
    }
  }
}
