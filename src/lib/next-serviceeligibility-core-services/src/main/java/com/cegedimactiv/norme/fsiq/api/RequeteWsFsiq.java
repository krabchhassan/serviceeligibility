package com.cegedimactiv.norme.fsiq.api;

import com.cegedimassurances.norme.commun.TypeHeaderIn;

/**
 * Interface de base pour toutes les requÃªtes FSIQ, c'est-Ã -dire qu'elles portent une en-tÃªte en
 * entrÃ©e.
 *
 * @author csignembe
 */
public interface RequeteWsFsiq {

  /**
   * Retourne le header d'une requÃªte soap d'appel de webservices
   *
   * @return {@link TypeHeaderIn}
   */
  TypeHeaderIn getHeaderIn();

  /**
   * Renseigne le header d'une requÃªte soap d'appel de webservices
   *
   * @param headerIn la valeur du header.
   */
  void setHeaderIn(TypeHeaderIn headerIn);
}
