package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarantBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarationBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDeclaration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDroits;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperHistoriqueDeclarations;
import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("declarationBackendService")
@Slf4j
public class DeclarationBackendServiceImpl extends GenericServiceImpl<Declaration>
    implements DeclarationBackendService {

  private final MapperDeclaration mapperDeclaration;

  private final DeclarantBackendDao declarantDao;

  private final MapperHistoriqueDeclarations mapperHistoriqueDeclarations;

  private final MapperDroits mapperDroits;

  private final CarteDematDao carteDematDao;

  /**
   * public constructeur.
   *
   * @param declarationDao bean dao injecte
   */
  @Autowired
  public DeclarationBackendServiceImpl(
      @Qualifier("declarationBackendDao") final IMongoGenericDao<Declaration> declarationDao,
      MapperDeclaration mapperDeclaration,
      MapperHistoriqueDeclarations mapperHistoriqueDeclarations,
      MapperDroits mapperDroits,
      CarteDematDao carteDematDao,
      DeclarantBackendDao declarantDao) {
    super(declarationDao);
    this.mapperDeclaration = mapperDeclaration;
    this.mapperHistoriqueDeclarations = mapperHistoriqueDeclarations;
    this.mapperDroits = mapperDroits;
    this.carteDematDao = carteDematDao;
    this.declarantDao = declarantDao;
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Declaration}
   */
  @Override
  @ContinueSpan(log = "getDeclarationDao")
  public DeclarationBackendDao getDeclarationDao() {
    return ((DeclarationBackendDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findById InfosAssureDto")
  public InfosAssureDto findById(final String id, boolean requestFromBenefTpDetails) {
    final Declaration declaration = this.getDeclarationDao().findById(id, Declaration.class);
    final List<CarteDemat> carteDematList =
        carteDematDao.findCarteByDeclarantAndAmcContrat(
            declaration.getIdDeclarant(),
            declaration.getContrat().getNumero(),
            declaration.get_id());

    List<String> periodeDebutList =
        carteDematList.stream().map(CarteDemat::getPeriodeDebut).distinct().toList();
    List<CarteDemat> latestDematCards = new ArrayList<>();
    for (String periodeDebut : periodeDebutList) {
      Optional<CarteDemat> lastDematCardForPeriod =
          carteDematList.stream()
              .filter(carteDemat -> periodeDebut.equals(carteDemat.getPeriodeDebut()))
              .findFirst();
      lastDematCardForPeriod.ifPresent(latestDematCards::add);
    }

    return this.mapperDeclaration.entityToDto(
        declaration, latestDematCards, requestFromBenefTpDetails);
  }

  @Override
  @ContinueSpan(log = "findLastDeclaration InfosAssureDto")
  public DroitsDto findLastDeclaration() {
    final List<Declaration> declarations = this.getDeclarationDao().findAll(Declaration.class);
    DroitsDto droitsDto = null;
    if (!declarations.isEmpty()) {
      int indice = declarations.size();
      Declaration lastDeclaration = declarations.get(indice - 1);
      droitsDto = mapperDroits.entityToDto(lastDeclaration);
    }
    return droitsDto;
  }

  @ContinueSpan(log = "declarationRework")
  public Map<String, RechercheDroitDto> declarationRework(
      final List<Declaration> declarations, final boolean keepOrder) {
    final Map<String, RechercheDroitDto> mapResult = new LinkedHashMap<>();
    String key = "0";
    for (final Declaration declaration : declarations) {
      if (StringUtils.isNotBlank(declaration.getIdDeclarant())
          && declaration.getContrat() != null
          && declaration.getBeneficiaire() != null
          && StringUtils.isNotBlank(declaration.getContrat().getNumeroAdherent())
          && StringUtils.isNotBlank(declaration.getBeneficiaire().getDateNaissance())
          && StringUtils.isNotBlank(declaration.getBeneficiaire().getRangNaissance())) {
        if (keepOrder) {
          key = String.valueOf(Integer.parseInt(key) + 1);
        } else {
          key =
              declaration.getContrat().getNumeroAdherent()
                  + declaration.getBeneficiaire().getDateNaissance()
                  + declaration.getBeneficiaire().getRangNaissance()
                  + declaration.getIdDeclarant();
        }
        final RechercheDroitDto droitDto;
        if (!mapResult.containsKey(key)) {
          final RechercheBeneficiaireDroitDto beneficiaireDroitDto =
              this.getBeneficiaireDroitDto(declaration);
          beneficiaireDroitDto.setDroitsOuverts(false);
          droitDto = new RechercheDroitDto();
          droitDto.setBeneficiaire(beneficiaireDroitDto);
        } else {
          droitDto = mapResult.get(key);
        }
        final RechercheContratDroitDto contratDroitDto = this.getContratDroitDto(declaration);
        droitDto
            .getBeneficiaire()
            .setDroitsOuverts(
                droitDto.getBeneficiaire().isDroitsOuverts() || contratDroitDto.isDroitsOuverts());
        droitDto.getContrats().add(contratDroitDto);
        droitDto.setTotalContrats(droitDto.getContrats().size());
        final String qualitePremierContrat = droitDto.getContrats().first().getQualite();
        droitDto.getBeneficiaire().setQualite(qualitePremierContrat);
        mapResult.put(key, droitDto);
      }
    }
    return mapResult;
  }

  @ContinueSpan(log = "declarationToDroits")
  public List<RechercheDroitDto> declarationToDroits(
      final List<Declaration> declarations, final boolean keepOrder) {
    final Map<String, RechercheDroitDto> mapResult =
        this.declarationRework(declarations, keepOrder);

    final List<RechercheDroitDto> droits = new ArrayList<>(mapResult.values());
    if (!keepOrder) {
      droits.sort(RechercheDroitDto::compareTo);
    }
    return droits;
  }

  @Override
  @ContinueSpan(log = "getInfoDroitsAssures List<RechercheDroitDto>")
  public List<RechercheDroitDto> getInfoDroitsAssures(String idDeclarant, String numeroPersonne) {
    final Long sizeRecherche =
        this.getDeclarationDao().countDeclarationByCriteria(idDeclarant, numeroPersonne);
    if (sizeRecherche > Constants.MAX_DECLARATIONS_FOR_UI) {
      log.warn(
          "There are too many results {}. We only keep the {} most recent results.",
          sizeRecherche,
          Constants.MAX_DECLARATIONS_FOR_UI);
    }
    // Recupération des infos assurées
    final List<Declaration> declarations =
        this.getDeclarationDao()
            .findDeclarationsByCriteria(
                idDeclarant, numeroPersonne, Constants.MAX_ITEMS_PER_LOAD_UI);

    return this.declarationToDroits(declarations, false);
  }

  private RechercheBeneficiaireDroitDto getBeneficiaireDroitDto(final Declaration declaration) {

    final RechercheBeneficiaireDroitDto beneficiaireDroitDto = new RechercheBeneficiaireDroitDto();
    // la date de naissance peut être lunaire, on se contente de la
    // formatter en jj/mm/aaaa
    // sachant qu'elle est stockée en aaaammjj (parse calcule une date si
    // format invalide)
    beneficiaireDroitDto.setDateNaissance(declaration.getBeneficiaire().getDateNaissance());
    if (StringUtils.isNotEmpty(declaration.getBeneficiaire().getDateNaissance())) {
      final String dateNaissance =
          declaration.getBeneficiaire().getDateNaissance().substring(6, 8)
              + "/"
              + declaration.getBeneficiaire().getDateNaissance().substring(4, 6)
              + "/"
              + declaration.getBeneficiaire().getDateNaissance().substring(0, 4);
      beneficiaireDroitDto.setDateNaissance(dateNaissance);
    }

    beneficiaireDroitDto.setRangNaissance(declaration.getBeneficiaire().getRangNaissance());
    beneficiaireDroitDto.setNirBeneficiaire(declaration.getBeneficiaire().getNirBeneficiaire());
    beneficiaireDroitDto.setCleNirBeneficiaire(
        declaration.getBeneficiaire().getCleNirBeneficiaire());
    beneficiaireDroitDto.setNirOd1(declaration.getBeneficiaire().getNirOd1());
    beneficiaireDroitDto.setCleNirOd1(declaration.getBeneficiaire().getCleNirOd1());
    beneficiaireDroitDto.setNom(declaration.getBeneficiaire().getAffiliation().getNom());
    beneficiaireDroitDto.setPrenom(declaration.getBeneficiaire().getAffiliation().getPrenom());
    beneficiaireDroitDto.setCivilite(declaration.getBeneficiaire().getAffiliation().getCivilite());
    if (declaration.getIdDeclarant() != null) {
      final Declarant declarant = this.declarantDao.findById(declaration.getIdDeclarant());
      if (declarant != null) {
        beneficiaireDroitDto.setAMC(declarant.get_id() + " - " + declarant.getNom());
      }
    }
    return beneficiaireDroitDto;
  }

  /**
   * Construit le contrat dto
   *
   * @param declaration declaration dto
   * @return droit dto.
   */
  private RechercheContratDroitDto getContratDroitDto(final Declaration declaration) {
    final RechercheContratDroitDto contratDroitDto = new RechercheContratDroitDto();

    contratDroitDto.setNumero(declaration.getContrat().getNumero());
    contratDroitDto.setNumeroAdherent(declaration.getContrat().getNumeroAdherent());
    contratDroitDto.setNumeroContratCollectif(declaration.getContrat().getNumeroContratCollectif());
    contratDroitDto.setQualification(declaration.getContrat().getQualification());
    contratDroitDto.setQualite(declaration.getBeneficiaire().getAffiliation().getQualite());
    contratDroitDto.setIdTechniqueDeclaration(declaration.get_id());
    contratDroitDto.setDroitsOuverts("V".equals(declaration.getCodeEtat()));
    contratDroitDto.setHistorique(mapperHistoriqueDeclarations.entityToDto(declaration));
    return contratDroitDto;
  }
}
