package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.organisation.dto.OrganizationDto;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.AbstractValidationContratService;
import com.cegedim.next.serviceeligibility.core.services.ReferentialService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationRdoService extends AbstractValidationContratService {

  private final OrganizationService organizationService;

  public ValidationRdoService(
      OrganizationService organizationService,
      ContractElementService contractElementService,
      ReferentialService referentialService,
      EventService eventService,
      BeyondPropertiesService beyondPropertiesService) {
    super(contractElementService, referentialService, eventService, beyondPropertiesService);
    this.organizationService = organizationService;
  }

  @Override
  public void callOrga(ContratAIV6 contract, ContractValidationBean contractValidationBean) {
    fillContractInfoInValidationBean(contract, contractValidationBean);

    try {
      OrganizationDto org =
          organizationService.getOrganizationByAmcNumber(contract.getIdDeclarant());
      if (org != null && org.getIsMainType()) {
        if (!organizationService.isOrgaAttached(org.getCode(), contract.getSocieteEmettrice())) {
          generateErrorContrat(
              "Impossible de déterminer une organisation secondaire pour l’organisation "
                  + org.getCode()
                  + " et ayant le n° d’AMC "
                  + contract.getSocieteEmettrice(),
              contractValidationBean);
        }
      } else if (org != null && !org.getIsMainType()) {
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
