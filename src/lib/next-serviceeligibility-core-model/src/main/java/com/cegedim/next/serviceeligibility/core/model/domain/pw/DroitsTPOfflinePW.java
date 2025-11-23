package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class DroitsTPOfflinePW {
  String offerCode;
  String offerVersionCode;
  LocalDateTime effectDate;
  String validityDate;
  String endValidityDate;
  String engineVersion;
  Map<String, DetailsByDomain> detailsByDomain;
}
