package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class TriggerBeneficiaryToDeclarations {

  private String dateFermeture;

  private String dateResiliation;

  private String dateRadiation;

  private TriggeredBeneficiary triggeredBeneficiary;

  private List<PeriodeSuspension> suspensionPeriods;

  private LinkedList<Declaration> existingDeclarations;

  private Declaration createdDeclaration;

  private Declaration futureDeclaration;

  private Declaration closedDeclaration;

  private LinkedList<Declaration> closedDeclarations = new LinkedList<>();

  // 1 création fermeture, 2 pas de fermeture, 3 rien à intégrer
  private int whatToDo;

  private boolean closeAll;

  private boolean changeRightPeriods;

  private LinkedList<Declaration> generatedDeclarations = new LinkedList<>();

  private ParametrageCarteTP parametrageCarteTP;

  private Trigger trigger;

  private boolean isContratResiliatedFromTheBeginning = false;
  private boolean isWarning;
}
