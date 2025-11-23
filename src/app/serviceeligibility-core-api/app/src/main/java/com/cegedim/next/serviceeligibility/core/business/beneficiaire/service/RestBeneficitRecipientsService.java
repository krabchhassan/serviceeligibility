package com.cegedim.next.serviceeligibility.core.business.beneficiaire.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.BenefitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.RequestBeneficitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireHTPBackendDaoImpl;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinataireRelevePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PeriodeDestinataire;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestBeneficitRecipientsService {

  @Autowired private BeneficiaireHTPBackendDaoImpl beneficiaireDao;

  @ContinueSpan(log = "getBenefitRecipients")
  public List<BenefitRecipientsDto> getBenefitRecipients(
      RequestBeneficitRecipientsDto request, BenefAIV5 benef) {
    List<BenefitRecipientsDto> benefitRecipientsDtoList = new ArrayList<>();
    extractBenefitRecipient(request, benefitRecipientsDtoList, benef);
    return benefitRecipientsDtoList;
  }

  @ContinueSpan(log = "getBenefForBenefitRecipients")
  public BenefAIV5 getBenefForBenefitRecipients(RequestBeneficitRecipientsDto request) {
    return beneficiaireDao.findBenefitRecipients(
        request.getIdPerson(),
        request.getSubscriberId(),
        request.getContractNumber(),
        request.getInsurerId());
  }

  private static void extractBenefitRecipient(
      RequestBeneficitRecipientsDto request,
      List<BenefitRecipientsDto> benefitRecipientsDtoList,
      BenefAIV5 benef) {
    Optional<ContratV5> contratV5Optional =
        benef.getContrats().stream()
            .filter(contratV5 -> request.getContractNumber().equals(contratV5.getNumeroContrat()))
            .findFirst();

    if (contratV5Optional.isPresent()) {
      DataAssure dataAssureV5 = contratV5Optional.get().getData();
      if (dataAssureV5 != null) {
        List<DestinataireRelevePrestations> destinatairesRelevePrestations =
            dataAssureV5.getDestinatairesRelevePrestations();
        if (CollectionUtils.isNotEmpty(destinatairesRelevePrestations))
          createBenefitRecipientDto(benefitRecipientsDtoList, destinatairesRelevePrestations);
        Collections.sort(benefitRecipientsDtoList, Collections.reverseOrder());
      }
    }
  }

  private static void createBenefitRecipientDto(
      List<BenefitRecipientsDto> benefitRecipientsDtoList,
      List<DestinataireRelevePrestations> destinatairesRelevePrestations) {
    for (DestinataireRelevePrestations destinataireRelevePrestationsV5 :
        destinatairesRelevePrestations) {
      BenefitRecipientsDto dto = new BenefitRecipientsDto();
      dto.setIdBeyond(destinataireRelevePrestationsV5.getIdBeyondDestinataireRelevePrestations());
      PeriodeDestinataire periode = destinataireRelevePrestationsV5.getPeriode();
      if (periode != null) {
        dto.setValidityStartDate(periode.getDebut());
        dto.setValidityEndDate(periode.getFin());
      }
      benefitRecipientsDtoList.add(dto);
    }
  }
}
