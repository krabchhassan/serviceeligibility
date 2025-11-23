package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract;

import java.util.List;
import lombok.Data;

@Data
public class GarantieDto {
  private String codeGarantie;
  private List<ProduitDto> produits;
}
