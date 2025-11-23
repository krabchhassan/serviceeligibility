package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarantBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Declarant}. */
public interface DeclarantBackendService extends GenericService<Declarant> {

  /**
   * @return La DAO des déclarants.
   */
  DeclarantBackendDao getDeclarantDao();

  /**
   * Recherche dans la base de donnees un declarant a partir de son identifiant technique.
   *
   * @param id l'identifiant du declarant.
   * @return le declarant.
   */
  Declarant findById(String id);

  /**
   * Recherche dans la base de donnees un declarant a partir de son identifiant technique.
   *
   * @param id l'identifiant du declarant.
   * @return le declarant dto.
   */
  DeclarantBackendDto findDtoById(String id);

  /**
   * Recherche dans la base de donnees les declarants par id et nom
   *
   * @param id l'identifiant du declarant.
   * @param nom nom du declarant.
   * @param couloir le couloir du declarant.
   * @param service le service recherché
   * @return liste des declarants dto trouves.
   */
  List<ServicesDeclarantDto> findServicesDtoByCriteria(
      String id, String nom, String couloir, String service);

  /**
   * Recherche dans la base de donnees tous les declarants mais declarants ALEGES : Seulement 3
   * proprietes remontent de la base : numero AMC, nom AMC et code partenaire.
   *
   * @return liste des declarants trouves. Si aucun declarant n'est trouve, cette méthode retournera
   *     une liste vide.
   */
  @Override
  List<Declarant> findAll();

  /**
   * Recherche dans la base de donnees tous les declarants.
   *
   * @return liste des declarants trouves au format {@code DTO}. Si aucun declarant n'est trouve,
   *     cette méthode retournera une liste vide.
   */
  List<DeclarantBackendDto> findAllDto();

  /**
   * Recherche dans la base de donnees tous les declarants.
   *
   * @return liste des declarants trouves au format {@code DTO}. Si aucun declarant n'est trouve,
   *     cette méthode retournera une liste vide.
   */
  List<DeclarantLightDto> findAllLightDto();

  /**
   * Creation du declarant
   *
   * @param declarant declarant a creer.
   * @return declarant cree dans la base
   */
  Declarant createDeclarant(DeclarantRequestDto declarant);

  /**
   * Modification du declarant.
   *
   * @param declarant declarant a modifier.
   */
  void updateDeclarant(DeclarantRequestDto declarant);

  /**
   * Trouve les declarants pour un utilisateur.
   *
   * @param user nom utilisateur
   * @param page page number (0,1,2,...)
   * @param pageSize page size
   * @return liste des declarants trouves. Si aucun declarant n'est trouve, cette méthode retournera
   *     une liste vide.
   */
  List<ServicesDeclarantDto> findListDtoByUser(String user, int page, int pageSize);

  /**
   * Trouve le nombre total des declarants (cree/modifie) pour un utilisateur.
   *
   * @param user nom utilisateur
   * @return le nombre total des declarants pour un utilisateur
   */
  long findTotalDeclarantsByUser(String user);

  /**
   * Validation des pilotagesDto
   *
   * @param liste des pilotagesDto
   * @return pilotages Request Dto.
   */
  List<PilotageDto> validationPilotagesRequestDto(List<PilotageDto> pilotagesDto)
      throws ExceptionServiceFormatDate;

  /**
   * Renvoie tous les declarant-echanges
   *
   * @return La liste des declarant-echange
   */
  DeclarantEchangeDto findAllDeclarantsEchanges();

  /**
   * @param insurerId
   * @param domains liste de domaines pour transcodification (si un seul domaine)
   * @return liste de domaines transcodés si trouvés ou liste d'origine
   */
  String transcodeDomain(String insurerId, String domains);

  /**
   * @param insurerId
   * @param domains liste de domaines pour transcodification (si un seul domaine)
   * @return liste de domaines transcodés si trouvés ou liste d'origine
   */
  List<String> transcodeDomain(String insurerId, List<String> domains);
}
