package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperContrat;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Contrat}. */
@Component
public class MapperContratImpl extends GenericMapperImpl<Contrat, ContratDto>
    implements MapperContrat {

  @Override
  public Contrat dtoToEntity(ContratDto contratDto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Contrat contrat = null;
    if (contratDto != null) {
      contrat = new Contrat();
      contrat.setCategorieSociale(contratDto.getCategorieSociale());
      contrat.setCivilitePorteur(contratDto.getCivilitePorteur());
      contrat.setDateResiliation(
          contratDto.getDateResiliation() == null
              ? null
              : sdf.format(contratDto.getDateResiliation()));
      contrat.setDateSouscription(
          contratDto.getDateSouscription() == null
              ? null
              : sdf.format(contratDto.getDateSouscription()));
      contrat.setDestinataire(contratDto.getDestinataire());
      contrat.setIndividuelOuCollectif(contratDto.getIndividuelOuCollectif());
      contrat.setIsContratCMU(contratDto.getIsContratCMU());
      contrat.setIsContratResponsable(contratDto.getIsContratResponsable());
      contrat.setLienFamilial(contratDto.getLienFamilial());
      contrat.setMotifFinSituation(contratDto.getMotifFinSituation());
      contrat.setNomPorteur(contratDto.getNomPorteur());
      contrat.setNumero(contratDto.getNumero());
      contrat.setNumeroAdherent(contratDto.getNumeroAdherent());
      contrat.setNumeroAdherentComplet(contratDto.getNumeroAdherentComplet());
      contrat.setNumeroContratCollectif(contratDto.getNumeroContratCollectif());
      contrat.setPrenomPorteur(contratDto.getPrenomPorteur());
      contrat.setQualification(contratDto.getQualification());
      contrat.setRangAdministratif(contratDto.getRangAdministratif());
      contrat.setSituationDebut(
          contratDto.getSituationDebut() == null
              ? null
              : sdf.format(contratDto.getSituationDebut()));
      contrat.setSituationFin(
          contratDto.getSituationfin() == null ? null : sdf.format(contratDto.getSituationfin()));
      contrat.setSituationParticuliere(contratDto.getSituationParticuliere());
      contrat.setType(contratDto.getType());
      contrat.setNumeroExterneContratIndividuel(contratDto.getNumeroExterneContratIndividuel());
      contrat.setNumeroExterneContratCollectif(contratDto.getNumeroExterneContratCollectif());
      contrat.setTypeConvention(contratDto.getTypeConvention());
      contrat.setCritereSecondaire(contratDto.getCritereSecondaire());
      contrat.setCritereSecondaireDetaille(contratDto.getCritereSecondaireDetaille());
      contrat.setModePaiementPrestations(contratDto.getModePaiementPrestations());
      contrat.setGestionnaire(contratDto.getGestionnaire());
      contrat.setGroupeAssures(contratDto.getGroupeAssures());
      contrat.setNumOperateur(contratDto.getNumeroOperateur());
      contrat.setNumAMCEchange(contratDto.getNumeroAMCEchanges());
      contrat.setEditeurCarte(contratDto.getEditeurCarte());
      contrat.setFondCarte(contratDto.getFondCarte());
      contrat.setAnnexe1Carte(contratDto.getAnnexe1Carte());
      contrat.setAnnexe2Carte(contratDto.getAnnexe2Carte());
      contrat.setSiret(contratDto.getSiret());
      contrat.setIdentifiantCollectivite(contratDto.getIdentifiantCollectivite());
      contrat.setGroupePopulation(contratDto.getGroupePopulation());
      contrat.setRaisonSociale(contrat.getRaisonSociale());
      contrat.setCodeRenvoi(contratDto.getCodeRenvoi());
      contrat.setLibelleCodeRenvoi(contratDto.getLibelleCodeRenvoi());
      contrat.setCodeItelis(contratDto.getCodeItelis());
    }
    return contrat;
  }

  @Override
  public ContratDto entityToDto(
      Contrat contrat,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    ContratDto contratDto = new ContratDto();
    if (contrat != null) {
      contratDto = new ContratDto();
      contratDto.setCategorieSociale(contrat.getCategorieSociale());
      contratDto.setCivilitePorteur(contrat.getCivilitePorteur());
      if (StringUtils.isNotEmpty(contrat.getDateResiliation())) {
        contratDto.setDateResiliation(
            DateUtils.parseDate(contrat.getDateResiliation(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotEmpty(contrat.getDateSouscription())) {
        contratDto.setDateSouscription(
            DateUtils.parseDate(contrat.getDateSouscription(), DateUtils.FORMATTERSLASHED));
      }
      contratDto.setDestinataire(contrat.getDestinataire());
      contratDto.setIndividuelOuCollectif(contrat.getIndividuelOuCollectif());
      contratDto.setIsContratCMU(contrat.getIsContratCMU());
      contratDto.setIsContratResponsable(contrat.getIsContratResponsable());
      contratDto.setLienFamilial(contrat.getLienFamilial());
      contratDto.setMotifFinSituation(contrat.getMotifFinSituation());
      contratDto.setNomPorteur(contrat.getNomPorteur());
      contratDto.setNumero(contrat.getNumero());
      contratDto.setNumeroAdherent(contrat.getNumeroAdherent());
      contratDto.setNumeroAdherentComplet(contrat.getNumeroAdherentComplet());
      contratDto.setNumeroContratCollectif(contrat.getNumeroContratCollectif());
      contratDto.setPrenomPorteur(contrat.getPrenomPorteur());
      contratDto.setQualification(contrat.getQualification());
      contratDto.setRangAdministratif(contrat.getRangAdministratif());
      if (StringUtils.isNotEmpty(contrat.getSituationDebut())) {
        contratDto.setSituationDebut(
            DateUtils.parseDate(contrat.getSituationDebut(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotEmpty(contrat.getSituationFin())) {
        contratDto.setSituationfin(
            DateUtils.parseDate(contrat.getSituationFin(), DateUtils.FORMATTERSLASHED));
      }
      contratDto.setSituationParticuliere(contrat.getSituationParticuliere());
      contratDto.setType(contrat.getType());
      contratDto.setNumeroExterneContratIndividuel(contrat.getNumeroExterneContratIndividuel());
      contratDto.setNumeroExterneContratCollectif(contrat.getNumeroExterneContratCollectif());
      contratDto.setTypeConvention(contrat.getTypeConvention());
      contratDto.setCritereSecondaire(contrat.getCritereSecondaire());
      contratDto.setCritereSecondaireDetaille(contrat.getCritereSecondaireDetaille());
      contratDto.setModePaiementPrestations(contrat.getModePaiementPrestations());
      contratDto.setGestionnaire(contrat.getGestionnaire());
      contratDto.setGroupeAssures(contrat.getGroupeAssures());
      contratDto.setNumeroOperateur(contrat.getNumOperateur());
      contratDto.setNumeroAMCEchanges(contrat.getNumAMCEchange());
      contratDto.setEditeurCarte(contrat.getEditeurCarte());

      contratDto.setFondCarte(contrat.getFondCarte());
      contratDto.setAnnexe1Carte(contrat.getAnnexe1Carte());
      contratDto.setAnnexe2Carte(contrat.getAnnexe2Carte());
      contratDto.setSiret(contrat.getSiret());
      contratDto.setIdentifiantCollectivite(contrat.getIdentifiantCollectivite());
      contratDto.setGroupePopulation(contrat.getGroupePopulation());
      contratDto.setRaisonSociale(contrat.getRaisonSociale());
      contratDto.setCodeRenvoi(contrat.getCodeRenvoi());
      contratDto.setLibelleCodeRenvoi(contrat.getLibelleCodeRenvoi());
      contratDto.setCodeItelis(contrat.getCodeItelis());
    }
    return contratDto;
  }
}
