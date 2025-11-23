package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageTrigger {

  private TriggeredBeneficiary triggeredBeneficiary;

  private ParametrageCarteTP parametrageCarteTP;

  private String dateEffet;

  private String dateSouscription;

  private TriggerEmitter origine;

  List<String> listDomainesParametrage;

  private boolean isRdo;

  private boolean isEventReprise;
}
