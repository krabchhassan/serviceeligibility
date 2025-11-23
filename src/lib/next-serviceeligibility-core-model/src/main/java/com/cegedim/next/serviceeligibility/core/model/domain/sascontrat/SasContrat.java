package com.cegedim.next.serviceeligibility.core.model.domain.sascontrat;

import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sasContrat")
@Data
public class SasContrat {
  @Id private String id;

  private String idDeclarant;
  private String numeroContrat;
  private String numeroAdherent;
  private String servicePrestationId;
  private List<TriggerBenefs> triggersBenefs;
  private List<String> anomalies;
  private List<Date> dates;
  private boolean recycling;
}
