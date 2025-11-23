package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class UniqueAccessPointTpSortSubscriberImpl implements UniqueAccessPointTpSortSubscriber {

  public void sort(List<ContractTP> contractTPS, UniqueAccessPointRequest request) {
    Comparator<ContractTP> comparator =
        (ContractTP contract1, ContractTP contract2) -> triNirOd1(contract1, contract2, request);
    contractTPS.sort(comparator);
  }

  private int triNirOd1(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    String nirCode = request.getNirCode();
    if (StringUtils.isNotBlank(nirCode)) {
      boolean isC1NirOd1 = isNirOd1(contractTP1, nirCode);
      boolean isC2NirOd2 = isNirOd1(contractTP2, nirCode);
      if (isC1NirOd1 && !isC2NirOd2) return -1;
      if (isC2NirOd2 && !isC1NirOd1) return 1;
    }
    return triNir(contractTP1, contractTP2, request);
  }

  private boolean isNirOd1(ContractTP contractTP, String nir) {
    return contractTP.getBeneficiaires().stream()
        .anyMatch(beneficiaireContract -> nir.equals(beneficiaireContract.getNirOd1()));
  }

  private int triNir(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    String nirCode = request.getNirCode();
    if (StringUtils.isNotBlank(nirCode)) {
      boolean isNirBenef1 = isNirBenef(contractTP1, nirCode);
      boolean isNirBenef2 = isNirBenef(contractTP2, nirCode);
      if (isNirBenef1 && !isNirBenef2) return -1;
      if (isNirBenef2 && !isNirBenef1) return 1;
    }
    return triOrdrePriorisation(contractTP1, contractTP2, request);
  }

  private boolean isNirBenef(ContractTP contractTP, String nir) {
    return contractTP.getBeneficiaires().stream()
        .anyMatch(beneficiaireContract -> nir.equals(beneficiaireContract.getNirBeneficiaire()));
  }

  private int triOrdrePriorisation(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    int res =
        StringUtils.compare(contractTP1.getOrdrePriorisation(), contractTP2.getOrdrePriorisation());
    if (res != 0) {
      return res;
    } else {
      return triAssurPrinc(contractTP1, contractTP2, request);
    }
  }

  private int triAssurPrinc(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    String nirCode = request.getNirCode();
    if (StringUtils.isNotBlank(nirCode)) {
      boolean isC1AssurePrincipal = isAssurePrincipal(contractTP1, nirCode);
      boolean isC2AssurePrincipal = isAssurePrincipal(contractTP2, nirCode);
      if (isC1AssurePrincipal && !isC2AssurePrincipal) return -1;
      if (isC2AssurePrincipal && !isC1AssurePrincipal) return 1;
    }
    return triContratColl(contractTP1, contractTP2, request);
  }

  private boolean isAssurePrincipal(ContractTP contractTP, String nir) {
    return contractTP.getBeneficiaires().stream()
        .anyMatch(
            beneficiaireContract ->
                (nir.equals(beneficiaireContract.getNirBeneficiaire())
                        || nir.equals(beneficiaireContract.getNirOd1()))
                    && "ASSURE".equals(beneficiaireContract.getAffiliation().getTypeAssure()));
  }

  private int triContratColl(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    String iscontract1ContratColl = contractTP1.getIndividuelOuCollectif();
    String iscontract2ContratColl = contractTP2.getIndividuelOuCollectif();
    if ("2".equals(iscontract1ContratColl) && "1".equals(iscontract2ContratColl)) return -1;
    if ("2".equals(iscontract2ContratColl) && "1".equals(iscontract1ContratColl)) return 1;
    return triNoemie(contractTP1, contractTP2, request);
  }

  private int triNoemie(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    boolean isC1Noemise = isNoemise(contractTP1);
    boolean isC2Noemise = isNoemise(contractTP2);
    if (isC1Noemise && !isC2Noemise) return -1;
    if (isC2Noemise && !isC1Noemise) return 1;
    return triPrioriteGarantie(contractTP1, contractTP2, request);
  }

  private boolean isNoemise(ContractTP contractTP) {
    return contractTP.getBeneficiaires().stream()
        .allMatch(
            beneficiaireContract ->
                beneficiaireContract.getAffiliation() != null
                    && beneficiaireContract.getAffiliation().getIsTeleTransmission());
  }

  private int triPrioriteGarantie(
      ContractTP contractTP1, ContractTP contractTP2, UniqueAccessPointRequest request) {
    String smallestGarantiePrioriteC1 = getSmallestGarantiePriorite(contractTP1, request);
    String smallestGarantiePrioriteC2 = getSmallestGarantiePriorite(contractTP2, request);
    int compare = StringUtils.compare(smallestGarantiePrioriteC1, smallestGarantiePrioriteC2);
    if (compare == 0) {
      return triNumContrat(contractTP1, contractTP2);
    }
    return compare;
  }

  String getSmallestGarantiePriorite(ContractTP contractTP, UniqueAccessPointRequest request) {
    List<PrioriteDroitContrat> prioriteDroitContratList =
        contractTP.getBeneficiaires().stream()
            .flatMap(beneficiaireContract -> beneficiaireContract.getDomaineDroits().stream())
            .filter(
                domaineDroitContract ->
                    CollectionUtils.isEmpty(request.getDomains())
                        || request.getDomains().contains(domaineDroitContract.getCode()))
            .flatMap(domaineDroitContract -> domaineDroitContract.getGaranties().stream())
            .flatMap(garantie -> garantie.getProduits().stream())
            .flatMap(produit -> produit.getReferencesCouverture().stream())
            .flatMap(referenceCouverture -> referenceCouverture.getNaturesPrestation().stream())
            .flatMap(naturePrestation -> naturePrestation.getPrioritesDroit().stream())
            .filter(prioriteDroitContrat -> isOverlappingPeriod(request, prioriteDroitContrat))
            .collect(Collectors.toList());
    Comparator<PrioriteDroitContrat> comparator =
        Comparator.comparing(PrioriteDroitContrat::getCode);
    if (CollectionUtils.isNotEmpty(prioriteDroitContratList)) {
      Optional<PrioriteDroitContrat> optional = prioriteDroitContratList.stream().min(comparator);
      return optional.map(PrioriteDroitContrat::getCode).orElse(null);
    } else {
      return null;
    }
  }

  private static boolean isOverlappingPeriod(
      UniqueAccessPointRequest request, PrioriteDroitContrat prioriteDroitContrat) {
    List<Periode> periodes = prioriteDroitContrat.getPeriodes();
    List<String> datesFin = periodes.stream().map(Periode::getFin).toList();
    String dateFin =
        datesFin.stream().filter(StringUtils::isNotBlank).max(String::compareTo).orElse(null);
    LocalDate localDateFin = null;

    List<String> datesDebut = periodes.stream().map(Periode::getDebut).toList();
    String dateDebut = datesDebut.stream().min(String::compareTo).orElse(null);
    LocalDate localDateDebut = null;
    if (dateDebut != null) {
      localDateDebut =
          LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
    }
    if (dateFin != null) {
      localDateFin =
          LocalDate.parse(dateFin, DateTimeFormatter.ofPattern(DateUtils.FORMATTERSLASHED));
    }
    LocalDate endDate = null;
    if (request.getEndDate() != null) {
      endDate = LocalDate.parse(request.getEndDate());
    }
    return DateUtils.isOverlapping(
        LocalDate.parse(request.getStartDate()), endDate, localDateDebut, localDateFin);
  }

  private int triNumContrat(ContractTP contractTP1, ContractTP contractTP2) {
    return contractTP1.getNumeroContrat().compareTo(contractTP2.getNumeroContrat());
  }
}
