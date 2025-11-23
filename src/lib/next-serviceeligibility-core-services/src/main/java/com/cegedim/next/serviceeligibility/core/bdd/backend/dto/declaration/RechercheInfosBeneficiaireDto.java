package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

/** Classe DTO: infos Beneficiaire. */
@Data
public class RechercheInfosBeneficiaireDto {

  int totalBeneficiaires = 0;

  private List<Pair<Boolean, String>> beneficiaires;
}
