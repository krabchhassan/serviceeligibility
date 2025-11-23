package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicePrestation")
@Data
public class ContratAIV5Recipient {
  @Id private String id;
  private String idDeclarant;
  private String societeEmettrice;
  private String numero;
  private List<AssureV5Recipient> assures;
}
