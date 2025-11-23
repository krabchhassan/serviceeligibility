package com.cegedim.next.serviceeligibility.consolidationcontract.services;

import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractHistoService {

  private final ContractDao contractDao;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  public boolean isSameContractFlux(Buffer buffer, Declaration declaration) {
    if (buffer == null || buffer.contract == null) {
      return false;
    }

    return StringUtils.equals(buffer.contract.getIdDeclarant(), declaration.getIdDeclarant())
        && StringUtils.equals(
            buffer.contract.getNumeroContrat(), declaration.getContrat().getNumero())
        && StringUtils.equals(
            buffer.contract.getNumeroAdherent(), declaration.getContrat().getNumeroAdherent())
        && StringUtils.equals(buffer.fluxKey, getFluxKey(declaration));
  }

  public Buffer getCurrentContract(
      Buffer previousContract, Declaration declaration, String collection) {
    if (isSameContractFlux(previousContract, declaration)) {
      return previousContract;
    }

    return new Buffer(
        contractDao.getContract(
            declaration.getIdDeclarant(),
            declaration.getContrat().getNumero(),
            declaration.getContrat().getNumeroAdherent(),
            collection),
        getFluxKey(declaration));
  }

  public void saveToElastic(ContractTP contract) {
    if (contract != null) {
      elasticHistorisationContractService.putContractHistoryOnElastic(contract);
      log.debug(
          "{} - {} - {} saved to elastic",
          contract.getIdDeclarant(),
          contract.getNumeroContrat(),
          contract.getNumeroAdherent());
    }
  }

  public void saveContractsToElastic(List<ContractTP> contracts) {
    elasticHistorisationContractService.putContractsHistoryOnElastic(contracts);
  }

  public String getFluxKey(Declaration declaration) {
    return declaration.getNomFichierOrigine() != null
        ? declaration.getNomFichierOrigine()
        : declaration.getUserModification();
  }

  public String getHistoKey(Declaration declaration) {
    return declaration.getIdDeclarant()
        + declaration.getContrat().getNumero()
        + declaration.getContrat().getNumeroAdherent();
  }

  @Data
  @RequiredArgsConstructor
  public static class Buffer {
    private final ContractTP contract;
    private final String fluxKey;
    private boolean created = false;
    private boolean updated = false;
  }
}
