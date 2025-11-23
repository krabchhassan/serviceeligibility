package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.HistoriqueTranscodageDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscodageDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ServiceDroitsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.ServicesTranscoDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscoParamDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageListDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.domain.HistoriqueTransco;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueTranscodage;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Transcodage}. */
@Service("transcodageService")
public class TranscodageServiceImpl extends GenericServiceImpl<Transcodage>
    implements TranscodageService {

  @Autowired private HistoriqueTranscodageDao historiqueTranscodageDao;

  @Autowired private TranscoParametrageService transcoParametrageService;

  @Autowired private ServiceDroitsService serviceDroitsService;

  /**
   * public constructeur.
   *
   * @param transcodageDao bean dao injecte
   */
  @Autowired
  public TranscodageServiceImpl(
      @Qualifier("transcodageDao") final IMongoGenericDao<Transcodage> transcodageDao) {
    super(transcodageDao);
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Transcodage}
   */
  @Override
  @ContinueSpan(log = "getTranscodageDao")
  public TranscodageDao getTranscodageDao() {
    return ((TranscodageDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findTranscoByCle")
  public TranscodageListDto findTranscoByCle(
      final String codeService, final String codeObjetTransco) {
    final List<Transcodage> transoList =
        this.getTranscodageDao().findByCodeObjetTransco(codeService, codeObjetTransco);
    TranscodageListDto transcodageListDto = null;
    if (transoList != null && !transoList.isEmpty()) {
      transcodageListDto = new TranscodageListDto();
      transcodageListDto.setCodeService(transoList.get(0).getCodeService());
      transcodageListDto.setCodeObjetTransco(transoList.get(0).getCodeObjetTransco());
      final List<TranscodageDto> transcodagesDto = new ArrayList<>();
      for (final Transcodage transcodage : transoList) {
        final TranscodageDto transcodageDto = new TranscodageDto();
        final List<String> cle = transcodage.getCle();
        transcodageDto.setCle(cle);
        transcodageDto.setCodeTransco(transcodage.getCodeTransco());
        transcodagesDto.add(transcodageDto);
      }
      Collections.sort(transcodagesDto);
      transcodageListDto.setTranscoList(transcodagesDto);
    }
    return transcodageListDto;
  }

  @Override
  @ContinueSpan(log = "findCodeByCle Transcodage")
  public String findCodeByCle(final String codeService, final String codeObjetTransco) {
    return this.getTranscodageDao()
        .findCodeTranscoByCodeObjetTransco(codeService, codeObjetTransco);
  }

  @Override
  @ContinueSpan(log = "updateTranscodage")
  public void updateTranscodage(final TranscodageListDto transcodageDto) {
    if (transcodageDto != null) {
      final List<Transcodage> transcoList =
          this.getTranscodageDao()
              .findByCodeObjetTransco(
                  transcodageDto.getCodeService(), transcodageDto.getCodeObjetTransco());
      if (transcoList != null && !transcoList.isEmpty()) {
        // Efface la liste
        for (final Transcodage transcodage : transcoList) {
          this.getTranscodageDao().delete(transcodage);
        }
      }
      // Creation
      if (transcodageDto.getTranscoList() != null && !transcodageDto.getTranscoList().isEmpty()) {
        this.setTranscodage(transcodageDto);
      }
    }
  }

  private void setTranscodage(final TranscodageListDto transcodageDto) {
    Transcodage transcodageEntity = null;
    for (final TranscodageDto transcodage : transcodageDto.getTranscoList()) {
      transcodageEntity = this.transcoDtoToEntity(transcodage);
      transcodageEntity.setCodeService(transcodageDto.getCodeService());
      transcodageEntity.setCodeObjetTransco(transcodageDto.getCodeObjetTransco());
      this.getTranscodageDao().create(transcodageEntity);
    }
    // Historique transcodage - prend les dates creation et modif,
    // user creation et modif de transcodageEntity pour historique
    // transcodage
    if (transcodageEntity != null) {
      final HistoriqueTranscodage histoTranscodage =
          this.transcoDtoToHistoryEntity(
              transcodageDto,
              transcodageEntity.getDateCreation(),
              transcodageEntity.getDateModification(),
              transcodageEntity.getUserCreation(),
              transcodageEntity.getUserModification());
      this.historiqueTranscodageDao.create(histoTranscodage);
    }
  }

  @Override
  @ContinueSpan(log = "deleteTranscodage")
  public void deleteTranscodage(final TranscodageDto transcodageDto) {
    final List<Transcodage> transcoList =
        this.getTranscodageDao()
            .findByCodeObjetTransco(
                transcodageDto.getCodeService(), transcodageDto.getCodeObjetTransco());
    for (final Transcodage transco : transcoList) {
      this.getTranscodageDao().delete(transco);
    }
  }

  /**
   * Mapper de transco dto vers list vers transco entity
   *
   * @param transcodageDto transco dto.
   * @return transco entity
   */
  private Transcodage transcoDtoToEntity(final TranscodageDto transcodageDto) {
    final Transcodage transcodage = new Transcodage();
    transcodage.setCodeService(transcodageDto.getCodeService());
    transcodage.setCodeObjetTransco(transcodageDto.getCodeObjetTransco());
    transcodage.setCle(transcodageDto.getCle());
    transcodage.setCodeTransco(transcodageDto.getCodeTransco());
    return transcodage;
  }

  /**
   * Mapper de transco list vers la historique transco entity
   *
   * @param transcodageDto transco list.
   * @param dateCreation date creation des transcodages.
   * @param dateModification date modif. des transcodages.
   * @param userCreation user creation des transcodages.
   * @param userModification user modification des transcodages.
   * @return historique transco entity
   */
  private HistoriqueTranscodage transcoDtoToHistoryEntity(
      final TranscodageListDto transcodageDto,
      final Date dateCreation,
      final Date dateModification,
      final String userCreation,
      final String userModification) {
    // Entity HistoriqueTranscodage contient : codeService,
    // codeObjetTransco, les proprietes traces et la liste des
    // HistoriqueTransco

    // Alimente : codeService, codeObjetTransco et les proprietes traces
    final HistoriqueTranscodage histoTranscodage = new HistoriqueTranscodage();
    histoTranscodage.setCodeService(transcodageDto.getCodeService());
    histoTranscodage.setCodeObjetTransco(transcodageDto.getCodeObjetTransco());
    histoTranscodage.setDateCreation(dateCreation);
    histoTranscodage.setDateModification(dateModification);
    histoTranscodage.setUserCreation(userCreation);
    histoTranscodage.setUserModification(userModification);

    // Creation de la liste des HistoriqueTransco
    final List<HistoriqueTransco> transcoHistoList = new ArrayList<>();
    if (transcodageDto.getTranscoList() != null) {
      // Creation de la liste des transcodages par la classe
      for (final TranscodageDto transcoDto : transcodageDto.getTranscoList()) {
        final HistoriqueTransco histoTransco = new HistoriqueTransco();
        final List<String> cleHisto = new ArrayList<>();
        if (transcoDto.getCle() != null) {
          for (final String cle : transcoDto.getCle()) {
            cleHisto.add(cle);
          }
        }
        histoTransco.setCle(cleHisto);
        histoTransco.setCodeTransco(transcoDto.getCodeTransco());
        transcoHistoList.add(histoTransco);
      }
    }
    histoTranscodage.setTranscoList(transcoHistoList);

    return histoTranscodage;
  }

  @ContinueSpan(log = "getCodesServiceTranscodage")
  public List<ServicesTranscoDto> getCodesServiceTranscodage() {
    final List<ServiceDroitsDto> servicesDroits =
        this.serviceDroitsService.findServicesWithTransco();
    final List<ServicesTranscoDto> servicesTransco = new ArrayList<>();

    final List<TranscoParamDto> listeParamTransco =
        this.transcoParametrageService.findAllTranscoParametrage();
    final Map<String, TranscoParamDto> mapTranscoParam = new HashMap<>();

    for (final TranscoParamDto transco : listeParamTransco) {
      mapTranscoParam.put(transco.getCodeObjetTransco(), transco);
    }

    for (final ServiceDroitsDto service : servicesDroits) {
      final ServicesTranscoDto serviceTransco = new ServicesTranscoDto();
      serviceTransco.setCodeService(service.getNom());
      final List<TranscoParamDto> transcoParamsDto = new ArrayList<>();
      for (final String codeTransco : service.getListTransco()) {
        if (mapTranscoParam.containsKey(codeTransco)) {
          transcoParamsDto.add(mapTranscoParam.get(codeTransco));
        }
      }
      serviceTransco.setTransco(transcoParamsDto);
      servicesTransco.add(serviceTransco);
    }

    return servicesTransco;
  }
}
