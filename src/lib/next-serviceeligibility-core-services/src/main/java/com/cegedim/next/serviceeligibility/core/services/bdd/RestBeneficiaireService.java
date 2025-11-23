package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireConsultationHistoryDao;
import com.cegedim.next.serviceeligibility.core.model.entity.BeneficiaireConsultationHistory;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestBeneficiaireService {

  private final BeneficiaireConsultationHistoryDao beneficiaireConsultationHistoryDao;

  @ContinueSpan(log = "findIdHistory")
  public List<Pair<Boolean, String>> findIdHistory(String user) {
    List<BeneficiaireConsultationHistory> benefHistory =
        beneficiaireConsultationHistoryDao.findBeneficiaireConsultationHistoriesByCriteria(
            user, true);
    List<Pair<Boolean, String>> idHistory = new ArrayList<>();
    for (BeneficiaireConsultationHistory benef : benefHistory) {
      idHistory.add(Pair.of(benef.isExternalOrigin(), benef.getIdElasticBeneficiaire()));
    }
    return idHistory;
  }

  @ContinueSpan(log = "addHistory")
  public void addHistory(String id, String env, String user) {
    BeneficiaireConsultationHistory newBeneficiaireConsultationHistory =
        new BeneficiaireConsultationHistory();
    newBeneficiaireConsultationHistory.setUser(user);
    newBeneficiaireConsultationHistory.setIdElasticBeneficiaire(id);
    newBeneficiaireConsultationHistory.setExternalOrigin(Constants.ENV_EXTERNE.equals(env));

    List<BeneficiaireConsultationHistory> beneficiaireConsultationHistories =
        beneficiaireConsultationHistoryDao.findBeneficiaireConsultationHistoriesByCriteria(user);
    for (int index = 0; index < beneficiaireConsultationHistories.size(); index++) {
      BeneficiaireConsultationHistory oldBeneficiaireConsultationHistory =
          beneficiaireConsultationHistories.get(index);
      if (newBeneficiaireConsultationHistory.equals(oldBeneficiaireConsultationHistory)) {
        oldBeneficiaireConsultationHistory.setDateConsultation(new Date());
        beneficiaireConsultationHistoryDao.update(oldBeneficiaireConsultationHistory);
        return;
      }
    }

    if (beneficiaireConsultationHistories.size() > 9) {
      BeneficiaireConsultationHistory oldDeclarationConsultationHistory =
          beneficiaireConsultationHistories.get(9);
      beneficiaireConsultationHistoryDao.delete(oldDeclarationConsultationHistory);
    }
    newBeneficiaireConsultationHistory.setDateConsultation(new Date());
    beneficiaireConsultationHistoryDao.create(newBeneficiaireConsultationHistory);
  }

  @ContinueSpan(log = "deleteHistory")
  public long deleteHistory() {
    return beneficiaireConsultationHistoryDao.deleteAllBeneficiaireConsultationHistory();
  }
}
