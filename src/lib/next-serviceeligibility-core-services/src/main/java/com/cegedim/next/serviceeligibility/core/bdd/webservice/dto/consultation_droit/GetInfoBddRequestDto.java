package com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit;

import com.cegedimassurances.norme.amc.TypeAmc;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd;
import com.cegedimassurances.norme.beneficiaire.TypeBeneficiaireDemandeur;
import com.cegedimassurances.norme.commun.TypeDates;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetInfoBddRequestDto {
  @Valid @NotNull private TypeBeneficiaireDemandeur beneficiaire;

  @Valid @NotNull private TypeAmc amc;

  @Valid @NotNull private TypeInfoBdd infoBdd;

  private TypeDates dates;
}
