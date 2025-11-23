package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.elast.contract.ContratElastic;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class TestHistoriqueContratService {

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  public void deleteDataIndexHistoContratForNumberContrat(@NonNull String numeroContrat) {
    List<ContratElastic> contratElastics =
        elasticHistorisationContractService.findByNumeroContrat(numeroContrat);
    if (!CollectionUtils.isEmpty(contratElastics)) {
      elasticHistorisationContractService.deleteHistoContrats(contratElastics);
    }
  }

  public List<ContratElastic> getIndexHistoContratByNumberContrat(@NonNull String numeroContrat) {
    return elasticHistorisationContractService.findByNumeroContrat(numeroContrat);
  }
}
