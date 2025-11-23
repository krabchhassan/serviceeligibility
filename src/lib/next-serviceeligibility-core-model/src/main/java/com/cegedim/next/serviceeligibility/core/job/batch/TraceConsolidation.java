package com.cegedim.next.serviceeligibility.core.job.batch;

import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "tracesConsolidation")
public class TraceConsolidation extends DocumentEntity {
  @Field(write = Field.Write.ALWAYS)
  private Object idDeclaration;

  @Field(write = Field.Write.ALWAYS)
  private Object idDeclarationConsolidee;

  @Field(write = Field.Write.ALWAYS)
  private String idDeclarant;

  @Field(write = Field.Write.ALWAYS)
  private Date dateExecution;

  @Field(write = Field.Write.ALWAYS)
  private String codeService;

  @Field(write = Field.Write.ALWAYS)
  private String codeRejet;

  @Field(write = Field.Write.ALWAYS)
  private String batch;

  @Field(write = Field.Write.ALWAYS)
  private String nomFichierARL;

  @Field(write = Field.Write.ALWAYS)
  private String codeClient;

  @Field(write = Field.Write.ALWAYS)
  private String collectionConsolidee;
}
