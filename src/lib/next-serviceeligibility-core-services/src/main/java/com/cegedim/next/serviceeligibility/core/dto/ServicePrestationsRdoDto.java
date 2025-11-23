package com.cegedim.next.serviceeligibility.core.dto;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ServicePrestationsRdo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ServicePrestationsRdoDto implements Serializable {
  private List<ServicePrestationsRdo> contrats;
}
