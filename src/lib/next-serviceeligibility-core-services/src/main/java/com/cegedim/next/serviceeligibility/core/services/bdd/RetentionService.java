package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.List;

public interface RetentionService {

  void updateOrCreateRetention(Retention retention);

  List<Retention> getAll();

  void updateStatus(Retention retention, RetentionStatus status);

  String calculDateRetention(ContratAIV6 contract, String numeroPersonne);

  boolean isMultiContrat(String idDeclarant, String personNumber);

  List<Retention> manageRetention(ContratAIV6 newContract, ContratAIV6 existingContract);

  void manageRetentionAssure(
      ContratAIV6 newContract,
      ContratAIV6 existingContract,
      Assure assure,
      List<Retention> retentionList);
}
