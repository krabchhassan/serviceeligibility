package com.cegedim.next.serviceeligibility.core.bobb;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "guarantee_view_contexts")
@Data
public class GuaranteeViewContexts {
  @Id private String id;
  private String idContractElement;
  private String contextKey;
  private Instant createdAt;
}
