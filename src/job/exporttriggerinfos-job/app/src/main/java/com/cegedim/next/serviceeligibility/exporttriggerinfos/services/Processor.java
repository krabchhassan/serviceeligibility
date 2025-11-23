package com.cegedim.next.serviceeligibility.exporttriggerinfos.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExportTriggerInfos;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.exporttriggerinfos.constants.Constants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Processor {

  private final TriggerCSVService triggerCSVService;

  @NewSpan
  public int export(CompteRenduExportTriggerInfos compteRenduExportTriggerInfos) {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;

    List<String> paths = triggerCSVService.saveNotExported();

    compteRenduExportTriggerInfos.setCheminsFichiersTriggers(paths);
    return processReturnCode;
  }
}
