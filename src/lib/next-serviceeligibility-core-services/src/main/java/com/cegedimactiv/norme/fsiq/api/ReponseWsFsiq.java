package com.cegedimactiv.norme.fsiq.api;

import com.cegedimassurances.norme.commun.TypeHeaderOut;

/**
 * Interface de base pour toutes les rÃ©ponses FSIQ, c'est-Ã -dire qu'elles portent un code rÃ©ponse
 * et une en-tÃªte de sortie.
 *
 * @author csignembe
 */
public interface ReponseWsFsiq extends ReponseWs {

  /**
   * Retourne le header de la rÃ©ponse Ã  une requÃªte d'appel de webservice
   *
   * @return {@link TypeHeaderOut}
   */
  TypeHeaderOut getHeaderOut();

  /**
   * Renseigne le Header de la rÃ©ponse Ã  une requÃªte d'appel de webservice
   *
   * @param headerOut la valeur du header.
   */
  void setHeaderOut(TypeHeaderOut headerOut);
}
