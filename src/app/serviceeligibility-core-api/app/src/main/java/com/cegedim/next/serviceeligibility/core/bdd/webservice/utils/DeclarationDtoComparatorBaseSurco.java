package com.cegedim.next.serviceeligibility.core.bdd.webservice.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDomaineDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class DeclarationDtoComparatorBaseSurco implements Comparator<DeclarationDto> {

  private ParametreBddServiceImpl parametreBddServiceImpl;

  private Map<String, String> domainsPrio;

  public DeclarationDtoComparatorBaseSurco(ParametreBddServiceImpl parametreBddServiceI) {
    this.parametreBddServiceImpl = parametreBddServiceI;
    domainsPrio = new HashMap<>();
    if (parametreBddServiceImpl != null) {
      List<ParametresDto> listDomaines = parametreBddServiceImpl.findByType(Constants.DOMAINE);

      if (!listDomaines.isEmpty()) {
        domainsPrio =
            listDomaines.stream()
                .collect(
                    Collectors.toMap(
                        ParametresDto::getCode,
                        item -> ((ParametresDomaineDto) item).getPriorite()));
      }
    }
  }

  @Override
  public int compare(DeclarationDto o1Dto, DeclarationDto o2Dto) {
    DomaineDroitDto d1Dto = o1Dto.getDomaineDroits().get(0);
    DomaineDroitDto d2Dto = o2Dto.getDomaineDroits().get(0);

    // Priorite Parametrage
    int prioriteParametrage =
        Integer.parseInt(
                StringUtils.isNotBlank(domainsPrio.get(d1Dto.getCode()))
                    ? domainsPrio.get(d1Dto.getCode())
                    : "0")
            - Integer.parseInt(
                StringUtils.isNotBlank(domainsPrio.get(d2Dto.getCode()))
                    ? domainsPrio.get(d2Dto.getCode())
                    : "0");

    if (prioriteParametrage != 0) {
      return prioriteParametrage;
    }

    // Priorite droit
    String o1PrioriteDroit = d1Dto.getPrioriteDroit().getCode();
    String o2PrioriteDroit = d2Dto.getPrioriteDroit().getCode();

    int prioriteDroit = o1PrioriteDroit.compareTo(o2PrioriteDroit);

    if (prioriteDroit != 0) {
      return prioriteDroit;
    }

    // Numero contrat
    String o1NumeroContrat = o1Dto.getContrat().getNumero();
    String o2NumeroContrat = o2Dto.getContrat().getNumero();

    int numeroContrat = o1NumeroContrat.compareTo(o2NumeroContrat);

    if (numeroContrat != 0) {
      return numeroContrat;
    }

    // Priorite conventionnement
    Integer o1PrioriteConventionnement;
    Integer o2PrioriteConventionnement;

    if (d1Dto.getConventionnements() == null || d1Dto.getConventionnements().isEmpty()) {
      o1PrioriteConventionnement = Integer.valueOf(0);
    } else {
      o1PrioriteConventionnement = d1Dto.getConventionnements().get(0).getPriorite();
    }

    if (d2Dto.getConventionnements() == null || d2Dto.getConventionnements().isEmpty()) {
      o2PrioriteConventionnement = Integer.valueOf(0);
    } else {
      o2PrioriteConventionnement = d2Dto.getConventionnements().get(0).getPriorite();
    }

    int prioriteConventionnement = o1PrioriteConventionnement.compareTo(o2PrioriteConventionnement);

    if (prioriteConventionnement != 0) {
      return prioriteConventionnement;
    }

    // when we are mapping contract to declaration we don't have effeetDebut
    if (o1Dto.getEffetDebut() == null || o2Dto.getEffetDebut() == null) {

      Date date1 = d1Dto.getPeriodeDroit().getPeriodeDebut();
      Date date2 = d2Dto.getPeriodeDroit().getPeriodeDebut();
      return date2.compareTo(date1);
    }
    // Date effet declaration
    return o1Dto.getEffetDebut().compareTo(o2Dto.getEffetDebut());
  }
}
