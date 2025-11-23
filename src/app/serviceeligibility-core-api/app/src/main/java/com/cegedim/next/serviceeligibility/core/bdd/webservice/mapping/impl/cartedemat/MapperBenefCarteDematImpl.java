package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BenefCarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat.MapperBenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code BenefCarteDemat}. */
@Component
public class MapperBenefCarteDematImpl
    extends GenericMapperImpl<BenefCarteDemat, BenefCarteDematDto>
    implements MapperBenefCarteDemat {

  @Autowired MapperBeneficiaireCouvertureImpl mapperBeneficiaireCouverture;

  @Override
  public BenefCarteDemat dtoToEntity(BenefCarteDematDto benefCarteDematDto) {
    return null;
  }

  @Override
  public BenefCarteDematDto entityToDto(
      BenefCarteDemat benefCarteDemat,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    BenefCarteDematDto benefCarteDematDto = null;
    if (benefCarteDemat != null) {
      benefCarteDematDto = new BenefCarteDematDto();
      Beneficiaire beneficiaire = benefCarteDemat.getBeneficiaire();
      Affiliation affiliation = beneficiaire.getAffiliation();
      LienContrat lienContrat = benefCarteDemat.getLienContrat();
      if (affiliation != null) {
        benefCarteDematDto.setNomBeneficiaire(affiliation.getNom());
        benefCarteDematDto.setNomPatronymique(affiliation.getNomPatronymique());
        benefCarteDematDto.setNomMarital(affiliation.getNomMarital());
        benefCarteDematDto.setPrenom(affiliation.getPrenom());
        benefCarteDematDto.setQualite(affiliation.getQualite());
        benefCarteDematDto.setTypeAssure(affiliation.getTypeAssure());
        benefCarteDematDto.setRegimeOD1(affiliation.getRegimeOD1());
        benefCarteDematDto.setCaisseOD1(affiliation.getCaisseOD1());
        benefCarteDematDto.setCentreOD1(affiliation.getCentreOD1());
        benefCarteDematDto.setRegimeOD2(affiliation.getRegimeOD2());
        benefCarteDematDto.setCaisseOD2(affiliation.getCaisseOD2());
        benefCarteDematDto.setCentreOD2(affiliation.getCentreOD2());
        benefCarteDematDto.setHasMedecinTraitant(affiliation.getHasMedecinTraitant());
        benefCarteDematDto.setRegimeParticulier(affiliation.getRegimeParticulier());
        benefCarteDematDto.setIsBeneficiaireACS(affiliation.getIsBeneficiaireACS());
        benefCarteDematDto.setIsTeleTransmission(affiliation.getIsTeleTransmission());
        benefCarteDematDto.setDebutAffiliation(
            DateUtils.stringToXMLGregorianCalendar(
                affiliation.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
      }
      if (lienContrat != null) {
        benefCarteDematDto.setLienFamilial(lienContrat.getLienFamilial());
        benefCarteDematDto.setRangAdministratif(lienContrat.getRangAdministratif());
        benefCarteDematDto.setModePaiementPrestations(lienContrat.getModePaiementPrestations());
      }
      benefCarteDematDto.setNirOd1(beneficiaire.getNirOd1());
      benefCarteDematDto.setCleNirOd1(beneficiaire.getCleNirOd1());
      benefCarteDematDto.setNirOd2(beneficiaire.getNirOd2());
      benefCarteDematDto.setCleNirOd2(beneficiaire.getCleNirOd2());
      benefCarteDematDto.setNirBeneficiaire(beneficiaire.getNirBeneficiaire());
      benefCarteDematDto.setCleNirBeneficiaire(beneficiaire.getCleNirBeneficiaire());
      benefCarteDematDto.setDateNaissance(beneficiaire.getDateNaissance());
      benefCarteDematDto.setRangNaissance(beneficiaire.getRangNaissance());
      benefCarteDematDto.setNumeroPersonne(beneficiaire.getNumeroPersonne());
      benefCarteDematDto.setRefExternePersonne(beneficiaire.getRefExternePersonne());

      benefCarteDematDto.setCouverture(
          mapperBeneficiaireCouverture.entityListToDtoList(
              benefCarteDemat.getDomainesCouverture(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
    }
    return benefCarteDematDto;
  }
}
