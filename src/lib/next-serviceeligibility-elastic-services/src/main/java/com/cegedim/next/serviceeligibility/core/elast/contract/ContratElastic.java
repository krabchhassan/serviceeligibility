package com.cegedim.next.serviceeligibility.core.elast.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "bdd-histo-contrat", createIndex = false)
public class ContratElastic {
  @Id private String id;
  private ContractTP contrat;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime dateSauvegarde;
}
