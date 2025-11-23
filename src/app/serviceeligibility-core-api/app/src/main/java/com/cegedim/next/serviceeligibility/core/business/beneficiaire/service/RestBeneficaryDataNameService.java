package com.cegedim.next.serviceeligibility.core.business.beneficiaire.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname.BeneficiaryDataNameDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname.RequestBeneficiaryDataNameDto;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireHTPBackendDaoImpl;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestBeneficaryDataNameService {

  @Autowired private BeneficiaireHTPBackendDaoImpl beneficiaireDao;

  public BeneficiaryDataNameDto getBeneficiaryDataName(
      RequestBeneficiaryDataNameDto request, BenefAIV5 benef) {
    return extractBeneficiaryDataName(request, benef);
  }

  public BenefAIV5 getBenefForBeneficiaryDataName(RequestBeneficiaryDataNameDto request) {
    return beneficiaireDao.findBeneficiaryDataName(
        request.getPersonNumber(), request.getContractNumber());
  }

  private BeneficiaryDataNameDto extractBeneficiaryDataName(
      RequestBeneficiaryDataNameDto request, BenefAIV5 benef) {
    Optional<ContratV5> contratV5Optional =
        benef.getContrats().stream()
            .filter(contratV5 -> request.getContractNumber().equals(contratV5.getNumeroContrat()))
            .findFirst();

    if (contratV5Optional.isPresent()) {
      DataAssure dataAssureV5 = contratV5Optional.get().getData();
      if (dataAssureV5 != null) {
        NomAssure nom = dataAssureV5.getNom();
        if (nom != null) {
          return createBeneficiaryDataNameDto(nom);
        }
      }
    }
    return null;
  }

  private BeneficiaryDataNameDto createBeneficiaryDataNameDto(NomAssure nom) {
    BeneficiaryDataNameDto dto = new BeneficiaryDataNameDto();
    dto.setLastName(nom.getNomFamille());
    dto.setCommonName(nom.getNomUsage());
    dto.setFirstName(nom.getPrenom());
    dto.setCivility(nom.getCivilite());
    return dto;
  }
}
