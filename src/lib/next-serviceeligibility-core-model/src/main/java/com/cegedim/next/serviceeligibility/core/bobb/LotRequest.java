package com.cegedim.next.serviceeligibility.core.bobb;

import lombok.Data;

@Data
public class LotRequest {
  private int perPage = 10;
  private int page = 1;
  private String sortBy;
  private String direction;
  private String code;
  private String libelle;
  private String codeAssureur;
  private String codeGarantie;
}
