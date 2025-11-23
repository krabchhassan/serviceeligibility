package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import com.cegedim.next.serviceeligibility.core.services.pojo.GenerationDomaineResult;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import java.util.List;

public interface TriggerDomaineService {

  GenerationDomaineResult generationDomaine(
      TriggeredBeneficiary triggeredBeneficiary,
      ParametrageCarteTP parametrageCarteTp,
      Trigger trigger,
      String dateFermeture,
      boolean isContratResiliatedFromTheBeginning,
      List<DroitsTPExtended> droitsTPExtendedList)
      throws TriggerWarningException,
          BobbNotFoundException,
          PwException,
          CarenceException,
          TriggerParametersException,
          BeneficiaryToIgnoreException;
}
