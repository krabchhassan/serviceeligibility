package com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ProductCombination {
  private List<GarantieTechnique> garantieTechniqueList;
  private List<LotAlmerys> lotAlmerysList;
  @NotNull private String dateDebut;
  private String dateFin;
}
