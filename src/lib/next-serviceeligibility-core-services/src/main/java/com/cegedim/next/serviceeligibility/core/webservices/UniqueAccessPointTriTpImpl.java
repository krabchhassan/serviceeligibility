package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class UniqueAccessPointTriTpImpl implements UniqueAccessPointTriTP {

  public void triTP(List<GenericRightDto> contrats, UniqueAccessPointRequest request) {
    Comparator<GenericRightDto> comparator =
        (GenericRightDto contract1, GenericRightDto contract2) ->
            triNirOd1(contract1, contract2, request);
    contrats.sort(comparator);
  }

  private int triNirOd1(
      GenericRightDto contract1, GenericRightDto contract2, UniqueAccessPointRequest request) {
    String nirCode = request.getNirCode();
    LocalDate startDate = DateUtils.stringToDate(request.getStartDate());
    LocalDate endDate = DateUtils.stringToDate(request.getEndDate());
    Identity id1 = contract1.getInsured().getIdentity();
    Identity id2 = contract2.getInsured().getIdentity();
    boolean iscontract1Affiliated = isAffiliated(id1, nirCode, startDate, endDate, true);
    boolean iscontract2Affiliated = isAffiliated(id2, nirCode, startDate, endDate, true);
    if (iscontract1Affiliated && !iscontract2Affiliated) return -1;
    if (iscontract2Affiliated && !iscontract1Affiliated) return 1;
    return triNir(contract1, contract2, request);
  }

  private boolean isAffiliated(
      Identity id, String nir, LocalDate startDate, LocalDate endDate, boolean nirOd1) {
    for (AffiliationRO aff : id.getAffiliationsRO()) {
      Period p = aff.getPeriod();
      LocalDate start = DateUtils.stringToDate(p.getStart());
      LocalDate end = DateUtils.stringToDate(p.getEnd());
      if (nirOd1) {
        if (DateUtils.isOverlapping(startDate, endDate, start, end)) {
          return aff.getNir().getCode().equals(nir);
        }
      } else if (id.getNir() != null && id.getNir().getCode() != null) {
        return id.getNir().getCode().equals(nir);
      }
    }

    return false;
  }

  private int triNir(
      GenericRightDto contract1, GenericRightDto contract2, UniqueAccessPointRequest request) {
    String nirCode = request.getNirCode();
    LocalDate startDate = DateUtils.stringToDate(request.getStartDate());
    LocalDate endDate = DateUtils.stringToDate(request.getEndDate());
    Identity id1 = contract1.getInsured().getIdentity();
    Identity id2 = contract2.getInsured().getIdentity();
    boolean iscontract1Affiliated = isAffiliated(id1, nirCode, startDate, endDate, false);
    boolean iscontract2Affiliated = isAffiliated(id2, nirCode, startDate, endDate, false);
    if (iscontract1Affiliated && !iscontract2Affiliated) return -1;
    if (iscontract2Affiliated && !iscontract1Affiliated) return 1;
    return triOrdrePriorisation(contract1, contract2, request);
  }

  private int triOrdrePriorisation(
      GenericRightDto contract1, GenericRightDto contract2, UniqueAccessPointRequest request) {
    int res =
        StringUtils.compare(contract1.getPrioritizationOrder(), contract2.getPrioritizationOrder());
    if (res != 0) {
      return res;
    } else {
      return triAssurPrincipal(contract1, contract2, request);
    }
  }

  private int triAssurPrincipal(
      GenericRightDto contract1, GenericRightDto contract2, UniqueAccessPointRequest request) {
    boolean iscontract1AssurePrincipal = isAssurePrincipal(contract1.getInsured());
    boolean iscontract2AssurePrincipal = isAssurePrincipal(contract2.getInsured());
    if (iscontract1AssurePrincipal && !iscontract2AssurePrincipal) return -1;
    if (iscontract2AssurePrincipal && !iscontract1AssurePrincipal) return 1;
    return triContratColl(contract1, contract2, request);
  }

  private boolean isAssurePrincipal(Insured insured) {
    if (insured.getQuality() != null) {
      return Constants.QUALITE_A.equals(insured.getQuality().getCode());
    }
    return false;
  }

  private int triContratColl(
      GenericRightDto contract1, GenericRightDto contract2, UniqueAccessPointRequest request) {
    boolean iscontract1ContratColl = !contract1.getIsIndividualContract();
    boolean iscontract2ContratColl = !contract2.getIsIndividualContract();
    if (iscontract1ContratColl && !iscontract2ContratColl) return -1;
    if (!iscontract1ContratColl && iscontract2ContratColl) return 1;
    return triNoemie(contract1, contract2, request);
  }

  private int triNoemie(
      GenericRightDto contract1, GenericRightDto contract2, UniqueAccessPointRequest request) {
    LocalDate startDate = DateUtils.stringToDate(request.getStartDate());
    LocalDate endDate = DateUtils.stringToDate(request.getEndDate());

    boolean iscontract1Noemise = isNoemise(contract1, startDate, endDate);
    boolean iscontract2Noemise = isNoemise(contract2, startDate, endDate);
    if (iscontract1Noemise && !iscontract2Noemise) return -1;
    if (iscontract2Noemise && !iscontract1Noemise) return 1;
    return triPrioriteGarantie(contract1, contract2, request);
  }

  private boolean isNoemise(GenericRightDto c, LocalDate startDate, LocalDate endDate) {
    if (c.getInsured().getDigitRelation() != null) {
      List<RemoteTransmission> cRemote = c.getInsured().getDigitRelation().getRemoteTransmissions();
      if (!CollectionUtils.isEmpty(cRemote)) {
        for (RemoteTransmission remote : cRemote) {
          Period p = remote.getPeriod();
          LocalDate start = DateUtils.stringToDate(p.getStart());
          LocalDate end = DateUtils.stringToDate(p.getEnd());
          if (Boolean.TRUE.equals(remote.getIsRemotelyTransmitted())
              && DateUtils.isOverlapping(startDate, endDate, start, end)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private int triPrioriteGarantie(
      GenericRightDto right1, GenericRightDto right2, UniqueAccessPointRequest request) {
    String smallestGarantiePrioritecontract1 = getSmallestGarantiePriorite(right1, request);
    String smallestGarantiePrioritecontract2 = getSmallestGarantiePriorite(right2, request);
    int compare =
        StringUtils.compare(smallestGarantiePrioritecontract1, smallestGarantiePrioritecontract2);
    if (compare == 0) {
      return triNumContrat(right1, right2);
    }
    return compare;
  }

  String getSmallestGarantiePriorite(GenericRightDto right, UniqueAccessPointRequest request) {
    List<Right> rightV4s =
        right.getInsured().getRights().stream()
            .filter(
                domaineDroitContract ->
                    CollectionUtils.isEmpty(request.getDomains())
                        || request.getDomains().contains(domaineDroitContract.getCode()))
            .toList();
    Comparator<Right> comparator = Comparator.comparing(Right::getPrioritizationOrder);
    List<Right> filterRigths =
        rightV4s.stream().filter(rightV4 -> rightV4.getPrioritizationOrder() != null).toList();
    Optional<Right> optional = filterRigths.stream().min(comparator);
    return optional.map(Right::getPrioritizationOrder).orElse(null);
  }

  private int triNumContrat(GenericRightDto contract1, GenericRightDto contract2) {
    return contract1.getNumber().compareTo(contract2.getNumber());
  }
}
