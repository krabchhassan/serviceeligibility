package com.cegedim.beyond.blb.recalcul.util;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.OmuHelperImpl;
import com.cegedim.next.serviceeligibility.core.model.crex.JobRecalculBlbEndEventDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CrexUtils {

  private final OmuHelper omuHelper = new OmuHelperImpl();

  // --------------------
  // METHODS
  // --------------------
  public void updateOmuCrex(final JobRecalculBlbEndEventDto jobRecalculBlbEndEventDto) {
    omuHelper.produceOutputParameters(jobRecalculBlbEndEventDto.toCrexMap());
  }
}
