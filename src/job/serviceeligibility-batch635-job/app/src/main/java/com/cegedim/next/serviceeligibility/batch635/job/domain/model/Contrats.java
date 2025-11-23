package com.cegedim.next.serviceeligibility.batch635.job.domain.model;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.CONTRATS_COLLECTION;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(CONTRATS_COLLECTION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrats {
  private String id;
  private String idDeclarant;
}
