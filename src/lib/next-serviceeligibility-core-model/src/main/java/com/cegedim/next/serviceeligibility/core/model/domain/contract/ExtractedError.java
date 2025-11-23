package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.query.ContractRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExtractedError {
  protected static final String PREFIX_CODE = "NEXT-SERVICEELIGIBILITY-CORE-EXTRACTED";

  private String code;
  private String msg;
  private ContractRequest request;

  // --------------------
  // NAMED ERRORS
  // --------------------
  public static final class NotFound extends ExtractedError {
    public NotFound(ContractRequest request) {
      super(PREFIX_CODE + "-404-1", errorMsg(request), request);
    }

    private static String errorMsg(ContractRequest request) {
      return String.format("Aucun contrat trouvé pour cette demande : %s", request);
    }
  }

  public static final class NoActiveContract extends ExtractedError {
    public NoActiveContract(ContractRequest request) {
      super(PREFIX_CODE + "-404-2", errorMsg(request), request);
    }

    private static String errorMsg(ContractRequest request) {
      return String.format("Aucun contrat actif à la date de recherche : %s", request);
    }
  }

  public static final class InvalidDateFormat extends ExtractedError {
    public InvalidDateFormat(ContractRequest request) {
      super(PREFIX_CODE + "-400-1", errorMsg(request), request);
    }

    private static String errorMsg(ContractRequest request) {
      return String.format("Format de date non valide : %s", request);
    }
  }
}
