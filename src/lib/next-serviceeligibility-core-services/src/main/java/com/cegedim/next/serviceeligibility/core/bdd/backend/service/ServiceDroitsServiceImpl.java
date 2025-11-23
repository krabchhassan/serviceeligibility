package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ServiceDroitsDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ServiceDroitsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscoParamDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceTrancoParametrageInconnu;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** Classe d'accès aux services lies aux {@code ServiceDroits}. */
@Service("serviceDroitsService")
public class ServiceDroitsServiceImpl extends GenericServiceImpl<ServiceDroits>
    implements ServiceDroitsService {

  private final TranscoParametrageService transcoParametrageService;

  /**
   * public constructeur.
   *
   * @param serviceDroitsDao bean dao injecte
   */
  @Autowired
  public ServiceDroitsServiceImpl(
      @Qualifier("serviceDroitsDao") final IMongoGenericDao<ServiceDroits> serviceDroitsDao,
      @Qualifier("transcoParametrageService")
          final TranscoParametrageService transcoParametrageService) {
    super(serviceDroitsDao);
    this.transcoParametrageService = transcoParametrageService;
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code ServiceDroits}
   */
  @Override
  @ContinueSpan(log = "getServiceDroitsDao")
  public ServiceDroitsDao getServiceDroitsDao() {
    return ((ServiceDroitsDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "getTranscoParametrageService")
  public TranscoParametrageService getTranscoParametrageService() {
    return this.transcoParametrageService;
  }

  @Override
  @ContinueSpan(log = "findCodesServiceTries")
  public List<ServiceDroitsDto> findCodesServiceTries() {
    final List<ServiceDroits> services = this.getServiceDroitsDao().findCodesService();
    final List<ServiceDroitsDto> servicesObjects = new ArrayList<>();

    for (final ServiceDroits service : services) {
      final ServiceDroitsDto serviceObject = this.serviceDroitsEntityToDto(service);
      servicesObjects.add(serviceObject);
    }
    return servicesObjects;
  }

  @Override
  @ContinueSpan(log = "findServicesWithTransco")
  public List<ServiceDroitsDto> findServicesWithTransco() {
    final List<ServiceDroits> services = this.getServiceDroitsDao().findServicesWithTransco();
    final List<ServiceDroitsDto> servicesTransco = new ArrayList<>();
    if (services != null && !services.isEmpty()) {
      Collections.sort(services);
      for (final ServiceDroits service : services) {
        final ServiceDroitsDto serviceTransco = new ServiceDroitsDto();
        serviceTransco.setNom(service.getCode());
        Collections.sort(service.getTransco());
        serviceTransco.setListTransco(service.getTransco());
        servicesTransco.add(serviceTransco);
      }
    }
    return servicesTransco;
  }

  @Override
  @ContinueSpan(log = "saveOrUpdate ServiceDroitsDto")
  public ServiceDroitsDto saveOrUpdate(final ServiceDroitsDto serviceDroitsDto) {
    final AtomicReference<ServiceDroitsDto> serviceDroitsDtoReturned =
        new AtomicReference<>(new ServiceDroitsDto());
    final AtomicReference<ServiceDroits> serviceDroits =
        new AtomicReference<>(this.serviceDroitsDtoToEntity(serviceDroitsDto));

    // Checking if service exists
    if (serviceDroitsDto.getNom() != null
        && !CollectionUtils.isEmpty(serviceDroitsDto.getListTransco())) {

      // Searching service by code
      final ServiceDroitsDto existingServiceDroitsDto =
          this.findOneByCode(serviceDroitsDto.getNom());

      // Update
      if (existingServiceDroitsDto != null) {
        serviceDroits.set(this.serviceDroitsDtoToEntity(existingServiceDroitsDto));
      } else { // Create
        serviceDroits.set(this.serviceDroitsDtoToEntity(serviceDroitsDto));
        // Clearing id if we send wrong data with an existing id
        serviceDroits.get().set_id(null);
      }

      // Emptying transcoding parameter list to always have a correct one
      serviceDroits.get().getTransco().clear();

      serviceDroitsDto
          .getListTransco()
          .forEach(
              transco -> {

                // Searching transcoding parameter in database
                final TranscoParamDto transcoParamDto =
                    this.getTranscoParametrageService().findTranscoParametrage(transco);
                if (transcoParamDto.getCodeObjetTransco() != null) {
                  serviceDroits.get().getTransco().add(transcoParamDto.getCodeObjetTransco());
                }
              });

      if (existingServiceDroitsDto != null) {
        this.getServiceDroitsDao().update(serviceDroits.get());
      } else {
        this.getServiceDroitsDao().create(serviceDroits.get());
      }

      // Mapping entity to Dto
      serviceDroitsDtoReturned.set(this.serviceDroitsEntityToDto(serviceDroits.get()));
    }

    return serviceDroitsDtoReturned.get();
  }

  @Override
  @ContinueSpan(log = "delete ServiceDroitsDto")
  public void delete(final String name) {
    // Searching document to delete
    final ServiceDroits serviceDroits = this.getServiceDroitsDao().findOneByCode(name);

    this.getServiceDroitsDao().delete(serviceDroits);
  }

  @Override
  @ContinueSpan(log = "findOneByCodeAndTransco")
  public ServiceDroitsDto findOneByCodeAndTransco(
      final String serviceCode, final String transcodingCode) {
    ServiceDroitsDto serviceDroitsDto = null;

    // Searching in database if service exists
    final ServiceDroits serviceDroits =
        this.getServiceDroitsDao().findOneByCodeAndTransco(serviceCode, transcodingCode);

    if (serviceDroits != null) {
      serviceDroitsDto = this.serviceDroitsEntityToDto(serviceDroits);
    }

    return serviceDroitsDto;
  }

  @Override
  @ContinueSpan(log = "findOneByCode ServiceDroitsDto")
  public ServiceDroitsDto findOneByCode(final String serviceCode) {
    ServiceDroitsDto serviceDroitsDto = null;

    // Searching in database if service exists
    final ServiceDroits serviceDroits = this.getServiceDroitsDao().findOneByCode(serviceCode);

    if (serviceDroits != null) {
      serviceDroitsDto = this.serviceDroitsEntityToDto(serviceDroits);
    }

    return serviceDroitsDto;
  }

  /**
   * Return a service entity from his Dto
   *
   * @param serviceDroitsDto serviceDroits Dto
   * @return A serviceDroits entity from his dto
   */
  private ServiceDroits serviceDroitsDtoToEntity(final ServiceDroitsDto serviceDroitsDto) {
    final ServiceDroits serviceDroits = new ServiceDroits();
    serviceDroits.set_id((serviceDroitsDto.getId()));
    serviceDroits.setCode(serviceDroitsDto.getNom());
    serviceDroits.setTransco(new ArrayList<>());
    if (!CollectionUtils.isEmpty(serviceDroitsDto.getListTransco())) {
      serviceDroitsDto
          .getListTransco()
          .forEach(
              trancoDto -> {
                // Checking if trasco object is existing
                final TranscoParamDto transcoParamDto =
                    this.getTranscoParametrageService().findTranscoParametrage(trancoDto);
                if (transcoParamDto != null) {
                  serviceDroits.getTransco().add(transcoParamDto.getCodeObjetTransco());
                } else {
                  throw new ExceptionServiceTrancoParametrageInconnu();
                }
              });
    }
    return serviceDroits;
  }

  /**
   * Return a serviceDroits Dto from his entity
   *
   * @param serviceDroits serviceDroits entity
   * @return A serviceDroits Dto from the entity
   */
  private ServiceDroitsDto serviceDroitsEntityToDto(final ServiceDroits serviceDroits) {
    final ServiceDroitsDto serviceDroitsDto = new ServiceDroitsDto();
    serviceDroitsDto.setId(serviceDroits.get_id());
    serviceDroitsDto.setNom(serviceDroits.getCode());
    serviceDroitsDto.setControleContextuel(serviceDroits.getControleContextuel());
    serviceDroitsDto.setListTransco(new ArrayList<>());
    serviceDroits.getTransco().forEach(transco -> serviceDroitsDto.getListTransco().add(transco));
    return serviceDroitsDto;
  }
}
