package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.GenericRightDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import java.util.List;

public interface UniqueAccessPointTriTP {
  void triTP(List<GenericRightDto> contrats, UniqueAccessPointRequest requete);
}
