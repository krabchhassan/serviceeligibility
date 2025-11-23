package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.mongodb.client.ClientSession;
import java.util.List;
import lombok.Data;

@Data
public class RequestTriggerProcessing {

  private String idTrigger;

  private boolean isRenouvellement;
  private boolean isRecycling;

  private List<TriggeredBeneficiary> benefs;

  private String updateTrigger;

  private Long randomRecyclingId;

  private ClientSession session;

  private String servicePrestationId;
}
