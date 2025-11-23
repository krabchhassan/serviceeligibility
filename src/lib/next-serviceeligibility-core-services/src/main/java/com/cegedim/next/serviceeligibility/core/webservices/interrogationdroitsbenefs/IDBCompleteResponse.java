package com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs;

import com.cegedimassurances.norme.commun.TypeCodeReponse;
import java.util.List;
import lombok.Data;

@Data
public class IDBCompleteResponse {
  private TypeCodeReponse codeReponse;

  private List<IDBResponse> droits;
}
