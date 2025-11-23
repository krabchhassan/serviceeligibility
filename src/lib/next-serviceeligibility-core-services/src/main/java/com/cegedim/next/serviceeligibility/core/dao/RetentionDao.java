package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import java.util.List;

public interface RetentionDao {
  long findAndLock(Retention retention);

  List<Retention> findAllByDelai(String idDeclarant, int delaiRetention);

  void createRetention(Retention retention);

  void updateRetention(Retention retention, RetentionStatus status);

  Retention findRetention(
      String insurerId, String subscriberNumber, String contractNumber, String personNumber);

  List<Retention> getAll();

  void updateRetentionStatus(Retention retention, RetentionStatus status);
}
