package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireBackendDao;
import com.cegedim.next.serviceeligibility.core.dao.ContractBackendDao;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.AssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.services.CalculDroitsTPPAUService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractHTPForPau;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.UniqueAccessPointUtil;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTriHTP;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("uniqueAccessPointServiceHTPV5")
public class UniqueAccessPointServiceV5HTPImpl extends UniqueAccessPointServiceHTPImpl {
  public UniqueAccessPointServiceV5HTPImpl(
      @Qualifier("uniqueAccessPointHtpTriV5") @Autowired
          UniqueAccessPointTriHTP uniqueAccessPointTriHTP,
      MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP,
      CalculDroitsTPPAUService calculDroitsTPPAUService,
      ContractBackendDao contractBackendDao,
      @Qualifier("beneficiaireHTPBackendDao") @Autowired
          BeneficiaireBackendDao beneficiaireHTPBackendDao) {
    this.uniqueAccessPointTriHTP = uniqueAccessPointTriHTP;
    this.mapperUniqueAccessPointServiceTP = mapperUniqueAccessPointServiceTP;
    this.calculDroitsTPPAUService = calculDroitsTPPAUService;
    this.contractBackendDao = contractBackendDao;
    this.beneficiaireHTPBackendDao = beneficiaireHTPBackendDao;
  }

  private UniqueAccessPointTriHTP uniqueAccessPointTriHTP;

  private MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP;

  private CalculDroitsTPPAUService calculDroitsTPPAUService;

  ContractBackendDao contractBackendDao;

  BeneficiaireBackendDao beneficiaireHTPBackendDao;

  protected void triHTP(final List<ContratAIV6> contrats, final UniqueAccessPointRequest requete) {
    this.uniqueAccessPointTriHTP.triHTP(contrats, requete);
  }

  protected List<BenefAIV5> findBenefFromRequest(final UniqueAccessPointRequest requete) {
    return this.beneficiaireHTPBackendDao.findBenefFromRequest(
        (UniqueAccessPointRequestV5) requete);
  }

  protected List<Right> getRightList(
      final UniqueAccessPointRequest requete,
      final DroitAssure droitAssure,
      final Periode periode,
      final String minDate,
      final String maxDate,
      final boolean force)
      throws CarenceException {
    final UniqueAccessPointRequestV5 castedRequest = (UniqueAccessPointRequestV5) requete;

    return this.mapperUniqueAccessPointServiceTP.mapFromExtendedRightsToRight(
        this.calculDroitsTPPAUService.calculDroitsTP(
            droitAssure, periode, minDate, maxDate, null, castedRequest),
        castedRequest,
        force);
  }

  protected List<ContratAIV6> getContractsForHTP(
      final UniqueAccessPointRequest requete, final List<BenefAIV5> benefs) {
    return this.contractBackendDao.findContractsForHTP(
        (UniqueAccessPointRequestV5) requete, benefs);
  }

  protected List<ContractHTPForPau> getContractsHTPForPau(
      final UniqueAccessPointRequest requete, final List<BenefAIV5> benefs) {
    UniqueAccessPointRequestV5 requestV5 = (UniqueAccessPointRequestV5) requete;
    List<ContratAIV6> contracts = this.contractBackendDao.findContractsForHTP(requestV5, benefs);
    if (Boolean.TRUE.equals(requestV5.getIsForced())) {
      List<ContractHTPForPau> filteredContracts = new ArrayList<>();
      Set<String> numeroPersonnes = UniqueAccessPointUtil.extractNumPersonnes(benefs);
      // Filter
      if (CollectionUtils.isNotEmpty(contracts)) {
        for (ContratAIV6 contract : contracts) {
          ContractHTPForPau contractHTPForPau = getContractHTPForPau(contract, numeroPersonnes);
          setRetour(requete, contractHTPForPau);
          filteredContracts.add(contractHTPForPau);
        }
      }
      return filteredContracts;
    }
    List<ContractHTPForPau> contractHTPForPaus = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(contracts)) {
      for (ContratAIV6 contract : contracts) {
        ContractHTPForPau contractHTPForPau = new ContractHTPForPau();
        contractHTPForPau.setContratAIV6(contract);
        contractHTPForPau.setRetour(0);
        contractHTPForPaus.add(contractHTPForPau);
      }
    }
    return contractHTPForPaus;
  }

  private static void setRetour(
      UniqueAccessPointRequest requete, ContractHTPForPau contractHTPForPau) {
    if (DateUtils.isOverlapping(
        requete.getStartDate(),
        requete.getEndDate(),
        contractHTPForPau.getPeriode().getDebut(),
        contractHTPForPau.getPeriode().getFin())) {
      contractHTPForPau.setRetour(0);
    } else if (requete.getEndDate() != null
        && DateUtils.before(requete.getEndDate(), contractHTPForPau.getPeriode().getDebut())) {
      // après
      contractHTPForPau.setRetour(1);
    } else if (DateUtils.before(contractHTPForPau.getPeriode().getFin(), requete.getStartDate())) {
      // avant
      contractHTPForPau.setRetour(2);
    }
  }

  private static ContractHTPForPau getContractHTPForPau(
      ContratAIV6 contract, Set<String> numeroPersonnes) {
    List<Assure> assures =
        contract.getAssures().stream()
            .filter(assure -> numeroPersonnes.contains(assure.getIdentite().getNumeroPersonne()))
            .toList();
    List<String> datesRadiation =
        assures.stream()
            .map(AssureCommun::getDateRadiation)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    List<DroitAssure> droitAssures =
        assures.stream().flatMap(assure -> assure.getDroits().stream()).toList();
    List<String> debutDroit =
        droitAssures.stream().map(droitAssure -> droitAssure.getPeriode().getDebut()).toList();
    String dateDebutGarantie = DateUtils.getMaxDate(debutDroit);
    String dateDebut =
        DateUtils.getMaxDate(List.of(contract.getDateSouscription(), dateDebutGarantie));

    List<String> finDroit =
        droitAssures.stream()
            .map(droitAssure -> droitAssure.getPeriode().getFin())
            .filter(Objects::nonNull)
            .toList();
    String dateFinGarantie = DateUtils.getMaxDate(finDroit);
    String dateRadiation = DateUtils.getMaxDate(datesRadiation);
    String dateFin =
        DateUtils.getMinDate(contract.getDateResiliation(), dateRadiation, dateFinGarantie);
    Periode periode = new Periode();
    periode.setDebut(dateDebut);
    periode.setFin(dateFin);
    ContractHTPForPau contractHTPForPau = new ContractHTPForPau();
    contractHTPForPau.setPeriode(periode);
    contractHTPForPau.setContratAIV6(contract);
    return contractHTPForPau;
  }

  @Override
  protected CollectiveContractV5 mapContratCollectif(final ContratCollectif contratCollectif) {
    final ContratCollectifV6 contratCollectifV6 = (ContratCollectifV6) contratCollectif;
    final CollectiveContractV5 collectiveContractV5 = new CollectiveContractV5();
    if (contratCollectifV6 != null) {
      collectiveContractV5.setNumber(contratCollectifV6.getNumero());
      collectiveContractV5.setExternalNumber(contratCollectifV6.getNumeroExterne());
      collectiveContractV5.setCompanyName(contratCollectifV6.getRaisonSociale());
    }
    return collectiveContractV5;
  }
}
