package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OcService {

  private final OrganisationServiceWrapper organisationServiceWrapper;

  @Cacheable(value = "ocCache", key = "#amc")
  @ContinueSpan(log = "getOC")
  public Oc getOC(String amc) {
    return getOCFromCall(amc);
  }

  private Oc getOCFromCall(String amc) {
    // Récupération de l'OC
    Organisation ocs = callOC(amc);
    if (ocs != null) {
      Oc oc = new Oc();
      oc.setCode(ocs.getCode());
      oc.setLibelle(ocs.getFullName());
      return oc;
    }
    return null;
  }

  private Organisation callOC(String amc) {
    return organisationServiceWrapper.getOrganizationByAmcNumber(amc);
  }

  @Cacheable(value = "ocCodeCache", key = "#code")
  @ContinueSpan(log = "getOCByCode")
  public Oc getOCByCode(String code) {

    // Récupération des OCs
    Organisation organisation = callOCByCode(code);
    if (organisation != null) {
      Oc oc = new Oc();
      oc.setCode(organisation.getCode());
      oc.setLibelle(organisation.getFullName());
      return oc;
    }
    return null;
  }

  private Organisation callOCByCode(String code) {
    return organisationServiceWrapper.getOrganisationByCode(code);
  }
}
