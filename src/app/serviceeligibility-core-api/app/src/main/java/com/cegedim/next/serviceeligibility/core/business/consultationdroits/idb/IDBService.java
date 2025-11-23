package com.cegedim.next.serviceeligibility.core.business.consultationdroits.idb;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service("IDBService")
@RequiredArgsConstructor
public class IDBService {

  private final IDBSteps idbSteps;

  @ContinueSpan(log = "getPeriodesAndContractNumForIDB")
  public Pair<String, Periode> getPeriodesAndContractNumForIDB(
      List<ContractDto> contractList, Date dateDebutInterro, Date dateFinInterro) {
    Map<String, Map<String, List<Periode>>> periodesByContract =
        idbSteps.getPeriodesByContract(contractList);
    List<Pair<String, Periode>> periodesWithContrat = new ArrayList<>();
    for (Map.Entry<String, Map<String, List<Periode>>> contractMapEntry :
        periodesByContract.entrySet()) {
      List<Periode> periodes = new ArrayList<>();
      for (Map.Entry<String, List<Periode>> entry : contractMapEntry.getValue().entrySet()) {
        periodes.addAll(entry.getValue());
      }
      DateUtils.getPeriodesFusionnees(
              periodes, DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED))
          .forEach(periode -> periodesWithContrat.add(Pair.of(contractMapEntry.getKey(), periode)));
    }

    Pair<String, Periode> selectedContractPeriod = null;
    if (CollectionUtils.isNotEmpty(periodesWithContrat)) {
      selectedContractPeriod =
          idbSteps.selectContractAndPeriodToReturn(
              periodesWithContrat,
              DateUtils.formatDate(dateDebutInterro),
              DateUtils.formatDate(dateFinInterro));
      if (selectedContractPeriod != null) {
        // Tronquer la periode selon la dateDebut et Fin d'interrogation
        idbSteps.truncatePeriode(
            selectedContractPeriod.getSecond(), dateDebutInterro, dateFinInterro);
      }
    }
    return selectedContractPeriod;
  }

  /**
   * Retourne une map de liste de contrats par bénéficiaire
   *
   * @param listeContracts la liste des contrats a regrouper
   * @return une map contenant une liste de déclaration par benef
   */
  public Map<String, List<ContractDto>> getContractsByBeneficiaire(
      final List<ContractDto> listeContracts) {
    Map<String, List<ContractDto>> mapBenef = new HashMap<>();
    for (ContractDto contractDto : listeContracts) {
      String key = getKeyDeclaration(contractDto);
      List<ContractDto> listeBenef = mapBenef.get(key);
      if (listeBenef == null) {
        listeBenef = new ArrayList<>();
      }
      listeBenef.add(contractDto);
      mapBenef.put(key, listeBenef);
    }
    return mapBenef;
  }

  private static String getKeyDeclaration(final ContractDto contractDto) {
    return contractDto.getBeneficiaire().getNirOd1()
        + "*"
        + contractDto.getBeneficiaire().getCleNirOd1()
        + "*"
        + contractDto.getBeneficiaire().getDateNaissance()
        + "*"
        + contractDto.getBeneficiaire().getRangNaissance();
  }
}
