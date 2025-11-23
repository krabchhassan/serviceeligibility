package com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit;

import com.cegedimassurances.norme.base_de_droit.TypeDeclaration;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import java.util.List;
import lombok.Data;

@Data
public class GetInfoBddResponseDto {
  private TypeCodeReponse codeReponse;
  private List<TypeDeclaration> droits;
}
