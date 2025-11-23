package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;

/** Interface de la classe d'accès aux {@code Declaration} de la base de donnees. */
public interface DeclarationBackendDao extends IMongoGenericDao<Declaration> {

  List<Declaration> findDeclarationsByCriteria(
      String idDeclarant, String numeroPersonne, int limit);

  Long countDeclarationByCriteria(String idDeclarant, String numeroPersonne);

  /**
   * Recherche des declarations correspondant aux informations de la déclaration en cours de
   * consultation
   *
   * @param idDeclarant l'id de l'amc
   * @param numeroPersonne le numero de personne recherché
   * @return une liste de declarations
   */
  List<Declaration> findDeclarationsByBenefContrat(
      String idDeclarant, String numeroPersonne, String numeroContrat, Integer limit);
}
