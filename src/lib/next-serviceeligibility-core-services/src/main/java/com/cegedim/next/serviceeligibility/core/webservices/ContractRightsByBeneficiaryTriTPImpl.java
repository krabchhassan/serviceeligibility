package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ContractRightsByBeneficiaryTriTPImpl implements UniqueAccessPointTriTP {

  public void triTP(List<GenericRightDto> contrats, UniqueAccessPointRequest request) {
    Comparator<GenericRightDto> comparator =
        (GenericRightDto contract1, GenericRightDto contract2) ->
            triNirOd1(contract1, contract2, request.getNirCode());
    contrats.sort(comparator);
  }

  private int triNirOd1(GenericRightDto contract1, GenericRightDto contract2, String nirCode) {
    Identity id1 = contract1.getInsured().getIdentity();
    Identity id2 = contract2.getInsured().getIdentity();
    boolean iscontract1Affiliated = isAffiliated(id1, nirCode, true);
    boolean iscontract2Affiliated = isAffiliated(id2, nirCode, true);
    if (iscontract1Affiliated && !iscontract2Affiliated) return -1;
    if (iscontract2Affiliated && !iscontract1Affiliated) return 1;
    return triNir(contract1, contract2, nirCode);
  }

  private boolean isAffiliated(Identity id, String nir, boolean nirOd1) {
    for (AffiliationRO aff : id.getAffiliationsRO()) {
      Period p = aff.getPeriod();
      LocalDate start = DateUtils.stringToDate(p.getStart());
      LocalDate end = DateUtils.stringToDate(p.getEnd());
      if (nirOd1) {
        if (DateUtils.isPeriodeValide(start, end)) {
          return aff.getNir().getCode().equals(nir);
        }
      } else if (id.getNir() != null && id.getNir().getCode() != null) {
        return id.getNir().getCode().equals(nir);
      }
    }

    return false;
  }

  private int triNir(GenericRightDto contract1, GenericRightDto contract2, String nirCode) {
    Identity id1 = contract1.getInsured().getIdentity();
    Identity id2 = contract2.getInsured().getIdentity();
    boolean iscontract1Affiliated = isAffiliated(id1, nirCode, false);
    boolean iscontract2Affiliated = isAffiliated(id2, nirCode, false);
    if (iscontract1Affiliated && !iscontract2Affiliated) return -1;
    if (iscontract2Affiliated && !iscontract1Affiliated) return 1;
    return triOrdrePriorisation(contract1, contract2);
  }

  private int triOrdrePriorisation(GenericRightDto contract1, GenericRightDto contract2) {
    int res =
        StringUtils.compare(contract1.getPrioritizationOrder(), contract2.getPrioritizationOrder());
    if (res != 0) {
      return res;
    } else {
      return triAssurPrincipal(contract1, contract2);
    }
  }

  private int triAssurPrincipal(GenericRightDto contract1, GenericRightDto contract2) {
    boolean iscontract1AssurePrincipal = contract1.getInsured().getIsSubscriber();
    boolean iscontract2AssurePrincipal = contract2.getInsured().getIsSubscriber();
    if (iscontract1AssurePrincipal && !iscontract2AssurePrincipal) return -1;
    if (iscontract2AssurePrincipal && !iscontract1AssurePrincipal) return 1;
    return triContratColl(contract1, contract2);
  }

  private int triContratColl(GenericRightDto contract1, GenericRightDto contract2) {
    boolean iscontract1ContratColl =
        contract1.getCollectiveContract() != null
            && StringUtils.isNotBlank(contract1.getCollectiveContract().getNumber());
    boolean iscontract2ContratColl =
        contract2.getCollectiveContract() != null
            && StringUtils.isNotBlank(contract2.getCollectiveContract().getNumber());
    if (iscontract1ContratColl && !iscontract2ContratColl) return -1;
    if (!iscontract1ContratColl && iscontract2ContratColl) return 1;
    return triNoemie(contract1, contract2);
  }

  private int triNoemie(GenericRightDto contract1, GenericRightDto contract2) {
    boolean iscontract1Noemise = isNoemise(contract1);
    boolean iscontract2Noemise = isNoemise(contract2);
    if (iscontract1Noemise && !iscontract2Noemise) return -1;
    if (iscontract2Noemise && !iscontract1Noemise) return 1;
    return triPrioriteGarantie(contract1, contract2);
  }

  private boolean isNoemise(GenericRightDto c) {
    if (c.getInsured().getDigitRelation() != null) {
      List<RemoteTransmission> cRemote = c.getInsured().getDigitRelation().getRemoteTransmissions();
      if (!CollectionUtils.isEmpty(cRemote)) {
        for (RemoteTransmission remote : cRemote) {
          Period p = remote.getPeriod();
          LocalDate start = DateUtils.stringToDate(p.getStart());
          LocalDate end = DateUtils.stringToDate(p.getEnd());
          if (Boolean.TRUE.equals(remote.getIsRemotelyTransmitted())
              && DateUtils.isPeriodeValide(start, end)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private int triPrioriteGarantie(GenericRightDto right1, GenericRightDto right2) {
    String smallestGarantiePrioritecontract1 = getSmallestGarantiePriorite(right1);
    String smallestGarantiePrioritecontract2 = getSmallestGarantiePriorite(right2);
    int compare =
        StringUtils.compare(smallestGarantiePrioritecontract1, smallestGarantiePrioritecontract2);
    if (compare == 0) {
      return triNumContrat(right1, right2);
    }
    return compare;
  }

  String getSmallestGarantiePriorite(GenericRightDto right) {
    List<Right> rights =
        right.getInsured().getRights().stream()
            .filter(r -> r.getPrioritizationOrder() != null)
            .toList();

    return rights.stream()
        .min(Comparator.comparing(Right::getPrioritizationOrder))
        .map(Right::getPrioritizationOrder)
        .orElse(null);
  }

  private int triNumContrat(GenericRightDto contract1, GenericRightDto contract2) {
    return contract1.getNumber().compareTo(contract2.getNumber());
  }
}
