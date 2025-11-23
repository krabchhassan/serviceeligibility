package com.cegedim.next.serviceeligibility.core.bobb.services;

import static com.cegedim.next.serviceeligibility.core.bobb.constants.CommonConstants.ERROR_MESSAGE;
import static com.cegedim.next.serviceeligibility.core.bobb.constants.CommonConstants.SERVICEELIGIBILITY_CORE_UI_GT_DETAIL_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.*;
import com.cegedim.next.serviceeligibility.core.bobb.dao.GuaranteeAccessTokenRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.GuaranteeViewContextRepository;
import com.cegedim.next.serviceeligibility.core.bobb.enumeration.AccessUrlType;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GuaranteeException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UserNotAuthorizedForGTException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AccessTokenServiceTest {

  public static final String GUARANTEE_ID = "G123";
  public static final String CODE_INSURER = "INS001";
  public static final String ID = "GT1200";
  public static final String PREFERRED_USERNAME = "user@example.com";
  public static final String WRITE_PERMISSIONS = "SE_P_WRITE_GT";
  public static final String READ_PERMISSIONS = "SE_P_READ_GT";
  public static final String CONTEXT_KEY_EXIST = "CTX-001";
  public static final String CONTEXT_KEY_MISSING = "CTX-404";
  @Mock private GuaranteeAccessTokenRepository guaranteeAccessTokenRepository;
  @Mock private GuaranteeViewContextRepository guaranteeViewContextRepository;
  @Mock private BobbCorrespondanceService bobbCorrespondanceService;
  @Mock private BeyondPropertiesService beyondPropertiesService;
  @Mock private RestTemplate restTemplate;

  @Spy @InjectMocks private AccessTokenService accessTokenService;

  @Test
  void should_save_request_data_and_return_response_when_correspondence_does_not_exist() {
    String mockResponse = returnMockPermissions();
    GuaranteeAccessRequest request =
        new GuaranteeAccessRequest(GUARANTEE_ID, CODE_INSURER, LocalDate.now(), PREFERRED_USERNAME);
    when(bobbCorrespondanceService.isCorrespondenceExist(GUARANTEE_ID, CODE_INSURER))
        .thenReturn(false);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

    GuaranteeAccessResponse response = accessTokenService.getGuaranteeAccessInfo(request);

    assertThat(response).isNotNull();
    assertThat(response.getUrl()).isNotBlank();
    assertThat(response.getUrl()).doesNotContain("=");
    assertThat(response.getExpiresAt()).isAfter(Instant.now());
    assertThat(response.getType().equalsIgnoreCase(AccessUrlType.creation.name()));

    ArgumentCaptor<GuaranteeAccessToken> captor =
        ArgumentCaptor.forClass(GuaranteeAccessToken.class);
    verify(guaranteeAccessTokenRepository).save(captor.capture());
  }

  @Test
  void should_save_id_contract_element_and_return_response_when_correspondence_exist() {
    String mockResponse = returnMockPermissions();
    ContractElement contractElement = new ContractElement();
    contractElement.setId(ID);
    GuaranteeAccessRequest request =
        new GuaranteeAccessRequest(GUARANTEE_ID, CODE_INSURER, LocalDate.now(), PREFERRED_USERNAME);
    when(bobbCorrespondanceService.isCorrespondenceExist(GUARANTEE_ID, CODE_INSURER))
        .thenReturn(true);

    when(bobbCorrespondanceService.findCorrespondance(GUARANTEE_ID, CODE_INSURER))
        .thenReturn(contractElement);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

    GuaranteeAccessResponse response = accessTokenService.getGuaranteeAccessInfo(request);

    assertThat(response).isNotNull();
    assertThat(response.getUrl()).isNotBlank();
    assertThat(response.getUrl()).doesNotContain("=");
    assertThat(response.getExpiresAt()).isAfter(Instant.now());

    ArgumentCaptor<GuaranteeViewContexts> captor =
        ArgumentCaptor.forClass(GuaranteeViewContexts.class);
    verify(guaranteeViewContextRepository).save(captor.capture());

    GuaranteeViewContexts savedToken = captor.getValue();
    assertThat(savedToken.getIdContractElement()).isEqualTo(ID);
    assertThat(SERVICEELIGIBILITY_CORE_UI_GT_DETAIL_URL + savedToken.getContextKey())
        .isEqualTo(response.getUrl());
    assertThat(savedToken.getCreatedAt()).isNotNull();
    assertThat(response.getType().equalsIgnoreCase(AccessUrlType.visualization.name()));
  }

  @Test
  void should_return_random_base64_url_string() {
    String token1 = ReflectionTestUtils.invokeMethod(accessTokenService, "generateSecureToken");
    String token2 = ReflectionTestUtils.invokeMethod(accessTokenService, "generateSecureToken");

    assertThat(token1).isNotBlank();
    assertThat(token2).isNotBlank();
    assertThat(token1).isNotEqualTo(token2);
    assertThat(token1).matches("^[A-Za-z0-9_-]+$");
    assertThat(Base64.getUrlDecoder().decode(token1)).hasSize(32);
  }

  @Test
  void should_be_one_hour_token_validity() {
    Duration validity =
        (Duration) ReflectionTestUtils.getField(AccessTokenService.class, "TOKEN_VALIDITY");
    assertThat(validity).isEqualTo(Duration.ofHours(1));
  }

  @Test
  void should_return_true_when_user_has_all_required_permissions() {
    String mockResponse = returnMockPermissions();
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
    Set<String> required = Set.of(WRITE_PERMISSIONS, READ_PERMISSIONS);
    boolean result = accessTokenService.isUserAuthorized(PREFERRED_USERNAME, required);
    assertTrue(result);
  }

  @Test
  void should_return_false_when_missing_one_permission() {
    String mockResponse =
        """
            [
                {"code": "SE_P_READ_GT"}
            ]
        """;
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
    Set<String> required = Set.of(WRITE_PERMISSIONS);
    boolean result = accessTokenService.isUserAuthorized(PREFERRED_USERNAME, required);
    assertFalse(result);
  }

  @Test
  void should_throw_exception_when_user_not_authorized() {
    GuaranteeAccessRequest request =
        new GuaranteeAccessRequest(GUARANTEE_ID, CODE_INSURER, LocalDate.now(), PREFERRED_USERNAME);

    Mockito.doReturn(false)
        .when(accessTokenService)
        .isUserAuthorized(Mockito.anyString(), Mockito.anySet());
    UserNotAuthorizedForGTException exception =
        assertThrows(
            UserNotAuthorizedForGTException.class,
            () -> accessTokenService.getGuaranteeCreationAccessInfo(request));
    assertEquals(ERROR_MESSAGE, exception.getMessage());
  }

  @Test
  void should_throw_guarantee_already_exists_exception_when_guarantee_exists() {
    GuaranteeAccessRequest request =
        new GuaranteeAccessRequest(GUARANTEE_ID, CODE_INSURER, LocalDate.now(), PREFERRED_USERNAME);

    Mockito.doReturn(true)
        .when(accessTokenService)
        .isUserAuthorized(Mockito.anyString(), Mockito.anySet());
    Mockito.doReturn(true).when(accessTokenService).existsByGuaranteeId(Mockito.any());
    GuaranteeException exception =
        assertThrows(
            GuaranteeException.class,
            () -> accessTokenService.getGuaranteeCreationAccessInfo(request));

    assertTrue(exception.getMessage().contains(GUARANTEE_ID));
    assertTrue(exception.getMessage().contains(CODE_INSURER));
  }

  @Test
  void should_save_request_data_for_create_api_and_return_response_when_guarantee_not_exist() {
    String mockResponse = returnMockPermissions();
    GuaranteeAccessRequest request =
        new GuaranteeAccessRequest(GUARANTEE_ID, CODE_INSURER, LocalDate.now(), PREFERRED_USERNAME);
    when(bobbCorrespondanceService.isCorrespondenceExist(GUARANTEE_ID, CODE_INSURER))
        .thenReturn(false);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

    GuaranteeCreationAccessResponse response =
        accessTokenService.getGuaranteeCreationAccessInfo(request);

    assertThat(response).isNotNull();
    assertThat(response.getUrl()).isNotBlank();
    assertThat(response.getUrl()).doesNotContain("=");
    assertThat(response.getExpiresAt()).isAfter(Instant.now());

    ArgumentCaptor<GuaranteeAccessToken> captor =
        ArgumentCaptor.forClass(GuaranteeAccessToken.class);
    verify(guaranteeAccessTokenRepository).save(captor.capture());
  }

  private static @NotNull String returnMockPermissions() {
    return """
            [
                {"code": "SE_P_WRITE_GT"},
                {"code": "SE_P_READ_GT"}
            ]
        """;
  }

  @Test
  void should_get_guarantee_info_when_context_key_found() {
    GuaranteeAccessToken token = new GuaranteeAccessToken();
    token.setContextKey(CONTEXT_KEY_EXIST);
    token.setGuaranteeId("G123");
    token.setCodeInsurer("INS001");
    token.setValidityDate(LocalDate.of(2025, 12, 31));
    token.setCreatedAt(Instant.now());

    when(guaranteeAccessTokenRepository.findByContextKey(CONTEXT_KEY_EXIST))
        .thenReturn(Optional.of(token));

    GuaranteeCreationResponse response =
        accessTokenService.getGuaranteeInfoByContextKey(CONTEXT_KEY_EXIST);

    assertThat(response).isNotNull();
    assertThat(response.guaranteeId()).isEqualTo("G123");
    assertThat(response.insurerCode()).isEqualTo("INS001");
    assertThat(response.validityDate()).isEqualTo(LocalDate.of(2025, 12, 31));

    verify(guaranteeAccessTokenRepository, times(1)).findByContextKey(CONTEXT_KEY_EXIST);
  }

  @Test
  void should_get_guarantee_info_when_context_key_not_found() {
    when(guaranteeAccessTokenRepository.findByContextKey(CONTEXT_KEY_MISSING))
        .thenReturn(Optional.empty());

    GuaranteeException exception =
        assertThrows(
            GuaranteeException.class,
            () -> accessTokenService.getGuaranteeInfoByContextKey(CONTEXT_KEY_MISSING));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertTrue(exception.getMessage().contains("Invalid or expired contextKey"));
    verify(guaranteeAccessTokenRepository, times(1)).findByContextKey(CONTEXT_KEY_MISSING);
  }

  @Test
  void should_get_gt_id_when_context_key_found() {
    GuaranteeViewContexts ctx = new GuaranteeViewContexts();
    ctx.setContextKey(CONTEXT_KEY_EXIST);
    ctx.setIdContractElement("GT-123");
    ctx.setCreatedAt(Instant.now());

    when(guaranteeViewContextRepository.findByContextKey(CONTEXT_KEY_EXIST))
        .thenReturn(Optional.of(ctx));

    GuaranteeVisualisationResponse response =
        accessTokenService.getGtIdByContextKey(CONTEXT_KEY_EXIST);

    assertThat(response).isNotNull();
    assertThat(response.gtId()).isEqualTo("GT-123");

    verify(guaranteeViewContextRepository, times(1)).findByContextKey(CONTEXT_KEY_EXIST);
  }

  @Test
  void should_get_gt_id_when_context_key_not_found() {
    when(guaranteeViewContextRepository.findByContextKey(CONTEXT_KEY_MISSING))
        .thenReturn(Optional.empty());

    GuaranteeException exception =
        assertThrows(
            GuaranteeException.class,
            () -> accessTokenService.getGtIdByContextKey(CONTEXT_KEY_MISSING));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertTrue(exception.getMessage().contains("Invalid or expired contextKey"));

    verify(guaranteeViewContextRepository, times(1)).findByContextKey(CONTEXT_KEY_MISSING);
  }
}
