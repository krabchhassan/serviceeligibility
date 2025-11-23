package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTPRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametrageCarteTPResponse;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.List;

public interface ParametrageCarteTPDao {

  /**
   * Retourne la liste des paramétrages de Cartes TP pour la liste d'AMC fournit
   *
   * @param perPage Nombre d'element par page
   * @param page Numero de page
   * @param sortBy Nom de la colonne de tri
   * @param direction Sens de tri
   * @param request Paramétrage Cartes TP de recherche
   * @return La liste des paramétrage de Cartes TP
   */
  ParametrageCarteTPResponse getByParams(
      int perPage, int page, String sortBy, String direction, ParametrageCarteTPRequest request);

  /**
   * @param amc idDeclarant
   * @param numeroContratIndividuel Numéro du contrat individuel
   * @return Le servicePrestation
   */
  ContratAIV6 getServicePrestationByContratIndividuelAndNumAdherent(
      String amc, String numeroContratIndividuel, String numeroAdherent);

  /**
   * Retourne la liste des paramétrages de Cartes TP pour la'AMC fourni et les paramétrages
   *
   * @param requestParametrageCarteTP
   * @return La liste des paramétrage de Cartes TP
   */
  List<ParametrageCarteTP> getByAmc(RequestParametrageCarteTP requestParametrageCarteTP);

  /**
   * Création d'un paramétrage de Carte TP
   *
   * @param parametrageCarteTP
   */
  void create(ParametrageCarteTP parametrageCarteTP);

  /** Suppression de tous les paramétrages de Carte TP */
  void deleteAll();

  void update(ParametrageCarteTP param);

  /**
   * Mise à jour du statut du paramétrage de carte TP
   *
   * @param id
   * @param statut
   */
  void updateStatus(String id, ParametrageCarteTPStatut statut);

  ParametrageCarteTP getById(String id);

  /**
   * Retourne la liste des paramétrages à exécuter
   *
   * @param date
   * @return
   */
  List<ParametrageCarteTP> getParametrageToExecute(String date, boolean isRdo);

  long deleteByAmc(String amc);

  List<Integer> getPriorityByAmc(String amc);

  boolean existParametrageCarteTPActif();

  List<String> getIdLotsInParametrageCarteTPActif();

  List<ParametrageCarteTP> findByGuaranteeCodeAndInsurerCode(
      String guaranteeCode, String insurerCode);
}
