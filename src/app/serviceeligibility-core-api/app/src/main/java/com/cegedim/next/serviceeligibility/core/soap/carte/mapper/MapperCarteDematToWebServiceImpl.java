package com.cegedim.next.serviceeligibility.core.soap.carte.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametreDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BenefCarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BeneficiaireCouvertureDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.ConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.DomaineConventionDto;
import com.cegedim.next.serviceeligibility.core.utility.Conversion;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedimassurances.norme.cartedemat.beans.Beneficiaires;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeResponse;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeResponseType;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2Response;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2ResponseType;
import com.cegedimassurances.norme.cartedemat.beans.Cartes;
import com.cegedimassurances.norme.cartedemat.beans.CartesV2;
import com.cegedimassurances.norme.cartedemat.beans.CodeReponse;
import com.cegedimassurances.norme.cartedemat.beans.Commentaires;
import com.cegedimassurances.norme.cartedemat.beans.Conventions;
import com.cegedimassurances.norme.cartedemat.beans.Couvertures;
import com.cegedimassurances.norme.cartedemat.beans.Domaines;
import com.cegedimassurances.norme.cartedemat.beans.ParametresFormule;
import com.cegedimassurances.norme.cartedemat.beans.Prestations;
import com.cegedimassurances.norme.cartedemat.beans.TypeAdresseContrat;
import com.cegedimassurances.norme.cartedemat.beans.TypeBeneficiaire;
import com.cegedimassurances.norme.cartedemat.beans.TypeBeneficiaireCouverture;
import com.cegedimassurances.norme.cartedemat.beans.TypeContrat;
import com.cegedimassurances.norme.cartedemat.beans.TypeContratV2;
import com.cegedimassurances.norme.cartedemat.beans.TypeConventionDomaine;
import com.cegedimassurances.norme.cartedemat.beans.TypeDomaineDroits;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderFonctionnelIn;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderFonctionnelOut;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderIn;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderOut;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderTechIn;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderTechOut;
import com.cegedimassurances.norme.cartedemat.beans.TypeParametreFormule;
import com.cegedimassurances.norme.cartedemat.beans.TypePrestation;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MapperCarteDematToWebServiceImpl implements MapperCarteDematToWebService {

  /** Logger. */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MapperCarteDematToWebServiceImpl.class);

  private final I18NService i18NService;

  private final CarteDematMapper carteDematMapper;

  public MapperCarteDematToWebServiceImpl() {
    i18NService = new I18NService();
    carteDematMapper = new CarteDematMapperImpl();
  }

  @Override
  public TypeHeaderOut mapHeaderOut(final TypeHeaderIn headerIn) {
    TypeHeaderOut typeHeaderOut = new TypeHeaderOut();
    typeHeaderOut.setHeaderFonctionnelOut(this.mapHeaderFoncOut(headerIn.getHeaderFonctionnelIn()));
    typeHeaderOut.setHeaderTechniqueOut(this.mapHeaderTechOut(headerIn.getHeaderTechniqueIn()));
    return typeHeaderOut;
  }

  @Override
  public CodeReponse createCodeReponseOK() {
    CodeReponse codeReponse = new CodeReponse();
    codeReponse.setCode(
        com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse.OK
            .getCode());
    codeReponse.setLibelle(i18NService.getMessage(codeReponse.getCode()));
    return codeReponse;
  }

  @Override
  public Commentaires createListeCommentairesVide() {
    return new Commentaires();
  }

  @Override
  public void mapCarteDematResponse(
      final CarteDematerialiseeResponse response, final CarteDematDto carteDematDto) {

    // l'objet correspondant au bloc cartes de la réponse n'est crée que si
    // on en trouve
    if (response.getCartes() == null) {
      Cartes cartes = new Cartes();
      response.setCartes(cartes);
    }
    response.getCartes().getCarte().add(mapCarteDematerialiseeResponseType(carteDematDto));
  }

  public void mapCarteDematResponse(
      final CarteDematerialiseeV2Response response, final CarteDematDto carteDematDto) {

    // l'objet correspondant au bloc cartes de la réponse n'est crée que si
    // on en trouve
    if (response.getCartes() == null) {
      CartesV2 cartes = new CartesV2();
      response.setCartes(cartes);
    }
    response.getCartes().getCarte().add(mapCarteDematerialiseeResponseTypeV2(carteDematDto));
  }

  /**
   * map un CarteDematerialiseeResponseType en fonction d'une CarteDematDto.
   *
   * @param carteDematDto objet contenant les valeurs à setter dans l'objet
   *     CarteDematerialiseeResponseType
   * @return un objet CarteDematerialiseeResponseType contenant les valeurs de l'objet CarteDematDto
   */
  private CarteDematerialiseeResponseType mapCarteDematerialiseeResponseType(
      final CarteDematDto carteDematDto) {

    LOGGER.debug("Mapping de la carte");

    CarteDematerialiseeResponseType carteDematerialiseeResponseType =
        carteDematMapper.carteDematDtoToCarteDematerialiseeResponseType(carteDematDto);

    // mapping contrat
    TypeContrat typeContrat = carteDematMapper.contratDtoToTypeContrat(carteDematDto.getContrat());
    carteDematerialiseeResponseType.setContrat(typeContrat);

    carteDematerialiseeResponseType.getContrat().setNumeroAMC(carteDematDto.getNumeroAmc());
    carteDematerialiseeResponseType.getContrat().setNomAMC(carteDematDto.getNomAmc());
    carteDematerialiseeResponseType.getContrat().setLibelleAMC(carteDematDto.getLibelleAmc());

    carteDematerialiseeResponseType.getContrat().setDebutPeriode(carteDematDto.getPeriodeDebut());
    carteDematerialiseeResponseType.getContrat().setFinPeriode(carteDematDto.getPeriodeFin());

    // mapping adresse contrat
    if (carteDematDto.getAdresse() != null) {
      TypeAdresseContrat typeAdresseContrat =
          carteDematMapper.adresseDtoToTypeAdresseContrat(carteDematDto.getAdresse());
      typeAdresseContrat.setTypeAdresse(carteDematDto.getAdresse().getTypeAdresseDto().getType());
      carteDematerialiseeResponseType.setAdresseContrat(typeAdresseContrat);
    }

    // mapping beneficiaires
    Beneficiaires beneficiaires = new Beneficiaires();
    for (BenefCarteDematDto benefCarteDematDto : carteDematDto.getBenefCarteDematDtos()) {
      beneficiaires.getBeneficiaire().add(mapTypeBeneficiaire(benefCarteDematDto));
    }
    carteDematerialiseeResponseType.setBeneficiaires(beneficiaires);

    // mapping domaines
    Domaines domaines = new Domaines();
    for (DomaineConventionDto domaineConventionDto : carteDematDto.getDomaineConventionDtos()) {
      domaines.getDomaine().add(mapTypeDomaineDroits(domaineConventionDto));
    }
    carteDematerialiseeResponseType.setDomaines(domaines);

    return carteDematerialiseeResponseType;
  }

  private CarteDematerialiseeV2ResponseType mapCarteDematerialiseeResponseTypeV2(
      final CarteDematDto carteDematDto) {

    LOGGER.debug("Mapping de la carte");

    CarteDematerialiseeV2ResponseType carteDematerialiseeResponseType =
        carteDematMapper.carteDematDtoToCarteDematerialiseeV2ResponseType(carteDematDto);

    // mapping contrat
    TypeContratV2 typeContrat =
        carteDematMapper.contratDtoToTypeContratV2(carteDematDto.getContrat());
    carteDematerialiseeResponseType.setContrat(typeContrat);

    carteDematerialiseeResponseType.getContrat().setNumeroAMC(carteDematDto.getNumeroAmc());
    carteDematerialiseeResponseType.getContrat().setNomAMC(carteDematDto.getNomAmc());
    carteDematerialiseeResponseType.getContrat().setLibelleAMC(carteDematDto.getLibelleAmc());

    carteDematerialiseeResponseType.getContrat().setDebutPeriode(carteDematDto.getPeriodeDebut());
    carteDematerialiseeResponseType.getContrat().setFinPeriode(carteDematDto.getPeriodeFin());

    // mapping adresse contrat
    if (carteDematDto.getAdresse() != null) {
      TypeAdresseContrat typeAdresseContrat =
          carteDematMapper.adresseDtoToTypeAdresseContrat(carteDematDto.getAdresse());
      typeAdresseContrat.setTypeAdresse(carteDematDto.getAdresse().getTypeAdresseDto().getType());
      carteDematerialiseeResponseType.setAdresseContrat(typeAdresseContrat);
    }

    // mapping beneficiaires
    Beneficiaires beneficiaires = new Beneficiaires();
    for (BenefCarteDematDto benefCarteDematDto : carteDematDto.getBenefCarteDematDtos()) {
      beneficiaires.getBeneficiaire().add(mapTypeBeneficiaire(benefCarteDematDto));
    }
    carteDematerialiseeResponseType.setBeneficiaires(beneficiaires);

    // mapping domaines
    Domaines domaines = new Domaines();
    for (DomaineConventionDto domaineConventionDto : carteDematDto.getDomaineConventionDtos()) {
      domaines.getDomaine().add(mapTypeDomaineDroits(domaineConventionDto));
    }
    carteDematerialiseeResponseType.setDomaines(domaines);

    return carteDematerialiseeResponseType;
  }

  private TypeBeneficiaire mapTypeBeneficiaire(final BenefCarteDematDto benefCarteDematDto) {

    TypeBeneficiaire typeBeneficiaire =
        carteDematMapper.benefCarteDematDtoToTypeBeneficiaire(benefCarteDematDto);
    Couvertures couvertures = new Couvertures();
    for (BeneficiaireCouvertureDto beneficiaireCouvertureDto : benefCarteDematDto.getCouverture()) {
      couvertures.getCouverture().add(mapTypeBeneficiaireCouverture(beneficiaireCouvertureDto));
    }
    typeBeneficiaire.setCouvertures(couvertures);
    return typeBeneficiaire;
  }

  private TypeBeneficiaireCouverture mapTypeBeneficiaireCouverture(
      final BeneficiaireCouvertureDto beneficiaireCouvertureDto) {

    TypeBeneficiaireCouverture typeBeneficiaireCouverture =
        carteDematMapper.beneficiaireCouvertureDtoToTypeBeneficiaireCouverture(
            beneficiaireCouvertureDto);
    Prestations prestations = new Prestations();
    for (PrestationDto prestationDto : beneficiaireCouvertureDto.getPrestationDtos()) {
      prestations.getPrestation().add(mapTypePrestation(prestationDto));
    }
    typeBeneficiaireCouverture.setPrestations(prestations);
    return typeBeneficiaireCouverture;
  }

  private TypePrestation mapTypePrestation(final PrestationDto prestationDto) {

    TypePrestation typePrestation = carteDematMapper.prestationDtoToTypePrestation(prestationDto);
    typePrestation.setCodePrestations(prestationDto.getCode());
    typePrestation.setCodeFormule(prestationDto.getFormule().getNumero());

    ParametresFormule parametresFormules = new ParametresFormule();
    for (ParametreDto parametreDto : prestationDto.getFormule().getParametres()) {
      parametresFormules.getParametreFormule().add(mapTypeParametreFormule(parametreDto));
    }
    typePrestation.setParametresFormule(parametresFormules);

    return typePrestation;
  }

  private TypeParametreFormule mapTypeParametreFormule(final ParametreDto parametreDto) {

    TypeParametreFormule typeParametreFormule =
        carteDematMapper.parametreDtoToTypeParametreFormule(parametreDto);
    typeParametreFormule.setNumeroParametres(parametreDto.getNumero());
    typeParametreFormule.setValeurParametres(parametreDto.getValeur());

    return typeParametreFormule;
  }

  private TypeDomaineDroits mapTypeDomaineDroits(final DomaineConventionDto domaineConventionDto) {
    TypeDomaineDroits typeDomaineDroits =
        carteDematMapper.domaineConventionDtoToTypeDomaineDroits(domaineConventionDto);
    Conventions conventions = new Conventions();
    for (ConventionDto conventionDto : domaineConventionDto.getConventionDtos()) {
      TypeConventionDomaine typeConventionDomaine =
          carteDematMapper.conventionDtoToTypeConventionDomaine(conventionDto);
      conventions.getConvention().add(typeConventionDomaine);
    }
    typeDomaineDroits.setConventions(conventions);
    return typeDomaineDroits;
  }

  /**
   * map TypeHeaderTechOut.
   *
   * @return le TypeHeaderTechOut renseigné
   */
  private TypeHeaderTechOut mapHeaderTechOut(TypeHeaderTechIn typeHeaderTechIn) {
    TypeHeaderTechOut typeHeaderTechOut = new TypeHeaderTechOut();
    typeHeaderTechOut.setIdServeur(
        Conversion.formatString(typeHeaderTechIn.getNomLogiciel(), 1, 14));
    typeHeaderTechOut.setIdServeur(System.getenv("HOSTNAME"));
    typeHeaderTechOut.setDate(Conversion.date2Gregorian(new Date()));
    typeHeaderTechOut.setIdSessionServeur(typeHeaderTechIn.getIdSessionServeur());
    typeHeaderTechOut.setIdSessionClient(typeHeaderTechIn.getIdSessionClient());
    typeHeaderTechOut.setVersionServeur(typeHeaderTechIn.getVersionLogiciel());
    return typeHeaderTechOut;
  }

  /**
   * map TypeHeaderFonctionnelOut.
   *
   * @return le TypeHeaderFonctionnelOut renseigné
   */
  private TypeHeaderFonctionnelOut mapHeaderFoncOut(TypeHeaderFonctionnelIn headerFonctionnelIn) {
    TypeHeaderFonctionnelOut typeHeaderFonctionnelOut = new TypeHeaderFonctionnelOut();

    typeHeaderFonctionnelOut.setIdDossierClient(headerFonctionnelIn.getIdDossierClient());
    typeHeaderFonctionnelOut.setIdDossierServeur(headerFonctionnelIn.getIdDossierServeur());
    typeHeaderFonctionnelOut.setNumAccreditation(null);
    typeHeaderFonctionnelOut.setNumOTP(null);
    return typeHeaderFonctionnelOut;
  }
}
