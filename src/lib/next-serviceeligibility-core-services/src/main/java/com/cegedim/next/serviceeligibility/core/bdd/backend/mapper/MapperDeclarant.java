package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.DeclarantBackendDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Declarant}. */
@Component
public class MapperDeclarant {

  @Autowired private MapperPilotage mapperPilotage;

  @Autowired private MapperTranscodageDomaineTP mapperTranscodageDomaineTP;
  @Autowired private MapperConventionTP mapperConventionTP;
  @Autowired private MapperCodeRenvoiTP mapperCodeRenvoiTP;
  @Autowired private MapperRegroupementDomainesTP mapperRegroupementDomainesTP;
  @Autowired private MapperFondCarteTP mapperFondCarteTP;

  public Declarant dtoToEntity(DeclarantBackendDto declarantDto) {
    Declarant declarant = null;
    if (declarantDto != null) {
      declarant = new Declarant();
      declarant.set_id(declarantDto.getNumero());
      declarant.setNumeroPrefectoral(declarantDto.getNumero());
      declarant.setNom(declarantDto.getNom());
      declarant.setLibelle(declarantDto.getLibelle());
      declarant.setSiret(declarantDto.getSiret());
      declarant.setCodePartenaire(declarantDto.getCodePartenaire());
      declarant.setCodeCircuit(declarantDto.getCodeCircuit());
      declarant.setEmetteurDroits(declarantDto.getEmetteurDroits());
      declarant.setOperateurPrincipal(declarantDto.getOperateurPrincipal());
      declarant.setIdClientBO(declarantDto.getIdClientBO());
      if (declarantDto.getPilotages() != null) {
        declarant.setPilotages(mapperPilotage.dtoListToEntityList(declarantDto.getPilotages()));
      }
      declarant.setNumerosAMCEchanges(declarantDto.getNumerosAMCEchanges());
      if (declarantDto.getTranscodageDomainesTP() != null) {
        declarant.setTranscodageDomainesTP(
            mapperTranscodageDomaineTP.dtoListToEntityList(
                declarantDto.getTranscodageDomainesTP()));
      }

      if (declarantDto.getConventionTP() != null) {
        declarant.setConventionTP(
            mapperConventionTP.dtoListToEntityList(declarantDto.getConventionTP()));
      }
      if (declarantDto.getCodeRenvoiTP() != null) {
        declarant.setCodeRenvoiTP(
            mapperCodeRenvoiTP.dtoListToEntityList(declarantDto.getCodeRenvoiTP()));
      }
      if (declarantDto.getRegroupementDomainesTP() != null) {
        declarant.setRegroupementDomainesTP(
            mapperRegroupementDomainesTP.dtoListToEntityList(
                declarantDto.getRegroupementDomainesTP()));
      }
      if (declarantDto.getFondCarteTP() != null) {
        declarant.setFondCarteTP(
            mapperFondCarteTP.dtoListToEntityList(declarantDto.getFondCarteTP()));
      }
      declarant.setDelaiRetention(declarantDto.getDelaiRetention());
    }
    return declarant;
  }

  public DeclarantBackendDto entityToDto(Declarant declarant) {
    DeclarantBackendDto declarantDto = null;

    if (declarant != null) {
      declarantDto = new DeclarantBackendDto();
      declarantDto.setIdClientBO(declarant.getIdClientBO());
      declarantDto.setNumero(declarant.getNumeroPrefectoral());
      declarantDto.setLibelle(declarant.getLibelle());
      declarantDto.setNom(declarant.getNom());
      declarantDto.setSiret(declarant.getSiret());
      declarantDto.setCodePartenaire(declarant.getCodePartenaire());
      declarantDto.setCodeCircuit(declarant.getCodeCircuit());
      declarantDto.setEmetteurDroits(declarant.getEmetteurDroits());
      declarantDto.setOperateurPrincipal(declarant.getOperateurPrincipal());
      if (declarant.getPilotages() != null) {
        declarantDto.setPilotages(mapperPilotage.entityListToDtoList(declarant.getPilotages()));
      }
      declarantDto.setNumerosAMCEchanges(declarant.getNumerosAMCEchanges());
      if (declarant.getTranscodageDomainesTP() != null) {
        declarantDto.setTranscodageDomainesTP(
            mapperTranscodageDomaineTP.entityListToDtoList(declarant.getTranscodageDomainesTP()));
      }
      if (declarant.getConventionTP() != null) {
        declarantDto.setConventionTP(
            mapperConventionTP.entityListToDtoList(declarant.getConventionTP()));
      }
      if (declarant.getCodeRenvoiTP() != null) {
        declarantDto.setCodeRenvoiTP(
            mapperCodeRenvoiTP.entityListToDtoList(declarant.getCodeRenvoiTP()));
      }
      if (declarant.getRegroupementDomainesTP() != null) {
        declarantDto.setRegroupementDomainesTP(
            mapperRegroupementDomainesTP.entityListToDtoList(
                declarant.getRegroupementDomainesTP()));
      }
      if (declarant.getFondCarteTP() != null) {
        declarantDto.setFondCarteTP(
            mapperFondCarteTP.entityListToDtoList(declarant.getFondCarteTP()));
      }
      declarantDto.setDelaiRetention(declarant.getDelaiRetention());
    }
    return declarantDto;
  }

  public List<Declarant> dtoListToEntityList(final List<DeclarantBackendDto> dtoList) {
    List<Declarant> declarantList = new ArrayList<>();
    for (DeclarantBackendDto dto : dtoList) {
      declarantList.add(dtoToEntity(dto));
    }
    return declarantList;
  }

  public List<DeclarantBackendDto> entityListToDtoList(final List<Declarant> declarantList) {
    List<DeclarantBackendDto> dtoList = new ArrayList<>();
    for (Declarant declarant : declarantList) {
      dtoList.add(entityToDto(declarant));
    }
    return dtoList;
  }
}
