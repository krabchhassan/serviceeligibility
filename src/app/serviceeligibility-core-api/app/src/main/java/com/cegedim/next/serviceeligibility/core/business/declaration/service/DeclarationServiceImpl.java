package com.cegedim.next.serviceeligibility.core.business.declaration.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperDroitsConsult;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclaration;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.*;
import com.cegedim.next.serviceeligibility.core.business.contrat.dao.ContratDao;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.business.declaration.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperContractToDeclarationDto;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Declaration}. */
@Service("declarationService2")
public class DeclarationServiceImpl extends GenericServiceImpl<Declaration>
    implements DeclarationService {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DeclarationServiceImpl.class);

  /** Bean permettant le mapping de la declaration. */
  @Autowired private MapperDeclaration mapperDeclaration;

  @Autowired private DeclarantService declarantService;

  @Autowired private ContratDao contratDao;

  @Autowired private VisioDroitsService vdService;

  @Autowired private MapperContractToDeclarationDto contratMapper;

  @Autowired private MapperDroitsConsult droitsConsult;

  @Autowired private ParametreBddServiceImpl parametreBddServiceImpl;

  /**
   * public constructeur.
   *
   * @param declarationDao bean dao injecte
   */
  @Autowired
  public DeclarationServiceImpl(
      @Qualifier("declarationDao") IMongoGenericDao<Declaration> declarationDao) {
    super(declarationDao);
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Declaration}
   */
  @Override
  @ContinueSpan(log = "getDeclarationDao")
  public DeclarationDao getDeclarationDao() {
    return (DeclarationDao) getGenericDao();
  }

  @Override
  @ContinueSpan(log = "getDroitsBeneficiaire")
  public List<DeclarationDto> getDroitsBeneficiaire(
      DemandeInfoBeneficiaire infoBenef,
      boolean isConsultationVersion2,
      boolean isConsultationVersion3,
      boolean isConsultationVersion4,
      boolean limitWaranties) {

    DeclarantDto declarant = declarantService.getAmcRecherche(infoBenef.getNumeroPrefectoral());
    List<DeclarationDto> declarationList = new ArrayList<>();

    int exceptionDetected = -1;
    if (isConsultationVersion4) {
      exceptionDetected =
          getDeclarationsContratsValides(
              infoBenef,
              false,
              declarant,
              isConsultationVersion2,
              true,
              limitWaranties,
              declarationList);
    } else {
      declarationList =
          getDeclarationsValides(
              infoBenef, false, declarant, isConsultationVersion2, isConsultationVersion3);
    }

    Set<String> segmentsRecherche = VisiodroitUtils.getSegmentRecherche(infoBenef);

    // Detecter 6002 : Droits du bénéficiaire non ouverts
    if (exceptionDetected == 2
        || !checkValiditePeriodeDroit(
            declarationList,
            isConsultationVersion4,
            infoBenef.getDateReference(),
            infoBenef.getDateFin())) {
      throw new ExceptionServiceDroitNonOuvert();
    }
    if (exceptionDetected > -1) {
      throw new ExceptionPriorisationGaranties();
    }

    if (isConsultationVersion4) {
      declarationList.forEach(declarationDto -> declarationDto.setEffetDebut(null));
    }

    // Détecter 6003 ... ?
    checkDomaineDroit(declarationList, segmentsRecherche);

    // Trouve les priorites correctes avant le tri
    VisiodroitUtils.setPrioritesBeneficiaire(declarationList, infoBenef.getNirBeneficiaire());
    declarationList.sort(new DeclarationDtoComparatorBaseSurco(parametreBddServiceImpl));
    vdService.checkBaseSurco(
        declarationList, infoBenef.getTypeRechercheSegment(), infoBenef.getListeSegmentRecherche());
    declarationList.sort(new DeclarationDtoComparatorBaseSurco(parametreBddServiceImpl));
    return declarationList;
  }

  @Override
  @ContinueSpan(log = "getDeclarationsCarteTiersPayant")
  public List<DeclarationDto> getDeclarationsCarteTiersPayant(
      DemandeInfoBeneficiaire infoBenef,
      boolean isConsultationVersion2,
      boolean isConsultationVersion3) {

    DeclarantDto declarant = declarantService.getAmcRecherche(infoBenef.getNumeroPrefectoral());
    List<DeclarationDto> listDeclarationsDto = new ArrayList<>();
    List<DeclarationDto> declarationList =
        getDeclarationsValides(
            infoBenef, true, declarant, isConsultationVersion2, isConsultationVersion3);

    // Detecter 6002 : Droits du bénéficiaire non ouverts
    if (!checkValiditePeriodeDroit(
        declarationList, false, infoBenef.getDateReference(), infoBenef.getDateFin())) {
      throw new ExceptionServiceDroitNonOuvert();
    }
    CarteTiersPayantUtils.setPrioriteCarteTiersPayant(
        declarationList, infoBenef.getNirBeneficiaire());
    declarationList.sort(new DeclarationDtoComparatorBaseSurco(parametreBddServiceImpl));
    String numeroContrat = declarationList.get(0).getContrat().getNumero();

    List<DeclarationDto> declarationsBeneficiaires =
        getDeclarationsValideCarteTiersPayant(
            infoBenef, declarant, numeroContrat, isConsultationVersion2);
    Map<String, List<DeclarationDto>> mapBeneficiaire =
        CarteTiersPayantUtils.getDeclarationByBeneficiaire(declarationsBeneficiaires);

    for (Map.Entry<String, List<DeclarationDto>> benef : mapBeneficiaire.entrySet()) {
      List<DeclarationDto> listeDeclarationBenef = benef.getValue();
      boolean isValide =
          checkValiditePeriodeDroit(
              listeDeclarationBenef, false, infoBenef.getDateReference(), infoBenef.getDateFin());
      if (isValide) {
        CarteTiersPayantUtils.checkDomaineDroitDoublon(listeDeclarationBenef);
        listDeclarationsDto.add(listeDeclarationBenef.get(0));
      }
    }

    // Tri des déclarations dans l'odre ASSURE->CONJOINT->ENFANTS
    listDeclarationsDto.sort(new CCMOComparator());

    return listDeclarationsDto;
  }

  /**
   * Methode effectuant une requete en base de donné permettant de retrouver la liste de
   * declarations en fonction de certains parametres.
   *
   * @param infoBenef demande reçue par web service
   * @return une liste de DeclarationDto directement remonte de la base de donné en fonction des
   *     critères de recherche
   */
  private List<DeclarationDto> getDeclarationsValides(
      DemandeInfoBeneficiaire infoBenef,
      boolean isRechercheCarteTP,
      DeclarantDto declarant,
      boolean isConsultationVersion2,
      boolean isConsultationVersion3) {
    TypeRechercheDeclarantService typeDeclarant =
        getTypeRechercheDeclarant(declarant, infoBenef.getNumeroPrefectoral());

    List<Declaration> declarationList =
        getDeclarationListe(infoBenef, isRechercheCarteTP, false, typeDeclarant);

    // Si la recherche beneficiaire par nir n'aboutit pas, on recherche par numero
    // adherent
    if ((declarationList == null || declarationList.isEmpty()) && isConsultationVersion3) {
      declarationList = getDeclarationListe(infoBenef, isRechercheCarteTP, true, typeDeclarant);
    }

    if (declarationList == null || declarationList.isEmpty()) {
      if (StringUtils.isEmpty(infoBenef.getNumeroAdherent())) {
        throw new ExceptionNumeroAdherentAbsent();
      } else if (declarant != null
          && (Constants.NUMERO_CCMO_ECHANGE.equals(declarant.getNumeroRNM())
              || Constants.NUMERO_CCMO.equals(declarant.getNumeroRNM()))) {
        throw new ExceptionServiceCartePapier();
      } else {
        throw new ExceptionServiceBeneficiaireInconnu();
      }
    }

    // BLUE-5061
    List<String> declOriginIdList =
        declarationList.stream().map(Declaration::getIdOrigine).filter(Objects::nonNull).toList();

    // Pour chacun de ces id, on va enlever les déclarations d'origine de la liste
    // des déclarations
    declarationList =
        declarationList.stream()
            .filter(declaration -> !declOriginIdList.contains(declaration.get_id()))
            .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(declarationList)) {
      declarationList.sort(Comparator.comparing(Declaration::get_id).reversed());
    }

    for (Declaration decl : declarationList) {
      droitsConsult.cumulDomainsDeclaration(decl);
    }

    if (!isRechercheCarteTP) {
      declarationList = VisiodroitUtils.eclaterDeclarationsParDomaine(declarationList);
    }
    Set<String> segmentsRecherche = VisiodroitUtils.getSegmentRecherche(infoBenef);

    List<List<Declaration>> declarationsByContract = getDeclarationListByContract(declarationList);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    boolean isExceptionBeneficiaireNonEligible = true;
    for (List<Declaration> declarations : declarationsByContract) {
      isExceptionBeneficiaireNonEligible &=
          filterDeclarations(infoBenef, segmentsRecherche, declarations, filteredDeclarations);
    }

    if (CollectionUtils.isNotEmpty(declarationsByContract) && isExceptionBeneficiaireNonEligible) {
      throw new ExceptionServiceBeneficiaireNonEligible();
    }

    return mapperListeDeclaration(
        infoBenef.getProfondeurRecherche(),
        filteredDeclarations,
        isConsultationVersion2,
        infoBenef.getNumeroPrefectoral(),
        isConsultationVersion3);
  }

  /**
   * Methode permettant de regrouper les déclarations par contrat.
   *
   * @param declarationList liste de déclarations à regrouper
   * @return une liste de liste de déclarations regroupées par contrat
   */
  static List<List<Declaration>> getDeclarationListByContract(List<Declaration> declarationList) {
    List<List<Declaration>> declarationsByContract = new ArrayList<>();
    while (!declarationList.isEmpty()) {
      List<Declaration> currentContractDeclarations = new ArrayList<>();
      currentContractDeclarations.add(declarationList.get(0));
      declarationList.remove(0);

      List<Declaration> toRemove = new ArrayList<>();
      for (Declaration d : declarationList) {
        if (d.getContrat()
            .getNumero()
            .equals(currentContractDeclarations.get(0).getContrat().getNumero())) {
          currentContractDeclarations.add(d);
          toRemove.add(d);
        }
      }
      declarationList.removeAll(toRemove);
      declarationsByContract.add(currentContractDeclarations);
    }
    return declarationsByContract;
  }

  /**
   * méthode utilisé pour pallier à un pb du 606 qui n'a pas fermé toutes les déclarations
   *
   * <p>La liste à filtrer est declarationListe, les déclarations filtrées sont contenues dans
   * filteredDeclarations
   *
   * @param infoBenef
   * @param segmentsRecherche
   * @param declarationList
   * @return booléen : true si aucune déclaration de declarationList ne correspond au domaineDroit
   *     demandé
   */
  static boolean filterDeclarations(
      DemandeInfoBeneficiaire infoBenef,
      Set<String> segmentsRecherche,
      List<Declaration> declarationList,
      List<Declaration> filteredDeclarations) {
    LocalDate dateAppel =
        LocalDate.ofInstant(infoBenef.getDateReference().toInstant(), ZoneId.systemDefault());
    LocalDate dateFinAppel = null;
    if (infoBenef.getDateFin() != null) {
      dateFinAppel =
          LocalDate.ofInstant(infoBenef.getDateFin().toInstant(), ZoneId.systemDefault());
    }
    if (CollectionUtils.isNotEmpty(segmentsRecherche)) {
      if (declarationList.stream()
          .noneMatch(
              declaration ->
                  segmentsRecherche.contains(declaration.getDomaineDroits().get(0).getCode()))) {
        return true;
      }
      List<Declaration> filterDeclarations = new ArrayList<>();
      for (String segment : segmentsRecherche) {
        List<Declaration> filtreredList =
            declarationList.stream()
                .filter(
                    declaration -> declaration.getDomaineDroits().get(0).getCode().equals(segment))
                .toList();
        int indice = 0;
        for (Declaration declaration : filtreredList) {
          if (indice == 0 && Constants.CODE_ETAT_VALIDE.equals(declaration.getCodeEtat())) {
            filteredDeclarations.addAll(declarationList);
            return false;
          }
          // il n'y a qu'un domaine par déclaration vu que ça a été éclaté.
          DomaineDroit domaine = declaration.getDomaineDroits().get(0);
          LocalDate dateDebut =
              LocalDate.parse(
                  domaine.getPeriodeDroit().getPeriodeDebut(), DateUtils.SLASHED_FORMATTER);
          LocalDate dateFin =
              LocalDate.parse(
                  domaine.getPeriodeDroit().getPeriodeFin(), DateUtils.SLASHED_FORMATTER);
          int callYear = dateAppel.getYear();
          int startYear = dateDebut.getYear();
          int endYear = dateFin.getYear();
          // vérifie si l'on est sur la même année.
          boolean conditionOnYear =
              ((callYear == startYear && startYear == endYear)
                  || (startYear != endYear) && (callYear >= startYear && callYear <= endYear));
          if (conditionOnYear
              && !dateAppel.isBefore(dateDebut)
              && (dateFinAppel == null || dateFinAppel.isAfter(dateDebut))) {
            if (!dateFin.isBefore(dateAppel)) {
              filterDeclarations.add(declaration);
            } // sinon pas de droit
            break;
          }
          indice++;
        }
      }
      filteredDeclarations.addAll(filterDeclarations);
      return false;
    } else {
      filteredDeclarations.addAll(declarationList);
      return false;
    }
  }

  /**
   * Methode effectuant une requete en base de donné permettant de retrouver la liste de
   * declarations en fonction de certains parametres.
   *
   * @param infoBenef demande reçue par web serice
   * @return une liste de DeclarationDto directement remonte de la base de donné en fonction des
   *     critères de recherche
   */
  @ContinueSpan(log = "getDeclarationsContratsValides")
  public int getDeclarationsContratsValides(
      DemandeInfoBeneficiaire infoBenef,
      boolean isRechercheCarteTP,
      DeclarantDto declarant,
      boolean isConsultationVersion2,
      boolean isConsultationVersion3,
      boolean limitWaranties,
      List<DeclarationDto> result) {
    TypeRechercheDeclarantService typeDeclarant =
        getTypeRechercheDeclarant(declarant, infoBenef.getNumeroPrefectoral());

    List<ContractTP> contrats =
        getListeContrat(infoBenef, isRechercheCarteTP, false, typeDeclarant);

    // Si la recherche beneficiaire par nir n'aboutit pas, on recherche par numero
    // adherent
    if (CollectionUtils.isEmpty(contrats)
        && isConsultationVersion3
        && StringUtils.isNotBlank(infoBenef.getNumeroAdherent())) {
      contrats = getListeContrat(infoBenef, isRechercheCarteTP, true, typeDeclarant);
    }

    if (CollectionUtils.isEmpty(contrats)) {
      if (StringUtils.isEmpty(infoBenef.getNumeroAdherent())) {
        throw new ExceptionNumeroAdherentAbsent();
      }
      if (declarant != null
          && (Constants.NUMERO_CCMO_ECHANGE.equals(declarant.getNumeroRNM())
              || Constants.NUMERO_CCMO.equals(declarant.getNumeroRNM()))) {
        throw new ExceptionServiceCartePapier();
      } else {
        throw new ExceptionServiceBeneficiaireInconnu();
      }
    }
    int exceptionDetected = -1;
    List<ContractTP> contratsEclates = new ArrayList<>();
    if (!isRechercheCarteTP) {
      exceptionDetected =
          VisiodroitUtils.eclaterBeneficiairesParDomaine(
              contrats, contratsEclates, limitWaranties, infoBenef);
    }

    result.addAll(
        mapperListeContratToDeclaration(
            infoBenef.getProfondeurRecherche(),
            contratsEclates,
            isConsultationVersion2,
            infoBenef.getNumeroPrefectoral(),
            isConsultationVersion3,
            infoBenef.getListeSegmentRecherche(),
            infoBenef.getSegmentRecherche(),
            infoBenef.getDateFin(),
            DateUtils.formatDate(infoBenef.getDateReference())));

    return exceptionDetected;
  }

  private List<Declaration> getDeclarationListe(
      DemandeInfoBeneficiaire infoBenef,
      boolean isRechercheCarteTP,
      boolean rechercheParAdherent,
      TypeRechercheDeclarantService typeDeclarant) {

    List<Declaration> declarationList = new ArrayList<>();
    switch (typeDeclarant) {
      case NUMERO_ECHANGE:
        declarationList =
            getDeclarationDao()
                .findDeclarationsByBeneficiaire(
                    infoBenef.getDateNaissance(),
                    infoBenef.getRangNaissance(),
                    infoBenef.getNirBeneficiaire(),
                    infoBenef.getCleNirBneficiare(),
                    infoBenef.getNumeroPrefectoral(),
                    isRechercheCarteTP,
                    false,
                    rechercheParAdherent,
                    infoBenef.getNumeroAdherent());
        break;
      case NUMERO_PREFECTORAL:
        declarationList =
            getDeclarationDao()
                .findDeclarationsByBeneficiaire(
                    infoBenef.getDateNaissance(),
                    infoBenef.getRangNaissance(),
                    infoBenef.getNirBeneficiaire(),
                    infoBenef.getCleNirBneficiare(),
                    infoBenef.getNumeroPrefectoral(),
                    isRechercheCarteTP,
                    true,
                    rechercheParAdherent,
                    infoBenef.getNumeroAdherent());
        break;
      default:
        break;
    }
    return declarationList;
  }

  /**
   * Récupère la liste des contrats pour une recherche par bénéficiaire
   *
   * @param infoBenef Informations bénéficiaire
   * @param isRechercheCarteTP boolean true si on effectue une recherche par carte de tiers payant
   * @param rechercheParAdherent boolean true si on effectue une rechercher par adhérent
   * @param typeDeclarant Type du déclarant
   * @return La liste des contrats pour une recherche par bénéficiaire
   */
  private List<ContractTP> getListeContrat(
      DemandeInfoBeneficiaire infoBenef,
      boolean isRechercheCarteTP,
      boolean rechercheParAdherent,
      TypeRechercheDeclarantService typeDeclarant) {

    List<ContractTP> contrats = new ArrayList<>();
    switch (typeDeclarant) {
      case NUMERO_ECHANGE:
        contrats =
            contratDao.findContractsTPByBeneficiary(
                infoBenef.getDateNaissance(),
                infoBenef.getRangNaissance(),
                infoBenef.getNirBeneficiaire(),
                infoBenef.getCleNirBneficiare(),
                infoBenef.getNumeroPrefectoral(),
                isRechercheCarteTP,
                false,
                rechercheParAdherent,
                infoBenef.getNumeroAdherent());
        break;
      case NUMERO_PREFECTORAL:
        contrats =
            contratDao.findContractsTPByBeneficiary(
                infoBenef.getDateNaissance(),
                infoBenef.getRangNaissance(),
                infoBenef.getNirBeneficiaire(),
                infoBenef.getCleNirBneficiare(),
                infoBenef.getNumeroPrefectoral(),
                isRechercheCarteTP,
                true,
                rechercheParAdherent,
                infoBenef.getNumeroAdherent());
        break;
      default:
        break;
    }
    return contrats;
  }

  /**
   * Methode effectuant une requete en base de donné permettant de retrouver la liste de
   * declarations correspondant à un contrat donnée
   *
   * @param infoBenef demande reçue par web serice
   * @return une liste de DeclarationDto directement remonte de la base de donné en fonction des
   *     critères de recherche
   */
  private List<DeclarationDto> getDeclarationsValideCarteTiersPayant(
      DemandeInfoBeneficiaire infoBenef,
      DeclarantDto declarant,
      String numeroContrat,
      boolean isConsultationVersion2) {
    List<Declaration> declarationList = null;
    TypeRechercheDeclarantService typeDeclarant =
        getTypeRechercheDeclarant(declarant, infoBenef.getNumeroPrefectoral());
    switch (typeDeclarant) {
      case NUMERO_ECHANGE:
        declarationList =
            getDeclarationDao()
                .findDeclarationsByNumeroContrat(
                    infoBenef.getNumeroPrefectoral(), numeroContrat, false);
        break;
      case NUMERO_PREFECTORAL:
        declarationList =
            getDeclarationDao()
                .findDeclarationsByNumeroContrat(
                    infoBenef.getNumeroPrefectoral(), numeroContrat, true);
        break;
      default:
        break;
    }
    return mapperListeDeclaration(
        infoBenef.getProfondeurRecherche(),
        declarationList,
        isConsultationVersion2,
        infoBenef.getNumeroPrefectoral(),
        false);
  }

  /**
   * Dans le cas d'une recherche de type bénéficiaire (segment non <code>null</code>), cette méthode
   * vérifie que le beneficiaire est eligible au service demandé :<br>
   * la liste de declaration doit en contenir au moins une dont le code d'un de ses domaine de droit
   * correspond au segment de recherche en paramètre.
   *
   * @param declarationList list de declaration à verifier
   * @param segmentsRecherche segments de recherche à chercher
   */
  @ContinueSpan(log = "checkDomaineDroit")
  public void checkDomaineDroit(
      final List<DeclarationDto> declarationList, Set<String> segmentsRecherche) {
    if (!segmentsRecherche.isEmpty()) {
      declarationList.removeIf(
          declaration -> !hasValidDomaineDroits(declaration.getDomaineDroits(), segmentsRecherche));
      if (declarationList.isEmpty()) {
        throw new ExceptionServiceBeneficiaireNonEligible();
      }
    }
  }

  /**
   * Verifie qu'il existe un domaineDroit dans une liste de domaineDroit dont le code correspond à
   * la liste des segments de recherche en paramètre. Tout les domaines de droit invalide sont
   * supprimer de la liste.
   *
   * @param domaineDroistList liste de domaine droit à verifier
   * @param segmentsRecherche liste des segments de recherche à chercher
   * @return vrai si au moins un code ou codeExterne correspond aux segmentsRecherche
   */
  private boolean hasValidDomaineDroits(
      final List<DomaineDroitDto> domaineDroistList, Set<String> segmentsRecherche) {

    domaineDroistList.removeIf(
        domaineDroit ->
            !segmentsRecherche.contains(domaineDroit.getCode())
                && !segmentsRecherche.contains(domaineDroit.getCodeExterne()));
    return !domaineDroistList.isEmpty();
  }

  private List<DeclarationDto> mapperListeDeclaration(
      TypeProfondeurRechercheService profondeurRecherche,
      List<Declaration> declarationList,
      boolean isConsultationVersion2,
      String numAmcRecherche,
      boolean isConsultationVersion3) {
    return mapperDeclaration.entityListToDtoList(
        declarationList,
        profondeurRecherche,
        isConsultationVersion2,
        isConsultationVersion3,
        numAmcRecherche);
  }

  @ContinueSpan(log = "mapperListeContratToDeclaration")
  public List<DeclarationDto> mapperListeContratToDeclaration(
      TypeProfondeurRechercheService profondeurRecherche,
      List<ContractTP> contratList,
      boolean isConsultationVersion2,
      String numAmcRecherche,
      boolean isConsultationVersion3,
      List<String> segmentRecherche,
      String typeRechercheSegment,
      Date dateFinDemande,
      String dateReference) {
    contratMapper.setProfondeurRecherche(profondeurRecherche);
    contratMapper.setFormatV2(isConsultationVersion2);
    contratMapper.setFormatV3(isConsultationVersion3);
    contratMapper.setNumAmcRecherche(numAmcRecherche);
    return contratMapper.entityListToDtoList(
        contratList, segmentRecherche, typeRechercheSegment, dateFinDemande, dateReference);
  }

  /**
   * Vérifie que la periode de couverture du beneficiaire est en accord avec la date de reference
   * passée dans la demande.
   *
   * @param declarationList liste de declaration a verifier
   * @param dateReference date de la demande de prise en charge
   * @return true si la liste des declaration est valide sinon false
   */
  @ContinueSpan(log = "checkValiditePeriodeDroit")
  public boolean checkValiditePeriodeDroit(
      List<DeclarationDto> declarationList,
      boolean isConsultationVersion4,
      Date dateReference,
      Date dateFin) {
    Date dateReferenceNoTime = DateUtils.formatDateWithHourMinuteSecond(dateReference);
    List<DeclarationDto> declarationsEligibles = new ArrayList<>();
    List<String> contratResilies = new ArrayList<>();
    if (!CollectionUtils.isEmpty(declarationList)) {
      declarationList.sort(Comparator.comparing(DeclarationDto::getEffetDebut).reversed());
    }
    for (DeclarationDto declaration : declarationList) {
      if (!contratResilies.contains(declaration.getContrat().getNumero())) {
        boolean isIncluded =
            declaration.getDomaineDroits().stream()
                .map(DomaineDroitDto::getPeriodeDroit)
                .anyMatch(
                    periode ->
                        !dateReferenceNoTime.before(periode.getPeriodeDebut())
                            // Dans le cas d'une resiliation il y a forcement un date de
                            // finFermeture
                            // pour le domaine qui porte la resiliation
                            && periode.getPeriodeFermetureFin() != null
                            && !dateReferenceNoTime.after(periode.getPeriodeFermetureFin()));
        boolean isValid =
            isDomaineDroitPeriodeDroitValide(
                declaration.getDomaineDroits(),
                dateReferenceNoTime,
                dateFin,
                isConsultationVersion4);
        if (isValid) {
          declarationsEligibles.add(declaration);
        }
        // BLUE-4685 && 5061
        if (!isValid
            && Constants.CODE_ETAT_RESILIATION.equals(declaration.getCodeEtat())
            && isIncluded) {
          contratResilies.add(declaration.getContrat().getNumero());
        }
      }
    }

    declarationList.clear();
    if (!declarationsEligibles.isEmpty()) {
      declarationList.addAll(declarationsEligibles);
    }

    return !declarationList.isEmpty();
  }

  /**
   * Verfie que'il existe une periode de droit valide dans une liste de domaineDroit les domaine de
   * droit non valide sont supprimé de la liste.
   *
   * @param domaineDroitList liste de DomaineDroitDto à verifier
   * @param dateReferenceNoTime date à laquelle les droit doivent être ouvert
   * @return vrai si au moins une pèriode valide est trouvée, faux sinon
   */
  private boolean isDomaineDroitPeriodeDroitValide(
      final List<DomaineDroitDto> domaineDroitList,
      final Date dateReferenceNoTime,
      final Date dateFin,
      boolean isVersion4) {

    final Iterator<DomaineDroitDto> domaineDroitIterator = domaineDroitList.iterator();
    DomaineDroitDto domaineDroit;
    while (domaineDroitIterator.hasNext()) {
      domaineDroit = domaineDroitIterator.next();
      if (!isPeriodeDroitValide(
          domaineDroit.getPeriodeDroit(), dateReferenceNoTime, dateFin, isVersion4)) {
        domaineDroitIterator.remove();
      }
    }
    return !domaineDroitList.isEmpty();
  }

  /**
   * Verifie que la periode de droit passee en parametre est valide.<br>
   *
   * @param periodeDroit la periodeDroitDto a verifier.
   * @param dateReferenceNoTime date a laquelle les droits doivent etre ouverts.
   * @param dateFin date jusqu a laquelle les droits doivent etre ouverts.
   * @return {@code true} si la periode de droit est valide, {@code false} sinon.
   */
  private boolean isPeriodeDroitValide(
      final PeriodeDroitDto periodeDroit,
      final Date dateReferenceNoTime,
      final Date dateFin,
      final boolean isVersion4) {
    final Date periodeDebut = periodeDroit.getPeriodeDebut();
    final Date periodeFin = periodeDroit.getPeriodeFin();
    if (dateFin == null) {
      return periodeDebut.compareTo(dateReferenceNoTime) <= 0
          && (periodeFin == null || periodeFin.compareTo(dateReferenceNoTime) >= 0);
    } else {
      return this.setPeriods(periodeDroit, dateReferenceNoTime, dateFin, isVersion4);
    }
  }

  private boolean setPeriods(
      final PeriodeDroitDto periodeDroit,
      final Date dateReference,
      final Date dateFin,
      final boolean isVersion4) {
    if (periodeDroit.getPeriodeDebut().compareTo(dateFin) <= 0
        && (periodeDroit.getPeriodeFin() == null
            || periodeDroit.getPeriodeFin().compareTo(dateReference) >= 0)) {
      // remove this if condition if you want to apply the
      // retrofitting of the periode into the requsest for other
      // versions
      if (isVersion4) {
        if (periodeDroit.getPeriodeDebut().compareTo(dateReference) < 0) {
          periodeDroit.setPeriodeDebut(dateReference);
        }
        if (periodeDroit.getPeriodeFin() == null
            || periodeDroit.getPeriodeFin().compareTo(dateFin) > 0) {
          periodeDroit.setPeriodeFin(dateFin);
        }
      }
      return true;
    }
    return false;
  }

  private TypeRechercheDeclarantService getTypeRechercheDeclarant(
      DeclarantDto declarant, String numAmcRecherche) {
    if (declarant != null) {
      // HACK - Le numero d'amc prefectoral étant celui fourni dans la
      // requete, on passe par le RNM qui est l'id reel de l'amc
      if (numAmcRecherche.equals(declarant.getNumeroRNM())) {
        return TypeRechercheDeclarantService.NUMERO_PREFECTORAL;
      } else {
        return TypeRechercheDeclarantService.NUMERO_ECHANGE;
      }
    }
    LOGGER.info("Aucune AMC n'existe avec l'id {}", numAmcRecherche);
    return TypeRechercheDeclarantService.UNDEFINED;
  }
}
