package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.BeneficiaireConsultationHistory;
import java.util.List;

/**
 * Interface de la classe d'accès aux {@code BeneficiaireConsultationHistory} de la base de donnees.
 */
public interface BeneficiaireConsultationHistoryDao
    extends IMongoGenericDao<BeneficiaireConsultationHistory> {

  /**
   * Recherche des BeneficiaireConsultationHistory suivant des criteres
   *
   * @param user le user pour lequel on cherche les BeneficiaireConsultationHistory
   * @return une liste de BeneficiaireConsultationHistory
   */
  List<BeneficiaireConsultationHistory> findBeneficiaireConsultationHistoriesByCriteria(
      String user);

  List<BeneficiaireConsultationHistory> findBeneficiaireConsultationHistoriesByCriteria(
      String user, boolean limit);

  long deleteAllBeneficiaireConsultationHistory();
}
