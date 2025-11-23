package com.cegedim.beyond.serviceeligibility.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MlsResponse {
  @NotNull private String code;
  @NotNull private String clientId;
  @NotNull private String clientSecret;
  @NotNull private String keycloakUrl;
  @NotNull private String apiClaimUrl;
  @NotNull private String beyondInfoUrl;
}
