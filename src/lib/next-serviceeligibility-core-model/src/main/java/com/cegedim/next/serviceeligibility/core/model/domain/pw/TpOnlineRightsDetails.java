package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TpOnlineRightsDetails {

  String nature;

  String network;

  public TpOnlineRightsDetails(TpOnlineRightsDetails source) {
    this.nature = source.getNature();
    this.network = source.getNetwork();
  }
}
