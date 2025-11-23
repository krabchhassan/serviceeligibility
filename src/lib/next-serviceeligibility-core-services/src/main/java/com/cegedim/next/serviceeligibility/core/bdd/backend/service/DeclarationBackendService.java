package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarationBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.DroitsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.InfosAssureDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.RechercheDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Declaration}. */
public interface DeclarationBackendService extends GenericService<Declaration> {

  /**
   * @return La DAO des déclarations.
   */
  DeclarationBackendDao getDeclarationDao();

  /**
   * Recherche dans la base les informations pour un assuré a partir d'un identifiant d'une
   * declaration.
   *
   * @param id l'identifiant technique d'une declaration.
   * @param requestFromBenefTpDetails boolean indiquant d'où provient l'appel
   * @return {@link InfosAssureDto}
   */
  InfosAssureDto findById(String id, boolean requestFromBenefTpDetails);

  /**
   * Recherche dans la base les informations pour un assuré de la dernière declaration existante
   *
   * @return {@link DroitsDto}
   */
  DroitsDto findLastDeclaration();

  List<RechercheDroitDto> getInfoDroitsAssures(String idDeclarant, String numeroPersonne);
}
