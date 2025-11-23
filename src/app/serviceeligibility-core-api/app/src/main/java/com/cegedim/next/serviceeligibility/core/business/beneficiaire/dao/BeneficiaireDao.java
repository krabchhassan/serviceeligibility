package com.cegedim.next.serviceeligibility.core.business.beneficiaire.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Beneficiaire;

/** Interface de la classe d'accès aux {@code Beneficiaire} de la base de donnees. */
public interface BeneficiaireDao extends IMongoGenericDao<Beneficiaire> {

  /**
   * Recherche dans la base de donnees un beneficiaire a partir d'un numéro AMC
   *
   * @param idDeclarant idDeclarant.
   * @return le beneficiaire.
   */
  Beneficiaire findByIdDeclarant(String idDeclarant);

  Long removeService(String idDeclarant, String service);
}
