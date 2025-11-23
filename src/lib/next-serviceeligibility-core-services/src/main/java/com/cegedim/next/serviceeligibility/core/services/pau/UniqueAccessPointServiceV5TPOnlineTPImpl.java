package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireBackendDao;
import com.cegedim.next.serviceeligibility.core.dao.ContractBackendDao;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTPV5;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTpSortRights;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTpSortSubscriber;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTriTP;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointTPRequestV5;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("uniqueAccessPointServiceTpOnlineV5")
public class UniqueAccessPointServiceV5TPOnlineTPImpl extends UniqueAccessPointServiceTPImpl {

  private final BeneficiaireBackendDao beneficiaireTPOnlineBackendDao;

  private final ContractBackendDao contractBackendDao;

  /** public constructeur. */
  @Autowired
  public UniqueAccessPointServiceV5TPOnlineTPImpl(
      @Qualifier("beneficiaireTPOnlineBackendDao")
          final BeneficiaireBackendDao beneficiaireTPOfflineBackendDao,
      final ContractBackendDao contractBackendDao,
      final MapperUniqueAccessPointServiceTPV5 mapperUniqueAccessPointServiceTPV5,
      @Qualifier("uniqueAccessPointTpOnlineSortSubscriberV5Impl")
          final UniqueAccessPointTpSortSubscriber uniqueAccessPointTpSortSubscriberV5,
      @Qualifier("uniqueAccessPointTpOnlineSortRightsV5Impl")
          final UniqueAccessPointTpSortRights uniqueAccessPointTpSortRightsV5,
      @Qualifier("uniqueAccessPointTriV5TpOnline") @Autowired
          final UniqueAccessPointTriTP uniqueAccessPointTriTP,
      @Autowired IssuingCompanyCodeService issuingCompanyCodeService,
      final UAPForceService uapForceService) {
    super(
        uniqueAccessPointTpSortSubscriberV5,
        mapperUniqueAccessPointServiceTPV5,
        uniqueAccessPointTpSortRightsV5,
        uniqueAccessPointTriTP,
        issuingCompanyCodeService,
        uapForceService);
    this.contractBackendDao = contractBackendDao;
    this.beneficiaireTPOnlineBackendDao = beneficiaireTPOfflineBackendDao;
  }

  protected List<BenefAIV5> findBenefFromRequest(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        (UniqueAccessPointTPRequestV5) uniqueAccessPointTPRequest;
    final List<BenefAIV5> benefsOnline = new ArrayList<>();
    final UniqueAccessPointRequestV5 request = uniqueAccessPointTPRequestV5.getRequest();

    final String subscriberIdRequest = request.getSubscriberId();
    final String insurerId = request.getInsurerId();
    final String nirCodeRequest = request.getNirCode();

    if (StringUtils.isNotBlank(request.getBeneficiaryId())) {
      List<BenefAIV5> results = this.beneficiaireTPOnlineBackendDao.findBenefFromRequest(request);
      benefsOnline.addAll(results);
      uniqueAccessPointTPRequestV5.setFoundByNumAMCEchange(false);
    } else if (StringUtils.isNotBlank(subscriberIdRequest)
        || StringUtils.isNotBlank(nirCodeRequest)) {
      // Recherche via Nir OU NumAdherent pour couvrir toutes les iterations dans la
      // doc ainsi que par idDeclarant ou AMC échange pour les iterations 1 & 2 puis
      // les filtres via le java
      List<BenefAIV5> results = this.beneficiaireTPOnlineBackendDao.findBenefFromRequest(request);
      this.loadResults(benefsOnline, uniqueAccessPointTPRequestV5, insurerId, results);
    }
    return benefsOnline;
  }

  @Override
  protected boolean hasBeneficiaryIdInRequest(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        (UniqueAccessPointTPRequestV5) uniqueAccessPointTPRequest;
    return uniqueAccessPointTPRequestV5.getRequest() != null
        && StringUtils.isNotBlank(uniqueAccessPointTPRequestV5.getRequest().getBeneficiaryId());
  }

  @Override
  protected UniqueAccessPointTPRequest getUniqueAccessPointTPRequest() {
    return new UniqueAccessPointTPRequestV5();
  }

  @Override
  protected boolean isTpOffline() {
    return false;
  }

  @Override
  protected List<ContractTP> getContracts(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    return this.contractBackendDao.findContractsForTPOnline(
        (UniqueAccessPointTPRequestV5) uniqueAccessPointTPRequest);
  }

  @Override
  protected List<ContractTP> getFuturContractsForIssuingCompanyCode(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest, final boolean isTpOnline) {
    return this.contractBackendDao.findFuturContractsForOS(uniqueAccessPointTPRequest, isTpOnline);
  }

  @Override
  protected List<ContractTP> getPastContractsForIssuingCompanyCode(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest, final boolean isTpOnline) {
    return this.contractBackendDao.findPastContractsForOS(uniqueAccessPointTPRequest, isTpOnline);
  }
}
