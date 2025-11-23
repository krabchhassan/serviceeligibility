package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "almv3_InfoSite")
public class InfoSite {

  private String refEntreprise;
  private String refSite;
  private Adresse adresse;
}
