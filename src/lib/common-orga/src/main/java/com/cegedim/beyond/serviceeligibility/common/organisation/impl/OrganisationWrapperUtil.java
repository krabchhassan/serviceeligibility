package com.cegedim.beyond.serviceeligibility.common.organisation.impl;

import com.cegedim.beyond.serviceeligibility.common.exception.OrganisationWrapperException;
import com.cegedim.beyond.serviceeligibility.common.model.MlsResponse;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@UtilityClass
class OrganisationWrapperUtil {
  private static final String API_KEY_SEPARATOR = "/";

  @NotNull
  static MlsResponse buildResponse(
      @NotNull String code,
      @NotNull String secret,
      @NotNull String keycloakUrl,
      @NotNull String apiClaimUrl,
      @NotNull String beyondInfoUrl) {
    String[] keycloakSecrets = OrganisationWrapperUtil.getApiKeyInformations(secret);
    if (keycloakSecrets != null && keycloakSecrets.length == 2) {
      return MlsResponse.builder()
          .code(code)
          .clientId(keycloakSecrets[0])
          .clientSecret(keycloakSecrets[1])
          .keycloakUrl(keycloakUrl)
          .apiClaimUrl(apiClaimUrl)
          .beyondInfoUrl(beyondInfoUrl)
          .build();
    }
    throw new OrganisationWrapperException("Invalid secret configuration for %s".formatted(code));
  }

  @Nullable
  private static String[] getApiKeyInformations(@Nullable String data) {
    if (StringUtils.hasText(data)) {
      return data.split(API_KEY_SEPARATOR);
    }
    return null;
  }
}
