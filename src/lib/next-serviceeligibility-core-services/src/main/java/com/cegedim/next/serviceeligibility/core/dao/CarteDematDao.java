package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.query.CriteresRechercheCarteDemat;
import com.mongodb.client.ClientSession;
import java.util.List;

/** Interface de la classe d'accès aux {@code carteDemat} de la base de donnees. */
public interface CarteDematDao extends IMongoGenericDao<CarteDemat> {

  List<CarteDemat> findCartesDematByDeclarantAndContrat(String idDeclarant, String numeroContrat);

  List<CarteDemat> findAllCartesDematByDeclarantAndAMCContrat(
      String idDeclarant, String numeroContrat, String numeroPersonne);

  int findCartesDematByDeclarantAndDateExec(String idDeclarant, String dateExec);

  List<CarteDemat> findCarteByDeclarantAndAmcContrat(
      final String idDeclarant, final String numeroContrat, String idDeclaration);

  CarteDemat findCarteByDeclarantAndNumeroPersonne(String idDeclarant, String numeroPersonne);

  /**
   * Renvoi une liste de carteDemats pour un contrat et une AMC partir d'une date de référenc<br>
   * <br>
   *
   * @param criteria critères de recherche,
   * @return La liste des carteDemats.
   */
  List<CarteDemat> findCartesDematByCriteria(CriteresRechercheCarteDemat criteria);

  /**
   * Renvoi une liste de carteDemats pour un numeroAdherent et une AMC partir d'une date de référenc
   * <br>
   * Le numero adherent se situe dans le champs numéro de contrat <br>
   *
   * @param criteria critères de recherche,
   * @return La liste des carteDemats.
   */
  List<CarteDemat> findCartesDematByAdherent(CriteresRechercheCarteDemat criteria);

  void updateIsLastCarteAll(List<CarteDemat> carteDemats, ClientSession session);

  int insertAll(List<CarteDemat> demats, ClientSession session);

  List<CarteDemat> getLastCartesByAmcContrats(String amcContrats);

  long deleteByAMC(String amc);

  long deleteByDeclaration(String declaration);
}
