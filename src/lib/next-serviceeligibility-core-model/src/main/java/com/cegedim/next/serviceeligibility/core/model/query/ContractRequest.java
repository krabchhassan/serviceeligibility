package com.cegedim.next.serviceeligibility.core.model.query;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Data
@Builder(toBuilder = true)
@ToString(of = {"amc", "nir", "birthDate", "searchDate"})
public class ContractRequest {
  @NotNull
  @Schema(title = "Insured id", example = "19003*******6", required = true)
  private String nir;

  @Nullable
  @Schema(title = "Birthday", example = "19900325")
  private String birthDate;

  @NotNull
  @Schema(title = "Benefit date", example = "2023/03/20", required = true)
  private String searchDate;

  @Nullable
  @Schema(
      hidden = true,
      allowableValues = {"OFFLINE", "ONLINE"},
      example = "OFFLINE")
  private TypePeriode type;

  @Nullable
  @Schema(title = "Issuer company id", example = "0000452433")
  private String amc;

  // UTILS
  @EqualsAndHashCode.Exclude
  @Schema(hidden = true)
  private String hash;
}
