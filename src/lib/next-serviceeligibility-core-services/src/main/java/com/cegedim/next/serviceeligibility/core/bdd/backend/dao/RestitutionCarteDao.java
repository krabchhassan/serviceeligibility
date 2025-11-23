package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.RestitutionCarte;
import com.mongodb.client.ClientSession;
import java.util.Date;
import java.util.List;

/** Interface de la classe d'accès aux {@code Traces} de la base de donnees. */
public interface RestitutionCarteDao extends IMongoGenericDao<RestitutionCarte> {

  /**
   * Retourne la liste des restitutions de carte pour un beneficiaire donnée dans un contrat donnée
   *
   * @param idDeclarant l'id de l'AMC
   * @param numeroPersonne le numero de personne recherché
   * @return liste de restitution carte.
   */
  List<RestitutionCarte> findRestitutionByIdDeclarantBenefContrat(
      String idDeclarant,
      String numeroPersonne,
      String numeroContrat,
      String numeroAdherent,
      Date dateEffet,
      Integer limit);

  List<String> getRestitutionsIdsByIdDeclarantBenefContrat(
      String idDeclarant,
      String numeroPersonne,
      String numeroContrat,
      String numeroAdherent,
      Integer limit);

  RestitutionCarte findById(String id);

  RestitutionCarte saveRestitutionCarte(RestitutionCarte restitutionCarte, ClientSession session);
}
