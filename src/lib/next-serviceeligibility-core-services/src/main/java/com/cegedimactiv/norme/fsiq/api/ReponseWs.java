package com.cegedimactiv.norme.fsiq.api;

import com.cegedimassurances.norme.commun.TypeCodeReponse;

/**
 * Interface pour toutes les rÃ©ponses de Websrevice contenant un code rÃ©ponse.
 *
 * @author csignembe
 */
public interface ReponseWs {

  /**
   * Retourne le status(code rÃ©ponse, libellÃ© descriptif du code rÃ©ponse et le cas Ã©chÃ©ant le
   * dÃ©tail des Ã©rreurs) de la rÃ©ponse Ã  une requÃªte soap.
   *
   * @return {@linkTypeCodeReponse}
   */
  TypeCodeReponse getCodeReponse();

  /**
   * Renseigne le status de la rÃ©ponse Ã  une requÃªte soap.
   *
   * @param code le code rÃ©ponse de la requÃªte
   */
  void setCodeReponse(TypeCodeReponse code);
}
