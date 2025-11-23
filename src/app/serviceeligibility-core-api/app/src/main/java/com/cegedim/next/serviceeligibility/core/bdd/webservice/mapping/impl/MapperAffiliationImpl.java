package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAffiliation;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Affiliation}. */
@Component
public class MapperAffiliationImpl extends GenericMapperImpl<Affiliation, AffiliationDto>
    implements MapperAffiliation {

  @Override
  public Affiliation dtoToEntity(AffiliationDto affiliationDto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Affiliation affiliation = null;
    if (affiliationDto != null) {
      affiliation = new Affiliation();
      affiliation.setCaisseOD1(affiliationDto.getCaisseOD1());
      affiliation.setCaisseOD2(affiliationDto.getCaisseOD2());
      affiliation.setCentreOD1(affiliationDto.getCentreOD1());
      affiliation.setCentreOD2(affiliationDto.getCentreOD2());
      affiliation.setCivilite(affiliationDto.getCivilite());
      affiliation.setIsBeneficiaireACS(affiliationDto.getIsBeneficiaireACS());
      affiliation.setHasMedecinTraitant(affiliationDto.getMedecinTraitant());
      affiliation.setNom(affiliationDto.getNom());
      affiliation.setNomMarital(affiliationDto.getNomMarital());
      affiliation.setNomPatronymique(affiliationDto.getNomPatronymique());
      affiliation.setPeriodeDebut(
          affiliationDto.getPeriodeDebut() == null
              ? null
              : sdf.format(affiliationDto.getPeriodeDebut()));
      affiliation.setPeriodeFin(
          affiliationDto.getPeriodeFin() == null
              ? null
              : sdf.format(affiliationDto.getPeriodeFin()));
      affiliation.setPrenom(affiliationDto.getPrenom());
      affiliation.setQualite(affiliationDto.getQualite());
      affiliation.setRegimeOD1(affiliationDto.getRegimeOD1());
      affiliation.setRegimeOD2(affiliationDto.getRegimeOD2());
      affiliation.setRegimeParticulier(affiliationDto.getRegimeParticulier());
      affiliation.setIsTeleTransmission(affiliationDto.getHasTeleTransmission());
      affiliation.setTypeAssure(affiliationDto.getTypeAssure());
    }
    return affiliation;
  }

  @Override
  public AffiliationDto entityToDto(
      Affiliation affiliation,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {

    AffiliationDto affiliationDto = null;
    if (affiliation != null) {
      affiliationDto = new AffiliationDto();
      affiliationDto.setCaisseOD1(affiliation.getCaisseOD1());
      affiliationDto.setCaisseOD2(affiliation.getCaisseOD2());
      affiliationDto.setCentreOD1(affiliation.getCentreOD1());
      affiliationDto.setCentreOD2(affiliation.getCentreOD2());
      affiliationDto.setCivilite(affiliation.getCivilite());
      affiliationDto.setIsBeneficiaireACS(affiliation.getIsBeneficiaireACS());
      affiliationDto.setMedecinTraitant(affiliation.getHasMedecinTraitant());
      affiliationDto.setNom(affiliation.getNom());
      affiliationDto.setNomMarital(affiliation.getNomMarital());
      affiliationDto.setNomPatronymique(affiliation.getNomPatronymique());
      if (StringUtils.isNotEmpty(affiliation.getPeriodeDebut())) {
        affiliationDto.setPeriodeDebut(
            DateUtils.parseDate(affiliation.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
      }
      if (StringUtils.isNotEmpty(affiliation.getPeriodeFin())) {
        affiliationDto.setPeriodeFin(
            DateUtils.parseDate(affiliation.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
      }
      affiliationDto.setPrenom(affiliation.getPrenom());
      affiliationDto.setQualite(affiliation.getQualite());
      affiliationDto.setRegimeOD1(affiliation.getRegimeOD1());
      affiliationDto.setRegimeOD2(affiliation.getRegimeOD2());
      affiliationDto.setRegimeParticulier(affiliation.getRegimeParticulier());
      affiliationDto.setHasTeleTransmission(affiliation.getIsTeleTransmission());
      affiliationDto.setTypeAssure(affiliation.getTypeAssure());
    }
    return affiliationDto;
  }
}
