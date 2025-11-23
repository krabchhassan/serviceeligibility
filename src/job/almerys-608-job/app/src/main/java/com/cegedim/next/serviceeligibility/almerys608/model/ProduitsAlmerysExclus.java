package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "produitsAlmerysExclus")
public class ProduitsAlmerysExclus extends DocumentEntity {
  String idDeclarant;
  String critereSecondaireDetaille;
  String codeExterneProduit;
}
