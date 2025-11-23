package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.InfoPilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.PilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.ValiditeDomainesDroitsDto;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoPilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.UniteDomainesDroitsEnum;
import com.cegedim.next.serviceeligibility.core.model.domain.ValiditeDomainesDroits;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Pilotage}. */
@Component
public class MapperPilotage {
  public Pilotage dtoToEntity(PilotageDto pilotageDto) {
    Pilotage pilotage = null;
    if (pilotageDto != null) {
      pilotage = new Pilotage();
      pilotage.setCodeService(pilotageDto.getNom());
      pilotage.setServiceOuvert(pilotageDto.getServiceOuvert());

      for (InfoPilotageDto infoDto : pilotageDto.getRegroupements()) {

        if (infoDto != null) {
          InfoPilotage info = new InfoPilotage();
          info.setNumDebutFichier(infoDto.getNumDebutFichier());
          info.setNumEmetteur(infoDto.getNumEmetteur());
          info.setNumClient(infoDto.getNumClient());
          info.setNomClient(infoDto.getNomClient());
          info.setVersionNorme(infoDto.getVersionNorme());
          info.setTypeFichier(infoDto.getTypeFichier());
          info.setCodePerimetre(infoDto.getCodePerimetre());
          info.setNomPerimetre(infoDto.getNomPerimetre());
          info.setTypeGestionnaireBO(infoDto.getTypeGestionnaireBO());
          info.setLibelleGestionnaireBO(infoDto.getLibelleGestionnaireBO());
          info.setCodeClient(infoDto.getCodeClient());
          info.setNumExterneContratIndividuel(infoDto.getNumExterneContratIndividuel());
          info.setNumExterneContratCollectif(infoDto.getNumExterneContratCollectif());
          info.setCodeComptable(infoDto.getCodeComptable());
          info.setPeriodeReferenceDebut(infoDto.getDateTimePeriodeReferenceDebut());
          info.setPeriodeReferenceFin(infoDto.getDateTimePeriodeReferenceFin());
          info.setFiltreDomaine(infoDto.getFiltreDomaine());
          info.setGenerateFichier(infoDto.getGenerateFichier());
          info.setDureeValidite(infoDto.getDureeValidite());
          info.setPeriodeValidite(infoDto.getPeriodeValidite());

          pilotage.setCritereRegroupement(infoDto.getCritereRegroupement());
          pilotage.setCritereRegroupementDetaille(infoDto.getCritereRegroupementDetaille());
          pilotage.setDateOuverture(infoDto.getDateTimeOuverture());
          pilotage.setDateSynchronisation(infoDto.getDateTimeSynchronisation());
          pilotage.setCouloirClient(infoDto.getCouloirClient());
          pilotage.setTypeConventionnement(infoDto.getTypeConventionnement());

          pilotage.setCaracteristique(info);
        }
      }
    }
    return pilotage;
  }

  public List<Pilotage> dtoToEntityComplet(PilotageDto pilotageDto) {
    List<Pilotage> pilotages = new ArrayList<>();
    if (pilotageDto != null) {
      for (InfoPilotageDto infoDto : pilotageDto.getRegroupements()) {
        Pilotage pilotage = new Pilotage();
        pilotage.setCodeService(pilotageDto.getNom());
        pilotage.setServiceOuvert(pilotageDto.getServiceOuvert());
        if (Constants.CARTETP_ONLY_FORUI.equals(pilotageDto.getId())) {
          pilotage.setIsCarteEditable(pilotageDto.getIsCarteEditable());
        }
        if (infoDto != null) {
          InfoPilotage info = new InfoPilotage();
          info.setNumDebutFichier(infoDto.getNumDebutFichier());
          info.setNumEmetteur(infoDto.getNumEmetteur());
          info.setNumClient(infoDto.getNumClient());
          info.setNomClient(infoDto.getNomClient());
          info.setVersionNorme(infoDto.getVersionNorme());
          info.setTypeFichier(infoDto.getTypeFichier());
          info.setCodePerimetre(infoDto.getCodePerimetre());
          info.setNomPerimetre(infoDto.getNomPerimetre());
          info.setTypeGestionnaireBO(infoDto.getTypeGestionnaireBO());
          info.setLibelleGestionnaireBO(infoDto.getLibelleGestionnaireBO());
          info.setCodeClient(infoDto.getCodeClient());
          info.setNumExterneContratIndividuel(infoDto.getNumExterneContratIndividuel());
          info.setNumExterneContratCollectif(infoDto.getNumExterneContratCollectif());
          info.setCodeComptable(infoDto.getCodeComptable());
          info.setPeriodeReferenceDebut(infoDto.getDateTimePeriodeReferenceDebut());
          info.setPeriodeReferenceFin(infoDto.getDateTimePeriodeReferenceFin());
          info.setFiltreDomaine(infoDto.getFiltreDomaine());
          info.setGenerateFichier(infoDto.getGenerateFichier());
          info.setDureeValidite(infoDto.getDureeValidite());
          info.setPeriodeValidite(infoDto.getPeriodeValidite());

          List<ValiditeDomainesDroits> validites = new ArrayList<>();
          List<ValiditeDomainesDroitsDto> validitesDto = infoDto.getValiditesDomainesDroits();

          if (CollectionUtils.isNotEmpty(validitesDto)) {
            for (ValiditeDomainesDroitsDto validiteDto : validitesDto) {
              ValiditeDomainesDroits validite = new ValiditeDomainesDroits();

              validite.setCodeDomaine(validiteDto.getCodeDomaine());
              validite.setDuree(validiteDto.getDuree());
              switch (validiteDto.getUnite()) {
                case "Jours":
                  validite.setUnite(UniteDomainesDroitsEnum.Jours);
                  break;

                case "Mois":
                  validite.setUnite(UniteDomainesDroitsEnum.Mois);
                  break;

                default:
                  validite.setUnite(null);
              }
              validite.setPositionnerFinDeMois(
                  Boolean.TRUE.equals(validiteDto.isPositionnerFinDeMois()));

              validites.add(validite);
            }

            info.setValiditesDomainesDroits(validites);
          }

          pilotage.setCritereRegroupement(infoDto.getCritereRegroupement());
          pilotage.setCritereRegroupementDetaille(infoDto.getCritereRegroupementDetaille());
          pilotage.setDateOuverture(infoDto.getDateTimeOuverture());
          pilotage.setDateSynchronisation(infoDto.getDateTimeSynchronisation());
          pilotage.setCouloirClient(infoDto.getCouloirClient());
          pilotage.setTypeConventionnement(infoDto.getTypeConventionnement());

          pilotage.setCaracteristique(info);
        }
        pilotages.add(pilotage);
      }
    }
    return pilotages;
  }

  public PilotageDto entityToDto(Pilotage pilotage) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    PilotageDto pilotageDto = null;
    if (pilotage != null) {
      pilotageDto = new PilotageDto();
      pilotageDto.setNom(pilotage.getCodeService());
      pilotageDto.setServiceOuvert(pilotage.getServiceOuvert());
      if (Constants.CARTE_TP.equals(pilotage.getCodeService())) {
        pilotageDto.setIsCarteEditable(pilotage.getIsCarteEditable());
      }
      InfoPilotage info = pilotage.getCaracteristique();
      List<InfoPilotageDto> regroupements = new ArrayList<>();
      InfoPilotageDto infoDto = new InfoPilotageDto();
      infoDto.setCritereRegroupement(pilotage.getCritereRegroupement());
      infoDto.setCritereRegroupementDetaille(pilotage.getCritereRegroupementDetaille());
      if (pilotage.getDateOuverture() != null) {
        infoDto.setDateOuverture(sdf.format(pilotage.getDateOuverture()));
      }
      if (pilotage.getDateSynchronisation() != null) {
        infoDto.setDateSynchronisation(sdf.format(pilotage.getDateSynchronisation()));
      }
      infoDto.setDateTimeOuverture(pilotage.getDateOuverture());
      infoDto.setDateTimeSynchronisation(pilotage.getDateSynchronisation());
      infoDto.setCouloirClient(pilotage.getCouloirClient());
      infoDto.setTypeConventionnement(pilotage.getTypeConventionnement());

      if (info != null) {
        infoDto.setNumDebutFichier(info.getNumDebutFichier());
        infoDto.setNumEmetteur(info.getNumEmetteur());
        infoDto.setNumClient(info.getNumClient());
        infoDto.setNomClient(info.getNomClient());
        infoDto.setVersionNorme(info.getVersionNorme());
        infoDto.setTypeFichier(info.getTypeFichier());
        infoDto.setCodePerimetre(info.getCodePerimetre());
        infoDto.setNomPerimetre(info.getNomPerimetre());
        infoDto.setTypeGestionnaireBO(info.getTypeGestionnaireBO());
        infoDto.setLibelleGestionnaireBO(info.getLibelleGestionnaireBO());
        infoDto.setCodeClient(info.getCodeClient());
        infoDto.setNumExterneContratIndividuel(info.getNumExterneContratIndividuel());
        infoDto.setNumExterneContratCollectif(info.getNumExterneContratCollectif());
        infoDto.setCodeComptable(info.getCodeComptable());
        infoDto.setDateTimePeriodeReferenceDebut(info.getPeriodeReferenceDebut());
        infoDto.setDateTimePeriodeReferenceFin(info.getPeriodeReferenceFin());
        if (info.getPeriodeReferenceDebut() != null) {
          infoDto.setPeriodeReferenceDebut(sdf.format(info.getPeriodeReferenceDebut()));
        }
        if (info.getPeriodeReferenceFin() != null) {
          infoDto.setPeriodeReferenceFin(sdf.format(info.getPeriodeReferenceFin()));
        }
        infoDto.setFiltreDomaine(info.getFiltreDomaine());
        infoDto.setGenerateFichier(info.getGenerateFichier());
        infoDto.setDureeValidite(info.getDureeValidite());
        infoDto.setPeriodeValidite(info.getPeriodeValidite());

        List<ValiditeDomainesDroits> validites = info.getValiditesDomainesDroits();
        List<ValiditeDomainesDroitsDto> validitesDto = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(validites)) {
          for (ValiditeDomainesDroits validite : validites) {
            ValiditeDomainesDroitsDto validiteDto = new ValiditeDomainesDroitsDto();

            validiteDto.setCodeDomaine(validite.getCodeDomaine());
            validiteDto.setDuree(validite.getDuree());
            if (UniteDomainesDroitsEnum.Jours.equals(validite.getUnite())) {
              validiteDto.setUnite("Jours");
            } else if (UniteDomainesDroitsEnum.Mois.equals(validite.getUnite())) {
              validiteDto.setUnite("Mois");
            } else {
              validiteDto.setUnite(null);
            }
            validiteDto.setPositionnerFinDeMois(
                Boolean.TRUE.equals(validite.isPositionnerFinDeMois()));

            validitesDto.add(validiteDto);
          }

          infoDto.setValiditesDomainesDroits(validitesDto);
        }
      }
      regroupements.add(infoDto);
      pilotageDto.setRegroupements(regroupements);
    }
    return pilotageDto;
  }

  public List<Pilotage> dtoListToEntityList(final List<PilotageDto> pilotageDtoList) {
    List<Pilotage> pilotageList = new ArrayList<>();
    for (PilotageDto pilotageDto : pilotageDtoList) {
      pilotageList.add(dtoToEntity(pilotageDto));
    }
    return pilotageList;
  }

  public List<PilotageDto> entityListToDtoList(final List<Pilotage> pilotageList) {
    List<PilotageDto> pilotageDtoList = new ArrayList<>();
    for (Pilotage pilotage : pilotageList) {
      pilotageDtoList.add(entityToDto(pilotage));
    }
    return pilotageDtoList;
  }
}
