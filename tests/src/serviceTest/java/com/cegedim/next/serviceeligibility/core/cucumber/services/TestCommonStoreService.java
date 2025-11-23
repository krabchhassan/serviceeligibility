package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Service
@Slf4j
public class TestCommonStoreService {
  private final MongoTemplate template;
  @Getter private final RestTemplate restTemplate;
  private final DefaultUriBuilderFactory factory;

  public static final String CORE_API = "serviceeligibility/core/api";
  public static final String CONSUMER_API = "serviceeligibility/consumer/api";
  public static final String PRESTIJ_WORKER = "serviceeligibility/prestij/worker";
  public static ObjectMapper OBJECT_MAPPER = null;

  /** Common Store */
  @Setter @Getter private Optional<HttpClientErrorException> httpClientErrorException;

  private static final int ONE_SECOND = 1000;
  private static final double MULTIPLIER = 1.5;

  public TestCommonStoreService(
      MongoTemplate template,
      RestTemplate restTemplate,
      ConfigurableEnvironment environment,
      ObjectMapper objectMapper) {
    this.template = template;
    this.restTemplate = restTemplate;
    OBJECT_MAPPER = objectMapper;
    String baseUrl = environment.getRequiredProperty("env.baseUrl");
    this.factory = new DefaultUriBuilderFactory(baseUrl);
  }

  public boolean dropCollections() throws InterruptedException {
    long t0 = System.currentTimeMillis();
    while (!template.getCollectionNames().isEmpty()) {
      Set<String> collectionNames = template.getCollectionNames();
      log.info("collectionNames : {}", collectionNames.size());
      for (String collection : collectionNames) {
        template.dropCollection(collection);
      }
      TimeUnit.MILLISECONDS.sleep(200);
    }
    long t1 = System.currentTimeMillis();
    log.info("dropCollections took {}ms", (t1 - t0));
    template.createCollection("declarations");
    template.createCollection("cartesDemat");
    template.createCollection("cartesPapier");
    template.createCollection("declarationsConsolideesCarteDemat");
    template.createCollection("tracesConsolidation");
    template.createCollection("tracesExtractionConso");
    template.createCollection("tracesExtractionSansConso");
    template.createCollection("historiqueExecutions");
    template.createCollection("restitutionCarte");
    return true;
  }

  @Nullable
  @Retryable(
      retryFor = ResourceAccessException.class,
      backoff = @Backoff(delay = ONE_SECOND, multiplier = MULTIPLIER))
  public <T> ResponseEntity<T> postToGivenURLWithGivenBody(
      @NotNull String path,
      @NotNull Object body,
      @NotNull ParameterizedTypeReference<T> returnType) {
    return authorizedRequest(path, body, returnType, HttpMethod.POST);
  }

  @Nullable
  @Retryable(
      retryFor = ResourceAccessException.class,
      backoff = @Backoff(delay = ONE_SECOND, multiplier = MULTIPLIER))
  public <T> ResponseEntity<T> putToGivenURLWithGivenBody(
      @NotNull String path,
      @NotNull Object body,
      @NotNull ParameterizedTypeReference<T> returnType) {
    return authorizedRequest(path, body, returnType, HttpMethod.PUT);
  }

  @Nullable
  @Retryable(
      retryFor = ResourceAccessException.class,
      backoff = @Backoff(delay = ONE_SECOND, multiplier = MULTIPLIER))
  public <T> ResponseEntity<T> getToGivenURL(
      @NotNull String path,
      @NotNull Map<String, ?> params,
      @NotNull ParameterizedTypeReference<T> returnType) {
    return authorizedRequest(path, params, null, returnType, HttpMethod.GET);
  }

  @Nullable
  @Retryable(
      retryFor = ResourceAccessException.class,
      backoff = @Backoff(delay = ONE_SECOND, multiplier = MULTIPLIER))
  public <T> ResponseEntity<T> getToGivenURLWithGivenBody(
      @NotNull String path,
      @NotNull Object body,
      @NotNull ParameterizedTypeReference<T> returnType) {
    return authorizedRequest(path, body, returnType, HttpMethod.GET);
  }

  private <T> ResponseEntity<T> authorizedRequest(
      @NotNull String path,
      @Nullable Object body,
      @NotNull ParameterizedTypeReference<T> returnType,
      HttpMethod httpMethod) {
    return authorizedRequest(path, null, body, returnType, httpMethod);
  }

  private <T> ResponseEntity<T> authorizedRequest(
      @NotNull String path,
      Map<String, ?> params,
      @Nullable Object body,
      @NotNull ParameterizedTypeReference<T> returnType,
      HttpMethod httpMethod) {
    UriBuilder builder = factory.builder().path(path);
    if (params != null) {
      params.forEach(builder::queryParam);
    }
    URI uri = builder.build();
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    RequestEntity<?> requestEntity = new RequestEntity<>(body, headers, httpMethod, uri);
    return restTemplate.exchange(requestEntity, returnType);
  }

  @Nullable
  public <T> ResponseEntity<T> getEntityById(
      @NotNull String path, Map<String, ?> uriVariables, @NotNull Class<T> returnType) {
    URI uri = factory.expand(path, uriVariables);
    RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
    return restTemplate.exchange(requestEntity, returnType);
  }

  @Nullable
  @Retryable(
      retryFor = ResourceAccessException.class,
      backoff = @Backoff(delay = ONE_SECOND, multiplier = MULTIPLIER))
  public <T> ResponseEntity<T> delete(String path, Class<T> returnType) {
    URI uri = factory.builder().path(path).build();
    RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);
    return restTemplate.exchange(requestEntity, returnType);
  }
}
