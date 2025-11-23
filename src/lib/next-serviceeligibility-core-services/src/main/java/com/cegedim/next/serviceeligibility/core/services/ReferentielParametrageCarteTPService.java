package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.dao.ReferentielParametrageCarteTPDao;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ReferentielParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferentielParametrageCarteTPService {

  private final ReferentielParametrageCarteTPDao dao;

  @ContinueSpan(log = "getByAmc ReferentielParametrageCarteTP")
  public ReferentielParametrageCarteTP getByAmc(String amc) {
    return dao.getByAmc(amc);
  }

  @ContinueSpan(log = "update ReferentielParametrageCarteTP")
  public void update(ReferentielParametrageCarteTP referentielParametrageCarteTP) {
    dao.update(referentielParametrageCarteTP);
  }

  @ContinueSpan(log = "getIdentifiantsCollectiviteForAmc ReferentielParametrageCarteTP")
  public List<String> getIdentifiantsCollectiviteForAmc(String amc) {
    return getByAmc(amc).getIdentifiantsCollectivite();
  }

  @ContinueSpan(log = "getGroupesPopulationForAmc ReferentielParametrageCarteTP")
  public List<String> getGroupesPopulationForAmc(String amc) {
    return getByAmc(amc).getGroupesPopulation();
  }

  @ContinueSpan(log = "getPortefeuilleForAmc ReferentielParametrageCarteTP")
  public List<String> getPortefeuilleForAmc(String amc) {
    return getByAmc(amc).getPortefeuille();
  }

  @ContinueSpan(log = "getByAmcs ReferentielParametrageCarteTP")
  public List<ReferentielParametrageCarteTP> getByAmcs(List<String> amcs) {
    return dao.getByAmcs(amcs);
  }

  @ContinueSpan(log = "saveReferentielParametrageCarteTP")
  public void saveReferentielParametrageCarteTP(ContratAIV6 contract) {
    ReferentielParametrageCarteTP ref = new ReferentielParametrageCarteTP();
    ref.setAmc(contract.getIdDeclarant());
    ContratCollectifV6 contratCollectifV6 = contract.getContratCollectif();
    List<String> identifiantsCollectivite = new ArrayList<>();
    List<String> groupesPopulation = new ArrayList<>();
    List<String> portefeuille = new ArrayList<>();
    if (contratCollectifV6 != null) {
      if (StringUtils.isNotBlank(contratCollectifV6.getIdentifiantCollectivite())) {
        identifiantsCollectivite.add(contratCollectifV6.getIdentifiantCollectivite());
      }
      if (StringUtils.isNotBlank(contratCollectifV6.getGroupePopulation())) {
        groupesPopulation.add(contratCollectifV6.getGroupePopulation());
      }
    }
    if (StringUtils.isNotBlank(contract.getCritereSecondaireDetaille())) {
      portefeuille.add(contract.getCritereSecondaireDetaille());
    }

    ref.setIdentifiantsCollectivite(identifiantsCollectivite);
    ref.setGroupesPopulation(groupesPopulation);
    ref.setPortefeuille(portefeuille);

    dao.update(ref);
  }

  @ContinueSpan(log = "deleteByAmc ReferentielParametrageCarteTP")
  public long deleteByAmc(final String amc) {
    return dao.deleteByAmc(amc);
  }
}
