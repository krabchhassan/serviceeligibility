package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.utility.Conversion;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedimassurances.norme.base_de_droit.*;
import com.cegedimassurances.norme.commun.*;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import java.util.Calendar;
import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MapperBaseDeDroitToWebServiceImpl implements MapperBaseDeDroitToWebService {

  /** Logger. */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MapperBaseDeDroitToWebServiceImpl.class);

  public static final String HOSP = "HOSP";

  private final I18NService i18NService;

  private final BaseDroitMapper baseDroitMapper;

  public MapperBaseDeDroitToWebServiceImpl() {
    i18NService = new I18NService();
    baseDroitMapper = new BaseDroitMapperImpl();
  }

  @Override
  public TypeHeaderOut mapHeaderOut(final TypeHeaderIn headerIn) {
    TypeHeaderOut typeHeaderOut = new TypeHeaderOut();
    typeHeaderOut.setHeaderFonc(this.mapHeaderFoncOut(headerIn.getHeaderFonc()));
    typeHeaderOut.setHeaderTech(this.mapHeaderTechOut(headerIn.getHeaderTech()));
    return typeHeaderOut;
  }

  @Override
  public TypeCodeReponse createCodeReponseOK() {
    TypeCodeReponse typeCodeReponse = new TypeCodeReponse();
    typeCodeReponse.setCode(CodeReponse.OK.getCode());
    typeCodeReponse.setLibelle(i18NService.getMessage(CodeReponse.OK.getCode()));
    return typeCodeReponse;
  }

  @Override
  public void mapInfoBddResponse(
      final GetInfoBddResponse getInfoBddResponse,
      final DeclarationDto declarationDto,
      boolean isDemandeBeneficiaire,
      XMLGregorianCalendar dateFin,
      boolean isConsultationVersion3,
      boolean mapperWithAdresse) {

    getInfoBddResponse
        .getDeclarations()
        .add(
            mapTypeDeclaration(
                declarationDto,
                isDemandeBeneficiaire,
                dateFin,
                isConsultationVersion3,
                mapperWithAdresse));
  }

  /**
   * map un TypeDeclaration en fonction d'une DeclarationDto.
   *
   * @param declarationDto objet contenant les valeurs à setter dans l'objet TypeDeclaration
   * @param isDemandeBeneficiaire indique s'il s'agit d'une demande de type carte beneficiaire (
   *     {@code true} ou carte famille ({@code false}.
   * @param dateFin
   * @param isConsultationVersion3
   * @return un objet TypeDeclaration contenant les valeurs de l'objet DeclarationDto
   */
  private TypeDeclaration mapTypeDeclaration(
      final DeclarationDto declarationDto,
      boolean isDemandeBeneficiaire,
      XMLGregorianCalendar dateFin,
      boolean isConsultationVersion3,
      boolean mapperWithAdresse) {

    LOGGER.debug("Mapping de la declaration");

    TypeDeclaration typeDeclaration =
        baseDroitMapper.declarationDtoToTypeDeclaration(declarationDto);
    typeDeclaration.setBeneficiaire(
        mapBeneficiaire(declarationDto.getBeneficiaire(), mapperWithAdresse));
    for (DomaineDroitDto domaineDroitDto : declarationDto.getDomaineDroits()) {
      typeDeclaration
          .getDomaineDroits()
          .add(
              mapTypeDomaineDroit(
                  domaineDroitDto, isDemandeBeneficiaire, dateFin, isConsultationVersion3));
    }
    return typeDeclaration;
  }

  private TypeBeneficiaire mapBeneficiaire(
      final BeneficiaireDto beneficiaireDto, boolean mapperWithAdresse) {

    LOGGER.debug("Mapping du beneficiaire ayant le nir OD1 {}", beneficiaireDto.getNirOd1());

    TypeBeneficiaire typeBeneficiaire =
        baseDroitMapper.beneficiaireDtoToTypeBeneficiaire(beneficiaireDto);
    if (!mapperWithAdresse) {
      typeBeneficiaire.getAdresses().clear();
    }
    typeBeneficiaire
        .getHistoriqueAffiliations()
        .add(
            baseDroitMapper.affiliationDtoToTypeHistoriqueAffiliation(
                beneficiaireDto.getAffiliation()));
    return typeBeneficiaire;
  }

  /**
   * map un TypeDomaineDroit en fonction d'une DomaineDroitDto.
   *
   * @param domaineDroitDto objet contenant les valeurs à setter dans l'objet TypeDomaineDroit
   * @param isDemandeBeneficiaire indique s'il s'agit d'une demande de type carte beneficiaire (
   *     {@code true} ou carte famille ({@code false}.
   * @param dateFin
   * @param isConsultationVersion3
   * @return un objet TypeDomaineDroit contenant les valeurs de l'objet DomaineDroitDto
   */
  private TypeDomaineDroit mapTypeDomaineDroit(
      DomaineDroitDto domaineDroitDto,
      boolean isDemandeBeneficiaire,
      XMLGregorianCalendar dateFin,
      boolean isConsultationVersion3) {

    LOGGER.debug("Mapping du domaine de droit {}", domaineDroitDto.getCode());

    TypeDomaineDroit typeDomaineDroit =
        baseDroitMapper.domaineDroitDtoToTypeDomaineDroit(domaineDroitDto);

    PeriodeDroitDto periodeDroit = domaineDroitDto.getPeriodeDroit();
    Date dateFinPeriode =
        domaineDroitDto.getPeriodeOnline() != null
                && domaineDroitDto.getPeriodeOnline().getPeriodeFin() != null
            ? domaineDroitDto.getPeriodeOnline().getPeriodeFin()
            : domaineDroitDto.getPeriodeDroit().getPeriodeFin();
    typeDomaineDroit.setPrioriteDroit(
        baseDroitMapper.prioriteDroitDtoToTypePrioriteDroit(domaineDroitDto.getPrioriteDroit()));
    if (HOSP.equals(domaineDroitDto.getCode()) && !isConsultationVersion3) {
      setDateRenouvellement(periodeDroit, dateFinPeriode, dateFin);
      typeDomaineDroit
          .getHistoriquePeriodeDroits()
          .add(
              baseDroitMapper.periodeDroitDtoToTypeHistoriquePeriodeDroitV4(
                  domaineDroitDto.getPeriodeDroit()));

    } else {
      typeDomaineDroit
          .getHistoriquePeriodeDroits()
          .add(
              baseDroitMapper.periodeDroitDtoToTypeHistoriquePeriodeDroit(
                  domaineDroitDto.getPeriodeDroit()));
    }
    if (isConsultationVersion3) {
      for (TypeHistoriquePeriodeDroit historiquePeriodeDroit :
          typeDomaineDroit.getHistoriquePeriodeDroits()) {
        if (historiquePeriodeDroit.getPeriodeFin() != null
            && dateFin != null
            && historiquePeriodeDroit
                .getPeriodeFin()
                .toGregorianCalendar()
                .before(dateFin.toGregorianCalendar())) {
          throw new ExceptionMetier(CodeReponse.DROIT_BENEF_NON_OUVERT, dateFin);
        }
      }
    }

    if (isDemandeBeneficiaire) {
      for (ConventionnementDto conventionnementDto : domaineDroitDto.getConventionnements()) {
        typeDomaineDroit
            .getConventionnements()
            .add(baseDroitMapper.conventionnementDtoToTypeConventionnement(conventionnementDto));
      }
      for (PrestationDto prestationDto : domaineDroitDto.getPrestations()) {
        typeDomaineDroit.getPrestations().add(mapTypePrestation(prestationDto));
      }
    }
    return typeDomaineDroit;
  }

  private void setDateRenouvellement(
      PeriodeDroitDto periodeDroit, Date dateFinDroit, XMLGregorianCalendar dateFinInterro) {
    if (periodeDroit.getDateRenouvellement() == null) {
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      DateUtils.resetTimeInDate(c);
      Date now = c.getTime();
      Date dateCalculee = dateFinDroit;
      Date dateFinInterroD = null;
      if (dateFinInterro != null) {
        dateFinInterroD = dateFinInterro.toGregorianCalendar().getTime();
      }
      if (dateFinInterroD != null && !dateFinDroit.before(dateFinInterroD)) {
        if (dateFinInterroD.before(now)) {
          dateCalculee = now;
        } else {
          dateCalculee = dateFinInterroD;
        }
      }
      c.setTime(dateCalculee);
      DateUtils.resetTimeInDate(c);
      periodeDroit.setDateRenouvellement(c.getTime());
    }
  }

  /**
   * map un TypePrestation en fonction d'une PrestationDto.
   *
   * @param prestationDto objet contenant les valeurs à setter dans l'objet TypePrestation
   * @return un objet TypePrestation contenant les valeurs de l'objet PrestationDto
   */
  private TypePrestation mapTypePrestation(PrestationDto prestationDto) {

    LOGGER.debug("Mapping de la prestation {}", prestationDto.getCode());

    TypePrestation typePrestation = baseDroitMapper.prestationDtoToTypePrestation(prestationDto);
    FormuleDto formule = prestationDto.getFormule();
    if (formule != null) {
      typePrestation.setFormule(mapTypeFormule(formule));
    }
    return typePrestation;
  }

  /**
   * map un TypeFormule en fonction d'une FormuleDto.
   *
   * @param formuleDto objet contenant les valeurs à setter dans l'objet TypeFormule
   * @return un objet TypeFormule contenant les valeurs de l'objet FormuleDto
   */
  private TypeFormule mapTypeFormule(FormuleDto formuleDto) {

    LOGGER.debug("Mapping de la formule {}", formuleDto.getLibelle());

    TypeFormule typeFormule = baseDroitMapper.prestationDtoToTypeFormule(formuleDto);
    for (ParametreDto parametreDto : formuleDto.getParametres()) {
      typeFormule.getParametres().add(baseDroitMapper.parametreDtoToTypeParametre(parametreDto));
    }
    return typeFormule;
  }

  /**
   * map TypeHeaderTechOut.
   *
   * @return le TypeHeaderTechOut renseigné
   */
  private TypeHeaderTechOut mapHeaderTechOut(TypeHeaderTechIn typeHeaderTechIn) {
    TypeHeaderTechOut typeHeaderTechOut = new TypeHeaderTechOut();

    typeHeaderTechOut.setIdServeur(typeHeaderTechIn.getIdLogicielClient());
    typeHeaderTechOut.setDate(Conversion.date2Gregorian(new Date()));
    typeHeaderTechOut.setIdSessionServeur(typeHeaderTechIn.getIdSessionServeur());
    typeHeaderTechOut.setIdSessionClient(typeHeaderTechIn.getIdSessionClient());
    typeHeaderTechOut.setTypeTransaction(null);
    typeHeaderTechOut.setVersionServeur(typeHeaderTechIn.getVersionLogicielClient());

    return typeHeaderTechOut;
  }

  /**
   * map TypeHeaderFonctionnelOut.
   *
   * @return le TypeHeaderFonctionnelOut renseigné
   */
  private TypeHeaderFonctionnelOut mapHeaderFoncOut(TypeHeaderFonctionnelIn headerFonctionnelIn) {
    TypeHeaderFonctionnelOut typeHeaderFonctionnelOut = new TypeHeaderFonctionnelOut();

    typeHeaderFonctionnelOut.setCodeContexte(headerFonctionnelIn.getCodeContexte());
    typeHeaderFonctionnelOut.setVersion(headerFonctionnelIn.getVersion());
    typeHeaderFonctionnelOut.setIdDossier(headerFonctionnelIn.getIdDossier());
    typeHeaderFonctionnelOut.setNumAccreditation(null);
    typeHeaderFonctionnelOut.setNumOTP(null);

    return typeHeaderFonctionnelOut;
  }
}
