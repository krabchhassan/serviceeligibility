package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperPrestation;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code PRestation}. */
@Component
public class MapperPrestationImpl extends GenericMapperImpl<Prestation, PrestationDto>
    implements MapperPrestation {

  @Autowired MapperFormuleImpl mapperFormule;

  @Autowired MapperFormuleMetierImpl mapperFormuleMetier;

  @Override
  public Prestation dtoToEntity(PrestationDto prestationDto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Prestation prestation = null;
    if (prestationDto != null) {
      prestation = new Prestation();
      prestation.setCode(prestationDto.getCode());
      prestation.setFormule(mapperFormule.dtoToEntity(prestationDto.getFormule()));
      prestation.setFormuleMetier(
          mapperFormuleMetier.dtoToEntity(prestationDto.getFormuleMetier()));
      prestation.setIsEditionRisqueCarte(prestationDto.getIsEditionRisqueCarte());
      prestation.setLibelle(prestationDto.getLibelle());
      prestation.setCodeRegroupement(prestationDto.getCodeRegroupement());
      prestation.setDateEffet(
          prestationDto.getDateEffet() == null ? null : sdf.format(prestationDto.getDateEffet()));
    }
    return prestation;
  }

  @Override
  public PrestationDto entityToDto(
      Prestation prestation,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    PrestationDto prestationDto = null;
    if (prestation != null) {
      prestationDto = new PrestationDto();
      prestationDto.setCode(prestation.getCode());
      if (checkMapFormule(profondeurRecherche)) {
        prestationDto.setFormule(
            mapperFormule.entityToDto(
                prestation.getFormule(),
                profondeurRecherche,
                isFormatV2,
                isFormatV3,
                numAmcRecherche));
      }
      prestationDto.setFormuleMetier(
          mapperFormuleMetier.entityToDto(
              prestation.getFormuleMetier(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      prestationDto.setIsEditionRisqueCarte(prestation.getIsEditionRisqueCarte());
      prestationDto.setLibelle(prestation.getLibelle());
      prestationDto.setCodeRegroupement(prestation.getCodeRegroupement());
      if (StringUtils.isNotEmpty(prestation.getDateEffet())) {
        prestationDto.setDateEffet(
            DateUtils.parseDate(prestation.getDateEffet(), DateUtils.FORMATTERSLASHED));
      }
    }
    return prestationDto;
  }

  /**
   * Verifie que on doit mapper les formules
   *
   * @return vrai si la profondeur de recherche est renseignée avec la valeur AVEC_FORMULE, faux
   *     sinon.
   */
  private boolean checkMapFormule(TypeProfondeurRechercheService profondeurRecherche) {
    return profondeurRecherche.equals(TypeProfondeurRechercheService.AVEC_FORMULES);
  }
}
