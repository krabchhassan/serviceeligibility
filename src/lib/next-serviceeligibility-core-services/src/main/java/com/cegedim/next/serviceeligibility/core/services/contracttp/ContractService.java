package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.services.pojo.BillingResult;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ContractService {
  private final ContractDao contractDao;

  @ContinueSpan(log = "deleteContractByAmc")
  public long deleteContractByAmc(final String idDeclarant) {
    return contractDao.deleteContractByAmc(idDeclarant);
  }

  /**
   * Utilisé dans le batch de purge. Supprime un bénéficiaire d'un contrat et renvoie un code en
   * fonction du résultat ; 1 si le contrat a été modifié, 0 si le contrat a été supprimé, et -1 si
   * le contrat n'a pas été trouvé en base
   *
   * @param idDeclarant AMC à laquelle le bénéficiaire est rattaché
   * @param numeroContrat Numéro du contrat recherché
   * @param numeroAdherent Numéro de l'adhérent recherché
   * @param numeroPersonne Numéro du bénéficiaire a supprimer
   * @return 1 si le contrat a été modifié, 0 si le contrat a été supprimé, et -1 si le contrat n'a
   *     pas été trouvé en base
   */
  @ContinueSpan(log = "deleteBenefFromContract")
  public int deleteBenefFromContract(
      String idDeclarant, String numeroContrat, String numeroAdherent, String numeroPersonne) {
    ContractTP contractTP = contractDao.getContract(idDeclarant, numeroContrat, numeroAdherent);

    if (ObjectUtils.isEmpty(contractTP)) {
      return -1;
    }

    // Récupération de la liste des bénéficiaires du contrat en enlevant celui
    // recherché
    List<BeneficiaireContractTP> filteredBenefList =
        contractTP.getBeneficiaires().stream()
            .filter(benef -> !benef.getNumeroPersonne().equals(numeroPersonne))
            .toList();

    // Si le contrat n'a plus de bénéficiaire, on supprime le contrat
    if (CollectionUtils.isEmpty(filteredBenefList)) {
      contractDao.deleteContract(contractTP.get_id());
      return 0;
    }

    // Si il reste au moins un bénéficiaire dans le contrat, on n'enlève que le
    // bénéficiaire
    contractTP.setBeneficiaires(filteredBenefList);
    contractDao.saveContract(contractTP);
    return 1;
  }

  @ContinueSpan(log = "getContractsForBillingJob")
  public List<BillingResult> getContractsForBillingJob(LocalDate date) {
    return contractDao.getContractsForBillingJob(date);
  }

  @ContinueSpan(log = "getContractsForBillingOTPJob")
  public List<BillingResult> getContractsForBillingOTPJob(LocalDate date, String amc) {
    return contractDao.getContractsForBillingOTPJob(date, amc);
  }

  @ContinueSpan(log = "getContractsByAmcAndPersonNumber")
  public List<ContractTP> getContractsByAmcAndPersonNumber(String amc, String personNumber) {
    return contractDao.findBy(amc, personNumber);
  }

  @ContinueSpan(log = "getContract")
  public ContractTP getContract(String idDeclarant, String numeroContrat, String numeroAdherent) {
    return contractDao.getContract(idDeclarant, numeroContrat, numeroAdherent);
  }
}
