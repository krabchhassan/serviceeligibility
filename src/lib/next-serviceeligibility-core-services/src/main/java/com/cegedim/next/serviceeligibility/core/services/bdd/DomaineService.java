package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineDroitBuffer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DomaineService {
  String validateDomainesConsolides(Collection<DomaineDroitBuffer> domaines);

  List<DomaineDroit> updateDomainesConsolides(Collection<DomaineDroitBuffer> listeDomaines);

  List<DomaineDroit> splitAndFilterDomaineDroits(
      Declaration selectedDeclaration, Date dateExec, boolean isInsurer);

  Map<String, Map<String, DomaineDroitBuffer>> groupeByDateFinAndCode(
      List<DomaineDroit> domaineDroits);
}
