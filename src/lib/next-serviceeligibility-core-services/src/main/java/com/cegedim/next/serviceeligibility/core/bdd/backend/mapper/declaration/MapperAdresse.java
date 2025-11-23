package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.CommunicationDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.AdresseAvecFixe;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeAdresse;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Adresse}. */
@Component
public class MapperAdresse {

  public CommunicationDto entityToDto(AdresseAvecFixe adresse) {
    CommunicationDto communicationDto = null;
    if (adresse != null) {
      communicationDto = new CommunicationDto();
      communicationDto.setEmail(adresse.getEmail());
      if (adresse.getTypeAdresse() != null) {
        communicationDto.setType(adresse.getTypeAdresse().getType());
      }
      communicationDto.setTelephone(
          StringUtils.defaultIfBlank(adresse.getTelephone(), adresse.getFixe()));

      List<String> lignesAdresse = new ArrayList<>();
      lignesAdresse.add(adresse.getLigne1());
      lignesAdresse.add(adresse.getLigne2());
      lignesAdresse.add(adresse.getLigne3());
      lignesAdresse.add(adresse.getLigne4());
      lignesAdresse.add(adresse.getLigne5());
      lignesAdresse.add(adresse.getLigne6());
      lignesAdresse.add(adresse.getLigne7());
      communicationDto.setLignesAdresse(lignesAdresse);
      communicationDto.setCodePostal(adresse.getCodePostal());
    }
    return communicationDto;
  }

  public Adresse dtoToEntity(CommunicationDto communicationDto) {
    Adresse adresse = null;
    if (communicationDto != null) {
      adresse = new Adresse();
      adresse.setEmail(communicationDto.getEmail());
      adresse.setTelephone(communicationDto.getTelephone());
      TypeAdresse typeAdresse = new TypeAdresse();
      if (communicationDto.getType() != null) {
        typeAdresse.setType(communicationDto.getType());
      }
      adresse.setTypeAdresse(typeAdresse);
      adresse.setLigne1(communicationDto.getLignesAdresse().get(0));
      adresse.setLigne2(communicationDto.getLignesAdresse().get(1));
      adresse.setLigne3(communicationDto.getLignesAdresse().get(2));
      adresse.setLigne4(communicationDto.getLignesAdresse().get(3));
      adresse.setLigne5(communicationDto.getLignesAdresse().get(4));
      adresse.setLigne6(communicationDto.getLignesAdresse().get(5));
      adresse.setLigne7(communicationDto.getLignesAdresse().get(6));
      adresse.setCodePostal(communicationDto.getCodePostal());
    }
    return adresse;
  }

  public List<CommunicationDto> entityListToDtoList(final List<AdresseAvecFixe> list) {
    List<CommunicationDto> dtoList = new ArrayList<>();
    for (AdresseAvecFixe domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
