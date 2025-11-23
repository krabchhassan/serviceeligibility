package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;

/** Classe générique à étendre par tous les Daos Mongo. */
public interface IMongoGenericDao<T extends DocumentEntity> {
  /**
   * Crée un document et l'insert dans la base de données. À l'insertion, l'identifiant technique du
   * document est créé.
   *
   * @param document le document à insérer.
   */
  void create(T document);

  /**
   * Recherche dans la base de données un document à partir de son identifiant technique.
   *
   * @param id l'identifiant du document.
   * @param clazz le type de document recherché.
   * @return le document.
   */
  T findById(String id, Class<T> clazz);

  /**
   * Recherche dans la base de données tous les documents de type <i>clazz</i>
   *
   * @param clazz le type de documents recherchés
   * @return liste des document trouvés. Si aucun document n'est trouvé, cette méthode retournera
   *     une liste vide.
   */
  List<T> findAll(Class<T> clazz);

  /**
   * Mets à jour un document.
   *
   * @param document le document à mettre à jour.
   * @return le document mis à jour.
   */
  T update(T document);

  /**
   * Supprime un document de la base de données.
   *
   * @param document le document à supprimer.
   */
  void delete(T document);

  /**
   * Recherche une liste de documents dans la base mongo à partir d'un de ses attributs
   *
   * @param fieldName le nom du champs à filtrer
   * @param value la valeur du champs
   * @param clazz le type du document
   * @return liste des documents
   */
  List<T> findByField(String fieldName, Object value, Class<T> clazz);

  /**
   * Drop la collection dont le type entite est passe en parametre.
   *
   * @param clazz Le type de l'entite.
   */
  void dropCollection(Class<T> clazz);
}
