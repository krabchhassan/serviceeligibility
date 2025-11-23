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

@Service("uniqueAccessPointServiceTpOfflineV5")
public class UniqueAccessPointServiceV5TPOfflineTPImpl extends UniqueAccessPointServiceTPImpl {

  private final BeneficiaireBackendDao beneficiaireTPOfflineBackendDao;

  private final ContractBackendDao contractBackendDao;

  /** public constructeur. */
  @Autowired
  public UniqueAccessPointServiceV5TPOfflineTPImpl(
      @Qualifier("beneficiaireTPOfflineBackendDao")
          final BeneficiaireBackendDao beneficiaireTPOfflineBackendDao,
      final ContractBackendDao contractBackendDao,
      final MapperUniqueAccessPointServiceTPV5 mapperUniqueAccessPointServiceTPV5,
      @Qualifier("uniqueAccessPointTpOfflineSortSubscriberV5Impl")
          final UniqueAccessPointTpSortSubscriber uniqueAccessPointTpSortSubscriberV5,
      @Qualifier("uniqueAccessPointTpOnlineSortRightsV5Impl")
          final UniqueAccessPointTpSortRights uniqueAccessPointTpSortRightsV5,
      @Qualifier("uniqueAccessPointTriV5TpOffline")
          final UniqueAccessPointTriTP uniqueAccessPointTriTP,
      final IssuingCompanyCodeService issuingCompanyCodeService,
      final UAPForceService uapForceService) {
    super(
        uniqueAccessPointTpSortSubscriberV5,
        mapperUniqueAccessPointServiceTPV5,
        uniqueAccessPointTpSortRightsV5,
        uniqueAccessPointTriTP,
        issuingCompanyCodeService,
        uapForceService);
    this.contractBackendDao = contractBackendDao;
    this.beneficiaireTPOfflineBackendDao = beneficiaireTPOfflineBackendDao;
  }

  @Override
  protected List<BenefAIV5> findBenefFromRequest(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    final List<BenefAIV5> benefsOffline = new ArrayList<>();
    final UniqueAccessPointTPRequestV5 uniqueAccessPointTPRequestV5 =
        (UniqueAccessPointTPRequestV5) uniqueAccessPointTPRequest;

    final UniqueAccessPointRequestV5 request = uniqueAccessPointTPRequestV5.getRequest();

    final String subscriberIdRequest = request.getSubscriberId();
    final String nirCodeRequest = request.getNirCode();
    final String insurerId = request.getInsurerId();

    if (StringUtils.isNotBlank(request.getBeneficiaryId())) {
      final List<BenefAIV5> results =
          this.beneficiaireTPOfflineBackendDao.findBenefFromRequest(request);
      benefsOffline.addAll(results);
      uniqueAccessPointTPRequestV5.setFoundByNumAMCEchange(false);
    } else if (StringUtils.isNotBlank(subscriberIdRequest)) {
      // Iterations 1 & 2
      final List<BenefAIV5> results =
          this.beneficiaireTPOfflineBackendDao.findBenefFromRequest(request);
      if (!results.isEmpty()) {
        this.loadResults(benefsOffline, uniqueAccessPointTPRequestV5, insurerId, results);
      } else {
        // Iteration 3 & 4
        request.setNirCode(null);
        final List<BenefAIV5> results2 =
            this.beneficiaireTPOfflineBackendDao.findBenefFromRequest(request);
        request.setNirCode(nirCodeRequest);
        if (!results2.isEmpty()) {
          this.loadResults(benefsOffline, uniqueAccessPointTPRequestV5, insurerId, results2);
        } else if (StringUtils.isNotBlank(nirCodeRequest)) {
          // Iterations 5 & 6
          request.setSubscriberId(null);
          final List<BenefAIV5> results3 =
              this.beneficiaireTPOfflineBackendDao.findBenefFromRequest(request);
          request.setSubscriberId(subscriberIdRequest);
          this.loadResults(benefsOffline, uniqueAccessPointTPRequestV5, insurerId, results3);
        }
      }
    } else if (StringUtils.isNotBlank(nirCodeRequest)) {
      // Iterations 5 & 6
      final List<BenefAIV5> results3 =
          this.beneficiaireTPOfflineBackendDao.findBenefFromRequest(request);
      this.loadResults(benefsOffline, uniqueAccessPointTPRequestV5, insurerId, results3);
    }
    return benefsOffline;
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
  protected boolean isTpOffline() {
    return true;
  }

  @Override
  protected List<ContractTP> getContracts(
      final UniqueAccessPointTPRequest uniqueAccessPointTPRequest) {
    return this.contractBackendDao.findContractsForTPOffline(
        (UniqueAccessPointTPRequestV5) uniqueAccessPointTPRequest);
  }

  @Override
  protected UniqueAccessPointTPRequest getUniqueAccessPointTPRequest() {
    return new UniqueAccessPointTPRequestV5();
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
