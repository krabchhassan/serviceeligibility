package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import java.util.List;
import lombok.Data;

@Data
public class OffresProduitsAmcResponse {
  private String amc;
  private String codeOC;
  private String libelleOC;
  private List<OffreProduits> offresProduits;
}
