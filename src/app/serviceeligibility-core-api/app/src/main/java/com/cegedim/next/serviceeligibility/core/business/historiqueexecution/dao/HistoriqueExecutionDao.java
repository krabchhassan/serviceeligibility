package com.cegedim.next.serviceeligibility.core.business.historiqueexecution.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import java.time.LocalDateTime;

/** Interface de la classe d'accès aux {@code HistoriqueExecution} de la base de donnees. */
public interface HistoriqueExecutionDao extends IMongoGenericDao<HistoriqueExecution> {
  /**
   * Recherche dans la base de donnees un historique execution a partir de son identifiant
   * technique.
   *
   * @param id l'identifiant de l'historique execution.
   * @return l'historique execution.
   */
  HistoriqueExecution findById(String id);

  /**
   * Recherche dans la base de donnees un historique execution a partir de son n° de batch et
   * idDeclarant. Si plusieurs historique sont présents alors on récupère le plus récent via sa
   * dateExecution et son _id
   *
   * @param batch N° du batch - Obligatoire
   * @param idDeclarant N° de l'AMC - Facultatif
   * @param codeService Code service - Facultatif
   * @return l'historique execution.
   */
  HistoriqueExecution findLastByBatchIdDeclarant(
      String batch, String idDeclarant, String codeService);

  /**
   * Supprime dans la base de donnees un historique execution a partir de son n° de batch
   *
   * @param batch N° du batch - Obligatoire
   * @param codeService Code service - Facultatif
   * @param dateExecution Date d'execution - Facultatif
   */
  void deleteByBatchDateExecution(String batch, String codeService, LocalDateTime dateExecution);

  /**
   * Ajoute dans la base de donnees un historique execution
   *
   * @param historiqueExecution Historique Execution à créer
   */
  void add(HistoriqueExecution historiqueExecution);

  /** Ajoute dans la base de donnees un historique execution */
  void deleteAll();
}
