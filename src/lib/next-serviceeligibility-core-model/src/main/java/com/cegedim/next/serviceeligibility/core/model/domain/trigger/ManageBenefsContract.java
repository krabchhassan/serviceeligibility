package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class ManageBenefsContract {
  List<TriggeredBeneficiary> benefs;
  boolean sasCree;
  SasContrat sasContrat;
  SasContrat sasContratRecyclage;
  LinkedList<Declaration> declarations;
  boolean erreurBenef;
  boolean warningBenef;
  int nbBenefKO;
  int nbBenefWarning;
}
