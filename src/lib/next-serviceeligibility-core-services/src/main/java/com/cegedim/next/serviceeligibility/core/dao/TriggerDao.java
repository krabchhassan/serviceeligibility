package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import java.util.Iterator;
import java.util.List;

public interface TriggerDao {

  Trigger getTriggerById(String id);

  TriggeredBeneficiary getTriggeredBenefById(String id);

  /**
   * Retourne la liste des triggers pour les parametres fournit
   *
   * @param perPage Nombre d'element par page
   * @param page Numero de page
   * @param sortBy Nom de la colonne de tri
   * @param direction Sens de tri
   * @param request Parametres de recherche
   * @return La liste des triggers
   */
  TriggerResponse getTriggers(
      int perPage, int page, String sortBy, String direction, TriggerRequest request);

  /**
   * Sauvegarde le declencheur en base (Upsert)
   *
   * @param trigger
   * @return
   */
  Trigger saveTrigger(Trigger trigger);

  /**
   * Cree le declencheur benef en base
   *
   * @param triggerBenef
   * @return
   */
  TriggeredBeneficiary saveTriggeredBeneficiary(TriggeredBeneficiary triggerBenef);

  /** Supprime tous les déclencheurs et leur béneficiaires */
  void removeAll();

  /** Retourne les bénéficiaires associés à un déclencheur */
  List<TriggeredBeneficiary> getTriggeredBeneficiaries(String idTrigger);

  Iterator<TriggeredBeneficiary> getTriggeredBeneficiariesStream(String idTrigger);

  List<String> getTriggeredBeneficiarieIdsWithStatus(
      String idTrigger, TriggeredBeneficiaryStatusEnum triggeredBeneficiaryStatusEnum);

  /** Retourne le nombre de bénéficiaires associés à un déclencheur */
  long getNbTriggeredBeneficiaries(String idTrigger);

  long getNbTriggeredBeneficiariesWithStatus(
      String idTrigger, TriggeredBeneficiaryStatusEnum triggeredBeneficiaryStatusEnum);

  /** Retourne les bénéficiaires associés à un servicePrestation */
  List<TriggeredBeneficiary> getTriggeredBeneficiariesByServicePrestation(
      String idServicePrestation);

  /** Retourne les bénéficiaires associés à un servicePrestation */
  TriggeredBeneficiary getLastTriggeredBeneficiariesByServicePrestation(
      String idServicePrestation, String idTriggerBenef);

  /**
   * Retourne les bénéficiaires en anomalie associés à un déclencheur
   *
   * @param perPage Nombre d'éléments par page
   * @param page Numéro de page
   * @param idTrigger Identifiant du déclencheur
   * @param motifAnomalieSortDirection Tri du motif d'anomalie (ASC ou DESC)
   * @return
   */
  TriggeredBeneficiaryResponse getTriggeredBeneficiariesWithError(
      int perPage, int page, String idTrigger, String motifAnomalieSortDirection);

  void updateOnlyStatus(String id, TriggerStatus status, String source);

  /**
   * Mise à jour d'une liste de bénéficiaires
   *
   * @param benefs
   */
  void updateTriggeredBeneficiaries(List<TriggeredBeneficiary> benefs);

  /**
   * Incrémentation du nombre de benef en erreur et à traiter et retourne le nb de benéf restant à
   * traiter
   *
   * @param triggerId Identifaint du déclencheur
   * @param nbBenefError Nombre de bénef en erreur
   * @param nbBenefToProcess Nombre de bénef à traiter
   */
  int manageBenefCounter(
      String triggerId, int nbBenefError, int nbBenefWarning, int nbBenefToProcess);

  /**
   * vérifie si il existe des triggers pour ce fichier qui sont encore a l'état ToProcess ou
   * Processing
   *
   * @param nomFichier nom du fichier d'origine
   * @param firstTriggerId idTrigger a partir duquel commencer à chercher
   */
  boolean isTriggerByFilenameNotProcessed(String nomFichier, String firstTriggerId);

  /**
   * retourne le nombre de déclarations ouvertes et fermées à partir des triggers d'un même fichier
   * origine
   *
   * @param nomFichier nom du fichier d'origine
   */
  int[] getNombreDeclarationForTriggerByFilename(String nomFichier);

  /**
   * Utilisé dans rdoserviceprestationpurge-job, supprime les triggers en base et renvoie la
   * quantité supprimée.
   *
   * @param idDeclarant numéro de l'amc a purger
   * @return nombre de triggers purgés
   */
  long deleteTriggerByAmc(String idDeclarant);

  /**
   * Utilisé dans rdoserviceprestationpurge-job, supprime les triggeredBenefs en base et renvoie la
   * quantité supprimée.
   *
   * @param idDeclarant numéro de l'amc a purger
   * @return nombre de triggeredBenefs purgés
   */
  long deleteTriggeredBeneficiaryByAmc(String idDeclarant);

  /**
   * Utilisé dans rdoserviceprestationpurge-job, supprime les triggers en base portant l'id passé en
   * paramètre.
   *
   * @param id id du trigger a supprimer
   * @return nombre de triggers purgés
   */
  long deleteTriggerById(String id);

  /**
   * Utilisé dans rdoserviceprestationpurge-job, supprime les triggeredBenefs en base portant l'id
   * passé en paramètre.
   *
   * @param id id du triggeredBenef a supprimer
   * @return nombre de triggeredBenefs purgés
   */
  long deleteTriggeredBeneficiaryById(String id);

  /**
   * @return une liste d'id des trigger qui ne sont pas encore sauvegarder sur s3
   */
  Iterator<Trigger> getIDsTriggerRenewNotArchived();

  void setExported(String id, boolean exported);

  int getNombreServicePrestationByTriggerId(String idTrigger, boolean recyclage);
}
