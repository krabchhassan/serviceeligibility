package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ServiceDroitsDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ServiceDroitsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code ServiceDroits}. */
public interface ServiceDroitsService extends GenericService<ServiceDroits> {

  ServiceDroitsDao getServiceDroitsDao();

  /**
   * TranscoParametrage service cast
   *
   * @return TranscoParametrage service
   */
  TranscoParametrageService getTranscoParametrageService();

  /**
   * Trouve la liste des services tries par triRestitution
   *
   * @return la liste des codes services.
   */
  List<ServiceDroitsDto> findCodesServiceTries();

  /**
   * Trouve la liste des services ayant une transco possible
   *
   * @return la liste transco.
   */
  List<ServiceDroitsDto> findServicesWithTransco();

  /**
   * Saving services
   *
   * @param serviceDroitsDto Service Dto to save
   * @return Service Dto saved
   */
  ServiceDroitsDto saveOrUpdate(final ServiceDroitsDto serviceDroitsDto);

  /**
   * Searching service by code et transcoding object code
   *
   * @param serviceCode service code
   * @param transcodingCode Transcoding object code
   * @return A service Dto with all asked parameters
   */
  ServiceDroitsDto findOneByCodeAndTransco(final String serviceCode, final String transcodingCode);

  /**
   * Searching service by code. We should have only one service by code
   *
   * @param serviceCode service code
   * @return Dto service with all asked parameters
   */
  ServiceDroitsDto findOneByCode(final String serviceCode);

  /**
   * Delete a service by his idd
   *
   * @param id Service id
   */
  void delete(final String id);
}
