package com.cegedim.next.serviceeligibility.core.model.domain.audit;

import java.util.Date;

public interface Audit {

  Date getDateCreation();

  void setDateCreation(Date dateCreation);

  String getUserCreation();

  void setUserCreation(String userCreation);

  Date getDateModification();

  void setDateModification(Date dateModification);

  String getUserModification();

  void setUserModification(String userModification);
}
