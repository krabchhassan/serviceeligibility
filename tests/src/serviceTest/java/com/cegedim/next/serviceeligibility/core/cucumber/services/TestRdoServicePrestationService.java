package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.RDOGroup;
import com.cegedim.next.serviceeligibility.core.services.serviceprestationsrdo.RestServicePrestationsRdoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestRdoServicePrestationService {

  private final RestServicePrestationsRdoService restServicePrestationsRdoService;

  public void createRdoServicePrestation(RDOGroup body) {
    restServicePrestationsRdoService.createServicePrestationsRdo(body);
  }

  public void deleteRdoServicePrestation() {
    restServicePrestationsRdoService.deleteAll();
  }
}
