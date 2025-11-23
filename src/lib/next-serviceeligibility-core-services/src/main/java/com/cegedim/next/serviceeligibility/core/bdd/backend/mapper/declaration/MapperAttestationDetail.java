package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.AttestationDetailDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.ConventionDto;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperAttestationDetail {

  @Autowired private MapperConvention mapperConvention;

  public DomaineDroit dtoToEntity() {
    return null;
  }

  public AttestationDetailDto entityToDto(DomaineCarte domaineCarte) {
    AttestationDetailDto attestationDetail = new AttestationDetailDto();
    attestationDetail.setCodeDomaine(domaineCarte.getCode());
    attestationDetail.setNumOrdreEdition(domaineCarte.getRang());
    attestationDetail.setTaux(domaineCarte.getTaux());
    attestationDetail.setUniteTaux(domaineCarte.getUnite());
    attestationDetail.setCodeRenvois(domaineCarte.getCodeRenvoi());
    attestationDetail.setLibelleRenvois(domaineCarte.getLibelleCodeRenvoi());
    if (domaineCarte.getConventionnements() != null) {
      List<ConventionDto> listeConvention =
          mapperConvention.entityListToDtoList(domaineCarte.getConventionnements());
      Collections.sort(listeConvention, Comparator.comparing(ConventionDto::getPriorite));
      attestationDetail.setConventions(listeConvention);
    }
    attestationDetail.setCodeRenvoisAdditionnel(domaineCarte.getCodeRenvoiAdditionnel());
    attestationDetail.setLibelleRenvoisAdditionnel(domaineCarte.getLibelleCodeRenvoiAdditionnel());
    return attestationDetail;
  }

  public AttestationDetailDto entityToDto(DomaineDroit domaineDroit) {
    AttestationDetailDto attestationDetail = new AttestationDetailDto();
    attestationDetail.setCodeDomaine(domaineDroit.getCode());
    attestationDetail.setNumOrdreEdition(domaineDroit.getNoOrdreDroit());
    attestationDetail.setTaux(domaineDroit.getTauxRemboursement());
    attestationDetail.setUniteTaux(domaineDroit.getUniteTauxRemboursement());
    attestationDetail.setCodeRenvois(domaineDroit.getCodeRenvoi());
    attestationDetail.setLibelleRenvois(domaineDroit.getLibelleCodeRenvoi());
    if (domaineDroit.getConventionnements() != null) {
      List<ConventionDto> listeConvention =
          mapperConvention.entityListToDtoList(domaineDroit.getConventionnements());
      Collections.sort(listeConvention, (c1, c2) -> c1.getPriorite().compareTo(c2.getPriorite()));
      attestationDetail.setConventions(listeConvention);
    }
    attestationDetail.setCodeRenvoisAdditionnel(domaineDroit.getCodeRenvoiAdditionnel());
    attestationDetail.setLibelleRenvoisAdditionnel(domaineDroit.getLibelleCodeRenvoiAdditionnel());
    return attestationDetail;
  }

  public List<AttestationDetailDto> entityListToDtoListFromDomaineCarte(
      final List<DomaineCarte> list) {
    List<AttestationDetailDto> dtoList = new ArrayList<>();
    for (DomaineCarte domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }

  public List<AttestationDetailDto> entityListToDtoListFromDomaineDroits(
      final List<DomaineDroit> list) {
    List<AttestationDetailDto> dtoList = new ArrayList<>();
    for (DomaineDroit domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
