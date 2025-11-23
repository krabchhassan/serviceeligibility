package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import java.util.List;

public interface ServiceDroitsDao extends IMongoGenericDao<ServiceDroits> {

  /**
   * Trouve la liste des codes services
   *
   * @return la liste des services non fictifs.
   */
  List<ServiceDroits> findCodesService();

  /**
   * Trouve la liste des services avec les transco possible
   *
   * @return la liste des services
   */
  List<ServiceDroits> findServicesWithTransco();

  /**
   * Searching a service by code and transcoding object code
   *
   * @param serviceCode Service code
   * @param transcodingCode Transcoding object code
   * @return A service with asked code and transcoding object code
   */
  ServiceDroits findOneByCodeAndTransco(final String serviceCode, final String transcodingCode);

  /**
   * Searching a service by his code. We should have only one service
   *
   * @param serviceCode Service code
   * @return The service with the asked code
   */
  ServiceDroits findOneByCode(final String serviceCode);
}
