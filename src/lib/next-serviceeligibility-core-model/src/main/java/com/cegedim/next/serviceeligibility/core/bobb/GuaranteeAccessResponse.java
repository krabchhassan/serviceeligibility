package com.cegedim.next.serviceeligibility.core.bobb;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuaranteeAccessResponse {
  private String url;
  private Instant expiresAt;
  private String type;
}
