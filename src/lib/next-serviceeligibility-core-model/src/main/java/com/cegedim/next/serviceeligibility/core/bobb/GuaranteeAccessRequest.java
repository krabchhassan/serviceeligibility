package com.cegedim.next.serviceeligibility.core.bobb;

import com.cegedim.next.serviceeligibility.core.utils.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuaranteeAccessRequest {
  @NotBlank(message = "L'information guaranteeId est obligatoire")
  String guaranteeId;

  @NotBlank(message = "L'information codeInsurer est obligatoire")
  String codeInsurer;

  @NotNull(message = "L'information validityDate est obligatoire")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  LocalDate validityDate;

  @NotBlank(message = "L'information preferredUsername est obligatoire")
  String preferredUsername;
}
