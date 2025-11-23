package com.cegedim.next.serviceeligibility.core.business.declarant.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Declarant}. */
public interface DeclarantService {

  /**
   * Recherche dans la base de donnees un declarant a partir de son identifiant technique.
   *
   * @param id l'identifiant du declarant.
   * @return le declarant.
   */
  Declarant findById(String id);

  /**
   * Recherche dans la base de donnees tous les declarants.
   *
   * @return liste des declarants trouves. Si aucun declarant n'est trouve, cette méthode retournera
   *     une liste vide.
   */
  List<Declarant> findAll();

  /**
   * Recherche dans la base de donnees tous les declarants.
   *
   * @return liste des declarants trouves au format {@code DTO}. Si aucun declarant n'est trouve,
   *     cette méthode retournera une liste vide.
   */
  List<DeclarantDto> findAllDto();

  /**
   * Retourne une AMC - Cherche par id prefectoral, puis par numero amc echange
   *
   * @param numAMC le numero de l'amc.
   * @return l'amc recherchée
   */
  DeclarantDto getAmcRecherche(String numAMC);

  /**
   * Retourne un declarant complet - Cherche par id prefectoral, puis par numero amc echange
   *
   * @param numAMC le numero de l'amc.
   * @return l'amc recherchée
   */
  Declarant getDeclarantAmcRecherche(String numAMC);

  /**
   * Mappe un déclarant avec les bonnes données
   *
   * @param numAMC le numero de l'amc qui a été utilisé pour rechercher le déclarant.
   * @param declarant le declarant trouvé en base.
   * @return le déclarant dto
   */
  DeclarantDto mapDeclarantForAmcRecherche(String numAMC, Declarant declarant);
}
