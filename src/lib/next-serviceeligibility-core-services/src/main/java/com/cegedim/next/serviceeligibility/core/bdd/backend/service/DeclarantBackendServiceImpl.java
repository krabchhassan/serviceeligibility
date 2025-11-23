package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarantBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.HistoriqueDeclarantDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ServiceDroitsDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.*;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueDeclarant;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** Classe d'accès aux services lies aux {@code Declarant}. */
@Service("declarantBackendService")
public class DeclarantBackendServiceImpl extends GenericServiceImpl<Declarant>
    implements DeclarantBackendService {

  private static final String ZERO_TIME = " 00:00:00";

  private final MapperDeclarant mapperDeclarant;

  private final MapperPilotage mapperPilotage;

  private final MapperTranscodageDomaineTP mapperTranscodageDomaineTP;
  private final MapperConventionTP mapperConventionTP;
  private final MapperCodeRenvoiTP mapperCodeRenvoiTP;
  private final MapperRegroupementDomainesTP mapperRegroupementDomainesTP;
  private final MapperFondCarteTP mapperFondCarteTP;

  private final MapperDeclarantEchange mapperDeclarantEchange;

  private final HistoriqueDeclarantDao historiqueDeclarantDao;

  private final ServiceDroitsDao serviceDroitsDao;

  private final DeclarantBackendDao declarantDao;

  private final EventService eventService;

  /**
   * public constructeur.
   *
   * @param declarantDao bean dao injecte
   */
  @Autowired
  public DeclarantBackendServiceImpl(
      DeclarantBackendDao declarantDao,
      final MapperDeclarant mapperDeclarant,
      final MapperPilotage mapperPilotage,
      final MapperTranscodageDomaineTP mapperTranscodageDomaineTP,
      final MapperConventionTP mapperConventionTP,
      final MapperCodeRenvoiTP mapperCodeRenvoiTP,
      final MapperRegroupementDomainesTP mapperRegroupementDomainesTP,
      final MapperFondCarteTP mapperFondCarteTP,
      final MapperDeclarantEchange mapperDeclarantEchange,
      final HistoriqueDeclarantDao historiqueDeclarantDao,
      final ServiceDroitsDao serviceDroitsDao,
      final EventService eventService) {
    super(declarantDao);
    this.mapperDeclarant = mapperDeclarant;
    this.mapperPilotage = mapperPilotage;
    this.mapperTranscodageDomaineTP = mapperTranscodageDomaineTP;
    this.mapperConventionTP = mapperConventionTP;
    this.mapperCodeRenvoiTP = mapperCodeRenvoiTP;
    this.mapperRegroupementDomainesTP = mapperRegroupementDomainesTP;
    this.mapperFondCarteTP = mapperFondCarteTP;
    this.mapperDeclarantEchange = mapperDeclarantEchange;
    this.historiqueDeclarantDao = historiqueDeclarantDao;
    this.serviceDroitsDao = serviceDroitsDao;
    this.declarantDao = declarantDao;
    this.eventService = eventService;
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Declaration}
   */
  @Override
  @ContinueSpan(log = "getDeclarantDao")
  public DeclarantBackendDao getDeclarantDao() {
    return ((DeclarantBackendDao) getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findById Declarant")
  public Declarant findById(String id) {
    return getDeclarantDao().findById(id);
  }

  /**
   * Retourne index du service dans la liste des pilotages
   *
   * @param pilotages liste des pilotages
   * @param service code service
   * @return index du service dans la liste des pilotages
   */
  private int getIndexService(List<PilotageDto> pilotages, String service) {
    for (PilotageDto pilotage : pilotages) {
      if (pilotage != null && pilotage.getNom() != null && pilotage.getNom().equals(service)) {
        return pilotages.indexOf(pilotage);
      }
    }
    return -1;
  }

  @Override
  @ContinueSpan(log = "findDtoById DeclarantBackendDto")
  public DeclarantBackendDto findDtoById(String id) {

    DeclarantBackendDto declarantDto = mapperDeclarant.entityToDto(findById(id));

    if (declarantDto != null) {
      // Codes services trie par triRestit.
      List<ServiceDroits> services = serviceDroitsDao.findCodesService();

      // Inclure tous les services, meme ceux sans pilotage
      // ensuite reconstituer une liste de services (pilotages) sans
      // double (= cas ALMV3) initialement chaque pilotage a au moins 1
      // regroupement (=liste a un seul element) pour les services en
      // double on ajoute les regroupements
      List<PilotageDto> pilotages = new ArrayList<>();

      // La boucle preserve le triRestit.
      for (ServiceDroits service : services) {
        getService(declarantDto, pilotages, service);
      }

      // affectation de la nouvelle liste reconstituee
      declarantDto.setPilotages(pilotages);
    }
    return declarantDto;
  }

  private void getService(
      DeclarantBackendDto declarantDto, List<PilotageDto> pilotages, ServiceDroits service) {
    if (service != null) {
      boolean foundService = false;
      for (PilotageDto pilotage : declarantDto.getPilotages()) {
        foundService = getPilotage(pilotages, service, foundService, pilotage);
      }
      if (!foundService) {
        // Inclure le service sans pilotages
        PilotageDto pilotageDtoVide = new PilotageDto();
        pilotageDtoVide.setNom(service.getCode());
        pilotageDtoVide.setId(service.getCode().replace("-", ""));
        pilotageDtoVide.setServiceOuvert(false);
        // Liste vide des regroupements
        List<InfoPilotageDto> listeRegroupementsVide = new ArrayList<>();
        pilotageDtoVide.setRegroupements(listeRegroupementsVide);
        pilotages.add(pilotageDtoVide);
      }
    }
  }

  private boolean getPilotage(
      List<PilotageDto> pilotages,
      ServiceDroits service,
      boolean foundService,
      PilotageDto pilotage) {
    if (service.getCode().equalsIgnoreCase(pilotage.getNom())) {
      // Consitution id technique pour ihm
      pilotage.setId(service.getCode().replace("-", ""));
      pilotage.setTypeService(service.getTypeService());
      int index = getIndexService(pilotages, pilotage.getNom());
      if (index == -1) {
        pilotages.add(pilotage);
      } else {
        if (pilotages.get(index) != null
            && pilotages.get(index).getRegroupements() != null
            && pilotage.getRegroupements() != null
            && !pilotage.getRegroupements().isEmpty()) {
          pilotages.get(index).getRegroupements().add(pilotage.getRegroupements().get(0));
        }
      }
      foundService = true;
    }
    return foundService;
  }

  private List<Declarant> findByCriteria(String id, String nom, String couloir, String service)
      throws UnsupportedEncodingException {
    try {
      return getDeclarantDao().findByCriteria(id, nom, couloir, service);
    } catch (UnsupportedEncodingException e) {
      throw new RequestValidationException("Invalid parameter encoding", e, HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  @ContinueSpan(log = "findServicesDtoByCriteria")
  public List<ServicesDeclarantDto> findServicesDtoByCriteria(
      String id, String nom, String couloir, String service) {

    List<ServicesDeclarantDto> declarantsDto = new ArrayList<>();

    if (!isCriteriaValid(id, nom, service, couloir)) {
      throw new RequestValidationException("Search criteria is not valid", HttpStatus.BAD_REQUEST);
    }

    try {
      List<Declarant> declarants = findByCriteria(id, nom, couloir, service);

      // Boolean pour savoir si on affiche des amc vide de pilotage actif
      boolean displayEmptyPilotage = StringUtils.isBlank(couloir) && StringUtils.isBlank(service);

      for (Declarant declarant : declarants) {
        ServicesDeclarantDto declarantDto = new ServicesDeclarantDto();
        declarantDto.setNumero(declarant.getNumeroPrefectoral());
        declarantDto.setNom(declarant.getNom());
        declarantDto.setLibelle(declarant.getLibelle());
        if (declarant.getPilotages() != null && !declarant.getPilotages().isEmpty()) {
          List<ServiceCouloirsDto> services =
              findServices(declarant.getPilotages(), couloir, service);
          declarantDto.setServices(services);
        }
        if (displayEmptyPilotage || !declarantDto.getServices().isEmpty()) {
          declarantsDto.add(declarantDto);
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new RequestValidationException("Invalid parameter encoding", e, HttpStatus.BAD_REQUEST);
    }

    return declarantsDto;
  }

  @Override
  @ContinueSpan(log = "findAll Declarant")
  public List<Declarant> findAll() {
    return getDeclarantDao().findAllDeclarants();
  }

  @Override
  @ContinueSpan(log = "findAllDto DeclarantBackendDto")
  public List<DeclarantBackendDto> findAllDto() {
    return mapperDeclarant.entityListToDtoList(findAll());
  }

  @Override
  @ContinueSpan(log = "findAllLightDto DeclarantLightDto")
  public List<DeclarantLightDto> findAllLightDto() {
    List<Declarant> declarants = findAll();
    List<DeclarantLightDto> declarantsLight = new ArrayList<>();
    for (Declarant declarant : declarants) {
      DeclarantLightDto declarantLight = new DeclarantLightDto();
      declarantLight.setNumero(declarant.getNumeroPrefectoral());
      declarantLight.setNom(declarant.getNom());
      declarantLight.setCodePartenaire(declarant.getCodePartenaire());
      declarantsLight.add(declarantLight);
    }
    return declarantsLight;
  }

  @Override
  @ContinueSpan(log = "createDeclarant")
  public Declarant createDeclarant(DeclarantRequestDto declarantDto) {
    Declarant declarant = findById(declarantDto.getNumero());
    if (declarant == null) {
      declarant = new Declarant();
      declarant.set_id(declarantDto.getNumero());
      declarant.setDateCreation(new Date());
      declarant.setUserCreation(declarantDto.getUser());
      initializeDeclarant(declarantDto, declarant);

      getDeclarantDao().create(declarant);

      // Creation hist. declarant
      HistoriqueDeclarant historiqueDeclarant =
          mapperHistoriqueDeclarant(declarant, declarantDto.getPilotages());
      historiqueDeclarantDao.create(historiqueDeclarant);

    } else {
      throw new RequestValidationException(
          String.format("The id %s already exists", declarantDto.getNumero()),
          HttpStatus.BAD_REQUEST);
    }
    return declarant;
  }

  private void initializeDeclarant(DeclarantRequestDto declarantDto, Declarant declarant) {
    declarant.setNumeroPrefectoral(declarantDto.getNumero());
    declarant.setNom(declarantDto.getNom());
    declarant.setIdClientBO(declarantDto.getIdClientBO());
    declarant.setLibelle(declarantDto.getLibelle());
    declarant.setSiret(declarantDto.getSiret());
    declarant.setCodePartenaire(declarantDto.getCodePartenaire());
    declarant.setCodeCircuit(declarantDto.getCodeCircuit());
    declarant.setEmetteurDroits(declarantDto.getEmetteurDroits());
    declarant.setOperateurPrincipal(declarantDto.getOperateurPrincipal());
    declarant.setDateModification(new Date());
    declarant.setUserModification(declarantDto.getUser());
    declarant.setNumerosAMCEchanges(declarantDto.getNumerosAMCEchanges());
    setDeclarantTranscodageDomaineTP(declarantDto, declarant);
    setDeclarantPilotages(declarantDto, declarant);
    setDeclarantConventionTP(declarantDto, declarant);
    setDeclarantCodeRenvoiTP(declarantDto, declarant);
    setDeclarantRegroupementDomaineTP(declarantDto, declarant);
    setDeclarantFondCarteTP(declarantDto, declarant);
    declarant.setDelaiRetention(declarantDto.getDelaiRetention());
  }

  private void setDeclarantPilotages(DeclarantRequestDto declarantDto, Declarant declarant) {
    if (declarantDto.getPilotages() != null && !declarantDto.getPilotages().isEmpty()) {
      List<Pilotage> pilotages = new ArrayList<>();
      for (PilotageDto pdto : declarantDto.getPilotages()) {
        pilotages.addAll(mapperPilotage.dtoToEntityComplet(pdto));
      }
      declarant.setPilotages(pilotages);
    }
  }

  private void setDeclarantTranscodageDomaineTP(
      DeclarantRequestDto declarantDto, Declarant declarant) {
    List<TranscoDomainesTP> transcoDomainesTPList = new ArrayList<>();
    if (declarantDto.getTranscodageDomainesTP() != null
        && !declarantDto.getTranscodageDomainesTP().isEmpty()) {
      for (TranscoDomainesTPDto transcoDomainesTPDto : declarantDto.getTranscodageDomainesTP()) {
        transcoDomainesTPList.add(mapperTranscodageDomaineTP.dtoToEntity(transcoDomainesTPDto));
      }
    }
    declarant.setTranscodageDomainesTP(transcoDomainesTPList);
  }

  private void setDeclarantConventionTP(DeclarantRequestDto declarantDto, Declarant declarant) {
    List<ConventionTP> conventionTPList = new ArrayList<>();
    if (declarantDto.getConventionTP() != null && !declarantDto.getConventionTP().isEmpty()) {
      for (ConventionTPDto conventionTPDto : declarantDto.getConventionTP()) {
        conventionTPList.add(mapperConventionTP.dtoToEntity(conventionTPDto));
      }
    }
    declarant.setConventionTP(conventionTPList);
  }

  private void setDeclarantCodeRenvoiTP(DeclarantRequestDto declarantDto, Declarant declarant) {
    List<CodeRenvoiTP> codeRenvoiTPList = new ArrayList<>();
    if (declarantDto.getCodeRenvoiTP() != null && !declarantDto.getCodeRenvoiTP().isEmpty()) {
      for (CodeRenvoiTPDto codeRenvoiTPDto : declarantDto.getCodeRenvoiTP()) {
        codeRenvoiTPList.add(mapperCodeRenvoiTP.dtoToEntity(codeRenvoiTPDto));
      }
    }
    declarant.setCodeRenvoiTP(codeRenvoiTPList);
  }

  private void setDeclarantRegroupementDomaineTP(
      DeclarantRequestDto declarantDto, Declarant declarant) {
    List<RegroupementDomainesTP> regroupementDomainesTPList = new ArrayList<>();
    if (declarantDto.getRegroupementDomainesTP() != null
        && !declarantDto.getRegroupementDomainesTP().isEmpty()) {
      for (RegroupementDomainesTPDto regroupementDomainesTPDto :
          declarantDto.getRegroupementDomainesTP()) {
        regroupementDomainesTPList.add(
            mapperRegroupementDomainesTP.dtoToEntity(regroupementDomainesTPDto));
      }
    }
    declarant.setRegroupementDomainesTP(regroupementDomainesTPList);
  }

  private void setDeclarantFondCarteTP(DeclarantRequestDto declarantDto, Declarant declarant) {
    List<FondCarteTP> fondCarteTPList = new ArrayList<>();
    if (declarantDto.getFondCarteTP() != null && !declarantDto.getFondCarteTP().isEmpty()) {
      for (FondCarteTPDto fondCarteTPDto : declarantDto.getFondCarteTP()) {
        fondCarteTPList.add(mapperFondCarteTP.dtoToEntity(fondCarteTPDto));
      }
    }
    declarant.setFondCarteTP(fondCarteTPList);
  }

  @Override
  @ContinueSpan(log = "updateDeclarant")
  public void updateDeclarant(DeclarantRequestDto declarantDto) {
    Declarant declarant = findById(declarantDto.getNumero());

    if (declarant != null
        && declarant.getDelaiRetention() != null
        && !declarant.getDelaiRetention().equals(declarantDto.getDelaiRetention())) {
      eventService.sendObservabilityEventDelaiRetentionModification(
          declarant, declarantDto.getDelaiRetention());
    }

    if (declarant != null) {
      // MAJ declarant
      initializeDeclarant(declarantDto, declarant);

      getDeclarantDao().update(declarant);
    } else {
      // Creation declarant
      declarant = createDeclarant(declarantDto);
    }

    // Creation hist. declarant
    HistoriqueDeclarant historiqueDeclarant =
        mapperHistoriqueDeclarant(declarant, declarantDto.getPilotages());
    historiqueDeclarantDao.create(historiqueDeclarant);
  }

  /**
   * Mapper declarant dans historiqueDeclarant.
   *
   * @param declarant declarant a mapper dans historiqueDeclarant
   * @param pilotagesDto liste pilotages dto
   * @return historique Declarant
   */
  private HistoriqueDeclarant mapperHistoriqueDeclarant(
      Declarant declarant, List<PilotageDto> pilotagesDto) {

    HistoriqueDeclarant historiqueDeclarant = new HistoriqueDeclarant();
    historiqueDeclarant.setNumeroPrefectoral(declarant.getNumeroPrefectoral());
    historiqueDeclarant.setNom(declarant.getNom());
    historiqueDeclarant.setLibelle(declarant.getLibelle());
    historiqueDeclarant.setSiret(declarant.getSiret());
    historiqueDeclarant.setCodePartenaire(declarant.getCodePartenaire());
    historiqueDeclarant.setCodeCircuit(declarant.getCodeCircuit());
    historiqueDeclarant.setEmetteurDroits(declarant.getEmetteurDroits());
    historiqueDeclarant.setOperateurPrincipal(declarant.getOperateurPrincipal());
    historiqueDeclarant.setUserCreation(declarant.getUserCreation());
    historiqueDeclarant.setDateCreation(declarant.getDateCreation());
    historiqueDeclarant.setUserModification(declarant.getUserModification());
    historiqueDeclarant.setDateModification(declarant.getDateModification());

    if (pilotagesDto != null && !pilotagesDto.isEmpty()) {
      List<Pilotage> pilotages = new ArrayList<>();
      for (PilotageDto pdto : pilotagesDto) {
        pilotages.addAll(mapperPilotage.dtoToEntityComplet(pdto));
      }
      historiqueDeclarant.setPilotages(pilotages);
    }

    return historiqueDeclarant;
  }

  @Override
  @ContinueSpan(log = "findListDtoByUser ServicesDeclarantDto")
  public List<ServicesDeclarantDto> findListDtoByUser(String user, int page, int pageSize) {
    List<ServicesDeclarantDto> declarantsDto = new ArrayList<>();
    List<Declarant> declarants = getDeclarantDao().findByUser(user, page, pageSize);
    for (Declarant declarant : declarants) {
      ServicesDeclarantDto declarantDto = new ServicesDeclarantDto();
      declarantDto.setNumero(declarant.getNumeroPrefectoral());
      declarantDto.setNom(declarant.getNom());
      declarantDto.setUser(user);
      declarantDto.setLibelle(declarant.getLibelle());
      if (declarant.getPilotages() != null && !declarant.getPilotages().isEmpty()) {
        List<ServiceCouloirsDto> services = findServices(declarant.getPilotages(), null, null);
        declarantDto.setServices(services);
      }
      declarantsDto.add(declarantDto);
    }

    return declarantsDto;
  }

  @Override
  @ContinueSpan(log = "findTotalDeclarantsByUser")
  public long findTotalDeclarantsByUser(String user) {
    return getDeclarantDao().findTotalDeclarantsByUser(user);
  }

  @Override
  @ContinueSpan(log = "validationPilotagesRequestDto")
  public List<PilotageDto> validationPilotagesRequestDto(List<PilotageDto> pilotagesDto)
      throws ExceptionServiceFormatDate, RequestValidationException {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    for (PilotageDto pilotageDto : pilotagesDto) {
      if (pilotageDto != null) {
        for (InfoPilotageDto infoDto : pilotageDto.getRegroupements()) {
          verifyDateOuverture(sdf, infoDto);
          // Verifier la date synchronisation
          veridyDateSynchronisation(sdf, infoDto);

          // Verifier la periode reference debut
          verifyPeriodeReferenceDebut(sdf, infoDto);

          // Verifier la periode reference fin
          verifyPeriodeReferenceFin(sdf, infoDto);

          // Vérifier les validitesDomainesDroits
          verifyValiditesDomainesDroits(infoDto);
        }
      }
    }
    return pilotagesDto;
  }

  private void verifyValiditesDomainesDroits(InfoPilotageDto infoDto)
      throws RequestValidationException {
    List<ValiditeDomainesDroitsDto> validites = infoDto.getValiditesDomainesDroits();

    for (ValiditeDomainesDroitsDto validite : validites) {
      this.verifyValidite(validite);
    }
  }

  private void verifyValidite(ValiditeDomainesDroitsDto validite)
      throws RequestValidationException {
    String jours = "Jours";

    if (StringUtils.isEmpty(validite.getCodeDomaine())) {
      throw new RequestValidationException("Le codeDomaine est absent", HttpStatus.BAD_REQUEST);
    }
    if (validite.getDuree() <= 0) {
      throw new RequestValidationException(
          "La durée doit exister et être positive", HttpStatus.BAD_REQUEST);
    }
    if (!"Mois".equals(validite.getUnite()) && !jours.equals(validite.getUnite())) {
      throw new RequestValidationException(
          "L'unite doit exister et ne peut être que 'Jours' ou 'Mois'", HttpStatus.BAD_REQUEST);
    }
    if (jours.equals(validite.getUnite())
        && Boolean.TRUE.equals(validite.isPositionnerFinDeMois())) {
      throw new RequestValidationException(
          "Le booléen positionnerFinDeMois ne peut être 'true' que si unite == 'Mois'",
          HttpStatus.BAD_REQUEST);
    }
    if ((jours.equals(validite.getUnite()) && validite.getDuree() > 365)
        || ("Mois".equals(validite.getUnite()) && validite.getDuree() > 12)) {
      throw new RequestValidationException(
          "La durée ne peut pas excéder 365 jours ou 12 mois", HttpStatus.BAD_REQUEST);
    }
  }

  private void verifyPeriodeReferenceFin(SimpleDateFormat sdf, InfoPilotageDto infoDto) {
    if (infoDto != null
        && infoDto.getPeriodeReferenceFin() != null
        && !infoDto.getPeriodeReferenceFin().trim().isEmpty()) {
      StringBuilder periodeReferenceFin =
          new StringBuilder(infoDto.getPeriodeReferenceFin().trim());
      if (periodeReferenceFin.length() == 10) {
        periodeReferenceFin.append(ZERO_TIME);
      }
      Date dateTimePeriodeReferenceFin;
      try {
        dateTimePeriodeReferenceFin = sdf.parse(periodeReferenceFin.toString());
      } catch (ParseException e) {
        throw new ExceptionServiceFormatDate();
      }
      if (!periodeReferenceFin.toString().equals(sdf.format(dateTimePeriodeReferenceFin))) {
        throw new ExceptionServiceFormatDate();
      }
      infoDto.setDateTimePeriodeReferenceFin(dateTimePeriodeReferenceFin);
    }
  }

  private void verifyPeriodeReferenceDebut(SimpleDateFormat sdf, InfoPilotageDto infoDto) {
    if (infoDto != null
        && infoDto.getPeriodeReferenceDebut() != null
        && !infoDto.getPeriodeReferenceDebut().trim().isEmpty()) {
      StringBuilder periodeReferenceDebut =
          new StringBuilder(infoDto.getPeriodeReferenceDebut().trim());
      if (periodeReferenceDebut.length() == 10) {
        periodeReferenceDebut.append(ZERO_TIME);
      }
      Date dateTimePeriodeReferenceDebut;
      try {
        dateTimePeriodeReferenceDebut = sdf.parse(periodeReferenceDebut.toString());
      } catch (ParseException e) {
        throw new ExceptionServiceFormatDate();
      }
      if (!periodeReferenceDebut.toString().equals(sdf.format(dateTimePeriodeReferenceDebut))) {
        throw new ExceptionServiceFormatDate();
      }
      infoDto.setDateTimePeriodeReferenceDebut(dateTimePeriodeReferenceDebut);
    }
  }

  private void veridyDateSynchronisation(SimpleDateFormat sdf, InfoPilotageDto infoDto) {
    if (infoDto != null
        && infoDto.getDateSynchronisation() != null
        && !infoDto.getDateSynchronisation().trim().isEmpty()) {
      StringBuilder dateSynchronisation =
          new StringBuilder(infoDto.getDateSynchronisation().trim());
      if (dateSynchronisation.length() == 10) {
        dateSynchronisation.append(ZERO_TIME);
      }
      Date dateTimeSynchronisation;
      try {
        dateTimeSynchronisation = sdf.parse(dateSynchronisation.toString());
      } catch (ParseException e) {
        throw new ExceptionServiceFormatDate();
      }
      if (!dateSynchronisation.toString().equals(sdf.format(dateTimeSynchronisation))) {
        throw new ExceptionServiceFormatDate();
      }
      infoDto.setDateTimeSynchronisation(dateTimeSynchronisation);
    }
  }

  private void verifyDateOuverture(SimpleDateFormat sdf, InfoPilotageDto infoDto) {
    // Verifier la date ouverture
    if (infoDto != null
        && infoDto.getDateOuverture() != null
        && !infoDto.getDateOuverture().trim().isEmpty()) {
      StringBuilder dateOuverture = new StringBuilder(infoDto.getDateOuverture().trim());
      if (dateOuverture.length() == 10) {
        dateOuverture.append(ZERO_TIME);
      }
      Date dateTimeOuverture;
      try {
        dateTimeOuverture = sdf.parse(dateOuverture.toString());
      } catch (ParseException e) {
        throw new ExceptionServiceFormatDate();
      }
      if (!dateOuverture.toString().equals(sdf.format(dateTimeOuverture))) {
        throw new ExceptionServiceFormatDate();
      }
      infoDto.setDateTimeOuverture(dateTimeOuverture);
    }
  }

  @Override
  @ContinueSpan(log = "findAllDeclarantsEchanges")
  public DeclarantEchangeDto findAllDeclarantsEchanges() {
    return mapperDeclarantEchange.entityToDto(getDeclarantDao().findAllDeclarantsEchanges());
  }

  /**
   * Recherche dans les pilotage les services
   *
   * @param pilotages liste des pilotage.
   * @return liste des services trouves.
   */
  private List<ServiceCouloirsDto> findServices(
      List<Pilotage> pilotages, String couloir, String service) {

    Map<String, List<String>> mapServiceCouloirs = new HashMap<>();
    Date dateToday = new Date();
    boolean couloirFound = StringUtils.isBlank(couloir);
    boolean serviceFound = StringUtils.isBlank(service);

    for (Pilotage pilotage : pilotages) {
      List<String> listeCouloirs = mapServiceCouloirs.get(pilotage.getCodeService());
      if (listeCouloirs == null) {
        listeCouloirs = new ArrayList<>();
      }
      if (!listeCouloirs.contains(pilotage.getCouloirClient())
          && pilotage.getDateOuverture() != null
          && Boolean.TRUE.equals(pilotage.getServiceOuvert())
          && dateToday.after(pilotage.getDateOuverture())) {
        setListeCouloirs(pilotage, listeCouloirs);
        mapServiceCouloirs.put(pilotage.getCodeService(), listeCouloirs);
        serviceFound = isServiceFound(service, serviceFound, pilotage);
        couloirFound = isCouloirFound(couloir, couloirFound, pilotage);
      }
    }
    if (!serviceFound || !couloirFound) {
      return new ArrayList<>();
    }
    List<ServiceCouloirsDto> listServiceCouloirs = new ArrayList<>();
    for (Map.Entry<String, List<String>> serviceCouloir : mapServiceCouloirs.entrySet()) {
      ServiceCouloirsDto serviceCouloirsDto = new ServiceCouloirsDto();
      serviceCouloirsDto.setService(serviceCouloir.getKey());
      serviceCouloirsDto.setListCouloirs(serviceCouloir.getValue());
      listServiceCouloirs.add(serviceCouloirsDto);
    }
    Collections.sort(listServiceCouloirs);
    return listServiceCouloirs;
  }

  private boolean isCouloirFound(String couloir, boolean couloirFound, Pilotage pilotage) {
    if (StringUtils.isNotBlank(pilotage.getCouloirClient())
        && StringUtils.isNotBlank(couloir)
        && pilotage.getCouloirClient().toUpperCase().contains(couloir.toUpperCase())) {
      couloirFound = true;
    }
    return couloirFound;
  }

  private boolean isServiceFound(String service, boolean serviceFound, Pilotage pilotage) {
    if (StringUtils.isNotBlank(pilotage.getCodeService())
        && StringUtils.isNotBlank(service)
        && pilotage.getCodeService().equalsIgnoreCase(service.toUpperCase())) {
      serviceFound = true;
    }
    return serviceFound;
  }

  private void setListeCouloirs(Pilotage pilotage, List<String> listeCouloirs) {
    if (StringUtils.isNotBlank(pilotage.getCouloirClient())) {
      listeCouloirs.add(pilotage.getCouloirClient());
    }
  }

  private boolean isCriteriaValid(String numero, String nom, String service, String codeCouloir) {
    int numeroPresent = StringUtils.isBlank(numero) ? 0 : 1;
    int nomPresent = StringUtils.isBlank(nom) ? 0 : 1;
    int serviceCouloirPresent =
        StringUtils.isBlank(service) && StringUtils.isBlank(codeCouloir) ? 0 : 1;
    return (numeroPresent + nomPresent + serviceCouloirPresent) == 1;
  }

  @ContinueSpan(log = "transcodeDomain")
  public String transcodeDomain(String insurerId, String domains) {
    if (StringUtils.isNotBlank(insurerId)
        && StringUtils.isNotBlank(domains)
        && !domains.contains(",")) {
      Declarant declarant = declarantDao.findById(insurerId);
      if (declarant != null && !CollectionUtils.isEmpty(declarant.getTranscodageDomainesTP())) {
        List<TranscoDomainesTP> domainsList =
            declarant.getTranscodageDomainesTP().stream()
                .filter(
                    transcoDomainesTP ->
                        transcoDomainesTP.getDomaineSource() != null
                            && domains.contains(transcoDomainesTP.getDomaineSource()))
                .toList();
        if (!CollectionUtils.isEmpty(domainsList)) {
          return String.join(",", domainsList.get(0).getDomainesCible());
        }
      }
    }
    return domains;
  }

  @ContinueSpan(log = "transcodeDomain")
  public List<String> transcodeDomain(String insurerId, List<String> domains) {
    if (StringUtils.isNotBlank(insurerId)
        && !CollectionUtils.isEmpty(domains)
        && domains.size() == 1) {
      Declarant declarant = declarantDao.findById(insurerId);
      if (declarant != null && !CollectionUtils.isEmpty(declarant.getTranscodageDomainesTP())) {
        List<TranscoDomainesTP> domainsList =
            declarant.getTranscodageDomainesTP().stream()
                .filter(transcoDomainesTP -> domains.contains(transcoDomainesTP.getDomaineSource()))
                .toList();
        if (!CollectionUtils.isEmpty(domainsList)) {
          return domainsList.get(0).getDomainesCible();
        }
      }
    }
    return domains;
  }
}
