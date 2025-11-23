package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract;

import java.util.List;
import lombok.Data;

@Data
public class ProduitDto {
  private String codeProduit;
  private List<ReferenceCouvertureDto> referencesCouverture;
}
