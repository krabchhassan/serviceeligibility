package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddResponseDto;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddRequest;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MapperConsultationDroit {
  private MapperConsultationDroit() {
    throw new IllegalStateException("Utility class");
  }

  public static ResponseEntity<GetInfoBddResponseDto> getResponse(GetInfoBddResponse response) {
    GetInfoBddResponseDto responseDto = new GetInfoBddResponseDto();
    responseDto.setCodeReponse(response.getCodeReponse());
    responseDto.setDroits(response.getDeclarations());
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  public static GetInfoBddRequest getRequest(GetInfoBddRequestDto requestDto) {
    GetInfoBddRequest request = new GetInfoBddRequest();
    request.setAmc(requestDto.getAmc());
    request.setBeneficiaire(requestDto.getBeneficiaire());
    request.setDates(requestDto.getDates());
    request.setInfoBdd(requestDto.getInfoBdd());
    return request;
  }
}
