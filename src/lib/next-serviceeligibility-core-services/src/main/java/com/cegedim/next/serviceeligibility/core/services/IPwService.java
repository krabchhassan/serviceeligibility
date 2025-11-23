package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflineAndOnlinePW;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import java.util.List;
import org.json.JSONArray;

public interface IPwService {

  List<DroitsTPOfflineAndOnlinePW> getDroitsOfflineAndOnlineProductsWorkshop(
      List<String> errorsToFill,
      String codeProduit,
      String codeAmc,
      String dateDebut,
      String dateFin)
      throws PwException;

  JSONArray getOfferStructure(
      String issuerCompany,
      String offerCode,
      String productCode,
      String startDate,
      String endDate,
      String context,
      String version);
}
