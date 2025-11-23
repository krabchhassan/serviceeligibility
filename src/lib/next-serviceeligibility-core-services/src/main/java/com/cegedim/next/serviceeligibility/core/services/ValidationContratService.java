package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.stereotype.Service;

@Service
public class ValidationContratService extends AbstractValidationContratService {

  private final OrganisationServiceWrapper organisationServiceWrapper;

  public ValidationContratService(
      OrganisationServiceWrapper organisationServiceWrapper,
      ContractElementService contractElementService,
      ReferentialService referentialService,
      EventService eventService,
      BeyondPropertiesService beyondPropertiesService) {
    super(contractElementService, referentialService, eventService, beyondPropertiesService);
    this.organisationServiceWrapper = organisationServiceWrapper;
  }

  @ContinueSpan(log = "callOrga")
  public void callOrga(ContratAIV6 contract, ContractValidationBean contractValidationBean) {
    fillContractInfoInValidationBean(contract, contractValidationBean);

    try {
      Organisation org =
          organisationServiceWrapper.getOrganizationByAmcNumber(contract.getIdDeclarant());
      if (org != null && org.isMain()) {
        if (!organisationServiceWrapper.isOrgaAttached(
            org.getCode(), contract.getSocieteEmettrice())) {
          generateErrorContrat(
              "Impossible de déterminer une organisation secondaire pour l’organisation "
                  + org.getCode()
                  + " et ayant le n° d’AMC "
                  + contract.getSocieteEmettrice(),
              contractValidationBean);
        }
      } else if (org != null && !org.isMain()) {
        if (contract.getIdDeclarant().equals(contract.getSocieteEmettrice())) {
          contract.setCodeOc(org.getCode());
        } else {
          generateErrorContrat(
              "Incohérence entre l’idDeclarant "
                  + contract.getIdDeclarant()
                  + " et la societeEmettrice "
                  + contract.getSocieteEmettrice(),
              contractValidationBean);
        }
      } else {
        generateErrorContrat(
            "Impossible de déterminer l'organisation liée à l'AMC n°" + contract.getIdDeclarant(),
            contractValidationBean);
      }
    } catch (Exception e) {
      generateErrorContrat(
          "Impossible de déterminer l'organisation liée à l'AMC n°" + contract.getIdDeclarant(),
          contractValidationBean);
    }
  }
}
