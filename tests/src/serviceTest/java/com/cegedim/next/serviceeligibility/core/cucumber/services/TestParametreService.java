package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.utils.ParametreControllerUtils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestParametreService {

  private final ParametreBddService parametreBddService;

  public void insertV2(String type, String json) throws IOException {
    ParametresDto parametresDto = ParametreControllerUtils.prepareInData(type, json);
    parametreBddService.insert(type, parametresDto);
  }

  public ParametresDto get(String type, String code) {
    return parametreBddService.findOneByType(type, code);
  }
}
