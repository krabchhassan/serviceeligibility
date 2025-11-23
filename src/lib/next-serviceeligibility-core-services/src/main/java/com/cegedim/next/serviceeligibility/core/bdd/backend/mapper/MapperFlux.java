package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.FluxInfoDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.InfoFichierDto;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoAMC;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoFichierEmis;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoFichierRecu;
import com.cegedim.next.serviceeligibility.core.model.entity.Flux;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.springframework.stereotype.Component;

@Component
public class MapperFlux {

  public FluxInfoDto entityToDto(Flux flux) {
    FluxInfoDto fluxInfoDto = new FluxInfoDto();
    InfoFichierDto infoFichierDto = new InfoFichierDto();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

    // info fichier DTO
    if (flux.isFichierEmis()) {
      setFichierEmis(flux, infoFichierDto);

    } else {
      setFichierRecu(flux, infoFichierDto);
    }

    fluxInfoDto.setBatch(flux.getBatch());
    InfoAMC infoAMC = flux.getInfoAMC();
    if (infoAMC != null) {
      fluxInfoDto.setCodeCircuit(infoAMC.getCodeCircuit());
      fluxInfoDto.setCodePartenaire(infoAMC.getCodePartenaire());
      fluxInfoDto.setNumAMCEchange(infoAMC.getNumAMCEchange());
      fluxInfoDto.setEmetteurDroits(infoAMC.getEmetteurDroits());
      fluxInfoDto.setNomAMC(infoAMC.getNomAMC());
      fluxInfoDto.setOperateurPrincipal(infoAMC.getOperateurPrincipal());
    }
    if (flux.getDateExecution() != null) {
      fluxInfoDto.setDateExecution(sdf.format(flux.getDateExecution()));
    }
    fluxInfoDto.setFichierEmis(flux.isFichierEmis());
    fluxInfoDto.setIdDeclarant(flux.getIdDeclarant());
    fluxInfoDto.setProcessus(flux.getProcessus());
    fluxInfoDto.setTypeFichier(flux.getTypeFichier());
    fluxInfoDto.setInfoFichier(infoFichierDto);

    return fluxInfoDto;
  }

  private void setFichierRecu(Flux flux, InfoFichierDto infoFichierDto) {
    InfoFichierRecu infoFichierRecu = flux.getInfoFichierRecu();
    infoFichierDto.setStatut(infoFichierRecu.getStatut());
    infoFichierDto.setCodeRejet(infoFichierRecu.getCodeRejet());
    infoFichierDto.setMessageRejet(infoFichierRecu.getMessageRejet());
    infoFichierDto.setNomFichier(infoFichierRecu.getNomFichier());
    if (infoFichierRecu.getMouvementRecus() != null) {
      infoFichierDto.setMouvementRecus(infoFichierRecu.getMouvementRecus().toString());
    }
    if (infoFichierRecu.getMouvementRejetes() != null) {
      infoFichierDto.setMouvementRejetes(infoFichierRecu.getMouvementRejetes().toString());
    }

    if (infoFichierRecu.getMouvementOk() != null) {
      infoFichierDto.setMouvementOk(infoFichierRecu.getMouvementOk().toString());
    }

    infoFichierDto.setNomFichierARL(infoFichierRecu.getNomFichierARL());
    infoFichierDto.setVersionFichier(infoFichierRecu.getVersionFichier());
    if (infoFichierRecu.getNumeroFichier() != null) {
      infoFichierDto.setNumeroFichier(infoFichierRecu.getNumeroFichier().toString());
    }
  }

  private void setFichierEmis(Flux flux, InfoFichierDto infoFichierDto) {
    InfoFichierEmis infoFichierEmis = flux.getInfoFichierEmis();
    infoFichierDto.setNomFichier(infoFichierEmis.getNomFichier());

    if (infoFichierEmis.getMouvementEmis() != null) {
      infoFichierDto.setMouvementEmis(infoFichierEmis.getMouvementEmis().toString());
    }

    if (infoFichierEmis.getMouvementNonEmis() != null) {
      infoFichierDto.setMouvementNonEmis(infoFichierEmis.getMouvementNonEmis().toString());
    }
    if (infoFichierEmis.getNumeroFichier() != null) {
      infoFichierDto.setNumeroFichier(infoFichierEmis.getNumeroFichier().toString());
    }
    infoFichierDto.setCritereSecondaire(infoFichierEmis.getCritereSecondaire());
    infoFichierDto.setCritereSecondaireDetaille(infoFichierEmis.getCritereSecondaireDetaille());
    infoFichierDto.setNomFichierARL(infoFichierEmis.getNomFichierARL());
    infoFichierDto.setVersionFichier(infoFichierEmis.getVersionFichier());
  }

  public List<FluxInfoDto> entityListToDtoList(final List<Flux> list) {
    List<FluxInfoDto> dtoList = new ArrayList<>();
    for (Flux domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
