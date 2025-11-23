package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.utility.ParametersEnum;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.DomainePrestation;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** Classe d'accès aux services lies aux {@code ParametreBdd}. */
@Service("parametreBddService")
public class ParametreBddServiceImpl extends GenericServiceImpl<ParametreBdd>
    implements ParametreBddService {

  private static final String LE_PARAMETRE = "Le paramètre ";
  private static final String N_EXISTE_PAS = " n'existe pas!";
  private static final String CODE = "code";
  private static final String LIBELLE = "libelle";
  private static final String ORDRE = "ordre";
  private static final String ICONE = "icone";
  private static final String REJETS = "rejets";
  private static final String CODES_RENVOI = "codesRenvoi";
  private static final String TYPE_ERREUR = "typeErreur";
  private static final String NIVEAU_ERREUR = "niveauErreur";
  private static final String MOTIF = "motif";
  private static final String TRANSCODIFICATION = "transcodification";
  private static final String CATEGORIE = "categorie";
  private static final String PRIORITE = "priorite";
  private static final String IS_CUMUL_GARANTIES = "isCumulGaranties";
  private static final String IS_CUMUL_PLAFONNE = "isCumulPlafonne";
  private static final String PARAM1 = "param1";
  private static final String PARAM2 = "param2";
  private static final String PARAM3 = "param3";
  private static final String PARAM4 = "param4";
  private static final String PARAM5 = "param5";
  private static final String PARAM6 = "param6";
  private static final String PARAM7 = "param7";
  private static final String PARAM8 = "param8";
  private static final String PARAM9 = "param9";
  private static final String PARAM10 = "param10";
  private static final String DOMAINES = "domaines";
  private static final String V1 = "V1";
  private static final String WHITE_SPACE = " ";

  /**
   * public constructeur.
   *
   * @param parametreBddDao bean dao injecte
   */
  @Autowired
  public ParametreBddServiceImpl(
      @Qualifier("parametreBddDao") final IMongoGenericDao<ParametreBdd> parametreBddDao) {
    super(parametreBddDao);
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code ParametreBdd}
   */
  @Override
  @ContinueSpan(log = "getParametreBddDao")
  public ParametreBddDao getParametreBddDao() {
    return ((ParametreBddDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findByType ParametresDto")
  @Cacheable(value = "parametreByTypeCache", key = "{#type}")
  public List<ParametresDto> findByType(final String type) {
    final List<ParametresDto> listeParametresDto = new ArrayList<>();
    final ParametreBdd bddParameter = this.getParametreBddDao().findParametres(type);
    final ParametresDtoFactory parametresDtoFactory = new ParametresDtoFactory();
    if (bddParameter != null) {
      for (final Object object : bddParameter.getListeValeurs()) {
        LinkedHashMap<String, Object> mapObject;
        if (object instanceof Map) {
          mapObject = (LinkedHashMap<String, Object>) object;
        } else {
          mapObject = new LinkedHashMap<>();
          mapObject.put(CODE, object);
        }
        final ParametresDto parametresDto =
            this.initializeParametresDto(type, parametresDtoFactory, mapObject);
        listeParametresDto.add(parametresDto);
      }
      Collections.sort(listeParametresDto);
    }
    return listeParametresDto;
  }

  @Override
  @ContinueSpan(log = "findPrestationsByDomaine")
  public List<ParametresPrestationDto> findPrestationsByDomaine(
      final String type, final String domaine) {
    return this.getParametreBddDao().findPrestationsByDomain(domaine);
  }

  @Override
  @ContinueSpan(log = "findOneByType ParametresDto")
  @Cacheable(value = "parametreCache", key = "{#type,#code}")
  public ParametresDto findOneByType(final String type, final String code) {
    final ParametreBdd bddParameter = this.getParametreBddDao().findParametres(type);
    final ParametresDtoFactory parametresDtoFactory = new ParametresDtoFactory();
    if (bddParameter != null) {
      for (final Object object : bddParameter.getListeValeurs()) {
        final LinkedHashMap<String, Object> mapObject = (LinkedHashMap<String, Object>) object;
        if (mapObject.get(CODE).equals(code)) {
          return this.initializeParametresDto(type, parametresDtoFactory, mapObject);
        }
      }
    }
    return null;
  }

  @Override
  @ContinueSpan(log = "findRejets")
  public Map<String, ErreursDto> findRejets() {
    final Map<String, ErreursDto> mapParametresDto = new HashMap<>();
    final ParametreBdd parametre = this.getParametreBddDao().findParametres(REJETS);
    if (parametre != null) {
      for (final Object convention : parametre.getListeValeurs()) {
        final LinkedHashMap<String, Object> mapConvention =
            (LinkedHashMap<String, Object>) convention;
        final ErreursDto parametresDto = new ErreursDto();
        parametresDto.setCode((String) mapConvention.get(CODE));
        parametresDto.setLibelle((String) mapConvention.get(LIBELLE));
        parametresDto.setMotif((String) mapConvention.get(MOTIF));
        parametresDto.setTypeErreur((String) mapConvention.get(TYPE_ERREUR));
        parametresDto.setNiveauErreur((String) mapConvention.get(NIVEAU_ERREUR));
        mapParametresDto.put(parametresDto.getCode(), parametresDto);
      }
    }
    return mapParametresDto;
  }

  @Override
  @ContinueSpan(log = "findCodesRenvoi")
  public Map<String, CodesRenvoiDto> findCodesRenvoi() {
    final Map<String, CodesRenvoiDto> mapParametresDto = new HashMap<>();
    final ParametreBdd parametre = this.getParametreBddDao().findParametres(CODES_RENVOI);
    if (parametre != null) {
      for (final Object codeRenvoi : parametre.getListeValeurs()) {
        final LinkedHashMap<String, Object> mapCodeRenvoi =
            (LinkedHashMap<String, Object>) codeRenvoi;
        final CodesRenvoiDto parametresDto = new CodesRenvoiDto();
        parametresDto.setCode((String) mapCodeRenvoi.get(CODE));
        parametresDto.setLibelle((String) mapCodeRenvoi.get(LIBELLE));
        mapParametresDto.put(parametresDto.getCode(), parametresDto);
      }
    }
    return mapParametresDto;
  }

  @Override
  @ContinueSpan(log = "findLibelleCodeRenvoi")
  public String findLibelleCodeRenvoi(String codeRenvoi) {
    if (codeRenvoi == null) {
      return null;
    }

    Map<String, CodesRenvoiDto> map = findCodesRenvoi();
    if (map.containsKey(codeRenvoi)) {
      return map.get(codeRenvoi).getLibelle();
    }
    return null;
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "parametreCache", key = "{#type,#parametresDto.getCode()}"),
        @CacheEvict(cacheNames = "parametreByTypeCache", key = "#type")
      })
  public List<ParametresDto> saveOrUpdate(
      final String type,
      final ParametresDto parametresDto,
      final boolean update,
      final String version) {
    final ParametreBdd parametreBdd;
    if (!version.equals(V1)) {
      this.raiseExceptionIfClaimCodeContainsWhiteSpaces(parametresDto.getCode());
      final ParametresDto existingParametreDto = this.findOneByType(type, parametresDto.getCode());
      if (!update && existingParametreDto != null) {
        throw new ValidationException(LE_PARAMETRE + parametresDto.getCode() + " existe déjà!");
      } else if (update && existingParametreDto == null) {
        throw new ValidationException(LE_PARAMETRE + parametresDto.getCode() + N_EXISTE_PAS);
      }
    }
    if (!update) {
      try {
        this.mapDtoToEntityByType(type, parametresDto);
      } catch (final ValidationException e) {
        throw new ValidationException(e.getMessage());
      }
    } else {
      try {
        parametreBdd = this.mapDtoToExistingEntityByType(type, parametresDto);
      } catch (final ValidationException e) {
        throw new ValidationException(e.getMessage());
      }
      this.getParametreBddDao().update(parametreBdd);
    }

    return this.findByType(type);
  }

  @Override
  public void insert(final String type, final ParametresDto parametresDto) {
    try {
      this.mapDtoToEntityByType(type, parametresDto);
    } catch (final ValidationException e) {
      throw new ValidationException(e.getMessage());
    }
  }

  /**
   * Raise an exception if claim code contains white space(s)
   *
   * @param claimCode Claim code
   * @throws ValidationException
   */
  private void raiseExceptionIfClaimCodeContainsWhiteSpaces(final String claimCode)
      throws ValidationException {
    if (claimCode != null && claimCode.contains(WHITE_SPACE)) {
      throw new ValidationException(Constants.CLAIM_CODE_CONTAINS_WHITE_SPACE);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "parametreCache", key = "{#type,#code}"),
        @CacheEvict(cacheNames = "parametreByTypeCache", key = "#type")
      })
  public List<ParametresDto> remove(final String type, final String code, final String version) {
    boolean parameterFound = false;
    if (version.equals(V1)) {
      parameterFound = true;
    } else {
      this.raiseExceptionIfClaimCodeContainsWhiteSpaces(code);
    }
    // Searching for the parameter to delete
    final ParametreBdd parametreBdd = this.getParametreBddDao().findParametres(type);
    if (parametreBdd != null) {
      for (final Object o : parametreBdd.getListeValeurs()) {
        final LinkedHashMap<String, Object> mapObject = (LinkedHashMap<String, Object>) o;
        if (mapObject.get(CODE).equals(code)) {
          parametreBdd.getListeValeurs().remove(mapObject);

          this.getParametreBddDao().update(parametreBdd);
          if (!version.equals(V1)) {
            parameterFound = true;
          }
          break;
        }
      }
    }
    if (!parameterFound) {
      throw new ValidationException(LE_PARAMETRE + code + N_EXISTE_PAS);
    }
    // Return dto
    return this.findByType(type);
  }

  private ParametresDto initializeParametresDto(
      final String type,
      final ParametresDtoFactory parametresDtoFactory,
      final LinkedHashMap<String, Object> mapObject) {
    final ParametresDto parametresDto = parametresDtoFactory.getParametresDto(type);
    parametresDto.setCode((String) mapObject.get(CODE));
    parametresDto.setLibelle((String) mapObject.get(LIBELLE));
    if (parametresDto instanceof ParametresServicesMetiersDto parametresServicesMetiersDto) {
      parametresServicesMetiersDto.setOrdre((String) mapObject.get(ORDRE));
      parametresServicesMetiersDto.setIcone((String) mapObject.get(ICONE));
    }

    // Mapping by type
    this.mapByType(type, parametresDto, mapObject);
    return parametresDto;
  }

  private void mapByType(
      final String type, final ParametresDto parameter, final HashMap<String, Object> mapObject) {
    if (parameter instanceof ParametresDomaineDto
        && (ParametersEnum.DOMAINE_IS.getType().equals(type)
            || ParametersEnum.DOMAINE_SP.getType().equals(type)
            || ParametersEnum.DOMAINE.getType().equals(type))) {
      this.mapTypeDomaineToExistingDto(parameter, mapObject);
    } else if (parameter instanceof ParametresServicesMetiersDto) {
      this.mapTypeServicesMetiersToExistingDto(parameter, mapObject);
    } else if (parameter instanceof ParametresFormuleDto) {
      this.mapTypeFormuleToExistingDto(parameter, mapObject);
    } else if (parameter instanceof ParametresPrestationDto) {
      this.mapTypePrestationToExistingDto(parameter, mapObject);
    }
  }

  private void mapDtoToEntityByType(final String type, final ParametresDto parametresDto) {

    ParametreBdd parametreBdd = this.getParametreBddDao().findParametres(type);
    final HashMap<String, Object> mapParameter = new LinkedHashMap<>();
    mapParameter.put(CODE, parametresDto.getCode());
    this.buildMapParameter(parametresDto, mapParameter);
    try {
      this.testExistingDomaines(parametresDto);
    } catch (final ValidationException e) {
      throw new ValidationException(e.getMessage());
    }

    if (parametreBdd != null) {
      parametreBdd.getListeValeurs().add(mapParameter);
      this.getParametreBddDao().update(parametreBdd);
    } else {
      parametreBdd = new ParametreBdd();
      parametreBdd.setCode(type);
      final List<Object> valueList = new ArrayList<>();
      valueList.add(mapParameter);
      parametreBdd.setListeValeurs(valueList);
      this.getParametreBddDao().create(parametreBdd);
    }
  }

  /**
   * Contrôle l'existence des domaines
   *
   * @param parametresDto Objet contenant la liste des domaines
   */
  private void testExistingDomaines(final ParametresDto parametresDto) {
    if (parametresDto instanceof ParametresPrestationDto parametresPrestationDto) {
      final List<DomainePrestation> domaines = parametresPrestationDto.getDomaines();
      if (!CollectionUtils.isEmpty(domaines)) {
        for (final DomainePrestation domaine : domaines) {
          final ParametresDto bddDomaine =
              this.findOneByType(ParametersEnum.DOMAINE.getType(), domaine.getCodeDomaine());
          if (bddDomaine == null) {
            throw new ValidationException("Le domaine " + domaine.getCodeDomaine() + N_EXISTE_PAS);
          }
        }
      }
    }
  }

  /**
   * Map a paramete to an existing dto
   *
   * @param type
   * @param parametresDto
   * @return
   */
  private ParametreBdd mapDtoToExistingEntityByType(
      final String type, final ParametresDto parametresDto) {
    try {
      this.testExistingDomaines(parametresDto);
    } catch (final ValidationException e) {
      throw new ValidationException(e.getMessage());
    }

    final ParametreBdd parametreBdd = this.getParametreBddDao().findParametres(type);

    if (parametreBdd != null) {
      // Watching for the existing parameter
      for (final Object object : parametreBdd.getListeValeurs()) {
        final HashMap<String, Object> mapParameter = (LinkedHashMap<String, Object>) object;
        if (mapParameter.get(CODE).equals(parametresDto.getCode())) {
          this.buildMapParameter(parametresDto, mapParameter);
        }
      }
    }

    return parametreBdd;
  }

  /**
   * Build the map object parameter
   *
   * @param parametresDto Parameter Dto
   * @param mapParameter Map to build
   */
  private void buildMapParameter(
      final ParametresDto parametresDto, final HashMap<String, Object> mapParameter) {
    mapParameter.put(LIBELLE, parametresDto.getLibelle());

    if (parametresDto instanceof ParametresDomaineDto parametresDomaineDto) {
      mapParameter.put(TRANSCODIFICATION, parametresDomaineDto.getTranscodification());
      mapParameter.put(CATEGORIE, parametresDomaineDto.getCategorie());
      mapParameter.put(PRIORITE, parametresDomaineDto.getPriorite());
      mapParameter.put(IS_CUMUL_GARANTIES, parametresDomaineDto.getIsCumulGaranties());
      mapParameter.put(IS_CUMUL_PLAFONNE, parametresDomaineDto.getIsCumulPlafonne());
    } else if (parametresDto instanceof ParametresServicesMetiersDto parametresServicesMetiersDto) {
      mapParameter.put(ORDRE, parametresServicesMetiersDto.getOrdre());
      mapParameter.put(ICONE, parametresServicesMetiersDto.getIcone());
    } else if (parametresDto instanceof ParametresFormuleDto parametresFormuleDto) {
      mapParameter.put(PARAM1, parametresFormuleDto.getParam1());
      mapParameter.put(PARAM2, parametresFormuleDto.getParam2());
      mapParameter.put(PARAM3, parametresFormuleDto.getParam3());
      mapParameter.put(PARAM4, parametresFormuleDto.getParam4());
      mapParameter.put(PARAM5, parametresFormuleDto.getParam5());
      mapParameter.put(PARAM6, parametresFormuleDto.getParam6());
      mapParameter.put(PARAM7, parametresFormuleDto.getParam7());
      mapParameter.put(PARAM8, parametresFormuleDto.getParam8());
      mapParameter.put(PARAM9, parametresFormuleDto.getParam9());
      mapParameter.put(PARAM10, parametresFormuleDto.getParam10());
    } else if (parametresDto instanceof ParametresPrestationDto parametresPrestationDto) {
      mapParameter.put(DOMAINES, parametresPrestationDto.getDomaines());
    }
  }

  /** Map a domaine parameter type */
  private void mapTypeDomaineToExistingDto(
      final ParametresDto parameter, final HashMap<String, Object> mapObject) {
    ((ParametresDomaineDto) parameter)
        .setTranscodification((String) mapObject.get(TRANSCODIFICATION));
    ((ParametresDomaineDto) parameter).setCategorie((String) mapObject.get(CATEGORIE));
    if (mapObject.get(PRIORITE) != null) {
      ((ParametresDomaineDto) parameter).setPriorite((String) mapObject.get(PRIORITE));
    }
    if (mapObject.get(IS_CUMUL_GARANTIES) != null) {
      ((ParametresDomaineDto) parameter)
          .setIsCumulGaranties((Boolean) mapObject.get(IS_CUMUL_GARANTIES));
    }
    if (mapObject.get(IS_CUMUL_PLAFONNE) != null) {
      ((ParametresDomaineDto) parameter)
          .setIsCumulPlafonne((Boolean) mapObject.get(IS_CUMUL_PLAFONNE));
    }
  }

  private void mapTypeServicesMetiersToExistingDto(
      final ParametresDto parameter, final HashMap<String, Object> mapObject) {
    ((ParametresServicesMetiersDto) parameter).setOrdre((String) mapObject.get(ORDRE));
    ((ParametresServicesMetiersDto) parameter).setIcone((String) mapObject.get(ICONE));
  }

  private void mapTypeFormuleToExistingDto(
      final ParametresDto parameter, final HashMap<String, Object> mapObject) {
    ((ParametresFormuleDto) parameter).setParam1((Boolean) mapObject.get(PARAM1));
    ((ParametresFormuleDto) parameter).setParam2((Boolean) mapObject.get(PARAM2));
    ((ParametresFormuleDto) parameter).setParam3((Boolean) mapObject.get(PARAM3));
    ((ParametresFormuleDto) parameter).setParam4((Boolean) mapObject.get(PARAM4));
    ((ParametresFormuleDto) parameter).setParam5((Boolean) mapObject.get(PARAM5));
    ((ParametresFormuleDto) parameter).setParam6((Boolean) mapObject.get(PARAM6));
    ((ParametresFormuleDto) parameter).setParam7((Boolean) mapObject.get(PARAM7));
    ((ParametresFormuleDto) parameter).setParam8((Boolean) mapObject.get(PARAM8));
    ((ParametresFormuleDto) parameter).setParam9((Boolean) mapObject.get(PARAM9));
    ((ParametresFormuleDto) parameter).setParam10((Boolean) mapObject.get(PARAM10));
  }

  private void mapTypePrestationToExistingDto(
      final ParametresDto parameter, final HashMap<String, Object> mapObject) {
    ((ParametresPrestationDto) parameter)
        .setDomaines((List<DomainePrestation>) mapObject.get(DOMAINES));
    if (CollectionUtils.isEmpty(((ParametresPrestationDto) parameter).getDomaines())) {
      ((ParametresPrestationDto) parameter).setDomaines(new ArrayList<>());
    }
  }

  public void dropCollectionParameter() {
    getParametreBddDao().dropCollection(ParametreBdd.class);
  }
}
