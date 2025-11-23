package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BulkContracts {
  private Map<String, List<ExtractedContract>> content;
  private Map<String, ExtractedError> errors;

  // UTILS
  public BulkContracts merge(@NotNull BulkContracts other) {
    if (Objects.nonNull(other.content)) content.putAll(other.content);
    if (Objects.nonNull(other.errors)) errors.putAll(other.errors);
    return this;
  }
}
