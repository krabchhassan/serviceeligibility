package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOnlineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageAtelierProduit {
  String codeOc;
  String codeOffre;
  String version;
  String codeProduit;
  ModeAssemblage modeAssemblage;
  String domaine;
  TpOfflineRightsDetails detailsOffline;
  TpOnlineRightsDetails detailsOnline;
  List<PAPNatureTags> naturesTags;

  String validityDate;
  String endValidityDate;

  String pwValidityDate;
  String pwEndValidityDate;
  String bobbEndValidityDate;
}
