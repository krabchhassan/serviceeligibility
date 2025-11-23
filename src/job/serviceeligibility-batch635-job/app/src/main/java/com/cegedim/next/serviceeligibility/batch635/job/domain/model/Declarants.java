package com.cegedim.next.serviceeligibility.batch635.job.domain.model;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.DECLARAMTS_COLLECTION;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(DECLARAMTS_COLLECTION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Declarants {
  private String id;
  private String emetteurDroits;
  private String codeCircuit;
  private String codePartenaire;
  private String nom;
}
