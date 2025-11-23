package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.enumeration.TraceSource;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import java.util.List;

public final class ContractReceptionEventUtils {

  private ContractReceptionEventUtils() {}

  public static void sendReceptionEvent(
      ContratAICommun newContract, String traceId, EventService eventService) {
    if (newContract == null) {
      newContract = new ContratAIV6();
    }
    newContract.setTraceId(traceId);
    eventService.sendObservabilityEventContractReception(newContract);
  }

  public static void sendReceptionEventForRdo(
      String fileName,
      Long lineNumber,
      String error,
      EventService eventService,
      TraceService traceService) {
    String traceId =
        traceService.createTraceWithError(
            TraceSource.File,
            TraceStatus.ErrorDeserializing,
            fileName,
            Constants.CONTRACT_TRACE,
            lineNumber,
            List.of(error));
    sendReceptionEvent(null, traceId, eventService);
  }
}
