package com.cegedim.next.serviceeligibility.core.bobb;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "guarantee_access_tokens")
@Data
public class GuaranteeAccessToken {
  @Id private String id;
  private String guaranteeId;
  private String codeInsurer;
  private LocalDate validityDate;
  private String contextKey;
  private Instant createdAt;
}
