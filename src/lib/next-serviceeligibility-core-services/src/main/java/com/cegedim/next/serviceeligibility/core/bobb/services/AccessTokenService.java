package com.cegedim.next.serviceeligibility.core.bobb.services;

import static com.cegedim.next.serviceeligibility.core.bobb.constants.CommonConstants.*;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.AUTHORIZATION_API_URL;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.AUTHORIZATION_ENDPOINT_USERS;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.*;
import com.cegedim.next.serviceeligibility.core.bobb.dao.GuaranteeAccessTokenRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.GuaranteeViewContextRepository;
import com.cegedim.next.serviceeligibility.core.bobb.enumeration.AccessUrlType;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GuaranteeException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UserNotAuthorizedForGTException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccessTokenService {
  private final GuaranteeAccessTokenRepository guaranteeAccessTokenRepository;
  private final GuaranteeViewContextRepository guaranteeViewContextRepository;
  private final BobbCorrespondanceService bobbCorrespondanceService;
  private final RestTemplate restTemplate;
  private final String authorizationUrl;
  private final String userPermissionsEndpoint;
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final Duration TOKEN_VALIDITY = Duration.ofHours(1);

  public AccessTokenService(
      GuaranteeAccessTokenRepository guaranteeAccessTokenRepository,
      GuaranteeViewContextRepository guaranteeViewContextRepository,
      BobbCorrespondanceService bobbCorrespondanceService,
      @Qualifier("authRestTemplate") RestTemplate restTemplate,
      BeyondPropertiesService beyondPropertiesService) {
    this.guaranteeAccessTokenRepository = guaranteeAccessTokenRepository;
    this.guaranteeViewContextRepository = guaranteeViewContextRepository;
    this.bobbCorrespondanceService = bobbCorrespondanceService;
    this.restTemplate = restTemplate;
    this.authorizationUrl =
        beyondPropertiesService
            .getProperty(AUTHORIZATION_API_URL)
            .orElse(NEXT_AUTHORIZATION_CORE_API);
    this.userPermissionsEndpoint =
        beyondPropertiesService.getProperty(AUTHORIZATION_ENDPOINT_USERS).orElse(USERS_ENDPOINT);
  }

  private String generateSecureToken() {
    byte[] bytes = new byte[32];
    RANDOM.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  public GuaranteeAccessResponse getGuaranteeAccessInfo(
      GuaranteeAccessRequest guaranteeAccessRequest) {
    if (!isUserAuthorized(
        guaranteeAccessRequest.getPreferredUsername(),
        Set.of(READ_GT_PERMISSION, WRITE_GT_PERMISSION))) {
      throw new UserNotAuthorizedForGTException(
          ERROR_MESSAGE,
          HttpStatus.UNPROCESSABLE_ENTITY,
          RestErrorConstants.ERROR_USER_NOT_AUTHORIZED);
    }
    boolean exists = existsByGuaranteeId(guaranteeAccessRequest);
    if (!exists) {
      return saveAndgetCreateGuaranteeAccessResponse(guaranteeAccessRequest);
    }
    return saveAndGetViewGuaranteeAccessResponse(guaranteeAccessRequest);
  }

  public GuaranteeCreationAccessResponse getGuaranteeCreationAccessInfo(
      GuaranteeAccessRequest guaranteeAccessRequest) {
    if (!isUserAuthorized(
        guaranteeAccessRequest.getPreferredUsername(),
        Set.of(READ_GT_PERMISSION, WRITE_GT_PERMISSION))) {
      throw new UserNotAuthorizedForGTException(
          ERROR_MESSAGE,
          HttpStatus.UNPROCESSABLE_ENTITY,
          RestErrorConstants.ERROR_USER_NOT_AUTHORIZED);
    }
    if (existsByGuaranteeId(guaranteeAccessRequest)) {
      throw new GuaranteeException(
          String.format(
              "La GT existe déjà pour guaranteeId: %s et codeInsurer: %s",
              guaranteeAccessRequest.getGuaranteeId(), guaranteeAccessRequest.getCodeInsurer()),
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_GUARANTEE);
    }
    return saveAndgetGuaranteeCreationAccessResponse(guaranteeAccessRequest);
  }

  private @NotNull GuaranteeAccessResponse saveAndGetViewGuaranteeAccessResponse(
      GuaranteeAccessRequest guaranteeAccessRequest) {
    String code = generateSecureToken();
    Instant now = Instant.now();
    ContractElement contractElement =
        bobbCorrespondanceService.findCorrespondance(
            guaranteeAccessRequest.getGuaranteeId(), guaranteeAccessRequest.getCodeInsurer());
    GuaranteeViewContexts token = new GuaranteeViewContexts();
    token.setContextKey(code);
    token.setIdContractElement(contractElement.getId());
    token.setCreatedAt(now);
    guaranteeViewContextRepository.save(token);
    Instant expiresAt = now.plus(TOKEN_VALIDITY);
    return new GuaranteeAccessResponse(
        SERVICEELIGIBILITY_CORE_UI_GT_DETAIL_URL + code,
        expiresAt,
        AccessUrlType.visualization.name());
  }

  private record TokenContext(String code, Instant expiresAt) {}

  private TokenContext createAndSaveToken(GuaranteeAccessRequest guaranteeAccessRequest) {
    String code = generateSecureToken();
    Instant now = Instant.now();
    GuaranteeAccessToken token = new GuaranteeAccessToken();
    token.setGuaranteeId(guaranteeAccessRequest.getGuaranteeId());
    token.setCodeInsurer(guaranteeAccessRequest.getCodeInsurer());
    token.setContextKey(code);
    token.setCreatedAt(now);
    token.setValidityDate(guaranteeAccessRequest.getValidityDate());
    guaranteeAccessTokenRepository.save(token);
    Instant expiresAt = now.plus(TOKEN_VALIDITY);
    return new TokenContext(code, expiresAt);
  }

  private @NotNull GuaranteeAccessResponse saveAndgetCreateGuaranteeAccessResponse(
      GuaranteeAccessRequest guaranteeAccessRequest) {
    TokenContext context = createAndSaveToken(guaranteeAccessRequest);
    return new GuaranteeAccessResponse(
        SERVICEELIGIBILITY_CORE_UI_GT_CREATE_URL + context.code(),
        context.expiresAt(),
        AccessUrlType.creation.name());
  }

  private @NotNull GuaranteeCreationAccessResponse saveAndgetGuaranteeCreationAccessResponse(
      GuaranteeAccessRequest guaranteeAccessRequest) {
    TokenContext context = createAndSaveToken(guaranteeAccessRequest);
    return new GuaranteeCreationAccessResponse(
        SERVICEELIGIBILITY_CORE_UI_GT_CREATE_URL + context.code(), context.expiresAt());
  }

  public boolean existsByGuaranteeId(GuaranteeAccessRequest guaranteeAccessRequest) {
    return bobbCorrespondanceService.isCorrespondenceExist(
        guaranteeAccessRequest.getGuaranteeId(), guaranteeAccessRequest.getCodeInsurer());
  }

  public boolean isUserAuthorized(String preferredUsername, Set<String> requiredPermissions) {
    JSONArray permissionArray = getUserPermissions(preferredUsername);

    Set<String> userPermissions =
        IntStream.range(0, permissionArray.length())
            .mapToObj(permissionArray::getJSONObject)
            .map(obj -> obj.optString(CODE))
            .collect(Collectors.toSet());

    return userPermissions.containsAll(requiredPermissions);
  }

  public GuaranteeVisualisationResponse getGtIdByContextKey(String contextKey) {
    return guaranteeViewContextRepository
        .findByContextKey(contextKey)
        .map(ctx -> new GuaranteeVisualisationResponse(ctx.getIdContractElement()))
        .orElseThrow(
            () ->
                new GuaranteeException(
                    "Invalid or expired contextKey: " + contextKey,
                    HttpStatus.BAD_REQUEST,
                    RestErrorConstants.ERROR_GUARANTEE));
  }

  public GuaranteeCreationResponse getGuaranteeInfoByContextKey(String contextKey) {
    return guaranteeAccessTokenRepository
        .findByContextKey(contextKey)
        .map(
            data ->
                GuaranteeCreationResponse.builder()
                    .guaranteeId(data.getGuaranteeId())
                    .insurerCode(data.getCodeInsurer())
                    .validityDate(data.getValidityDate())
                    .build())
        .orElseThrow(
            () ->
                new GuaranteeException(
                    "Invalid or expired contextKey: " + contextKey,
                    HttpStatus.BAD_REQUEST,
                    RestErrorConstants.ERROR_GUARANTEE));
  }

  private @NotNull JSONArray getUserPermissions(String preferredUsername) {
    ResponseEntity<String> response =
        restTemplate.getForEntity(getUrl(preferredUsername), String.class);
    return new JSONArray(new JSONTokener(response.getBody()));
  }

  private String getUrl(String preferredUsername) {
    return String.format(URL_FORMAT, authorizationUrl, userPermissionsEndpoint, preferredUsername);
  }
}
