package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperAdresseImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperContratImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat.MapperCarteDemat;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code CarteDemat}. */
@Component
public class MapperCarteDematImpl extends GenericMapperImpl<CarteDemat, CarteDematDto>
    implements MapperCarteDemat {

  @Autowired MapperBenefCarteDematImpl mapperBenefCarteDemat;

  @Autowired MapperContratImpl mapperContrat;

  @Autowired MapperAdresseImpl mapperAdresse;

  @Autowired MapperDomaineConventionImpl mapperDomaineConvention;

  @Autowired private DeclarantService declarantService;

  @Override
  public CarteDemat dtoToEntity(CarteDematDto carteDematDto) {
    CarteDemat carteDemat = null;
    if (carteDematDto != null) {
      carteDemat = new CarteDemat();
      carteDemat.setContrat(mapperContrat.dtoToEntity(carteDematDto.getContrat()));
      carteDemat.setAdresse(mapperAdresse.dtoToEntity(carteDematDto.getAdresse()));
      List<DomaineConvention> domainesConventions =
          mapperDomaineConvention.dtoListToEntityList(carteDematDto.getDomaineConventionDtos());
      domainesConventions.sort(Comparator.comparingInt(DomaineConvention::getRang));
      carteDemat.setDomainesConventions(domainesConventions);
      carteDemat.setBeneficiaires(
          mapperBenefCarteDemat.dtoListToEntityList(carteDematDto.getBenefCarteDematDtos()));
    }
    return carteDemat;
  }

  @Override
  public CarteDematDto entityToDto(
      CarteDemat carteDemat,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    CarteDematDto carteDematDto = null;
    if (carteDemat != null) {
      carteDematDto = new CarteDematDto();

      carteDematDto.setNumeroAmc(carteDemat.getIdDeclarant());
      carteDematDto.setPeriodeDebut(
          DateUtils.stringToXMLGregorianCalendar(
              carteDemat.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
      carteDematDto.setPeriodeFin(
          DateUtils.stringToXMLGregorianCalendar(
              carteDemat.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
      carteDematDto.setNomAmc(carteDemat.getIdDeclarant());
      Declarant declarant = declarantService.findById(carteDemat.getIdDeclarant());

      if (declarant != null) {
        carteDematDto.setNomAmc(declarant.getNom());
        carteDematDto.setLibelleAmc(declarant.getLibelle());
      }
      carteDematDto.setContrat(
          mapperContrat.entityToDto(
              carteDemat.getContrat(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      carteDematDto.setAdresse(
          mapperAdresse.entityToDto(
              carteDemat.getAdresse(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      carteDematDto.setDomaineConventionDtos(
          mapperDomaineConvention.entityListToDtoList(
              carteDemat.getDomainesConventions(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
      carteDematDto.setBenefCarteDematDtos(
          mapperBenefCarteDemat.entityListToDtoList(
              carteDemat.getBeneficiaires(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
    }
    return carteDematDto;
  }
}
