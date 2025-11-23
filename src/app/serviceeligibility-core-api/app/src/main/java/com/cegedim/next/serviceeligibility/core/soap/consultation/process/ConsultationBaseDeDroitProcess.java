package com.cegedim.next.serviceeligibility.core.soap.consultation.process;

import com.cegedimassurances.norme.base_de_droit.GetInfoBddRequest;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;

@FunctionalInterface
public interface ConsultationBaseDeDroitProcess {

  /**
   * Execute une consultation de la base de droit.
   *
   * @param requete requete envoyé à la base de droit
   * @param response response renvoyé par la base de droit
   * @exception ExceptionMetier exception renvoyé lorsqu'un problème metier est identifié
   * @exception ExceptionTechnique exception renvoyé lorqu'un problème technique est identifié.
   */
  void execute(GetInfoBddRequest requete, GetInfoBddResponse response);
}
