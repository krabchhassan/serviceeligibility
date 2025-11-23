package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import lombok.Data;

@Data
public class DomainRights {
  String domainCode;
  TpOfflineRightsDetails tpOffline;
  TpOnlineRightsDetails tpOnline;
}
