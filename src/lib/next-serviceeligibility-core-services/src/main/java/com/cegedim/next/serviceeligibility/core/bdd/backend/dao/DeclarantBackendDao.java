package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.domain.DeclarantsEchange;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import java.io.UnsupportedEncodingException;
import java.util.List;

/** Interface de la classe d'accès aux {@code Declarant} de la base de donnees. */
public interface DeclarantBackendDao extends IMongoGenericDao<Declarant> {

  /**
   * Recherche dans la base de donnees un declarant a partir de son identifiant technique.
   *
   * @param id l'identifiant du declarant.
   * @return le declarant.
   */
  Declarant findById(String id);

  /**
   * Recherche dans la base de donnees les declarants par critere de recherche
   *
   * @param id l'identifiant du declarant.
   * @param nom nom du declarant.
   * @param couloir le couloir du declarant.
   * @param service le service recherché
   * @return liste des declarants trouves.
   */
  List<Declarant> findByCriteria(String id, String nom, String couloir, String service)
      throws UnsupportedEncodingException;

  /**
   * Recherche dans la base de donnees tous les declarants mais declarants ALEGES : Seulement 3
   * proprietes remontent de la base : numero AMC, nom AMC et code partenaire.
   *
   * @return liste des declarants trouves. Si aucun declarant n'est trouve, cette méthode retournera
   *     une liste vide.
   */
  List<Declarant> findAllDeclarants();

  /**
   * Recherche dans la base de donnees les declarants pour un utilisateur.
   *
   * @param user nom utilisateur
   * @param page page number (0,1,2,...)
   * @param pageSize page size
   * @return liste des declarants trouves. Si aucun declarant n'est trouve, cette méthode retournera
   *     une liste vide.
   */
  List<Declarant> findByUser(String user, int page, int pageSize);

  /**
   * Trouve le nombre total des declarants (cree/modifie) pour un utilisateur.
   *
   * @param user nom utilisateur
   * @return le nombre total des declarants pour un utilisateur
   */
  long findTotalDeclarantsByUser(String user);

  // @formatter:off

  /** Renvoi la liste de tous les declarant-echanges. */
  // @formatter:on
  DeclarantsEchange findAllDeclarantsEchanges();
}
