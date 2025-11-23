package com.cegedim.next.serviceeligibility.core.webservices.clc;

import com.cegedimassurances.norme.commun.TypeCodeReponse;
import java.util.List;
import lombok.Data;

@Data
public class CLCCompleteResponse {
  private TypeCodeReponse codeReponse;

  private List<CLCResponse> droits;
}
