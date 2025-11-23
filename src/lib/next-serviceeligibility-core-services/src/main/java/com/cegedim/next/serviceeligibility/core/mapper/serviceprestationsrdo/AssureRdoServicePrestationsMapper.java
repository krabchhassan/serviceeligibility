package com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Assure;
import org.mapstruct.Mapper;

@Mapper
public interface AssureRdoServicePrestationsMapper {
  Assure assureToAssureRdoServicePrestations(
      com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure assure);
}
