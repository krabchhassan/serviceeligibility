package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAdresse;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperTypeAdresse;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Adresse}. */
@Component
public class MapperAdresseImpl extends GenericMapperImpl<Adresse, AdresseDto>
    implements MapperAdresse {

  @Autowired MapperTypeAdresse mapperTypeAdresse;

  @Override
  public Adresse dtoToEntity(AdresseDto adresseDto) {
    Adresse adresse = null;
    if (adresseDto != null) {
      adresse = new Adresse();
      adresse.setEmail(adresseDto.getEmail());
      adresse.setCodePostal(adresseDto.getCodePostal());
      adresse.setLigne1(adresseDto.getLigne1());
      adresse.setLigne2(adresseDto.getLigne2());
      adresse.setLigne3(adresseDto.getLigne3());
      adresse.setLigne4(adresseDto.getLigne4());
      adresse.setLigne5(adresseDto.getLigne5());
      adresse.setLigne6(adresseDto.getLigne6());
      adresse.setLigne7(adresseDto.getLigne7());
      adresse.setPays(adresseDto.getPays());
      adresse.setTelephone(adresseDto.getTelephone());
      adresse.setTypeAdresse(mapperTypeAdresse.dtoToEntity(adresseDto.getTypeAdresseDto()));
    }
    return adresse;
  }

  @Override
  public AdresseDto entityToDto(
      Adresse adresse,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    AdresseDto adresseDto = null;
    if (adresse != null) {
      adresseDto = new AdresseDto();
      adresseDto.setCodePostal(adresse.getCodePostal());
      adresseDto.setEmail(adresse.getEmail());
      adresseDto.setLigne1(adresse.getLigne1());
      adresseDto.setLigne2(adresse.getLigne2());
      adresseDto.setLigne3(adresse.getLigne3());
      adresseDto.setLigne4(adresse.getLigne4());
      adresseDto.setLigne5(adresse.getLigne5());
      adresseDto.setLigne6(adresse.getLigne6());
      adresseDto.setLigne7(adresse.getLigne7());
      adresseDto.setPays(adresse.getPays());
      adresseDto.setTelephone(adresse.getTelephone());
      adresseDto.setTypeAdresseDto(
          mapperTypeAdresse.entityToDto(
              adresse.getTypeAdresse(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
    }
    return adresseDto;
  }
}
