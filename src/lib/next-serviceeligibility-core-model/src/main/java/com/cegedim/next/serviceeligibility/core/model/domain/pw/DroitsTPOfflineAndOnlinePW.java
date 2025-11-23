package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class DroitsTPOfflineAndOnlinePW {
  String offerCode;
  ModeAssemblage assemblyMode;
  String offerVersionCode;
  LocalDateTime effectDate;
  String validityDate;
  String endValidityDate;
  String engineVersion;
  List<DomainRights> domains;
}
