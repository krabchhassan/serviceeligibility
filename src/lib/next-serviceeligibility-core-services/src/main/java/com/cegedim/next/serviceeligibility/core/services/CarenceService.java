package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import java.util.List;

public interface CarenceService {

  static final String BENEFIT_TYPE_FIELD = "natureCode";
  static final String WAITING_CODE_FIELD = "waitingCode";
  static final String START_DATE_FIELD = "startEffectDate";
  static final String END_DATE_FIELD = "endEffectDate";

  List<ParametrageCarence> getParametragesCarence(
      String issuerCompany, String offerCode, String productCode) throws CarenceException;
}
